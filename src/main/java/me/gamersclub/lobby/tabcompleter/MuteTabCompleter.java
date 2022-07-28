package me.gamersclub.lobby.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MuteTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("mute")) {
                if (args.length == 2) {
                    list.add("1");
                    list.add("7");
                    list.add("14");
                    list.add("30");
                    list.add("60");
                    list.add("90");
                    return list;
                } else if (args.length >= 3) {
                    return list;
                }
            }
        }
        return null;
    }
}
