package views;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import model.Arc;
import model.Delivery;
import model.Node;

public class ViewDelivery {
	
	Delivery delivery;
	ViewMain mere;
	ViewNode dest;
	ArrayList<ViewArc> arcs = new ArrayList<ViewArc>();

	public ViewDelivery(Delivery d, ViewMain m) {
		delivery = d;
		mere = m;
		
		Node destNode = delivery.getDest();
		dest = new ViewNode(destNode, mere);
		dest.setMyDefaultColor(delivery.getSchedule().getColor());
		dest.setMyDefaultRadius(8);
		dest.setDefault();
		
		
//		for (Arc a : delivery.getPathToDest().getArcs()) {
//			ViewArc va = new ViewArc(a, mere);
//			va.setColor(delivery.getSchedule().getColor());
//			va.setEpaisseur(3);
//			arcs.add(va);
//		}
	}
	
	public void paint(Graphics g, boolean onlyDest) {
		dest.paint(g);
		if (! onlyDest) {
			// affichage des arcs de delivery.getPathToDest() 
			for(Arc a : delivery.getPathToDest().getArcs())
			{
				ViewArcChemin vac = new ViewArcChemin(a,mere,delivery.isRetardPrevu());
				vac.paint(g);
			}
		}
	}
	
	public List<ViewArc> getViewArcs()
	{
		return arcs;
	}

}
