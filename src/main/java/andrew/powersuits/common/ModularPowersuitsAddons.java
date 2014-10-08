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
        ClientProxy.loadCurrentLanguage();
        AddonConfig.loadOptions();
        proxy.registerHandlers();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
        AddonRecipeManager.cheatyLeather();

        if (Loader.isModLoaded("appliedenergistics2")) {
            TerminalHandler.registerHandler();
        }

    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        AddonRecipeManager.addRecipes();
        AddonConfig.loadPowerModules();

        AddonConfig.getConfig().save();


    }

}