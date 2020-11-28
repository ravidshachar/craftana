package io.github.ravidshachar.craftana;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONException;

import static io.github.ravidshachar.craftana.Methods.stackTraceToString;

import java.io.IOException;

public final class craftana extends JavaPlugin {
	
	final CraftanaCommandExecutor commandExecutor = new CraftanaCommandExecutor(this);
	final Server server = new Server(25566);
	
    @Override
    public void onEnable() {
    	getLogger().info("onEnable has been invoked!");
    	
    	// Register all commands
    	this.getCommand("setclock").setExecutor(commandExecutor);
    	this.getCommand("setgraph").setExecutor(commandExecutor);
    	this.getCommand("cleardashboard").setExecutor(commandExecutor);
    	this.getCommand("import").setExecutor(commandExecutor);
    	this.getCommand("export").setExecutor(commandExecutor);
    	
    	this.getCommand("drawrect").setExecutor(commandExecutor); // ***DEV ONLY***
    	
    	getServer().getPluginManager().registerEvents(new CraftanaListener(), this); // register events
    	
    	// Register tick events, currently only updateAll() function
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
        
        // Run metric exporter with /metrics endpoint on port 25566
       	ServletContextHandler context = new ServletContextHandler();
       	context.setContextPath("/");
       	server.setHandler(context);
       	context.addServlet(new ServletHolder(new MCExporter()), "/metrics");
       	Thread serverRunner = new Thread() {
       	    public void run() {
       	        try {
       	            server.start();
       	            server.join();
       	        } catch(Exception e) {
       	            getLogger().info(stackTraceToString(e));
       	        }
       	    }  
       	};

       	serverRunner.start();
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    	clearAll(commandExecutor);
    	MCExporter.reset();
    	try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			getLogger().info(stackTraceToString(e));
		}
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