/*
 * Copyright (C) 2011-2012 Keyle
 *
 * This file is part of MyPet
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyPet. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.types.bat;

import de.Keyle.MyPet.entity.pathfinder.movement.PathfinderGoalControl;
import de.Keyle.MyPet.entity.pathfinder.movement.PathfinderGoalFollowOwner;
import de.Keyle.MyPet.entity.pathfinder.movement.PathfinderGoalMeleeAttack;
import de.Keyle.MyPet.entity.pathfinder.movement.PathfinderGoalRide;
import de.Keyle.MyPet.entity.pathfinder.target.*;
import de.Keyle.MyPet.entity.pathfinder.target.PathfinderGoalHurtByTarget;
import de.Keyle.MyPet.entity.pathfinder.target.PathfinderGoalOwnerHurtByTarget;
import de.Keyle.MyPet.entity.pathfinder.target.PathfinderGoalOwnerHurtTarget;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.skill.skills.Ride;
import net.minecraft.server.*;


public class EntityMyBat extends EntityMyPet
{
    public EntityMyBat(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/bat.png";

        petPathfinderSelector.addGoal("Ride", new PathfinderGoalRide(this, this.walkSpeed + 0.15F, Ride.speedPerLevel));
        if (MyPet.getStartDamage(MyBat.class) > 0)
        {
            petPathfinderSelector.addGoal("LeapAtTarget", new PathfinderGoalLeapAtTarget(this, this.walkSpeed + 0.1F));
            petPathfinderSelector.addGoal("MeleeAttack", new PathfinderGoalMeleeAttack(this, this.walkSpeed, 3, 20));
            petTargetSelector.addGoal("OwnerHurtByTarget", new PathfinderGoalOwnerHurtByTarget(this));
            petTargetSelector.addGoal("OwnerHurtTarget", new PathfinderGoalOwnerHurtTarget(myPet));
            petTargetSelector.addGoal("HurtByTarget", new PathfinderGoalHurtByTarget(this, true));
            petTargetSelector.addGoal("ControlTarget", new PathfinderGoalControlTarget(myPet, 1));
            petTargetSelector.addGoal("AggressiveTarget", new PathfinderGoalAggressiveTarget(myPet, 15));
            petTargetSelector.addGoal("FarmTarget", new PathfinderGoalFarmTarget(myPet, 15));
        }
        petPathfinderSelector.addGoal("Control", new PathfinderGoalControl(myPet, this.walkSpeed + 0.1F));
        petPathfinderSelector.addGoal("FollowOwner", new PathfinderGoalFollowOwner(this, this.walkSpeed, 10.0F, 5.0F, 20F));
        petPathfinderSelector.addGoal("LookAtPlayer", false, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        petPathfinderSelector.addGoal("RandomLockaround", new PathfinderGoalRandomLookaround(this));
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntity()
    {
        if (this.bukkitEntity == null)
        {
            this.bukkitEntity = new CraftMyBat(this.world.getServer(), this);
        }
        return this.bukkitEntity;
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setHanging(((MyBat) myPet).ishanging());
        }
    }

    public void setHanging(boolean flags)
    {
        int i = this.datawatcher.getByte(16);
        if (flags)
        {
            this.datawatcher.watch(16, (byte) (i | 0x1));
        }
        else
        {
            this.datawatcher.watch(16, (byte) (i & 0xFFFFFFFE));
        }
        ((MyBat) myPet).hanging = flags;
    }

    public boolean isHanging()
    {
        return (this.datawatcher.getByte(16) & 0x1) != 0;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void a()
    {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0)); // hanging
    }

    @Override
    protected String aY()
    {
        return "mob.bat.idle";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String aZ()
    {
        return "mob.bat.hurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String ba()
    {
        return "mob.bat.death";
    }

    public void j_()
    {
        super.j_();
        this.motY *= 0.6000000238418579D;
        this.locY += 0.65;
    }
}
