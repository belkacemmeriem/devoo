package model;

public class Delivery
{
	Node dest;
	Schedule schedule;
	Chemin pathToDest;
	
	
	public Node getDest()
	{
		return dest;
	}


	public Schedule getSchedule()
	{
		return schedule;
	}


	public Chemin getPathToDest()
	{
		return pathToDest;
	}


	public Delivery(Node destination, Schedule schedule, Chemin pathToDest)
	{
		super();
		this.dest = destination;
		this.schedule = schedule;
		this.pathToDest = pathToDest;
	}
	
}
