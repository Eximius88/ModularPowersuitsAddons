package andrew.powersuits.network;

import andrew.powersuits.common.AddonConfig;
import andrew.powersuits.common.AddonLogger;
import andrew.powersuits.network.muse.MusePacketOld;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by User: Andrew2448
 * 1:29 PM 4/27/13
 */
public class AndrewPacketHandler implements IPacketHandler {

    public AndrewPacketHandler register() {
        addPacketType(1, AndrewPacketMagnetMode.class);

        NetworkRegistry.instance().registerChannel(this, "psa");
        return this;
    }

    public static BiMap<Integer, Constructor<? extends MusePacketOld>> packetConstructors = HashBiMap
            .create();

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player player) {
        if (payload.channel.equals(AddonConfig.getNetworkChannelName())) {
            MusePacketOld repackaged = repackage(payload, player);
            if (repackaged != null) {
                Side side = FMLCommonHandler.instance().getEffectiveSide();
                if (side == Side.CLIENT) {
                    repackaged.handleClient((EntityClientPlayerMP) player);
                } else if (side == Side.SERVER) {
                    repackaged.handleServer((EntityPlayerMP) player);
                }

            }
        }
    }

    public static MusePacketOld repackage(Packet250CustomPayload payload, Player player) {
        MusePacketOld repackaged = null;
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload.data));
        int packetType;
        try {
            packetType = data.readInt();
            repackaged = useConstructor(packetConstructors.get(packetType), data, player);
        } catch (IOException e) {
            AddonLogger.logError("PROBLEM READING PACKET TYPE D:");
            e.printStackTrace();
            return null;
        }
        return repackaged;
    }

    /**
     * @param type
     * @return
     */
    public static int getTypeID(MusePacketOld packet) {
        try {
            return packetConstructors.inverse().get(getConstructor(packet.getClass()));
        } catch (NoSuchMethodException e) {
            AddonLogger.logError("INVALID PACKET CONSTRUCTOR D:");
            e.printStackTrace();
        } catch (SecurityException e) {
            AddonLogger.logError("PACKET SECURITY PROBLEM D:");
            e.printStackTrace();
        }
        return -150;
    }

    /**
     * Returns the constructor of the given object. Keep in sync with
     * useConstructor.
     *
     * @param packetType
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    protected static Constructor<? extends MusePacketOld> getConstructor(Class<? extends MusePacketOld> packetType) throws NoSuchMethodException, SecurityException {
        return packetType.getConstructor(DataInputStream.class, Player.class);
    }

    /**
     * Returns a new instance of the object, created via the constructor in
     * question. Keep in sync with getConstructor.
     *
     * @param constructor
     * @return
     */
    protected static MusePacketOld useConstructor(Constructor<? extends MusePacketOld> constructor, DataInputStream data, Player player) {
        try {
            return constructor.newInstance(data, player);
        } catch (InstantiationException e) {
            AddonLogger.logError("PROBLEM INSTATIATING PACKET D:");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            AddonLogger.logError("PROBLEM ACCESSING PACKET D:");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            AddonLogger.logError("INVALID PACKET CONSTRUCTOR D:");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            AddonLogger.logError("PROBLEM INVOKING PACKET CONSTRUCTOR D:");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addPacketType(int id, Class<? extends MusePacketOld> packetType) {
        try {
            packetConstructors.put(id, getConstructor(packetType));
            return true;
        } catch (NoSuchMethodException e) {
            AddonLogger.logError("UNABLE TO REGISTER PACKET TYPE: "
                    + packetType + ": INVALID CONSTRUCTOR");
            e.printStackTrace();
        } catch (SecurityException e) {
            AddonLogger.logError("UNABLE TO REGISTER PACKET TYPE: "
                    + packetType + ": SECURITY PROBLEM");
            e.printStackTrace();
        }
        return false;
    }
}
