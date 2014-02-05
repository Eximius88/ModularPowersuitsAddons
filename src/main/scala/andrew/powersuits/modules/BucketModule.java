package andrew.powersuits.modules;

import andrew.powersuits.common.AddonUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidStack;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by User: Andrew2448
 * 4:59 PM 5/4/13
 */
public class BucketModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_BUCKET = "Bucket";
    public static final ItemStack bucket = new ItemStack(Item.bucketEmpty);
    private boolean isEmpty = true;
    public FluidStack contained = null;
    public ArrayList<FluidStack> list = new ArrayList<FluidStack>();

    public BucketModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(new ItemStack(Item.bucketEmpty, 1));
    }

    @Override
    public String getTextureFile() {
        return null;
    }

    @Override
    public Icon getIcon(ItemStack item) {
        return bucket.getIconIndex();
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_BUCKET;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.bucket.name");
    }

    @Override
    public String getDescription() {
        return "An internal bucket put into your power tool";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {
        float f = 1.0F;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        MovingObjectPosition MOP = getMovingObjectPositionFromPlayer(world, player, isEmpty);
        if (MOP == null) {
            return;
        } else {
            FillBucketEvent event = new FillBucketEvent(player, item, world, MOP);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                if (player.capabilities.isCreativeMode) {
                    return;
                }
            }
            if (MOP.typeOfHit == EnumMovingObjectType.TILE) {
                int i = MOP.blockX, j = MOP.blockY, k = MOP.blockZ;
                if (!world.canMineBlock(player, i, j, k)) {
                    return;
                }
                if (isEmpty) { // Picking Up Liquid
                    if (!player.canPlayerEdit(i, j, k, MOP.sideHit, item)) {
                        return;
                    }
                    if (world.getBlockMaterial(i, j, k) == Material.water && world.getBlockMetadata(i, j, k) == 0) {
                        world.setBlockToAir(i, j, k);
                        if (player.capabilities.isCreativeMode) {
                            return;
                        }
                        if (isEmpty) {
                            contained = new FluidStack(Block.waterStill.blockID, 1);
                            AddonUtils.setLiquid(item, "Water");
                            isEmpty = false;
                        }
                        return;
                    }
                    if (world.getBlockMaterial(i, j, k) == Material.lava && world.getBlockMetadata(i, j, k) == 0) {
                        world.setBlockToAir(i, j, k);
                        if (player.capabilities.isCreativeMode) {
                            return;
                        }

                        if (isEmpty) {
                            contained = new FluidStack(Block.lavaStill.blockID, 1);
                            AddonUtils.setLiquid(item, "Lava");
                            isEmpty = false;
                        }
                        return;
                    }
                } else { // Placing Liquid
                    if (MOP.sideHit == 0) {
                        --j;
                    }

                    if (MOP.sideHit == 1) {
                        ++j;
                    }

                    if (MOP.sideHit == 2) {
                        --k;
                    }

                    if (MOP.sideHit == 3) {
                        ++k;
                    }

                    if (MOP.sideHit == 4) {
                        --i;
                    }

                    if (MOP.sideHit == 5) {
                        ++i;
                    }

                    if (!player.canPlayerEdit(i, j, k, MOP.sideHit, item)) {
                        return;
                    }

                    if (contained != null && this.tryPlaceContainedLiquid(world, d0, d1, d2, i, j, k) && !player.capabilities.isCreativeMode) {
                        AddonUtils.setLiquid(item, "Empty");
                        isEmpty = true;
                        return;
                    }
                }
            }
        }
    }

    public boolean tryPlaceContainedLiquid(World world, double par2, double par4, double par6, int x, int y, int z) {
        if (!world.isAirBlock(x, y, z) && world.getBlockMaterial(x, y, z).isSolid()) {
            return false;
        } else {
            if (world.provider.isHellWorld && contained.fluidID == Block.waterMoving.blockID) {
                world.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                for (int l = 0; l < 8; ++l) {
                    world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
                int i = contained.fluidID;
                world.setBlock(x, y, z, i, 0, 3);
            }
            return true;
        }
    }

    public MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
        float f = 1.0F;
        float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
        float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
        double d0 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double) f;
        double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double) f + 1.62D - (double) par2EntityPlayer.yOffset;
        double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double) f;
        Vec3 vec3 = par1World.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (par2EntityPlayer instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        return par1World.rayTraceBlocks_do_do(vec3, vec31, par3, !par3);
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }
}
