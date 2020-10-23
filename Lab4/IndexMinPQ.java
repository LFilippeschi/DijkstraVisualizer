//code taken from https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/IndexMinPQ.java.html

/******************************************************************************
 * Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne, Addison-Wesley
 * Professional, 2011, ISBN 0-321-57351-X. http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * algs4.jar is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * algs4.jar. If not, see http://www.gnu.org/licenses.
 ******************************************************************************/

package Lab4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
    private int maxN;
    private int n;
    private int[] pq;
    private int[] qp;
    private Key[] keys;

    public IndexMinPQ(int maxN) {
        if (maxN < 0)
            throw new IllegalArgumentException();
        this.maxN = maxN;
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; i++)
            qp[i] = -1; // initializes the support array to -1 (ie element not yet visited)
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(int i) { // checks support array
        return qp[i] != -1;
    }

    public int size() {
        return n;
    }

    public void insert(int i, Key key) { // if key already present throws exception
        if (contains(i))
            throw new IllegalArgumentException();
        n++;
        qp[i] = n; // qp[100] = 1, qp[10] = 2, (saves the position in the priority queue)
        pq[n] = i; // pq[1] = 100, pq[2] = 10, (priority queue increasing index tells in which
                   // position in the support array the value is stored and also the key value)
        keys[i] = key; // keys[100] = key, keys[10] = key
        swim(n); // order the heap 
    }

    public int minIndex() { // returns minimum index value
        if (n == 0)
            throw new NoSuchElementException();
        return pq[1];
    }

    public Key minKey() { // return key of minimum index
        if (n == 0)
            throw new NoSuchElementException();
        return keys[pq[1]];
    }

    public int delMin() { // removes min element from the priority queue
        if (n == 0)
            throw new NoSuchElementException();
        int min = pq[1];
        exch(1, n--); // puts first position in last position in the array and reduces "array size"
                      // making the first element not reachable
        sink(1); // now the heap is not in order and we have to sink the first position to its
                 // correct position
        qp[min] = -1; //reset last position in the support array 
        keys[min] = null; //removes the key 
        pq[n + 1] = -1; //removes the last element 
        return min;
    }

    public Key keyOf(int i) {
        if (!contains(i))
            throw new NoSuchElementException();
        else
            return keys[i];
    }

    public void changeKey(int i, Key key) {
        if (!contains(i))
            throw new NoSuchElementException();
        keys[i] = key; //just replace key 
        swim(qp[i]); // updates heap, both are needed as we don't know where it should go 
        sink(qp[i]);
    }

    public void change(int i, Key key) {
        changeKey(i, key);
    }

    public void delete(int i) {
        if (!contains(i))
            throw new NoSuchElementException();
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private void swim(int k) { // goes up the heap, parent is at position k/2 4,5->2
        while (k > 1 && greater(k / 2, k)) { // while child is greater than parent swim up
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) { // start at k and make the value sing to the bottom of the heap. child is at
                               // position k*2 and k*2 +1
        while (2 * k <= n) { //until we haven't reached the bottom 
            int j = 2 * k; // j = child, k = parent 
            if (j < n && greater(j, j + 1)) // select the greater child and updates j 
                j++;
            if (!greater(k, j)) // if parent is not greater than child break same as if parent < child break 
                break;
            exch(k, j); // swap child and parent 
            k = j; // update parent, move down
        }
    }

    public Iterator<Integer> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Integer> {
        private IndexMinPQ<Key> copy;

        public HeapIterator() {
            copy = new IndexMinPQ<Key>(pq.length - 1);
            for (int i = 1; i <= n; i++)
                copy.insert(pq[i], keys[pq[i]]);
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public Integer next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return copy.delMin();
        }
    }
}
