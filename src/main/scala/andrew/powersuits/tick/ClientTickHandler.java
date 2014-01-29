package andrew.powersuits.tick;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.item.ItemPowerArmorChestplate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import andrew.powersuits.common.AddonConfig;
import andrew.powersuits.common.AddonLogger;
import andrew.powersuits.common.AddonUtils;
import andrew.powersuits.modules.MagnetModule;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Created by User: Andrew2448
 * 12:47 PM 4/27/13
 */
public class ClientTickHandler implements ITickHandler {

    private static ClientTickHandler instance;
    private ArrayList<EntityItem> SMPmagneticItems = new ArrayList<EntityItem>();

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        Minecraft mc = Minecraft.getMinecraft();

        if(type.contains(TickType.CLIENT) && mc.theWorld != null) {

            EntityClientPlayerMP player = mc.thePlayer;
            ItemStack torso = player.getCurrentArmor(2);
            if (torso != null && torso.getItem() instanceof ItemPowerArmorChestplate) {
                if (ModuleManager.itemHasActiveModule(torso, MagnetModule.MODULE_MAGNET)) {
                    if (AddonConfig.useDebugMode) {
                        AddonLogger.logDebug("Entering client tick handler for magnet.");
                    }
                    updateMagnetMode(mc.theWorld, player);
                }
            }
        }
    }

    public void addSMPMagneticItem(int i, World world) {
        Entity entity = world.getEntityByID(i);
        if(entity == null || !(entity instanceof EntityItem)) {
            return;
        }
        SMPmagneticItems.add((EntityItem)entity);
        if (AddonConfig.useDebugMode) {
            AddonLogger.logDebug("Recieved magnet mode packet.");
        }
    }

    @SuppressWarnings("unchecked")
    private void updateMagnetMode(World world, EntityPlayerSP player) {
        float distancexz = 16;
        float distancey = 8;
        double maxspeedxz = 0.5;
        double maxspeedy = 0.5;
        double speedxz = 0.05;
        double speedy = 0.07;

        List<EntityItem> items;
        if(world.isRemote) {
            items = SMPmagneticItems;
        }

        else {
            items = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(distancexz, distancey, distancexz));
        }

        for(Iterator<EntityItem> iterator = items.iterator(); iterator.hasNext();) {
            EntityItem item = iterator.next();

            if(item.delayBeforeCanPickup > 0) {
                continue;
            }

            if(item.isDead && world.isRemote) {
                iterator.remove();
            }

            if(!AddonUtils.canItemFitInInventory(player, item.getEntityItem())) {
                continue;
            }

            double dx = player.posX - item.posX;
            double dy = player.posY + player.getEyeHeight() - item.posY;
            double dz = player.posZ - item.posZ;
            double absxz = Math.sqrt(dx*dx+dz*dz);
            double absy = Math.abs(dy);

            if(absxz > distancexz) {
                continue;
            }

            if(absxz > 1) {
                dx /= absxz;
                dz /= absxz;
            }

            if(absy > 1) {
                dy /= absy;
            }

            double vx = item.motionX + speedxz*dx;
            double vy = item.motionY + speedy*dy;
            double vz = item.motionZ + speedxz*dz;

            double absvxz = Math.sqrt(vx*vx+vz*vz);
            double absvy = Math.abs(vy);

            double rationspeedxz = absvxz / maxspeedxz;
            if(rationspeedxz > 1) {
                vx/=rationspeedxz;
                vz/=rationspeedxz;
            }

            double rationspeedy = absvy / maxspeedy;
            if(rationspeedy > 1) {
                vy/=rationspeedy;
            }

            if(absvxz < 0.2 && absxz < 0.2 && world.isRemote) {
                item.setDead();
            }
            if (AddonConfig.useDebugMode) {
                AddonLogger.logDebug("Set velocity "+vx+" "+vy+" "+vz+".");
            }
            item.setVelocity(vx, vy, vz);
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

    }

    public static void load() {
        instance = new ClientTickHandler();
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "MPSA: Client Tick";
    }

    public static ClientTickHandler instance() {
        return instance;
    }
}