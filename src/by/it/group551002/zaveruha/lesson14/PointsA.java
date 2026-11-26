package by.it.group551002.zaveruha.lesson14;

import java.util.Scanner;
import java.util.Arrays;

public class PointsA {

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

    static double distance(double[] a, double[] b) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        double dz = a[2] - b[2];

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        double[][] points = new double[N][3];

        parent = new int[N];
        size = new int[N];

        for (int i = 0; i < N; i++) {
            points[i][0] = sc.nextDouble();
            points[i][1] = sc.nextDouble();
            points[i][2] = sc.nextDouble();

            parent[i] = i;
            size[i] = 1;
        }

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (distance(points[i], points[j]) < D) {
                    union(i, j);
                }
            }
        }

        int[] clusters = new int[N];
        int count = 0;

        for (int i = 0; i < N; i++) {
            if (find(i) == i) {
                clusters[count] = size[i];
                count++;
            }
        }

        Arrays.sort(clusters, 0, count);

        for (int i = count - 1; i >= 0; i--) {
            System.out.print(clusters[i] + " ");
        }

        sc.close();
    }
}
