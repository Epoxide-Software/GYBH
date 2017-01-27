package org.epoxide.gybh.client.renderer;

import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RendererBarrel extends TileEntitySpecialRenderer<TileEntityModularBarrel> {

    @Override
    public void renderTileEntityAt (TileEntityModularBarrel te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te != null) {

            if (te.itemStack != null)
                renderItemStack(te, x, y, z);
        }
    }


    public void renderItemStack (TileEntityModularBarrel tileEntity, double x, double y, double z) {

        for (int i = 0; i < 4; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
            GlStateManager.rotate(90.0F * i, 0.0F, 1.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.translate(0.0F, 0.0F, 0.4375F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.4F, 0.07F);
            GlStateManager.scale(0.45F, 0.45F, 0.45F);
            EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRendererObj, tileEntity.itemStack.getDisplayName(), 0, 0, 0, 0, -180f, 0, false, false);

            GlStateManager.translate(0.0F, -0.2F, 0.07F);
            EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRendererObj, tileEntity.stored + "/" + tileEntity.capacity, 0, 0, 0, 0, -180f, 0, false, false);
            GlStateManager.popMatrix();
            this.renderItem(tileEntity.itemStack, tileEntity.getWorld());
            GlStateManager.popMatrix();
        }
    }

    private void renderItem (ItemStack itemstack, World world) {


        if (itemstack != null) {
            EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, itemstack);
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int i = 0;

            GlStateManager.rotate((float) i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);


            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().renderItem.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
