package org.epoxide.gybh.tileentity;

import org.epoxide.gybh.api.BarrelTier;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerBarrel implements IItemHandler {

    private final TileEntityModularBarrel tile;

    private int count;

    private int capacity;

    private ItemStack contents;

    public ItemHandlerBarrel (TileEntityModularBarrel tile, int capacity, int count, ItemStack contents) {

        this.tile = tile;
        this.count = count;
        this.capacity = capacity;
        this.contents = contents;
    }

    public void setTier (BarrelTier tier) {

        this.capacity = tier.getCapacity();
    }

    public ItemStack getContentStack () {

        return this.contents;
    }

    public int getCount () {

        return this.count;
    }

    public int getCapacity () {

        return this.capacity;
    }

    public void setCount (int count) {

        this.count = count;

        if (this.count == 0) {

            this.contents = null;
        }
    }

    @Override
    public int getSlots () {

        return this.capacity / (this.contents != null ? this.contents.getMaxStackSize() : 64);
    }

    @Override
    public ItemStack getStackInSlot (int slot) {

        if (this.contents != null) {

            final int amountInSlot = slot < this.count / this.contents.getMaxStackSize() ? this.contents.getMaxStackSize() : slot == this.count / this.contents.getMaxStackSize() ? this.count % this.contents.getMaxStackSize() : 0;
            final ItemStack stack = new ItemStack(this.contents.getItem(), amountInSlot, this.contents.getItemDamage());
            stack.setTagCompound(this.contents.getTagCompound());
            return stack;
        }

        return null;
    }

    @Override
    public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {

        if (this.count == this.capacity)
            return stack;

        // Barrel is empty, add item for first time.
        if (this.count == 0) {

            if (!simulate) {

                this.contents = stack;
                this.count = stack.stackSize;
                this.tile.updateTile();
            }

            return null;
        }

        // Barrel wasn't empty, check if we can add item.
        if (ItemStackUtils.areStacksEqual(this.contents, stack, true)) {

            final int remaining = this.addItems(stack.stackSize, simulate);

            if (remaining <= this.capacity)
                return null;

            else if (remaining > this.capacity) {

                stack.stackSize = remaining;
                return stack;
            }
        }

        return stack;
    }

    @Override
    public ItemStack extractItem (int slot, int amount, boolean simulate) {

        if (this.count == 0) {

            this.tile.updateTile();
            return null;
        }

        final int amountToGive = this.count >= amount ? amount : this.count;

        final ItemStack stack = new ItemStack(this.contents.getItem(), amountToGive, this.contents.getItemDamage());
        stack.setTagCompound(this.contents.getTagCompound());

        if (!simulate) {

            this.count -= amountToGive;

            if (this.count <= 0) {
                this.contents = null;
            }
        }

        this.tile.updateTile();
        return stack;
    }

    /**
     * Adds to the amount of items stored by the barrel. Prevents overflow issues.
     *
     * @param amount The amount of items to add to the tank.
     * @param simulate Whether or not the values should actually be changed.
     * @return The amount of items which were NOT consumed by the barrel.
     */
    public int addItems (int amount, boolean simulate) {

        final int remainingSpace = this.capacity - (this.count + amount);

        if (remainingSpace > 0) {

            if (!simulate) {
                this.count += amount;
                this.tile.updateTile();
            }

            return 0;
        }

        else {

            if (!simulate) {
                this.count = this.capacity;
                this.tile.updateTile();
            }

            return -remainingSpace;
        }
    }
}
