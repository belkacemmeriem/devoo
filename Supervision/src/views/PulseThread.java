package views;

import java.util.LinkedList;
import sun.awt.Mutex;

public class PulseThread extends Thread
{
	protected LinkedList<ViewArcPath> pulsingArcs;
	protected Mutex mtx_pulsingArcs;
	protected Mutex mtx_sleepTime;
	protected boolean running = true;
	protected ViewMain viewMain;
	
	int sleepTime;
	final int DEF_WIDTH = ViewArcPath.defaultThick;
	final int PULSE_WIDTH = 6;
	
    public void run()
    {
    	int i = 0;
    	ViewArcPath last = pulsingArcs.getLast();;
    	while (running)
    	{
    		if (i>=pulsingArcs.size())
    			i = 0;
    		
    		last.setThick(DEF_WIDTH);
    		
    		mtx_pulsingArcs.lock();
    		pulsingArcs.get(i).setThick(PULSE_WIDTH);
    		last = pulsingArcs.get(i);
    		mtx_pulsingArcs.unlock();
    		
    		viewMain.repaint();
    		i++;
    		try
			{
				Thread.sleep(sleepTime);
			} catch (InterruptedException e)
			{
                Thread.currentThread().interrupt(); // Très important de réinterrompre
                running = false; // Sortie de la boucle infinie
			}
    	}
    }
	
	public void setSleepTime(int sleepTime)
	{
		this.sleepTime = sleepTime;
	}

	public PulseThread(LinkedList<ViewArcPath> pulsingArcs,
			Mutex mtx_pulsingArcs,
			ViewMain mere,
			int sleepTime)
	{
		super();
		this.pulsingArcs = pulsingArcs;
		this.mtx_pulsingArcs = mtx_pulsingArcs;
		this.viewMain = mere;
		this.sleepTime = sleepTime;
	}
}
