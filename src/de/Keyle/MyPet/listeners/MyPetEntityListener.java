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

package de.Keyle.MyPet.listeners;

import de.Keyle.MyPet.MyPetPlugin;
import de.Keyle.MyPet.entity.pathfinder.movement.PathfinderGoalRide;
import de.Keyle.MyPet.entity.types.CraftMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPet.LeashFlag;
import de.Keyle.MyPet.entity.types.MyPet.PetState;
import de.Keyle.MyPet.entity.types.MyPetType;
import de.Keyle.MyPet.entity.types.chicken.CraftMyChicken;
import de.Keyle.MyPet.entity.types.irongolem.CraftMyIronGolem;
import de.Keyle.MyPet.entity.types.ocelot.MyOcelot;
import de.Keyle.MyPet.entity.types.pig.MyPig;
import de.Keyle.MyPet.entity.types.sheep.MySheep;
import de.Keyle.MyPet.entity.types.slime.MySlime;
import de.Keyle.MyPet.entity.types.villager.MyVillager;
import de.Keyle.MyPet.entity.types.wolf.MyWolf;
import de.Keyle.MyPet.event.MyPetLeashEvent;
import de.Keyle.MyPet.skill.skills.Behavior;
import de.Keyle.MyPet.skill.skills.Poison;
import de.Keyle.MyPet.util.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class MyPetEntityListener implements Listener
{
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;

            if (event.getEntity() instanceof CraftMyPet)
            {
                if (e.getDamager() instanceof Player)
                {
                    Player damager = (Player) e.getDamager();
                    MyPet myPet = MyPetList.getMyPet(event.getEntity().getEntityId());
                    if (myPet.getCraftPet().getHandle().isRidden())
                    {
                        event.setCancelled(true);
                        if (myPet.getSkillSystem().hasSkill("Ride"))
                        {
                            if (myPet.getCraftPet().getHandle().petPathfinderSelector.hasGoal("Ride"))
                            {
                                ((PathfinderGoalRide) myPet.getCraftPet().getHandle().petPathfinderSelector.getGoal("Ride")).toggleRiding();
                            }
                        }
                    }
                    else if (damager.getItemInHand().getType() == MyPetConfig.leashItem)
                    {
                        String msg;
                        if (myPet.getHealth() > myPet.getMaxHealth() / 3 * 2)
                        {
                            msg = "" + ChatColor.GREEN + myPet.getHealth() + ChatColor.WHITE + "/" + myPet.getMaxHealth();
                        }
                        else if (myPet.getHealth() > myPet.getMaxHealth() / 3)
                        {
                            msg = "" + ChatColor.YELLOW + myPet.getHealth() + ChatColor.WHITE + "/" + myPet.getMaxHealth();
                        }
                        else
                        {
                            msg = "" + ChatColor.RED + myPet.getHealth() + ChatColor.WHITE + "/" + myPet.getMaxHealth();
                        }
                        damager.sendMessage(MyPetUtil.setColors("%aqua%%petname%").replace("%petname%", myPet.petName));
                        damager.sendMessage(MyPetUtil.setColors("   HP:       %hp%").replace("%petname%", myPet.petName).replace("%hp%", msg));
                        if (!myPet.getOwner().equals(damager))
                        {
                            damager.sendMessage(MyPetUtil.setColors("   Owner:    %Owner%").replace("%Owner%", myPet.getOwner().getName()));
                        }
                        else
                        {
                            if (!myPet.isPassiv())
                            {
                                int damage = MyPet.getStartDamage(myPet.getClass()) + (myPet.getSkillSystem().hasSkill("Damage") ? myPet.getSkillSystem().getSkillLevel("Damage") : 0);
                                damager.sendMessage(MyPetUtil.setColors("   Damage: %dmg%").replace("%petname%", myPet.petName).replace("%dmg%", "" + damage));
                            }
                            if (MyPetConfig.hungerSystem)
                            {
                                damager.sendMessage(MyPetUtil.setColors("   Hunger: %hunger%").replace("%hunger%", "" + myPet.getHungerValue()));
                            }
                            if (MyPetConfig.levelSystem)
                            {
                                int lvl = myPet.getExperience().getLevel();
                                double exp = myPet.getExperience().getCurrentExp();
                                double reqEXP = myPet.getExperience().getRequiredExp();
                                damager.sendMessage(MyPetUtil.setColors("   Level:    %lvl%").replace("%lvl%", "" + lvl));
                                damager.sendMessage(MyPetUtil.setColors("   EXP:      %exp%/%reqexp%").replace("%exp%", String.format("%1.2f", exp)).replace("%reqexp%", String.format("%1.2f", reqEXP)));
                            }
                        }
                        damager.sendMessage("");

                        event.setCancelled(true);
                    }
                    else if (myPet.getOwner().equals(damager) && (!MyPetConfig.ownerCanAttackPet || !MyPetUtil.canHurtAt(myPet.getOwner().getPlayer(), myPet.getLocation())))
                    {
                        event.setCancelled(true);
                    }
                    else if (!myPet.getOwner().equals(damager) && !MyPetUtil.canHurt(damager, myPet.getOwner().getPlayer()))
                    {
                        event.setCancelled(true);
                    }
                }
            }
            else if (MyPetType.isLeashableEntityType(event.getEntity().getType()))
            {
                if (e.getDamager() instanceof Player)
                {
                    Player damager = (Player) e.getDamager();

                    if (!MyPetList.hasMyPet(damager))
                    {
                        LivingEntity leashTarget = (LivingEntity) event.getEntity();
                        if (!MyPetPermissions.has(damager, "MyPet.user.leash." + MyPetType.getMyPetTypeByEntityType(leashTarget.getType()).getTypeName()) || damager.getItemInHand().getType() != MyPetConfig.leashItem)
                        {
                            return;
                        }

                        boolean willBeLeashed = false;

                        List<LeashFlag> leashFlags = MyPet.getLeashFlags(MyPetType.getMyPetTypeByEntityType(leashTarget.getType()).getMyPetClass());

                        for (LeashFlag flag : leashFlags)
                        {
                            if (flag == LeashFlag.None)
                            {
                                willBeLeashed = true;
                            }
                            else if (flag == LeashFlag.Adult)
                            {
                                if (leashTarget instanceof Ageable)
                                {
                                    willBeLeashed = ((Ageable) leashTarget).isAdult();
                                }
                                else if (leashTarget instanceof Zombie)
                                {
                                    willBeLeashed = !((Zombie) leashTarget).isBaby();
                                }
                            }
                            else if (flag == LeashFlag.Baby)
                            {
                                if (leashTarget instanceof Ageable)
                                {
                                    willBeLeashed = !((Ageable) leashTarget).isAdult();
                                }
                                else if (leashTarget instanceof Zombie)
                                {
                                    willBeLeashed = ((Zombie) leashTarget).isBaby();
                                }
                            }
                            else if (flag == LeashFlag.LowHp)
                            {
                                willBeLeashed = leashTarget.getHealth() <= 2;
                            }
                            else if (flag == LeashFlag.UserCreated)
                            {
                                if (leashTarget instanceof IronGolem)
                                {
                                    willBeLeashed = ((IronGolem) leashTarget).isPlayerCreated();
                                }
                            }
                            else if (flag == LeashFlag.Tamed)
                            {
                                if (leashTarget instanceof Tameable)
                                {
                                    willBeLeashed = ((Tameable) leashTarget).isTamed();
                                }
                            }
                        }

                        if (willBeLeashed)
                        {
                            event.setCancelled(true);
                            MyPet myPet = MyPetType.getMyPetTypeByEntityType(leashTarget.getType()).getNewMyPetInstance(MyPetPlayer.getMyPetPlayer(damager.getName()));
                            MyPetUtil.getServer().getPluginManager().callEvent(new MyPetLeashEvent(myPet));
                            MyPetList.addMyPet(myPet);
                            myPet.createPet(leashTarget.getLocation());
                            if (leashTarget instanceof Ocelot)
                            {
                                ((MyOcelot) myPet).setCatType(((Ocelot) leashTarget).getCatType().getId());
                                ((MyOcelot) myPet).setSitting(((Ocelot) leashTarget).isSitting());
                            }
                            else if (leashTarget instanceof Wolf)
                            {
                                ((MyWolf) myPet).setSitting(((Wolf) leashTarget).isSitting());
                                ((MyWolf) myPet).setCollarColor(DyeColor.getByData((byte) ((CraftWolf) leashTarget).getHandle().getCollarColor()));
                            }
                            else if (leashTarget instanceof Sheep)
                            {
                                ((MySheep) myPet).setColor(((Sheep) leashTarget).getColor().getData());
                                ((MySheep) myPet).setSheared(((Sheep) leashTarget).isSheared());
                            }
                            else if (leashTarget instanceof Villager)
                            {
                                ((MyVillager) myPet).setProfession(((Villager) leashTarget).getProfession().getId());
                            }
                            else if (leashTarget instanceof Pig)
                            {
                                ((MyPig) myPet).setSaddle(((Pig) leashTarget).hasSaddle());
                            }
                            else if (leashTarget instanceof Slime)
                            {
                                ((MySlime) myPet).setSize(((Slime) leashTarget).getSize());
                            }
                            event.getEntity().remove();
                            MyPetUtil.getDebugLogger().info("New Pet leashed:");
                            MyPetUtil.getDebugLogger().info("   " + myPet.toString());
                            MyPetUtil.getDebugLogger().info(MyPetPlugin.getPlugin().savePets(false) + " pet/pets saved.");
                            damager.sendMessage(MyPetUtil.setColors(MyPetLanguage.getString("Msg_AddLeash")));
                        }
                    }

                }
            }
        }
        else if (event.getEntity() instanceof CraftMyChicken)
        {
            if (event.getCause() == DamageCause.FALL)
            {
                event.setCancelled(true);
            }
        }
        else if (event.getEntity() instanceof CraftMyIronGolem)
        {
            if (event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.DROWNING)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageResult(EntityDamageEvent event)
    {
        if (!(event instanceof EntityDamageByEntityEvent) || event.isCancelled())
        {
            return;
        }
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        if (event.getEntity() instanceof LivingEntity)
        {
            if (e.getDamager() instanceof Player)
            {
                Player damager = (Player) e.getDamager();
                if (MyPetList.hasMyPet(damager))
                {
                    MyPet myPet = MyPetList.getMyPet(damager);
                    if (myPet.status == PetState.Here && event.getEntity() != myPet.getCraftPet())
                    {
                        myPet.getCraftPet().getHandle().goalTarget = ((CraftLivingEntity) event.getEntity()).getHandle();
                    }

                }
                else if (e.getDamager() instanceof CraftMyPet)
                {
                    MyPet myPet = ((CraftMyPet) e.getDamager()).getHandle().getMyPet();
                    if (myPet.getSkillSystem().hasSkill("Poison"))
                    {
                        Poison poisonSkill = (Poison) myPet.getSkillSystem().getSkill("Poison");
                        if (poisonSkill.getPoison())
                        {
                            PotionEffect effect = new PotionEffect(PotionEffectType.POISON, 5, 1);
                            ((LivingEntity) event.getEntity()).addPotionEffect(effect);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event)
    {
        if (event.getEntity() instanceof CraftMyPet)
        {
            MyPet myPet = MyPetList.getMyPet(event.getEntity().getEntityId());
            myPet.status = PetState.Dead;
            myPet.respawnTime = MyPetConfig.respawnTimeFixed + (myPet.getExperience().getLevel() * MyPetConfig.respawnTimeFactor);
            if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                if (!(e.getDamager() instanceof Player && myPet.getOwner() != e.getDamager()))
                {
                    event.setDroppedExp(0);
                }
            }
            SendDeathMessage(event);
            myPet.sendMessageToOwner(MyPetUtil.setColors(MyPetLanguage.getString("Msg_RespawnIn").replace("%petname%", myPet.petName).replace("%time%", "" + myPet.respawnTime)));
        }
        if (MyPetConfig.levelSystem && event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
        {
            if (((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager() instanceof CraftMyPet)
            {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                if (MyPetList.isMyPet(e.getDamager().getEntityId()))
                {
                    MyPet myPet = MyPetList.getMyPet(e.getDamager().getEntityId());
                    event.setDroppedExp(myPet.getExperience().addExp(e.getEntity().getType()));
                }
            }
            else if (((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager() instanceof Player)
            {
                Player owner = (Player) ((EntityDamageByEntityEvent) event.getEntity().getLastDamageCause()).getDamager();
                if (MyPetList.hasMyPet(owner))
                {
                    MyPet myPet = MyPetList.getMyPet(owner);
                    if (myPet.isPassiv())
                    {
                        myPet.getExperience().addExp(event.getEntity().getType(), MyPetConfig.passivePercentPerMonster);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent event)
    {
        if (!event.isCancelled())
        {
            if (event.getEntity() instanceof CraftMyPet)
            {
                if (MyPetList.isMyPet(event.getEntity().getEntityId()))
                {
                    MyPet myPet = MyPetList.getMyPet(event.getEntity().getEntityId());
                    if (myPet.getSkillSystem().hasSkill("Behavior"))
                    {
                        Behavior behaviorSkill = (Behavior) myPet.getSkillSystem().getSkill("Behavior");
                        if (behaviorSkill.getLevel() > 0)
                        {
                            if (behaviorSkill.getBehavior() == Behavior.BehaviorState.Friendly)
                            {
                                event.setCancelled(true);
                            }
                            else if (event.getTarget() instanceof Player && ((Player) event.getTarget()).getName().equals(myPet.getOwner().getName()))
                            {
                                event.setCancelled(true);
                            }
                            else if (behaviorSkill.getBehavior() == Behavior.BehaviorState.Raid)
                            {
                                if (event.getTarget() instanceof Player)
                                {
                                    event.setCancelled(true);
                                }
                                else if (event.getTarget() instanceof Tameable && ((Tameable) event.getTarget()).isTamed())
                                {
                                    event.setCancelled(true);
                                }
                                else if (event.getTarget() instanceof CraftMyPet)
                                {
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
            else if (event.getEntity() instanceof IronGolem)
            {
                if (event.getTarget() instanceof CraftMyPet)
                {
                    if (event.getReason() == TargetReason.RANDOM_TARGET)
                    {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private void SendDeathMessage(final EntityDeathEvent event)
    {

        if (event.getEntity() instanceof CraftMyPet)
        {
            MyPet myPet = ((CraftMyPet) event.getEntity()).getMyPet();
            String killer = MyPetUtil.setColors(MyPetLanguage.getString("Unknown"));
            if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();

                if (e.getDamager().getType() == EntityType.PLAYER)
                {
                    if (e.getDamager() == myPet.getOwner())
                    {
                        killer = "You";
                    }
                    else
                    {
                        killer = ((Player) e.getDamager()).getName();
                    }
                }
                else if (e.getDamager().getType() == EntityType.WOLF)
                {
                    Wolf w = (Wolf) e.getDamager();
                    if (w.isTamed())
                    {
                        killer = "Wolf (" + w.getOwner().getName() + ')';
                    }
                    else
                    {
                        killer = "Wolf";
                    }
                }
                else if (e.getDamager() instanceof CraftMyPet)
                {
                    CraftMyPet craftMyPet = (CraftMyPet) e.getDamager();
                    killer = craftMyPet.getMyPet().petName + " (" + craftMyPet.getOwner().getName() + ')';
                }
                else
                {
                    killer = e.getDamager().getType().getName();
                }
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.BLOCK_EXPLOSION))
            {
                killer = "Explosion";
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.DROWNING))
            {
                killer = "Drowning";
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.FALL))
            {
                killer = "Fall";
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.FIRE) || event.getEntity().getLastDamageCause().getCause().equals(DamageCause.FIRE_TICK))
            {
                killer = "Fire";
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.LAVA))
            {
                killer = "Lava";
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.LIGHTNING))
            {
                killer = "Lightning";
            }
            else if (event.getEntity().getLastDamageCause().getCause().equals(DamageCause.VOID))
            {
                killer = "The Void";
            }
            myPet.sendMessageToOwner(MyPetUtil.setColors(MyPetLanguage.getString("Msg_DeathMessage")).replace("%petname%", myPet.petName) + killer);
        }
    }
}