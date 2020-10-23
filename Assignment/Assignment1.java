package Assignment;
/*=============================================================================
 |   Assignment:  ASSIGNMENT 1
 |
 |       Author:  Leonardo Filippeschi
 |       Contac:  lfil@kth.se
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
 |  Description:  Create a filter written in C that reads characters from stdin 
 |                until an end-of-file marker (EOF) is read. For every character
 |                read, the filter should check if the character is the 
 |                character ‘a’ in which case it should output an ‘X’ to stdout 
 |                otherwise it should output the character read to stdout.
 |
 |        Input:  Characters from stdin 
 |
 |       Output:  Characters passed as input with the filter applied
 |
 |    Algorithm:  
 |      
 |
 |
 *===========================================================================*/

import java.io.IOException;

public class Assignment1 {
    public static void main(String[] args) throws IOException {
        char c = (char) System.in.read();
        while (c != (char) -1) {
            if (c == 'a') {
                System.out.print('X');
                c = (char) System.in.read();
            } else {
                System.out.print(c);
                c = (char) System.in.read();
            }
        }
    }
}