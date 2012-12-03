package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.text.Segment;

import model.Arc;

public class ViewArc {
	Arc arc;
	ViewMain mere;
	int epaisseur;
	static int defaultEpaisseur = 1;
	Color color;
	static Color defaultColor = new Color(150, 150, 150); 
	
	public ViewArc(Arc a, ViewMain m) {
		arc = a;
		mere = m;
		setDefault();
	}
	
	public Arc getArc() {
		return arc;
	}
	
	public double sqr(double x) {
		return x*x;
	}
	
	public double dist2(double x1, double y1, double x2, double y2) {
		return sqr(x1 - x2) + sqr(y1 - y2);
	}
	
	public double distToSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		double l2 = dist2(x2, y2, x3, y3);
		if (l2 == 0)
			return Math.sqrt(dist2(x1, y1, x2, y2));
		double t = ((x1 - x2) * (x3 - x2) + (y1 - y2) * (y3 - y2)) / l2;
		if (t < 0)
			return Math.sqrt(dist2(x1, y1, x2, y2));
		if (t > 1)
			return Math.sqrt(dist2(x1, y1, x3, y3));
		System.out.println("OW");
		double x = x2 + t * (x3 - x2);
		double y = y2 + t * (y3 - y2);
		return Math.sqrt(dist2(x1, y1, x, y));
	}
	
	public double distance(int x, int y) {
		double x1 = (double) x;
		double y1 = (double) y;
		double x2 = (double) mere.xpix(arc.getOrigin().getX());
		double y2 = (double) mere.ypix(arc.getOrigin().getY());
		double x3 = (double) mere.xpix(arc.getDest().getX());
		double y3 = (double) mere.ypix(arc.getDest().getY());
		return distToSegment(x1, y1, x2, y2, x3, y3);
	}
	
	public boolean isClicked(int x, int y) {
		double distance = distance(x, y);
		if (distance <= 5.0)
			return true;
		return false;
	}
	
	public void setEpaisseur(int n) {
		epaisseur = n;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setDefault() {
		epaisseur = defaultEpaisseur;
		color = defaultColor;
	}
	
	public void paint(Graphics g) {
		int x1 = mere.xpix(arc.getOrigin().getX());
		int y1 = mere.ypix(arc.getOrigin().getY());
		int x2 = mere.xpix(arc.getDest().getX());
		int y2 = mere.ypix(arc.getDest().getY());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(epaisseur));
		g2d.drawLine(x1, y1, x2, y2);
		g2d.setStroke(new BasicStroke(1));
	}
}