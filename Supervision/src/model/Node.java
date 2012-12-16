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
	protected Integer			id;

	protected ArrayList<Arc> inArcs	= new ArrayList<Arc>();
	protected ArrayList<Arc> outArcs	= new ArrayList<Arc>();

	public Node(Integer anX, Integer aY, Integer anID)
	{
		x=anX;
		y=aY;
		id=anID;
	}
        
	public Integer getX() {
		return x;
	}


	public Integer getY() {
		return y;
	}

	public Integer getID()
	{
		return id;
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

	public double getDuration(Integer anID)
	{

		for (Arc a : outArcs)
		{
			if (a.getDest().getID() == anID)
			{
				return a.getDuration();
			}
		}
		return 0;
	}

}
