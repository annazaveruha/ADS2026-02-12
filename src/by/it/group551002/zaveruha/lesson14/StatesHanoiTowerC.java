
package by.it.group551002.zaveruha.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] size;

    static int[] height = new int[3];
    static int[] firstStep;

    static int step = 1;

    // Поиск корня с сжатием пути
    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // Объединение двух множеств
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

    // Выполнение одного хода
    static void move(int from, int to) {
        height[from]--;
        height[to]++;

        int maxHeight = Math.max(height[0],
                Math.max(height[1], height[2]));

        if (firstStep[maxHeight] == -1) {
            firstStep[maxHeight] = step;
        } else {
            union(step, firstStep[maxHeight]);
        }

        step++;
    }

    // Рекурсивное решение Ханойских башен
    static void hanoi(int n, int from, int to, int temp) {
        if (n == 0) {
            return;
        }

        hanoi(n - 1, from, temp, to);

        move(from, to);

        hanoi(n - 1, temp, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        step = 1;

        height[0] = n;
        height[1] = 0;
        height[2] = 0;

        int moves = (1 << n) - 1;

        parent = new int[moves + 1];
        size = new int[moves + 1];

        firstStep = new int[n + 1];
        // Инициализация DSU
        for (int i = 1; i <= moves; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Инициализация массива первых ходов
        for (int i = 0; i <= n; i++) {
            firstStep[i] = -1;
        }

        // Все диски изначально находятся на первом стержне
        height[0] = n;
        height[1] = 0;
        height[2] = 0;

        // Перемещение дисков с первого стержня на второй
        hanoi(n, 0, 1, 2);

        // Получение размеров компонент
        int[] result = new int[moves];
        int count = 0;

        for (int i = 1; i <= moves; i++) {
            if (find(i) == i) {
                result[count] = size[i];
                count++;
            }
        }

        // Сортировка по возрастанию
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (result[j] > result[j + 1]) {
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        // Вывод результата
        for (int i = 0; i < count; i++) {
            System.out.print(result[i] + " ");
        }

        sc.close();
    }
}
