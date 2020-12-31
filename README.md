Craftana  
=========

This project makes metrics visualisation with prometheus possible in minecraft!  
  
Commands:  
---------
* /sethistogram \<Histogram ID\> \<Prometheus address\> \<min value\> \<max value\> \<PromQL Query\>
* /setclock \<Clock ID\> \<Prometheus address\> \<max value (can also be auto)\> \<PromQL Query\>  
* /setgraph \<Graph ID\> \<Prometheus address\> \<step (seconds)\> \<max value (can also be auto)\> \<PromQL Query\>  
* /cleardashboard  
* /import \<path e.g. /path/to/config/file.json\>  
* /export \<path e.g. /path/to/config/file.json\>  
  
Exporter:  
---------
Craftana also exports some basic metrics such as:  
* craftana_online_players - A gauge. Current amount of online players.  
* craftana_histograms - A gauge. Current amount of histograms in the dashboard.  
* craftana_clocks - A gauge. Current amount of clocks in the dashboard.  
* craftana_graphs - A gauge. Current amount of graphs in the dashboard.  
  
Docker:
-------
```
docker run -it -p 25565:25565 -p 25566:25566 --name craftana_server ravidshachar/craftana:0.3.0
```  
