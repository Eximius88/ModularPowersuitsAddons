package andrew.powersuits.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by Eximius88 on 2/3/14.
 */
/*
public class WaypointManager {

    public ArrayList<Waypoint> wayPts = new ArrayList();
    private ArrayList<Waypoint> old2dWayPts = new ArrayList();
    private ArrayList<Waypoint> updatedPts;
    private EntityWaypointContainer entityWaypointContainer = null;
    private File settingsFile;

    public WaypointManager(VoxelMap minimap)
    {
        this.minimap = minimap;
    }

    public void handleDeath()
    {
        TreeSet<Integer> toDel = new TreeSet();
        for (Waypoint pt : this.wayPts)
        {
            if (pt.name.equals("Latest Death")) {
                pt.name = "Previous Death";
            }
            if (pt.name.startsWith("Previous Death")) {
                if (this.minimap.deathpoints > 1)
                {
                    int num = 0;
                    try
                    {
                        if (pt.name.length() > 15) {
                            num = Integer.valueOf(pt.name.substring(15)).intValue();
                        }
                    }
                    catch (Exception e)
                    {
                        num = 0;
                    }
                    pt.red -= (pt.red - 0.5F) / 8.0F;
                    pt.green -= (pt.green - 0.5F) / 8.0F;
                    pt.blue -= (pt.blue - 0.5F) / 8.0F;
                    pt.name = ("Previous Death " + (num + 1));
                }
                else
                {
                    toDel.add(Integer.valueOf(this.wayPts.indexOf(pt)));
                }
            }
        }
        if ((this.minimap.deathpoints < 2) && (toDel.size() > 0))
        {
            TreeSet<Integer> toDelReverse = (TreeSet)toDel.descendingSet();
            for (Integer index : toDelReverse) {
                deleteWaypoint(index.intValue());
            }
        }
        if (this.minimap.deathpoints > 0)
        {
            TreeSet<Integer> dimensions = new TreeSet();
            dimensions.add(Integer.valueOf(this.minimap.game.thePlayer.dimension));
            addWaypoint(new Waypoint("Latest Death", this.minimap.game.thePlayer.dimension != -1 ? this.minimap.xCoord() : this.minimap.xCoord() * 8, this.minimap.game.thePlayer.dimension != -1 ? this.minimap.zCoord() : this.minimap.zCoord() * 8, this.minimap.yCoord() - 1, true, 1.0F, 1.0F, 1.0F, "skull", this.minimap.getCurrentSubWorldName(), dimensions));
        }
    }

    public void newWorld()
    {
        for (Waypoint pt : this.wayPts) {
            if ((pt.dimensions.size() == 0) || (pt.dimensions.contains(Integer.valueOf(this.minimap.game.thePlayer.dimension)))) {
                pt.inDimension = true;
            } else {
                pt.inDimension = false;
            }
        }
        injectWaypointEntity();
    }

    public void newSubWorldName(String name)
    {
        String currentSubWorld = scrubName(name);
        for (Waypoint pt : this.wayPts) {
            if ((currentSubWorld == "") || (pt.world == "") || (currentSubWorld.equals(pt.world))) {
                pt.inWorld = true;
            } else {
                pt.inWorld = false;
            }
        }
    }

    public void saveWaypoints()
    {
        String worldNameSave = this.minimap.getCurrentWorldName();
        if (worldNameSave.endsWith(":25565"))
        {
            int portSepLoc = worldNameSave.lastIndexOf(":");
            if (portSepLoc != -1) {
                worldNameSave = worldNameSave.substring(0, portSepLoc);
            }
        }
        worldNameSave = scrubFileName(worldNameSave);

        File homeDir = VoxelMap.getAppDir("minecraft").getAbsoluteFile();
        File mcDir = Minecraft.getMinecraft().mcDataDir.getAbsoluteFile();
        if (!homeDir.equals(mcDir))
        {
            String localDirName = "";
            if (Minecraft.getMinecraft().isIntegratedServerRunning())
            {
                localDirName = mcDir.getName();
                if (((localDirName.equalsIgnoreCase("minecraft")) || (localDirName.equalsIgnoreCase("."))) && (mcDir.getParentFile() != null)) {
                    localDirName = mcDir.getParentFile().getName();
                }
                localDirName = "~" + localDirName;
            }
            this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/VoxelMods/voxelMap"), worldNameSave + localDirName + ".points");
            saveWaypointsTo(this.settingsFile);
            homeDir = new File(Minecraft.getMinecraft().mcDataDir, "/mods/VoxelMods/voxelMap/");
            if (!homeDir.exists()) {
                homeDir.mkdirs();
            }
            this.settingsFile = new File(Minecraft.getMinecraft().mcDataDir, "/mods/VoxelMods/voxelMap/" + worldNameSave + ".points");
            saveWaypointsTo(this.settingsFile);
        }
        else
        {
            this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/VoxelMods/voxelMap"), worldNameSave + ".points");
            saveWaypointsTo(this.settingsFile);
        }
    }

    public void saveWaypointsTo(File settingsFile)
    {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));

            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMddHHmm").format(now);
            out.println("filetimestamp:" + timestamp);
            for (Waypoint pt : this.wayPts) {
                if (!pt.name.startsWith("^"))
                {
                    String dimensionsString = "";
                    for (Integer dimension : pt.dimensions) {
                        dimensionsString = dimensionsString + "" + dimension + "#";
                    }
                    if (dimensionsString.equals("")) {
                        dimensionsString = "-1#0#";
                    }
                    out.println("name:" + scrubName(pt.name) + ",x:" + pt.x + ",z:" + pt.z + ",y:" + pt.y + ",enabled:" + Boolean.toString(pt.enabled) + ",red:" + pt.red + ",green:" + pt.green + ",blue:" + pt.blue + ",suffix:" + pt.imageSuffix + ",world:" + scrubName(pt.world) + ",dimensions:" + dimensionsString);
                }
            }
            out.close();
        }
        catch (Exception local)
        {
            this.minimap.chatInfo("§EError Saving Waypoints");
        }
    }

    public String scrubName(String input)
    {
        input = input.replace(":", "~colon~");
        input = input.replace(",", "~comma~");
        return input;
    }

    private String descrubName(String input)
    {
        input = input.replace("~colon~", ":");
        input = input.replace("~comma~", ",");
        return input;
    }

    public String scrubFileName(String input)
    {
        input = input.replace("<", "~less~");
        input = input.replace(">", "~greater~");
        input = input.replace(":", "~colon~");
        input = input.replace("\"", "~quote~");
        input = input.replace("/", "~slash~");
        input = input.replace("\\", "~backslash~");
        input = input.replace("|", "~pipe~");
        input = input.replace("?", "~question~");
        input = input.replace("*", "~star~");
        return input;
    }

    public void loadWaypoints()
    {
        boolean loaded = false;
        this.wayPts = new ArrayList();
        String worldNameStandard = this.minimap.getCurrentWorldName();
        if (worldNameStandard.endsWith(":25565"))
        {
            int portSepLoc = worldNameStandard.lastIndexOf(":");
            if (portSepLoc != -1) {
                worldNameStandard = worldNameStandard.substring(0, portSepLoc);
            }
        }
        worldNameStandard = scrubFileName(worldNameStandard);

        String worldNameWithPort = scrubFileName(this.minimap.getCurrentWorldName());

        String worldNameWithoutPort = this.minimap.getCurrentWorldName();
        int portSepLoc = worldNameWithoutPort.lastIndexOf(":");
        if (portSepLoc != -1) {
            worldNameWithoutPort = worldNameWithoutPort.substring(0, portSepLoc);
        }
        worldNameWithoutPort = scrubFileName(worldNameWithoutPort);

        String worldNameWithDefaultPort = scrubFileName(worldNameWithoutPort + "~colon~25565");

        loaded = loadWaypointsExtensible(worldNameStandard);
        if (!loaded) {
            loaded = loadOldWaypoints(worldNameWithoutPort, worldNameWithDefaultPort, worldNameWithPort);
        }
        if (!loaded) {
            loaded = findReiWaypoints(worldNameWithoutPort);
        }
        if (!loaded) {
            this.minimap.chatInfo("§ENo waypoints exist for this world/server.");
        } else {
            populateOld2dWaypoints();
        }
    }

    private boolean loadWaypointsExtensible(String worldNameStandard)
    {
        File homeDir = VoxelMap.getAppDir("minecraft").getAbsoluteFile();
        File mcDir = Minecraft.getMinecraft().mcDataDir.getAbsoluteFile();
        if (!homeDir.equals(mcDir))
        {
            long homeDate = -1L;
            long localDate = -1L;
            String localDirName = "";
            if (this.minimap.game.isIntegratedServerRunning())
            {
                localDirName = mcDir.getName();
                if (((localDirName.equalsIgnoreCase("minecraft")) || (localDirName.equalsIgnoreCase("."))) && (mcDir.getParentFile() != null)) {
                    localDirName = mcDir.getParentFile().getName();
                }
                localDirName = "~" + localDirName;
            }
            File settingsFileLocal = new File(Minecraft.getMinecraft().mcDataDir, "/mods/VoxelMods/voxelMap/" + worldNameStandard + ".points");
            localDate = getDateFromSave(settingsFileLocal);
            File settingsFileHome = new File(VoxelMap.getAppDir("minecraft/mods/VoxelMods/voxelMap"), worldNameStandard + localDirName + ".points");
            if ((!settingsFileHome.exists()) && (!settingsFileLocal.exists())) {
                settingsFileHome = new File(VoxelMap.getAppDir("minecraft/mods/VoxelMods/voxelMap"), worldNameStandard + ".points");
            }
            homeDate = getDateFromSave(settingsFileHome);
            if ((!settingsFileHome.exists()) && (!settingsFileLocal.exists())) {
                return false;
            }
            if (!settingsFileHome.exists()) {
                this.settingsFile = settingsFileLocal;
            } else if (!settingsFileLocal.exists()) {
                this.settingsFile = settingsFileHome;
            } else {
                this.settingsFile = (homeDate > localDate ? settingsFileHome : settingsFileLocal);
            }
        }
        else
        {
            this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/VoxelMods/voxelMap"), worldNameStandard + ".points");
        }
        if (this.settingsFile.exists())
        {
            try
            {
                BufferedReader in = new BufferedReader(new FileReader(this.settingsFile));
                String sCurrentLine;
                while ((sCurrentLine = in.readLine()) != null) {
                    if (!sCurrentLine.startsWith("filetimestamp"))
                    {
                        String[] curLine = sCurrentLine.split(",");
                        String name = "";
                        int x = 0;
                        int z = 0;
                        int y = -1;
                        boolean enabled = false;
                        float red = 0.5F;
                        float green = 0.0F;
                        float blue = 0.0F;
                        String suffix = "";
                        String world = "";
                        TreeSet<Integer> dimensions = new TreeSet();
                        for (int t = 0; t < curLine.length; t++)
                        {
                            String[] keyValuePair = curLine[t].split(":");
                            if (keyValuePair.length == 2)
                            {
                                String key = keyValuePair[0];
                                String value = keyValuePair[1];
                                if (key.equals("name"))
                                {
                                    name = descrubName(value);
                                }
                                else if (key.equals("x"))
                                {
                                    x = Integer.parseInt(value);
                                }
                                else if (key.equals("z"))
                                {
                                    z = Integer.parseInt(value);
                                }
                                else if (key.equals("y"))
                                {
                                    y = Integer.parseInt(value);
                                }
                                else if (key.equals("enabled"))
                                {
                                    enabled = Boolean.parseBoolean(value);
                                }
                                else if (key.equals("red"))
                                {
                                    red = Float.parseFloat(value);
                                }
                                else if (key.equals("green"))
                                {
                                    green = Float.parseFloat(value);
                                }
                                else if (key.equals("blue"))
                                {
                                    blue = Float.parseFloat(value);
                                }
                                else if (key.equals("suffix"))
                                {
                                    suffix = value;
                                }
                                else if (key.equals("world"))
                                {
                                    world = descrubName(value);
                                }
                                else if (key.equals("dimensions"))
                                {
                                    String[] dimensionStrings = value.split("#");
                                    for (int s = 0; s < dimensionStrings.length; s++) {
                                        dimensions.add(Integer.valueOf(Integer.parseInt(dimensionStrings[s])));
                                    }
                                    if (dimensions.size() == 0)
                                    {
                                        dimensions.add(Integer.valueOf(0));
                                        dimensions.add(Integer.valueOf(-1));
                                    }
                                }
                            }
                        }
                        if (!name.equals("")) {
                            loadWaypoint(name, x, z, y, enabled, red, green, blue, suffix, world, dimensions);
                        }
                    }
                }
                in.close();
            }
            catch (Exception local)
            {
                this.minimap.chatInfo("§EError Loading Waypoints");
                System.out.println("waypoint load error: " + local.getLocalizedMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private long getDateFromSave(File settingsFile)
    {
        if (settingsFile.exists()) {
            try
            {
                BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                String sCurrentLine = in.readLine();
                String[] curLine = sCurrentLine.split(":");
                if (curLine[0].equals("filetimestamp"))
                {
                    in.close();
                    return Long.parseLong(curLine[1]);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return -1L;
    }

    private boolean loadOldWaypoints(String worldNameWithPort, String worldNameWithDefaultPort, String worldNameWithoutPort)
    {
        try
        {
            this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/zan"), worldNameWithPort + ".points");
            if (!this.settingsFile.exists()) {
                this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/zan"), worldNameWithDefaultPort + ".points");
            }
            if (!this.settingsFile.exists()) {
                this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/zan"), worldNameWithoutPort + ".points");
            }
            if (!this.settingsFile.exists()) {
                this.settingsFile = new File(VoxelMap.getAppDir("minecraft"), worldNameWithoutPort + ".points");
            }
            if (this.settingsFile.exists())
            {
                TreeSet<Integer> dimensions = new TreeSet();
                dimensions.add(Integer.valueOf(-1));
                dimensions.add(Integer.valueOf(0));
                BufferedReader in = new BufferedReader(new FileReader(this.settingsFile));
                String sCurrentLine;
                while ((sCurrentLine = in.readLine()) != null)
                {
                    String[] curLine = sCurrentLine.split(":");
                    if (curLine.length == 4) {
                        loadWaypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), -1, Boolean.parseBoolean(curLine[3]), 0.0F, 1.0F, 0.0F, "", "", dimensions);
                    } else if (curLine.length == 7) {
                        loadWaypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), -1, Boolean.parseBoolean(curLine[3]), Float.parseFloat(curLine[4]), Float.parseFloat(curLine[5]), Float.parseFloat(curLine[6]), "", "", dimensions);
                    } else if (curLine.length == 8)
                    {
                        if ((curLine[3].contains("true")) || (curLine[3].contains("false"))) {
                            loadWaypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), -1, Boolean.parseBoolean(curLine[3]), Float.parseFloat(curLine[4]), Float.parseFloat(curLine[5]), Float.parseFloat(curLine[6]), curLine[7], "", dimensions);
                        } else {
                            loadWaypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), Integer.parseInt(curLine[3]), Boolean.parseBoolean(curLine[4]), Float.parseFloat(curLine[5]), Float.parseFloat(curLine[6]), Float.parseFloat(curLine[7]), "", "", dimensions);
                        }
                    }
                    else if (curLine.length == 9) {
                        loadWaypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), Integer.parseInt(curLine[3]), Boolean.parseBoolean(curLine[4]), Float.parseFloat(curLine[5]), Float.parseFloat(curLine[6]), Float.parseFloat(curLine[7]), curLine[8], "", dimensions);
                    }
                }
                in.close();
                return true;
            }
            return false;
        }
        catch (Exception local)
        {
            this.minimap.chatInfo("§EError Loading Waypoints");
            System.out.println("waypoint load error: " + local.getLocalizedMessage());
        }
        return false;
    }

    private boolean findReiWaypoints(String worldNameWithoutPort)
    {
        boolean foundSome = false;
        this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/rei_minimap"), worldNameWithoutPort + ".points");
        if (!this.settingsFile.exists()) {
            this.settingsFile = new File(Minecraft.getMinecraft().mcDataDir, "/mods/rei_minimap/" + worldNameWithoutPort + ".points");
        }
        if (this.settingsFile.exists())
        {
            loadReiWaypoints(this.settingsFile, 0);
            foundSome = true;
        }
        else
        {
            for (int t = -25; t < 25; t++)
            {
                this.settingsFile = new File(VoxelMap.getAppDir("minecraft/mods/rei_minimap"), worldNameWithoutPort + ".DIM" + t + ".points");
                if (!this.settingsFile.exists()) {
                    this.settingsFile = new File(Minecraft.getMinecraft().mcDataDir, "/mods/rei_minimap/" + worldNameWithoutPort + ".DIM" + t + ".points");
                }
                if (this.settingsFile.exists())
                {
                    foundSome = true;
                    loadReiWaypoints(this.settingsFile, t);
                }
            }
        }
        return foundSome;
    }

    private void loadReiWaypoints(File settingsFile, int dimension)
    {
        try
        {
            if (settingsFile.exists())
            {
                TreeSet<Integer> dimensions = new TreeSet();
                dimensions.add(Integer.valueOf(dimension));
                BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                String sCurrentLine;
                while ((sCurrentLine = in.readLine()) != null)
                {
                    String[] curLine = sCurrentLine.split(":");
                    if (curLine.length == 6)
                    {
                        int color = Integer.parseInt(curLine[5], 16);
                        float red = (color >> 16 & 0xFF) / 255.0F;
                        float green = (color >> 8 & 0xFF) / 255.0F;
                        float blue = (color >> 0 & 0xFF) / 255.0F;




                        int x = Integer.parseInt(curLine[1]);
                        int z = Integer.parseInt(curLine[3]);
                        if (dimension == -1)
                        {
                            x *= 8;
                            z *= 8;
                        }
                        loadWaypoint(curLine[0], x, z, Integer.parseInt(curLine[2]), Boolean.parseBoolean(curLine[4]), red, green, blue, "", "", dimensions);
                    }
                }
            }
        }
        catch (Exception e)
        {
            this.minimap.chatInfo("§EError Loading Old Rei Waypoints");
            System.out.println("waypoint load error: " + e.getLocalizedMessage());
        }
    }

    public void loadWaypoint(String name, int x, int z, int y, boolean enabled, float red, float green, float blue, String suffix, String world, TreeSet<Integer> dimensions)
    {
        Waypoint newWaypoint = new Waypoint(name, x, z, y, enabled, red, green, blue, suffix, world, dimensions);
        this.wayPts.add(newWaypoint);
    }

    public void populateOld2dWaypoints()
    {
        this.old2dWayPts = new ArrayList();
        for (Waypoint wpt : this.wayPts) {
            if (wpt.getY() <= 0) {
                this.old2dWayPts.add(wpt);
            }
        }
    }

    public void check2dWaypoints()
    {
        if ((this.minimap.game.thePlayer.dimension == 0) &&
                (this.old2dWayPts.size() > 0))
        {
            this.updatedPts = new ArrayList();
            for (Waypoint pt : this.old2dWayPts) {
                if ((Math.abs(pt.getX() - this.minimap.xCoord()) < 400) && (Math.abs(pt.getZ() - this.minimap.zCoord()) < 400) && (this.minimap.game.thePlayer.worldObj.getChunkFromBlockCoords(pt.getX(), pt.getZ()).isChunkLoaded))
                {
                    pt.setY(this.minimap.game.thePlayer.worldObj.getHeightValue(pt.getX(), pt.getZ()));
                    this.updatedPts.add(pt);
                    saveWaypoints();
                }
            }
            this.old2dWayPts.removeAll(this.updatedPts);
            System.out.println("remaining old 2d waypoints: " + this.old2dWayPts.size());
        }
    }

    private void deleteWaypoint(int i)
    {
        this.old2dWayPts.remove(this.wayPts.get(i));
        this.entityWaypointContainer.removeWaypoint((Waypoint)this.wayPts.get(i));
        ((Waypoint)this.wayPts.get(i)).kill();
        this.wayPts.remove(i);
        saveWaypoints();
    }

    public void deleteWaypoint(Waypoint point)
    {
        this.old2dWayPts.remove(point);
        this.entityWaypointContainer.removeWaypoint(point);
        point.kill();
        this.wayPts.remove(point);
        saveWaypoints();
    }

    public void addWaypoint(Waypoint newWaypoint)
    {
        this.wayPts.add(newWaypoint);
        this.entityWaypointContainer.addWaypoint(newWaypoint);
        saveWaypoints();
    }

    private void purgeWaypointEntity()
    {
        List entities = this.minimap.game.theWorld.getLoadedEntityList();
        for (int t = 0; t < entities.size(); t++)
        {
            Entity entity = (Entity)entities.get(t);
            if ((entity instanceof EntityWaypointContainer)) {
                entity.setDead();
            }
        }
    }

    public void injectWaypointEntity()
    {
        this.entityWaypointContainer = new EntityWaypointContainer(this.minimap.getWorld());
        for (Waypoint wpt : this.wayPts) {
            this.entityWaypointContainer.addWaypoint(wpt);
        }
        this.minimap.getWorld().spawnEntityInWorld(this.entityWaypointContainer);
    }

    public void moveWaypointEntityToBack()
    {
        List entities = this.minimap.game.theWorld.getLoadedEntityList();
        synchronized (entities)
        {
            int t = entities.indexOf(this.entityWaypointContainer);
            if (t == -1)
            {
                purgeWaypointEntity();

                injectWaypointEntity();
                return;
            }
            if (t != entities.size() - 1) {
                Collections.swap(entities, t, entities.size() - 1);
            }
        }
    }



}*/
