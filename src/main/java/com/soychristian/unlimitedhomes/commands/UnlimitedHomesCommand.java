package com.soychristian.unlimitedhomes.commands;

import com.soychristian.unlimitedhomes.UnlimitedHomes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class UnlimitedHomesCommand implements CommandExecutor {
    UnlimitedHomes plugin;
    public UnlimitedHomesCommand(UnlimitedHomes plugin) {
        this.plugin = plugin;
    }
    private Player player;
    private FileConfiguration config;
    private int playerHomes;
    private ConfigurationSection playerHomesSection;
    /*
    * TODO: La mejor forma de guardar las homes es que cada home tenga un titulo de seccion numerico, asi es mas facil de manipular.
    *  playerHomesSection es un arreglo con las secciones de las homes del jugador, y actualmente es "home-1", "home-2", etc.
    * - Mejor, "1", "2", etc.
    * TODO: Despues de cambiar a numeros, lo mas practico es que el usuario defina el nombre de su home, ya que al utilizar numeros, si se elimina una home, las demas se desordenan.
    * */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }
        player = (Player) sender;
        config = plugin.getConfig();
        playerHomesSection = config.isConfigurationSection("homes." + player.getName()) ? config.getConfigurationSection("homes." + player.getName()) : null;
        //playerHomesSection = config.getConfigurationSection("homes." + player.getName());
        playerHomes = playerHomesSection == null ? 0 : playerHomesSection.getKeys(false).size();
        /*if (playerHomesSection == null) {
            playerHomes = 0;
        } else {
            playerHomes = playerHomesSection.getKeys(false).size();
        }*/

        if (args.length == 0) {
            player.sendMessage("You must specify a subcommand!\n" +
                    "Available subcommands: set, delete, list, tp");
        } else {
            String argument = args[0];
            switch (argument) {
                case "set":
                    if (args.length == 1){
                        player.sendMessage("You must specify a home name!");
                    } else {
                        String homeName = args[1];
                        //config.set("players." + player.getName() + ".homes.", playerHomes);
                        //plugin.saveConfig();
                        //playerHomes++;
                        player.sendMessage("You have set a home!");
                        //config.set("homes." + player.getName() + ".home-%a".replace("%a", String.valueOf(playerHomes)), player.getLocation());
                        //config.set("homes." + player.getName() + ".%a".replace("%a", String.valueOf(playerHomes)), player.getLocation());
                        config.set("homes." + player.getName() + ".%a".replace("%a", homeName), player.getLocation());
                        if (playerHomesSection == null){
                            playerHomesSection = config.getConfigurationSection("homes." + player.getName());
                        }
                        playerHomes = playerHomesSection.getKeys(false).size();
                        player.sendMessage("You have %a homes!".replace("%a", String.valueOf(playerHomes)));
                        plugin.saveConfig();
                    }
                    break;
                case "delete":
                    player.sendMessage("You have deleted a home!");
                    if (args.length == 1) {
                        player.sendMessage("You must specify a home to delete!");
                    } else {
                        String home = args[1];
                        /*if (!(home.matches("[0-9]+"))) {
                            player.sendMessage("You must specify a valid home!");
                            return true;
                        }*/

                        if (playerHomesSection.getKeys(false).contains(home)) {
                            config.set("homes." + player.getName() + "." + home, null);
                            plugin.saveConfig();
                            player.sendMessage("You have deleted home %a!".replace("%a", home));
                        } else {
                            player.sendMessage("You don't have a home with that name!");
                        }
                        plugin.saveConfig();
                    }
                    break;
                case "list":
                    player.sendMessage("You have listed your homes!");
                    if (playerHomesSection == null || playerHomesSection.getKeys(false).isEmpty()) {
                        player.sendMessage("You don't have any homes!");
                    } else {
                        for (String home : playerHomesSection.getKeys(false)) {
                            Location homeLocation = (Location) playerHomesSection.get(home);
                            //player.sendMessage("Home %a: %b".replace("%a", home).replace("%b", playerHomesSection.getString(home)));
                            player.sendMessage("Home %a: X: %x Y: %y Z: %z".replace("%a", colorize("&6" + home + "&f"))
                                    .replace("%x", String.valueOf(homeLocation.getBlockX()))
                                    .replace("%y", String.valueOf(homeLocation.getBlockY()))
                                    .replace("%z", String.valueOf(homeLocation.getBlockZ()))
                            );
                        }
                    }
                    break;
                case "tp":
                    player.sendMessage("You have teleported to a home!");
                    if (args.length == 1){
                        player.sendMessage("You must specify a home to teleport to!");
                    } else {
                        String home = args[1];
                        if (playerHomesSection.getKeys(false).isEmpty()){
                            player.sendMessage("You don't have any homes!");
                        } else if (!(playerHomesSection.getKeys(false).contains(home))) {
                            player.sendMessage("You don't have a home with that name!");
                            Set<String> homesArray = playerHomesSection.getKeys(false);
                            String homesName = "";
                            for (String homeName : homesArray) {
                                homesName += colorize("%a").replace("%a", colorize("&6" + homeName + "&f")) + ", ";
                            }
                            //homesArray.forEach(homeName -> homesName += homeName + ", ");
                            player.sendMessage("Available homes: %a".replace("%a", homesName));
                        } else {
                            Location homeLocation = (Location) playerHomesSection.get(home);
                            player.teleport(homeLocation);
                            player.sendMessage("You have teleported to home %a!".replace("%a", home));
                        }
                    }
                    break;
                default:
                    player.sendMessage("That is not a valid subcommand!\n" +
                            "Available subcommands: set, delete, list, tp");
            }
        }
        return true;
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
