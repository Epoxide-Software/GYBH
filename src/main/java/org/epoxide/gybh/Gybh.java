package org.epoxide.gybh;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import org.epoxide.gybh.api.BarrelTier;
import org.epoxide.gybh.api.GybhApi;
import org.epoxide.gybh.blocks.BlockBarrel;
import org.epoxide.gybh.common.ProxyCommon;
import org.epoxide.gybh.items.ItemBlockBarrel;
import org.epoxide.gybh.items.ItemBarrelUpgrade;
import org.epoxide.gybh.libs.ConfigurationHandler;
import org.epoxide.gybh.libs.Constants;
import org.epoxide.gybh.tabs.CreativeTabGybh;
import org.epoxide.gybh.tileentity.TileEntityModularBarrel;

import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.OreDictUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = Constants.MODID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER, dependencies = "required-after:bookshelf@[1.4.2.327,)")
public class Gybh {

    @SidedProxy(serverSide = Constants.SERVER, clientSide = Constants.CLIENT)
    public static ProxyCommon proxy;

    @Instance(Constants.MODID)
    public static Gybh instance;

    public static Block blockModularBarrels;
    public static Item itemBarrelUpgrade;
    public static Item itemBlockModularBarrel;

    public static CreativeTabs tabGybh;

    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {

        GameRegistry.registerTileEntity(TileEntityModularBarrel.class, "modularBarrel");


        ConfigurationHandler.initConfig(event.getSuggestedConfigurationFile());
        tabGybh = new CreativeTabGybh();
        blockModularBarrels = new BlockBarrel();
        itemBlockModularBarrel = new ItemBlockBarrel(blockModularBarrels);
        GameRegistry.register(blockModularBarrels);
        GameRegistry.register(itemBlockModularBarrel);

        itemBarrelUpgrade = new ItemBarrelUpgrade();
        GameRegistry.register(itemBarrelUpgrade);

        proxy.registerBlockRenderers();
    }

    @EventHandler
    public void init (FMLInitializationEvent event) {

        GybhApi.REGISTRY = GybhApi.REGISTRY.entrySet().stream().sorted(Entry.comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        proxy.registerBlockRenderers();

        for (final BarrelTier tier : GybhApi.REGISTRY.values()) {

            if (tier.tier == 1)
                GameRegistry.addRecipe(new ShapedOreRecipe(GybhApi.createTieredBarrel(tier), new Object[] { "xsx", "szs", "xsx", 'x', tier.recipe, 's', OreDictUtils.STONE, 'z', Blocks.CHEST }));

            GameRegistry.addRecipe(new ShapedOreRecipe(GybhApi.createTierUpgrade(tier), new Object[] { "xsx", "sss", "xsx", 'x', tier.recipe, 's', OreDictUtils.STONE, 's', OreDictUtils.SLIMEBALL }));
        }

        if (Loader.isModLoaded("Waila"))
            FMLInterModComms.sendMessage("Waila", "register", "org.epoxide.gybh.plugins.PluginWaila.registerAddon");

        if (Loader.isModLoaded("theoneprobe"))
            FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "org.epoxide.gybh.plugins.PluginTOP$GetTheOneProbe");
    }

    @EventHandler
    public void handleIMC (IMCEvent event) {

        for (final IMCMessage msg : event.getMessages())
            if (msg.key.equalsIgnoreCase("addTier") && msg.isNBTMessage()) {

                final NBTTagCompound tag = msg.getNBTValue();
                final String name = tag.getString("tierName");
                final Block block = Block.getBlockFromName(tag.getString("blockId"));
                final int meta = tag.getInteger("meta");
                final int tier = tag.getInteger("tier");
                final Object recipe = this.getRecipeFromStackString(tag.getString("recipe"));

                if (!name.isEmpty() && block != null && meta >= 0 && tier >= 0 && recipe != null)
                    GybhApi.createTier(msg.getSender(), name, block, meta, recipe, Math.min(10, tier));

                else
                    Constants.LOG.info(msg.getSender() + " tried to register a tier, but it failed.");
            }

            else if (msg.key.equalsIgnoreCase("removeTier") && msg.isResourceLocationMessage())
                GybhApi.removeTier(msg.getSender(), msg.getResourceLocationValue());
    }

    private Object getRecipeFromStackString (String string) {

        final ItemStack stack = ItemStackUtils.createStackFromString(string);
        return ItemStackUtils.isValidStack(stack) ? stack : string;
    }
}