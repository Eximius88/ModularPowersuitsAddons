package andrew.powersuits.modules;

import andrew.powersuits.common.AddonConfig;
import andrew.powersuits.common.AddonUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.StatCollector;

import java.util.List;

public class AutoFeederModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_AUTO_FEEDER = "Auto-Feeder";
    public static final String EATING_ENERGY_CONSUMPTION = "Eating Energy Consumption";
    public static final String EATING_EFFICIENCY = "Auto-Feeder Efficiency";

    public AutoFeederModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(EATING_ENERGY_CONSUMPTION, 100);
        addBaseProperty(EATING_EFFICIENCY, 50);
        addTradeoffProperty("Efficiency", EATING_ENERGY_CONSUMPTION, 100);
        addTradeoffProperty("Efficiency", EATING_EFFICIENCY, 50);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getTextureFile() {
        return "autofeeder";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MODULE_AUTO_FEEDER;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.autoFeeder.name");
    }

    @Override
    public String getDescription() {
        return "Whenever you're hungry, this module will grab the bottom-left-most food item from your inventory and feed it to you, storing the rest for later.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (AddonConfig.useOldAutoFeeder) {
            IInventory inv = player.inventory;
            double foodLevel = (double) AddonUtils.getFoodLevel(item);
            double saturationLevel = AddonUtils.getSaturationLevel(item);
            double efficiency = ModuleManager.computeModularProperty(item, EATING_EFFICIENCY);
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    for (int a = 0; a < stack.stackSize; a++) {
                        foodLevel += food.getHealAmount();
                        saturationLevel += food.getSaturationModifier();
                    }
                    foodLevel = foodLevel * efficiency / 100.0;
                    saturationLevel = saturationLevel * efficiency / 100.0;
                    AddonUtils.setFoodLevel(item, foodLevel);
                    AddonUtils.setSaturationLevel(item, saturationLevel);
                    player.inventory.setInventorySlotContents(i, null);
                }
            }
            double eatingEnergyConsumption = ModuleManager.computeModularProperty(item, EATING_ENERGY_CONSUMPTION);
            FoodStats foodStats = player.getFoodStats();
            int foodNeeded = 20 - foodStats.getFoodLevel();
            if (foodNeeded > 0 && (eatingEnergyConsumption * foodNeeded < ElectricItemUtils.getPlayerEnergy(player)) && AddonUtils.getFoodLevel(item) > foodNeeded) {
                if (AddonUtils.getSaturationLevel(item) >= 1.0F) {
                    foodStats.addStats(foodNeeded, 1.0F);
                    AddonUtils.setSaturationLevel(item, AddonUtils.getSaturationLevel(item) - 1.0F);
                } else {
                    foodStats.addStats(foodNeeded, 0.0F);
                }
                AddonUtils.setFoodLevel(item, AddonUtils.getFoodLevel(item) - foodNeeded);
                ElectricItemUtils.drainPlayerEnergy(player, eatingEnergyConsumption * foodNeeded);
            }
        } else {
            double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
            IInventory inv = player.inventory;
            double foodLevel = (double) AddonUtils.getFoodLevel(item);
            double saturationLevel = AddonUtils.getSaturationLevel(item);
            double efficiency = ModuleManager.computeModularProperty(item, EATING_EFFICIENCY);
            FoodStats foodStats = player.getFoodStats();
            int foodNeeded = 20 - foodStats.getFoodLevel();
            for (int i = 0; i < inv.getSizeInventory() && foodNeeded > foodLevel; i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    for (; stack.stackSize > 0 && foodNeeded > foodLevel; stack.stackSize--) {
                        foodLevel += food.getHealAmount() * efficiency / 100.0;
                        saturationLevel += food.getSaturationModifier() * efficiency / 100.0;
                    }
                    if (stack.stackSize == 0) {
                        player.inventory.setInventorySlotContents(i, null);
                    }
                }
            }
            double eatingEnergyConsumption = foodNeeded * ModuleManager.computeModularProperty(item, EATING_ENERGY_CONSUMPTION);
            int foodConsumed = (int) Math.min(foodNeeded, Math.min(foodLevel, eatingEnergyConsumption * totalEnergy));
            if (foodConsumed > 0) {
                if (saturationLevel >= 1.0F) {
                    foodStats.addStats(foodConsumed, 1.0F);
                    saturationLevel -= 1.0F;
                } else {
                    foodStats.addStats(foodConsumed, 0.0F);
                }
                foodLevel -= foodConsumed;
                ElectricItemUtils.drainPlayerEnergy(player, eatingEnergyConsumption * foodConsumed);
            }
            AddonUtils.setFoodLevel(item, foodLevel);
            AddonUtils.setSaturationLevel(item, saturationLevel);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
