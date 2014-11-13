package andrew.powersuits.modules;

import java.util.HashMap;
import java.util.Set;

import appeng.api.AEApi;
import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import net.machinemuse.utils.ElectricItemUtils;
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
    	if(is == null)
    		return false;
    	if(is.getUnlocalizedName() == null)
    		return false;
    	if(is.getUnlocalizedName().equals("item.powerFist"))
    		return true;
    	return false;
    }

    @Override
    public boolean usePower(EntityPlayer entityPlayer, double v, ItemStack itemStack) {
        boolean ret = false;
        if (( v * 5 ) < ( ElectricItemUtils.getPlayerEnergy( entityPlayer ) * 5 ))
        {
            ElectricItemUtils.drainPlayerEnergy(entityPlayer, ( v * 5 ) );
            ret = true;
        }

        return ret;
    }

    @Override
    public boolean hasPower(EntityPlayer entityPlayer, double v, ItemStack itemStack) {
        return (( v * 5 ) < ( ElectricItemUtils.getPlayerEnergy(entityPlayer) * 5 ));
    }

    @Override
    public IConfigManager getConfigManager(ItemStack itemStack) {
        IConfigManager config = new WirelessConfig(itemStack);
        config.registerSetting( Settings.SORT_BY, SortOrder.NAME );
		config.registerSetting( Settings.VIEW_MODE, ViewItems.ALL );
		config.registerSetting( Settings.SORT_DIRECTION, SortDir.ASCENDING );

		config.readFromNBT(itemStack.getTagCompound());
        return config;
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
    
    class WirelessConfig implements IConfigManager{

    	HashMap<Enum, Enum> enums = new HashMap<Enum, Enum>();
    	
    	final ItemStack stack;
    	
		public WirelessConfig(ItemStack itemStack) {
			stack = itemStack;
		}

		@Override
		public Enum getSetting(Enum arg0) {
			if(enums.containsKey(arg0)){
				return enums.get(arg0);
			}
			return null;
		}

		@Override
		public Set<Enum> getSettings() {
			return enums.keySet();
		}

		@Override
		public Enum putSetting(Enum arg0, Enum arg1) {
			enums.put(arg0, arg1);
			writeToNBT(stack.getTagCompound());
			return arg1;
		}

		@Override
		public void registerSetting(Enum arg0, Enum arg1) {
			if(!enums.containsKey(arg0)){
				putSetting(arg0, arg1);
			}
			
		}

		@Override
		public void readFromNBT(NBTTagCompound tagCompound)
		{
			NBTTagCompound tag = null;
			if(tagCompound.hasKey("configWirelessTerminal")){
				tag = tagCompound.getCompoundTag("configWirelessTerminal");
			}
			
			if(tag == null)
				return;
			
			for (Enum key : enums.keySet())
			{
				try
				{
					if ( tag.hasKey( key.name() ) )
					{
						String value = tag.getString( key.name() );

						// Provides an upgrade path for the rename of this value in the API between rv1 and rv2
						//TODO implement on rv2 update
						/*if( value.equals( "EXTACTABLE_ONLY" ) ){
							value = StorageFilter.EXTRACTABLE_ONLY.toString();
						} else if( value.equals( "STOREABLE_AMOUNT" ) ) {
							value = LevelEmitterMode.STORABLE_AMOUNT.toString();
						}*/

						Enum oldValue = enums.get( key );

						Enum newValue = Enum.valueOf( oldValue.getClass(), value );

						putSetting( key, newValue );
					}
				}
				catch (IllegalArgumentException e)
				{
					
				}
			}
		}

		
		@Override
		public void writeToNBT(NBTTagCompound tagCompound)
		{
			NBTTagCompound tag = new NBTTagCompound();
			if(tagCompound.hasKey("configWirelessTerminal")){
				tag = tagCompound.getCompoundTag("configWirelessTerminal");
			}

			for (Enum e : enums.keySet())
			{
				tag.setString( e.name(), enums.get( e ).toString() );
			}
			tagCompound.setTag("configWirelessTerminal", tag);

		}
    	
    }

}
