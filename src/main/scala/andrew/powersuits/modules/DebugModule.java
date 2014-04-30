package andrew.powersuits.modules;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.energy.BasicBatteryModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by JBS_INTERACTIVE_LOU on 4/29/2014.
 */
public class DebugModule extends PowerModuleBase implements IRightClickModule {

    public static final String MODULE_DEBUG = "Debug Module";
    public static final String ENERGY_GEN = "Energy Generation";
    public static final String DEBUG_DESCRIPTION = "Just a Debug Module to make testing easier. If you happen to find this it is NOT meant to be used regularly, as it will most likely mess things up";
    public EntityPlayer player = Minecraft.getMinecraft().thePlayer;

    public DebugModule(List<IModularItem> validItems){
        super(validItems);

        addBaseProperty(ENERGY_GEN, 4250000);


    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
        ElectricItemUtils.givePlayerEnergy(playerClicking, 4250000);

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

    @Override
    public String getTextureFile() {
        return "crystalsphere";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_DEBUG;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.debugModule.name");
    }

    @Override
    public String getDescription() {
        return DEBUG_DESCRIPTION;
    }
}
