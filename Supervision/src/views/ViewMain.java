package views;

import java.awt.Color;
import java.awt.Graphics;

import model.FeuilleDeRoute;
import model.ZoneGeo;

import supervision.Controleur;
import supervision.Dessin;

public class ViewMain {
	Controleur controleur;
	Dessin dessin;
	ViewZoneGeo zonegeo;
	ViewFeuilleDeRoute feuilleDeRoute;
	int border = 20;
	
	public ViewMain(Dessin d) {
		dessin = d;
		zonegeo = null;
		feuilleDeRoute = null;
	}
	
	public void repaint() {
		dessin.repaint();
	}
	
	public void setZoneGeo(ZoneGeo zg) {
		zonegeo = new ViewZoneGeo(zg, this);
	}
	
	public void setFeuilleDeRoute(FeuilleDeRoute f) {
		feuilleDeRoute = new ViewFeuilleDeRoute(f, this);
	}
	
	public int xpix(int xplan) {
		int canvasWidth = dessin.getWidth() - 2*border;
		float xpourcent = (xplan - zonegeo.getXmin()) / (float)zonegeo.getWidth();
		return (int) (border + xpourcent * canvasWidth);
	}
	
	public int ypix(int yplan) {
		int canvasHeight = dessin.getHeight() - 2*border;
		float ypourcent = (yplan - zonegeo.getYmin()) / (float)zonegeo.getHeight();
		return (int) (border + ypourcent * canvasHeight);
	}
	
	public Object findAt(int x, int y, boolean onlyArcs) {
		return zonegeo.findAt(x, y, onlyArcs);
	}
	
	public void paint(Graphics g) {
		dessin.setBackground(new Color(230, 230, 230));
		if (zonegeo != null)
			zonegeo.paint(g);
	}

	public void setControleur(Controleur ctrl) {
		controleur = ctrl;
	}
}
