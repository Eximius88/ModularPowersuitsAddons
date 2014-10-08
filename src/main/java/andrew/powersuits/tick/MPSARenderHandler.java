package andrew.powersuits.tick;

import andrew.powersuits.client.WaterMeter;
import andrew.powersuits.common.AddonConfig;
import andrew.powersuits.common.AddonUtils;
import andrew.powersuits.common.AddonWaterUtils;
import andrew.powersuits.modules.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;

/**
 * Created by Shawn on 10/7/2014.
 */
public class MPSARenderHandler {

    private Minecraft mc;

    public MPSARenderHandler(Minecraft mc) {
        this.mc = mc;
    }

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

    ItemStack food = new ItemStack(Items.cooked_beef);
    ItemStack torch = new ItemStack(Blocks.torch);
    ItemStack clock = new ItemStack(Items.clock);
    ItemStack compass = new ItemStack(Items.compass);

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
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
                    ScaledResolution screen = new ScaledResolution(Minecraft.getMinecraft(), mc.displayWidth, mc.displayHeight);
                    drawMeters(player, screen);
                }
            }
        }
    }

    public void findInstalledModules(EntityClientPlayerMP player) {
        if (player != null) {
            ItemStack tool = player.getCurrentEquippedItem();
            if (tool != null && tool.getItem() instanceof ItemPowerFist) {

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
}
