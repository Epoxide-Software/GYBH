package org.epoxide.gybh.client.renderer;

import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RendererBarrel extends TileEntitySpecialRenderer<TileEntityModularBarrel> {

    @Override
    public void renderTileEntityAt (TileEntityModularBarrel te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te != null) {

            //            final FluidTank fluid = te.tank;

            //            if (fluid != null && fluid.getFluid() != null && fluid.getFluidAmount() > 0) {
            //
            //                GlStateManager.pushMatrix();
            //                GlStateManager.enableBlend();
            //
            //                RenderUtils.translateAgainstPlayer(te.getPos(), false);
            //                RenderUtils.renderFluid(fluid.getFluid(), te.getPos(), 0.06d, 0.12d, 0.06d, 0.0d, 0.0d, 0.0d, 0.88d, (double) fluid.getFluidAmount() / (double) fluid.getCapacity() * 0.8d, 0.88d);
            //
            //                GlStateManager.disableBlend();
            //                GlStateManager.popMatrix();
            //            }
        }
    }
}
