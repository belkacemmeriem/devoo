package views;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Delivery;
import model.Schedule;

public class ViewSchedule {
	Schedule schedule;
	ViewMain mere;
	ArrayList<ViewDelivery> deliveries = new ArrayList<ViewDelivery>();

	public ViewSchedule(Schedule s, ViewMain m) {
		schedule = s;
		mere = m;
		update();
	}
	
	public void update() {
		deliveries.clear();
		for (Delivery d : schedule.getDeliveries()) {
			ViewDelivery vd = new ViewDelivery(d, mere);
			System.out.println("DELIVERY "+ Schedule.timeToString(d.getSchedule().getStartTime()) + " +1"); 
			deliveries.add(vd);
		}
	}
	
	public void paint(Graphics g, boolean onlyNodes) {
		for (ViewDelivery vd : deliveries) {
			vd.paint(g, onlyNodes);
		}
	}
	
	public List<ViewArc> getViewArcs()
	{
		LinkedList<ViewArc> list = new LinkedList<ViewArc>();
		for (ViewDelivery deliv: deliveries)
		{
			list.addAll(deliv.getViewArcs());
		}
		return list;
	}
}
