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

package de.Keyle.MyPet.entity.types.slime;

import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPetType;
import de.Keyle.MyPet.util.MyPetPlayer;
import net.minecraft.server.v1_4_5.NBTTagCompound;

public class MySlime extends MyPet
{
    protected int size = 1;

    public MySlime(MyPetPlayer petOwner)
    {
        super(petOwner);
        this.petName = "Slime";
    }

    public int getSize()
    {
        if (status == PetState.Here)
        {
            return ((CraftMySlime) getCraftPet()).getSize();
        }
        else
        {
            return size;
        }
    }

    public void setSize(int value)
    {
        if (status == PetState.Here)
        {
            ((CraftMySlime) getCraftPet()).setSize(value);
        }
        this.size = value;
    }

    @Override
    public NBTTagCompound getExtendedInfo()
    {
        NBTTagCompound info = new NBTTagCompound("Info");
        info.setInt("Size", getSize());
        return info;
    }

    @Override
    public void setExtendedInfo(NBTTagCompound info)
    {
        setSize(info.getInt("Size"));
    }

    @Override
    public MyPetType getPetType()
    {
        return MyPetType.Slime;
    }

    @Override
    public String toString()
    {
        return "MySlime{owner=" + getOwner().getName() + ", name=" + petName + ", exp=" + experience.getExp() + "/" + experience.getRequiredExp() + ", lv=" + experience.getLevel() + ", status=" + status.name() + ", skilltree=" + (skillTree != null ? skillTree.getName() : "-") + ", size=" + getSize() + "}";
    }
}