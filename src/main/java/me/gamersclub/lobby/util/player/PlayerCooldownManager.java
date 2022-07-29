package me.gamersclub.lobby.util.player;

import org.bukkit.Bukkit;

import static me.gamersclub.lobby.GamersClubMain.*;

public class PlayerCooldownManager {

    public static boolean isInAnarchyQueue(String uuid) {
        //If the player is not in the anarchy command queue, it will return false but add them to said queue
        if (anarchyCooldownList.contains(uuid)) {
            return true;
        } else {
            anarchyCooldownList.add(uuid);
            anarchyRemove(uuid, 8L);
            return false;
        }
    }

    public static boolean isInFormQueue(String uuid) {
        //If the player is not in the form queue, it will return false but add them to the queue
        if (openedFormQueue.contains(uuid)) {
            return true;
        } else {
            openedFormQueue.add(uuid);
            formQueueRemove(uuid, 5L);
            return false;
        }
    }

    public static void formQueueRemove(String uuid, long duration) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> openedFormQueue.remove(uuid),duration);
    }

    public static void anarchyRemove(String uuid, long duration) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> anarchyCooldownList.remove(uuid),duration);
    }
}
