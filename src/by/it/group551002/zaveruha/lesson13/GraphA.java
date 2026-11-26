
package by.it.group551002.zaveruha.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> degree = new HashMap<>();

        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] vertices = edge.trim().split("->");

            String from = vertices[0].trim();
            String to = vertices[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            degree.putIfAbsent(from, 0);
            degree.put(to, degree.getOrDefault(to, 0) + 1);
        }

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : degree.keySet()) {
            if (degree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            for (String next : graph.get(vertex)) {
                degree.put(next, degree.get(next) - 1);

                if (degree.get(next) == 0) {
                    queue.add(next);
                }
            }
        }

        if (result.size() == graph.size()) {
            for (String vertex : result) {
                System.out.print(vertex + " ");
            }
        }

        scanner.close();
    }
}
