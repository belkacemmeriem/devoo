package model;

import java.util.ArrayList;

public class FeuilleDeRoute
{
	protected
	ArrayList<Schedule> timeZones;

	
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
	

}
