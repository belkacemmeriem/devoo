package command;

import model.Delivery;
import model.FeuilleDeRoute;
import model.Node;

public class CommandeInsertNode extends Commande {
	
	Node newNode, existingNode;
	FeuilleDeRoute fdr;
	boolean after;

	public CommandeInsertNode(Node n, boolean after, Node e, FeuilleDeRoute fdr) {
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
