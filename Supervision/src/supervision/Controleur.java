package supervision;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

import parsexml.ParseXML;
import model.ZoneGeo;
import views.ViewArc;
import views.ViewMain;
import views.ViewNode;

public class Controleur {

	protected
	ViewMain viewmain;
	ZoneGeo zonegeo;
	Object selected;
	Etat etat = Etat.VIDE;
	
	public Controleur() {
		loadZone("./content/plan400.xml");
	}
	
	public void loadZone(String path) {
		zonegeo = new ZoneGeo();
    	ParseXML parseXml = new ParseXML(path, zonegeo);
    	if (viewmain != null) {
    		viewmain.setZoneGeo(zonegeo);
    		viewmain.repaint();
    	}
    	etat = Etat.REMPLISSAGE;
	}
	
	public void setViewMain(ViewMain vm) {
		viewmain = vm;
    	if (viewmain != null) {
    		System.out.println("HERE");
    		viewmain.setZoneGeo(zonegeo);
    		viewmain.repaint();
    	}
	}
	
	public void deselect() {
		if (selected != null) {
			if (selected instanceof ViewNode) {
				ViewNode s = (ViewNode) selected;
				s.setColor(new Color(0, 0, 0));
				s.setRadius(8);
			}
			else if (selected instanceof ViewArc) {
				ViewArc s = (ViewArc) selected;
				s.setColor(new Color(150, 150, 150));
			}
		}
		selected = null;
	}

	public int click(int x, int y) {
        int retour = -1;
		if (etat == Etat.REMPLISSAGE)
		{
			Object clicked = viewmain.findAt(x, y);
			if (clicked == null) {
				deselect();                             
			}
			else if (clicked instanceof ViewNode) {
				deselect();
				selected = clicked;
				ViewNode vn = (ViewNode) clicked;
				vn.setColor(new Color(255, 0, 0));
				vn.setRadius(16);
                retour = vn.getNode().getID();
			}
			else if (clicked instanceof ViewArc) {
				deselect();
				selected = clicked;
				ViewArc va = (ViewArc) clicked;
				va.setColor(new Color(255, 0, 0));
				System.out.println("OK");
			}
		}
                		
		viewmain.repaint();
        return retour;
	}

}