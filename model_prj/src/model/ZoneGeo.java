package model;

import java.util.ArrayList;

public class ZoneGeo
{
	ArrayList<Node> nodes;
	ArrayList<Arc> arcs;
	
	public void addNode(Node node)
	{
		if (nodes.get(node.id) != null)
			System.out.println("ZoneGeo.addNode: inserted node id " + node.id + " already exists! (ignored)");
		else
			nodes.add(node.id, node);			
	}
	
	public void addArc(int originID, int destID, int speed, int lenght, String name)
	{
		if (nodes.get(originID) == null)
			System.out.println("ZoneGeo.addArc: inserted origin id " + originID + " already exists! (ignored)");
		else if (nodes.get(destID) == null)
			System.out.println("ZoneGeo.addArc: inserted origin id " + destID + " already exists! (ignored)");
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
	
}
