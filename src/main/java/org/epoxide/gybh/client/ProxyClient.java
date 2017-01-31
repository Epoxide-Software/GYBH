package org.epoxide.gybh.client;

import org.epoxide.gybh.Gybh;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;
import org.epoxide.gybh.client.renderer.RendererBarrel;
import org.epoxide.gybh.client.renderer.BarrelItemOverride;
import org.epoxide.gybh.client.renderer.UpgradeItemOverride;
import org.epoxide.gybh.common.ProxyCommon;
import org.epoxide.gybh.libs.Constants;
import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import net.darkhax.bookshelf.client.model.ModelRetexturable;
import net.darkhax.bookshelf.events.RenderItemEvent;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProxyClient extends ProxyCommon {

    public static final ModelResourceLocation MODEL = new ModelResourceLocation(new ResourceLocation(Constants.MODID, "modular_barrel"), null);
    public static final ModelResourceLocation MODEL_UPGRADE = new ModelResourceLocation(new ResourceLocation(Constants.MODID, "barrel_upgrade"), null);

    @Override
    public void registerBlockRenderers () {

        MinecraftForge.EVENT_BUS.register(this);

        ModelLoader.setCustomModelResourceLocation(Gybh.itemBlockModularBarrel, 0, MODEL);
        ModelLoader.setCustomModelResourceLocation(Gybh.itemBarrelUpgrade, 0, MODEL_UPGRADE);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityModularBarrel.class, new RendererBarrel());
    }

    @SubscribeEvent
    public void onModelBake (ModelBakeEvent event) {

        try {

            IModel currentModel = ModelLoaderRegistry.getModel(new ResourceLocation(Constants.MODID, "block/modular_barrel"));

            if (currentModel instanceof IRetexturableModel) {

                final IRetexturableModel model = (IRetexturableModel) currentModel;
                final IBakedModel baseModel = event.getModelRegistry().getObject(MODEL);

                if (baseModel instanceof IPerspectiveAwareModel)
                    event.getModelRegistry().putObject(MODEL, new ModelRetexturable(model, Blocks.GLASS.getDefaultState(), RenderUtils.getBasicTransforms((IPerspectiveAwareModel) baseModel), new BarrelItemOverride()));
            }

            currentModel = ModelLoaderRegistry.getModel(new ResourceLocation(Constants.MODID, "item/barrel_upgrade"));

            if (currentModel instanceof IRetexturableModel) {

                final IRetexturableModel model = (IRetexturableModel) currentModel;
                final IBakedModel baseModel = event.getModelRegistry().getObject(MODEL_UPGRADE);

                if (baseModel instanceof IPerspectiveAwareModel)
                    event.getModelRegistry().putObject(MODEL_UPGRADE, new ModelRetexturable(model, Blocks.GLASS.getDefaultState(), RenderUtils.getBasicTransforms((IPerspectiveAwareModel) baseModel), new UpgradeItemOverride()));
            }
        }

        catch (final Exception exception) {

            Constants.LOG.warn(exception);
            exception.printStackTrace();
        }
    }

    @SubscribeEvent
    public void renderItem (RenderItemEvent.Allow event) {

        if (event.getItemStack().getItem() == Gybh.itemBlockModularBarrel)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void renderItem (RenderItemEvent.Pre event) {

        GlStateManager.pushMatrix();
        BarrelTier tier = null;
        ItemStack itemStack = null;
        int stored = 0;

        if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("TileData")) {
            NBTTagCompound tag = event.getItemStack().getTagCompound().getCompoundTag("TileData");
            tier = GybhApi.getTier(tag.getString("TierID"));
            itemStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("ItemStackData"));
            stored = tag.getCompoundTag("ItemStackData").getInteger("Stored");
        }

        for (int i = 0; i < 4; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5D, 0.5D, 0.5D);
            GlStateManager.rotate(90.0F * i, 0.0F, 1.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.translate(0.0F, 0.0F, 0.4375F);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.4F, 0.07F);
            GlStateManager.scale(0.4F, 0.4F, 0.4F);
            EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRendererObj, itemStack.getDisplayName(), 0, 0, 0, 0, -180f, 0, false, false);

            GlStateManager.translate(0.0F, -0.2F, 0.07F);
            EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRendererObj, getStoredCapacity(stored) + "/" + getStoredCapacity(tier.getCapacity()), 0, 0, 0, 0, -180f, 0, false, false);
        }

        GlStateManager.popMatrix();
    }


     private String getStoredCapacity (int stored) {

        int stacks = (int) Math.floor(stored / 64);
        int remaining = stored - (stacks * 64);
        if (remaining > 0)
            return stacks + "x64+" + remaining;
        else
            return stacks + "x64";
    }
}
