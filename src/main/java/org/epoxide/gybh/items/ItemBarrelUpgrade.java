package org.epoxide.gybh.items;

import java.util.List;

import org.epoxide.gybh.Gybh;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;
import org.epoxide.gybh.libs.Constants;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBarrelUpgrade extends Item {

    public ItemBarrelUpgrade () {

        this.setMaxStackSize(16);
        this.setRegistryName(new ResourceLocation(Constants.MODID, "barrel_upgrade"));
        this.setCreativeTab(Gybh.tabGybh);
        this.setUnlocalizedName("gybh.upgrade");
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, EntityPlayer player, List<String> info, boolean advanced) {

        BarrelTier tier = null;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("TierID")) {
            tier = GybhApi.getTier(stack.getTagCompound().getString("TierID"));
        }

        GybhApi.createTierTooltip(tier, null, 0, info);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems (Item item, CreativeTabs tab, List<ItemStack> itemList) {

        for (final BarrelTier tier : GybhApi.REGISTRY.values()) {
            itemList.add(GybhApi.createTierUpgrade(tier));
        }
    }
}