
package by.it.group551002.zaveruha.lesson13;

import java.util.*;

public class GraphC {

    static Map<String, List<String>> graph = new HashMap<>();
    static Map<String, List<String>> reverseGraph = new HashMap<>();

    static Set<String> visited = new HashSet<>();
    static List<String> order = new ArrayList<>();

    static List<List<String>> components = new ArrayList<>();

    static void dfs1(String vertex) {
        visited.add(vertex);

        for (String next : graph.get(vertex)) {
            if (!visited.contains(next)) {
                dfs1(next);
            }
        }

        order.add(vertex);
    }

    static void dfs2(String vertex, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String next : reverseGraph.get(vertex)) {
            if (!visited.contains(next)) {
                dfs2(next, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] vertices = edge.trim().split("->");

            String from = vertices[0].trim();
            String to = vertices[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            reverseGraph.putIfAbsent(from, new ArrayList<>());
            reverseGraph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
            reverseGraph.get(to).add(from);
        }

        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex);
            }
        }

        visited.clear();

        for (int i = order.size() - 1; i >= 0; i--) {
            String vertex = order.get(i);

            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();

                dfs2(vertex, component);

                Collections.sort(component);
                components.add(component);
            }
        }

        Map<String, Integer> componentNumber = new HashMap<>();

        for (int i = 0; i < components.size(); i++) {
            for (String vertex : components.get(i)) {
                componentNumber.put(vertex, i);
            }
        }

        List<Set<Integer>> componentGraph = new ArrayList<>();
        int[] degree = new int[components.size()];

        for (int i = 0; i < components.size(); i++) {
            componentGraph.add(new HashSet<>());
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                int a = componentNumber.get(from);
                int b = componentNumber.get(to);

                if (a != b && componentGraph.get(a).add(b)) {
                    degree[b]++;
                }
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(
                (a, b) -> components.get(a).get(0)
                        .compareTo(components.get(b).get(0))
        );

        for (int i = 0; i < degree.length; i++) {
            if (degree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (String vertex : components.get(current)) {
                System.out.print(vertex);
            }
            System.out.println();

            for (int next : componentGraph.get(current)) {
                degree[next]--;

                if (degree[next] == 0) {
                    queue.add(next);
                }
            }
        }

        scanner.close();
    }
}
