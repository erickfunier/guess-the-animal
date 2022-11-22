package animals;

import java.util.ArrayList;
import java.util.List;

public class Animal {

    private String[] name;
    private List<String[]> facts;

    public Animal(String[] name) {
        this.name = name;
        this.facts = new ArrayList<>();
    }

    public String getNameWithoutPrefix() {
        return name[1];
    }

    public String getNameWithPrefix() {
        return name[0];
    }

    public List<String[]> getFacts() {
        return facts;
    }

    public void addFact(String[] fact) {
        this.facts.add(0, fact);
    }
}
