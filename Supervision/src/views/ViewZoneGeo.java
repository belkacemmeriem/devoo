package views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import model.Arc;
import model.Node;
import model.ZoneGeo;

/**
 * Vue de la zone
 * @param zg la zone
 * @param m la vue principale
 */
public class ViewZoneGeo {
	protected ZoneGeo zoneGeo;
	protected ViewMain viewMain;
	protected ArrayList<ViewNode> nodes = new ArrayList<ViewNode>();
	protected ArrayList<ViewArc> arcs = new ArrayList<ViewArc>();
	public final static double defaultDistNode = 7.0;
	public final static double defaultDistArc = 5.0;
	
	public ViewZoneGeo(ZoneGeo zg, ViewMain m) {
		zoneGeo = zg;
		viewMain = m;
		for (Node n : zoneGeo.getNodes().values()) {
			ViewNode vn = new ViewNode(n, viewMain, false);
			if (n.getID() == zoneGeo.getWarehouseID()) {
				vn.setMyDefaultColor(new Color(0, 0, 255));
				vn.setMyDefaultRadius(8);
				vn.setDefault();
			}
			nodes.add(vn);
		}
		for (Arc a : zoneGeo.getArcs()) {
			ViewArc va = new ViewArc(a, viewMain);
			arcs.add(va);
		}
	}
	
	public int getXmin() {
		return zoneGeo.getXmin();
	}
	
	public int getXmax() {
		return zoneGeo.getXmax();
	}
	
	public int getYmin() {
		return zoneGeo.getYmin();
	}
	
	public int getYmax() {
		return zoneGeo.getYmax();
	}
	
	public int getWidth() {
		return zoneGeo.getWidth();
	}
	
	public int getHeight() {
		return zoneGeo.getHeight();
	}
	
	/**
	 * Peint this sur le canvas.
	 * @param g objet graphics
	 */
	public void paint(Graphics g) {	
		for (ViewArc va : arcs) {
			va.paint(g);
		}
		for (ViewNode vn : nodes) {
			vn.paint(g);
		}

	}

	/**
	 * Renvoie l'objet situé au point x,y
	 * @param x abscisse
	 * @param y ordonnée
	 * @param onlyArcs pour chercher seulement parmi les arcs
	 * @return l'objet situé au point x,y
	 */
	public Object findAt(int x, int y, boolean onlyArcs) {
		double distMin;
		
		// en premier, test selection node
		if (onlyArcs == false) { 
			distMin = defaultDistNode;
			ViewNode clicked = null;
			for (ViewNode vn : nodes) {
				double dist = vn.distance(x, y);
				if (dist < distMin) {
					distMin = dist;
					clicked = vn;
				}
			}
			if (clicked != null)
				return (Object) clicked;
		}
		
		// puis les arcs (le clic droit ne selectionne qu'eux)
		distMin = defaultDistArc;
		ViewArc clicked = null;
		for (ViewArc va : arcs) {
			double dist = va.distance(x, y);
			if (dist < distMin) {
				distMin = dist;
				clicked = va;
			}
		}
		if (clicked != null)
			return (Object) clicked;
		
		return null;
	}

	/**
	 * Renvoie la vue sur le node n
	 * @param n un node
	 * @return la vue sur le node n
	 */
	public ViewNode getNode(Node n) {
		for (ViewNode vn : nodes) {
			if (vn.getNode().getID() == n.getID())
				return vn;
		}
		return null;
	}
	
}
