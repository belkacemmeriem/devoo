package command;

import model.Delivery;
import model.RoadMap;
import model.Node;
import model.Schedule;

/**
 * Renvoie une commande pour annuler ou répéter l'ajout d'un node dans un schedule.
 * (en mode remplissage)
 *
 * @param  node  le node à ajouter
 * @param  newSchedule le schedule où on doit ajouter le node
 * @param  fdr  la feuille de route actuelle
 */
public class CommandAddNode extends Command {

	Node node;
	Schedule newSchedule, oldSchedule;
	RoadMap rm;
	
	public CommandAddNode(Node node, Schedule newSchedule, RoadMap rm) {
		this.node = node;
		this.newSchedule = newSchedule;
		Delivery d = rm.getDelivery(node);
		if (d != null)
			oldSchedule = d.getSchedule();
		else
			oldSchedule = null;
		this.rm = rm;
	}

	@Override
	public void undo() {
		rm.delNode(node);
		if (oldSchedule != null)
			rm.addNode(node, oldSchedule);
	}

	@Override
	public void redo() {
		Delivery d = rm.getDelivery(node);
		if (d != null)
			rm.delNode(node);
		rm.addNode(node, newSchedule);
	}

}
