package andrew.powersuits.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Eximius88 on 2/3/14.
 */
public class EntityWaypointContainer extends Entity {
    private Waypoint waypoint;
    private boolean inNether = false;
    public ArrayList<Waypoint> wayPts = new ArrayList();
    public ArrayList<Waypoint> deadWayPts = new ArrayList();

    public EntityWaypointContainer(World par1World)
    {
        super(par1World);
        this.ignoreFrustumCheck = true;
    }

    public void onUpdate()
    {
        for (Waypoint pt : this.wayPts) {
            if (pt.isDead) {
                this.deadWayPts.add(pt);
            }
        }
        this.wayPts.removeAll(this.deadWayPts);
        this.deadWayPts.clear();
        sortWaypoints();
    }

    protected void entityInit() {}

    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

    public boolean isInRangeToRenderVec3D(Vec3 par1Vec3)
    {
        return true;
    }

    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

    public float getBrightness(float par1)
    {
        return 1.0F;
    }

    public void addWaypoint(Waypoint newWaypoint)
    {
        this.wayPts.add(newWaypoint);
    }

    public void removeWaypoint(Waypoint point)
    {
        this.wayPts.remove(point);
    }

    public void sortWaypoints()
    {
        Collections.sort(this.wayPts);
    }
}
