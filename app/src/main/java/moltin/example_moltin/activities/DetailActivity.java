package moltin.example_moltin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applidium.shutterbug.utils.ShutterbugManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.data.ModifierItem;
import moltin.example_moltin.data.TotalCartItem;
import moltin.example_moltin.data.VariationItem;
import moltin.example_moltin.fragments.CartFragment;
import moltin.example_moltin.fragments.DetailFragment;

public class DetailActivity extends SlidingFragmentActivity implements CartFragment.OnFragmentUpdatedListener, CartFragment.OnFragmentChangeListener, CartFragment.OnFragmentInteractionListener, NumberPicker.OnValueChangeListener {

    private SlidingMenu menu;
    private android.app.Fragment mContent;
    private CartFragment menuFragment;

    private ArrayList<CartItem> itemsForCart;
    private TotalCartItem cart;

    private Point screenSize;

    private String itemId;
    private String itemTitle;
    private String itemDescription;
    private String itemPictureUrl;
    private String itemBrand;
    private String itemPrice;
    private String itemModifier;
    private String itemCollection;

    private String[][][] modifiers;

    private String[] urls;

    private Moltin moltin;

    private int quantity=1;

    private int[] modifierIndex=null;
    ArrayList<ModifierItem> modItems=null;
    ArrayList<VariationItem> varItems=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);

        moltin = new Moltin(this);

        menu = getSlidingMenu();
        menu.setShadowWidth(20);
        menu.setBehindWidth(getScreenWidth()-50);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setFadeEnabled(false);
        menu.setBehindScrollScale(0.5f);
        setSlidingActionBarEnabled(true);


        if (savedInstanceState != null)
            mContent = getFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null) {
            mContent = DetailFragment.newInstance();
        }

        setContentView(R.layout.activity_detail);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        itemsForCart=new ArrayList<CartItem>();
        cart=new TotalCartItem(new JSONObject());
        cart.setItems(itemsForCart);

        setBehindContentView(R.layout.cart_content_frame);
        menuFragment = CartFragment.newInstance(cart, getApplicationContext());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.cart_content_frame, menuFragment)
                .commit();

        itemId = getIntent().getExtras().getString("ID");
        itemTitle = getIntent().getExtras().getString("TITLE");
        itemDescription = getIntent().getExtras().getString("DESCRIPTION");
        itemPictureUrl = getIntent().getExtras().getString("PICTURE");
        itemBrand = getIntent().getExtras().getString("BRAND");
        itemPrice = getIntent().getExtras().getString("PRICE");
        itemModifier = getIntent().getExtras().getString("MODIFIER");
        itemCollection = getIntent().getExtras().getString("COLLECTION");

        urls = itemPictureUrl.split("\"");

        try {
            ((TextView)findViewById(R.id.txtDetailTitle)).setText(itemTitle);
            ((TextView)findViewById(R.id.txtDetailDescription)).setText(itemDescription);
            ((TextView)findViewById(R.id.txtDetailBrand)).setText(itemBrand);
            ((TextView)findViewById(R.id.txtDetailPrice)).setText(itemPrice);
            ((TextView)findViewById(R.id.txtDetailCollection)).setText(itemCollection);

            if(urls!=null && urls.length>0 && urls[0].length()>3)
            {
                ShutterbugManager.getSharedImageManager(getApplicationContext()).download(urls[0].replace("|",""), ((ImageView)findViewById(R.id.imgDetailPhoto)));
            } else
                ((ImageView)findViewById(R.id.imgDetailPhoto)).setImageResource(android.R.color.transparent);
        } catch (Exception e) {
            e.printStackTrace();
        }



        try
        {
            if(itemModifier!=null && itemModifier.length()>0 && !itemModifier.equals("null"))
            {
                JSONArray jsonModsArray=null;
                try {
                    jsonModsArray = new JSONArray(itemModifier);
                } catch (JSONException ex) {

                }

                JSONObject jsonMods=null;
                try {
                    jsonMods = new JSONObject(itemModifier);
                } catch (JSONException ex) {

                }
                varItems = new ArrayList<VariationItem>();
                modItems = new ArrayList<ModifierItem>();

                for(int x=0;(jsonMods!=null && x==0) || (jsonModsArray!=null && x<jsonModsArray.length());x++)
                {


                    if(jsonModsArray!=null)
                        jsonMods=jsonModsArray.getJSONObject(x);

                    Iterator i1 = jsonMods.keys();

                    String modifierId, modifierTitle, variationId, variationTitle, variationDiffernce;
                    while (i1.hasNext()) {
                        String key1 = (String) i1.next();
                        if (jsonMods.get(key1) instanceof JSONObject) {
                            varItems = new ArrayList<VariationItem>();

                            modifierId = jsonMods.getJSONObject(key1).getString("id");
                            modifierTitle = jsonMods.getJSONObject(key1).getString("title");

                            JSONObject jsonVars = jsonMods.getJSONObject(key1).getJSONObject("variations");
                            Iterator i2 = jsonVars.keys();

                            while (i2.hasNext()) {
                                String key2 = (String) i2.next();
                                if (jsonVars.get(key2) instanceof JSONObject) {
                                    variationId = jsonVars.getJSONObject(key2).getString("id");
                                    variationTitle = jsonVars.getJSONObject(key2).getString("title");
                                    variationDiffernce = (jsonVars.getJSONObject(key2).has("difference") ? jsonVars.getJSONObject(key2).getString("difference") : "");

                                    varItems.add(new VariationItem(variationId, variationTitle, variationDiffernce));
                                }
                            }

                            modItems.add(new ModifierItem(modifierId, modifierTitle, varItems));
                        }
                    }
                }
                LinearLayout layoutModifiers=(LinearLayout)findViewById(R.id.layModifiers);

                modifierIndex=new int[modItems.size()];

                for(int i=0;i<modItems.size();i++)
                {
                    modifierIndex[i]=0;
                    TextView textModifier=new TextView(this);
                    textModifier.setText("Select " + modItems.get(i).getItemTitle());
                    textModifier.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
                    layoutModifiers.addView(textModifier);

                    ArrayList<String> spinnerArray = new ArrayList<String>();

                    for(int j=0;j<modItems.get(i).getItemVariation().size();j++)
                    {
                        spinnerArray.add(modItems.get(i).getItemVariation().get(j).getItemTitle() + " (" + modItems.get(i).getItemVariation().get(j).getItemDifference() + ")");
                    }

                    final int index=i;

                    Spinner spinner = new Spinner(this);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

                    spinner.setAdapter(spinnerArrayAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            modifierIndex[index]=i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    layoutModifiers.addView(spinner);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            LinearLayout layoutImages=(LinearLayout)findViewById(R.id.layImages);
            LinearLayout layoutScrollImages=(LinearLayout)findViewById(R.id.layScrollImages);

            if(urls.length>1)
            {
                layoutImages.setVisibility(View.VISIBLE);
                if(layoutScrollImages.getChildCount() > 0)
                    layoutScrollImages.removeAllViews();

                for(int i=0;i<urls.length;i++)
                {
                    com.applidium.shutterbug.FetchableImageView img=new com.applidium.shutterbug.FetchableImageView(this);
                    String imageUrlNext=urls[i].replace("|","");
                    if(img!=null && imageUrlNext.length()>3)
                    {
                        ShutterbugManager.getSharedImageManager(getApplicationContext()).download(imageUrlNext, ((ImageView)img));
                    }
                    else
                    img.setImageResource(android.R.color.transparent);

                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                            (int)(80*scale + 0.5f),
                            (int)(80*scale + 0.5f));
                    img.setLayoutParams(params);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((ImageView)findViewById(R.id.imgDetailPhoto)).setImageDrawable(((ImageView) view).getDrawable());
                        }
                    });

                    layoutScrollImages.addView(img);
                }
            }
            else
            {
                layoutImages.setVisibility(View.GONE);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        moltin= new Moltin(this);
        try {
            moltin.cart.inCart(itemId, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
                        JSONObject jsonObject=(JSONObject)msg.obj;
                        try {
                            if(jsonObject.has("status") && jsonObject.getBoolean("status"))
                                ((LinearLayout)findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                            else
                                ((LinearLayout)findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ((TextView)findViewById(R.id.txtActivityTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
            ((TextView)findViewById(R.id.txtDetailTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
            ((TextView)findViewById(R.id.txtDetailPrice)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
            ((TextView)findViewById(R.id.txtDetailCollection)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
            ((TextView)findViewById(R.id.txtDetailBrand)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
            ((TextView)findViewById(R.id.txtDetailDescription)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
            ((Button)findViewById(R.id.btnPutIntoCart)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        return screenSize.x;
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnPlus:
                    ((LinearLayout) findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.update(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(),new String[][]{{"quantity",""+(menuFragment.cart.getItems().get((int)view.getTag()).getItemQuantity()+1)}}, new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnMinus:
                    ((LinearLayout) findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.update(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(),new String[][]{{"quantity",""+(menuFragment.cart.getItems().get((int)view.getTag()).getItemQuantity()-1)}}, new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnDelete:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.remove(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(), new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCheckout:
                    if(menuFragment.cart!=null && menuFragment.cart.getItemTotalNumber()!=null && menuFragment.cart.getItemTotalNumber()>0)
                    {
                        Intent intent = new Intent(this, BillingActivity.class);
                        intent.putExtra("JSON",menuFragment.cart.getItemJson().toString());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.btnMenu:
                    onHomeClicked();
                    break;
                case R.id.btnCart:
                    onHomeClicked();
                    break;
                case R.id.btnPutIntoCart:
                    show();
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

    public void onHomeClicked() {
        toggle();
    }

    public void show()
    {

        final Dialog d = new Dialog(this);
        d.setTitle("Select quantity");
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
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((Button)findViewById(R.id.btnPutIntoCart)).setEnabled(false);
                try {

                    String[][] modsArray=new String[0][0];
                    if(modItems!=null)
                    {
                        modsArray=new String[modItems.size()][2];
                        for(int i=0;i<modItems.size();i++)
                        {
                            modsArray[i][0]=/*"modifier["+*/modItems.get(i).getItemId()/*+"]"*/;
                            modsArray[i][1]=modItems.get(i).getItemVariation().get(modifierIndex[i]).getItemId();
                        }
                    }

                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.insert(itemId, np.getValue(), modsArray, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                            JSONObject jsonObject = (JSONObject) msg.obj;
                            if (msg.what == Constants.RESULT_OK) {

                                Toast.makeText(getApplicationContext(), "Product added to cart.", Toast.LENGTH_LONG).show();
                                try {
                                    if (jsonObject.has("status") && jsonObject.getBoolean("status"))
                                        ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                                    else
                                        ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ((Button) findViewById(R.id.btnPutIntoCart)).setEnabled(true);

                                menuFragment.refresh();
                                toggle();
                                return true;
                            } else {
                                Toast.makeText(getApplicationContext(), "Error while adding to cart. Please try again.", Toast.LENGTH_LONG).show();
                                ((Button) findViewById(R.id.btnPutIntoCart)).setEnabled(true);
                                return false;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        quantity = newVal;
    }

    @Override
    public void onFragmentInteractionForCartItem(CartItem item) {

    }

    @Override
    public void onFragmentChangeForCartItem(TotalCartItem cart) {
        ((TextView)findViewById(R.id.txtTotalPrice)).setText(cart.getItemTotalPrice());
    }

    @Override
    public void onFragmentUpdatedForCartItem() {
        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
    }
}