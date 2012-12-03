package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import tsp.GraphLivraisons;

public class FeuilleDeRoute
{
	protected
	ArrayList<Schedule> timeZones;
	
	protected ZoneGeo zoneGeo;

	public FeuilleDeRoute(ArrayList<Schedule> timeZones, ZoneGeo zoneGeo)
	{
		super();
		this.timeZones = timeZones;
		this.zoneGeo = zoneGeo;
	}

	public ZoneGeo getZoneGeo() {
		return zoneGeo;
	}
	
	public ArrayList<Schedule> getTimeZones()
	{
		return timeZones;
	}
	
	public ArrayList<Node> getFullPath()
	{
		ArrayList<Node> fullPath = new ArrayList<Node>();
		for (Schedule s : timeZones)
		{
			for (Delivery d : s.getDeliveries())
			{
				 fullPath.addAll(d.getPathToDest().getTrajectory());
				 fullPath.add(d.dest);
			}
		}
		return fullPath;
	}
	
	public void computeWithTSP()
	{
		GraphLivraisons gl = new GraphLivraisons(this);
		gl.createGraph();
		ArrayList<Delivery> result = gl.calcItineraire();
		
		// pour chaque Schedule, on cherche les deliveries concern√©es
		for (Schedule sch : timeZones)
		{
			ArrayList<Delivery> newDelivs = new ArrayList<Delivery>();
			for (Delivery d : result)
			{
				if (d.schedule == sch)
				{
					//on les ins√®re, dans l'ordre, dans la nouvelle liste du Schedule
					newDelivs.add(d);
				}
			}
			sch.setDeliveries(newDelivs);
		}
		computeArrivalTimes();
	}

	
	protected void computeArrivalTimes()
	{
		
	}
	
	public void generateReport() throws IOException
	{
	    PrintWriter writer;
	    int n = 5;

	    writer =  new PrintWriter(new BufferedWriter(new FileWriter("rapport.txt")));
	   
	    writer.println("Rapport de la feuille de route pour le livreur.");
	    writer.println("La tournée est divisée en "+timeZones.size()+" créneaux");
	    for(Schedule s: timeZones)
	    {
		    writer.println("Dans le créneau" + s.startTime+  " "+s.endTime);
		    for(Delivery d : s.getDeliveries())
		    {
		    	for(Node a :d.getPathToDest().getTrajectory())
		    	{
				    writer.println("Passer par le noeud:"+ a.getID());

		    	}
			    writer.println("Arrivee prevue a:"+ d.getHeurePrevue());
			    writer.println("Arrivee prevue a:"+ d.getHeurePrevue());

		    }
	    }

	    writer.close();
	}
}
