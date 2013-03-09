package model;

import java.util.List;

public interface CloseModelInterface {
	public void add(Line r);

	public List<Line> getNodes();

	public Element generateClosures(Element e);

}
