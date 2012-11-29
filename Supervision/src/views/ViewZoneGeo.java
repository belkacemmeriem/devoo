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
	
	public ViewZoneGeo(ZoneGeo zg, ViewMain m) {
		zonegeo = zg;
		mere = m;
		for (Node n : zonegeo.getNodes().values()) {
			ViewNode vn = new ViewNode(n, mere);
			nodes.add(vn);
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
		for (ViewNode vn : nodes) {
			vn.paint(g);
		}
//			for (Arc a : n.getInArcs()) {
//				g.drawLine(
//					mere.xpix(a.getOrigin().getX()), 
//					mere.ypix(a.getOrigin().getY()), 
//					mere.xpix(a.getDest().getX()), 
//					mere.ypix(a.getDest().getY())
//				);
//			}
	}

	public Object findAt(int x, int y) {
		for (ViewNode vn : nodes) {
			if (vn.isClicked(x, y))
				return (Object) vn;
		}
		return null;
	}
	
}
