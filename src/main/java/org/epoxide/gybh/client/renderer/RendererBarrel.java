package org.epoxide.gybh.client.renderer;

import org.apache.commons.lang3.StringUtils;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import com.google.common.collect.ImmutableMap;

import net.darkhax.bookshelf.client.model.ITileEntityRender;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class RendererBarrel extends TileEntitySpecialRenderer<TileEntityModularBarrel> implements ITileEntityRender<TileEntityModularBarrel> {

    @Override
    public void renderTileEntityAt (TileEntityModularBarrel te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te.itemStack != null) {
            this.renderItemStack(te, x, y, z);
        }
    }

    public void renderItemStack (TileEntityModularBarrel tileEntity, double x, double y, double z) {

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
            drawNameplate(Minecraft.getMinecraft().fontRendererObj, StringUtils.abbreviate(tileEntity.itemStack.getDisplayName(), 17), 0, 0, 0, 0, -180f, 0, false);
            GlStateManager.translate(0.0F, -0.2F, 0.0F);

            drawNameplate(Minecraft.getMinecraft().fontRendererObj, this.getStoredCapacity(tileEntity.stored) + "/" + this.getStoredCapacity(tileEntity.capacity), 0, 0, 0, 0, -180f, 0, false);

            GlStateManager.popMatrix();
        }
    }

    private void renderItem (ItemStack itemstack) {

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

    private String getStoredCapacity (int stored) {

        final int stacks = (int) Math.floor(stored / 64);
        final int remaining = stored - stacks * 64;
        if (remaining > 0)
            return stacks + "x64+" + remaining;
        else
            return stacks + "x64";
    }

    @Override
    public ImmutableMap<String, String> getRenderStates (TileEntityModularBarrel tileEntity) {

        final BarrelTier tier = tileEntity.tier;
        final ItemStack itemStack = tileEntity.itemStack;

        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (tier != null && tier.renderState != null) {
            builder.put("frame", RenderUtils.getSprite(tier.renderState).getIconName());
        }
        if (itemStack != null) {
            final Item i = itemStack.getItem();
            if (i instanceof ItemBlock) {
                builder.put("background", RenderUtils.getSprite(Block.getBlockFromItem(i).getStateFromMeta(tileEntity.itemStack.getItemDamage())).getIconName());
            }
            else {
                builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
            }
        }
        else {
            builder.put("background", RenderUtils.getSprite(Blocks.STONE.getDefaultState()).getIconName());
        }
        return builder.build();
    }

    public static void drawNameplate (FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        final Tessellator tessellator = Tessellator.getInstance();
        final VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(-i - 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos(-i - 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos(i + 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos(i + 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
