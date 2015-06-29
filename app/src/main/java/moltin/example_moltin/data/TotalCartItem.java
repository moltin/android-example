package moltin.example_moltin.data;

import org.json.JSONObject;

import java.util.ArrayList;

public class TotalCartItem {

    private String itemTotalPrice;
    private Integer itemTotalNumber;
    private ArrayList<CartItem> items;


    public TotalCartItem()
    {

    }

    public TotalCartItem(JSONObject json)
    {
        try
        {
            if(json.has("total_items") && !json.isNull("total_items"))
                itemTotalNumber=json.getInt("total_items");

            if(json.has("totals") && !json.isNull("totals") && json.getJSONObject("totals").has("pre_discount"))
                itemTotalPrice=json.getJSONObject("totals").getJSONObject("pre_discount").getJSONObject("formatted").getString("with_tax");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public TotalCartItem(String itemTotalPrice,
                         Integer itemTotalNumber)
    {
        this.itemTotalPrice=itemTotalPrice;
        this.itemTotalNumber=itemTotalNumber;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public Integer getItemTotalNumber() {
        return itemTotalNumber;
    }

    public void setItemTotalNumber(Integer itemTotalNumber) {
        this.itemTotalNumber = itemTotalNumber;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}
