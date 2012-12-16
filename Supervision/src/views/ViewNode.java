package views;

import java.awt.Color;
import java.awt.Graphics;

import model.Node;

public class ViewNode {
	Node node;
	ViewMain viewMain;
	boolean square;
	int radius;
	int myDefaultRadius;
	static int defaultRadius = 5;
	Color color;
	Color myDefaultColor;

	static Color defaultColor = new Color(0, 0, 0);
	
	public ViewNode(Node n, ViewMain m, boolean sqr) {
		node = n;
		viewMain = m;
		square = sqr;
		myDefaultRadius = defaultRadius;
		myDefaultColor = defaultColor;
		setDefault();
	}
	
	public Node getNode() {
		return node;
	}
	
	public double distance(int x, int y) {
		double xpixel = (double) viewMain.xpix(node.getX());
		double ypixel = (double) viewMain.ypix(node.getY());
		double dx = Math.abs((double) x - xpixel);
		double dy = Math.abs((double) y - ypixel);
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public boolean isClicked(int x, int y) {
		if (distance(x, y) < radius) {
			return true;
		}
		return false;
	}
	
	public void setRadius(int n) {
		radius = n;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setMyDefaultRadius(int myDefaultRadius) {
		this.myDefaultRadius = myDefaultRadius;
	}

	public void setMyDefaultColor(Color myDefaultColor) {
		this.myDefaultColor = myDefaultColor;
	}
	
	public void setDefault() {
		radius = myDefaultRadius;
		color = myDefaultColor;
	}
	
	public void paint(Graphics g) {
		int xpixel = viewMain.xpix(node.getX());
		int ypixel = viewMain.ypix(node.getY());
		
		g.setColor(color); // boule noires
		if (square)
			g.fillRect(xpixel-radius/2, ypixel-radius/2, radius, radius);
		else
			g.fillOval(xpixel-radius/2, ypixel-radius/2, radius, radius);
		
	}
}
