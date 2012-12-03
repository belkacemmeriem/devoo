package views;

import java.awt.Graphics;
import java.util.ArrayList;

import model.Delivery;
import model.Schedule;

public class ViewSchedule {
	Schedule schedule;
	ViewMain mere;
	ArrayList<ViewDelivery> deliveries = new ArrayList<ViewDelivery>();

	public ViewSchedule(Schedule s, ViewMain m) {
		schedule = s;
		mere = m;
		for (Delivery d : s.getDeliveries()) {
			ViewDelivery vd = new ViewDelivery(d, mere);
			deliveries.add(vd);
		}
	}
	
	public void paint(Graphics g) {
		for (ViewDelivery vd : deliveries) {
			vd.paint(g);
		}
	}
}
