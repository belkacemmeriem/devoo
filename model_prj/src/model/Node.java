/**
 * 
 */
package model;

import java.util.ArrayList;

/**
 * @author yann
 * 
 */
public class Node
{

	protected int		x, y;
	protected int		id;
	
	protected ArrayList<Arc> inArcs;
	protected ArrayList<Arc> outArcs;
	
	
	public Node(int x, int y, int id)
	{
		super();
		this.x = x;
		this.y = y;
		this.id = id;
	}


	public ArrayList<Arc> getInArcs()
	{
		return inArcs;
	}


	public ArrayList<Arc> getOutArcs()
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
	
	public ArrayList<Node> getOutNodes()
	{
		ArrayList<Node> list = new ArrayList<Node>();
		
		for (Arc a: outArcs)
		{
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
