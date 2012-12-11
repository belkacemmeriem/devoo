package supervision;

import Exception.GraphException;
import Exception.ReadMapXMLException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import parsexml.*;
import model.Delivery;
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
	Schedule selectedSchedule;

	Etat etat = Etat.VIDE;
	Fenetre fenetre;
	ArrayList<Schedule> schedules;

	public Controleur() {
	}
	
	public void setEtat(Etat aetat){
		etat=aetat;
	}
	
	public boolean nodeSelected() {
		return (selected != null && selected instanceof ViewNode);
	}
	
	public boolean deliverySelected() {
		return (nodeSelected() && feuilleDeRoute.getDelivery(((ViewNode) selected).getNode()) != null);
	}
	
	public Schedule getSelectedSchedule() {
		return selectedSchedule;
	}

	public void setSelectedSchedule(Schedule selectedSchedule) {
		this.selectedSchedule = selectedSchedule;
	}

	public void setFenetre(Fenetre fenetre) {
		this.fenetre = fenetre;
		// loadSchedules(); <-- pas necessaire
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
		if (path != null)
		{
			loadSchedules();
			try {
				zonegeo = new ZoneGeo();
				new ParseMapXML(path, zonegeo);
				feuilleDeRoute = new FeuilleDeRoute(schedules, zonegeo);
				etat = Etat.REMPLISSAGE;
				viewmain.setZoneGeo(zonegeo,path);
				viewmain.setFeuilleDeRoute(feuilleDeRoute);
				viewmain.repaint();
				fenetre.validate();
				fenetre.update();
			} catch (ReadMapXMLException ex) {
				new ViewError(ex.getMessage());
				zonegeo = null;
			}
		}
	}

	public void exportReport(File path) {
		if(path!= null)
		try {
			feuilleDeRoute.generateReport(path);
		} catch (IOException e) {
		}
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
		if (etat != Etat.VIDE)
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
				Delivery deliv = feuilleDeRoute.getDelivery(vn.getNode());
				if (deliv != null) {
					selectedSchedule = deliv.getSchedule();
					fenetre.setSchedule(selectedSchedule);
				}
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
		fenetre.update();
		return retour;
	}

	public void add() {
		if (selected != null && selected instanceof ViewNode && selectedSchedule != null) {
			Node n = ((ViewNode) selected).getNode();
			Delivery d = feuilleDeRoute.getDelivery(n);
			if (d != null)
				feuilleDeRoute.delNode(n);
			feuilleDeRoute.addNode(n, selectedSchedule);
			viewmain.updateFeuilleDeRoute();
			viewmain.repaint();
			fenetre.update();
		}
	}

	public void del() {
		if (selected != null && selected instanceof ViewNode) {
			Node n = ((ViewNode) selected).getNode();
			feuilleDeRoute.delNode(n);
			viewmain.updateFeuilleDeRoute();
			viewmain.repaint();
			fenetre.update();
		}
	}

	public Etat getEtat() {
		return etat;
	}

	public void genererTournee() {
		try {
			feuilleDeRoute.computeWithTSP();
		} catch (GraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setEtat(Etat.MODIFICATION);
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
		fenetre.update();
	}

}