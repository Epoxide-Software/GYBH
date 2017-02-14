package org.epoxide.gybh.client.renderer;

import net.darkhax.bookshelf.client.model.ModelMultiRetexturable;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BarrelItemOverride extends ItemOverrideList {

    public BarrelItemOverride () {

        super(ImmutableList.of());
    }

    @Override
    public IBakedModel handleItemState (IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

        if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("TileData")) {

            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("TileData")) {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag("TileData");
                final BarrelTier tier = GybhApi.getTier(tag.getString("TierID"));
                final ItemStack itemStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("ItemStackData"));
                if (tier != null) {

                    final IBlockState state = tier.renderState;

                    if (state != null) {
                        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
                        builder.put("frame", RenderUtils.getSprite(state).getIconName());
                        if (itemStack != null)
                            builder.put("background", RenderUtils.getSprite(Block.getBlockFromItem(itemStack.getItem()).getStateFromMeta(itemStack.getItemDamage())).getIconName());
                        else
                            builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
                        return ((ModelMultiRetexturable) originalModel).getRetexturedModel(builder.build());
                    }
                }
            }
        }

        else{
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            builder.put("frame", RenderUtils.getSprite(Blocks.FIRE.getDefaultState()).getIconName());
            builder.put("background", RenderUtils.getSprite(Blocks.FIRE.getDefaultState()).getIconName());
            return ((ModelMultiRetexturable) originalModel).getRetexturedModel(builder.build());
        }
        return originalModel;
    }
}