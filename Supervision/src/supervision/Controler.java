package supervision;

import ihm.DeliveryList;
import ihm.ViewError;
import ihm.Window;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Arc;
import model.Delivery;
import model.Node;
import model.RoadMap;
import model.Schedule;
import model.ZoneGeo;
import parsexml.ParseDelivTimeXML;
import parsexml.ParseMapXML;
import views.ViewArc;
import views.ViewMain;
import views.ViewNode;
import Exception.GraphException;
import Exception.ReadMapXMLException;

import command.CommandAddNode;
import command.CommandDelNode;
import command.CommandInsertNode;
import command.CommandList;
import command.CommandModifDelNode;
import command.CommandToggleTournee;

public class Controler {

	protected CommandList commands = new CommandList();
	protected ViewMain viewMain;
	protected ZoneGeo zoneGeo;
	protected RoadMap feuilleDeRoute;
	protected Object selected, highlighted;
	protected int insertButton = 0;
	protected Schedule selectedSchedule;

	protected State etat = State.EMPTY;
	protected Window window;
	protected ArrayList<Schedule> schedules;
	
	/**
	 * Renvoie vrai si un node est sélectionné.
	 * @return vrai si un node est sélectionné.
	 */
	public boolean nodeSelected() {
		return (selected != null 
				&& selected instanceof ViewNode);
	}
	
	/**
	 * Renvoie vrai si une livraison est sélectionnée.
	 * @return vrai si une livraison est sélectionnée.
	 */
	public boolean deliverySelected() {
		return (nodeSelected() 
				&& feuilleDeRoute.getDelivery(((ViewNode) selected).getNode()) != null);
	}
	
	/**
	 * Renvoie vrai si l'entrepôt est sélectionné.
	 * @return vrai si l'entrepôt est sélectionné.
	 */
	public boolean warehouseSelected() {
		return (deliverySelected()
				&& feuilleDeRoute.getWarehouse().getDest() == ((ViewNode) selected).getNode());
	}
	
	/**
	 * Renvoie le nombre de delivery dans la roadmap.
	 * @return le nombre de delivery dans la roadmap.
	 */
	public int nbDeliveries() {
		return feuilleDeRoute.getAllDeliveries().size() - 1; // on ne compte pas l'entrepot
	}
	
	/**
	 * Renvoie un texte indicatif sur ce qui est sélectionné.
	 * @return un texte indicatif sur ce qui est sélectionné.
	 */
	public String getLabel() {
		String s = "Aucune.";
		if (selected instanceof ViewNode) {
			Node n = ((ViewNode) selected).getNode();
			s = "[Intersection " + n.getID() + "]";
			Delivery d = feuilleDeRoute.getDelivery(n);
			if (etat == State.MODIFICATION && d != null) {
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

	public void setSelectedSchedule(Schedule s) {
		this.selectedSchedule = s;
	}

	public void setWindow(Window w) {
		this.window = w;
	}

	/**
	 * Charge le fichier contenant la liste des plages horaires.
	 */
	public void loadSchedules() {
		if(window.getListLivraison()!=null)
		{
			window.getListLivraison().removeAll();
		}
		ParseDelivTimeXML parserSched = new ParseDelivTimeXML();
		schedules = parserSched.getPlagesHoraires();
		window.setSchedules(schedules);
		selectedSchedule = null;
	}

	/**
	 * Charge une zone passée en paramètres.
	 * @param path un fichier de zone
	 */
	public void loadZone(File path) {
		if (path != null) {
			loadSchedules();
			try {
				zoneGeo = new ZoneGeo();
				new ParseMapXML(path, zoneGeo);
				feuilleDeRoute = new RoadMap(schedules, zoneGeo);
				etat = State.FILLING;
				commands.clear();
				viewMain.setZoneGeo(zoneGeo,path);
				viewMain.setRoadMap(feuilleDeRoute);
				viewMain.repaint();
				window.validate();
				window.update();
			} catch (ReadMapXMLException ex) {
				new ViewError(ex.getMessage());
				zoneGeo = null;
			}
		}
	}

	/**
	 * Genere un rapport dans le fichier passé en paramètres
	 * @param path fichier où le rapport va être généré
	 */
	public void exportReport(File path) {
		if(path!= null)
		try {
			feuilleDeRoute.generateReport(path);
		} catch (IOException e) {
		}
	}

	public void setViewMain(ViewMain vm) {
		viewMain = vm;
	}

	public ViewMain getViewMain() {
		return viewMain;
	}
	
	/**
	 * Sélectionne le node n sur le plan et dans la liste.
	 * @param n le node à sélectionner
	 */
	public void selectNode(Node n) {
		ViewNode vn = viewMain.getNode(n);
		vn.setColor(new Color(255, 0, 0));
		vn.setRadius(12);
		window.getListLivraison().setSelected(n.getID().toString());
	}
	
	/**
	 * Déselectionne le node n
	 * @param n le node à déselectionner
	 */
	public void deselectNode(Node n) {
		ViewNode vn = viewMain.getNode(n);
		vn.setDefault();
	}

	/**
	 * Déselectionne l'objet passé.
	 * @param obj L'objet à déselectionner
	 */
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

	/**
	 * Effectue un click aux coordonnées x, y
	 * @param x abscisse
	 * @param y ordonnée
	 * @param button bouton de la souris utilisé
	 */
	public void click(int x, int y, int button) {
		boolean onlyArcs = (button == 3);
		if (etat != State.EMPTY)
		{
			Object clicked = viewMain.findAt(x, y, onlyArcs);
			if (clicked == null) {
				deselect(highlighted);
				deselect(selected);
			}
			else if (clicked instanceof ViewNode) {
				ViewNode vn = (ViewNode) clicked;
				deselect(selected);
				deselect(highlighted);
				System.out.println("CLIK NODE");
				if (etat == State.MODIFICATION && insertButton != 0
						&& selected != null && selected instanceof ViewNode
						&& vn.getNode() != feuilleDeRoute.getWarehouse().getDest()) {
					ViewNode vns = (ViewNode) selected;
					Delivery d = feuilleDeRoute.getDelivery(vn.getNode());
					System.out.println("COND REUN");
					if (insertButton == 1) {
						feuilleDeRoute.insertNodeBefore(vns.getNode(), d);
						commands.add(new CommandInsertNode(vns.getNode(), false, vn.getNode(), feuilleDeRoute));
					} else if (insertButton == 2) {
						feuilleDeRoute.insertNodeAfter(vns.getNode(), d);
						commands.add(new CommandInsertNode(vns.getNode(), true, vn.getNode(), feuilleDeRoute));
					}
					window.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
					viewMain.updateRoadMap();
				}
				selectNode(vn.getNode());
				Delivery deliv = feuilleDeRoute.getDelivery(vn.getNode());
				if (deliv != null && deliv != feuilleDeRoute.getWarehouse()) {
					selectedSchedule = deliv.getSchedule();
					window.setSchedule(selectedSchedule);
				}
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
		viewMain.repaint();
		window.update();
	}
	
	/**
	 * Highlight l'objet situé aux coordonnées x, y
	 * @param x abscisse
	 * @param y ordonnée
	 */
	public void highlight(int x, int y) {
		if (etat != State.EMPTY)
		{
			Object high = viewMain.findAt(x, y, false);
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

		viewMain.repaint();
		window.update();
	}

	/**
	 * Ajoute (si possible) le node sélectionné à la feuille de route
	 */
	public void add() {
		if (selected != null && selected instanceof ViewNode && selectedSchedule != null) {
			Node n = ((ViewNode) selected).getNode();
			commands.add(new CommandAddNode(n, selectedSchedule, feuilleDeRoute));
			Delivery d = feuilleDeRoute.getDelivery(n);
			if (d != null)
				feuilleDeRoute.delNode(n);
			feuilleDeRoute.addNode(n, selectedSchedule);
			viewMain.updateRoadMap();
			viewMain.repaint();
			window.update();
			DeliveryList listeLivraison = window.getListLivraison();
			String addr = n.getID().toString();
			if(listeLivraison.livExists(addr)) {
				window.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
			}
			else {
				listeLivraison.addLiv(getSelectedSchedule(), addr);
			}
		}
	}

	/**
	 * Supprime (si possible) le node sélectionné à la feuille de route
	 */
	public void del() {
		if (selected != null && selected instanceof ViewNode) {
			Node n = ((ViewNode) selected).getNode();
			if (etat == State.MODIFICATION)
				commands.add(new CommandModifDelNode(n, feuilleDeRoute));
			else
				commands.add(new CommandDelNode(n, feuilleDeRoute));
			feuilleDeRoute.delNode(n);
			window.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
			viewMain.updateRoadMap();
			viewMain.repaint();
			window.update();
		}
	}

	public State getEtat() {
		return etat;
	}

	/**
	 * Génère ou Dé-Génère l'itinéraire.
	 * @param record indique si on doit enregistrer une command.
	 */
	public void toggleGenererTournee(boolean record) {
		if (record)
			commands.add(new CommandToggleTournee(this));
		if (etat == State.FILLING) {
			try {
				feuilleDeRoute.computeWithTSP();
				etat = State.MODIFICATION;
			} catch (GraphException e) {
				Object[] options = { "Ok" };
				JOptionPane.showOptionDialog(new JFrame(),
					"Trop de données : la tournée n'a pas pu etre calculée.",
					"Trop de données",
					JOptionPane.ERROR_MESSAGE, 
					JOptionPane.ERROR_MESSAGE, null,
					options, options[0]);
			}
		} else if (etat == State.MODIFICATION) {
			feuilleDeRoute.backToInit();
			etat = State.FILLING;
		}
		window.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
		viewMain.updateRoadMap();
		viewMain.repaint();
		window.update();
	}

	public void setInsertButton(int i) {
		if (i != 0 && i == insertButton) // => toggle
			i = 0;
		insertButton = i;
		window.setInsertButton(i);
	}
	
	public RoadMap getFeuilleDeRoute() {
		return feuilleDeRoute;
	}
	
	/**
	 * Annule une action
	 */
	public void undo() {
		commands.undo();
		window.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
		viewMain.updateRoadMap();
		viewMain.repaint();
		window.update();
	}
	
	/**
	 * Renvoie vrai si une annulation est possible
	 * @return vrai si une annulation est possible
	 */
	public boolean undoAble() {
		return (commands.getIndice() != 0);
	}
	
	/**
	 * Répète une action
	 */
	public void redo() {
		commands.redo();
		window.getListLivraison().updateAllSchedules(feuilleDeRoute.getSchedules());
		viewMain.updateRoadMap();
		viewMain.repaint();
		window.update();
	}
	
	/**
	 * Renvoie vrai si une répétition est possible
	 * @return vrai si une répétition est possible
	 */
	public boolean redoAble() {
		return (commands.getIndice() != commands.size());
	}

}