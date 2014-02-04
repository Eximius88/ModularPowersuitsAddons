package andrew.powersuits.modules;

import andrew.powersuits.util.DimensionUtilities;
import andrew.powersuits.util.MPSATeleporter;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class TestTeleportModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_DIMENSIONAL_RIFT = "WIP Dimensional Tear Generator WIP";
    public static final String DIMENSIONAL_RIFT_ENERGY_GENERATION = "Energy Consumption";
    public static final String DIMENSIONAL_RIFT_HEAT_GENERATION = "Heat Generation";

    public TestTeleportModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(DIMENSIONAL_RIFT_HEAT_GENERATION, 50);
        addBaseProperty(DIMENSIONAL_RIFT_ENERGY_GENERATION, 20000);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        this.defaultTag.setBoolean(MuseItemUtils.ONLINE, false);
    }



    @Override
    public String getTextureFile() {
        return "kineticgen";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_DIMENSIONAL_RIFT;
    }

    @Override
    public String getLocalizedName() {
        return MODULE_DIMENSIONAL_RIFT;
    }

    @Override
    public String getDescription() {
        return "Generate a tear in the space-time continuum that will teleport the player to its relative coordinates in the nether or overworld. You must bind this module to a key and activate it to use.";
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
        //World world = Minecraft.getMinecraft().theWorld;
       // Entity target =
        /*if (world.provider.isSurfaceWorld())
        {
            //DimensionUtilities.doDimensionTransfer(playerClicking, -1);
            playerClicking.travelToDimension(-1);

        }
        if (playerClicking.dimension != -1)
        {
            ChunkCoordinates coords = (playerClicking != null) ? (playerClicking).getBedLocation(playerClicking.dimension) : null;
            if ((coords == null) || ((coords.posX == 0) && (coords.posY == 0) && (coords.posZ == 0))) {
                coords = world.getSpawnPoint();
            }
            int yPos = coords.posY;
            while ((world.getBlockId(coords.posX, yPos, coords.posZ) != 0) && (world.getBlockId(coords.posX, yPos + 1, coords.posZ) != 0)) {
                yPos++;
            }
            playerClicking.setPositionAndUpdate(coords.posX + 0.5D, yPos, coords.posZ + 0.5D);
        }
        else if (playerClicking.dimension == -1)
        {
            DimensionUtilities.doDimensionTransfer(playerClicking, 0);
        }*/
       /* if(playerClicking.ridingEntity == null && playerClicking.riddenByEntity == null && playerClicking instanceof EntityPlayerMP) {
            if(playerClicking.dimension != -1) {
                //MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)playerClicking, -1);
               // --par1ItemStack.stackSize;
            } else {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)playerClicking, 0);
               // --par1ItemStack.stackSize;
            }
        }*/

        EntityPlayerMP thePlayer = (EntityPlayerMP) playerClicking;
        if (playerClicking.dimension != -1)
        {
            thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, -1 , new MPSATeleporter(thePlayer.mcServer.worldServerForDimension(-1)));
        }
        else
        {
            thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0, new MPSATeleporter(thePlayer.mcServer.worldServerForDimension(0)));
        }


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
