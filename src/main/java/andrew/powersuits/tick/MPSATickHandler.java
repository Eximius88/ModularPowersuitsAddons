package andrew.powersuits.tick;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

/**
 * Created by Shawn on 10/7/2014.
 */
public class MPSATickHandler {

    private Minecraft mc;

    public MPSATickHandler(Minecraft mc) {
        this.mc = mc;
    }

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event) {


    }
}
