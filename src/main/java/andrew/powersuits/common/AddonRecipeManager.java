package andrew.powersuits.common;



import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class AddonRecipeManager {

    public static void cheatyLeather() {
        if (AddonConfig.useCheatyLeatherRecipe && ModCompatability.isThermalExpansionLoaded()) {

            NBTTagCompound toSend = new NBTTagCompound();
            toSend.setInteger("energy", 1350);
            toSend.setTag("input", new NBTTagCompound());
            toSend.setTag("output", new NBTTagCompound());

            new ItemStack(Items.rotten_flesh).writeToNBT(toSend.getCompoundTag("input"));
            new ItemStack(Items.leather).writeToNBT(toSend.getCompoundTag("output"));
            FMLInterModComms.sendMessage("ThermalExpansion", "FurnaceRecipe", toSend);
        }
    }


    public static void addRecipes() {
        ItemStack circuit = ItemComponent.wiring;
        ItemStack glass = new ItemStack(Blocks.glass);
        ItemStack glassPane = new ItemStack(Blocks.glass_pane);
        ItemStack lapis = new ItemStack(Items.dye, 1, 4);
        ItemStack lapisBlock = new ItemStack(Blocks.lapis_block);
        ItemStack ironIngot = new ItemStack(Items.iron_ingot);
        ItemStack redstoneBlock = new ItemStack(Blocks.redstone_block);
        ItemStack diamond = new ItemStack(Items.diamond);
        ItemStack comparator = new ItemStack(Items.comparator);
        ItemStack goldIngot = new ItemStack(Items.gold_ingot);
        ItemStack leather = new ItemStack(Items.leather);
        ItemStack rottenFlesh = new ItemStack(Items.rotten_flesh);
        ItemStack sugar = new ItemStack(Items.sugar);


        if (ModCompatability.vanillaRecipesEnabled()) {

            //===========================================================================================================================
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.solarPanel, true, "PPP", "PLP", "PPP", 'P', glassPane, 'L', lapisBlock));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.magnet, true, "III", "SSS", "III", 'I', ironIngot, 'S', ItemComponent.solenoid));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.computerChip, true, "RCR", "GDG", 'R', redstoneBlock, 'C', comparator, 'G', goldIngot, 'D', diamond));
            //===========================================================================================================================
        }

        if (ModCompatability.UERecipesEnabled() && ModCompatability.isBasicComponentsLoaded()) {
            String basicCircuit = "basicCircuit";
            String advancedCircuit = "advancedCircuit";
            String eliteCircuit = "eliteCircuit";

            //===========================================================================================================================
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.solarPanel, true, "GGG", "CLC", "SSS", 'G', glass, 'C', basicCircuit, 'L', lapisBlock, 'S', "plateSteel"));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.magnet, "ICI", "SSS", "ICI", 'I', ironIngot, 'C', advancedCircuit, 'S', ItemComponent.solenoid));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.computerChip, "CRC", "GDG", 'C', eliteCircuit, 'R', redstoneBlock, 'G', goldIngot, 'D', diamond));
            //===========================================================================================================================
        }

        if (ModCompatability.IC2RecipesEnabled() && ModCompatability.isIndustrialCraftLoaded()) {
            circuit = ModCompatability.getIC2Item("electronicCircuit");
            ItemStack advCircuit = ModCompatability.getIC2Item("advancedCircuit");
            String refIron = "ingotRefinedIron";
            String tin = "ingotTin";
            String copper = "ingotCopper";
            ItemStack reBattery = ModCompatability.getIC2Item("reBattery");
            ItemStack fullBattery = ModCompatability.getIC2Item("chargedReBattery");
            ItemStack energyCrystal = ModCompatability.getIC2Item("energyCrystal");
            ItemStack lapotronCrystal = ModCompatability.getIC2Item("lapotronCrystal");
            ItemStack iridiumOre = ModCompatability.getIC2Item("iridiumOre");
            ItemStack carbonPlate = ModCompatability.getIC2Item("carbonPlate");
            ItemStack machine = ModCompatability.getIC2Item("machine");
            ItemStack advMachine = ModCompatability.getIC2Item("advancedMachine");
            ItemStack gen = ModCompatability.getIC2Item("generator");
            ItemStack ovscanner = ModCompatability.getIC2Item("ovScanner");
            ovscanner.setItemDamage(1);
            ItemStack goldCable = ModCompatability.getIC2Item("insulatedGoldCableItem");

            //===========================================================================================================================
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.solarPanel, true, "LGL", "GLG", "CBC", 'L', lapis, 'G', glass, 'C', circuit, 'B', gen));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.magnet, "ICI", "SSS", "ICI", 'I', ironIngot, 'C', advCircuit, 'S', ItemComponent.solenoid));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.computerChip, "CSC", "GGG", 'C', advCircuit, 'S', ovscanner, 'G', goldCable));
            //===========================================================================================================================
        }

        if (ModCompatability.GregTechRecipesEnabled() && ModCompatability.isIndustrialCraftLoaded() && ModCompatability.isGregTechLoaded()) {
            String energyFlowCircuit = "craftingCircuitTier07";
            String superConductor = "craftingSuperconductor";
            String dataStorageCircuit = "craftingCircuitTier05";
            ItemStack reinforcedGlass = ModCompatability.getIC2Item("reinforcedGlass");
            ItemStack advCircuit = ModCompatability.getIC2Item("advancedCircuit");
            ItemStack ovscanner = ModCompatability.getIC2Item("ovScanner");
            ovscanner.setItemDamage(1);

            //===========================================================================================================================
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.solarPanel, true, "GGG", "PCP", 'G', reinforcedGlass, 'P', "plateAlloyIridium", 'C', energyFlowCircuit));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.magnet, true, "ICI", "SSS", "ICI", 'I', "ingotTitanium", 'C', superConductor, 'S', ItemComponent.solenoid));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.computerChip, true, "ADA", "DOD", "ADA", 'A', advCircuit, 'D', dataStorageCircuit, 'O', ovscanner));
            //===========================================================================================================================
        }

        if (ModCompatability.ThermalExpansionRecipesEnabled() && ModCompatability.isThermalExpansionLoaded()) {
            ItemStack pneumaticServo = GameRegistry.findItemStack("ThermalExpansion", "pneumaticServo", 1);
            ItemStack machineFrame = GameRegistry.findItemStack("ThermalExpansion", "machineFrame", 1);
            ItemStack powerCoilGold = GameRegistry.findItemStack("ThermalExpansion", "powerCoilGold", 1);
            ItemStack powerCoilSilver = GameRegistry.findItemStack("ThermalExpansion", "powerCoilSilver", 1);
            ItemStack powerCoilElectrum = GameRegistry.findItemStack("ThermalExpansion", "powerCoilElectrum", 1);
            ItemStack gearCopper = GameRegistry.findItemStack("ThermalExpansion", "gearCopper", 1);
            ItemStack gearTin = GameRegistry.findItemStack("ThermalExpansion", "gearTin", 1);
            ItemStack gearInvar = GameRegistry.findItemStack("ThermalExpansion", "gearInvar", 1);
            ItemStack compressedSawdust = GameRegistry.findItemStack("ThermalExpansion", "sawdustCompressed", 1);
            ItemStack energyFrameFull = GameRegistry.findItemStack("ThermalExpansion", "energyFrameFull", 1);
            ItemStack conduitEnergy = GameRegistry.findItemStack("ThermalExpansion", "conduitEnergyReinforcedEmpty", 1);
            ItemStack teleportFrameFull = GameRegistry.findItemStack("ThermalExpansion", "teleportBase", 1);
            ItemStack multimeter = GameRegistry.findItemStack("ThermalExpansion", "multimeter", 1);

            //===========================================================================================================================
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.solarPanel, "GGG", "CLC", " E ", 'G', glass, 'L', lapisBlock, 'C', powerCoilElectrum, 'E', powerCoilSilver));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.magnet, "ICI", "SSS", "ICI", 'I', ironIngot, 'C', powerCoilGold, 'S', ItemComponent.solenoid));
            GameRegistry.addRecipe(new ShapedOreRecipe(AddonComponent.computerChip, " O ", "GMS", " E ", 'E', powerCoilElectrum, 'S', powerCoilSilver, 'G', powerCoilGold, 'M', multimeter, 'O', ItemComponent.solenoid));
            //===========================================================================================================================
        }


    }

}