package org.epoxide.gybh.tileentity;

import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;

import net.darkhax.bookshelf.tileentity.TileEntityBasic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityModularBarrel extends TileEntityBasic {

    public BarrelTier tier;

    public int stored;

    public int capacity;

    public ItemStack itemStack;

    public TileEntityModularBarrel () {

        this.stored = 0;
        this.capacity = 0;
    }

    public void upgradeBarrel (BarrelTier upgradeTier, IBlockState state) {

        this.tier = upgradeTier;
        this.capacity = upgradeTier.getCapacity();
        this.getWorld().notifyBlockUpdate(this.pos, state, state, 8);
        this.markDirty();
    }

    @Override
    public void writeNBT (NBTTagCompound dataTag) {

        if (this.tier != null) {

            dataTag.setString("TierID", this.tier.identifier.toString());

            if (this.itemStack != null) {

                final NBTTagCompound itemStackTag = new NBTTagCompound();

                itemStackTag.setInteger("Stored", this.stored);
                this.itemStack.writeToNBT(itemStackTag);

                dataTag.setTag("ItemStackData", itemStackTag);
            }
        }

    }

    @Override
    public void readNBT (NBTTagCompound dataTag) {

        this.tier = GybhApi.getTier(dataTag.getString("TierID"));

        if (this.tier != null) {
            this.capacity = this.tier.getCapacity();

            if (dataTag.hasKey("ItemStackData")) {

                final NBTTagCompound itemStackTag = dataTag.getCompoundTag("ItemStackData");
                this.itemStack = ItemStack.loadItemStackFromNBT(itemStackTag);
                this.stored = itemStackTag.getInteger("Stored");
            }
        }
    }
}