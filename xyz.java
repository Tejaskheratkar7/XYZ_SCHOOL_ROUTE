package com.xyz.schoolbus;

import java.util.*;

class Edge {
    String dest;
    int weight;

    Edge(String dest, int weight) {
        this.dest = dest;
        this.weight = weight;
    }
}

class Node implements Comparable<Node> {
    String name;
    int dist;

    Node(String name, int dist) {
        this.name = name;
        this.dist = dist;
    }

    public int compareTo(Node other) {
        return this.dist - other.dist;
    }
}

class BusStop {
    String name;
    int distanceFromSchool;

    BusStop(String name, int distanceFromSchool) {
        this.name = name;
        this.distanceFromSchool = distanceFromSchool;
    }
}

public class XYZSchoolBusRouteSystem {

    static List<BusStop> stops = new ArrayList<>();
    static Map<String, List<Edge>> graph = new HashMap<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        initializeStops();

        int choice;
        do {
            System.out.println("\n--- XYZ SCHOOL BUS ROUTE SYSTEM ---");
            System.out.println("1. Display Bus Stops");
            System.out.println("2. Display Shortest Route from Home");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    displayBusStops();
                    break;

                case 2:
                    System.out.print("\nEnter distance from Home to School (km): ");
                    int homeDistance = sc.nextInt();
                    findShortestRoute(homeDistance);
                    break;

                case 3:
                    System.out.println("Exiting system.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 3);

        sc.close();
    }

    static void initializeStops() {
        stops.add(new BusStop("Stop A", 3));
        stops.add(new BusStop("Stop B", 5));
        stops.add(new BusStop("Stop C", 9));
        stops.add(new BusStop("Stop D", 12));
        stops.add(new BusStop("Stop E", 14));
    }

    static void displayBusStops() {
        System.out.println("\nBus Stops (distance from School):");
        for (BusStop stop : stops) {
            System.out.println(stop.name + " - " + stop.distanceFromSchool + " km");
        }
    }

    static void findShortestRoute(int homeDistance) {

        graph.clear();

        BusStop nearestStop = findNearestStop(homeDistance);

        System.out.println("\nNearest Bus Stop: " + nearestStop.name);

        buildGraph(homeDistance, nearestStop);

        dijkstra("Home", "School");
    }


    static BusStop findNearestStop(int homeDistance) {

        BusStop nearest = null;
        int minDiff = Integer.MAX_VALUE;

        for (BusStop stop : stops) {
            int diff = Math.abs(homeDistance - stop.distanceFromSchool);
            if (diff < minDiff) {
                minDiff = diff;
                nearest = stop;
            }
        }
        return nearest;
    }

    static void buildGraph(int homeDistance, BusStop nearestStop) {

        addEdge("Home", nearestStop.name,
                Math.abs(homeDistance - nearestStop.distanceFromSchool));

        addEdge(nearestStop.name, "School",
                nearestStop.distanceFromSchool);

        addEdge("School", "Stop A", 4);
        addEdge("School", "Stop B", 3);

        addEdge("Stop A", "Stop C", 4);
        addEdge("Stop B", "Stop C", 6);
        addEdge("Stop B", "Stop D", 5);

        addEdge("Stop C", "Stop E", 2);
        addEdge("Stop D", "Stop E", 3);
    }

    static void addEdge(String u, String v, int w) {
        graph.putIfAbsent(u, new ArrayList<>());
        graph.putIfAbsent(v, new ArrayList<>());
        graph.get(u).add(new Edge(v, w));
        graph.get(v).add(new Edge(u, w));
    }

    // DIJKSTRA Algorithm

    static void dijkstra(String src, String dest) {

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> edgeUsed = new HashMap<>();

        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (String node : graph.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
        }

        dist.put(src, 0);
        pq.add(new Node(src, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            for (Edge e : graph.get(current.name)) {
                int newDist = dist.get(current.name) + e.weight;

                if (newDist < dist.get(e.dest)) {
                    dist.put(e.dest, newDist);
                    parent.put(e.dest, current.name);
                    edgeUsed.put(e.dest, e.weight);
                    pq.add(new Node(e.dest, newDist));
                }
            }
        }

        printRoute(parent, edgeUsed, src, dest, dist.get(dest));
    }


    static void printRoute(Map<String, String> parent,
                           Map<String, Integer> edgeUsed,
                           String src,
                           String dest,
                           int totalDist) {

        List<String> path = new ArrayList<>();
        String current = dest;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);

        System.out.println("\nShortest Route:");
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.println(path.get(i) + " --(" +
                    edgeUsed.get(path.get(i + 1)) + " km)--> " +
                    path.get(i + 1));
        }

        System.out.println("Total Distance Covered = " + totalDist + " km");
    }
}




