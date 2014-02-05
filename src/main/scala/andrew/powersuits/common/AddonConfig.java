package andrew.powersuits.common;

import andrew.powersuits.modules.*;
import cpw.mods.fml.common.Loader;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraftforge.common.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AddonConfig extends Config {


    public static String category = "Modular Powersuits Addons";

    public static BlockTorch torch;

    public static File configFolder;
    private static Configuration config;

    public static boolean useAdvancedOreScannerMessage;
    public static boolean useOldAutoFeeder;
    public static boolean useCheatyLeatherRecipe;
    public static boolean useHUDStuff;
    public static boolean useDebugMode;
    public static boolean use24hClock;

    public static int manualID;

    public static void init(Configuration config) {
        AddonConfig.config = config;
        config.load();


        config.save();
    }

    public static void addModule(IPowerModule module) {
        ModuleManager.addModule(module);
    }


    public static void loadPowerModules() {
        List<IModularItem> ARMORONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso, ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet);
        List<IModularItem> ALLITEMS = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso, ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet, ModularPowersuits.powerTool);
        List<IModularItem> HEADONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead);
        List<IModularItem> TORSOONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorTorso);
        List<IModularItem> LEGSONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorLegs);
        List<IModularItem> FEETONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorFeet);
        List<IModularItem> TOOLONLY = Arrays.asList((IModularItem) ModularPowersuits.powerTool);

        addModule(new InPlaceAssemblerModule(TOOLONLY));
        addModule(new KineticGeneratorModule(LEGSONLY));
        addModule(new SolarGeneratorModule(HEADONLY));
        addModule(new AutoFeederModule(HEADONLY));
        addModule(new MagnetModule(TORSOONLY));
        addModule(new OreScannerModule(TOOLONLY));
        addModule(new LeafBlowerModule(TOOLONLY));
        addModule(new ThermalGeneratorModule(TORSOONLY));
        addModule(new MobRepulsorModule(TORSOONLY));
        addModule(new FlintAndSteelModule(TOOLONLY));
        addModule(new ClockModule(HEADONLY));
        addModule(new CompassModule(HEADONLY));
        addModule(new LightningModule(TOOLONLY));
        addModule(new WaterTankModule(TORSOONLY));
        //addModule(new BucketModule(TOOLONLY));
        addModule(new DimensionalRiftModule(TOOLONLY));
        addModule(new AdvancedSolarGenerator(HEADONLY));
        //addModule(new CoalGenerator(TORSOONLY));
        addModule(new NitrogenCoolingSystem(TORSOONLY));
        addModule(new MechanicalAssistance(TORSOONLY));


        if (Loader.isModLoaded("AppliedEnergistics")) {
            addModule(new AppEngWirelessModule(TOOLONLY));

        }

        if (ModCompatability.isIndustrialCraftLoaded()) {
            addModule(new EUReaderModule(TOOLONLY));
            addModule(new TreetapModule(TOOLONLY));
        }

        if (ModCompatability.isThermalExpansionLoaded()) {
            addModule(new TEMultimeterModule(TOOLONLY));
        }

        try {
            torch = (BlockTorch) Block.blocksList[Block.torchWood.blockID];
            addModule(new TorchPlacerModule(TOOLONLY));
        } catch (Exception e) {
            AddonLogger.logError("Some mod is overriding the default torch. MPSA Torch Placer Module is being disabled.");
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void setConfigFolderBase(File folder) {
        configFolder = new File(folder.getAbsolutePath() + "/machinemuse/andrew");
    }

    public static void initItems() {
        //manualID = getConfig().getItem("MPS Manual", 24770).getInt();
    }

    public static void loadOptions() {
        useAdvancedOreScannerMessage = getConfig().get(category, "Use Detailed Ore Scanner Message", true).getBoolean(true);
        useOldAutoFeeder = getConfig().get(category, "Use Old Auto Feeder Method", false).getBoolean(false);
        useCheatyLeatherRecipe = getConfig().get(category, "Use Cheaty Leather Recipe (Requires Thermal Expansion)", true).getBoolean(true);
        useHUDStuff = getConfig().get(category, "Use HUD for certain modules (Auto Feeder, Torch Placer, Compass, Clock, etc.", true).getBoolean(true);
        useDebugMode = getConfig().get(category, "Use Debug mode. WARNING: WILL PROBABLY SPAM YOUR CONSOLE", false).getBoolean(false);
        use24hClock = getConfig().get(category, "Use a 24h clock instead of 12h", false).getBoolean(false);
    }

    public static String getNetworkChannelName() {
        return "psa";
    }


}