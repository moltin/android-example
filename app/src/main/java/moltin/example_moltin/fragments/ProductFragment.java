package moltin.example_moltin.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import moltin.example_moltin.R;
import moltin.example_moltin.activities.ProductActivity;
import moltin.example_moltin.data.ProductItem;
import moltin.example_moltin.interfaces.CustomRecyclerView;
import moltin.example_moltin.interfaces.ProductListAdapterHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends android.app.Fragment {
    FragmentActivity activity;
    CustomRecyclerView customRecyclerView;
    ProductListAdapterHolder adapter;
    LinearLayoutManager layoutManager;
    private OnProductFragmentInteractionListener mListener;

    private ArrayList<ProductItem> items;
    private int width;
    public View rootView;

    public static ProductFragment newInstance(ArrayList<ProductItem> posts, int width) {
        ProductFragment fragment = new ProductFragment();
        fragment.setArgs(posts, width);
        return fragment;
    }

    public void setArgs(ArrayList<ProductItem> posts, int width) {
        this.items = posts;
        this.width = width;
    }

    public ProductFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
        setRetainInstance(true);

        try {
            mListener = (OnProductFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product, container, false);

        customRecyclerView = (CustomRecyclerView) rootView.findViewById(R.id.recycler_view);

        adapter = new ProductListAdapterHolder(activity, items, width);

        customRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                try {

                    ProductActivity act = ProductActivity.instance;
                    if (act != null)
                        act.setPosition(layoutManager.findFirstVisibleItemPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        customRecyclerView.setWidth(width);
        customRecyclerView.setAdapter(adapter);
        customRecyclerView.setHasFixedSize(true);
        customRecyclerView.setLayoutManager(layoutManager);
        customRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.SetOnItemClickListener(new ProductListAdapterHolder.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                mListener.onFragmentInteractionForProductItem(items.get(position));
            }
        });

    }


    public interface OnProductFragmentInteractionListener {
        public void onFragmentInteractionForProductItem(ProductItem itemId);
    }
}
