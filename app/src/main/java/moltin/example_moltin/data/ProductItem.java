package moltin.example_moltin.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductItem {

    private String itemId;
    private String[] itemPictureUrl;
    private String itemName;
    private String itemDescription;
    private String itemSlug;
    private String itemBrand;
    private String itemPrice;
    private String itemModifier;
    private String itemCollection;

    public ProductItem()
    {

    }

    public ProductItem(JSONObject json)
    {
        try
        {
            if(json.has("id") && !json.isNull("id"))
                itemId=json.getString("id");

            if(json.has("title") && !json.isNull("title"))
                itemName=json.getString("title");

            if(json.has("description") && !json.isNull("description"))
                itemDescription=json.getString("description");

            if(json.has("slug") && !json.isNull("slug"))
                itemSlug=json.getString("slug");

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

            if(json.has("brand") && !json.isNull("brand") && json.getJSONObject("brand").has("value"))
                itemBrand=json.getJSONObject("brand").getString("value");

            if(json.has("price") && !json.isNull("price") && json.getJSONObject("price").has("data"))
                itemPrice=json.getJSONObject("price").getJSONObject("data").getJSONObject("formatted").getString("with_tax");

            if(json.has("collection") && !json.isNull("collection") && json.getJSONObject("collection").has("value"))
                itemCollection=json.getJSONObject("collection").getString("value");

            if(json.has("modifiers") && !json.isNull("modifiers"))
            {
                if(json.get("modifiers") instanceof JSONObject)
                    itemModifier = json.getJSONObject("modifiers").toString();
                else if(json.get("modifiers") instanceof JSONArray)
                    itemModifier = json.getJSONArray("modifiers").toString();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public ProductItem(String itemId,
                       String[] itemPictureUrl,
                       String itemName,
                       String itemDescription,
                       String itemSlug,
                       String itemBrand,
                       String itemPrice)
    {
        this.itemId=itemId;
        this.itemPictureUrl=itemPictureUrl;
        this.itemName=itemName;
        this.itemDescription=itemDescription;
        this.itemSlug=itemSlug;
        this.itemBrand=itemBrand;
        this.itemPrice=itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String[] getItemPictureUrl() {
        return itemPictureUrl;
    }

    public String getItemPictureUrls() {

        String urls="";
        for(int i=0;itemPictureUrl!=null && i<itemPictureUrl.length;i++)
        {
            if(i>0)
                urls += "\"";
            urls += itemPictureUrl[i];
        }
        return urls;
    }

    public void setItemPictureUrl(String[] itemPictureUrl) {
        this.itemPictureUrl = itemPictureUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getShortItemDescription() {
        if(itemDescription!=null && itemDescription.length()>100) return itemDescription.substring(0,100) + "...";
        else return itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemSlug() {
        return itemSlug;
    }

    public void setItemSlug(String itemSlug) {
        this.itemSlug = itemSlug;
    }

    public String getItemBrand() {
        return (itemBrand==null || itemBrand.equals("null") ? "" : "by: " + itemBrand);
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemPrice() {
        return (itemPrice==null || itemPrice.equals("null") ? "" : itemPrice);
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemModifier() {
        return itemModifier;
    }

    public void setItemModifier(String itemModifier) {
        this.itemModifier = itemModifier;
    }

    public String getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(String itemCollection) {
        this.itemCollection = itemCollection;
    }
}
