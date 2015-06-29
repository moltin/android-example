package moltin.example_moltin.data;

import java.util.ArrayList;

public class ModifierItem {

    private String itemId;
    private String itemTitle;
    private ArrayList<VariationItem> itemVariation;

    public ModifierItem()
    {

    }

    public ModifierItem(String itemId,
                        String itemTitle,
                        ArrayList<VariationItem> itemVariation)
    {
        this.itemId=itemId;
        this.itemTitle=itemTitle;
        this.itemVariation=itemVariation;
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

    public ArrayList<VariationItem> getItemVariation() {
        return itemVariation;
    }

    public void setItemVariation(ArrayList<VariationItem> itemVariation) {
        this.itemVariation = itemVariation;
    }
}
