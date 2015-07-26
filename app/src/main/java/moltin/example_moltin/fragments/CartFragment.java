package moltin.example_moltin.fragments;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.data.TotalCartItem;
import moltin.example_moltin.interfaces.CartItemArrayAdapter;

public class CartFragment extends ListFragment implements AbsListView.OnScrollListener {

    private Context context;
    private ArrayList<CartItem> items;
    public TotalCartItem cart;
    private CartItemArrayAdapter itemAdapter;
    private int lastPosition=0;
    private Moltin moltin;

    private OnFragmentChangeListener changeListener;
    private OnFragmentUpdatedListener updatedListener;

    private boolean loading=false;

    public static CartFragment newInstance(TotalCartItem cart, Context context) {
        CartFragment fragment = new CartFragment();
        fragment.setArgs(cart, context);
        return fragment;
    }

    public CartFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try
        {
            itemAdapter = new CartItemArrayAdapter(getActivity(), items);
            setListAdapter(itemAdapter);
            getListView().setOnScrollListener(this);

            try
            {
                getListView().setSelection(lastPosition);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    final static int SCROLL_STATE_IDLE=0;
    int currentFirstVisibleItem=0;
    int currentVisibleItemCount=0;
    int currentScrollState=SCROLL_STATE_IDLE;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    private void isScrollCompleted() {

        lastPosition = currentFirstVisibleItem;

        if (!loading && this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {

            final ListView lv=getListView();
            if (lv.getLastVisiblePosition() == lv.getAdapter().getCount() -1 &&
                    lv.getChildAt(lv.getChildCount() - 1).getBottom() <= lv.getHeight())
            {
                loading=true;
            }
            else if (lv.getFirstVisiblePosition() == 0 &&
                    lv.getChildAt(0).getTop() >= 0)
            {
                if(lv  != null && lv.getAdapter() != null && lv.getAdapter().getCount()<=1)
                    return;

                loading=true;
            }
        }
    }

    public void setArgs(TotalCartItem cartItem, Context ctx) {
        this.cart=cartItem;
        this.items = cart.getItems();
        this.context=ctx;
        moltin = new Moltin(context);
        refresh();
    }

    public void refresh() {
        try
        {
            moltin.cart.contents(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    updatedListener.onFragmentUpdatedForCartItem();
                    if (msg.what == Constants.RESULT_OK) {

                        items = new ArrayList<CartItem>();
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if (json.has("status") && json.getBoolean("status") && json.has("result") && !json.isNull("result") && json.getJSONObject("result").has("contents") && !json.getJSONObject("result").isNull("contents")) {
                                JSONObject jsonContent;

                                if(json.getJSONObject("result").get("contents") instanceof JSONObject)
                                {
                                    jsonContent=json.getJSONObject("result").getJSONObject("contents");
                                    cart=new TotalCartItem(json.getJSONObject("result"));
                                }
                                else
                                {
                                    jsonContent = new JSONObject();
                                    cart=new TotalCartItem(new JSONObject());
                                }


                                Iterator i1 = jsonContent.keys();

                                while (i1.hasNext()) {
                                    String key1 = (String) i1.next();
                                    if (jsonContent.get(key1) instanceof JSONObject) {

                                        CartItem itemForArray=new CartItem(jsonContent.getJSONObject(key1));
                                        itemForArray.setItemIdentifier(key1);

                                        items.add(itemForArray);
                                    }
                                }

                                cart.setItems(items);

                                itemAdapter = new CartItemArrayAdapter(getActivity(), items);
                                setListAdapter(itemAdapter);

                                try
                                {
                                    getListView().setSelection(lastPosition);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                changeListener.onFragmentChangeForCartItem(cart);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            changeListener = (OnFragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentChangeListener");
        }
        try {
            updatedListener = (OnFragmentUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentUpdatedListener");
        }
    }

    public interface OnFragmentChangeListener {
        void onFragmentChangeForCartItem(TotalCartItem cart);
    }

    public interface OnFragmentUpdatedListener {
        void onFragmentUpdatedForCartItem();
    }
}
