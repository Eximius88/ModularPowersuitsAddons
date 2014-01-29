package andrew.powersuits.client;

//import andrew.powersuits.book.SmallFontRenderer;
//import andrew.powersuits.book.page.BookPage;
//import andrew.powersuits.book.page.ContentsPage;
//import andrew.powersuits.book.page.IntroPage;
//import andrew.powersuits.book.page.TextPage;
import andrew.powersuits.common.AddonConfig;
import andrew.powersuits.common.AddonLogger;
import andrew.powersuits.common.CommonProxy;
import andrew.powersuits.common.ModularPowersuitsAddons;
import andrew.powersuits.tick.ClientTickHandler;
import andrew.powersuits.tick.CommonTickHandler;
import andrew.powersuits.tick.RenderTickHandler;
import com.google.common.base.Charsets;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ClientProxy extends CommonProxy {

    private static RenderTickHandler renderTickHandler;
    private static ClientTickHandler clientTickHandler;
    private static CommonTickHandler commonTickHandler;




   // public static SmallFontRenderer smallFontRenderer;

    public static final String LANG_PATH = "/assets/powersuitaddons/lang/";
    public static String extractedLanguage = "";


    @SideOnly(Side.CLIENT)
    public static String getCurrentLanguage() {
        return Minecraft.getMinecraft().gameSettings.language;
    }




    @SideOnly(Side.CLIENT)
    public static void loadCurrentLanguage() {
        if (getCurrentLanguage() != extractedLanguage) {
            extractedLanguage = getCurrentLanguage();
        }
        try {
            InputStream inputStream = ModularPowersuitsAddons.INSTANCE.getClass().getResourceAsStream(LANG_PATH + extractedLanguage + ".lang");
            Properties langPack = new Properties();
            langPack.load(new InputStreamReader(inputStream, Charsets.UTF_8));
            LanguageRegistry.instance().addStringLocalization(langPack, extractedLanguage);
        } catch (Exception e) {
            e.printStackTrace();
            AddonLogger.logError("Couldn't read MPSA localizations for language " + extractedLanguage + " :(");
        }
    }


    @SideOnly(Side.CLIENT)
    public static String translate(String str) {
        loadCurrentLanguage();
        return StatCollector.translateToLocal(str);
    }








    @Override
    public void registerHandlers() {
        super.registerHandlers();

        if (AddonConfig.useHUDStuff) {
            renderTickHandler = new RenderTickHandler();
            TickRegistry.registerTickHandler(renderTickHandler, Side.CLIENT);
        }

        clientTickHandler = new ClientTickHandler();
        TickRegistry.registerTickHandler(clientTickHandler, Side.CLIENT);
        ClientTickHandler.load();

        commonTickHandler = new CommonTickHandler();
        TickRegistry.registerTickHandler(commonTickHandler, Side.CLIENT);
    }



   /* @Override
    public void registerRenderers() {
        Minecraft mc = Minecraft.getMinecraft();
        smallFontRenderer = new SmallFontRenderer(mc.gameSettings, "/font/default.png", mc.renderEngine, false);
    }

    public static Document manual;

    public void readManuals() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        manual = readManual("/mods/PowersuitAddons/resources/manuals/manual.xml", factory);
        initManualPages();
    }

    Document readManual(String location, DocumentBuilderFactory factory) {
        try {
            InputStream stream = ModularPowersuitsAddons.class.getResourceAsStream(location);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stream);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

/*    public static Map<String, Class<? extends BookPage>> pageClasses = new HashMap<String, Class<? extends BookPage>>();

    public static void registerManualPage(String type, Class<? extends BookPage> clazz) {
        pageClasses.put(type, clazz);
    }

    public static Class<? extends BookPage> getPageClass(String type) {
        return pageClasses.get(type);
    }

    void initManualPages() {
        ClientProxy.registerManualPage("intro", IntroPage.class);
        ClientProxy.registerManualPage("contents", ContentsPage.class);
        ClientProxy.registerManualPage("text", TextPage.class);
    }
    */
}