package views;

import java.util.LinkedList;
import sun.awt.Mutex;

public class PulseThread extends Thread
{
	protected LinkedList<ViewArcChemin> pulsingArcs;
	protected Mutex mtx_pulsingArcs;
	protected boolean running = true;
	protected ViewMain mere;
	
	final int SLEEP_TIME = 100;
	final int DEF_WIDTH = ViewArcChemin.defaultEpaisseur;
	final int PULSE_WIDTH = 5;
	
    public void run()
    {
    	int i = 0;
    	ViewArcChemin last = pulsingArcs.getLast();;
    	while (running)
    	{
    		if (i>=pulsingArcs.size())
    			i = 0;
    		
    		last.setEpaisseur(DEF_WIDTH);
    		
    		mtx_pulsingArcs.lock();
    		pulsingArcs.get(i).setEpaisseur(PULSE_WIDTH);
    		last = pulsingArcs.get(i);
    		mtx_pulsingArcs.unlock();
    		
    		mere.repaint();
    		i++;
    		try
			{
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e)
			{
                Thread.currentThread().interrupt(); // Très important de réinterrompre
                running = false; // Sortie de la boucle infinie
			}
    	}
    }
	
	public PulseThread(LinkedList<ViewArcChemin> pulsingArcs,
			Mutex mtx_pulsingArcs,
			ViewMain mere)
	{
		super();
		this.pulsingArcs = pulsingArcs;
		this.mtx_pulsingArcs = mtx_pulsingArcs;
		this.mere = mere;
	}
}
