/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Set;

/**
 *
 * @author David
 */
public class Regle {

    private Set<String> left;
    private Set<String> right;
    private double support;
    private double confiance;
    private double lift;

    public Regle(Set<String> left, Set<String> right, double support, double confiance, double lift) {
        this.left = left;
        this.right = right;
        this.support = support;
        this.confiance = confiance;
        this.lift = lift;
    }

    @Override
    public String toString() {
        if(left.isEmpty() || right.isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(String s : left){
            sb.append(s);
        }
        sb.append(" ---> ");
        for(String s : right){
            sb.append(s);
        }
       
        // l = closure.size();
		/*
         * for (Element s : closure) { sb.append(s.getItems()); i++; if (i < l)
         * { sb.append(separator); } }
         */
        sb.append(" support = ").append((float) support);
        return sb.toString();
    }
}