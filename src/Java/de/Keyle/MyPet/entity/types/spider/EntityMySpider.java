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

package de.Keyle.MyPet.entity.types.spider;

import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.World;

public class EntityMySpider extends EntityMyPet
{
    public EntityMySpider(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/spider.png";
        this.walkSpeed = 0.4F;
        this.setPathfinder();
    }

    public EnumMonsterType getMonsterType()
    {
        return EnumMonsterType.ARTHROPOD;
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntity()
    {
        if (this.bukkitEntity == null)
        {
            this.bukkitEntity = new CraftMySpider(this.world.getServer(), this);
        }
        return this.bukkitEntity;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void a()
    {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0)); // N/A
    }

    @Override
    protected void a(int i, int j, int k, int l)
    {
        makeSound("mob.spider.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String aY()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String aZ()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String ba()
    {
        return "mob.spider.death";
    }

    public void f(boolean flag)
    {
        byte b0 = this.datawatcher.getByte(16);

        if (flag)
        {
            b0 = (byte) (b0 | 0x1);
        }
        else
        {
            b0 = (byte) (b0 & 0xFFFFFFFE);
        }
        this.datawatcher.watch(16, b0);
    }

    public void j_()
    {
        super.j_();
        if (!this.world.isStatic)
        {
            f(this.positionChanged);
        }
    }
}