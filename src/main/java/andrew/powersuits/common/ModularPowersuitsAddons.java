package andrew.powersuits.common;


import andrew.powersuits.client.ClientProxy;
import andrew.powersuits.modules.TerminalHandler;
import andrew.powersuits.network.MPSAPacketHandler;
//import appeng.api.Util;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.init.Localization;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Mod(modid = ModularPowersuitsAddons.modid, name = "Andrew's Modular Powersuits Addons", version = "@VERSION@", dependencies = "required-after:powersuits; required-after:numina")


public class ModularPowersuitsAddons {
    @SidedProxy(clientSide = "andrew.powersuits.client.ClientProxy", serverSide = "andrew.powersuits.common.CommonProxy")
    public static CommonProxy proxy;

    @Instance("PowersuitAddons")
    public static ModularPowersuitsAddons INSTANCE;

    public final static String modid = "powersuitaddons";
    public static final String CHANNEL = "psa";
    public static FMLEventChannel packetHandler;


    public static GuiHandler guiHandler = new GuiHandler();


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        packetHandler = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL);

        INSTANCE = this;
        File newConfig = new File(event.getModConfigurationDirectory() + "/machinemuse/andrew/PowersuitsAddons.cfg");
        AddonConfig.init(new Configuration(newConfig));
        AddonConfig.setConfigFolderBase(event.getModConfigurationDirectory());



        AddonConfig.initItems();
        proxy.registerRenderers();
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        packetHandler.register(new MPSAPacketHandler());
        AddonComponent.populate();

        if (!AddonUtils.isServerSide()) {
            System.out.println("MPSA: Loading Localization");
            ClientProxy.loadCurrentLanguage();
        }
        AddonConfig.loadOptions();
        proxy.registerHandlers();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
        AddonRecipeManager.cheatyLeather();

        if (Loader.isModLoaded("appliedenergistics2")) {
        	String version = null;
        	try{
        		version = ((String) Class.forName("appeng.core.AEConfig").getField("VERSION").get(null)).toLowerCase();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	if(version == null){
        		System.err.println("MSPA: Can't check AE version :MSPA");
        	}else if(version.contains("rv1")){
        		AddonConfig.aeVersion = AddonConfig.AEVersion.Rv1;
        		TerminalHandler.registerHandler();
        	}else if(version.contains("rv2")){
        		AddonConfig.aeVersion = AddonConfig.AEVersion.Rv2;
        		TerminalHandler.registerHandler();
        	}else{
        		System.out.println("MPSA: This version of MPSA is not coompatible with the version of AE2 :MPSA");
        	}
        }

    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AddonRecipeManager.addRecipes();
        AddonConfig.loadPowerModules();

        AddonConfig.getConfig().save();


    }

}