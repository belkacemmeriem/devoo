package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import model.Arc;

/**
 * Vue d'un arc de la ZoneGeo
 * @param a un arc
 * @param m la vue principale
 */
public class ViewArc {
	Arc arc;
	ViewMain viewMain;
	int thick;
	static int defaultThick = 1;
	Color color;
	static Color defaultColor = new Color(200, 200, 200); 
	static Color defaultRoadColor = new Color(255, 255, 150);
	
	public ViewArc(Arc a, ViewMain m) {
		arc = a;
		viewMain = m;
		setDefault();
	}
	
	public Arc getArc() {
		return arc;
	}
	
	protected double sqr(double x) {
		return x*x;
	}
	
	protected double dist2(double x1, double y1, double x2, double y2) {
		return sqr(x1 - x2) + sqr(y1 - y2);
	}
	
	protected double distToSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		double l2 = dist2(x2, y2, x3, y3);
		if (l2 == 0)
			return Math.sqrt(dist2(x1, y1, x2, y2));
		double t = ((x1 - x2) * (x3 - x2) + (y1 - y2) * (y3 - y2)) / l2;
		if (t < 0)
			return Math.sqrt(dist2(x1, y1, x2, y2));
		if (t > 1)
			return Math.sqrt(dist2(x1, y1, x3, y3));
		double x = x2 + t * (x3 - x2);
		double y = y2 + t * (y3 - y2);
		return Math.sqrt(dist2(x1, y1, x, y));
	}
	
	/**
	 * Renvoie la distance entre le point x,y et this.
	 * @param x abscisse du point
	 * @param y ordonnée du point
	 * @return la distance entre le point x,y et this.
	 */
	public double distance(int x, int y) {
		double x1 = (double) x;
		double y1 = (double) y;
		double x2 = (double) viewMain.xpix(arc.getOrigin().getX());
		double y2 = (double) viewMain.ypix(arc.getOrigin().getY());
		double x3 = (double) viewMain.xpix(arc.getDest().getX());
		double y3 = (double) viewMain.ypix(arc.getDest().getY());
		return distToSegment(x1, y1, x2, y2, x3, y3);
	}
	
	public void setEpaisseur(int n) {
		thick = n;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setDefault() {
		thick = defaultThick;
		color = defaultColor;
	}
	
	/**
	 * Peint this sur le canvas.
	 * @param g objet graphics
	 */
	public void paint(Graphics g) {
		int x1 = viewMain.xpix(arc.getOrigin().getX());
		int y1 = viewMain.ypix(arc.getOrigin().getY());
		int x2 = viewMain.xpix(arc.getDest().getX());
		int y2 = viewMain.ypix(arc.getDest().getY());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(defaultRoadColor);
		g2d.setStroke(new BasicStroke(thick));
		float normalX = (float) ((float)(y2-y1)/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
		float normalY = (float) ((float)(x2-x1)/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
	
		int[] x = new int[]{x1,x2,x2+(int)(normalX*4),x1+(int)(normalX*4)};
	    int[] y = new int[]{y1,y2,y2-(int)(normalY*4),y1-(int)(normalY*4)};
	    g.fillPolygon(x, y, x.length);
		g2d.setColor(defaultColor);
	    g.drawPolygon(x, y, x.length);
		g2d.setStroke(new BasicStroke(1));
	}
}
