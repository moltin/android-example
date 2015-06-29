package moltin.example_moltin.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applidium.shutterbug.utils.ShutterbugManager;

import java.util.List;

import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;

public class CartItemArrayAdapter extends ArrayAdapter<CartItem> {

    private Context context;
    private CartItem item;

    public CartItemArrayAdapter(Context context, List<CartItem> items) {
        super(context, R.layout.cart_list_item, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        {
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.cart_list_item, parent, false);

                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.quantity = (TextView) convertView.findViewById(R.id.txtQuantity);
                holder.image = (com.applidium.shutterbug.FetchableImageView) convertView.findViewById(R.id.imgItem);
                holder.btnPlus = (LinearLayout) convertView.findViewById(R.id.btnPlus);
                holder.btnMinus = (LinearLayout) convertView.findViewById(R.id.btnMinus);
                holder.btnDelete = (LinearLayout) convertView.findViewById(R.id.btnDelete);

                convertView.setTag(holder);
            } else {
                try
                {
                    holder = (ViewHolder) convertView.getTag();

                    if (holder == null)
                    {
                        convertView = inflater.inflate(R.layout.cart_list_item, parent, false);

                        holder = new ViewHolder();
                        holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                        holder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                        holder.quantity = (TextView) convertView.findViewById(R.id.txtQuantity);
                        holder.image = (com.applidium.shutterbug.FetchableImageView) convertView.findViewById(R.id.imgItem);
                        holder.btnPlus = (LinearLayout) convertView.findViewById(R.id.btnPlus);
                        holder.btnMinus = (LinearLayout) convertView.findViewById(R.id.btnMinus);
                        holder.btnDelete = (LinearLayout) convertView.findViewById(R.id.btnDelete);

                        convertView.setTag(holder);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return convertView;
                }
            }
            item = getItem(position);


            try {
                holder.title.setText(item.getItemName());
                holder.price.setText(item.getItemPrice());
                holder.quantity.setText("" + item.getItemQuantity());

                holder.btnPlus.setTag(position);
                holder.btnMinus.setTag(position);
                holder.btnDelete.setTag(position);

                if(item.getItemPictureUrl()!=null && item.getItemPictureUrl().length>0);
                {
                    try {
                        String imageUrl= item.getItemPictureUrl()[0];
                        if(imageUrl!=null && imageUrl.length()>3)
                        {
                            ShutterbugManager.getSharedImageManager(context).download(imageUrl, ((ImageView)holder.image));
                        }
                        else holder.image.setImageResource(android.R.color.transparent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView price;
        TextView quantity;
        com.applidium.shutterbug.FetchableImageView image;
        LinearLayout btnPlus;
        LinearLayout btnMinus;
        LinearLayout btnDelete;
    }
}
