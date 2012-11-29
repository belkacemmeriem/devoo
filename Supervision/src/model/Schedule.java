package model;

import java.util.ArrayList;

/**
 * 
 * @author yann
 *
 */
public class Schedule
{
	protected
	float startTime, endTime;	//en HEURES! ex: 1,5 = 1h30
	ArrayList<Delivery> deliveries = new ArrayList<Delivery>();

	
	public ArrayList<Delivery> getDeliveries()
	{
		return deliveries;
	}

	public Schedule(float start, float end)
	{
		super();
		
		this.startTime = start;
		this.endTime = end;
	}

	public float getStartTime()
	{
		return startTime;
	}

	public void setStartTime(float start)
	{
		this.startTime = start;
	}

	public float getEndTime()
	{
		return endTime;
	}

	public void setEndTime(float end)
	{
		this.endTime = end;
	}
	
	public String startTimeString()
	{
		return floatToTime(startTime);
	}
	
	public String endTimeString()
	{
		return floatToTime(endTime);
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
