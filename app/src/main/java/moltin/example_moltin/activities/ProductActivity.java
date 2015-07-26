package moltin.example_moltin.activities;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.data.ProductItem;
import moltin.example_moltin.data.TotalCartItem;
import moltin.example_moltin.fragments.CartFragment;
import moltin.example_moltin.fragments.ProductFragment;


public class ProductActivity extends SlidingFragmentActivity implements CartFragment.OnFragmentUpdatedListener, CartFragment.OnFragmentChangeListener, ProductFragment.OnProductFragmentInteractionListener {
    private Moltin moltin;
    private ArrayList<ProductItem> items;
    private ArrayList<CartItem> itemsForCart;
    private TotalCartItem cart;
    public static ProductActivity instance = null;

    private SlidingMenu menu;
    private android.app.Fragment mContent;
    private CartFragment menuFragment;

    private Point screenSize;

    private String itemId="";
    private String collection="";

    private int currentOffset=0;
    private int limit=20;
    private boolean endOfList=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        itemId = getIntent().getExtras().getString("ID");
        collection = getIntent().getExtras().getString("COLLECTION");

        moltin = new Moltin(this);

        menu = getSlidingMenu();
        menu.setShadowWidth(20);
        menu.setBehindWidth(getListviewWidth()-50);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setFadeEnabled(false);
        menu.setBehindScrollScale(0.5f);
        setSlidingActionBarEnabled(true);

        currentOffset=0;
        endOfList=false;

        items=new ArrayList<ProductItem>();

        if (savedInstanceState != null)
            mContent = getFragmentManager().getFragment(savedInstanceState, "mContentProduct");
        if (mContent == null) {
            mContent = ProductFragment.newInstance(items);
        }

        setContentView(R.layout.activity_product);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        itemsForCart=new ArrayList<CartItem>();
        cart=new TotalCartItem(new JSONObject());
        cart.setItems(itemsForCart);

        setBehindContentView(R.layout.cart_content_frame);
        menuFragment = CartFragment.newInstance(cart, getApplicationContext());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.cart_content_frame, menuFragment)
                .commit();

        ((TextView)findViewById(R.id.txtActivityTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
        ((TextView)findViewById(R.id.txtActivityTitleCart)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
        ((TextView)findViewById(R.id.txtActivityTitle)).setText(collection.toUpperCase());
        try {
            getProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getListviewWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        return screenSize.x;
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnPlus:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.update(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(),new String[][]{{"quantity",""+(menuFragment.cart.getItems().get((int)view.getTag()).getItemQuantity()+1)}}, new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnMinus:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.update(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(),new String[][]{{"quantity",""+(menuFragment.cart.getItems().get((int)view.getTag()).getItemQuantity()-1)}}, new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnDelete:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.remove(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(), new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCheckout:
                    if(menuFragment.cart!=null && menuFragment.cart.getItemTotalNumber()!=null && menuFragment.cart.getItemTotalNumber()>0)
                    {
                        Intent intent = new Intent(this, BillingActivity.class);
                        intent.putExtra("JSON",menuFragment.cart.getItemJson().toString());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.alert_cart_is_empty), Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.btnMenu:
                    finish();
                    break;
                case R.id.btnCart:
                    onHomeClicked();
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onHomeClicked() {
        toggle();
    }

    private void getProducts() throws Exception {

        if(endOfList)
            return;

        ((LinearLayout)findViewById(R.id.layMainLoading)).setVisibility(View.VISIBLE);
        moltin.product.listing(new String[][]{{"collection", itemId},{"limit",Integer.toString(limit)},{"offset",Integer.toString(currentOffset)}}, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ((LinearLayout)findViewById(R.id.layMainLoading)).setVisibility(View.GONE);
                if (msg.what == Constants.RESULT_OK) {

                    try {
                        JSONObject json = (JSONObject) msg.obj;
                        if (json.has("status") && json.getBoolean("status") && json.has("result") && json.getJSONArray("result").length() > 0) {
                            for (int i = 0; i < json.getJSONArray("result").length(); i++) {
                                items.add(new ProductItem(json.getJSONArray("result").getJSONObject(i)));
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), getString(R.string.alert_no_products_available), Toast.LENGTH_LONG).show();
                        }

                        if(items.size()==json.getJSONObject("pagination").getInt("total"))
                            endOfList=true;
                        else
                            endOfList=false;

                        ((ProductFragment) mContent).loading=false;
                        ((ProductFragment) mContent).adapter.notifyDataSetChanged();
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

    public void getNewPage(int currentNumber)
    {
        currentOffset=currentNumber;
        try {
            getProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteractionForProductItem(ProductItem item) {
        try
        {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("ID",item.getItemId());
            intent.putExtra("TITLE",item.getItemName());
            intent.putExtra("DESCRIPTION",item.getItemDescription());
            intent.putExtra("PICTURE",item.getItemPictureUrls());
            intent.putExtra("BRAND",item.getItemBrand());
            intent.putExtra("PRICE",item.getItemPrice());
            intent.putExtra("MODIFIER",item.getItemModifier());
            intent.putExtra("COLLECTION",item.getItemCollection());
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try
        {
            instance=null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        try
        {
            menuFragment.refresh();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onFragmentChangeForCartItem(TotalCartItem cart) {
        ((TextView)findViewById(R.id.txtTotalPrice)).setText(cart.getItemTotalPrice());
    }

    @Override
    public void onFragmentUpdatedForCartItem() {
        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
    }
}