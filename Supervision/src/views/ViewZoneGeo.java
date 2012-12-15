package views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import model.Arc;
import model.Node;
import model.ZoneGeo;

public class ViewZoneGeo {
	protected ZoneGeo zonegeo;
	protected ViewMain mere;
	protected ArrayList<ViewNode> nodes = new ArrayList<ViewNode>();
	protected ArrayList<ViewArc> arcs = new ArrayList<ViewArc>();
	
	public ViewZoneGeo(ZoneGeo zg, ViewMain m) {
		zonegeo = zg;
		mere = m;
		for (Node n : zonegeo.getNodes().values()) {
			ViewNode vn = new ViewNode(n, mere, false);
			if (n.getID() == zonegeo.getWarehouseID()) {
				vn.setMyDefaultColor(new Color(0, 0, 255));
				vn.setMyDefaultRadius(8);
				vn.setDefault();
			}
			nodes.add(vn);
		}
		for (Arc a : zonegeo.getArcs()) {
			ViewArc va = new ViewArc(a, mere);
			arcs.add(va);
		}
	}
	
	public int getXmin() {
		return zonegeo.getXmin();
	}
	
	public int getXmax() {
		return zonegeo.getXmax();
	}
	
	public int getYmin() {
		return zonegeo.getYmin();
	}
	
	public int getYmax() {
		return zonegeo.getYmax();
	}
	
	public int getWidth() {
		return zonegeo.getWidth();
	}
	
	public int getHeight() {
		return zonegeo.getHeight();
	}
	
	public void paint(Graphics g) {	
		for (ViewArc va : arcs) {
			va.paint(g);
		}
		for (ViewNode vn : nodes) {
			vn.paint(g);
		}

	}

	public Object findAt(int x, int y, boolean onlyArcs) {
		double distMin;
		
		// en premier, test selection node
		if (onlyArcs == false) { 
			distMin = 7.0;
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
		distMin = 5.0;
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

	public ViewNode getNode(Node n) {
		for (ViewNode vn : nodes) {
			if (vn.getNode().getID() == n.getID())
				return vn;
		}
		return null;
	}
	
}
