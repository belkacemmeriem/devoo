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
	protected Integer	x, y;
	Integer				id;

	ArrayList<Arc>		inArcs	= new ArrayList<Arc>();
	ArrayList<Arc>		outArcs	= new ArrayList<Arc>();

	public Integer getID()
	{
		return id;
	}

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

	public ArrayList<Node> getOutNodes()
	{
		ArrayList<Node> list = new ArrayList<Node>();

		for (Arc a : outArcs)
		{
			list.add(a.getDest());
		}
		return list;
	}

	public ArrayList<Node> getInNodes()
	{
		ArrayList<Node> list = new ArrayList<Node>();

		for (Arc a : inArcs)
		{
			list.add(a.getDest());
		}
		return list;
	}

	public Integer getDistance(Integer anID)
	{
		for (Arc a : inArcs)
		{
			if (a.getDest().getID() == anID)
			{
				return a.lenght;
			}
		}
		throw new IllegalStateException("The required id is unexisting");
	}

}
