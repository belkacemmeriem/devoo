package views;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.RoadMap;
import model.Schedule;
import sun.awt.Mutex;

/**
 * Vue de la roadmap
 * @param r la roadmap
 * @param m la vue principale
 */
public class ViewRoadMap {
	
	public final static int PULSE_SLEEP_MIN = 30;
	public final static int PULSE_SLEEP_MAX = 500;
	
	protected RoadMap roadMap;
	protected ViewMain viewMain;
	protected ArrayList<ViewSchedule> schedules = new ArrayList<ViewSchedule>();
	protected LinkedList<ViewArcPath> pulsingArcs;
	protected Mutex mtx_pulsingArcs = new Mutex();
	protected PulseThread pulseThread = null;
	protected int pulseSleepTime = 100;
	
	public ViewRoadMap(RoadMap r, ViewMain vm) {
		roadMap = r;
		viewMain = vm;
		for (Schedule s : r.getSchedules()) {
			ViewSchedule vs = new ViewSchedule(s, viewMain);
			schedules.add(vs);
		}
	}
	
	/**
	 * Peint this sur le canvas.
	 * @param g objet graphics
	 */
	public void paint(Graphics g) {
		for (ViewSchedule vs : schedules) {
			vs.paint(g);
		}
	}
	
	/**
	 * Met à jour la vue de la roadmap
	 */
	public void update() {
		for (ViewSchedule vs : schedules) {
			vs.update();
		}
		
		updatePulsingArcs();
	}
	
	public void setPulseSleepTime(int sleepTime)
	{
		if (pulseThread != null)
			pulseThread.setSleepTime(sleepTime);
		
		pulseSleepTime = sleepTime;
	}
	
	protected List<ViewArcPath> updatePulsingArcs()
	{
		if (pulseThread != null)
		{
			pulseThread.interrupt();
			pulseThread = null;
		}
		
		LinkedList<ViewArcPath> list = new LinkedList<ViewArcPath>();
		for (ViewSchedule sch: schedules)
		{
			list.addAll(sch.getViewArcs());
		}
		mtx_pulsingArcs.lock();
		pulsingArcs = list;
		mtx_pulsingArcs.unlock();
		
		
		if (list.size() != 0)
		{
			pulseThread = new PulseThread(pulsingArcs, mtx_pulsingArcs, viewMain, pulseSleepTime);
			pulseThread.start();
		}
		return list;
	}
}
