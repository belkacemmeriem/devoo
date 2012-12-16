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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import parsexml.*;
import model.Arc;
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
	
	public String getLabel() {
		String s = "Aucune.";
		if (selected instanceof ViewNode) {
			Node n = ((ViewNode) selected).getNode();
			s = "[Intersection " + n.getID() + "]";
			Delivery d = feuilleDeRoute.getDelivery(n);
			if (etat == Etat.MODIFICATION && d != null) {
				s += " Passage a ~" + Schedule.timeToString(d.getHeurePrevue());
				if (d.isRetardPrevu())
					s += " (en retard)";
			}
		} else if (selected instanceof ViewArc) {
			Arc a = ((ViewArc) selected).getArc();
			s = "[Rue " + a.getName() + "] Temps de parcourt : ~" + Math.round(a.getDuration()) + " minutes";
		}
		return s;
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
		if(fenetre.getListLivraison()!=null)
		{
			fenetre.getListLivraison().removeAll();
		}
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

	public ViewMain getViewMain() {
		return viewmain;
	}
	
	public void selectNode(Node n) {
		ViewNode vn = viewmain.getNode(n);
		vn.setColor(new Color(255, 0, 0));
		vn.setRadius(12);
	}
	
	public void deselectNode(Node n) {
		ViewNode vn = viewmain.getNode(n);
		vn.setDefault();
	}

	public void deselect(Object obj) {
		if (obj != null) {
			if (obj instanceof ViewNode) {
				ViewNode s = (ViewNode) obj;
				deselectNode(s.getNode());
			}
			else if (obj instanceof ViewArc) {
				ViewArc s = (ViewArc) obj;
				s.setDefault();
			}
		}
	}

	public void click(int x, int y, int button) {
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
					fenetre.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
					viewmain.updateFeuilleDeRoute();
				}
				selectNode(vn.getNode());
				Delivery deliv = feuilleDeRoute.getDelivery(vn.getNode());
				if (deliv != null && deliv != feuilleDeRoute.getWarehouse()) {
					selectedSchedule = deliv.getSchedule();
					fenetre.setSchedule(selectedSchedule);
				}
				fenetre.getListLivraison().setSelected(vn.getNode().getID().toString());
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
			fenetre.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
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
			fenetre.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
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
				etat = Etat.MODIFICATION;
			} catch (GraphException e) {
				Object[] options = { "Ok" };
				int optionChoisie = JOptionPane.showOptionDialog(new JFrame(),
							"Trop de données : la tournée n'a pas pu etre calculée.",
							"Trop de données",
							JOptionPane.ERROR_MESSAGE, 
							JOptionPane.ERROR_MESSAGE, null,
							options, options[0]);
			}
		} else if (etat == Etat.MODIFICATION) {
			feuilleDeRoute.backToInit();
			etat = Etat.REMPLISSAGE;
		}
		fenetre.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
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
		fenetre.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
		fenetre.update();
	}
	
	public boolean undoAble() {
		return (commandes.getIndice() != 0);
	}
	
	public void redo() {
		commandes.redo();
		fenetre.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
		viewmain.updateFeuilleDeRoute();
		viewmain.repaint();
		fenetre.update();
	}
	
	public boolean redoAble() {
		return (commandes.getIndice() != commandes.size());
	}

}