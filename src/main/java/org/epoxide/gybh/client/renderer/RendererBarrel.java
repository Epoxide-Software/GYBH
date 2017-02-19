package org.epoxide.gybh.client.renderer;

import com.google.common.collect.ImmutableMap;
import net.darkhax.bookshelf.client.model.ITileEntityRender;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

public class RendererBarrel extends TileEntitySpecialRenderer<TileEntityModularBarrel> implements ITileEntityRender<TileEntityModularBarrel> {

    @Override
    public void renderTileEntityAt(TileEntityModularBarrel te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te.itemStack != null)
            renderItemStack(te, x, y, z);
    }


    public void renderItemStack(TileEntityModularBarrel tileEntity, double x, double y, double z) {

        for (int i = 0; i < 4; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
            GlStateManager.rotate(90.0F * i, 0.0F, 1.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.translate(0.0F, 0.0F, 0.4375F);

            float add = 0;
            if (!(tileEntity.itemStack.getItem() instanceof ItemBlock)) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.1F);
                this.renderItem(tileEntity.itemStack);
                GlStateManager.popMatrix();
                add += 0.6;
            }
            GlStateManager.scale(0.4F, 0.4F, 0.4F);
            GlStateManager.translate(0.0F, 0.4F + add, 0.2F);
            EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRendererObj, tileEntity.itemStack.getDisplayName(), 0, 0, 0, 0, -180f, 0, false, false);
            GlStateManager.translate(0.0F, -0.2F, 0.0F);

            EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRendererObj, getStoredCapacity(tileEntity.stored) + "/" + getStoredCapacity(tileEntity.capacity), 0, 0, 0, 0, -180f, 0, false, false);


            GlStateManager.popMatrix();
        }
    }

    private void renderItem(ItemStack itemstack) {
        if (itemstack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    private String getStoredCapacity(int stored) {

        int stacks = (int) Math.floor(stored / 64);
        int remaining = stored - (stacks * 64);
        if (remaining > 0)
            return stacks + "x64+" + remaining;
        else
            return stacks + "x64";
    }


    @Override
    public ImmutableMap<String, String> getRenderStates(TileEntityModularBarrel tileEntity) {
        BarrelTier tier = tileEntity.tier;
        ItemStack itemStack = tileEntity.itemStack;

        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (tier != null && tier.renderState != null)
            builder.put("frame", RenderUtils.getSprite(tier.renderState).getIconName());
        if (itemStack != null) {
            Item i = itemStack.getItem();
            if (i instanceof ItemBlock)
                builder.put("background", RenderUtils.getSprite(Block.getBlockFromItem(i).getStateFromMeta(tileEntity.itemStack.getItemDamage())).getIconName());
            else
                builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
        } else {
            builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
        }
        return builder.build();
    }
}
