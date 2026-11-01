package by.it.group551002.zaveruha.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private class Node implements Map.Entry<Integer, String> {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public Integer getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }
    }

    private Node root;
    private int size;

    private void rotateRight(Node x) {
        Node p = x.parent;
        Node g = p.parent;

        p.left = x.right;

        if (x.right != null) {
            x.right.parent = p;
        }

        x.right = p;
        p.parent = x;
        x.parent = g;

        if (g == null) {
            root = x;
        } else if (g.left == p) {
            g.left = x;
        } else {
            g.right = x;
        }
    }

    private void rotateLeft(Node x) {
        Node p = x.parent;
        Node g = p.parent;

        p.right = x.left;

        if (x.left != null) {
            x.left.parent = p;
        }

        x.left = p;
        p.parent = x;
        x.parent = g;

        if (g == null) {
            root = x;
        } else if (g.left == p) {
            g.left = x;
        } else {
            g.right = x;
        }
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                if (p.left == x) {
                    rotateRight(x);
                } else {
                    rotateLeft(x);
                }
            } else if (g.left == p && p.left == x) {
                rotateRight(p);
                rotateRight(x);
            } else if (g.right == p && p.right == x) {
                rotateLeft(p);
                rotateLeft(x);
            } else if (g.left == p && p.right == x) {
                rotateLeft(x);
                rotateRight(x);
            } else {
                rotateRight(x);
                rotateLeft(x);
            }
        }
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node last = null;

        while (current != null) {
            last = current;

            if (key.equals(current.key)) {
                splay(current);
                return current;
            }

            if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (last != null) {
            splay(last);
        }

        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            root = new Node(key, value, null);
            size = 1;
            return null;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;

            if (key.equals(current.key)) {
                String old = current.value;
                current.value = value;
                splay(current);
                return old;
            }

            if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node node = new Node(key, value, parent);

        if (key < parent.key) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        size++;
        splay(node);

        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }

        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValue(node.left, value)
                || containsValue(node.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Node node = findNode((Integer) key);

        if (node == null) {
            return null;
        }

        String old = node.value;
        Node left = node.left;
        Node right = node.right;

        if (left != null) {
            left.parent = null;
        }

        if (right != null) {
            right.parent = null;
        }

        if (left == null) {
            root = right;
        } else {
            root = left;

            Node max = left;
            while (max.right != null) {
                max = max.right;
            }

            splay(max);

            root.right = right;

            if (right != null) {
                right.parent = root;
            }
        }

        size--;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        appendInOrder(root, result, new boolean[]{true});
        result.append("}");
        return result.toString();
    }

    private void appendInOrder(
            Node node, StringBuilder result, boolean[] first) {

        if (node == null) {
            return;
        }

        appendInOrder(node.left, result, first);

        if (!first[0]) {
            result.append(", ");
        }

        result.append(node.key).append("=").append(node.value);
        first[0] = false;

        appendInOrder(node.right, result, first);
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }

        Node node = root;

        while (node.left != null) {
            node = node.left;
        }

        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }

        Node node = root;

        while (node.right != null) {
            node = node.right;
        }

        splay(node);
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = root;
        Node best = null;

        while (node != null) {
            if (node.key < key) {
                best = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (best == null) {
            return null;
        }

        Integer result = best.key;
        splay(best);
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = root;
        Node best = null;

        while (node != null) {
            if (node.key <= key) {
                best = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (best == null) {
            return null;
        }

        Integer result = best.key;
        splay(best);
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = root;
        Node best = null;

        while (node != null) {
            if (node.key >= key) {
                best = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (best == null) {
            return null;
        }

        Integer result = best.key;
        splay(best);
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = root;
        Node best = null;

        while (node != null) {
            if (node.key > key) {
                best = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (best == null) {
            return null;
        }

        Integer result = best.key;
        splay(best);
        return result;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer key) {
        MySplayMap result = new MySplayMap();
        copyLess(root, key, result);
        return result;
    }

    private void copyLess(Node node, Integer key, MySplayMap result) {
        if (node == null) {
            return;
        }

        copyLess(node.left, key, result);

        if (node.key < key) {
            result.put(node.key, node.value);
            copyLess(node.right, key, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer key) {
        MySplayMap result = new MySplayMap();
        copyGreaterOrEqual(root, key, result);
        return result;
    }

    private void copyGreaterOrEqual(
            Node node, Integer key, MySplayMap result) {

        if (node == null) {
            return;
        }

        copyGreaterOrEqual(node.left, key, result);

        if (node.key >= key) {
            result.put(node.key, node.value);
        }

        copyGreaterOrEqual(node.right, key, result);
    }

    // Остальные обязательные методы интерфейса

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(
            Integer fromKey, boolean fromInclusive,
            Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(
            Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(
            Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<Integer, String> subMap(
            Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }
}