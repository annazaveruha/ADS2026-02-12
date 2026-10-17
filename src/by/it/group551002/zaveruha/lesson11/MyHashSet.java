package by.it.group551002.zaveruha.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Один элемент односвязного списка
    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    // Массив "корзин"
    private Node<E>[] table;

    // Количество элементов в Set
    private int size;

    // Размер массива по умолчанию
    private static final int DEFAULT_CAPACITY = 16;

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
    }

    // Получаем индекс в массиве по hashCode объекта
    private int getIndex(Object obj) {
        if (obj == null) {
            return 0;
        }

        int hash = obj.hashCode();

        // Делаем hash положительным
        hash = hash & 0x7fffffff;

        return hash % table.length;
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
    public boolean contains(Object obj) {
        int index = getIndex(obj);

        Node<E> current = table[index];

        while (current != null) {
            if (obj == null) {
                if (current.value == null) {
                    return true;
                }
            } else {
                if (obj.equals(current.value)) {
                    return true;
                }
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E obj) {
        int index = getIndex(obj);

        // Проверяем, нет ли уже такого элемента
        Node<E> current = table[index];

        while (current != null) {
            if (obj == null) {
                if (current.value == null) {
                    return false;
                }
            } else {
                if (obj.equals(current.value)) {
                    return false;
                }
            }

            current = current.next;
        }

        // Добавляем новый элемент в начало списка
        Node<E> newNode = new Node<>(obj);
        newNode.next = table[index];
        table[index] = newNode;

        size++;

        return true;
    }

    @Override
    public boolean remove(Object obj) {
        int index = getIndex(obj);

        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {

            boolean equals;

            if (obj == null) {
                equals = current.value == null;
            } else {
                equals = obj.equals(current.value);
            }

            if (equals) {

                // Если удаляем первый элемент списка
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    // Пересоединяем предыдущий элемент
                    previous.next = current.next;
                }

                size--;

                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("[");

        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];

            while (current != null) {

                if (!first) {
                    result.append(", ");
                }

                result.append(current.value);

                first = false;
                current = current.next;
            }
        }

        result.append("]");

        return result.toString();
    }

    // Заглушки

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

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}