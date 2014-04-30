package andrew.powersuits.modules;


import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;


import java.text.DecimalFormat;
import java.util.List;

public class EUReaderModule extends PowerModuleBase implements IRightClickModule {

    public static final String MODULE_EU_READER = "EU Reader";

    public EUReaderModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(ModCompatability.getIC2Item("ecMeter"));
    }

    @Override
    public String getTextureFile() {
        return "eureader";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_EU_READER;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.euReader.name");
    }

    @Override
    public String getDescription() {
        return "An IC2 EU Reader integrated in your power tool.";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {

    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        {
            if (!IC2.platform.isSimulating()) {
                return true;
            }
            TileEntity tileEntity = EnergyNet.instance.getTileEntity(world, x, y, z);
            if (((tileEntity instanceof IEnergySource)) || ((tileEntity instanceof IEnergyConductor)) || ((tileEntity instanceof IEnergySink))) {
                NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);

                long currentTotalEnergyEmitted = EnergyNet.instance.getTotalEnergyEmitted(tileEntity);
                long currentTotalEnergySunken = EnergyNet.instance.getTotalEnergySunken(tileEntity);
                long currentMeasureTime = world.getWorldTime();
                if ((nbtData.getInteger("lastMeasuredTileEntityX") != x) || (nbtData.getInteger("lastMeasuredTileEntityY") != y) || (nbtData.getInteger("lastMeasuredTileEntityZ") != z)) {
                    nbtData.setInteger("lastMeasuredTileEntityX", x);
                    nbtData.setInteger("lastMeasuredTileEntityY", y);
                    nbtData.setInteger("lastMeasuredTileEntityZ", z);

                    IC2.platform.messagePlayer(player, "Starting new measurement", new Object[0]);
                } else {
                    long measurePeriod = currentMeasureTime - nbtData.getLong("lastMeasureTime");
                    if (measurePeriod < 1L) {
                        measurePeriod = 1L;
                    }
                    double deltaEmitted = (currentTotalEnergyEmitted - nbtData.getLong("lastTotalEnergyEmitted")) / measurePeriod;
                    double deltaSunken = (currentTotalEnergySunken - nbtData.getLong("lastTotalEnergySunken")) / measurePeriod;

                    DecimalFormat powerFormat = new DecimalFormat("0.##");

                    IC2.platform.messagePlayer(player, "Measured power [EU/t]: " + powerFormat.format(deltaSunken) + " in " + powerFormat.format(deltaEmitted) + " out " + powerFormat.format(deltaSunken - deltaEmitted) + " gain" + " (avg. over " + measurePeriod + " ticks)", new Object[0]);
                }
                nbtData.setLong("lastTotalEnergyEmitted", currentTotalEnergyEmitted);
                nbtData.setLong("lastTotalEnergySunken", currentTotalEnergySunken);
                nbtData.setLong("lastMeasureTime", currentMeasureTime);
            }
            return true;
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }

}