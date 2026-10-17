package by.it.group551002.zaveruha.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class MyPriorityQueue<E> implements java.util.Queue<E> {

    private E[] elements;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    public MyPriorityQueue() {
        this.elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            E[] newElements = (E[]) new Comparable[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            this.elements = newElements;
        }
    }

    // Просеивание вверх (Sift-Up)
    private void siftUp(int index) {
        E key = elements[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E e = elements[parent];
            if (((Comparable<? super E>) key).compareTo(e) >= 0) {
                break;
            }
            elements[index] = e;
            index = parent;
        }
        elements[index] = key;
    }

    // Просеивание вниз (Sift-Down)
    private void siftDown(int index) {
        E key = elements[index];
        int half = size >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            E c = elements[child];
            int right = child + 1;

            if (right < size && ((Comparable<? super E>) elements[right]).compareTo(c) < 0) {
                child = right;
                c = elements[child];
            }

            if (((Comparable<? super E>) key).compareTo(c) <= 0) {
                break;
            }

            elements[index] = c;
            index = child;
        }
        elements[index] = key;
    }

    // Построение кучи из произвольного массива за O(n) (Heapify / Floyd's algorithm)
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    // Удаление по конкретному индексу внутри кучи
    private E removeAt(int index) {
        int s = --size;
        if (s == index) {
            elements[index] = null;
        } else {
            E moved = elements[s];
            elements[s] = null;
            elements[index] = moved;
            siftDown(index);
            if (elements[index] == moved) {
                siftUp(index);
            }
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        elements[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[0];
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = elements[0];
        E last = elements[--size];
        elements[size] = null;
        if (size != 0) {
            elements[0] = last;
            siftDown(0);
        }
        return result;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c) {
            if (offer(e)) {
                modified = true;
            }
        }
        return modified;
    }

    // Удаление всех элементов c помощью алгоритма Floyd (O(n))
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (!c.contains(elements[r])) {
                elements[w++] = elements[r];
            }
        }
        if (w != size) {
            for (int i = w; i < size; i++) {
                elements[i] = null;
            }
            size = w;
            heapify();
            return true;
        }
        return false;
    }

    // Оставление только совпавших элементов с помощью алгоритма Floyd (O(n))
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        int w = 0;
        for (int r = 0; r < size; r++) {
            if (c.contains(elements[r])) {
                elements[w++] = elements[r];
            }
        }
        if (w != size) {
            for (int i = w; i < size; i++) {
                elements[i] = null;
            }
            size = w;
            heapify();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

     //////                   Заглушки для Collection
    @Override public boolean remove(Object o) {
        if (o == null) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}