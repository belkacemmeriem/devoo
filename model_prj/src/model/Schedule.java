package model;

/**
 * 
 * @author yann
 *
 */
public class Schedule
{
	float start, end;

	public Schedule(float start, float end)
	{
		super();
		
		this.start = start;
		this.end = end;
	}

	public float getStart()
	{
		return start;
	}

	public void setStart(float start)
	{
		this.start = start;
	}

	public float getEnd()
	{
		return end;
	}

	public void setEnd(float end)
	{
		this.end = end;
	}
	
	public String Start_str()
	{
		return floatToTime(start);
	}
	
	public String End_str()
	{
		return floatToTime(end);
	}
	
	protected String floatToTime(float value)
	{
		String str = new String();
		str+= Math.floor(value);
		str+= ":";
		
		int minutes = (int)(value-Math.floor(value))*60;
		if (minutes > 9)
			str+=minutes;
		else
			str+= '0'+minutes;
		return str;
	}
	
}
