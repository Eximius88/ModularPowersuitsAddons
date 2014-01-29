package andrew.powersuits.modules;

import appeng.api.IWirelessTermHandler;
import appeng.api.Util;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.Player;
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
    public boolean usePower(Player player, float amount, ItemStack is) {
        return true;
    }

    @Override
    public boolean hasPower(Player player, ItemStack is) {
        return true;
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

    public static void registerHandler(){
        if(Loader.isModLoaded("AppliedEnergistics")){
            Util.getWirelessTermRegistery().registerWirelessHandler(new TerminalHandler());

        }

    }

    public static NBTTagCompound openNbtData(ItemStack item)
    {
        NBTTagCompound compound = item.getTagCompound();
        if (compound == null) {
            item.setTagCompound(compound = new NBTTagCompound());
        }
        return compound;
    }

}
