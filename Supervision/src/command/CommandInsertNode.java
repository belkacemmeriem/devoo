package command;

import model.Delivery;
import model.RoadMap;
import model.Node;

/**
 * Renvoie une commande pour annuler ou répéter l'insertion d'un node avant ou après un autre.
 * (en mode modification)
 *
 * @param  n  le node à ajouter.
 * @param  after  vrai si insertion après, faux si insertion avant.
 * @param  e  le node après ou avant lequel on doit ajouter le node.
 * @param  rm  la feuille de route actuelle
 */
public class CommandInsertNode extends Command {
	
	private Node newNode, existingNode;
	private RoadMap rm;
	private boolean after;

	public CommandInsertNode(Node n, boolean after, Node e, RoadMap rm) {
		this.newNode = n;
		this.after = after;
		this.existingNode = e;
		this.rm = rm;
	}

	@Override
	public void undo() {
		rm.delNode(newNode);
	}

	@Override
	public void redo() {
		Delivery d = rm.getDelivery(existingNode);
		if (d != null) {
			if (after)
				rm.insertNodeAfter(newNode, d);
			else
				rm.insertNodeBefore(newNode, d);
		}
	}

}
