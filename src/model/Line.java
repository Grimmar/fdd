package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Line {

    private String identifier;
    private List<String> items;

    public Line() {
        items = new ArrayList<String>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean addItem(String item) {
        return items.add(item.trim());
    }

    public List<String> getItems() {
        return items;
    }

    public boolean contains(Set<String> l) {
        boolean contains = true;
        for (String s : l) {
            if (!items.contains(s)) {
                contains = false;
            }
        }
        return contains;
    }

    @Override
    public String toString() {
        String chaine;
        chaine = identifier;
        for (String n : items) {
            chaine += " | " + n;
        }
        return chaine;
    }
}
