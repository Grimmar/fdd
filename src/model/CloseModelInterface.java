package model;

import java.util.List;
import java.util.Set;

public interface CloseModelInterface {

    public void add(Line r);

    public List<Line> getNodes();

    public Element generateClosures(Element e);

    public double computeSupport(Set<String> set);
}
