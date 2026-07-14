package projects.campusnavigation;

import java.util.*;

public class Dijkstra {
    public static class PathResult {
        public final List<String> path;
        public final double totalDistance;

        public PathResult(List<String> path, double totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }

        @Override
        public String toString() {
            if (path == null || path.isEmpty()) {
                return "No route found.";
            }
            return String.join(" -> ", path) + " (Distance: " + String.format("%.1f", totalDistance) + " meters)";
        }
    }

    private static class PQNode implements Comparable<PQNode> {
        final String vertex;
        final double distance;

        PQNode(String vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(PQNode other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public static PathResult findShortestPath(Graph graph, String start, String destination) {
        if (!graph.getVertices().contains(start) || !graph.getVertices().contains(destination)) {
            return new PathResult(Collections.emptyList(), 0);
        }

        if (graph.isNodeBlocked(start) || graph.isNodeBlocked(destination)) {
            return new PathResult(Collections.emptyList(), 0);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(new PQNode(start, 0.0));

        while (!pq.isEmpty()) {
            PQNode current = pq.poll();
            String u = current.vertex;

            if (u.equals(destination)) {
                break;
            }

            if (visited.contains(u)) {
                continue;
            }
            visited.add(u);

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                String v = edge.target;
                if (visited.contains(v)) {
                    continue;
                }

                double alt = distances.get(u) + edge.weight;
                if (alt < distances.get(v)) {
                    distances.put(v, alt);
                    predecessors.put(v, u);
                    pq.add(new PQNode(v, alt));
                }
            }
        }

        if (distances.get(destination) == Double.MAX_VALUE) {
            return new PathResult(Collections.emptyList(), 0);
        }

        // Reconstruct path
        List<String> path = new LinkedList<>();
        String step = destination;
        while (step != null) {
            path.add(0, step);
            step = predecessors.get(step);
        }

        return new PathResult(path, distances.get(destination));
    }
}
