package andrew.powersuits.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class DimensionUtilities {

    public static void doDimensionTransfer(EntityLivingBase entity, int dimension)
    {
        if ((entity instanceof EntityPlayerMP))
        {
            EntityPlayerMP player = (EntityPlayerMP)entity;
            player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension, new MPSATeleporter(player.mcServer.worldServerForDimension(dimension)));
        }
        else
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int j = entity.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(j);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimension);
            entity.dimension = dimension;
            entity.worldObj.removeEntity(entity);
            entity.isDead = false;

            minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1, new MPSATeleporter(worldserver1));

            Entity e = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
            if (e != null)
            {
                e.copyDataFrom(entity, true);
                worldserver1.spawnEntityInWorld(e);
            }
            entity.isDead = true;

            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
        }
    }
}
