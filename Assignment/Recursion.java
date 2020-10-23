package Assignment;
public class Recursion {
    //1,3,6,10.15 nth number?

    public static int sum(int x){
        while (x > 0)
            return sum(x-1) + x;
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(sum(10));
    }
}