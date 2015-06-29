package moltin.example_moltin.interfaces;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applidium.shutterbug.utils.ShutterbugManager;

import java.util.ArrayList;
import java.util.List;

import moltin.example_moltin.R;
import moltin.example_moltin.data.CollectionItem;

public class CollectionListAdapterHolder extends CustomRecyclerView.Adapter<CollectionListAdapterHolder.ViewHolder> {

    private final FragmentActivity activity;
    private List<CollectionItem> items = new ArrayList<CollectionItem>();
    private int width;
    private OnItemClickListener itemClickListener;

    public CollectionListAdapterHolder(FragmentActivity mActivity, List<CollectionItem> items, int width) {
        this.activity = mActivity;
        this.items = items;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.collection_list_item, parent, false);

        CustomRecyclerView.LayoutParams params = (CustomRecyclerView.LayoutParams)sView.getLayoutParams();
        params.width = width;
        sView.setLayoutParams(params);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
        holder.title.setText(items.get(position).getItemName());
        holder.description.setText(items.get(position).getShortItemDescription());
        holder.button.setTag(items.get(position).getItemId());

        ((Button)holder.button).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((TextView)holder.description).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
        ((TextView)holder.title).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));

        holder.image.setImageResource(android.R.color.transparent);
        if(items.get(position).getItemPictureUrl()!=null && items.get(position).getItemPictureUrl().length>0);
        {
            try {
                String imageUrl=items.get(position).getItemPictureUrl()[0];
                if(imageUrl!=null && imageUrl.length()>3)
                {
                    ShutterbugManager.getSharedImageManager(activity.getApplicationContext()).download(imageUrl, ((ImageView)holder.image));
                }
                else holder.image.setImageResource(android.R.color.transparent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends CustomRecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, description;
        Button button;
        com.applidium.shutterbug.FetchableImageView image;
        LinearLayout layoutImages;
        LinearLayout layoutScrollImages;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTitle);
            description = (TextView) view.findViewById(R.id.txtDescription);
            button = (Button) view.findViewById(R.id.btnInItem);
            image = (com.applidium.shutterbug.FetchableImageView) view.findViewById(R.id.imgItem);
            layoutImages = (LinearLayout) view.findViewById(R.id.layImages);
            layoutScrollImages = (LinearLayout) view.findViewById(R.id.layScrollImages);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }
}
