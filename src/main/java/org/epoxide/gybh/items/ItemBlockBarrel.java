package org.epoxide.gybh.items;

import java.util.List;

import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;
import org.epoxide.gybh.libs.Constants;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBarrel extends ItemBlock {

    public ItemBlockBarrel (Block block) {

        super(block);
        this.setMaxStackSize(1);
        this.setRegistryName(new ResourceLocation(Constants.MODID, "modular_barrel"));
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, EntityPlayer player, List<String> info, boolean advanced) {

        BarrelTier tier = null;
        ItemStack itemStack = null;
        int storage = 0;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("TileData")) {
            final NBTTagCompound tag = stack.getTagCompound().getCompoundTag("TileData");
            tier = GybhApi.getTier(tag.getString("TierID"));
            itemStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("ItemStackData"));
            storage = tag.getCompoundTag("ItemStackData").getInteger("Stored");
        }

        GybhApi.createTierTooltip(tier, itemStack, storage, info);
    }
}