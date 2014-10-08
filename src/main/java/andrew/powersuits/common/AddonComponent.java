package andrew.powersuits.common;

import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.item.ItemStack;

public class AddonComponent {
    public static ItemStack magnet;
    public static ItemStack solarPanel;
    public static ItemStack computerChip;
    public static ItemStack liquidNitrogen;
    public static ItemStack rubberHose;

    public static void populate() {
        if (MPSItems.components() != null) {
            solarPanel = MPSItems.components().addComponent("componentSolarPanel", "A light sensitive device that will generate electricity from the sun.", "solarpanel");
            magnet = MPSItems.components().addComponent("componentMagnet", "A metallic device that generates a magnetic field which pulls items towards the player.", "magnetb");
            computerChip = MPSItems.components().addComponent("componentComputerChip", "An upgraded control circuit that contains a CPU which is capable of more advanced calculations.", "computerchip");
            rubberHose = MPSItems.components().addComponent("componentRubberHose", "A heavily insulated rubber hose capable of withstanding extreme heat or cold", "rubberhose");
            liquidNitrogen = MPSItems.components().addComponent("componentLiquidNitrogen", "A bucket of Liquid Nitrogen", "liquidnitrogen");
        } else {
            AddonLogger.logError("MPS components were not initialized, MPSA componenets will not be activated.");
        }
    }

}