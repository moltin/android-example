package moltin.example_moltin.fragments;

import android.app.Activity;
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
import moltin.example_moltin.activities.CollectionActivity;
import moltin.example_moltin.data.CollectionItem;
import moltin.example_moltin.interfaces.CollectionListAdapterHolder;
import moltin.example_moltin.interfaces.CustomRecyclerView;

public class CollectionFragment extends android.app.Fragment {
    FragmentActivity activity;
    CustomRecyclerView customRecyclerView;
    CollectionListAdapterHolder adapter;
    LinearLayoutManager layoutManager;
    private OnCollectionFragmentInteractionListener mCollectionFragmentInteractionListener;
    private OnCollectionFragmentPictureDownloadListener mCollectionFragmentPictureDownloadListener;

    private ArrayList<CollectionItem> items;
    private int width;
    public View rootView;

    public static CollectionFragment newInstance(ArrayList<CollectionItem> posts, int width) {
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArgs(posts, width);
        return fragment;
    }

    public void setArgs(ArrayList<CollectionItem> posts, int width) {
        this.items = posts;
        this.width = width;
    }

    public CollectionFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
        setRetainInstance(true);

        try {
            mCollectionFragmentInteractionListener = (OnCollectionFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {
            mCollectionFragmentPictureDownloadListener = (OnCollectionFragmentPictureDownloadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPicturesDownloadListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);

        customRecyclerView = (CustomRecyclerView) rootView.findViewById(R.id.recycler_view);

        adapter = new CollectionListAdapterHolder(activity, items, width);

        customRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                try {

                    CollectionActivity act = CollectionActivity.instance;
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

        adapter.SetOnItemClickListener(new CollectionListAdapterHolder.OnItemClickListener() {

            @Override
            public void onItemClick(View v , int position) {
                mCollectionFragmentInteractionListener.onFragmentInteractionForCollectionItem(items.get(position).getItemId());
            }
        });

        adapter.SetOnPicturesDownloadListener(new CollectionListAdapterHolder.OnPicturesDownloadListener() {
            @Override
            public void onPictureDownloadListener() {
                mCollectionFragmentPictureDownloadListener.onCollectionFragmentPictureDownloadListener();
            }
        });
    }


    public interface OnCollectionFragmentInteractionListener {
        public void onFragmentInteractionForCollectionItem(String itemId);
    }

    public interface OnCollectionFragmentPictureDownloadListener {
        public void onCollectionFragmentPictureDownloadListener();
    }
}