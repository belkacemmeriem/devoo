/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yann
 * 
 */
public class Node
{

	protected int		x, y;
	protected int		id;
	
	protected List<Arc> inArcs = new ArrayList<Arc>();
	protected List<Arc> outArcs = new ArrayList<Arc>();
	
	
	public Node(int x, int y, int id)
	{
		super();
		this.x = x;
		this.y = y;
		this.id = id;
	}


	public List<Arc> getInArcs()
	{
		return inArcs;
	}


	public List<Arc> getOutArcs()
	{
		return outArcs;
	}
	
	public void addInArc(Arc arc)
	{
		inArcs.add(arc);
	}
	
	public void addOutArc(Arc arc)
	{
		outArcs.add(arc);
	}
	
	public List<Node> getOutNodes()
	{
		ArrayList<Node> list = new ArrayList<Node>();
		
		for (Arc a: outArcs)
		{
			if ( !list.contains(a.getDest()) )	//evacuer deux arcs qui meneraient au mm noeud.
				list.add(a.getDest());
		}
		return list;
	}
	
	public ArrayList<Node> getInNodes()
	{
		ArrayList<Node> list = new ArrayList<Node>();
		
		for (Arc a: inArcs)
		{
			list.add(a.getDest());
		}
		return list;
	}

}
