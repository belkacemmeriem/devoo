package views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import supervision.State;

import model.Arc;
import model.Delivery;
import model.Node;

/**
 * Vue d'une delivery de la roadmap
 * @param d une delivery
 * @param m la vue principale
 */
public class ViewDelivery {
	
	Delivery delivery;
	ViewMain viewMain;
	ViewNode dest;
	ArrayList<ViewArcPath> arcs = new ArrayList<ViewArcPath>();

	public ViewDelivery(Delivery d, ViewMain m) {
		delivery = d;
		viewMain = m;
		
		Node destNode = delivery.getDest();
		dest = new ViewNode(destNode, viewMain, delivery.isRetardPrevu());
		dest.setMyDefaultColor(delivery.getSchedule().getColor());
		dest.setMyDefaultRadius(10);
		dest.setDefault();
		
		if (viewMain.getControler().getEtat() == State.MODIFICATION) {
			for(Arc a : delivery.getPathToDest().getArcs()) {
				Color c = delivery.getSchedule().getColor();
				ViewArcPath vac = new ViewArcPath(a, viewMain, c, delivery.isRetardPrevu());
				arcs.add(vac);
			}
		}
	}
	
	/**
	 * Peint this sur le canvas.
	 * @param g objet graphics
	 */
	public void paint(Graphics g) {
		dest.paint(g);
		for(ViewArcPath vac : arcs) {
			vac.paint(g);
		}
	}
	
	public List<ViewArcPath> getViewArcs()
	{
		return arcs;
	}

}
