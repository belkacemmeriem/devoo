package views;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sun.awt.Mutex;
import model.EtatFDR;
import model.FeuilleDeRoute;
import model.Schedule;

public class ViewFeuilleDeRoute {
	
	public final static int PULSE_SLEEP_MIN = 30;
	public final static int PULSE_SLEEP_MAX = 300;
	
	protected FeuilleDeRoute feuilleDeRoute;
	protected ViewMain mere;
	protected ArrayList<ViewSchedule> schedules = new ArrayList<ViewSchedule>();
	protected LinkedList<ViewArcChemin> pulsingArcs;
	protected Mutex mtx_pulsingArcs = new Mutex();
	protected PulseThread pulseThread = null;
	protected int pulseSleepTime = 100;
	
	public ViewFeuilleDeRoute(FeuilleDeRoute f, ViewMain vm) {
		feuilleDeRoute = f;
		mere = vm;
		for (Schedule s : f.getSchedules()) {
			ViewSchedule vs = new ViewSchedule(s, mere);
			schedules.add(vs);
		}
	}
	
	public void paint(Graphics g) {
		for (ViewSchedule vs : schedules) {
			vs.paint(g);
		}
	}
	
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
	
	protected List<ViewArcChemin> updatePulsingArcs()
	{
		if (pulseThread != null)
		{
			pulseThread.interrupt();
			pulseThread = null;
		}
		
		LinkedList<ViewArcChemin> list = new LinkedList<ViewArcChemin>();
		for (ViewSchedule sch: schedules)
		{
			list.addAll(sch.getViewArcs());
		}
		mtx_pulsingArcs.lock();
		pulsingArcs = list;
		mtx_pulsingArcs.unlock();
		
		
		if (list.size() != 0)
		{
			pulseThread = new PulseThread(pulsingArcs, mtx_pulsingArcs, mere, pulseSleepTime);
			pulseThread.start();
		}
		return list;
	}
}
