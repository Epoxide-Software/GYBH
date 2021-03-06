// package org.epoxide.gybh.plugins;
//
// import java.util.List;
//
// import org.epoxide.gybh.blocks.BlockTank;
//
// import mcp.mobius.waila.api.IWailaConfigHandler;
// import mcp.mobius.waila.api.IWailaDataAccessor;
// import mcp.mobius.waila.api.IWailaDataProvider;
// import mcp.mobius.waila.api.IWailaRegistrar;
// import org.epoxide.gybh.api.GybhApi;
// import org.epoxide.gybh.tileentity.TileEntityModularTank;
// import net.minecraft.entity.player.EntityPlayerMP;
// import net.minecraft.item.ItemStack;
// import net.minecraft.nbt.NBTTagCompound;
// import net.minecraft.tileentity.TileEntity;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.world.World;
// import net.minecraftforge.fml.common.Optional;
//
// @Optional.Interface(iface = "IWailaDataProvider", modid = "Waila")
// public class PluginWaila implements IWailaDataProvider {
//
// @Override
// public ItemStack getWailaStack (IWailaDataAccessor data, IWailaConfigHandler
// cfg) {
//
// return data.getStack();
// }
//
// @Override
// public List<String> getWailaHead (ItemStack stack, List<String> tip,
// IWailaDataAccessor data, IWailaConfigHandler cfg) {
//
// return tip;
// }
//
// @Override
// public List<String> getWailaBody (ItemStack stack, List<String> tip,
// IWailaDataAccessor data, IWailaConfigHandler cfg) {
//
// if (data.getTileEntity() instanceof TileEntityModularTank) {
//
// final TileEntityModularTank tank = (TileEntityModularTank)
// data.getTileEntity();
// GybhApi.createTierTooltip(tank.tier, tank.tank.getFluid(), tip);
// }
//
// return tip;
// }
//
// @Override
// public List<String> getWailaTail (ItemStack stack, List<String> tip,
// IWailaDataAccessor data, IWailaConfigHandler cfg) {
//
// return tip;
// }
//
// @Override
// public NBTTagCompound getNBTData (EntityPlayerMP player, TileEntity te,
// NBTTagCompound tag, World world, BlockPos pos) {
//
// if (te != null && !te.isInvalid())
// te.writeToNBT(tag);
//
// return tag;
// }
//
// public static void registerAddon (IWailaRegistrar register) {
//
// final PluginWaila dataProvider = new PluginWaila();
// register.registerBodyProvider(dataProvider, BlockTank.class);
// }
// }