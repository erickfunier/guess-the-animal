import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>();

        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        while (n > 0) {
            deque.push(String.valueOf(scanner.nextInt()));
            n--;
        }

        while (!deque.isEmpty()) {
            System.out.println(deque.pop());
        }
    }
}