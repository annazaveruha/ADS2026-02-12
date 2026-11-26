
package by.it.group551002.zaveruha.lesson14;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class SitesB {

    static int[] parent;
    static int[] size;

    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA != rootB) {
            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }

            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        HashMap<String, Integer> sites = new HashMap<>();
        ArrayList<int[]> connections = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.equals("end")) {
                break;
            }

            String[] pair = line.split("\\+");

            String siteA = pair[0];
            String siteB = pair[1];

            if (!sites.containsKey(siteA)) {
                sites.put(siteA, sites.size());
            }

            if (!sites.containsKey(siteB)) {
                sites.put(siteB, sites.size());
            }

            int a = sites.get(siteA);
            int b = sites.get(siteB);

            connections.add(new int[]{a, b});
        }

        int n = sites.size();

        parent = new int[n];
        size = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        for (int[] connection : connections) {
            union(connection[0], connection[1]);
        }

        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                result.add(size[i]);
            }
        }

        Collections.sort(result, Collections.reverseOrder());

        for (int value : result) {
            System.out.print(value + " ");
        }

        sc.close();
    }
}
