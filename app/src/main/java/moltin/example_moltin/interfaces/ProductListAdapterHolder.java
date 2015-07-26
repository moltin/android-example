package moltin.example_moltin.interfaces;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applidium.shutterbug.FetchableImageView;
import com.applidium.shutterbug.utils.ShutterbugManager;

import java.util.ArrayList;
import java.util.List;

import moltin.example_moltin.R;
import moltin.example_moltin.data.ProductItem;

public class ProductListAdapterHolder extends RecyclerView.Adapter<ProductListAdapterHolder.ViewHolder> {

    private final FragmentActivity activity;
    private List<ProductItem> items = new ArrayList<ProductItem>();
    private OnItemClickListener itemClickListener;

    public ProductListAdapterHolder(FragmentActivity mActivity, List<ProductItem> items) {
        this.activity = mActivity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.product_list_item, parent, false);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder , int position) {
        holder.title.setText(items.get(position).getItemName());
        holder.price.setText(items.get(position).getItemPrice());

        ((TextView)holder.price).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), activity.getString(R.string.font_regular)));
        ((TextView)holder.title).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), activity.getString(R.string.font_bold)));

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
                //e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, price;
        FetchableImageView image;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTitle);
            price = (TextView) view.findViewById(R.id.txtPrice);
            image = (FetchableImageView) view.findViewById(R.id.imgItem);
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
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }
}
