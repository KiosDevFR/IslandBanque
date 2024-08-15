package org.cubitech.islandbanque.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.cubitech.islandbanque.events.HeadDatabaseAPIListener;

public class UtilsBanque {

    public static ItemStack getMaterial(String material){
        if(material == null){
            return new ItemStack(Material.PAPER);
        }
        String prefix = material.substring(0,3);
        if(prefix.equalsIgnoreCase("hdb")){
            ItemStack is = HeadDatabaseAPIListener.getApi().getItemHead(material.substring(3));
            if(is != null){
                return is;
            }else{
                Bukkit.getConsoleSender().sendMessage("WARNING: A HEAD WITH ID " + material.substring(3) + " COULDN'T BE FOUND.");
                return new ItemStack(Material.PLAYER_HEAD);
            }
        }
        Material m = Material.getMaterial(material);
        if(m == null){
            return new ItemStack(Material.PAPER);
        }
        return new ItemStack(Material.getMaterial(material));
    }

    public static ItemStack getHead(String playerName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setOwner(playerName);
        item.setItemMeta(skull);
        return item;
    }

    public static int getTypeIntFromText(String type){
        return switch (type){
            case "argent" -> 0;
            case "exp" -> 1;
            case "farmpoints" -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    /**
     * Gets the String name from the type
     * @return
     * @param type
     */
    public static String getStringFromType(int type){
        return switch (type){
            case 0 -> "money";
            case 1 -> "xp";
            case 2 -> "farmpoints";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static String formatLongToSpaces(long number) {
        String result = String.valueOf(number);
        int length = result.length();

        for (int i = length - 3; i > 0; i -= 3) {
            String parte1 = result.substring(0, i);
            String parte2 = result.substring(i);
            result = parte1 + ' ' + parte2;
        }

        return result;
    }
    public static String formatNumber(double number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1_000_000) {
            return String.format("%.2fK", number / 1000);
        } else if (number < 1_000_000_000) {
            return String.format("%.2fM", number / 1_000_000);
        } else {
            return String.format("%.2fMd", number / 1_000_000_000);
        }
    }

    public static String ticksToHoursMinutes(long ticks) {
        long seconds = ticks / 20;

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        // Formatea el resultado
        String formatTime = String.format("%02dh %02dm", hours, minutes);

        return formatTime;
    }

    public static int getTotalExperience(int level) {
        int xp = 0;

        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

    public static int getTotalExperience(Player player) {
        return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(player.getLevel());
    }

    public static void setTotalExperience(Player player, int amount) {
        int level = 0;
        int xp = 0;
        float a = 0;
        float b = 0;
        float c = -amount;

        if (amount > getTotalExperience(0) && amount <= getTotalExperience(15)) {
            a = 1;
            b = 6;
        } else if (amount > getTotalExperience(15) && amount <= getTotalExperience(30)) {
            a = 2.5f;
            b = -40.5f;
            c += 360;
        } else if (amount > getTotalExperience(30)) {
            a = 4.5f;
            b = -162.5f;
            c += 2220;
        }
        level = (int) Math.floor((-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a));
        xp = amount - getTotalExperience(level);
        player.setLevel(level);
        player.setExp(0);
        player.giveExp(xp);
    }
}