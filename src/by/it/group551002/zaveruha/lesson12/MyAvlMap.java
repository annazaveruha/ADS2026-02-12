package by.it.group551002.zaveruha.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // Внутренний класс узла АВЛ-дерева
    private static class Node {
        Integer key;
        String value;
        int height;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Новый узел всегда имеет высоту 1
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (node.value == null ? value == null : node.value.equals(value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node current = root;
        while (current != null) {
            int cmp = k.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    // Вспомогательная переменная для сохранения старого значения при замене
    private String lastReplacedValue;

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        lastReplacedValue = null;
        root = insert(root, key, value);
        return lastReplacedValue;
    }

    private Node insert(Node node, Integer key, String value) {
        // 1. Стандартная вставка в BST
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            // Ключ уже существует, обновляем значение
            lastReplacedValue = node.value;
            node.value = value;
            return node;
        }

        // 2. Обновление высоты текущего узла
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // 3. Получение коэффициента сбалансированности
        int balance = getBalance(node);

        // 4. Балансировка дерева (4 случая)

        // Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        lastReplacedValue = null;
        root = deleteNode(root, (Integer) key);
        return lastReplacedValue;
    }

    private Node deleteNode(Node root, Integer key) {
        if (root == null) return null;

        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            root.left = deleteNode(root.left, key);
        } else if (cmp > 0) {
            root.right = deleteNode(root.right, key);
        } else {
            lastReplacedValue = root.value;
            size--;

            // Узел с одним или без детей
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                // Узел с двумя детьми: получаем наименьший в правом поддереве
                Node temp = minValueNode(root.right);
                root.key = temp.key;
                root.value = temp.value;
                // Компенсируем временное уменьшение size перед рекурсивным удалением дубликата
                size++;
                root.right = deleteNode(root.right, temp.key);
            }
        }

        if (root == null) return null;

        // Обновляем высоту
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // Балансировка после удаления
        int balance = getBalance(root);

        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            nodeLeftRight(root); // helper or inline
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private void nodeLeftRight(Node root) {
        // Just for clarity if needed
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // --- Вспомогательные методы АВЛ-дерева ---

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // --- Метод toString() ---

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;

        inOrderTraversal(node.left, sb);

        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);

        inOrderTraversal(node.right, sb);
    }

    // --- Заглушки для остальных методов интерфейса Map ---
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}