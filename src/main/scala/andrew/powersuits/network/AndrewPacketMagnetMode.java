package andrew.powersuits.network;

import andrew.powersuits.network.muse.MusePacketOld;
import andrew.powersuits.tick.ClientTickHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInputStream;

/**
 * Created by User: Andrew2448
 * 1:30 PM 4/27/13
 */
public class AndrewPacketMagnetMode extends MusePacketOld {

    protected int entityID;

    public AndrewPacketMagnetMode(Player player, int id) {
        super(player);
        writeInt(id);
    }

    public AndrewPacketMagnetMode(DataInputStream data, Player player) {
        super(data, player);
        entityID = readInt();
    }

    public void handleClient(EntityClientPlayerMP player) {
        ClientTickHandler.instance().addSMPMagneticItem(entityID, player.worldObj);
    }

    public void handleServer(EntityPlayerMP player) {

    }

}
