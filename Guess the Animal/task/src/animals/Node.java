package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    private String data;
    private Node no;
    private Node yes;

    Node() {}

    Node(String data) {
        this.data = data;
        yes = null;
        no = null;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return no == null && yes == null;
    }

    public String getData() {
        return data;
    }

    public Node getNo() {
        return no;
    }

    public Node getYes() {
        return yes;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setNo(Node no) {
        this.no = no;
    }

    public void setYes(Node yes) {
        this.yes = yes;
    }
}
