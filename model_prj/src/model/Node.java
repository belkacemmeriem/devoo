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
        protected
	Integer		x, y;
	Integer		id;

	protected ArrayList<Arc> inArcs;
	protected ArrayList<Arc> outArcs;


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
        public Integer getDistance(Integer anID)
        {
		ArrayList<Node> list = new ArrayList<Node>();

		for (Arc a: inArcs)
		{
			if(a.getDest().getID()==anID)
                        {
                            return a.lenght;
                        }
		}
                throw new IllegalStateException("The required id is unexisting");
        }

}