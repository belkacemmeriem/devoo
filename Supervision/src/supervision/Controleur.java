package supervision;

import java.awt.Color;

import parsexml.ParseXML;
import model.ZoneGeo;
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
		}
		selected = null;
	}

	public void click(int x, int y) {
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
			}
		}
		
		viewmain.repaint();
	}

}
