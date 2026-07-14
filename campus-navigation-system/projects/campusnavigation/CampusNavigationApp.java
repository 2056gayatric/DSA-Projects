package projects.campusnavigation;

import java.util.*;

public class CampusNavigationApp {
    private static Graph campusMap;

    public static void main(String[] args) {
        initializeMap();
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================");
        System.out.println("   Rowan University Campus Navigation System     ");
        System.out.println("=================================================");
        System.out.println("Status: Graph successfully loaded with 20 nodes.");

        while (true) {
            printMenu();
            System.out.print("Select an option (1-6): ");
            String choiceStr = scanner.nextLine().trim();
            if (choiceStr.isEmpty()) continue;

            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 6.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleFindRoute(scanner);
                    break;
                case 2:
                    handleViewMap();
                    break;
                case 3:
                    handleToggleBlockage(scanner);
                    break;
                case 4:
                    handleViewBlockages();
                    break;
                case 5:
                    campusMap.clearAllBlockages();
                    System.out.println("\n[Success] All blockages cleared. Map restored to default.");
                    break;
                case 6:
                    System.out.println("\nThank you for using Rowan Campus Navigation. Safe travels!");
                    return;
                default:
                    System.out.println("Invalid selection. Choose a number between 1 and 6.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n-------------------------------------------------");
        System.out.println("1. Find Shortest Route");
        System.out.println("2. View Campus Map Connections");
        System.out.println("3. Toggle Blockage (Construction/Reroute)");
        System.out.println("4. View Active Blockages");
        System.out.println("5. Reset All Blockages");
        System.out.println("6. Exit");
        System.out.println("-------------------------------------------------");
    }

    private static void initializeMap() {
        campusMap = new Graph();

        // Add 20+ Rowan University Locations
        campusMap.addVertex("Chamberlain Student Center");
        campusMap.addVertex("Campbell Library");
        campusMap.addVertex("Savitz Hall");
        campusMap.addVertex("Robinson Hall");
        campusMap.addVertex("Science Hall");
        campusMap.addVertex("Rowan Hall");
        campusMap.addVertex("Engineering Hall");
        campusMap.addVertex("Business Hall");
        campusMap.addVertex("Bozorth Hall");
        campusMap.addVertex("Bunce Hall");
        campusMap.addVertex("Wilson Hall");
        campusMap.addVertex("Esby Gym");
        campusMap.addVertex("Rec Center");
        campusMap.addVertex("Barnes & Noble");
        campusMap.addVertex("Hollybush Mansion");
        campusMap.addVertex("Oak Hall");
        campusMap.addVertex("Laurel Hall");
        campusMap.addVertex("Mimosa Hall");
        campusMap.addVertex("Chestnut Hall");
        campusMap.addVertex("Magnolia Hall");

        // Connect locations with weights (distances in meters)
        // Student Center area
        campusMap.addEdge("Chamberlain Student Center", "Campbell Library", 120);
        campusMap.addEdge("Chamberlain Student Center", "Savitz Hall", 150);
        campusMap.addEdge("Chamberlain Student Center", "Rec Center", 200);
        campusMap.addEdge("Chamberlain Student Center", "Laurel Hall", 180);

        // Library area
        campusMap.addEdge("Campbell Library", "Robinson Hall", 100);
        campusMap.addEdge("Campbell Library", "Science Hall", 140);
        campusMap.addEdge("Campbell Library", "Savitz Hall", 110);

        // Academic core
        campusMap.addEdge("Savitz Hall", "Robinson Hall", 90);
        campusMap.addEdge("Savitz Hall", "Bunce Hall", 220);
        campusMap.addEdge("Robinson Hall", "Science Hall", 80);
        campusMap.addEdge("Robinson Hall", "Wilson Hall", 170);
        campusMap.addEdge("Science Hall", "Rowan Hall", 130);
        campusMap.addEdge("Science Hall", "Engineering Hall", 150);

        // Engineering & Business
        campusMap.addEdge("Rowan Hall", "Engineering Hall", 50);
        campusMap.addEdge("Engineering Hall", "Business Hall", 160);
        campusMap.addEdge("Rowan Hall", "Bozorth Hall", 210);

        // West Campus (Bozorth, Bunce, Wilson)
        campusMap.addEdge("Bozorth Hall", "Bunce Hall", 180);
        campusMap.addEdge("Bunce Hall", "Wilson Hall", 130);
        campusMap.addEdge("Wilson Hall", "Hollybush Mansion", 250);

        // Recreation & Bookstore
        campusMap.addEdge("Rec Center", "Esby Gym", 80);
        campusMap.addEdge("Esby Gym", "Barnes & Noble", 190);
        campusMap.addEdge("Savitz Hall", "Barnes & Noble", 300);

        // Housing / Residence Halls
        campusMap.addEdge("Laurel Hall", "Oak Hall", 90);
        campusMap.addEdge("Oak Hall", "Mimosa Hall", 110);
        campusMap.addEdge("Mimosa Hall", "Chestnut Hall", 100);
        campusMap.addEdge("Chestnut Hall", "Magnolia Hall", 120);
        campusMap.addEdge("Magnolia Hall", "Chamberlain Student Center", 240);
        campusMap.addEdge("Laurel Hall", "Chestnut Hall", 150);
    }

    private static void handleFindRoute(Scanner scanner) {
        System.out.println("\n--- Find Route ---");
        List<String> list = new ArrayList<>(campusMap.getVertices());
        Collections.sort(list);

        System.out.println("Available Locations:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, list.get(i));
        }

        System.out.print("Enter Start Location Name or Index: ");
        String startVal = scanner.nextLine().trim();
        System.out.print("Enter Destination Name or Index: ");
        String destVal = scanner.nextLine().trim();

        String start = resolveLocation(startVal, list);
        String dest = resolveLocation(destVal, list);

        if (start == null || dest == null) {
            System.out.println("[Error] One or both locations are invalid. Please check your spelling or indices.");
            return;
        }

        System.out.println("\nCalculating path from [" + start + "] to [" + dest + "]...");
        Dijkstra.PathResult result = Dijkstra.findShortestPath(campusMap, start, dest);

        if (result.path == null || result.path.isEmpty()) {
            System.out.println("[No Route Found] There is no accessible path due to active blockages.");
        } else {
            System.out.println("\n[Optimal Route]");
            System.out.println(result.toString());
        }
    }

    private static void handleViewMap() {
        System.out.println("\n--- Campus Map (Active Edges) ---");
        List<String> sortedVertices = new ArrayList<>(campusMap.getVertices());
        Collections.sort(sortedVertices);

        for (String node : sortedVertices) {
            String status = campusMap.isNodeBlocked(node) ? " [BLOCKED]" : "";
            System.out.print(node + status + " is connected to:\n");
            List<Graph.Edge> neighbors = campusMap.getNeighbors(node);
            if (neighbors.isEmpty()) {
                System.out.println("  * No active connections");
            } else {
                for (Graph.Edge edge : neighbors) {
                    System.out.printf("  -> %-30s | Distance: %.1f meters\n", edge.target, edge.weight);
                }
            }
        }
    }

    private static void handleToggleBlockage(Scanner scanner) {
        System.out.println("\n--- Toggle Blockages ---");
        System.out.println("1. Block/Unblock a Location (Node)");
        System.out.println("2. Block/Unblock a Path between two Locations (Edge)");
        System.out.print("Choose option (1-2): ");
        String modeStr = scanner.nextLine().trim();

        List<String> list = new ArrayList<>(campusMap.getVertices());
        Collections.sort(list);

        if ("1".equals(modeStr)) {
            System.out.print("Enter Location Name or Index to toggle: ");
            String input = scanner.nextLine().trim();
            String node = resolveLocation(input, list);
            if (node == null) {
                System.out.println("[Error] Invalid location.");
                return;
            }

            if (campusMap.isNodeBlocked(node)) {
                campusMap.unblockNode(node);
                System.out.println("[Success] Location " + node + " is now UNBLOCKED.");
            } else {
                campusMap.blockNode(node);
                System.out.println("[Success] Location " + node + " is now BLOCKED.");
            }
        } else if ("2".equals(modeStr)) {
            System.out.print("Enter Location 1 Name or Index: ");
            String input1 = scanner.nextLine().trim();
            System.out.print("Enter Location 2 Name or Index: ");
            String input2 = scanner.nextLine().trim();

            String node1 = resolveLocation(input1, list);
            String node2 = resolveLocation(input2, list);

            if (node1 == null || node2 == null) {
                System.out.println("[Error] Invalid location(s).");
                return;
            }

            if (campusMap.isEdgeBlocked(node1, node2)) {
                campusMap.unblockEdge(node1, node2);
                System.out.println("[Success] Path between " + node1 + " and " + node2 + " is now UNBLOCKED.");
            } else {
                campusMap.blockEdge(node1, node2);
                System.out.println("[Success] Path between " + node1 + " and " + node2 + " is now BLOCKED.");
            }
        } else {
            System.out.println("[Error] Invalid choice.");
        }
    }

    private static void handleViewBlockages() {
        System.out.println("\n--- Active Blockages ---");
        Set<String> blockedNodes = campusMap.getBlockedNodes();
        Set<String> blockedEdges = campusMap.getBlockedEdges();

        if (blockedNodes.isEmpty() && blockedEdges.isEmpty()) {
            System.out.println("No active blockages on the map.");
            return;
        }

        if (!blockedNodes.isEmpty()) {
            System.out.println("Blocked Locations (Nodes):");
            for (String node : blockedNodes) {
                System.out.println("  - " + node);
            }
        }

        if (!blockedEdges.isEmpty()) {
            System.out.println("Blocked Paths (Edges):");
            for (String edge : blockedEdges) {
                System.out.println("  - " + edge);
            }
        }
    }

    private static String resolveLocation(String input, List<String> list) {
        if (input == null || input.isEmpty()) return null;

        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < list.size()) {
                return list.get(index);
            }
        } catch (NumberFormatException e) {
            // Check by substring match or exact match
            for (String loc : list) {
                if (loc.equalsIgnoreCase(input) || loc.toLowerCase().contains(input.toLowerCase())) {
                    return loc;
                }
            }
        }
        return null;
    }
}
