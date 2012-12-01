package views;

import java.awt.Color;
import java.awt.Graphics;

import model.Node;

public class ViewNode {
	Node node;
	ViewMain mere;
	int radius = 8;
	Color color = new Color(0, 0, 0);
	
	public ViewNode(Node n, ViewMain m) {
		node = n;
		mere = m;
	}
	
	public Node getNode() {
		return node;
	}
	
	public boolean isClicked(int x, int y) {
		int xpixel = mere.xpix(node.getX());
		int ypixel = mere.ypix(node.getY());
		
		if (Math.abs(x-xpixel) <= 8
			&& Math.abs(y-ypixel) <= 8) {
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
	
	public void paint(Graphics g) {
		int xpixel = mere.xpix(node.getX());
		int ypixel = mere.ypix(node.getY());
		
		g.setColor(color); // boule noires
		g.fillOval(xpixel-radius/2, ypixel-radius/2, radius, radius);
	}
}
