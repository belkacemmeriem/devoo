package command;

import model.Delivery;
import model.RoadMap;
import model.Node;
import model.Schedule;

/**
 * Renvoie une commande pour annuler ou répéter la suppression d'un node en mode modification.
 *
 * @param  n  le node à ajouter
 * @param  rm  la feuille de route actuelle
 */
public class CommandModifDelNode extends Command {
	
	private Node node, ref;
	private RoadMap rm;
	private Schedule schedule;
	private enum InsertState { BEFORE, AFTER, ALONE };
	private InsertState insertState;

	public CommandModifDelNode(Node n, RoadMap rm) {
		this.node = n;
		Delivery me = rm.getDelivery(node);
		System.out.println("A");
		Delivery prev = rm.previousDelivery(me);
		System.out.println("B");
		Delivery next = rm.nextDelivery(me);
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
		
		this.rm = rm;
	}

	@Override
	public void undo() {
		Delivery d = null;
		if (ref != null)
			d = rm.getDelivery(ref);
		
		switch (insertState) {
		case AFTER:
			if (d != null)
				rm.insertNodeAfter(node, d);
			break;
			
		case BEFORE:
			if (d != null)
				rm.insertNodeBefore(node, d);
			break;
			
		case ALONE:
			Delivery newDelivery = new Delivery(node, schedule);
			schedule.insert(newDelivery, 0);
			rm.recalcPathTo(newDelivery);
			rm.recalcPathTo(rm.nextDelivery(newDelivery));		
			rm.computeArrivalTimes();
			break;
		}
	}

	@Override
	public void redo() {
		rm.delNode(node);
	}

}
