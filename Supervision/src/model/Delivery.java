package model;

public class Delivery
{
	private
	Node dest;
	public void setSchedule(Schedule schedule)
	{
		this.schedule = schedule;
	}


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
