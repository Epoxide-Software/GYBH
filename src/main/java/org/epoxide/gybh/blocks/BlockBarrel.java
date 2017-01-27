package org.epoxide.gybh.blocks;

import java.util.List;
import java.util.Random;

import org.epoxide.gybh.Gybh;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;
import org.epoxide.gybh.items.ItemBarrelUpgrade;
import org.epoxide.gybh.libs.Constants;
import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import net.darkhax.bookshelf.lib.BlockStates;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBarrel extends BlockContainer {

    public BlockBarrel () {

        super(Material.GLASS);
        this.setUnlocalizedName("gybh.barrel");
        this.setRegistryName(new ResourceLocation(Constants.MODID, "modular_barrel"));
        this.setCreativeTab(Gybh.tabGybh);
        this.setHardness(0.3F);
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(((IExtendedBlockState) this.blockState.getBaseState()).withProperty(BlockStates.HELD_STATE, null).withProperty(BlockStates.BLOCK_ACCESS, null).withProperty(BlockStates.BLOCKPOS, null));
    }

    @Override
    public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        final TileEntityModularBarrel barrel = (TileEntityModularBarrel) worldIn.getTileEntity(pos);

        // Handle bad tank
        if (barrel == null || barrel.tier == null)
            return heldItem != null && !(heldItem.getItem() instanceof ItemBlock);

        // Handle upgrade
        if (heldItem != null && heldItem.getItem() instanceof ItemBarrelUpgrade) {

            final BarrelTier upgradeTier = GybhApi.getTierFromStack(heldItem);

            if (!barrel.isInvalid() && upgradeTier != null && barrel.tier.canApplyUpgrage(upgradeTier)) {

                barrel.upgradeBarrel(upgradeTier, state);
                heldItem.stackSize--;
                return true;
            }
        }

        if (barrel.itemStack == null) {
            if (heldItem != null) {
                if (heldItem.stackSize <= barrel.capacity) {
                    barrel.itemStack = heldItem.copy();
                    barrel.itemStack.stackSize = 1;
                    barrel.stored = heldItem.stackSize;
                    playerIn.setHeldItem(hand, null);
                    return true;
                }
            }
        }
        else {

            if (playerIn.isSneaking() && heldItem == null) {
                ItemStack stack = barrel.itemStack.copy();
                int dropSize = Math.min(64, barrel.stored);
                stack.stackSize = dropSize;
                playerIn.dropItem(stack, false);

                barrel.stored -= dropSize;
                if (barrel.stored == 0) {
                    barrel.itemStack = null;
                }
                return true;

            }
            else if (heldItem != null) {
                if (ItemStackUtils.areStacksEqual(barrel.itemStack, heldItem, true)) {
                    barrel.stored += heldItem.stackSize;

                    playerIn.setHeldItem(hand, null);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public BlockStateContainer createBlockState () {

        return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { BlockStates.HELD_STATE, BlockStates.BLOCK_ACCESS, BlockStates.BLOCKPOS });
    }

    @Override
    public IBlockState getExtendedState (IBlockState state, IBlockAccess world, BlockPos pos) {

        state = ((IExtendedBlockState) state).withProperty(BlockStates.BLOCK_ACCESS, world).withProperty(BlockStates.BLOCKPOS, pos);

        if (world.getTileEntity(pos) instanceof TileEntityModularBarrel) {

            final TileEntityModularBarrel tile = (TileEntityModularBarrel) world.getTileEntity(pos);

            if (tile != null && tile.tier != null)
                return ((IExtendedBlockState) state).withProperty(BlockStates.HELD_STATE, tile.tier.renderState);
        }
        return state;
    }

    @Override
    public boolean hasComparatorInputOverride (IBlockState state) {

        return true;
    }

    @Override
    public TileEntity createNewTileEntity (World world, int meta) {

        return new TileEntityModularBarrel();
    }

    @Override
    public boolean isOpaqueCube (IBlockState state) {

        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer () {

        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @Override
    public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {

        return ItemStackUtils.createStackFromTileEntity(world.getTileEntity(pos));
    }

    @Override
    public int quantityDropped (Random rnd) {

        return 0;
    }

    @Override
    public int getComparatorInputOverride (IBlockState blockState, World worldIn, BlockPos pos) {

        return 0;
    }

    @Override
    public boolean removedByPlayer (IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

        final TileEntityModularBarrel barrel = (TileEntityModularBarrel) world.getTileEntity(pos);
        if (barrel != null) {
            if (barrel.itemStack != null) {
                ItemStack stack = barrel.itemStack.copy();
                stack.stackSize = barrel.stored;
                ItemStackUtils.dropStackInWorld(world, pos, stack);
                barrel.itemStack = null;
            }
            if (!player.capabilities.isCreativeMode) {

                ItemStackUtils.dropStackInWorld(world, pos, ItemStackUtils.createStackFromTileEntity(barrel));
            }
        }
        return world.setBlockToAir(pos);
    }

    @Override
    public void onBlockPlacedBy (World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if (stack.hasTagCompound()) {

            final TileEntityModularBarrel barrel = (TileEntityModularBarrel) worldIn.getTileEntity(pos);

            if (barrel != null)
                barrel.readNBT(stack.getTagCompound().getCompoundTag("TileData"));
        }
    }

    @Override
    public void onBlockExploded (World world, BlockPos pos, Explosion explosion) {

        ItemStackUtils.dropStackInWorld(world, pos, ItemStackUtils.createStackFromTileEntity(world.getTileEntity(pos)));
        world.setBlockToAir(pos);
        this.onBlockDestroyedByExplosion(world, pos, explosion);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item itemIn, CreativeTabs tab, List<ItemStack> itemList) {

        for (final BarrelTier tier : GybhApi.REGISTRY.values())
            itemList.add(GybhApi.createTieredBarrel(tier));
    }
}