package test;
import static org.junit.Assert.*;


import model.Arc;
import model.Node;

import org.junit.BeforeClass;
import org.junit.Test;



public class ArcTest {
	static Arc unArc;


	@BeforeClass
	public static void init()
	{
		Node origin = new Node (10,10,1);
		Node dest = new Node (10,10,2);
		 unArc = new Arc( origin,  dest, 5,10,"testArc");
	}

	@Test
	public void testGetters() 
	{
		int success=0;
		if(unArc.getOrigin().getID()==1) success ++;
		if(unArc.getDest().getID()==2) success ++;
		if(unArc.getDuration()==2) success ++;
		if(success ==3)
			return;
		fail("test Arc Failed");

	}

}


