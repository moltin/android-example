package moltin.example_moltin.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applidium.shutterbug.FetchableImageView;
import com.applidium.shutterbug.utils.ShutterbugManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.data.TotalCartItem;

public class ReceiptActivity extends Activity {

    private Moltin moltin;
    private TotalCartItem totalCartItem;
    private JSONObject paymentJson;
    private JSONObject cartJson;
    private LinearLayout layItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        moltin = new Moltin(this);

        layItems= (LinearLayout)findViewById(R.id.layItems);

        try {
            String cartJsonString=getIntent().getExtras().getString("JSON_CART");

            cartJson = new JSONObject(cartJsonString);
            paymentJson = new JSONObject(getIntent().getExtras().getString("JSON_PAYMENT"));

            TextView txtCourier=(TextView)findViewById(R.id.txtCourier);
            txtCourier.setText("");
            txtCourier.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            TextView txtCost=(TextView)findViewById(R.id.txtCost);
            txtCost.setText("");
            txtCost.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            TextView txtAddress=(TextView)findViewById(R.id.txtAddress);
            txtAddress.setText("");
            txtAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            TextView txtCard=(TextView)findViewById(R.id.txtCard);
            txtCard.setText("");
            txtCard.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            TextView txtSubtotal=(TextView)findViewById(R.id.txtSubtotal);
            txtSubtotal.setText("");
            txtSubtotal.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            TextView txtDelivery=(TextView)findViewById(R.id.txtDelivery);
            txtDelivery.setText("");
            txtDelivery.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            TextView txtTotal=(TextView)findViewById(R.id.txtTotal);
            txtTotal.setText("");
            txtTotal.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

            Double shipping=Double.parseDouble(paymentJson.getJSONObject("result").getJSONObject("order").getString("shipping_price"));
            Double total=Double.parseDouble(paymentJson.getJSONObject("result").getJSONObject("order").getString("total"));

            try
            {
                txtCourier.setText(paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("shipping").getString("value"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                txtCost.setText(paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("shipping").getJSONObject("data").getJSONObject("price").getJSONObject("data").getJSONObject("formatted").getString("with_tax"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                String address_1=(paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").isNull("address_1") ? "" : paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").getString("address_1"));
                String address_2=(paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").isNull("address_2") ? "" : paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").getString("address_2"));
                String postcode=(!paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").isNull("postcode") ? "" : paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").getString("postcode"));
                String city=(paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").isNull("city") ? "" : paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").getString("city"));
                String country=(paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").getJSONObject("country").isNull("value") ? "" : paymentJson.getJSONObject("result").getJSONObject("order").getJSONObject("ship_to").getJSONObject("data").getJSONObject("country").getString("value"));

                txtAddress.setText(address_1
                                + (address_2.length()>0 ? ", " + address_2 : "")
                                + (city.length()>0 ? ", " + city : "")
                                + (postcode.length()>0 ? " " + postcode : "")
                                + (country.length()>0 ? ", " + country : "")
                );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                txtCard.setText(paymentJson.getJSONObject("result").getJSONObject("data").getJSONObject("card").getString("brand") + " " + paymentJson.getJSONObject("result").getJSONObject("data").getJSONObject("card").getString("last4"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                txtSubtotal.setText(cartJson.getJSONObject("currency").getString("format").replace("{price}",String.format("%.2f", total-shipping)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                txtDelivery.setText(cartJson.getJSONObject("currency").getString("format").replace("{price}",String.format("%.2f", shipping)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                txtTotal.setText(cartJson.getJSONObject("currency").getString("format").replace("{price}",String.format("%.2f", total)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            ArrayList<CartItem> items = new ArrayList<CartItem>();
            try {

                Iterator i1 = cartJson.getJSONObject("contents").keys();

                while (i1.hasNext()) {
                    String key1 = (String) i1.next();
                    if (cartJson.getJSONObject("contents").get(key1) instanceof JSONObject) {

                        CartItem itemForArray=new CartItem(cartJson.getJSONObject("contents").getJSONObject(key1));
                        itemForArray.setItemIdentifier(key1);

                        items.add(itemForArray);

                        setItems(layItems, itemForArray.getItemName(), itemForArray.getItemTotalPrice(), itemForArray.getItemQuantity()+"", (itemForArray.getItemPictureUrl().length>0 ? itemForArray.getItemPictureUrl()[0] : ""));
                    }
                }

                totalCartItem.setItems(items);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        changeFonts((RelativeLayout) findViewById(R.id.layMain));
    }

    private void setItems(LinearLayout layItems, String title, String price, String quantity, String imgUrl)
    {
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());

        View myView;

        myView = factory.inflate(R.layout.receipt_list_item, null);

        TextView txtTitle=(TextView) myView.findViewById(R.id.txtTitle);
        TextView txtPrice=(TextView) myView.findViewById(R.id.txtPrice);
        TextView txtQuantity=(TextView) myView.findViewById(R.id.txtQuantity);
        FetchableImageView imgItem=(FetchableImageView) myView.findViewById(R.id.imgItem);

        txtTitle.setText(title);
        txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
        txtPrice.setText(price);
        txtPrice.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
        txtQuantity.setText(quantity);
        txtQuantity.setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));

        ShutterbugManager.getSharedImageManager(ReceiptActivity.this).download(imgUrl, ((ImageView)imgItem));

        layItems.addView(myView);
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnDone:
                    Intent intent = new Intent(getApplicationContext(), CollectionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.btnBack:
                    finish();
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void changeFonts(ViewGroup root) {
        try
        {
            for(int i = 0; i <root.getChildCount(); i++) {
                View v = root.getChildAt(i);
                if(v instanceof Button) {
                    ((Button)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
                } else if(v instanceof TextView) {
                    ((TextView)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
                } else if(v instanceof EditText) {
                    ((EditText)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_regular)));
                } else if(v instanceof ViewGroup) {
                    changeFonts((ViewGroup) v);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
