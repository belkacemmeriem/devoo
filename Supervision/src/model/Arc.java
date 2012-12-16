package model;

public class Arc
{
	protected Node		origin, dest;
	protected int		speed;
	protected int		length;
	protected String	name;

	public String getName() {
		return name;
	}

	public Arc(Node origin, Node dest, int speed, int length, String name)
	{
		super();
		this.origin = origin;
		this.dest = dest;
		this.speed = speed;
		this.length = length;
		this.name = name;
	}	

	public Node getOrigin()
	{
		return origin;
	}

	public Node getDest()
	{
		return dest;
	}
	
	public double getDuration()
	{
		return ((double)length/speed/60.0);
	}
}
