package command;

import model.Delivery;
import model.FeuilleDeRoute;
import model.Node;

/**
 * Renvoie une commande pour annuler ou répéter l'insertion d'un node avant ou après un autre.
 * (en mode modification)
 *
 * @param  n  le node à ajouter.
 * @param  after  vrai si insertion après, faux si insertion avant.
 * @param  e  le node après ou avant lequel on doit ajouter le node.
 * @param  fdr  la feuille de route actuelle
 */
public class CommandInsertNode extends Command {
	
	private Node newNode, existingNode;
	private FeuilleDeRoute fdr;
	private boolean after;

	public CommandInsertNode(Node n, boolean after, Node e, FeuilleDeRoute fdr) {
		this.newNode = n;
		this.after = after;
		this.existingNode = e;
		this.fdr = fdr;
	}

	@Override
	public void undo() {
		fdr.delNode(newNode);
	}

	@Override
	public void redo() {
		Delivery d = fdr.getDelivery(existingNode);
		if (d != null) {
			if (after)
				fdr.insertNodeAfter(newNode, d);
			else
				fdr.insertNodeBefore(newNode, d);
		}
	}

}
