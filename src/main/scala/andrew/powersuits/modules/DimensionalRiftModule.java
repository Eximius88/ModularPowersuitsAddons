package andrew.powersuits.modules;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 5:41 PM 4/30/13
 */
public class DimensionalRiftModule extends PowerModuleBase implements IPlayerTickModule {
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
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
