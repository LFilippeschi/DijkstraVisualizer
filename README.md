# DijkstraVisualizer

After my course in Algorithm and Data structures i was interested in visualizing the Dijkstra Algorithm and compare it with my previous 
"StationVisualizer" project. 
There's two ways of running the program, trace every step of the algorithm or just show the shortest path. The first will take a very long time 
because the drawing function is relatively slow to the raw computation, while the second will be very fast. Much faster than my brute force 
implementation in the previous project. 

There's also two possibilities in drawing the nodes: In a random manner or in a cluster manner.
In the first Nodes will spawn randomly on the screen and the edges will also be random resulting in a big mess to get anything meaningful out of it.
The second will first spawn random clusters on the screen, in each cluster Nodes will be created in a random radius (10<r<100) away from from the 
center of the cluster.
The sparcity of each network can be selected at initialization. 

Random Implementation.

![image](https://drive.google.com/uc?export=view&id=1rRZFbXCOgvP8MsVpADM99dyw7_ryga1X)

Cluster Implementation.

![image](https://drive.google.com/uc?export=view&id=1Whkh2A9FZ7W57uj20se1m29__N-vs9tV)
