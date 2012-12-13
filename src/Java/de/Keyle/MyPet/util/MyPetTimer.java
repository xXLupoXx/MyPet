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

package de.Keyle.MyPet.util;

import de.Keyle.MyPet.MyPetPlugin;
import de.Keyle.MyPet.entity.types.MyPet;

import java.util.ArrayList;
import java.util.List;

public class MyPetTimer
{
    private int timer = -1;

    private static boolean resetTimer = false;

    private final List<Scheduler> tasksToSchedule = new ArrayList<Scheduler>();

    public void stopTimer()
    {
        if (timer != -1)
        {
            MyPetPlugin.getPlugin().getServer().getScheduler().cancelTask(timer);
            timer = -1;
        }
    }

    public void startTimer()
    {
        stopTimer();

        timer = MyPetPlugin.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(MyPetPlugin.getPlugin(), new Runnable()
        {
            int autoSaveTimer = MyPetConfig.autoSaveTime;

            public void run()
            {
                for (MyPet myPet : MyPetList.getAllMyPets())
                {
                    myPet.scheduleTask();
                }
                for (Scheduler task : tasksToSchedule)
                {
                    task.schedule();
                }
                for (MyPetPlayer player : MyPetPlayer.getPlayerList())
                {
                    player.schedule();
                }
                if (resetTimer && MyPetConfig.autoSaveTime > 0)
                {
                    autoSaveTimer = MyPetConfig.autoSaveTime;
                    resetTimer = false;
                }
                if (MyPetConfig.autoSaveTime > 0 && autoSaveTimer-- < 0)
                {
                    MyPetPlugin.getPlugin().savePets(false);
                    autoSaveTimer = MyPetConfig.autoSaveTime;
                }
            }
        }, 0L, 20L);
    }

    public void resetTimer()
    {
        resetTimer = true;
    }

    public void addTask(Scheduler scheduler)
    {
        tasksToSchedule.add(scheduler);
    }

    public void removeTask(Scheduler scheduler)
    {
        tasksToSchedule.remove(scheduler);
    }
}