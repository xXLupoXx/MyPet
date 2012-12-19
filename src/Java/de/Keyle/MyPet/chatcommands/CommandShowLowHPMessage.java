package de.Keyle.MyPet.chatcommands;

import de.Keyle.MyPet.util.MyPetPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShowLowHPMessage implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            MyPetPlayer myPetPlayer = MyPetPlayer.getMyPetPlayer(player);

            myPetPlayer.setShowLowHPMessage(!myPetPlayer.getShowLowHPMessage());
            myPetPlayer.setDataChanged();
        }
        return true;
    }
}
