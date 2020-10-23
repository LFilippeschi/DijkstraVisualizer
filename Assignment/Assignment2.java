package Assignment;
/*=============================================================================
 |   Assignment:  ASSIGNMENT NUMBER AND TITLE
 |
 |       Author:  Leonardo FIlippeschi
 |
 |        Class:  ID1020 HT202- ALgorithms and Data Structures 
 |
 |   Instructor:  Robert Rönngren
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  Create a JAVA-program which:
 |                1) Reads a positive number from stdin into an int variable 
 |                   namned nrElements. The value is the number of elements in
 |                   a dynamically allocated array of integers.
 |                2) Creates an array of integers with nrElements elements.
 |                3) Reads nrElements integers from stdin and stores them in the
 |                   array.
 |                4) Prints the elements of the array to stdout in reverse order
 |                   compared to how they were input.
 |      
 |
 |        Input:  Positive number which determines the size of the array 
 |                followed by the integers to store in it. 
 |
 |       Output:  Elements of the array in reverse order
 |
 *===========================================================================*/

import java.io.IOException;
import java.util.Scanner;

public class Assignment2 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        int nrElements = scanner.nextInt();
        int[] array = new int[nrElements];
        while (count != nrElements) {
            array[nrElements - 1 - count++] = scanner.nextInt();
        }
        count = 0;
        while (count != nrElements) {
            System.out.println(array[(count++)]);
        }
        scanner.close();
    }
}