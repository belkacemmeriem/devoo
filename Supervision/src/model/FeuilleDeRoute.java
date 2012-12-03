package model;

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
}
