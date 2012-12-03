package model;

public class Delivery
{
	Node dest;
	Schedule schedule;
	Chemin pathToDest;
	
	protected int heurePrevue;
	
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


	public Delivery(Node destination, Schedule schedule)
	{
		super();
		this.dest = destination;
		this.schedule = schedule;
	}


	public void setPathToDest(Chemin pathToDest) {
		this.pathToDest = pathToDest;
	}
	
}
