package model;

import java.awt.Color;
import java.util.ArrayList;

/**
 * 
 * @author yann
 *
 */
public class Schedule
{
	protected
	int startTime, endTime;	//en minutes
    Color couleur;
	ArrayList<Delivery> deliveries = new ArrayList<Delivery>();

	
	public ArrayList<Delivery> getDeliveries()
	{
		return deliveries;
	}

	public Schedule(int start, int end, Color color)
	{
		super();
		
		this.startTime = start;
		this.endTime = end;
		this.couleur = color;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public int getEndTime()
	{
		return endTime;
	}
	
	public String startTimeString()
	{
		return timeToString(startTime);
	}
	
	public String endTimeString()
	{
		return timeToString(endTime);
	}
	
	public void appendDelivery(Delivery delivery)
	{
		delivery.setSchedule(this);
		deliveries.add(delivery);
	}
	
	public void setDeliveries(ArrayList<Delivery> delivs)
	{
		this.deliveries = delivs;
		for (Delivery d : this.deliveries)
		{
			d.setSchedule(this);
		}
	}

	static public String timeToString(int time)
	{
		String str = new String();
		
		Integer heures = (int) (time/60);
		if (heures > 9)
			str+=heures.toString();
		else
			str+= '0'+heures.toString();
		
		str+= ":";
		
		Integer minutes = (int)(time%60);
		if (minutes > 9)
			str+=minutes.toString();
		else
			str+= '0'+minutes.toString();
		return str;
	}
	
	/*
	 * ANCIENNE VERSION (en float, heures)
	 * 
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
	*/
}
