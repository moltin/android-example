package moltin.example_moltin.data;

public class ShippingItem {

    private String itemSlug;
    private String itemTitle;
    private String itemPrice;
    private String itemTotalPrice;

    public ShippingItem()
    {

    }

    public ShippingItem(String itemSlug,
                        String itemTitle,
                        String itemPrice,
                        String itemTotalPrice)
    {
        this.itemSlug=itemSlug;
        this.itemTitle=itemTitle;
        this.itemPrice=itemPrice;
        this.itemTotalPrice=itemTotalPrice;
    }

    public String getItemSlug() {
        return itemSlug;
    }

    public void setItemSlug(String itemSlug) {
        this.itemSlug = itemSlug;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemPrice() {
        return itemPrice;
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
}
