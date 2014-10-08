package andrew.powersuits.modules;

import appeng.api.AEApi;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Eximius88 on 1/13/14.
 */
public class TerminalHandler implements IWirelessTermHandler {


    @Override
    public boolean canHandle(ItemStack is) {
        return true;
    }

    @Override
    public boolean usePower(EntityPlayer entityPlayer, double v, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean hasPower(EntityPlayer entityPlayer, double v, ItemStack itemStack) {
        return false;
    }

    @Override
    public IConfigManager getConfigManager(ItemStack itemStack) {
        return null;
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        {
            if (item == null) {
                return null;
            }
            NBTTagCompound tag = openNbtData(item);
            if (tag != null) {
                return tag.getString("encKey");
            }
            return null;
        }
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        {
            if (item == null) {
                return;
            }
            NBTTagCompound tag = openNbtData(item);
            if (tag != null) {
                tag.setString("encKey", encKey);
            }
        }
    }

    public static void registerHandler() {
        if (Loader.isModLoaded("appliedenergistics2")) {
            AEApi.instance().registries().wireless().registerWirelessHandler(new TerminalHandler());
            System.out.println("MPSA: Registering AE Terminal Handler :MPSA");

        }

    }

    public static NBTTagCompound openNbtData(ItemStack item) {
        NBTTagCompound compound = item.getTagCompound();
        if (compound == null) {
            item.setTagCompound(compound = new NBTTagCompound());
        }
        return compound;
    }

}
