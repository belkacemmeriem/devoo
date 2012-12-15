package views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import supervision.Etat;

import model.Arc;
import model.Delivery;
import model.Node;

public class ViewDelivery {
	
	Delivery delivery;
	ViewMain mere;
	ViewNode dest;
	ArrayList<ViewArcChemin> arcs = new ArrayList<ViewArcChemin>();

	public ViewDelivery(Delivery d, ViewMain m) {
		delivery = d;
		mere = m;
		
		Node destNode = delivery.getDest();
		dest = new ViewNode(destNode, mere, delivery.isRetardPrevu());
		dest.setMyDefaultColor(delivery.getSchedule().getColor());
		dest.setMyDefaultRadius(10);
		dest.setDefault();
		
		if (mere.getControleur().getEtat() == Etat.MODIFICATION) {
			for(Arc a : delivery.getPathToDest().getArcs()) {
				Color c = delivery.getSchedule().getColor();
				ViewArcChemin vac = new ViewArcChemin(a, mere, c, delivery.isRetardPrevu());
				arcs.add(vac);
			}
		}
	}
	
	public void paint(Graphics g) {
		dest.paint(g);
		for(ViewArcChemin vac : arcs) {
			vac.paint(g);
		}
	}
	
	public List<ViewArcChemin> getViewArcs()
	{
		return arcs;
	}

}
