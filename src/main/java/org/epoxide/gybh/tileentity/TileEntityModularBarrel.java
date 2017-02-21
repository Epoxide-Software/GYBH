package org.epoxide.gybh.tileentity;

import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;

import net.darkhax.bookshelf.tileentity.TileEntityBasic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityModularBarrel extends TileEntityBasic {

    private BarrelTier tier;

    private ItemHandlerBarrel inventory;

    private NBTTagCompound tagCache;

    private String tierId;

    private int tierLookupAttempt = 0;

    public TileEntityModularBarrel () {

    }

    public void upgradeBarrel (BarrelTier upgradeTier, IBlockState state) {

        this.tier = upgradeTier;
        this.inventory.setTier(upgradeTier);
        this.getWorld().notifyBlockUpdate(this.pos, state, state, 8);
        this.markDirty();
    }

    public BarrelTier getTier () {

        if (this.tier == null && this.tierLookupAttempt < 5) {

            this.tier = GybhApi.getTier(this.tierId);
            this.tierLookupAttempt++;
        }

        return this.tier;
    }

    public ItemHandlerBarrel getInventory () {

        return this.inventory;
    }

    public void updateTile () {

        final IBlockState state = this.getWorld().getBlockState(this.pos);
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 8);
    }

    public void help () {

    }

    @Override
    public void writeNBT (NBTTagCompound dataTag) {

        final BarrelTier tier = this.getTier();

        if (tier != null && this.inventory != null) {

            dataTag.setString("TierID", tier.identifier.toString());

            if (this.inventory.getContentStack() != null) {

                final NBTTagCompound itemStackTag = new NBTTagCompound();
                this.inventory.getContentStack().writeToNBT(itemStackTag);
                dataTag.setTag("ItemStackData", itemStackTag);

                itemStackTag.setInteger("Stored", this.inventory.getCount());
            }
        }

        else {

            this.tagCache.merge(dataTag);
        }
    }

    @Override
    public void readNBT (NBTTagCompound dataTag) {

        this.tagCache = dataTag;
        this.tierId = dataTag.getString("TierID");
        this.tier = GybhApi.getTier(this.tierId);

        ItemStack contents = null;
        int count = 0;
        int capacity = 0;

        if (dataTag.hasKey("ItemStackData")) {

            final NBTTagCompound itemStackTag = dataTag.getCompoundTag("ItemStackData");
            contents = ItemStack.loadItemStackFromNBT(itemStackTag);
            count = itemStackTag.getInteger("Stored");
        }

        capacity = this.tier != null ? this.tier.getCapacity() : count;

        this.inventory = new ItemHandlerBarrel(this, capacity, count, contents);
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.getInventory();

        return super.getCapability(capability, facing);
    }
}