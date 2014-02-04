package andrew.powersuits.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class Waypoint implements Serializable, Comparable {
    private static final long serialVersionUID = 8136790917447997951L;
    public String name;
    public String imageSuffix = "";
    public String world = "";
    public TreeSet<Integer> dimensions = new TreeSet();
    public int x;
    public int z;
    public int y;
    public boolean enabled;
    public boolean isDead = false;
    public boolean inWorld = true;
    public boolean inDimension = true;
    public float red = 0.0F;
    public float green = 1.0F;
    public float blue = 0.0F;

    public Waypoint(String name, int x, int z, int y, boolean enabled, float red, float green, float blue, String suffix, String world, TreeSet<Integer> dimensions)
    {
        this.name = name;
        this.x = x;
        this.z = z;
        this.y = y;
        this.enabled = enabled;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.imageSuffix = suffix;
        this.world = world;
        this.dimensions = dimensions;
    }

    public int getUnified()
    {
        return -16777216 + ((int)(this.red * 255.0F) << 16) + ((int)(this.green * 255.0F) << 8) + (int)(this.blue * 255.0F);
    }

    public void kill()
    {
        this.enabled = false;
        this.isDead = true;
    }

    public boolean isActive()
    {
        return (this.enabled) && (this.inWorld) && (this.inDimension);
    }

    public int getX()
    {
        return Minecraft.getMinecraft().thePlayer.dimension == -1 ? this.x / 8 : this.x;
    }

    public int getZ()
    {
        return Minecraft.getMinecraft().thePlayer.dimension == -1 ? this.z / 8 : this.z;
    }

    public int getY()
    {
        return this.y;
    }

    public void setX(int x)
    {
        this.x = (Minecraft.getMinecraft().thePlayer.dimension == -1 ? x * 8 : x);
    }

    public void setZ(int z)
    {
        this.z = (Minecraft.getMinecraft().thePlayer.dimension == -1 ? z * 8 : z);
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int compareTo(Object arg0)
    {
        double distance = getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer);
        return getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > ((Waypoint)arg0).getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) ? -1 : 1;
    }

    public double getDistanceSqToEntity(Entity par1Entity)
    {
        double var2 = getX() - par1Entity.posX;
        double var4 = getY() - par1Entity.posY;
        double var6 = getZ() - par1Entity.posZ;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }
}
