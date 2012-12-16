package command;

import model.Node;
import model.RoadMap;
import model.Schedule;

/**
 * Renvoie une commande pour annuler ou répéter la suppression d'un node en mode remplissage.
 *
 * @param  n  le node à ajouter
 * @param  rm  la feuille de route actuelle
 */
public class CommandDelNode extends Command {
	
	private Node node;
	private RoadMap rm;
	private Schedule schedule;

	public CommandDelNode(Node n, RoadMap rm) {
		this.node = n;
		this.rm = rm;
		this.schedule = rm.getDelivery(n).getSchedule();
	}

	@Override
	public void undo() {
		rm.addNode(node, schedule);
	}

	@Override
	public void redo() {
		rm.delNode(node);
	}

}
