package me.mintoyatsu.greentext;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public class GreentextPlayerListener extends PlayerListener {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
    public boolean bGreentext;
    public boolean bRedtext;
    public boolean bYellowtext;
    public boolean bOrangetext;
    
    public static Greentext plugin; public GreentextPlayerListener(Greentext instance) {
        plugin = instance;
    }

    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage();
        
        if (event.getMessage().startsWith(">") && bGreentext == true) {
            message = stripColors(message);
            event.setMessage(ChatColor.GREEN + message);
        } else if (event.getMessage().startsWith("<") && bRedtext == true) {
            message = stripColors(message);
            event.setMessage(ChatColor.RED + message);
        } else if (event.getMessage().endsWith(">") && bYellowtext == true) {
            message = stripColors(message);
            event.setMessage(ChatColor.YELLOW + message);
        } else if (event.getMessage().endsWith("<") && bOrangetext == true) {
            message = stripColors(message);
            event.setMessage(ChatColor.GOLD + message);
        } else {
            event.setMessage(message);
        }
    }
    
    private static String stripColors(String message)
    {
        return STRIP_COLOR_PATTERN.matcher(ChatColor.stripColor(message)).replaceAll("");
    }
}
