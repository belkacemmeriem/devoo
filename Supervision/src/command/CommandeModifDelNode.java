package command;

import model.Delivery;
import model.FeuilleDeRoute;
import model.Node;
import model.Schedule;

public class CommandeModifDelNode extends Commande {
	
	private Node node, ref;
	private FeuilleDeRoute fdr;
	private Schedule schedule;
	private enum InsertState { BEFORE, AFTER, ALONE };
	private InsertState insertState;

	public CommandeModifDelNode(Node n, FeuilleDeRoute fdr) {
		this.node = n;
		Delivery me = fdr.getDelivery(node);
		System.out.println("A");
		Delivery prev = fdr.previousDelivery(me);
		System.out.println("B");
		Delivery next = fdr.nextDelivery(me);
		schedule = me.getSchedule();
		if (prev.getSchedule() == schedule) {
			ref = prev.getDest();
			insertState = InsertState.AFTER;
		} else if (next.getSchedule() == schedule) {
			ref = next.getDest();
			insertState = InsertState.BEFORE;
		} else {
			ref = null;
			insertState = InsertState.ALONE;
		}
			
		
		this.fdr = fdr;
	}

	@Override
	public void undo() {
		Delivery d = null;
		if (ref != null)
			d = fdr.getDelivery(ref);
		
		switch (insertState) {
		case AFTER:
			if (d != null)
				fdr.insertNodeAfter(node, d);
			break;
			
		case BEFORE:
			if (d != null)
				fdr.insertNodeBefore(node, d);
			break;
			
		case ALONE:
			Delivery newDelivery = new Delivery(node, schedule);
			schedule.insert(newDelivery, 0);
			fdr.recalcPathTo(newDelivery);
			fdr.recalcPathTo(fdr.nextDelivery(newDelivery));		
			fdr.computeArrivalTimes();
			break;
		}
	}

	@Override
	public void redo() {
		fdr.delNode(node);
	}

}
