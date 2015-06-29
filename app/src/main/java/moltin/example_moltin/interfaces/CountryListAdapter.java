package moltin.example_moltin.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import moltin.example_moltin.R;
import moltin.example_moltin.data.CountryItem;

public class CountryListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflator;
    private ArrayList<CountryItem> list;
    public CountryListAdapter(ArrayList<CountryItem> list, Context context) {
        this.context =context;
        this.list = list;
        this.inflator= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MainListHolder mHolder;

        View v = convertView;
            mHolder = new MainListHolder();

            v = inflator.inflate(R.layout.country_item, null);
            mHolder.country=  (TextView) v.findViewById(R.id.txtCountry);
            mHolder.country.setText(list.get(position).getItemTitle());

            v.setTag(position);
        return v;
    }
    class MainListHolder
    {
        private TextView country;
    }

}
