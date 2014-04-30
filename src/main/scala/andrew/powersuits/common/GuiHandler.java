package andrew.powersuits.common;

import andrew.powersuits.client.ClientProxy;
//import andrew.powersuits.client.ManualGui;
import andrew.powersuits.client.PortableCraftingGui;
import andrew.powersuits.container.PortableCraftingContainer;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    public static int craftingGuiID = 0;
    public static int manualGuiID = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new PortableCraftingContainer(player.inventory, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new PortableCraftingGui(player, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            /*case 1:
                ItemStack stack = player.getCurrentEquippedItem();
                return new ManualGui(stack, ClientProxy.manual);*/
            default:
                return null;
        }
    }

}
