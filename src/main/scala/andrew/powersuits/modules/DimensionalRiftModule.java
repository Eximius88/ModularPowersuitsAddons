package andrew.powersuits.modules;


import andrew.powersuits.util.MPSATeleporter;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class DimensionalRiftModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_DIMENSIONAL_RIFT = "WIP Dimensional Tear Generator WIP";
    public static final String DIMENSIONAL_RIFT_ENERGY_GENERATION = "Energy Consumption";
    public static final String DIMENSIONAL_RIFT_HEAT_GENERATION = "Heat Generation";
    private int timer = 0, cooldown = 0;

    public DimensionalRiftModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(DIMENSIONAL_RIFT_HEAT_GENERATION, 55);
        addBaseProperty(DIMENSIONAL_RIFT_ENERGY_GENERATION, 20000);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, false);
    }

    @Override
    public String getTextureFile() {
        return "kineticgen";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_DIMENSIONAL_RIFT;
    }

    @Override
    public String getLocalizedName() {
        return MODULE_DIMENSIONAL_RIFT;
    }

    @Override
    public String getDescription() {
        return "Generate a tear in the space-time continuum that will teleport the player to its relative coordinates in the nether or overworld. You must bind this module to a key and activate it to use.";
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {

        if ((playerClicking.ridingEntity == null) && (playerClicking.riddenByEntity == null) && ((playerClicking instanceof EntityPlayerMP)))
        {
            EntityPlayerMP thePlayer = (EntityPlayerMP)playerClicking;
            if (thePlayer.dimension != -1)
            {
                thePlayer.setLocationAndAngles(0.5D, thePlayer.posY, 0.5D, thePlayer.rotationYaw, thePlayer.rotationPitch);
                thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, -1, new MPSATeleporter(thePlayer.mcServer.worldServerForDimension(-1)));
            }
            else if (thePlayer.dimension == -1 || thePlayer.dimension == 1)
            {
                if (!world.isRemote){
                MinecraftServer.getServerConfigurationManager(MinecraftServer.getServer()).respawnPlayer((EntityPlayerMP) thePlayer, 0, true);
            }
            }
            ElectricItemUtils.drainPlayerEnergy(thePlayer, ModuleManager.computeModularProperty(item, DIMENSIONAL_RIFT_ENERGY_GENERATION));
            MuseHeatUtils.heatPlayer(thePlayer, ModuleManager.computeModularProperty(item, DIMENSIONAL_RIFT_HEAT_GENERATION));
        }

    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }
}
