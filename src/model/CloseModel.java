package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CloseModel implements CloseModelInterface {

    private List<Line> nodes;

    public CloseModel() {
        nodes = new ArrayList<Line>();
    }

    @Override
    public void add(Line r) {
        nodes.add(r);
    }

    @Override
    public List<Line> getNodes() {
        return nodes;
    }

    @Override
    public Element generateClosures(Element e) {
        float support = 0;
        Element closure = new Element();
        Set<String> s = null;
        for (Line l : nodes) {
            if (l.contains(e.getItems())) {
                if (s == null) {
                    s = new TreeSet<String>(l.getItems());
                } else {
                    s.retainAll(l.getItems());
                }
                support++;
            }
        }
        if (s != null) {
            closure.addItems(s);
        }

        e.setSupport(support / nodes.size());
        e.setClosure(closure);
        return e;
    }

    @Override
    public double computeSupport(Set<String> set) {
        double support = 0;
        for (Line l : nodes) {
            if (l.contains(set)) {
                support++;
            }
        }
        return support / nodes.size();
    }
}
