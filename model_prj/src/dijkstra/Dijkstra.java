/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;
import model.Arc;
import model.Node;
import model.ZoneGeo;

import java.util.*;

/**
 *
 * @author anisbenyoub
 */


public class Dijkstra {

    protected
            Integer Infinity =1000;
    
    protected  Integer getMinVertex(HashMap<Integer,Integer> dist)
    {
        Set cles = dist.keySet();
        Iterator it = cles.iterator();
        Integer min=Infinity;
        Integer node=0;
        while (it.hasNext())
        {
            Object cle = it.next();
            Object valeur = dist.get(cle); 
            if(dist.get(cle)<min);
            {
                min=dist.get(cle);
                node=cle.hashCode();
            }
                
        }
        return node;
    }
    public   List<Node> Dijkstra(ZoneGeo theGraph, Node source )
 /*
 1  function Dijkstra(Graph, source):
 2      for each vertex v in Graph:                                // Initializations
 3          dist[v] := infinity ;                                  // Unknown distance function from 
 4                                                                 // source to v
 5          previous[v] := undefined ;                             // Previous node in optimal path
 6      end for                                                    // from source
 7      
 8      dist[source] := 0 ;                                        // Distance from source to source
 9      Q := the set of all nodes in Graph ;                       // All nodes in the graph are
10                                                                 // unoptimized - thus are in Q
11      while Q is not empty:                                      // The main loop
12          u := vertex in Q with smallest distance in dist[] ;    // Start node in first case
13          remove u from Q ;
14          if dist[u] = infinity:
15              break ;                                            // all remaining vertices are
16          end if                                                 // inaccessible from source
17          
18          for each neighbor v of u:                              // where v has not yet been 
19                                                                 // removed from Q.
20              alt := dist[u] + dist_between(u, v) ;
21              if alt < dist[v]:                                  // Relax (u,v,a)
22                  dist[v] := alt ;
23                  previous[v] := u ;
24                  decrease-key v in Q;                           // Reorder v in the Queue
25              end if
26          end for
27      end while
28  return dist;
*/      
    {
        HashMap<Integer,Integer> dist = new HashMap<Integer, Integer>(); 
        HashMap<Integer,Integer> previous = new HashMap<Integer, Integer>(); 
        
        for(Node v : theGraph.getNodes())
        {
            dist.put(v.getID(),Infinity);
            previous.put(v.getID(),-1);
        }
        dist.put(source.getID(),0);
         Comparator<PriorityNode> comparator = new NodeComparator();
        PriorityQueue<PriorityNode> Q = new PriorityQueue<PriorityNode>(10, comparator);
        for(Node v : theGraph.getNodes())
        {
            PriorityNode newNode = new PriorityNode(Infinity,v.getID());
            Q.add(newNode);
        }
        PriorityNode aNode =  new PriorityNode(0,source.getID());
        Q.add(aNode);
        while(Q.size()!=0)
        {
            Node u = theGraph.getNode(getMinVertex(dist));
            Q.remove(u.getID());
            if(dist.get(u.getID())==Infinity)
            {
            }
            else
            {
                for(Node v : u.getOutNodes())
                {
                    Integer alt=dist.get(u.getID())+u.getDistance(v.getID() );
                    if(alt<dist.get(v.getID()))
                    {
                        dist.put(v.getID(),alt);
                        previous.put(v.getID(), u.getID());
                        PriorityNode newNode=  new PriorityNode(alt,v.getID());
                        Q.add(newNode) ;
                    }
                }
            }
            
        }
        List<Node> Solution= new ArrayList<Node>();
        return Solution;
    }
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
