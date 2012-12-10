package de.Keyle.MyPet.entity.types.enderman;

import de.Keyle.MyPet.entity.types.CraftMyPet;
import de.Keyle.MyPet.entity.types.creeper.EntityMyCreeper;
import org.bukkit.craftbukkit.CraftServer;


public class CraftMyEnderman extends CraftMyPet
{
    public CraftMyEnderman(CraftServer server, EntityMyEnderman entityMyEnderman)
    {
        super(server, entityMyEnderman);
    }


    public void setBlockData(short flag)
    {
        ((EntityMyEnderman) getHandle()).setBlockData(flag);
    }

    public void setBlockID(short flag)
    {
        ((EntityMyEnderman) getHandle()).setBlockID(flag);
    }

    public void setScreaming(boolean flag)
    {
        ((EntityMyEnderman) getHandle()).setScreaming(flag);
    }

    public short getBlockID()
    {
        return ((EntityMyEnderman) getHandle()).getBlockID();
    }

    public short getBlockData()
    {
        return ((EntityMyEnderman) getHandle()).getBlockData();
    }

    public boolean isScreaming()
    {
        return ((EntityMyEnderman) getHandle()).isScreaming();
    }

    @Override
    public String toString()
    {
        return "CraftMyEnderman{isPet=" + getHandle().isMyPet() + ",owner=" + getOwner() + ",BlockID=" + getBlockID() + ",BlockData=" + getBlockID() +"}";
    }


}
