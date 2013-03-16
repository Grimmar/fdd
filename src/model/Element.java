package model;

import java.util.Set;
import java.util.TreeSet;

public class Element {

    private Set<String> items;
    private Element closure;
    private double support;

    public Element() {
        this.items = new TreeSet<String>();
        this.closure = null;
        this.support = 0;
    }

    public Set<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    public void addItems(Set<String> items) {
        this.items.addAll(items);
    }

    public void intersect(Set<String> s) {
        this.items.retainAll(s);
    }

    public Element getClosure() {
        return closure;
    }

    public void setClosure(Element closure) {
        this.closure = closure;

    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Element other = (Element) obj;
        if (items == null) {
            if (other.items != null) {
                return false;
            }
        } else if (!items.equals(other.items)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String separator = ", ";
        int i = 0, l = items.size();
        for (String s : items) {
            sb.append(s);
            i++;
            if (i < l) {
                sb.append(separator);
            }
        }
        sb.append(" => ");

        i = 0;
        // l = closure.size();
		/*
         * for (Element s : closure) { sb.append(s.getItems()); i++; if (i < l)
         * { sb.append(separator); } }
         */
        if (closure != null) {
            sb.append(closure.getItems());
        }
        sb.append(" support = ").append((float) support);
        return sb.toString();
    }
}
