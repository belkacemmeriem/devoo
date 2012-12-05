/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;
import java.util.Comparator;

/**
 *
 * @author anisbenyoub
 */

// StringLengthComparator.java

public class NodeComparator implements Comparator<PriorityNode>
{
    @Override
    public int compare(PriorityNode x, PriorityNode y)
    {
        return (int)(x.distance-y.distance);
    }


}