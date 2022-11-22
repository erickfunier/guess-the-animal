package animals;

public class Node {
    Object value;
    Node left;
    Node right;

    Node(Object value) {
        this.value = value;
        right = null;
        left = null;
    }
}
