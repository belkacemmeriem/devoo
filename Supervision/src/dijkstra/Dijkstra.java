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
            static float Infinity =1000;
    
    public  static ArrayList<Chemin> solve(ZoneGeo theGraph, Node source, ArrayList<Node> toEnglobe)

    {
    	ArrayList<Node> toCompute= (ArrayList<Node>) toEnglobe.clone();
        HashMap<Integer,Float> dist = new HashMap<Integer, Float>(); 
        HashMap<Integer,Integer> previous = new HashMap<Integer, Integer>(); 
        
        for(Node v : theGraph.getNodes().values())
        {
            dist.put(v.getID(),Infinity);
            previous.put(v.getID(),-1);
        }
        dist.put(source.getID(),new Float(0));
         Comparator<PriorityNode> comparator = new NodeComparator();
         PriorityQueue<PriorityNode> Q = new PriorityQueue<PriorityNode>((int)Infinity, comparator);
         System.out.println("Les points");
         
        for(Node v : theGraph.getNodes().values())
        {
            PriorityNode newNode = new PriorityNode(Infinity,v.getID());
            System.out.println(v.getID());
            //Q.add(newNode);
        }
        PriorityNode aNode =  new PriorityNode(0,source.getID());
        Q.add(aNode);
        while((Q.size()!=0)/*&& (toEnglobe.size()!=0)*/)
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
                    float alt=dist.get(u.getID())+u.getDuration(v.getID() );
                    if(alt<dist.get(v.getID()))
                    {
                        dist.put(v.getID(),alt);
                        previous.put(v.getID(), u.getID());
                        PriorityNode newNode=  new PriorityNode(alt,v.getID());
                        Q.add(newNode) ;
                    }
                }
                
                //for(Node k : toCompute)
	            //{
	            //if(k.getID()==u.getID())
	            //	{
	            //		toEnglobe.remove(k);
	            //	}
	            //}
	            
            }
        }
        ArrayList<Chemin> solution= new ArrayList<Chemin>();
        for(Integer v : previous.keySet())
        {
        	System.out.println("point "+v+ "previous "+  previous.get(v));
        }
        for(Node n : toCompute)
        {
        	float distance=0;
        	ArrayList<Node> contenu = new ArrayList<Node>();
        	Node temp=theGraph.getNode(n.getID());
    		System.out.println("point1 "+n.getID());
        	contenu.add(temp);
    		System.out.println("point2 "+temp.getID());
        	while(temp.getID()!=source.getID())
        	{
        		Integer theID =previous.get(temp.getID());
            	System.out.println("point3 "+theID);
        		Node toADD=theGraph.getNode(theID);
        		contenu.add(toADD);
            	System.out.println(toADD.getDuration(temp.getID()));
            	distance+=toADD.getDuration(temp.getID());
            	System.out.println(distance);
            	temp=theGraph.getNode(theID);
        	}
        	Collections.reverse(contenu);
        	Chemin newChemin = new Chemin(contenu,distance);        
        	solution.add(newChemin);
        }
        	
        return solution;
    }
}
