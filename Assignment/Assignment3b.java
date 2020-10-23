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
 |  Description:  DESCRIBE THE PROBLEM THAT THIS PROGRAM WAS WRITTEN TO
 |      SOLVE.
 |
 |        Input:  DESCRIBE THE INPUT THAT THE PROGRAM REQUIRES.
 |
 |       Output:  DESCRIBE THE OUTPUT THAT THE PROGRAM PRODUCES.
 |
 |    Algorithm:  OUTLINE THE APPROACH USED BY THE PROGRAM TO SOLVE THE
 |      PROBLEM.
 |
 |
 *===========================================================================*/
package Assignment;
public class Assignment3b {

    public static class DoubleLinkedList<T> {

        private Node head;

        private static class Node {

            // when item is declared static, a new object item will not be created but
            // instead we will point to the same object and just override the value inside
            // it
            private int item;
            private Node next;
            private Node prev;

            public Node() {
                this.item = 0;
                this.next = this;
                this.prev = this;
            }

            public Node(int x) {
                this.item = x;
                this.next = this;
                this.prev = this;
            }

            public String toString() {
                String next = "";
                String prev = "";
                if (this.next == null && this.prev == null) {
                    next = "null";
                    prev = "null";
                }

                String rtn = "\nitem: " + this.item + "\nnext: " + next + "\nprev: " + prev;
                return rtn;
            }
        }

        public DoubleLinkedList() {
            head = new Node(-1);
        }

        public Node remove(int value) throws Exception {
            if (value == -1)
                throw new Exception("Inavlid element!");
            Node rtn = new Node();
            rtn = head.next;
            while (rtn.item != value) {
                if (rtn.item == -1)
                    throw new Exception("No such element!");
                rtn = rtn.next;
            }
            rtn.prev.next = rtn.next;
            rtn.next.prev = rtn.prev;
            rtn.next = rtn;
            rtn.prev = rtn;
            return rtn;
        }

        public String toString() {
            String rtn = "";
            Node n = new Node();
            n = head.next;
            while (n.item != -1) {
                rtn = rtn.concat("" + n.item + " ");
                n = n.next;
            }
            return rtn;
        }

        public void insertAfter(int x, int value) throws Exception {
            Node p = new Node();
            Node n = new Node(value);
            p = head.next;
            if (x == -1) {
                n.next = head.next;
                head.next = n;
                n.next.prev = n;
                n.prev = head;
            } else {
                while (p.item != -1) {
                    if (p.item == x)
                        break;
                    p = p.next;
                }
                if (p.item != x) {
                    throw new Exception("No element found!");
                }
                n.next = p.next;
                p.next = n;
                n.prev = p;
                n.next.prev = n;
            }
        }

        public void insertFirst(int value) throws Exception {
            insertAfter(-1, value);
        }

        public void insertLast(int value) throws Exception {
            insertAfter(head.prev.item, value);
        }

        public void insertSortedElement(int value) throws Exception {
            Node n = new Node();
            n = head.next;
            if (value < n.item)
                insertFirst(value);
            else {
                while (value > n.item) {
                    if (n.item == -1)
                        break;
                    n = n.next;
                }
                insertAfter(n.prev.item, value);
            }

        }

        public void insertSortedElements(int[] arr) throws Exception {
            for (int i = 0; i < arr.length; i++) {
                this.insertSortedElement(arr[i]);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        DoubleLinkedList list = new DoubleLinkedList();

        list.insertFirst(33);
        list.insertAfter(-1, 10);
        list.insertLast(15);
        list.insertAfter(15, 100);
        System.out.println(list);
        list.insertSortedElement(20);
        list.insertSortedElement(16);
        list.insertSortedElement(1);
        list.insertSortedElement(101);
        list.insertSortedElement(200);
        // list.remove(-1);
        System.out.println(list);
        list.remove(15);
        System.out.println(list);
        list.remove(1);
        System.out.println(list);
        list.remove(200);
        System.out.println(list);
        list.remove(33);
        System.out.println(list);
        DoubleLinkedList.Node n = list.remove(16);
        System.out.println(n);
        System.out.println(list.head);
        System.out.println(list);

    }

}