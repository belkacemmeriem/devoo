package model;

import java.util.ArrayList;


public class ZoneGeo
{
	protected
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Arc> arcs = new ArrayList<Arc>();
	int warehouseID;
	

	public ArrayList<Node> getNodes()
	{
		return nodes;
	}
	public Node getNode(Integer anID)
	{
		for(Node n : nodes)
		{
			if(n.getID()==anID)
			{
				return n;
			}
		}
        throw new IllegalStateException("The required id is unexisting");
	}
	public void addNode(Node node)
	{
			nodes.add(node.id, node);
	}
	
	public void addArc(int originID, int destID, int speed, int lenght, String name)
	{
		if (nodes.get(originID) == null)
			System.out.println("ZoneGeo.addArc: origin id " + originID + " doesn't exists! (arc ignored)");
		else if (nodes.get(destID) == null)
			System.out.println("ZoneGeo.addArc: dest id " + destID + " doesn't exists! (arc ignored)");
		else
		{
			Arc arc = new Arc(	nodes.get(originID),
								nodes.get(destID),
								speed,
								lenght,
								name );
			
			arcs.add(arc);
			nodes.get(originID).addOutArc(arc);
			nodes.get(destID).addInArc(arc);
		}
	}
	
	public void setWarehouse(int id)
	{
		warehouseID = id;
	}
	
	public Node getWarehouse()
	{
		return nodes.get(warehouseID);
	}
	
	public int getWarehouseID()
	{
		return warehouseID;
	}
	
}
