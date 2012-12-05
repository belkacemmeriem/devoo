/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;
import model.Arc;
import model.Node;
import model.ZoneGeo;
import model.Chemin;

import java.util.*;

/**
 *
 * @author anisbenyoub
 */


public class Dijkstra {

    protected
            static Integer Infinity =1000;
    
    public  static ArrayList<Chemin> solve(ZoneGeo theGraph, Node source, ArrayList<Node> toEnglobe)

    {
    	ArrayList<Node> toCompute= (ArrayList<Node>) toEnglobe.clone();
        HashMap<Integer,Integer> dist = new HashMap<Integer, Integer>(); 
        HashMap<Integer,Integer> previous = new HashMap<Integer, Integer>(); 
        
        for(Node v : theGraph.getNodes().values())
        {
            dist.put(v.getID(),Infinity);
            previous.put(v.getID(),-1);
        }
        dist.put(source.getID(),0);
         Comparator<PriorityNode> comparator = new NodeComparator();
        PriorityQueue<PriorityNode> Q = new PriorityQueue<PriorityNode>(Infinity, comparator);
        for(Node v : theGraph.getNodes().values())
        {
            PriorityNode newNode = new PriorityNode(Infinity,v.getID());
            Q.add(newNode);
        }
        PriorityNode aNode =  new PriorityNode(0,source.getID());
        Q.add(aNode);
        while((Q.size()!=0)&& (toEnglobe.size()!=0))
        {
        	PriorityNode uPN=Q.poll();
            Node u = theGraph.getNode(uPN.id);
            
            if(dist.get(u.getID())==Infinity)
            {
            }
            else
            {
                for(Node v : u.getOutNodes())
                {
                    Integer alt=dist.get(u.getID())+u.getDuration(v.getID() );
                    if(alt<dist.get(v.getID()))
                    {
                        dist.put(v.getID(),alt);
                        previous.put(v.getID(), u.getID());
                        PriorityNode newNode=  new PriorityNode(alt,v.getID());
                        Q.add(newNode) ;
                    }
                }
                
                for(Node k : toCompute)
	            {
	            	if(k.getID()==u.getID())
	            	{
	            		toEnglobe.remove(k);
	            	}
	            }
	            
            }
        }
        ArrayList<Chemin> solution= new ArrayList<Chemin>();
        for(Node n : toCompute)
        {
        	Integer distance=0;
        	ArrayList<Node> contenu = new ArrayList<Node>();
        	Node temp=theGraph.getNode(n.getID());
        	contenu.add(temp);
        	while(temp.getID()!=source.getID())
        	{
        		Integer theID =previous.get(temp.getID());
        		Node toADD=theGraph.getNode(theID);
        		contenu.add(toADD);
            	temp=theGraph.getNode(theID);
            	distance+=toADD.getDuration(temp.getID());
        	}
        	Collections.reverse(contenu);
        	Chemin newChemin = new Chemin(contenu,distance);        
        	solution.add(newChemin);
        }
        	
        return solution;
    }
}
