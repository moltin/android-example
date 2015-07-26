package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;

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

        ((TextView)findViewById(R.id.txtPaymentCardNumber)).setRawInputType(InputType.TYPE_CLASS_NUMBER);
        ((TextView)findViewById(R.id.txtPaymentCVC)).setRawInputType(InputType.TYPE_CLASS_NUMBER);

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
                        ((TextView)findViewById(R.id.txtPaymentCardNumber)).setError(getString(R.string.required_field_credit_card));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        number=((TextView)findViewById(R.id.txtPaymentCardNumber)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString().trim().equals("") || Integer.parseInt(((TextView) findViewById(R.id.txtPaymentExpirationMonth)).getText().toString().trim())>12)
                    {
                        ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).setError(getString(R.string.required_field_expiration_month));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        expiry_month=((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString().trim().equals("") || Integer.parseInt(((TextView) findViewById(R.id.txtPaymentExpirationYear)).getText().toString().trim())>3000)
                    {
                        ((TextView)findViewById(R.id.txtPaymentExpirationYear)).setError(getString(R.string.required_field_expiration_year));
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        expiry_year=((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentCVC)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentCVC)).setError(getString(R.string.required_field_cvv));
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
                            showAlert(false,getString(R.string.alert_generic_error));
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
                            showAlert(false,getString(R.string.alert_generic_error));
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
                            showAlert(false,getString(R.string.alert_generic_error));
                        }

                        return true;
                    } else {
                        try {

                            JSONObject json = (JSONObject) msg.obj;

                            showAlert(false,json.getString("error"));

                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert(false,getString(R.string.alert_generic_error));
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
                            showAlert(false,getString(R.string.alert_generic_error));
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
                            showAlert(false,getString(R.string.alert_generic_error));
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
                .setTitle((done ?getString(R.string.text_payment):getString(R.string.text_error)))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (done) {
                            eraseCurrentCart();

                            Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
                            intent.putExtra("JSON_CART", json);
                            intent.putExtra("JSON_PAYMENT", jsonPayment.toString());
                            startActivity(intent);
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
