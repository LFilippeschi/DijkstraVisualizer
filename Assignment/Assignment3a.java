package Assignment;
/*=============================================================================
 |   Assignment:  ASSIGNMENT 
 |
 |       Author:  Leonardo Filippeschi
 |      Contact:  lfil@kth.se
 |
 |      Created:  28.08.2020
 |  Last edited:  28.08.2020
 |
 |        Class:  ID1020 HT202- ALgorithms and Data Structures 
 |
 |   Instructor:  Robert Rönngren
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  Implementation of a generic iterable Linked list
 |
 *===========================================================================*/

import java.util.Iterator;
import java.util.Scanner;

public class Assignment3a {

    /**
     * Iterable generic Linked list
     * 
     * @param <T>
     */
    public static class SingleLinkedList<T> extends Object implements Iterable<T>, Comparable<SingleLinkedList<T>> {

        private Node<T> head;

        private static class Node<T> {

            // when item is declared static, a new object item will not be created but
            // instead we will point to the same object and just override the value inside
            // it
            private T item;
            private Node<T> next;

            public Node() {
            }

            public Node(T t) {
                this.item = t;
            }
        }

        public SingleLinkedList() {
        }

        public void insert(T x) {
            Node<T> n = new Node<T>(x);
            if (head == null) {
                head = n;
            } else {
                n.next = head;
                head = n;
            }

        }

        public void insertLast(T x) {
            Node<T> n = new Node<T>(x);
            Node<T> tmp = new Node<T>();
            tmp = head;
            if (head == null) {
                head = n;
            } else {
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = n;
            }

        }

        /**
         * Removes elements following the LIFO system
         * 
         * @return
         * @throws Exception
         */
        public T remove() throws Exception {
            if (head == null)
                throw new Exception("Empty list!");
            Node<T> rtn = new Node<T>();
            rtn.next = head;
            rtn.item = head.item;
            head = head.next;
            rtn.next.next = null;
            return rtn.item;
        }

        public T removeLast() throws Exception {
            if (head == null)
                throw new Exception("Empty list!");
            Node<T> rtn = new Node<T>();
            Node<T> tmp = new Node<T>();
            rtn = head;
            if (rtn.next == null)
                head = null;
            while (rtn.next != null) {
                tmp = rtn;
                rtn = rtn.next;
            }
            tmp.next = null;
            return rtn.item;
        }

        public boolean isEmpty() {
            Boolean bool;
            if (head == null)
                bool = true;
            else
                bool = false;
            return bool;
        }

        public String toString() {
            String rtn = "";
            Node<T> n = new Node<T>();
            n = this.head;
            while (n != null) {
                rtn = rtn.concat("[" + n.item + "]");
                if (n.next != null)
                    rtn = rtn.concat(",");
                n = n.next;
            }
            return rtn;
        }

        public void insertAfterValue(T x, T value) throws Exception {
            Node<T> p = new Node<T>();
            p = head;
            while (p != null) {
                if (p.item == x)
                    break;
                p = p.next;
            }
            if (p == null) {
                throw new Exception("No element found!");
            }
            Node<T> n = new Node<T>(value);
            n.next = p.next;
            p.next = n;
        }

        public void insertionSort(int[] arr) {
            int n = arr.length;
            for (int i = 1; i < n; ++i) {
                int key = arr[i];
                int j = i - 1;

                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
        }

        public void insertSortedElements(int[] arr) throws Exception {
            this.insertionSort(arr);
            for (int i = 0; i < arr.length; i++) {
                // this.insert(arr[i]);
            }
        }

        public boolean contains(T t) {
            for (T iterable_element : this) {
                if (iterable_element.equals(t))
                    return true;
            }
            return false;
        }

        public Iterator<T> iterator() {
            return new MyIterator<T>(this);
        }

        public static class MyIterator<T> implements Iterator<T> {
            private Node<T> current;

            public MyIterator(SingleLinkedList<T> list) {
                current = list.head;
            }

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                T item = current.item;
                current = current.next;
                return item;

            }

        }

        @Override
        public int compareTo(SingleLinkedList arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    public static void main(String[] args) throws Exception {
        SingleLinkedList<Integer> list = new SingleLinkedList<Integer>();
        SingleLinkedList<Character> list2 = new SingleLinkedList<Character>();

        // test function to create SingleLinkedList queue from data file
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            list.insert(scanner.nextInt());
        }
        System.out.println(list.isEmpty());
        list.insert(10);
        list.insert(100);
        System.out.println(list.isEmpty());
        list.insert(20);
        list.insert(-10);
        System.out.println(list.remove());
        System.out.println(list);
        list.insertAfterValue(100, 60);
        System.out.println(list);
        int[] arr = { 10, 2, 20, -10, 50 };
        list.insertSortedElements(arr);
        System.out.println(list);
        System.out.println("Print using iterator");
        for (Integer i : list) {
            System.out.println(i);
        }

        list2.insert('x');
        list2.insert('c');
        list2.insert('z');
        list2.insertAfterValue('x', 'f');
        System.out.println(list2.remove());
        System.out.println(list2);
        System.out.println("Print using iterator");
        for (Character i : list2) {
            System.out.println(i);
        }

        scanner.close();
    }

}