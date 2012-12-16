package command;

import model.Delivery;
import model.FeuilleDeRoute;
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
	FeuilleDeRoute fdr;
	
	public CommandAddNode(Node node, Schedule newSchedule, FeuilleDeRoute fdr) {
		this.node = node;
		this.newSchedule = newSchedule;
		Delivery d = fdr.getDelivery(node);
		if (d != null)
			oldSchedule = d.getSchedule();
		else
			oldSchedule = null;
		this.fdr = fdr;
	}

	@Override
	public void undo() {
		fdr.delNode(node);
		if (oldSchedule != null)
			fdr.addNode(node, oldSchedule);
	}

	@Override
	public void redo() {
		Delivery d = fdr.getDelivery(node);
		if (d != null)
			fdr.delNode(node);
		fdr.addNode(node, newSchedule);
	}

}
