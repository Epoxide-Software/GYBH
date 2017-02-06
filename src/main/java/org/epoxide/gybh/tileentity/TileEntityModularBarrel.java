package org.epoxide.gybh.tileentity;

import net.minecraft.item.ItemBlock;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;

import com.google.common.collect.ImmutableMap;
import net.darkhax.bookshelf.client.model.ITileEntityRender;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.darkhax.bookshelf.tileentity.TileEntityBasic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityModularBarrel extends TileEntityBasic implements ITileEntityRender {

    public BarrelTier tier;
    public int stored;
    public int capacity;
    public ItemStack itemStack;

    public TileEntityModularBarrel() {

        this.stored = 0;
        this.capacity = 0;
    }

    public void upgradeBarrel(BarrelTier upgradeTier, IBlockState state) {

        this.tier = upgradeTier;
        this.capacity = upgradeTier.getCapacity();
        this.getWorld().notifyBlockUpdate(this.pos, state, state, 8);
        this.markDirty();
    }

    @Override
    public void writeNBT(NBTTagCompound dataTag) {

        if (this.tier != null) {

            dataTag.setString("TierID", this.tier.identifier.toString());

            if (this.itemStack != null) {

                final NBTTagCompound itemStackTag = new NBTTagCompound();

                itemStackTag.setInteger("Stored", stored);
                this.itemStack.writeToNBT(itemStackTag);

                dataTag.setTag("ItemStackData", itemStackTag);
            }
        }

    }

    @Override
    public void readNBT(NBTTagCompound dataTag) {

        this.tier = GybhApi.getTier(dataTag.getString("TierID"));

        if (this.tier != null) {
            this.capacity = tier.getCapacity();

            if (dataTag.hasKey("ItemStackData")) {

                final NBTTagCompound itemStackTag = dataTag.getCompoundTag("ItemStackData");
                this.itemStack = ItemStack.loadItemStackFromNBT(itemStackTag);
                this.stored = itemStackTag.getInteger("Stored");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ImmutableMap<String, String> getRenderStates() {

        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (this.tier != null && this.tier.renderState != null)
            builder.put("frame", RenderUtils.getSprite(this.tier.renderState).getIconName());
        if (itemStack != null) {
            Item i = itemStack.getItem();
            if (i instanceof ItemBlock)
                builder.put("background", RenderUtils.getSprite(Block.getBlockFromItem(i).getStateFromMeta(itemStack.getItemDamage())).getIconName());
            else
                builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
        } else {
            builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
        }
        return builder.build();
    }
}