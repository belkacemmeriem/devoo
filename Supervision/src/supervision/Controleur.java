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

import command.CommandeAddNode;
import command.CommandeDelNode;
import command.CommandeInsertNode;
import command.CommandeModifDelNode;
import command.CommandeToggleTournee;
import command.Commandes;

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

	protected Commandes commandes = new Commandes();
	protected ViewMain viewmain;
	protected ZoneGeo zonegeo;
	protected FeuilleDeRoute feuilleDeRoute;
	protected Object selected, highlighted;
	protected int insertButton = 0;
	protected Schedule selectedSchedule;

	protected Etat etat = Etat.VIDE;
	protected Fenetre fenetre;
	protected ArrayList<Schedule> schedules;

	public Controleur() {
	}
	
	public boolean nodeSelected() {
		return (selected != null 
				&& selected instanceof ViewNode);
	}
	
	public boolean deliverySelected() {
		return (nodeSelected() 
				&& feuilleDeRoute.getDelivery(((ViewNode) selected).getNode()) != null);
	}
	
	public boolean warehouseSelected() {
		return (deliverySelected()
				&& feuilleDeRoute.getWarehouse().getDest() == ((ViewNode) selected).getNode());
	}
	
	public int nbDeliveries() {
		return feuilleDeRoute.getAllDeliveries().size() - 1; // on ne compte pas l'entrepot
	}
	
	public Schedule getSelectedSchedule() {
		return selectedSchedule;
	}

	public void setSelectedSchedule(Schedule selectedSchedule) {
		this.selectedSchedule = selectedSchedule;
	}

	public void setFenetre(Fenetre fenetre) {
		this.fenetre = fenetre;
	}

	public void loadSchedules() {
		ParseDelivTimeXML parserSched = new ParseDelivTimeXML();
		schedules = parserSched.getPlagesHoraires();
		fenetre.setSchedules(schedules);
		selectedSchedule = null;
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
				commandes.clear();
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

	public void deselect(Object obj) {
		if (obj != null) {
			if (obj instanceof ViewNode) {
				ViewNode s = (ViewNode) obj;
				s.setDefault();
			}
			else if (obj instanceof ViewArc) {
				ViewArc s = (ViewArc) obj;
				s.setDefault();
			}
		}
	}

	public int click(int x, int y, int button) {
		int retour = -1;
		boolean onlyArcs = (button == 3);
		if (etat != Etat.VIDE)
		{
			Object clicked = viewmain.findAt(x, y, onlyArcs);
			if (clicked == null) {
				deselect(highlighted);
				deselect(selected);
			}
			else if (clicked instanceof ViewNode) {
				ViewNode vn = (ViewNode) clicked;
				deselect(selected);
				deselect(highlighted);
				System.out.println("CLIK NODE");
				if (etat == Etat.MODIFICATION && insertButton != 0
						&& selected != null && selected instanceof ViewNode
						&& vn.getNode() != feuilleDeRoute.getWarehouse().getDest()) {
					ViewNode vns = (ViewNode) selected;
					Delivery d = feuilleDeRoute.getDelivery(vn.getNode());
					System.out.println("COND REUN");
					if (insertButton == 1) {
						feuilleDeRoute.insertNodeBefore(vns.getNode(), d);
						commandes.add(new CommandeInsertNode(vns.getNode(), false, vn.getNode(), feuilleDeRoute));
					} else if (insertButton == 2) {
						feuilleDeRoute.insertNodeAfter(vns.getNode(), d);
						commandes.add(new CommandeInsertNode(vns.getNode(), true, vn.getNode(), feuilleDeRoute));
					}
					viewmain.updateFeuilleDeRoute();
				}
				vn.setColor(new Color(255, 0, 0));
				vn.setRadius(11);
				Delivery deliv = feuilleDeRoute.getDelivery(vn.getNode());
				if (deliv != null && deliv != feuilleDeRoute.getWarehouse()) {
					selectedSchedule = deliv.getSchedule();
					fenetre.setSchedule(selectedSchedule);
				}
				retour = vn.getNode().getID();
			}
			else if (clicked instanceof ViewArc) {
				deselect(selected);
				deselect(highlighted);
				ViewArc va = (ViewArc) clicked;
				va.setColor(new Color(255, 0, 0));
				va.setEpaisseur(3);
			}
			highlighted = null;
			selected = clicked;
		}

		setInsertButton(0);
		viewmain.repaint();
		fenetre.update();
		return retour;
	}
	
	public void highlight(int x, int y) {
		if (etat != Etat.VIDE)
		{
			Object high = viewmain.findAt(x, y, false);
			if (high != null && high == selected) {
				deselect(highlighted);
			}
			else if (high == null) {
				deselect(highlighted);
			}
			else if (high instanceof ViewNode) {
				deselect(highlighted);
				highlighted = high;
				ViewNode vn = (ViewNode) high;
				vn.setColor(new Color(255, 255, 0));
				vn.setRadius(14);
			}
			else if (high instanceof ViewArc) {
				deselect(highlighted);
				highlighted = high;
				ViewArc va = (ViewArc) high;
				va.setEpaisseur(4);
			}
		}

		viewmain.repaint();
		fenetre.update();
	}

	public void add() {
		if (selected != null && selected instanceof ViewNode && selectedSchedule != null) {
			Node n = ((ViewNode) selected).getNode();
			commandes.add(new CommandeAddNode(n, selectedSchedule, feuilleDeRoute));
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
			if (etat == Etat.MODIFICATION)
				commandes.add(new CommandeModifDelNode(n, feuilleDeRoute));
			else
				commandes.add(new CommandeDelNode(n, feuilleDeRoute));
			feuilleDeRoute.delNode(n);
			viewmain.updateFeuilleDeRoute();
			viewmain.repaint();
			fenetre.update();
		}
	}

	public Etat getEtat() {
		return etat;
	}

	public void toggleGenererTournee(boolean record) {
		if (record)
			commandes.add(new CommandeToggleTournee(this));
		if (etat == Etat.REMPLISSAGE) {
			try {
				feuilleDeRoute.computeWithTSP();
			} catch (GraphException e) {
				e.printStackTrace();
			}
			etat = Etat.MODIFICATION;
		} else if (etat == Etat.MODIFICATION) {
			feuilleDeRoute.backToInit();
			etat = Etat.REMPLISSAGE;
		}
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
		fenetre.update();
	}

	public void setInsertButton(int i) {
		if (i != 0 && i == insertButton) // => toggle
			i = 0;
		insertButton = i;
		fenetre.setInsertButton(i);
	}
	
	public FeuilleDeRoute getFeuilleDeRoute() {
		return feuilleDeRoute;
	}
	
	public void undo() {
		commandes.undo();
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
		fenetre.update();
	}
	
	public boolean undoAble() {
		return (commandes.getIndice() != 0);
	}
	
	public void redo() {
		commandes.redo();
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
		fenetre.update();
	}
	
	public boolean redoAble() {
		return (commandes.getIndice() != commandes.size());
	}

}