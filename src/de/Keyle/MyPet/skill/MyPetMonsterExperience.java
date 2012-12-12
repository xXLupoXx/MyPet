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

package de.Keyle.MyPet.skill;

import org.bukkit.entity.EntityType;

public class MyPetMonsterExperience
{
    private double min;
    private double max;
    private EntityType entityType;

    public MyPetMonsterExperience(double min, double max, EntityType entityType)
    {
        if (max >= min)
        {
            this.max = max;
            this.min = min;
        }
        else if (max <= min)
        {
            this.max = min;
            this.min = max;
        }
        this.entityType = entityType;
    }

    public MyPetMonsterExperience(double exp, EntityType entityType)
    {
        this.max = exp;
        this.min = exp;
        this.entityType = entityType;
    }

    public double getRandomExp()
    {
        return max == min ? max : ((int) (doubleRandom(min, max) * 100)) / 100.;
    }

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
    }

    public EntityType getEntityType()
    {
        return entityType;
    }

    public void setMin(double min)
    {
        this.min = min;
        if (min > max)
        {
            max = min;
        }
    }

    public void setMax(double max)
    {
        this.max = max;
        if (max < min)
        {
            min = max;
        }
    }

    public void setExp(double exp)
    {
        max = (min = exp);
    }

    private static double doubleRandom(double low, double high)
    {
        return Math.random() * (high - low) + low;
    }

    @Override
    public String toString()
    {
        return entityType.getName() + "{min=" + min + ", max=" + max + "}";
    }
}