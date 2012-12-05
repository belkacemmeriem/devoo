package views;

import java.awt.Graphics;

import model.Delivery;
import model.Node;

public class ViewDelivery {
	
	Delivery delivery;
	ViewMain mere;
	ViewNode dest;

	public ViewDelivery(Delivery d, ViewMain m) {
		delivery = d;
		mere = m;
		Node destNode = delivery.getDest();
		dest = new ViewNode(destNode, mere);
		dest.setMyDefaultColor(delivery.getSchedule().getColor());
		dest.setMyDefaultRadius(8);
		dest.setDefault();
	}
	
	public void paint(Graphics g, boolean onlyDest) {
		dest.paint(g);
		if (! onlyDest) {
			// affichage des arcs de delivery.getPathToDest() 
		}
	}
	
	public List<ViewArc> getViewArcs()
	{
		return arcs;
	}

}
