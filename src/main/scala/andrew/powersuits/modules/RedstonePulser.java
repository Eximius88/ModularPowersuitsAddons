package andrew.powersuits.modules;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Eximius88 on 4/29/2014.
 */
public class RedstonePulser extends PowerModuleBase implements IRightClickModule {

    private boolean wiresProvidePower = true;
    private Set blocksNeedingUpdate = new HashSet();

    public static final String MODULE_PULSER = "Redstone Pulser";
    public static final String REDSTONE_ENERGY_CONSUMPTION = "Energy Consumption";
    public static final String HEAT = "Heat Emission";

    public RedstonePulser(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addBaseProperty(REDSTONE_ENERGY_CONSUMPTION, 490000, "");
        addBaseProperty(HEAT, 0.5f, "");
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {

    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {


    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        int i1 = world.getBlockMetadata(x, y, z);
        int j1 = i1 & 7;
        int k1 = 8 - (i1 & 8);

        if (k1 == 0)
        {
            return true;
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, j1 + k1, 3);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
            //this.func_82536_d(world, x, y, z, j1);
            //world.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
            return true;
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }

    @Override
    public String getTextureFile() {
        return "laser";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_PULSER;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.redsonePulser.name");
    }

    @Override
    public String getDescription() {
        return "Pretty much a portable button";
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) > 0 ? 15 : 0;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        int i1 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if ((i1 & 8) == 0)
        {
            return 0;
        }
        else
        {
            int j1 = i1 & 7;
            return j1 == 5 && par5 == 1 ? 15 : (j1 == 4 && par5 == 2 ? 15 : (j1 == 3 && par5 == 3 ? 15 : (j1 == 2 && par5 == 4 ? 15 : (j1 == 1 && par5 == 5 ? 15 : 0))));
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir)
    {
        return true;
    }
}
