package andrew.powersuits.tick;

import java.util.ArrayList;
import java.util.EnumSet;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import andrew.powersuits.client.WaterMeter;
import andrew.powersuits.common.AddonConfig;
import andrew.powersuits.common.AddonUtils;
import andrew.powersuits.common.AddonWaterUtils;
import andrew.powersuits.modules.AutoFeederModule;
import andrew.powersuits.modules.ClockModule;
import andrew.powersuits.modules.CompassModule;
import andrew.powersuits.modules.TorchPlacerModule;
import andrew.powersuits.modules.WaterTankModule;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class RenderTickHandler implements ITickHandler {

    static double yBaseIcon;
    static int yBaseString;

    static {
        if (Config.useGraphicalMeters()) {
            yBaseIcon = 150.0;
            yBaseString = 155;
        } else {
            yBaseIcon = 26.0;
            yBaseString = 32;
        }
    }

    double yOffsetIcon = 16.0;
    int yOffsetString = 18;
    String ampm = "";

    ArrayList<String> modules;

    protected static WaterMeter water;

    ItemStack food = new ItemStack(Item.beefCooked);
    ItemStack torch = new ItemStack(Block.torchWood);
    ItemStack clock = new ItemStack(Item.pocketSundial);
    ItemStack compass = new ItemStack(Item.compass);

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        modules = new ArrayList<String>();
        findInstalledModules(player);
        if (player != null && MuseItemUtils.modularItemsEquipped(player).size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
            for (int i = 0; i < modules.size(); i++) {
                if (modules.get(i).equals(AutoFeederModule.MODULE_AUTO_FEEDER)) {
                    int foodLevel = (int) AddonUtils.getFoodLevel(player.getCurrentArmor(3));
                    String num = MuseStringUtils.formatNumberShort(foodLevel);
                    if (i == 0) {
                        MuseRenderer.drawString(num, 17, yBaseString);
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon, food);
                    } else {
                        MuseRenderer.drawString(num, 17, yBaseString + (yOffsetString * i));
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), food);
                    }
                } else if (modules.get(i).equals(TorchPlacerModule.MODULE_TORCH_PLACER)) {
                    int torchLevel = AddonUtils.getTorchLevel(player.getCurrentEquippedItem());
                    int maxTorchLevel = (int) ModuleManager.computeModularProperty(player.getCurrentEquippedItem(), TorchPlacerModule.MAX_TORCH_STORAGE);
                    String num = MuseStringUtils.formatNumberShort(torchLevel) + "/" + MuseStringUtils.formatNumberShort(maxTorchLevel);
                    if (i == 0) {
                        MuseRenderer.drawString(num, 17, yBaseString);
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon, torch);
                    } else {
                        MuseRenderer.drawString(num, 17, yBaseString + (yOffsetString * i));
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), torch);
                    }
                } else if (modules.get(i).equals(ClockModule.MODULE_CLOCK)) {
                    long time = player.worldObj.provider.getWorldTime();
                    int hour = (int) ((time % 24000) / 1000);
                    if (AddonConfig.use24hClock) {
                        if (hour < 19) {
                            hour += 6;
                        } else {
                            hour -= 18;
                        }
                        ampm = "h";
                    } else {
                        if (hour < 6) {
                            hour += 6;
                            ampm = " AM";
                        } else if (hour == 6) {
                            hour = 12;
                            ampm = " PM";
                        } else if (hour > 6 && hour < 18) {
                            hour -= 6;
                            ampm = " PM";
                        } else if (hour == 18) {
                            hour = 12;
                            ampm = " AM";
                        } else {
                            hour -= 18;
                            ampm = " AM";
                        }
                    }
                    if (i == 0) {
                        MuseRenderer.drawString(hour + ampm, 17, yBaseString);
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon, clock);
                    } else {
                        MuseRenderer.drawString(hour + ampm, 17, yBaseString + (yOffsetString * i));
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), clock);
                    }
                } else if (modules.get(i).equals(CompassModule.MODULE_COMPASS)) {
                    if (i == 0) {
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon, compass);
                    } else {
                        MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), compass);
                    }
                } else if (modules.get(i).equals(WaterTankModule.MODULE_WATER_TANK)) {
                    Minecraft mc = Minecraft.getMinecraft();
                    ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                    drawMeters(player, screen);
                }
            }
        }
    }

    public void findInstalledModules(EntityClientPlayerMP player) {
        if (player != null) {
            ItemStack tool = player.getCurrentEquippedItem();
            if (tool != null && tool.getItem() instanceof ItemPowerFist) {
                if (ModuleManager.itemHasActiveModule(tool, TorchPlacerModule.MODULE_TORCH_PLACER)) {
                    modules.add(TorchPlacerModule.MODULE_TORCH_PLACER);
                }
            }
            ItemStack helmet = player.getCurrentArmor(3);
            if (helmet != null && helmet.getItem() instanceof ItemPowerArmorHelmet) {
                if (ModuleManager.itemHasActiveModule(helmet, AutoFeederModule.MODULE_AUTO_FEEDER)) {
                    modules.add(AutoFeederModule.MODULE_AUTO_FEEDER);
                }
                if (ModuleManager.itemHasActiveModule(helmet, ClockModule.MODULE_CLOCK)) {
                    modules.add(ClockModule.MODULE_CLOCK);
                }
                if (ModuleManager.itemHasActiveModule(helmet, CompassModule.MODULE_COMPASS)) {
                    modules.add(CompassModule.MODULE_COMPASS);
                }
            }
            ItemStack chest = player.getCurrentArmor(2);
            if (chest != null && chest.getItem() instanceof ItemPowerArmorChestplate) {
                if (ModuleManager.itemHasActiveModule(chest, WaterTankModule.MODULE_WATER_TANK)) {
                    modules.add(WaterTankModule.MODULE_WATER_TANK);
                }
            }
        }
    }

    private void drawMeters(EntityPlayer player, ScaledResolution screen) {
        double currWater = AddonWaterUtils.getPlayerWater(player);
        double maxWater = AddonWaterUtils.getMaxWater(player);
        String currStr = MuseStringUtils.formatNumberShort(currWater);
        String maxStr = MuseStringUtils.formatNumberShort(maxWater);
        if (Config.useGraphicalMeters()) {
            if (water == null) {
                water = new WaterMeter();
            }
            double left = screen.getScaledWidth() - 12;
            double top = (screen.getScaledHeight() / 2.0 - 16) + 40;
            water.draw(left, top, currWater / maxWater);
            MuseRenderer.drawRightAlignedString(currStr, left - 2, top + 15); //10
        } else {
            MuseRenderer.drawString(currStr + '/' + maxStr + " buckets", 1, 19);
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return "MPSA: Render Tick";
    }
}
