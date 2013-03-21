/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;

/**
 *
 * @author David
 */
public class Rule implements Comparable {

    private Set<String> left;
    private Set<String> right;
    private double support;
    private double confiance;
    private double lift;

    public Rule(Set<String> left, Set<String> right, double support, double confiance, double lift) {
        this.left = left;
        this.right = right;
        this.support = support;
        this.confiance = confiance;
        this.lift = lift;
    }

    @Override
    public String toString() {
        if (left.isEmpty() || right.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : left) {
            sb.append(s);
        }
        sb.append(" ---> ");
        for (String s : right) {
            sb.append(s);
        }
        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        sb.append(" support = ").append(nf.format(support));
        sb.append(" confiance = ").append(nf.format(confiance));
        sb.append(" lift = ").append(nf.format(lift));
        return sb.toString();
    }

    public Set<String> getLeft() {
        return left;
    }

    public Set<String> getRight() {
        return right;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Rule) {
            Rule r = (Rule) o;
            if (this.confiance > r.confiance) {
                return -1;
            } else if (this.confiance == r.confiance) {
                return 0;
            }
            return 0;
        }
        return 0;
    }
}
