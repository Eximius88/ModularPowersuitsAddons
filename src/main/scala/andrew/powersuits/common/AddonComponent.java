package andrew.powersuits.common;

import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.item.ItemStack;

public class AddonComponent {
    public static ItemStack magnet;
    public static ItemStack solarPanel;
    public static ItemStack computerChip;
    public static ItemStack liquidNitrogen;
    public static ItemStack rubberHose;
<<<<<<< HEAD
    public static ItemStack rawCarbon;
=======
>>>>>>> 7c07ccff844f95d9e90237f1c812434cfd66d4bb

    public static void populate() {
        if (ModularPowersuits.components != null) {
            solarPanel = ModularPowersuits.components.addComponent("componentSolarPanel", "A light sensitive device that will generate electricity from the sun.", "solarpanel");
            magnet = ModularPowersuits.components.addComponent("componentMagnet", "A metallic device that generates a magnetic field which pulls items towards the player.", "magnetb");
            computerChip = ModularPowersuits.components.addComponent("componentComputerChip", "An upgraded control circuit that contains a CPU which is capable of more advanced calculations.", "computerchip");
            rubberHose = ModularPowersuits.components.addComponent("componentRubberHose", "A heavily insulated rubber hose capable of withstanding extreme heat or cold", "rubberhose");
            liquidNitrogen = ModularPowersuits.components.addComponent("componentLiquidNitrogen", "A bucket of Liquid Nitrogen", "liquidnitrogen");
<<<<<<< HEAD
            rawCarbon = ModularPowersuits.components.addComponent("componentRawCarbon", "Raw carbon material used to make carbon myofiber", "carbon");
=======
>>>>>>> 7c07ccff844f95d9e90237f1c812434cfd66d4bb
        } else {
            AddonLogger.logError("MPS components were not initialized, MPSA componenets will not be activated.");
        }
    }

}