package by.it.group551002.zaveruha.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private Node<E>[] table;

    private int size;

    // Первый и последний элементы
    // в порядке добавления
    private Node<E> first;
    private Node<E> last;

    private static class Node<E> {
        E value;

        // Следующий элемент в хеш-цепочке
        Node<E> next;

        // Следующий элемент в порядке добавления
        Node<E> orderNext;

        Node(E value) {
            this.value = value;
        }
    }

    public MyLinkedHashSet() {
        table = new Node[16];
    }

    private int getIndex(Object obj) {
        if (obj == null) {
            return 0;
        }

        return (obj.hashCode() & 0x7fffffff) % table.length;
    }

    // --------------------------------------------------
    // size
    // --------------------------------------------------

    @Override
    public int size() {
        return size;
    }

    // --------------------------------------------------
    // isEmpty
    // --------------------------------------------------

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // --------------------------------------------------
    // contains
    // --------------------------------------------------

    @Override
    public boolean contains(Object obj) {

        int index = getIndex(obj);

        Node<E> current = table[index];

        while (current != null) {

            if (obj == null && current.value == null) {
                return true;
            }

            if (obj != null && obj.equals(current.value)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    // --------------------------------------------------
    // add
    // --------------------------------------------------

    @Override
    public boolean add(E obj) {

        int index = getIndex(obj);

        // Если элемент уже есть,
        // ничего не добавляем
        if (contains(obj)) {
            return false;
        }

        Node<E> newNode = new Node<>(obj);

        // Добавляем в хеш-таблицу
        newNode.next = table[index];
        table[index] = newNode;

        // Добавляем в список порядка
        if (first == null) {
            first = newNode;
            last = newNode;
        } else {
            last.orderNext = newNode;
            last = newNode;
        }

        size++;

        return true;
    }

    // --------------------------------------------------
    // remove
    // --------------------------------------------------

    @Override
    public boolean remove(Object obj) {

        int index = getIndex(obj);

        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {

            boolean equal;

            if (obj == null) {
                equal = current.value == null;
            } else {
                equal = obj.equals(current.value);
            }

            if (equal) {

                // Удаляем из хеш-таблицы
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                // Удаляем из списка порядка добавления
                removeFromOrder(current);

                size--;

                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    // --------------------------------------------------
    // Удаление из списка порядка добавления
    // --------------------------------------------------

    private void removeFromOrder(Node<E> node) {

        // Если удаляем первый
        if (node == first) {
            first = first.orderNext;

            if (first == null) {
                last = null;
            }

            return;
        }

        Node<E> current = first;

        while (current != null) {

            if (current.orderNext == node) {

                current.orderNext = node.orderNext;

                // Если удаляем последний
                if (node == last) {
                    last = current;
                }

                return;
            }

            current = current.orderNext;
        }
    }

    // --------------------------------------------------
    // clear
    // --------------------------------------------------

    @Override
    public void clear() {

        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        first = null;
        last = null;

        size = 0;
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {

        String result = "[";

        Node<E> current = first;

        boolean firstElement = true;

        while (current != null) {

            if (!firstElement) {
                result += ", ";
            }

            result += current.value;

            firstElement = false;

            current = current.orderNext;
        }

        result += "]";

        return result;
    }

    // --------------------------------------------------
    // containsAll
    // --------------------------------------------------

    @Override
    public boolean containsAll(Collection<?> c) {

        for (Object obj : c) {

            if (!contains(obj)) {
                return false;
            }
        }

        return true;
    }

    // --------------------------------------------------
    // addAll
    // --------------------------------------------------

    @Override
    public boolean addAll(Collection<? extends E> c) {

        boolean changed = false;

        for (E obj : c) {

            if (add(obj)) {
                changed = true;
            }
        }

        return changed;
    }

    // --------------------------------------------------
    // removeAll
    // --------------------------------------------------

    @Override
    public boolean removeAll(Collection<?> c) {

        boolean changed = false;

        for (Object obj : c) {

            if (remove(obj)) {
                changed = true;
            }
        }

        return changed;
    }

    // --------------------------------------------------
    // retainAll
    // --------------------------------------------------

    @Override
    public boolean retainAll(Collection<?> c) {

        boolean changed = false;

        Node<E> current = first;

        while (current != null) {

            Node<E> next = current.orderNext;

            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }

            current = next;
        }

        return changed;
    }

    // --------------------------------------------------
    // Заглушки
    // --------------------------------------------------

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}