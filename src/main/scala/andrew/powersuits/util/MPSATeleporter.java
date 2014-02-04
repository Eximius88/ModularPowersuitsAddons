package andrew.powersuits.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class MPSATeleporter extends Teleporter

{
    private final WorldServer worldInstance;

    public MPSATeleporter(WorldServer par1WorldServer)
    {
        super(par1WorldServer);
        this.worldInstance = par1WorldServer;
    }

    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        EntityLivingBase player = (EntityLivingBase)par1Entity;
        clearTeleportPath(this.worldInstance, player);
        par1Entity.fallDistance = 0.0F;
    }

    private void clearTeleportPath(World world, EntityLivingBase entity)
    {
        if (entity.dimension != -1)
        {
            boolean canFindHigherGround = false;
            double posY = entity.posY;
            for (;;)
            {
                if ((world.getBlockId((int)entity.posX, (int)posY, (int)entity.posZ) == 0) || (posY >= 256.0D))
                {
                    canFindHigherGround = true;
                    break;
                }
                posY += 1.0D;
            }
            if (canFindHigherGround)
            {
                while ((world.getBlockId((int)entity.posX, (int)posY - 1, (int)entity.posZ) == 0) && (posY > 0.0D)) {
                    posY -= 1.0D;
                }
                entity.setPosition(entity.posX, posY, entity.posZ);
            }
            else
            {
                for (int q = (int)Math.floor(entity.posY) - 2; q < entity.posY + 1.0D; q++) {
                    for (int i = (int)Math.floor(entity.posX) - 1; i < entity.posX + 1.0D; i++) {
                        for (int k = (int)Math.floor(entity.posZ) - 1; k < entity.posZ + 1.0D; k++) {
                            if (q == (int)Math.floor(entity.posY - 2.0D)) {
                                world.setBlock(i, q, k, 0);
                            }
                        }
                    }
                }
            }
        }
        else if (entity.dimension == -1)
        {
            boolean canFindHigherGround = false;
            double posY = entity.posY;
            for (;;)
            {
                if ((world.getBlockId((int)entity.posX, (int)posY, (int)entity.posZ) == 0) || (posY >= 256.0D))
                {
                    canFindHigherGround = true;
                    break;
                }
                posY += 1.0D;
            }
            if (canFindHigherGround) {
                entity.setPosition(entity.posX, posY, entity.posZ);
            } else {
                for (int q = (int)Math.floor(entity.posY) - 2; q < entity.posY + 1.0D; q++) {
                    for (int i = (int)Math.floor(entity.posX) - 1; i < entity.posX + 1.0D; i++) {
                        for (int k = (int)Math.floor(entity.posZ) - 1; k < entity.posZ + 1.0D; k++) {
                            if (q == (int)Math.floor(entity.posY - 2.0D)) {
                                world.setBlock(i, q, k, Block.netherrack.blockID);
                            } else {
                                world.setBlock(i, q, k, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}


