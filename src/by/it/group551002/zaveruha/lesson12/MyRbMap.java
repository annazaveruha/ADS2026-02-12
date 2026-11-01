package by.it.group551002.zaveruha.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // Внутренний класс узла красно-черного дерева
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
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
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public String get(Object key) {
        Node node = getNode((Integer) key);
        return node == null ? null : node.value;
    }

    private Node getNode(Integer key) {
        if (key == null) return null;
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        if (root == null) {
            root = new Node(key, value, BLACK, null);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        Node newNode = new Node(key, value, RED, parent);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    private void fixAfterInsertion(Node x) {
        while (x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node y = x.parent.parent.right; // Дядя
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.left; // Дядя
                if (y != null && y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    @Override
    public String remove(Object key) {
        Node node = getNode((Integer) key);
        if (node == null) return null;
        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    private void deleteNode(Node p) {
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null) ? p.left : p.right;

        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }

            p.left = p.right = p.parent = null;

            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            root = null;
        } else {
            if (p.color == BLACK) {
                fixAfterDeletion(p);
            }
            if (p.parent != null) {
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else if (p == p.parent.right) {
                    p.parent.right = null;
                }
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private Node successor(Node t) {
        if (t == null) return null;
        else if (t.right != null) {
            Node p = t.right;
            while (p.left != null) p = p.left;
            return p;
        } else {
            Node p = t.parent;
            Node ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null) r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null) root = r;
            else if (p.parent.left == p) p.parent.left = r;
            else p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null) root = l;
            else if (p.parent.right == p) p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    private static boolean colorOf(Node p) {
        return (p == null) ? BLACK : p.color;
    }

    private static Node parentOf(Node p) {
        return (p == null) ? null : p.parent;
    }

    private static void setColor(Node p, boolean c) {
        if (p != null) p.color = c;
    }

    private static Node leftOf(Node p) {
        return (p == null) ? null : p.left;
    }

    private static Node rightOf(Node p) {
        return (p == null) ? null : p.right;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            sub.put(node.key, node.value);
            fillHeadMap(node.left, toKey, sub);
            fillHeadMap(node.right, toKey, sub);
        } else {
            fillHeadMap(node.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            sub.put(node.key, node.value);
            fillTailMap(node.left, fromKey, sub);
            fillTailMap(node.right, fromKey, sub);
        } else {
            fillTailMap(node.right, fromKey, sub);
        }
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

    // --- Остальные обязательные методы интерфейса SortedMap (заглушки/реализации по минимуму) ---
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }
}