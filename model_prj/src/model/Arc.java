package model;

public class Arc
{
	protected Node		origin, dest;
	protected int		speed;
	protected int		lenght;
	protected String	name;

	public Arc(Node origin, Node dest, int speed, int lenght, String name)
	{
		super();
		this.origin = origin;
		this.dest = dest;
		this.speed = speed;
		this.lenght = lenght;
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
	
	public float getDuration()
	{
		return lenght/speed;
	}
}
