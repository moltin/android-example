package moltin.example_moltin.data;

public class VariationItem {

    private String itemId;
    private String itemTitle;
    private String itemDifference;

    public VariationItem()
    {

    }

    public VariationItem(String itemId,
                         String itemTitle,
                         String itemDifference)
    {
        this.itemId=itemId;
        this.itemTitle=itemTitle;
        this.itemDifference=itemDifference;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDifference() {
        return itemDifference;
    }

    public void setItemDifference(String itemDifference) {
        this.itemDifference = itemDifference;
    }
}
