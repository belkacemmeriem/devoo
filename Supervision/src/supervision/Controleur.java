package supervision;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.File;

import parsexml.*;
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
        Fenetre fenetre;
	
	public Controleur() {
	}

        public void setFenetre(Fenetre fenetre) {
            this.fenetre = fenetre;
            ParseDelivTimeXML parser = new ParseDelivTimeXML();
            fenetre.setSchedules(parser.getPlagesHoraires());
        }
	
	public void loadZone(File path) {
		zonegeo = new ZoneGeo();
    	ParseMapXML parseXml = new ParseMapXML(path, zonegeo);
    	setAndRepaintZoneGeo();
    	etat = Etat.REMPLISSAGE;
	}
	public void exportReport(File path) {
		// feuilleDeRoute.generateReport(path);
	}
	public void setViewMain(ViewMain vm) {
        viewmain = vm;
        setAndRepaintZoneGeo();
	}
        
    public void setAndRepaintZoneGeo()
    {
        if (viewmain != null && zonegeo != null) {
			viewmain.setZoneGeo(zonegeo);
			viewmain.repaint();
        }
    }
	
	public void deselect() {
		if (selected != null) {
			if (selected instanceof ViewNode) {
				ViewNode s = (ViewNode) selected;
				s.setDefault();
			}
			else if (selected instanceof ViewArc) {
				ViewArc s = (ViewArc) selected;
				s.setDefault();
			}
		}
		selected = null;
	}

	public int click(int x, int y, int button) {
        int retour = -1;
        boolean onlyArcs = (button == 3);
		if (etat == Etat.REMPLISSAGE)
		{
			Object clicked = viewmain.findAt(x, y, onlyArcs);
			if (clicked == null) {
				deselect();                             
			}
			else if (clicked instanceof ViewNode) {
				deselect();
				selected = clicked;
				ViewNode vn = (ViewNode) clicked;
				vn.setColor(new Color(255, 0, 0));
				vn.setRadius(9);
                retour = vn.getNode().getID();
			}
			else if (clicked instanceof ViewArc) {
				deselect();
				selected = clicked;
				ViewArc va = (ViewArc) clicked;
				va.setColor(new Color(255, 0, 0));
				va.setEpaisseur(3);
			}
		}
                		
		viewmain.repaint();
        return retour;
	}

}