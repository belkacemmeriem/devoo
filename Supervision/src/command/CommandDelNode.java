package command;

import model.Delivery;
import model.FeuilleDeRoute;
import model.Node;
import model.Schedule;

/**
 * Renvoie une commande pour annuler ou répéter la suppression d'un node en mode remplissage.
 *
 * @param  n  le node à ajouter
 * @param  fdr  la feuille de route actuelle
 */
public class CommandDelNode extends Command {
	
	private Node node;
	private FeuilleDeRoute fdr;
	private Schedule schedule;

	public CommandDelNode(Node n, FeuilleDeRoute fdr) {
		this.node = n;
		this.fdr = fdr;
		this.schedule = fdr.getDelivery(n).getSchedule();
	}

	@Override
	public void undo() {
		fdr.addNode(node, schedule);
	}

	@Override
	public void redo() {
		fdr.delNode(node);
	}

}
