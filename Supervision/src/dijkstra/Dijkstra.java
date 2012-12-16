/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import model.Node;
import model.Path;
import model.ZoneGeo;

/**
 *
 * @author anisbenyoub
 */


public class Dijkstra {

    protected
            static double Infinity =1000000;
    
    public  static ArrayList<Path> solve(ZoneGeo theGraph, Node source, ArrayList<Node> toEnglobe)

    {
    	ArrayList<Node> toCompute= (ArrayList<Node>) toEnglobe.clone();
        HashMap<Integer,Double> dist = new HashMap<Integer, Double>(); 
        HashMap<Integer,Integer> previous = new HashMap<Integer, Integer>(); 
        
        for(Node v : theGraph.getNodes().values())
        {
            dist.put(v.getID(),Infinity);
            previous.put(v.getID(),-1);
        }
        dist.put(source.getID(),new Double(0));
         Comparator<PriorityNode> comparator = new NodeComparator();
         PriorityQueue<PriorityNode> Q = new PriorityQueue<PriorityNode>((int)Infinity, comparator);
         
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
                    double alt=dist.get(u.getID())+u.getDuration(v.getID() );

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
        ArrayList<Path> solution= new ArrayList<Path>();
        for(Node n : toCompute)
        {
        	double distance=0;
        	ArrayList<Node> contenu = new ArrayList<Node>();
        	Node temp=theGraph.getNode(n.getID());
        	contenu.add(temp);
        	while(temp.getID()!=source.getID())
        	{
        		Integer theID =previous.get(temp.getID());
        		Node toADD=theGraph.getNode(theID);
        		contenu.add(toADD);
            	distance+=Math.round(toADD.getDuration(temp.getID()));
            	temp=theGraph.getNode(theID);
        	}
        	Collections.reverse(contenu);
        	Path newChemin = new Path(contenu,distance);        
        	solution.add(newChemin);
        }
        	
        return solution;
    }
}
