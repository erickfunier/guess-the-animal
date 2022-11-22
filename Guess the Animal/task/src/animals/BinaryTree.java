package animals;

public class BinaryTree {

    Node root;

    private Node addRecursive(Node current, Object value, String relation) {
        if (current == null) {
            return new Node(value);
        }

        if (relation.equals("NO")) {
            //current.left = addRecursive(current.left, value, relation);
            current.left = new Node(value);
        } else if (relation.equals("YES")) {
            //current.right = addRecursive(current.right, value, relation);
            current.right = new Node(value);
        }
        return current;
    }

    public void add(Object value, String relation) {
        root = addRecursive(root, value, relation);
    }

    private boolean containsNodeRecursive(Node current, int value) {
        if (current == null) {
            return false;
        }
        /*if (value == current.value) {
            return true;
        }
        return value < current.value
                ? containsNodeRecursive(current.left, value)
                : containsNodeRecursive(current.right, value);*/
        return false;
    }

    public boolean containsNode(int value) {
        return containsNodeRecursive(root, value);
    }

    public Node addPreOrder(Node root, Node node, Object value, String relation) {
        Node toRetun = null;
        if (root != null) {
            if (root.value == node.value) {
                if (relation.equals("")) {
                    root.value = value;
                } else if (relation.equals("NO")) {
                    root.left = new Node(value);
                } else {
                    root.right = new Node(value);
                }
                return root;

            } else {
                toRetun = addPreOrder(root.left, node, value, relation);
                if (toRetun != null) {
                    return toRetun;
                }
                toRetun = addPreOrder(root.right, node, value, relation);
            }
        }
        return toRetun;
    }

    public void traversePreOrder(Node node) {
        if (node != null) {
            if (((String[])node.value)[1].toLowerCase().startsWith("it")) {
                System.out.println("- The " + ((Animal)node.right.value).getNameWithoutPrefix() + " " + Main.getPrefix(((Animal) node.right.value).getFacts().get(0)) + ".");
                System.out.println("- The " + ((Animal)node.left.value).getNameWithoutPrefix() + " " + Main.getPrefix(((Animal) node.left.value).getFacts().get(0)) + ".");
            }
        }
    }

    private Node deleteRecursive(Node current, String value) {
        if (current == null) {
            return null;
        }

        if (value == current.value) {
            // Node to delete found
            // ... code to delete the node will go here
        }
        /*if (value < current.value) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }*/
        current.right = deleteRecursive(current.right, value);
        return current;
    }

    public void delete(String value) {
        root = deleteRecursive(root, value);
    }

    public void getQuestion(Node root) {
        if (root != null) {
            System.out.println(root.value);
        }
    }
}
