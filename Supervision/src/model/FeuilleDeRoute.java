package model;

import java.awt.Color;
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
		
		//ajout d'un schedule réservé au retour à l'entrepot
		Schedule retourSch = new Schedule(0, 1440, Color.BLACK);
		Delivery entrepot = new Delivery(zoneGeo.getWarehouse(), retourSch);
		retourSch.appendDelivery(entrepot);
		timeZones.add(retourSch);
		
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
				 fullPath.add(d.getDest());
			}
		}
		return fullPath;
	}
	
	public void computeWithTSP()
	{
		
		GraphLivraisons gl = new GraphLivraisons(this);
		gl.createGraph();
		ArrayList<Delivery> result = gl.calcItineraire();
		
		// pour chaque Schedule, on cherche les deliveries concernées
		for (Schedule sch : timeZones)
		{
			ArrayList<Delivery> newDelivs = new ArrayList<Delivery>();
			for (Delivery d : result)
			{
				if (d.schedule == sch)
				{
					//on les insère, dans l'ordre, dans la nouvelle liste du Schedule
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
	
	public ArrayList<Delivery> getAllDeliveries()
	{
		ArrayList<Delivery> retour = new ArrayList<Node>();
		for (Schedule sch : timeZones)
		{
			for (Delivery d : sch.getDeliveries())
			{
				retour.add(d);
			}
		}
	}
}
