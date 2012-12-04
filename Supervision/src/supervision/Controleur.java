package supervision;

import Exception.NodeIDInexistant;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import parsexml.*;
import model.FeuilleDeRoute;
import model.Node;
import model.Schedule;
import model.ZoneGeo;
import views.ViewArc;
import views.ViewError;
import views.ViewMain;
import views.ViewNode;

public class Controleur {

	protected
	ViewMain viewmain;
	ZoneGeo zonegeo;
	FeuilleDeRoute feuilleDeRoute;
	Object selected;
	Etat etat = Etat.VIDE;
	Fenetre fenetre;
        ArrayList<Schedule> schedules;
	
	public Controleur() {
	}

        public void setFenetre(Fenetre fenetre) {
            this.fenetre = fenetre;
            loadSchedules();
        }
    
        public void loadSchedules()
        {
            ParseDelivTimeXML parserSched = new ParseDelivTimeXML();
            schedules = parserSched.getPlagesHoraires();
            /* // A remettre si necessaire, a virer sinon :
            ArrayList<Schedule> fenSchedules = new ArrayList<Schedule>();
            for (Schedule s : schedules) {
                    fenSchedules.add(s);
            }
            */
            fenetre.setSchedules(schedules);
        }
	
        public void loadZone(File path) {
            zonegeo = new ZoneGeo();
            try {
                ParseMapXML parserMap = new ParseMapXML(path, zonegeo);
            } catch (NodeIDInexistant ex) {
                new ViewError(ex.getMessage(), true);
            }
            feuilleDeRoute = new FeuilleDeRoute(schedules, zonegeo);
            viewmain.setZoneGeo(zonegeo);
            viewmain.setFeuilleDeRoute(feuilleDeRoute);
                    viewmain.repaint();
                    fenetre.validate();
            etat = Etat.REMPLISSAGE;
	}
	
	public void exportReport(File path) {
		// feuilleDeRoute.generateReport(path);
	}
	
	public void setViewMain(ViewMain vm) {
        viewmain = vm;
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
				vn.setRadius(11);
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
	
	public void add() {
		Node n = ((ViewNode) selected).getNode();
		feuilleDeRoute.addNode(n, feuilleDeRoute.getTimeZones().get(1));
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
	}
	
	public Etat getEtat() {
		return etat;
	}

}