package projects.campusnavigation;

import java.util.*;

public class Graph {
    public static class Edge {
        public final String target;
        public final double weight;

        public Edge(String target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();
    private final Set<String> blockedNodes = new HashSet<>();
    private final Set<String> blockedEdges = new HashSet<>(); // Format: "node1<->node2" (sorted alphabetically)

    public void addVertex(String label) {
        adjacencyList.putIfAbsent(label, new ArrayList<>());
    }

    public void addEdge(String source, String destination, double weight) {
        addVertex(source);
        addVertex(destination);
        adjacencyList.get(source).add(new Edge(destination, weight));
        adjacencyList.get(destination).add(new Edge(source, weight)); // Undirected graph
    }

    public List<Edge> getNeighbors(String vertex) {
        if (blockedNodes.contains(vertex)) {
            return Collections.emptyList();
        }
        List<Edge> activeNeighbors = new ArrayList<>();
        List<Edge> allNeighbors = adjacencyList.getOrDefault(vertex, Collections.emptyList());
        for (Edge edge : allNeighbors) {
            if (!blockedNodes.contains(edge.target) && !isEdgeBlocked(vertex, edge.target)) {
                activeNeighbors.add(edge);
            }
        }
        return activeNeighbors;
    }

    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    // Blockage management
    public void blockNode(String node) {
        if (adjacencyList.containsKey(node)) {
            blockedNodes.add(node);
        }
    }

    public void unblockNode(String node) {
        blockedNodes.remove(node);
    }

    public void blockEdge(String node1, String node2) {
        blockedEdges.add(getEdgeKey(node1, node2));
    }

    public void unblockEdge(String node1, String node2) {
        blockedEdges.remove(getEdgeKey(node1, node2));
    }

    public void clearAllBlockages() {
        blockedNodes.clear();
        blockedEdges.clear();
    }

    public boolean isNodeBlocked(String node) {
        return blockedNodes.contains(node);
    }

    public boolean isEdgeBlocked(String node1, String node2) {
        return blockedEdges.contains(getEdgeKey(node1, node2));
    }

    public Set<String> getBlockedNodes() {
        return Collections.unmodifiableSet(blockedNodes);
    }

    public Set<String> getBlockedEdges() {
        return Collections.unmodifiableSet(blockedEdges);
    }

    private String getEdgeKey(String node1, String node2) {
        return node1.compareTo(node2) < 0 ? node1 + "<->" + node2 : node2 + "<->" + node1;
    }
}
