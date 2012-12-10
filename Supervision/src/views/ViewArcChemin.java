package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import model.Arc;

public class ViewArcChemin {
	Arc arc;
	ViewMain mere;
	int epaisseur;
	boolean late;
	static int defaultEpaisseur = 3;
	Color color;
	
	public ViewArcChemin(Arc a, ViewMain m, Color c, boolean retard) {
		arc = a;
		mere = m;
		late = retard;
		if (late)
			color = c.darker();
		else
			color = c.brighter();
		epaisseur = defaultEpaisseur;
	}
	
	public Arc getArc() {
		return arc;
	}
	
	public void paint(Graphics g) {
		int x1 = mere.xpix(arc.getOrigin().getX());
		int y1 = mere.ypix(arc.getOrigin().getY());
		int x2 = mere.xpix(arc.getDest().getX());
		int y2 = mere.ypix(arc.getDest().getY());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(epaisseur));
		float normalX = (float) ((float)(y2-y1)/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
		float normalY = (float) ((float)(x2-x1)/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
		g2d.drawLine(x1+(int)(normalX*2), y1+(int)(normalY*2), x2+(int)(normalX*2), y2+(int)(normalY*2));
	}
}
