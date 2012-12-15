package command;

import model.Delivery;
import model.FeuilleDeRoute;
import model.Node;
import model.Schedule;

public class CommandeDelNode extends Commande {
	
	private Node node;
	private FeuilleDeRoute fdr;
	private Schedule schedule;

	public CommandeDelNode(Node n, FeuilleDeRoute fdr) {
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
