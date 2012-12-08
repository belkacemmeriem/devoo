package views;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sun.awt.Mutex;
import java.util.concurrent.locks.ReadWriteLock;

import model.EtatFDR;
import model.FeuilleDeRoute;
import model.Schedule;

public class ViewFeuilleDeRoute {
	protected FeuilleDeRoute feuilleDeRoute;
	protected ViewMain mere;
	protected ArrayList<ViewSchedule> schedules = new ArrayList<ViewSchedule>();
	protected LinkedList<ViewArc> pulsingArcs;
	protected ReadWriteLock mtx_pulsingArcs;
	
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
			boolean onlyNodes = (feuilleDeRoute.getEtat() == EtatFDR.INIT);
			vs.paint(g, onlyNodes);
		}
	}
	
	public void update() {
		for (ViewSchedule vs : schedules) {
			vs.update();
		}
	}
	
	protected List<ViewArc> updateViewArcs()
	{
		LinkedList<ViewArc> list = new LinkedList<ViewArc>();
		for (ViewSchedule sch: schedules)
		{
			list.addAll(sch.getViewArcs());
		}
		mtx_pulsingArcs.writeLock().lock();
		pulsingArcs = list;
		mtx_pulsingArcs.writeLock().unlock();
		return list;
	}
}
