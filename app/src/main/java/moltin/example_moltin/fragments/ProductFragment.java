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
import moltin.example_moltin.interfaces.ProductListAdapterHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends android.app.Fragment {
    private FragmentActivity activity;
    public RecyclerView recyclerView;
    public ProductListAdapterHolder adapter;
    private LinearLayoutManager layoutManager;
    private OnProductFragmentInteractionListener mListener;
    private ArrayList<ProductItem> items;
    public View rootView;
    public boolean loading = false;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public static ProductFragment newInstance(ArrayList<ProductItem> posts) {
        ProductFragment fragment = new ProductFragment();
        fragment.setArgs(posts);
        return fragment;
    }

    public void setArgs(ArrayList<ProductItem> posts) {
        this.items = posts;
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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        adapter = new ProductListAdapterHolder(activity, items);

        return rootView;
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildCount();
                totalItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).getItemCount();
                pastVisiblesItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (!loading) {
                    if ( (visibleItemCount+pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        ProductActivity act = ProductActivity.instance;

                        act.getNewPage(items.size());
                    }
                }
            }
        });

        adapter.SetOnItemClickListener(new ProductListAdapterHolder.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                mListener.onFragmentInteractionForProductItem(items.get(position));
            }
        });

    }


    public interface OnProductFragmentInteractionListener {
        void onFragmentInteractionForProductItem(ProductItem itemId);
    }
}
