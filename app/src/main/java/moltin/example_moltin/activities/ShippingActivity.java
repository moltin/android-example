package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CountryItem;
import moltin.example_moltin.interfaces.CountryListAdapter;

public class ShippingActivity extends Activity {

    private Moltin moltin;

    String email="";

    String first_name="";
    String last_name="";
    String address_1="";
    String address_2="";
    String country="";
    String postcode="";

    String b_first_name="";
    String b_last_name="";
    String b_address_1="";
    String b_address_2="";
    String b_country="";
    String b_postcode="";

    String json;

    AlertDialog dialog;
    ArrayList<CountryItem> listCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        moltin = new Moltin(this);

        json=getIntent().getExtras().getString("JSON");

        email = getIntent().getExtras().getString("EMAIL");

        b_first_name = getIntent().getExtras().getString("B_FIRST_NAME");
        b_last_name = getIntent().getExtras().getString("B_LAST_NAME");
        b_address_1 = getIntent().getExtras().getString("B_ADDRESS_1");
        b_address_2 = getIntent().getExtras().getString("B_ADDRESS_2");
        b_country = getIntent().getExtras().getString("B_COUNTRY");
        b_postcode = getIntent().getExtras().getString("B_POSTCODE");

        getCountryCodes();

        changeFonts((RelativeLayout) findViewById(R.id.layMain));
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnPlaceOrder:

                    boolean placeOrder=true;
                    boolean oneErrorPerTry=true;

                    if( ((TextView)findViewById(R.id.txtShippingFirstName)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingFirstName)).setError(getString(R.string.required_field_first_name));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        first_name=((TextView)findViewById(R.id.txtShippingFirstName)).getText().toString();
                    }
                    if( ((TextView)findViewById(R.id.txtShippingLastName)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingLastName)).setError(getString(R.string.required_field_last_name));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        last_name=((TextView)findViewById(R.id.txtShippingLastName)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingAddress1)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingAddress1)).setError(getString(R.string.required_field_address));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_1=((TextView)findViewById(R.id.txtShippingAddress1)).getText().toString();
                    }

                    if( !((TextView)findViewById(R.id.txtShippingAddress1)).getText().toString().trim().equals(""))
                    {
                        address_2=((TextView)findViewById(R.id.txtShippingAddress2)).getText().toString();
                    }


                    if( ((TextView)findViewById(R.id.txtShippingCity)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingCity)).setError(getString(R.string.required_field_city));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_2 += (address_2.length()>0 ? ", " : "") + ((TextView)findViewById(R.id.txtShippingCity)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingZip)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingZip)).setError(getString(R.string.required_field_zip));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        postcode=((TextView)findViewById(R.id.txtShippingZip)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingState)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingState)).setError(getString(R.string.required_field_state));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_2 += (address_2.length()>0 ? ", " : "") + ((TextView)findViewById(R.id.txtShippingState)).getText().toString();
                    }

                    if(country.equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingCountry)).setError(getString(R.string.required_field_country));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }

                    if(placeOrder)
                    {
                        Intent intent = new Intent(this, ShippingMethodActivity.class);
                        intent.putExtra("EMAIL",email);
                        intent.putExtra("B_FIRST_NAME",b_first_name);
                        intent.putExtra("B_LAST_NAME",b_last_name);
                        intent.putExtra("B_ADDRESS_1",b_address_1);
                        intent.putExtra("B_ADDRESS_2",b_address_2);
                        intent.putExtra("B_COUNTRY",b_country);
                        intent.putExtra("B_POSTCODE",b_postcode);
                        intent.putExtra("S_FIRST_NAME",first_name);
                        intent.putExtra("S_LAST_NAME",last_name);
                        intent.putExtra("S_ADDRESS_1",address_1);
                        intent.putExtra("S_ADDRESS_2",address_2);
                        intent.putExtra("S_COUNTRY",country);
                        intent.putExtra("S_POSTCODE",postcode);
                        intent.putExtra("JSON", json);
                        startActivity(intent);
                    }
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

    private void getCountryCodes() {
        try {

            moltin.address.fields("","",new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    JSONObject json=(JSONObject)msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONObject jsonCountries = json.getJSONObject("result").getJSONObject("country").getJSONObject("available");
                            {
                                Iterator i = jsonCountries.keys();
                                listCountry = new ArrayList<CountryItem>();
                                while (i.hasNext()) {
                                    String key = (String) i.next();
                                    listCountry.add(new CountryItem(key,jsonCountries.getString(key).toString()));
                                }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCountries(View view)
    {
        try
        {
            Collections.sort(listCountry);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Country");
        ListView list=new ListView(this);
        list.setAdapter(new CountryListAdapter(listCountry, this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                country = listCountry.get((int)view.getTag()).getItemId();
                ((TextView)findViewById(R.id.txtShippingCountry)).setText(listCountry.get((int)view.getTag()).getItemTitle());
            }
        });
        builder.setView(list);
        dialog=builder.create();
        dialog.show();
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
