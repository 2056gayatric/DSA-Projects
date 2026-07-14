# Campus Navigation System

A graph-based Java command-line application designed to compute and display optimal walking routes across 20+ Rowan University campus locations, supporting dynamic rerouting around construction blockages and blocked pathways.

## Features
- **Graph Representation**: Models campus landmarks (Chamberlain Student Center, Campbell Library, Savitz Hall, Rowan Hall, etc.) as vertices and walking pathways as weighted edges (distances in meters).
- **Dijkstra's Shortest Path Algorithm**: Computes the optimal path in $O(E \log V)$ time using a custom Min-Priority Queue.
- **Dynamic Path Rerouting**: Allows simulated path blockages (due to events or construction) and automatically recalculates alternative shortest pathways on the remaining graph.
- **Console User Interface**: Clean, decoupled, menu-driven CLI separating UI console logic from graph traversal algorithms.

## Class Architecture
- `Graph.java`: Maintains the vertices, adjacency lists, and active/blocked states.
- `Dijkstra.java`: Encapsulates path calculation and node history parsing.
- `CampusNavigationApp.java`: Configures the Rowan campus graph and handles console user input/output.

## Getting Started

### Prerequisites
- Java SE Development Kit (JDK 8 or higher)

### Compiling
Compile all Java source files from the project directory:
```bash
javac -d bin src/*.java
```

### Running
Run the compiled application:
```bash
java -cp bin projects.campusnavigation.CampusNavigationApp
```
