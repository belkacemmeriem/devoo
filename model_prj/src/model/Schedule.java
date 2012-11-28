package model;

/**
 * 
 * @author yann
 *
 */
public class Schedule
{
	float start, end;	//en HEURES!

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
	
	public String startStr()
	{
		return floatToTime(start);
	}
	
	public String endStr()
	{
		return floatToTime(end);
	}
	
	protected String floatToTime(float value)
	{
		String str = new String();
		
		Integer heures = (int)Math.floor(value);
		if (heures > 9)
			str+=heures.toString();
		else
			str+= '0'+heures.toString();
		
		str+= ":";
		
		Integer minutes = (int)((value-Math.floor(value))*60);
		if (minutes > 9)
			str+=minutes.toString();
		else
			str+= '0'+minutes.toString();
		return str;
	}
	
}
