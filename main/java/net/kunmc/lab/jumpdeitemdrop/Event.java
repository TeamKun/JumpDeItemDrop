package net.kunmc.lab.jumpdeitemdrop;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

public class Event implements Listener {

    @EventHandler
    public void onJumpEvent(PlayerJumpEvent e){
        if(!Command.game)
            return;

        Location loc = e.getPlayer().getLocation();
        boolean onTeam = false;
        Team team = e.getPlayer().getScoreboard().getTeam("JumpDeItem");

        if(team!=null) {
            if (team.getPlayers().contains(e.getPlayer())) {
                onTeam = true;
            }
        }

        ItemStack i = ItemLogic.dropItem(onTeam);
        loc.getWorld().dropItem(loc,i);

    }

}
