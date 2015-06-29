package moltin.example_moltin.data;

public class CountryItem implements Comparable<CountryItem> {

    private String itemId;
    private String itemTitle;

    public CountryItem()
    {

    }

    public CountryItem(String itemId,
                       String itemTitle)
    {
        this.itemId=itemId;
        this.itemTitle=itemTitle;
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

    @Override
    public int compareTo(CountryItem country) {
        return this.getItemTitle().compareTo(country.getItemTitle());
    }
}
