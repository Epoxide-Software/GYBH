package org.epoxide.gybh.blocks;

import java.util.List;
import java.util.Random;

import org.epoxide.gybh.Gybh;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;
import org.epoxide.gybh.items.ItemBarrelUpgrade;
import org.epoxide.gybh.libs.Constants;
import org.epoxide.gybh.tileentity.ItemHandlerBarrel;
import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import net.darkhax.bookshelf.lib.BlockStates;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
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

        // Handle bad barrel
        if (barrel == null || barrel.getTier() == null)
            return heldItem != null && !(heldItem.getItem() instanceof ItemBlock);

        // Handle upgrade
        if (heldItem != null && heldItem.getItem() instanceof ItemBarrelUpgrade) {

            final BarrelTier upgradeTier = GybhApi.getTierFromStack(heldItem);

            if (!barrel.isInvalid() && upgradeTier != null && barrel.getTier().canApplyUpgrage(upgradeTier)) {

                barrel.upgradeBarrel(upgradeTier, state);
                heldItem.stackSize--;
                barrel.updateTile();
                return true;
            }
        }

        // Handle adding new blocks
        if (ItemStackUtils.isValidStack(heldItem)) {

            final ItemStack remainder = barrel.getInventory().insertItem(0, heldItem, false);
            playerIn.setHeldItem(hand, remainder);
            barrel.updateTile();
            return true;
        }

        // Handle item extraction
        else {

            final ItemHandlerBarrel inventory = barrel.getInventory();

            if (inventory.getCount() <= 0)
                return false;

            final int dropSize = playerIn.isSneaking() ? inventory.getContentStack().getMaxStackSize() : 1;
            final ItemStack extracted = inventory.extractItem(0, dropSize, false);

            if (extracted != null) {

                if (!worldIn.isRemote) {

                    playerIn.entityDropItem(extracted, 1f);
                }

                barrel.updateTile();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean removedByPlayer (IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {

        this.handleDrops(player, world, pos, world.getTileEntity(pos));
        return world.setBlockToAir(pos);
    }

    @Override
    public void onBlockExploded (World world, BlockPos pos, Explosion explosion) {

        this.handleDrops(null, world, pos, world.getTileEntity(pos));
        world.setBlockToAir(pos);
        super.onBlockExploded(world, pos, explosion);
    }

    private void handleDrops (EntityPlayer player, World world, BlockPos pos, TileEntity tile) {
        
        if (player != null && ((player.isCreative() && player.isSneaking()) || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0)) {

            ItemStackUtils.dropStackInWorld(world, pos, ItemStackUtils.createStackFromTileEntity(tile));
            return;
        }
        
        if (!(tile instanceof TileEntityModularBarrel) || (player != null && player.isCreative()))
            return;

        final TileEntityModularBarrel barrel = (TileEntityModularBarrel) tile;

        if (barrel.getInventory() != null) {

            final int maxStackSize = barrel.getInventory().getContentStack().getMaxStackSize();

            while (barrel.getInventory().getCount() > 0) {

                final ItemStack drop = barrel.getInventory().extractItem(0, maxStackSize, false);

                if (drop != null) {
                    ItemStackUtils.dropStackInWorld(world, pos, drop);
                }
            }
        }

        ItemStackUtils.dropStackInWorld(world, pos, ItemStackUtils.createStackFromTileEntity(tile));
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

            if (tile != null && tile.getTier() != null)
                return ((IExtendedBlockState) state).withProperty(BlockStates.HELD_STATE, tile.getTier().renderState);
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
    public void onBlockPlacedBy (World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if (stack.hasTagCompound()) {

            final TileEntityModularBarrel barrel = (TileEntityModularBarrel) worldIn.getTileEntity(pos);

            if (barrel != null) {

                barrel.readNBT(stack.getTagCompound().getCompoundTag("TileData"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item itemIn, CreativeTabs tab, List<ItemStack> itemList) {

        for (final BarrelTier tier : GybhApi.REGISTRY.values()) {
            itemList.add(GybhApi.createTieredBarrel(tier));
        }
    }
}