package views;

import java.awt.Graphics;
import java.util.ArrayList;

import model.EtatFDR;
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
			System.out.print("X");
			boolean onlyNodes = (feuilleDeRoute.getEtat() == EtatFDR.INIT);
			vs.paint(g, onlyNodes);
		}
	}
	
	public void update() {
		for (ViewSchedule vs : schedules) {
			vs.update();
		}
	}
}
