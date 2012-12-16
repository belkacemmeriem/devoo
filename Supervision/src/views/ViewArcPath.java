package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import model.Arc;
import sun.awt.Mutex;

public class ViewArcPath {
	Arc arc;
	ViewMain viewMain;
	int thick;
	boolean late;
	static int defaultThick = 2;
	Color color;
	protected Mutex mtx_pulse = new Mutex();
	
	public ViewArcPath(Arc a, ViewMain m, Color c, boolean retard) {
		arc = a;
		viewMain = m;
		late = retard;
		if (late)
			color = c.darker();
		else
			color = c.brighter();
		thick = defaultThick;
	}
	
	public Arc getArc() {
		return arc;
	}
	
	public void setThick(int width)
	{
		mtx_pulse.lock();
		thick = width; 
		mtx_pulse.unlock();
	}
	
	public void paint(Graphics g) {
		int x1 = viewMain.xpix(arc.getOrigin().getX());
		int y1 = viewMain.ypix(arc.getOrigin().getY());
		int x2 = viewMain.xpix(arc.getDest().getX());
		int y2 = viewMain.ypix(arc.getDest().getY());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		
		mtx_pulse.lock();
		g2d.setStroke(new BasicStroke(thick));
		mtx_pulse.unlock();
		
		float normalX = (float) ((float)(y2-y1)/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
		float normalY = (float) ((float)(x2-x1)/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
		g2d.drawLine(x1+(int)(normalX*2), y1+(int)(normalY*2), x2+(int)(normalX*2), y2+(int)(normalY*2));
	}
}
