package andrew.powersuits.modules;

import andrew.powersuits.common.AddonLogger;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.powermodule.tool.PickaxeModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eximius88 on 1/29/14.
 */
public class AOEPickUpgradeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_AOE_PICK_UPGRADE = "Diamond Drill Upgrade";
    public static final ItemStack ironPickaxe = new ItemStack(Item.pickaxeIron);
    public static final String ENERGY_CONSUMPTION = "Energy Consumption";


    public AOEPickUpgradeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addInstallCost(new ItemStack(Item.diamond, 3));
        addBaseProperty(ENERGY_CONSUMPTION, 5, "J");

    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityPlayer player) {

        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            for (int i = -1; i < 1; i++) {
                for (int j = -1; j < 1; j++) {
                    playerMP.theItemInWorldManager.tryHarvestBlock(i, y, j);
                    playerMP.theItemInWorldManager.tryHarvestBlock(x, y + 1, z);
                    playerMP.theItemInWorldManager.tryHarvestBlock(x, y - 1, z);
                }

            }

        }
        return false;
    }


    public boolean isBreakable(int id) {
        return id == Block.dirt.blockID || id == Block.grass.blockID || id == Block.gravel.blockID || id == Block.cobblestone.blockID || id == Block.stone.blockID || id == Block.sand.blockID || id == Block.sandStone.blockID || id == Block.snow.blockID || id == Block.slowSand.blockID || id == Block.netherrack.blockID || id == Block.whiteStone.blockID;
    }

    @Override
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        event.newSpeed = (float) ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(),
                PickaxeModule.PICKAXE_HARVEST_SPEED);
    }

    @Override
    public String getTextureFile() {
        return "diamondupgrade1";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_AOE_PICK_UPGRADE;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.aoePickUpgrade.name");
    }

    @Override
    public String getDescription() {
        return "An updrade that will allow the pickaxe module to mine a 3x3 area of blocks";
    }


}