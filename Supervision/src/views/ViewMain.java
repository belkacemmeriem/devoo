package views;

import ihm.Drawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import model.RoadMap;
import model.Node;
import model.ZoneGeo;

import supervision.Controler;

/**
 * La vue principale
 */
public class ViewMain {
	Controler controler;
	Drawing drawing;
	ViewZoneGeo zoneGeo;
	ViewRoadMap roadMap;
	int border = 20;
	java.awt.Image image;
	BufferedImage img;

	
	public ViewMain(Drawing d) 
	{
		drawing = d;
		zoneGeo = null;
		roadMap = null;
	}
	
	public void repaint() {
		drawing.repaint();
	}
	
	public void setZoneGeo(ZoneGeo zg,File path) {
		zoneGeo = new ViewZoneGeo(zg, this);
		 try {
			  img= ImageIO.read(new File(path.toString().substring(0, path.toString().length()-3)+"png"));
			  image=  img.getScaledInstance(drawing.getWidth(),drawing.getHeight(),BufferedImage.SCALE_DEFAULT);
		} catch (IOException e) {
		}
	}
	
	public void setRoadMap(RoadMap f) {
		roadMap = new ViewRoadMap(f, this);
	}
	
	/**
	 * Renvoie le pixel correspondant à une coordonnée du modèle.
	 * @param xplan abscisse du modèle
	 * @return le pixel correspondant à une coordonnée du modèle.
	 */
	public int xpix(int xplan) {
		int canvasWidth = drawing.getWidth() - 2*border;
		float xpourcent = (xplan - zoneGeo.getXmin()) / (float)zoneGeo.getWidth();
		return (int) (border + xpourcent * canvasWidth);
	}
	
	/**
	 * Renvoie le pixel correspondant à une coordonnée du modèle.
	 * @param xplan ordonnée du modèle
	 * @return le pixel correspondant à une coordonnée du modèle.
	 */
	public int ypix(int yplan) {
		int canvasHeight = drawing.getHeight() - 2*border;
		float ypourcent = (yplan - zoneGeo.getYmin()) / (float)zoneGeo.getHeight();
		return (int) (border + ypourcent * canvasHeight);
	}
	
	/**
	 * Renvoie l'objet se trouvant aux coordonnées x,y
	 * @param x abscisse
	 * @param y ordonnée
	 * @param onlyArcs permet de ne sélectionner que les arcs
	 * @return l'objet se trouvant aux coordonnées x,y
	 */
	public Object findAt(int x, int y, boolean onlyArcs) {
		return zoneGeo.findAt(x, y, onlyArcs);
	}
	
	/**
	 * Peint this sur le canvas.
	 * @param g objet graphics
	 */
	public void paint(Graphics g) {
		drawing.setBackground(new Color(255, 255, 255));
		if (zoneGeo != null) {
			if (img != null) {
				image=  img.getScaledInstance(drawing.getWidth(),drawing.getHeight(),BufferedImage.SCALE_DEFAULT);
				g.drawImage(image, 0, 0, null);
			}
			zoneGeo.paint(g);
		}
		if (roadMap != null) {
			roadMap.paint(g);
		}
	}

	public void setControler(Controler ctrl) {
		controler = ctrl;
	}
	
	public Controler getControler() {
		return controler;
	}
	
	public ViewNode getNode(Node n) {
		return zoneGeo.getNode(n);
	}

	public void updateRoadMap() {
		roadMap.update();
	}
	
	public void updatePulseSleep(int sleepTime) {
		if (roadMap != null)
			roadMap.setPulseSleepTime(sleepTime);
	}
}
