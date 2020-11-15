package io.github.ravidshachar.craftana;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.JSONException;

import static io.github.ravidshachar.craftana.Methods.stackTraceToString;

import java.io.IOException;

public final class craftana extends JavaPlugin {
	
    @Override
    public void onEnable() {
    	final CraftanaCommandExecutor commandExecutor = new CraftanaCommandExecutor(this);
    	getLogger().info("onEnable has been invoked!");
    	this.getCommand("setclock").setExecutor(commandExecutor);
    	this.getCommand("cleardashboard").setExecutor(commandExecutor);
    	this.getCommand("setgraph").setExecutor(commandExecutor);
    	clearAll(commandExecutor);
    	BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
					updateAll(commandExecutor);
				} catch (NumberFormatException | JSONException | IOException e) {
					// TODO Auto-generated catch block
					getLogger().info(stackTraceToString(e));
				}
            }
        }, 0L, 100L);
    }
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    }
    
    public void updateAll(CraftanaCommandExecutor commandExecutor) throws NumberFormatException, JSONException, IOException {
    	commandExecutor.clockDashboard.updateDashboard();
    	commandExecutor.graphDashboard.updateDashboard();
    }
    
    public void clearAll(CraftanaCommandExecutor commandExecutor) {
    	commandExecutor.clockDashboard.clearDashboard();
    	commandExecutor.graphDashboard.clearDashboard();
    	getLogger().info("All clear!");
    }
}