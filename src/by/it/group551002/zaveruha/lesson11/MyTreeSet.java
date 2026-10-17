package by.it.group551002.zaveruha.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private E[] array;
    private int size;

    public MyTreeSet() {
        array = (E[]) new Object[10];
    }

    // --------------------------------------------------
    // Увеличение массива
    // --------------------------------------------------

    private void grow() {
        E[] newArray = (E[]) new Object[array.length * 2];

        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }

        array = newArray;
    }

    // --------------------------------------------------
    // Сравнение элементов
    // --------------------------------------------------

    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    // --------------------------------------------------
    // Поиск позиции элемента
    // --------------------------------------------------

    private int findPosition(E obj) {

        int left = 0;
        int right = size - 1;

        while (left <= right) {

            int middle = (left + right) / 2;

            int result = compare(array[middle], obj);

            if (result == 0) {
                return middle;
            }

            if (result < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return left;
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

        if (size == 0) {
            return false;
        }

        E element = (E) obj;

        int position = findPosition(element);

        return position < size
                && compare(array[position], element) == 0;
    }

    // --------------------------------------------------
    // add
    // --------------------------------------------------

    @Override
    public boolean add(E obj) {

        // Находим место для элемента
        int position = findPosition(obj);

        // Если такой элемент уже есть
        if (position < size
                && compare(array[position], obj) == 0) {
            return false;
        }

        // Если массив заполнен
        if (size == array.length) {
            grow();
        }

        // Сдвигаем элементы вправо
        for (int i = size; i > position; i--) {
            array[i] = array[i - 1];
        }

        // Вставляем новый элемент
        array[position] = obj;

        size++;

        return true;
    }

    // --------------------------------------------------
    // remove
    // --------------------------------------------------

    @Override
    public boolean remove(Object obj) {

        if (size == 0) {
            return false;
        }

        E element = (E) obj;

        int position = findPosition(element);

        // Элемента нет
        if (position >= size
                || compare(array[position], element) != 0) {
            return false;
        }

        // Сдвигаем элементы влево
        for (int i = position; i < size - 1; i++) {
            array[i] = array[i + 1];
        }

        array[size - 1] = null;

        size--;

        return true;
    }

    // --------------------------------------------------
    // clear
    // --------------------------------------------------

    @Override
    public void clear() {

        for (int i = 0; i < size; i++) {
            array[i] = null;
        }

        size = 0;
    }

    // --------------------------------------------------
    // toString
    // --------------------------------------------------

    @Override
    public String toString() {

        String result = "[";

        for (int i = 0; i < size; i++) {

            if (i > 0) {
                result += ", ";
            }

            result += array[i];
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

        for (int i = size - 1; i >= 0; i--) {

            if (!c.contains(array[i])) {

                remove(array[i]);
                changed = true;
            }
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
