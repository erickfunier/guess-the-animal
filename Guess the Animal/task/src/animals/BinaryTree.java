package animals;


import java.util.Objects;

public class BinaryTree {

    Node root;

    BinaryTree(Node root) {
        this.root = root;
    }

    private Node addRecursive(Node current, String data, String relation) {
        if (current == null) {
            return new Node(data);
        }

        if (relation.equals("NO")) {
            //current.left = addRecursive(current.left, value, relation);
            current.setNo(new Node(data));
        } else if (relation.equals("YES")) {
            //current.right = addRecursive(current.right, value, relation);
            current.setYes(new Node(data));
        }
        return current;
    }

    public void add(String data, String relation) {
        root = addRecursive(root, data, relation);
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

    public Node addPreOrder(Node root, Node node, String value, String relation) {
        Node toRetun = null;
        if (root != null) {
            if (Objects.equals(root.getData(), node.getData())) {
                if (relation.equals("")) {
                    root.setData(value);
                } else if (relation.equals("NO")) {
                    root.setNo(new Node(value));
                } else {
                    root.setYes(new Node(value));
                }
                return root;

            } else {
                toRetun = addPreOrder(root.getNo(), node, value, relation);
                if (toRetun != null) {
                    return toRetun;
                }
                toRetun = addPreOrder(root.getYes(), node, value, relation);
            }
        }
        return toRetun;
    }

    private String removePrefix(String animal) {
        return animal.replace("a ", "").replace("an ", "");
    }

    private static String removeFactPrefix(String fact) {
        return fact.toLowerCase().replace("it ", "");
    }

    public static String getFactWithPrefix(String fact, String relation) {
        String[] pos = {"can", "has", "is"};
        String[] neg = {"can't", "doesn't have", "isn't"};

        if (relation.equals("pos")) {
            for (String po : pos) {
                if (fact.toLowerCase().contains(po)) {
                    return removeFactPrefix(fact);
                }
            }
        } else {
            for (int i = 0; i < pos.length; i++) {
                if (fact.toLowerCase().contains(pos[i])) {
                    return neg[i] + removeFactPrefix(fact.toLowerCase().replace(pos[i], ""));
                }
            }
        }
        return fact;
    }

    public void traversePreOrder(Node node) {
        if (node != null) {
            if (node.getData().toLowerCase().startsWith("it")) {
                System.out.println("- The " + removePrefix(node.getYes().getData()) + " " + getFactWithPrefix(node.getData(), "pos") + ".");
                System.out.println("- The " + removePrefix(node.getNo().getData()) + " " + getFactWithPrefix(node.getData(), "neg") + ".");
            }
        }
    }

    private Node deleteRecursive(Node current, String value) {
        if (current == null) {
            return null;
        }

        if (value == current.getData()) {
            // Node to delete found
            // ... code to delete the node will go here
        }
        /*if (value < current.value) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }*/
        current.setYes(deleteRecursive(current.getYes(), value));
        return current;
    }

    public void delete(String value) {
        root = deleteRecursive(root, value);
    }

    public void getQuestion(Node root) {
        if (root != null) {
            System.out.println(root.getData());
        }
    }
}
