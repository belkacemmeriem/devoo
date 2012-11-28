package model;

import java.util.ArrayList;
import java.util.List;

public class ZoneGeo
{
	List<Node> nodes = new ArrayList<Node>();
	List<Arc> arcs = new ArrayList<Arc>();
	
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
	
}
