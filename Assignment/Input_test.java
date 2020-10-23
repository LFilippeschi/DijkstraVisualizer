package Assignment;
import java.util.Scanner;

class Input_test {

    public static void hello(String text) {

        System.out.println(text);
    }

    public static void main(String[] args) {

        // System.out.println("hello how is your day?");

        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.nextLine());

        scanner.close();
    }
}