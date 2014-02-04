package andrew.powersuits.modules;

import andrew.powersuits.util.DimensionUtilities;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 5:41 PM 4/30/13
 */
public class DimensionalRiftModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_DIMENSIONAL_RIFT = "WIP Dimensional Tear Generator WIP";
    public static final String DIMENSIONAL_RIFT_ENERGY_GENERATION = "Energy Consumption";
    public static final String DIMENSIONAL_RIFT_HEAT_GENERATION = "Heat Generation";
    private int timer = 0, cooldown = 0;

    public DimensionalRiftModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(DIMENSIONAL_RIFT_HEAT_GENERATION, 50);
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
        return MuseCommonStrings.CATEGORY_MOVEMENT;
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
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (ModuleManager.isModuleOnline(item.getTagCompound(), MODULE_DIMENSIONAL_RIFT)) {
            //player.
            World world = Minecraft.getMinecraft().theWorld;
            Entity target = Minecraft.getMinecraft().thePlayer;
            if ((world.isRemote) || (!(target instanceof EntityLivingBase))) {

            }
            if (target.dimension == 1)
            {
                if ((target instanceof EntityPlayer)) {
                    ((EntityPlayer)target).addChatMessage("Nothing happens...");
                }

            }
            if (target.dimension != -1)
            {
                ChunkCoordinates coords = (target instanceof EntityPlayer) ? ((EntityPlayer)target).getBedLocation(target.dimension) : null;
                if ((coords == null) || ((coords.posX == 0) && (coords.posY == 0) && (coords.posZ == 0))) {
                    coords = world.getSpawnPoint();
                }
                int yPos = coords.posY;
                while ((world.getBlockId(coords.posX, yPos, coords.posZ) != 0) && (world.getBlockId(coords.posX, yPos + 1, coords.posZ) != 0)) {
                    yPos++;
                }
                ((EntityLivingBase)target).setPositionAndUpdate(coords.posX + 0.5D, yPos, coords.posZ + 0.5D);
            }
            else if (target.dimension == -1)
            {
                DimensionUtilities.doDimensionTransfer((EntityLivingBase) target, 0);
            }

        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
