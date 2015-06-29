package moltin.example_moltin.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class CartItem {

    private String itemIdentifier;
    private String itemId;
    private String itemName;
    private String itemSlug;
    private String itemPrice;
    private String itemTotalPrice;
    private Integer itemQuantity;
    private String[] itemPictureUrl;

    public CartItem()
    {

    }

    public CartItem(JSONObject json)
    {
        try
        {
            if(json.has("id") && !json.isNull("id"))
                itemId=json.getString("id");

            if(json.has("title") && !json.isNull("title"))
                itemName=json.getString("title");

            if(json.has("slug") && !json.isNull("slug"))
                itemSlug=json.getString("slug");

            if(json.has("pricing") && !json.isNull("pricing") && json.getJSONObject("pricing").has("formatted"))
                itemPrice=json.getJSONObject("pricing").getJSONObject("formatted").getString("with_tax");

            if(json.has("totals") && !json.isNull("totals") && json.getJSONObject("totals").has("pre_discount"))
                itemTotalPrice=json.getJSONObject("totals").getJSONObject("pre_discount").getJSONObject("formatted").getString("with_tax");

            if(json.has("quantity") && !json.isNull("quantity"))
                itemQuantity=json.getInt("quantity");

            if(json.has("images") && !json.isNull("images") && json.get("images") instanceof JSONArray)
            {
                if(json.getJSONArray("images").length()>0 && json.getJSONArray("images").getJSONObject(0).has("url") && json.getJSONArray("images").getJSONObject(0).getJSONObject("url").has("http"))
                {
                    itemPictureUrl = new String[json.getJSONArray("images").length()];
                    for(int i=0;i<json.getJSONArray("images").length();i++)
                    {
                        itemPictureUrl[i]=json.getJSONArray("images").getJSONObject(i).getJSONObject("url").getString("http");
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public CartItem(String itemId,
                    String itemName,
                    String itemSlug,
                    String itemPrice,
                    String itemTotalPrice,
                    Integer itemQuantity)
    {
        this.itemId=itemId;
        this.itemName=itemName;
        this.itemSlug=itemSlug;
        this.itemPrice=itemPrice;
        this.itemTotalPrice=itemTotalPrice;
        this.itemQuantity=itemQuantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSlug() {
        return itemSlug;
    }

    public void setItemSlug(String itemSlug) {
        this.itemSlug = itemSlug;
    }

    public String getItemPrice() {
        return (itemPrice==null || itemPrice.equals("null") ? "" : itemPrice);
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String[] getItemPictureUrl() {
        return itemPictureUrl;
    }

    public void setItemPictureUrl(String[] itemPictureUrl) {
        this.itemPictureUrl = itemPictureUrl;
    }
}
