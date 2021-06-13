package net.kunmc.lab.jumpdeitemdrop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class JumpDeItemDrop extends JavaPlugin {

    static List<Material> blockList = new ArrayList<>();
    static List<Material> itemList = new ArrayList<>();
    static int numberOfTeam ;

    @Override
    public void onEnable() {
        this.getCommand("JumpDeItem").setExecutor(new Command());
        this.getCommand("JumpDeItem").setTabCompleter(new Command());
        Bukkit.getPluginManager().registerEvents(new Event(), this);

        saveDefaultConfig();
        saveConfig();

        numberOfTeam = getConfig().getInt("NumberOfTeams");

        Material[] items = Material.values();
        for(int i=0;i < items.length;i++) {
            if(!items[i].isAir()&&items[i].isItem()){
                if (items[i].isBlock()) {
                    blockList.add(items[i]);
                } else {
                    itemList.add(items[i]);
                }
            }
        }
    }

    @Override
    public void onDisable(){
        getConfig().set("NumberOfTeams",numberOfTeam);
        saveConfig();
    }



}
