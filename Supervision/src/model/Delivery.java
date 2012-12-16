package model;

public class Delivery
{
	protected
	Node dest;



	Schedule schedule;
	Chemin pathToDest;
	
	Integer heurePrevue = null;
	boolean retardPrevu;
	
	
	public boolean isRetardPrevu()
	{
		return retardPrevu;
	}

	public void setRetardPrevu(boolean retardPrevu)
	{
		this.retardPrevu = retardPrevu;
	}
	
	public Integer getHeurePrevue()
	{
		return heurePrevue;
	}

	public void setSchedule(Schedule schedule)
	{
		this.schedule = schedule;
	}
	
	public void setHeurePrevue(Integer heurePrevue)
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

	public void resetHeuresEtChemin()
	{
		setHeurePrevue(null);
		setRetardPrevu(false);
		pathToDest = null;
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


	public void setPathToDest(Chemin pathToDest)
	{
		this.pathToDest = pathToDest;
	}
	
	
}
