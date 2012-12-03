package model;

public class Delivery
{
	Node dest;
	Schedule schedule;
	Chemin pathToDest;
	
	protected int heurePrevue;
	
	public int getHeurePrevue()
	{
		return heurePrevue;
	}


	public void setHeurePrevue(int heurePrevue)
	{
		this.heurePrevue = heurePrevue;
	}


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
