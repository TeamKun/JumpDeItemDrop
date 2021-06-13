package net.kunmc.lab.jumpdeitemdrop;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;

public class Command implements TabCompleter, @Nullable CommandExecutor {

    static boolean game = false;

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(command.getName().equals("JumpDeItem")){
            if (args.length <= 1) {
                return (sender.hasPermission("JumpDeItem")
                        ? Stream.of("start","stop","help","setJoinNumber")
                        : Stream.of("start","stop","help","setJoinNumber"))
                        .filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
            }
            if(args.length == 2 && args[0].equals("setJoinNumber")){
                return (sender.hasPermission("JumpDeItem")
                        ? Stream.of("<人数>")
                        : Stream.of("<人数>"))
                        .filter(e -> e.startsWith(args[1])).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equals("JumpDeItem"))
            return false;

        //team関係コマンド
        if(args.length!=2&&args.length!=1){
            sender.sendMessage(ChatColor.RED+"[プラグイン]コマンドの形式が異なります。コマンドの一覧は/JumpDeItem helpで確認できます");
            return false;
        }


            if(args[0].equals("setJoinNumber")){
                if(args[1].matches("[+-]?\\d*(\\.\\d+)?")){
                    if(Integer.parseInt(args[1])>=0 ){
                        JumpDeItemDrop.numberOfTeam = Integer.parseInt(args[1]);
                        sender.sendMessage(ChatColor.GREEN+"[プラグイン]ゲーム開始時にチームに参加する人数を"+args[1]+"人にしました");
                    }else{
                        sender.sendMessage(ChatColor.RED+"[プラグイン]引数には0以上の整数を入れてください");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED+"[プラグイン]引数には数値を入れてください");
                }
                return false;
            }


        //その他のコマンド
        if(args.length!=1){
            sender.sendMessage(ChatColor.RED+"[プラグイン]コマンドの形式が異なります。コマンドの一覧は/JumpDeItem helpで確認できます");
            return false;
        }

        if(args[0].equals("start")){
            if(!game) {
                game = true;
                sender.sendMessage(ChatColor.GREEN + "[プラグイン]アイテムドロップをONにしました！");

                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard board = manager.getMainScoreboard();
                Team team;
                if (!board.getTeams().contains(board.getTeam("JumpDeItem"))) {
                    team = board.registerNewTeam("JumpDeItem");
                    team.color(NamedTextColor.AQUA);
                } else {
                    team = board.getTeam("JumpDeItem");
                }

                int n = JumpDeItemDrop.numberOfTeam;
                List<Player> onlinePlayer = new ArrayList<>(Bukkit.getOnlinePlayers());
                team.getPlayers().forEach(player -> {
                    if (onlinePlayer.contains(player)) {
                        onlinePlayer.remove(player);
                    }
                });
                Random r = new Random();

                for (int i = n; i > 0; i--) {
                    Player p = onlinePlayer.get(r.nextInt(onlinePlayer.size()));
                    team.addPlayer(p);
                    onlinePlayer.remove(p);
                    if (onlinePlayer.size() <= 0) {
                        i = 0;
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED+"[プラグイン]ゲームはすでに開始されています");
            }
            return false;
        }

        if(args[0].equals("stop")){
            if(game) {
                game = false;
                sender.sendMessage(ChatColor.GREEN + "[プラグイン]アイテムドロップをOFFにしました！");
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard board = manager.getMainScoreboard();
                Team team = board.getTeam("JumpDeItem");
                assert team != null;
                team.getPlayers().forEach(team::removePlayer);
            }else{
                sender.sendMessage(ChatColor.RED+"[プラグイン]ゲームは開始されていません");
            }
            return false;
        }


        if(args[0].equals("help")){
            sender.sendMessage(ChatColor.GREEN+"[プラグイン]コマンドの一覧を表示します!");
            sender.sendMessage(ChatColor.GOLD+"===================================");
            sender.sendMessage(ChatColor.AQUA+"・/JumpDeItem start");
            sender.sendMessage("アイテムドロップをONにします");
            sender.sendMessage(ChatColor.AQUA+"・/JumpDeItem stop");
            sender.sendMessage("アイテムドロップをOFFにします");
            sender.sendMessage(ChatColor.AQUA+"・/JumpDeItem setJoinNumber <人数>");
            sender.sendMessage("ゲーム開始時にチームに入れる人数の設定");
            sender.sendMessage(ChatColor.AQUA+"・/JumpDeItem help");
            sender.sendMessage("コマンドの一覧を表示します");
            sender.sendMessage(ChatColor.GOLD+"===================================");
            return false;
        }

        sender.sendMessage(ChatColor.RED+"[プラグイン]コマンドの形式が異なります。コマンドの一覧は/JumpDeItem helpで確認できます");
        return false;
    }
}
