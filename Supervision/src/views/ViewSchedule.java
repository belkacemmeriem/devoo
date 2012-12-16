package views;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Delivery;
import model.Schedule;

/**
 * Vue d'un schedule
 * @param s le schedule
 * @param m la vue principale
 */
public class ViewSchedule {
	Schedule schedule;
	ViewMain viewMain;
	ArrayList<ViewDelivery> deliveries = new ArrayList<ViewDelivery>();

	public ViewSchedule(Schedule s, ViewMain m) {
		schedule = s;
		viewMain = m;
		update();
	}
	
	/**
	 * Met à jour la vue du schedule
	 */
	public void update() {
		deliveries.clear();
		for (Delivery d : schedule.getDeliveries()) {
			ViewDelivery vd = new ViewDelivery(d, viewMain);
			deliveries.add(vd);
		}
	}
	
	/**
	 * Peint this sur le canvas.
	 * @param g objet graphics
	 */
	public void paint(Graphics g) {
		for (ViewDelivery vd : deliveries) {
			vd.paint(g);
		}
	}
	
	public List<ViewArcPath> getViewArcs()
	{
		LinkedList<ViewArcPath> list = new LinkedList<ViewArcPath>();
		for (ViewDelivery deliv: deliveries)
		{
			list.addAll(deliv.getViewArcs());
		}
		return list;
	}
}
