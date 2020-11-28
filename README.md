Craftana  
=========

This project allows for metrics visualisation with prometheus in minecraft.  
  
Commands:  
---------
* /setclock <Clock ID> <Prometheus address> <max value (can also be auto)> <PromQL Query>  
* /setgraph <Graph ID> <Prometheus address> <step (seconds)> <max value (can also be auto)> <PromQL Query>  
* /cleardashboard  
* /import <path e.g. /path/to/config/file.json>  
* /export <path e.g. /path/to/config/file.json>  
  
Exporter:  
---------
Craftana also exports some basic metrics such as:  
* craftana_online_players - A gauge. Current amount of online players.  
* craftana_clocks - A gauge. Current amount of clocks in the dashboard.  
* craftana_graphs - A gauge. Current amount of graphs in the dashboard.