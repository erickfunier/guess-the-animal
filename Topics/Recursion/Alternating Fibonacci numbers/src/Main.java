import java.util.Scanner;

public class Main {

    public static long fibRec(long n) {
        if (n <= 1) {
            return n;
        }
        return fibRec(n - 1) + fibRec(n - 2);
    }

    public static long fib(long n) {
        long result = fibRec(n);

        if (n % 2 == 0) {
            return -result;
        } else {
            return result;
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(fib(n));
    }
}