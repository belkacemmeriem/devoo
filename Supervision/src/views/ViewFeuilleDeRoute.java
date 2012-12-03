package views;

import java.awt.Graphics;
import java.util.ArrayList;

import model.FeuilleDeRoute;
import model.Schedule;

public class ViewFeuilleDeRoute {
	protected FeuilleDeRoute feuilleDeRoute;
	protected ViewMain mere;
	protected ArrayList<ViewSchedule> schedules = new ArrayList<ViewSchedule>();
	
	public ViewFeuilleDeRoute(FeuilleDeRoute f, ViewMain vm) {
		feuilleDeRoute = f;
		mere = vm;
		for (Schedule s : f.getTimeZones()) {
			ViewSchedule vs = new ViewSchedule(s, mere);
			schedules.add(vs);
		}
	}
	
	public void paint(Graphics g) {
		for (ViewSchedule vs : schedules) {
			vs.paint(g);
		}
	}
}
