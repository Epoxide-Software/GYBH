package org.epoxide.gybh.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epoxide.gybh.Gybh;
import org.epoxide.gybh.libs.ConfigurationHandler;
import org.epoxide.gybh.libs.Constants;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.ModUtils;
import net.darkhax.bookshelf.lib.util.OreDictUtils;
import net.darkhax.bookshelf.lib.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public class GybhApi {

    /**
     * Registry of all tiers that have been registered.
     */
    public static Map<ResourceLocation, BarrelTier> REGISTRY = new HashMap<ResourceLocation, BarrelTier>();

    // Tier 1
    public static final BarrelTier WOOD_OAK = createTier("oak", Blocks.PLANKS, 0, new ItemStack(Blocks.PLANKS, 1, 0), 1);
    public static final BarrelTier WOOD_SPRUCE = createTier("spruce", Blocks.PLANKS, 1, new ItemStack(Blocks.PLANKS, 1, 1), 1);
    public static final BarrelTier WOOD_BIRCH = createTier("birch", Blocks.PLANKS, 2, new ItemStack(Blocks.PLANKS, 1, 2), 1);
    public static final BarrelTier WOOD_JUNGLE = createTier("jungle", Blocks.PLANKS, 3, new ItemStack(Blocks.PLANKS, 1, 3), 1);
    public static final BarrelTier WOOD_ACACIA = createTier("acacia", Blocks.PLANKS, 4, new ItemStack(Blocks.PLANKS, 1, 4), 1);
    public static final BarrelTier WOOD_DARK_OAK = createTier("dark_oak", Blocks.PLANKS, 5, new ItemStack(Blocks.PLANKS, 1, 5), 1);
    public static final BarrelTier CLAY = createTier("clay", Blocks.HARDENED_CLAY, 0, OreDictUtils.INGOT_BRICK, 1);

    // Tier 2
    public static final BarrelTier STONE_COBBLE = createTier("stone_cobble", Blocks.COBBLESTONE, 0, OreDictUtils.COBBLESTONE, 2);
    public static final BarrelTier STONE_SMOOTH = createTier("stone_smooth", Blocks.STONE, 0, new ItemStack(Blocks.STONE, 1, 0), 2);
    public static final BarrelTier STONE_GRANITE = createTier("stone_granite", Blocks.STONE, 1, new ItemStack(Blocks.STONE, 1, 1), 2);
    public static final BarrelTier STONE_GRANITE_SMOOTH = createTier("stone_granite_smooth", Blocks.STONE, 2, new ItemStack(Blocks.STONE, 1, 2), 2);
    public static final BarrelTier STONE_DIORITE = createTier("stone_diorite", Blocks.STONE, 3, new ItemStack(Blocks.STONE, 1, 3), 2);
    public static final BarrelTier STONE_DIORITE_SMOOTH = createTier("stone_diorite_smooth", Blocks.STONE, 4, new ItemStack(Blocks.STONE, 1, 4), 2);
    public static final BarrelTier STONE_ANDESITE = createTier("stone_andesite", Blocks.STONE, 5, new ItemStack(Blocks.STONE, 1, 5), 2);
    public static final BarrelTier STONE_ANDESITE_SMOOTH = createTier("stone_andesite_smooth", Blocks.STONE, 6, new ItemStack(Blocks.STONE, 1, 6), 2);
    public static final BarrelTier SANDSTONE_BRICK = createTier("sandstone_brick", Blocks.SANDSTONE, 0, Blocks.SANDSTONE, 2);
    public static final BarrelTier SANDSTONE_BRICK_RED = createTier("sandstone_brick_red", Blocks.RED_SANDSTONE, 0, Blocks.RED_SANDSTONE, 2);
    public static final BarrelTier BRICK = createTier("brick", Blocks.BRICK_BLOCK, 0, Blocks.BRICK_BLOCK, 2);
    public static final BarrelTier BRICK_NETHER = createTier("brick_nether", Blocks.NETHER_BRICK, 0, OreDictUtils.INGOT_BRICK_NETHER, 2);
    public static final BarrelTier BRICK_STONE = createTier("brick_stone", Blocks.STONEBRICK, 0, Blocks.STONEBRICK, 2);
    public static final BarrelTier BRICK_PURPUR = createTier("brick_purpur", Blocks.PURPUR_BLOCK, 0, Blocks.PURPUR_BLOCK, 2);
    public static final BarrelTier BRICK_END = createTier("brick_end", Blocks.END_BRICKS, 0, Blocks.END_BRICKS, 2);
    public static final BarrelTier PRISMARINE = createTier("prismarine", Blocks.PRISMARINE, 0, Items.PRISMARINE_SHARD, 2);

    // Tier 3
    public static final BarrelTier IRON = createTier("iron", Blocks.IRON_BLOCK, 0, OreDictUtils.INGOT_IRON, 3);
    public static final BarrelTier GOLD = createTier("gold", Blocks.GOLD_BLOCK, 0, OreDictUtils.INGOT_GOLD, 3);
    public static final BarrelTier OBSIDIAN = createTier("obsidian", Blocks.OBSIDIAN, 0, OreDictUtils.OBSIDIAN, 3);

    // Tier 4
    public static final BarrelTier LAPIS = createTier("lapis", Blocks.LAPIS_BLOCK, 0, OreDictUtils.GEM_LAPIS, 4);
    public static final BarrelTier REDSTONE = createTier("redstone", Blocks.REDSTONE_BLOCK, 0, OreDictUtils.DUST_REDSTONE, 4);
    public static final BarrelTier QUARTZ = createTier("quartz", Blocks.QUARTZ_BLOCK, 0, OreDictUtils.GEM_QUARTZ, 4);

    // Tier 5
    public static final BarrelTier DIAMOND = createTier("diamond", Blocks.DIAMOND_BLOCK, 0, OreDictUtils.GEM_DIAMOND, 5);
    public static final BarrelTier EMERALD = createTier("emerald", Blocks.EMERALD_BLOCK, 0, OreDictUtils.GEM_EMERALD, 5);

    /**
     * Registers a tier with the tier registry.
     *
     * @param identifier The identifier to use.
     * @param tier The tier object to register.
     *
     * @return The tier object being registered, for ease of use.
     */
    public static BarrelTier registerTier (ResourceLocation identifier, BarrelTier tier) {

        REGISTRY.put(identifier, tier);
        return tier;
    }

    /**
     * Retrieves a BarrelTier based on it's identifier.
     *
     * @param identifier The identifier to look up.
     *
     * @return The BarrelTier that was found, or the default tier.
     */
    public static BarrelTier getTier (String identifier) {

        return REGISTRY.get(new ResourceLocation(identifier));
    }

    /**
     * Creates a new ItemStack which represents the upgrade item for the tier passed.
     *
     * @param tier The tier to create an upgrade stack for.
     *
     * @return An ItemStack which represents the upgrade item for the tier.
     */
    public static ItemStack createTierUpgrade (BarrelTier tier) {

        final ItemStack stack = new ItemStack(Gybh.itemBarrelUpgrade);
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("TierID", tier.identifier.toString());
        stack.setTagCompound(tag);
        return stack;
    }

    /**
     * Creates a new ItemStack which represents the tank for the tier passed.
     *
     * @param tier The tier to create an upgrade stack for.
     *
     * @return An ItemStack which represents the tank for the tier.
     */
    public static ItemStack createTieredBarrel (BarrelTier tier) {

        final ItemStack stack = new ItemStack(Gybh.blockModularBarrels);
        ItemStackUtils.prepareDataTag(stack);
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("TierID", tier.identifier.toString());
        stack.getTagCompound().setTag("TileData", tag);
        return stack;
    }

    /**
     * Reads a BarrelTier from an ItemStack.
     *
     * @param stack The stack to read the tier from.
     *
     * @return The BarrelTier that was found.
     */
    public static BarrelTier getTierFromStack (ItemStack stack) {

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("TierID"))
            return getTier(stack.getTagCompound().getString("TierID"));

        return null;
    }

    /**
     * Generates the tooltip for a BarrelTier.
     *
     * @param tier The tier to represent.
     * @param tooltip The list to add to.
     */
    public static void createTierTooltip (BarrelTier tier, ItemStack stack, int storage,  List<String> tooltip) {

        final EntityPlayer clientPlayer = PlayerUtils.getClientPlayer();

        if (tier != null && clientPlayer != null && clientPlayer.worldObj != null) {

            if (stack != null) {

                tooltip.add(I18n.format("tooltip.gybh.capacity", storage, tier.getCapacity()));
                tooltip.add(I18n.format("tooltip.gybh.contents") + ": " + stack.getDisplayName());
            }

            else
                tooltip.add(I18n.format("tooltip.gybh.capacity.upgrade", tier.getCapacity() / 1000));

            tooltip.add(I18n.format("tooltip.gybh.block") + ": " + ItemStackUtils.getStackFromState(tier.renderState, 1).getDisplayName());
            tooltip.add(I18n.format("tooltip.gybh.tier") + ": " + tier.tier);

            tooltip.add(I18n.format("tooltip.gybh.owner", ChatFormatting.BLUE, ModUtils.getModName(tier.identifier.getResourceDomain())));
            return;
        }

        tooltip.add(ChatFormatting.RED + "[WARNING] " + ChatFormatting.GRAY + I18n.format("tooltip.gybh.missing"));
    }

    public static void removeTier (String modId, ResourceLocation tier) {

        if (REGISTRY.remove(tier) != null)
            Constants.LOG.info("The tier " + tier.toString() + " was removed by " + modId);
    }

    public static BarrelTier createTier (String modId, String name, Block block, int meta, Object recipe, int tier) {

        final ResourceLocation identifier = new ResourceLocation(modId, name);
        final BarrelTier BarrelTier = new BarrelTier(identifier, block.getStateFromMeta(meta), recipe, tier);
        REGISTRY.put(identifier, BarrelTier);

        return BarrelTier;
    }

    /**
     * Generates a basic markdown table for all tiers added by a mod.
     *
     * @param modId The ID of the mod to look for.
     */
    public static void printMarkdownTable (String modId) {

        System.out.println("|Name|Identifier|Tier|Capacity|Notes|");
        System.out.println("|----|----------|----|--------|-----|");
        for (BarrelTier tier : REGISTRY.values())
            if (tier.identifier.getResourceDomain().startsWith(modId))
                System.out.println(String.format("|%s|%s|%d|%s|%s|", ItemStackUtils.getStackFromState(tier.renderState, 1).getDisplayName(), tier.identifier.toString(), tier.tier, (tier.getCapacity() / 1000) + "B", tier.isFlammable(null, null, null) ? "flammable" : ""));
    }

    private static BarrelTier createTier (String name, Block block, int meta, Object recipe, int tier) {

        return createTier(Constants.MODID, name, block, meta, recipe, tier);
    }
}