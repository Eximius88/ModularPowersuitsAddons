package andrew.powersuits.network;


public class AndrewPacketHandler{


    /*public static BiMap<Integer, Constructor<? extends MusePacketOld>> packetConstructors = HashBiMap
            .create();
*/
   /* @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, EntityPlayer player) {
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

    public static MusePacketOld repackage(Packet250CustomPayload payload, EntityPlayer player) {
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
    /*protected static Constructor<? extends MusePacketOld> getConstructor(Class<? extends MusePacketOld> packetType) throws NoSuchMethodException, SecurityException {
        return packetType.getConstructor(DataInputStream.class, EntityPlayer.class);
    }

    /**
     * Returns a new instance of the object, created via the constructor in
     * question. Keep in sync with getConstructor.
     *
     * @param constructor
     * @return
     */
   /* protected static MusePacketOld useConstructor(Constructor<? extends MusePacketOld> constructor, DataInputStream data, EntityPlayer player) {
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
    }*/
}
