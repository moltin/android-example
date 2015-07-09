package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CountryItem;
import moltin.example_moltin.data.ShippingItem;

public class PaymentActivity extends Activity {

    private Moltin moltin;

    String customer="";
    String email="";

    String s_first_name="";
    String s_last_name="";
    String s_address_1="";
    String s_address_2="";
    String s_country="";
    String s_postcode="";

    String b_first_name="";
    String b_last_name="";
    String b_address_1="";
    String b_address_2="";
    String b_country="";
    String b_postcode="";

    static String gateway="stripe";
    static String method="purchase";
    String shipping="";

    String order="";

    String number="";
    String expiry_month="";
    String expiry_year="";
    String cvv="";

    int month=1;
    int year=2015;

    private JSONObject jsonPayment;
    private String json;

    private ArrayList<ShippingItem> shippingArray;
    private int lastShippingIndex=0;

    AlertDialog dialog;
    ArrayList<CountryItem> listCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        moltin = new Moltin(this);

        json=getIntent().getExtras().getString("JSON_CART");

        shipping = getIntent().getExtras().getString("SHIPPING");
        email = getIntent().getExtras().getString("EMAIL");

        s_first_name = getIntent().getExtras().getString("S_FIRST_NAME");
        s_last_name = getIntent().getExtras().getString("S_LAST_NAME");
        s_address_1 = getIntent().getExtras().getString("S_ADDRESS_1");
        s_address_2 = getIntent().getExtras().getString("S_ADDRESS_2");
        s_country = getIntent().getExtras().getString("S_COUNTRY");
        s_postcode = getIntent().getExtras().getString("S_POSTCODE");

        b_first_name = getIntent().getExtras().getString("B_FIRST_NAME");
        b_last_name = getIntent().getExtras().getString("B_LAST_NAME");
        b_address_1 = getIntent().getExtras().getString("B_ADDRESS_1");
        b_address_2 = getIntent().getExtras().getString("B_ADDRESS_2");
        b_country = getIntent().getExtras().getString("B_COUNTRY");
        b_postcode = getIntent().getExtras().getString("B_POSTCODE");

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



                    if( ((TextView)findViewById(R.id.txtPaymentCardNumber)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentCardNumber)).setError("Credit card number is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        number=((TextView)findViewById(R.id.txtPaymentCardNumber)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).setError("Credit card expiration month is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        expiry_month=((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentExpirationYear)).setError("Credit card expiration year is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        expiry_year=((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentCVC)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentCVC)).setError("Credit card cvc/cvv is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        cvv=((TextView)findViewById(R.id.txtPaymentCVC)).getText().toString();
                    }

                    if(placeOrder)
                    {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                        findCustomer();
                    }
                    break;
                case R.id.txtPaymentExpirationMonth:
                    showMonths();
                    break;
                case R.id.txtPaymentExpirationYear:
                    showYears();
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

    public void showMonths()
    {

        final Dialog d = new Dialog(this);
        d.setTitle("Select month");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        b1.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_violet));
        b1.setTextColor(getResources().getColor(android.R.color.white));
        b1.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        Button b2 = (Button) d.findViewById(R.id.button2);
        b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_violet));
        b2.setTextColor(getResources().getColor(android.R.color.white));
        b2.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        month=1;
        np.setMaxValue(12);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                month=i1;
            }
        });
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).setText(""+month);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString().length()>0)
                    Integer.parseInt(((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString());
                d.dismiss();
            }
        });
        d.show();
    }

    public void showYears()
    {

        final Dialog d = new Dialog(this);
        d.setTitle("Select year");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        b1.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_violet));
        b1.setTextColor(getResources().getColor(android.R.color.white));
        b1.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        Button b2 = (Button) d.findViewById(R.id.button2);
        b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_violet));
        b2.setTextColor(getResources().getColor(android.R.color.white));
        b2.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        Calendar calendar = Calendar.getInstance();
        int yearToday = calendar.get(Calendar.YEAR);
        year=yearToday;

        np.setMaxValue(yearToday + 20);
        np.setMinValue(yearToday);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                year=i1;
            }
        });
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((TextView)findViewById(R.id.txtPaymentExpirationYear)).setText(""+year);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString().length()>0)
                    Integer.parseInt(((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString());
                d.dismiss();
            }
        });
        d.show();
    }

    private void eraseCurrentCart()
    {
        moltin.cart.setIdentifier("");
    }

    private void cartOrder() {
        try {
            JSONObject addressBilling=new JSONObject();
            addressBilling.put("first_name",b_first_name);
            addressBilling.put("last_name",b_last_name);
            addressBilling.put("address_1",b_address_1);
            addressBilling.put("address_2",b_address_2);
            addressBilling.put("country",b_country);
            addressBilling.put("postcode",b_postcode);

            JSONObject addressShipping=new JSONObject();
            addressShipping.put("first_name",s_first_name);
            addressShipping.put("last_name",s_last_name);
            addressShipping.put("address_1",s_address_1);
            addressShipping.put("address_2",s_address_2);
            addressShipping.put("country",s_country);
            addressShipping.put("postcode",s_postcode);

            JSONObject orderData=new JSONObject();
            orderData.put("customer", customer);
            orderData.put("shipping", shipping);
            orderData.put("gateway", gateway);
            orderData.put("bill_to", addressBilling);
            orderData.put("ship_to", addressShipping);

            moltin.cart.order(orderData, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    JSONObject json = (JSONObject) msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            if(json.getBoolean("status"))
                            {
                                order = json.getJSONObject("result").getString("id");
                                payment();
                            }
                            else
                            {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                String errors="";

                                Iterator i = json.getJSONObject("errors").keys();

                                while (i.hasNext()) {
                                    String key = (String) i.next();

                                    JSONArray arrayError=json.getJSONObject("errors").getJSONArray(key);

                                    for(int j=0;j<arrayError.length();j++)
                                    {
                                        if(errors.length()!=0)
                                            errors+="\n";
                                        errors +=arrayError.getString(j);
                                    }
                                }

                                showAlert(false, errors);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,"We are experiencing some problems, please try later!");
                        }

                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                        try {
                            String errors="";

                            Iterator i = json.getJSONObject("errors").keys();

                            while (i.hasNext()) {
                                String key = (String) i.next();

                                JSONArray arrayError=json.getJSONObject("errors").getJSONArray(key);

                                for(int j=0;j<arrayError.length();j++)
                                {
                                    if(errors.length()!=0)
                                        errors+="\n";
                                    errors +=arrayError.getString(j);
                                }
                            }

                            showAlert(false,errors);

                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,"We are experiencing some problems, please try later!");
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payment() {
        try {
            JSONObject jsonCard=new JSONObject();
            jsonCard.put("number", number);
            jsonCard.put("expiry_month", expiry_month);
            jsonCard.put("expiry_year", expiry_year);
            jsonCard.put("cvv", cvv);

            JSONObject jsonData=new JSONObject();
            jsonData.put("data", jsonCard);


            moltin.checkout.payment(method, order, jsonData, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                    if (msg.what == Constants.RESULT_OK) {
                        try {

                            jsonPayment = (JSONObject) msg.obj;
                            if(jsonPayment.getBoolean("status"))
                            {
                                showAlert(true,jsonPayment.getJSONObject("result").getString("message"));
                            }
                            else
                            {
                                showAlert(false,jsonPayment.getString("error"));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,"We are experiencing some problems, please try later!");
                        }

                        return true;
                    } else {
                        try {

                            JSONObject json = (JSONObject) msg.obj;

                            showAlert(false,json.getString("error"));

                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,"We are experiencing some problems, please try later!");
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCustomer() {
        try {
            moltin.customer.create(new String[][]{
                    {"first_name",s_first_name},
                    {"last_name",s_last_name},
                    {"email",email}
            }, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if(json.getBoolean("status"))
                            {
                                customer=json.getJSONObject("result").getString("id");
                                cartOrder();
                            }
                            else
                            {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                String errors=json.getJSONArray("errors").getString(0);

                                showAlert(false,errors);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,"We are experiencing some problems, please try later!");
                        }

                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                        try {
                            JSONObject json = (JSONObject) msg.obj;

                            String errors=json.getJSONArray("errors").getString(0);

                            showAlert(false,errors);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,"We are experiencing some problems, please try later!");
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findCustomer() {
        try {
            moltin.customer.find(new String[][]{
                    {"email", email}
            }, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if(json.getJSONArray("result").length()==0)
                            {
                                createCustomer();
                            }
                            else
                            {
                                customer=json.getJSONArray("result").getJSONObject(0).getString("id");
                                cartOrder();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                        try {
                            JSONObject json = (JSONObject) msg.obj;

                            customer=json.getJSONArray("result").getJSONObject(0).getString("id");
                            cartOrder();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(final boolean done,String message)
    {
        new AlertDialog.Builder(this)
                .setTitle((done ?"Payment":"Error"))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (done) {
                            eraseCurrentCart();

                            Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
                            intent.putExtra("JSON_CART", json);
                            intent.putExtra("JSON_PAYMENT", jsonPayment.toString());
                            startActivity(intent);
                            /*Intent intent = new Intent(getApplicationContext(), CollectionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                        }
                    }
                })
                .setIcon((done ? android.R.drawable.ic_dialog_info : android.R.drawable.ic_dialog_alert))
                .show();
    }

    protected void changeFonts(ViewGroup root) {
        try
        {
            for(int i = 0; i <root.getChildCount(); i++) {
                View v = root.getChildAt(i);
                if(v instanceof Button) {
                    ((Button)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
                } else if(v instanceof TextView) {
                    ((TextView)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
                } else if(v instanceof EditText) {
                    ((EditText)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
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
