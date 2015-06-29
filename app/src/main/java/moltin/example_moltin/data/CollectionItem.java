package moltin.example_moltin.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class CollectionItem {

    private String itemId;
    private String[] itemPictureUrl;
    private String itemName;
    private String itemDescription;
    private String itemSlug;

    public CollectionItem()
    {

    }

    public CollectionItem(JSONObject json)
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public CollectionItem(String itemId,
                          String[] itemPictureUrl,
                          String itemName,
                          String itemDescription,
                          String itemSlug)
    {
        this.itemId=itemId;
        this.itemPictureUrl=itemPictureUrl;
        this.itemName=itemName;
        this.itemDescription=itemDescription;
        this.itemSlug=itemSlug;
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
}
