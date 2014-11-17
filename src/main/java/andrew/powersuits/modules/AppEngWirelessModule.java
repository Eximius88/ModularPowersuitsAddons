package andrew.powersuits.modules;

import appeng.api.AEApi;
import appeng.items.tools.powered.ToolWirelessTerminal;
import cpw.mods.fml.common.registry.GameRegistry;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 1:42 AM 6/22/13
 */
public class AppEngWirelessModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_APPENG_WIRELESS = "AppEng Wireless Terminal";
    private ItemStack wirelessTerminal;


    public AppEngWirelessModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        wirelessTerminal = AEApi.instance().items().itemWirelessTerminal.stack(1);
        addInstallCost(wirelessTerminal);

    }

    @Override
    public String getTextureFile() {
        return "ItemWirelessTerminal";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_APPENG_WIRELESS;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.appengWireless.name");
    }

    @Override
    public String getDescription() {
        return "An Applied Energistics wireless terminal integrated into your power tool.";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {

        AEApi.instance().registries().wireless().OpenWirelessTermainlGui(item, world, player);
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
