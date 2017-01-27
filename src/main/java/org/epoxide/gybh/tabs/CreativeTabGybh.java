package org.epoxide.gybh.tabs;

import org.epoxide.gybh.api.GybhApi;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabGybh extends CreativeTabs {

    public CreativeTabGybh () {

        super("gybh");
        this.setBackgroundImageName("item_search.png");
    }

    @Override
    public Item getTabIconItem () {

        return Items.BUCKET;
    }

    @Override
    public ItemStack getIconItemStack () {

        return GybhApi.createTieredBarrel(GybhApi.WOOD_OAK);
    }

    @Override
    public boolean hasSearchBar () {

        return true;
    }
}
