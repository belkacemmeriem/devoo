package test;

import static org.junit.Assert.*;

import model.Arc;
import model.Node;

import org.junit.Test;

public class ArcTest {

	@Test
	public void test() 
	{
		int success=0;
		Node origin = new Node (10,10,1);
		Node dest = new Node (10,10,2);
		Arc unArc = new Arc( origin,  dest, 5,10,"testArc");
		if(unArc.getOrigin().getID()==1) success ++;
		if(unArc.getDest().getID()==2) success ++;
		if(unArc.getDuration()==2) success ++;
		if(success ==3)
			return;
		fail("Not yet implemented");
	}

}
