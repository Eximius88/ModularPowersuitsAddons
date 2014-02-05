package andrew.powersuits.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

import java.util.logging.Logger;

/**
 * Created by User: Andrew2448
 * 6:39 PM 5/5/13
 */
public abstract class AddonLogger {
    public static final Logger logger = Logger.getLogger("MPSA-" + FMLCommonHandler.instance().getEffectiveSide());

    static {
        logger.setParent(FMLLog.getLogger());
    }

    public static void logDebug(String string) {
        logger.info(string);
    }

    public static void logError(String string) {
        logger.warning(string);

    }
}
