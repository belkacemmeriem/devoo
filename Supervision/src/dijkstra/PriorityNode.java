/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;

/**
 *
 * @author anisbenyoub
 */
public class PriorityNode {
    protected
            Integer id;
            double distance;
            public PriorityNode(double aDistance,Integer anID)
            {
                id=anID;
                distance= aDistance;
            }
    
}
