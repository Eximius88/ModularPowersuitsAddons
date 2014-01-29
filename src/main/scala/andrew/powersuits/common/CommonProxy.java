package andrew.powersuits.common;

import andrew.powersuits.network.AndrewPacketHandler;
import andrew.powersuits.tick.CommonTickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    private static CommonTickHandler commonTickHandler;
    public static AndrewPacketHandler packetHandler;


    public void registerHandlers() {
        commonTickHandler = new CommonTickHandler();
        TickRegistry.registerTickHandler(commonTickHandler, Side.SERVER);
        CommonTickHandler.load();

        packetHandler = new AndrewPacketHandler();
        packetHandler.register();
    }

    public void registerRenderers() {}

    public void readManuals() {}

    public static String getCurrentLanguage() { return null; }

    public static void loadCurrentLanguage() {}

    public static String translate(String str) {return str;}


}
