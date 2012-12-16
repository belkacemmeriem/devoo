package test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Arc;
import model.Path;
import model.Node;
import model.ZoneGeo;

import org.junit.BeforeClass;
import org.junit.Test;

import dijkstra.Dijkstra;


public class ZoneGeoTest {
	static ZoneGeo uneZone;



	@BeforeClass
	public static void init()
	{
		//Creation d'un graphe 
				Node n1 = new Node(0,0,0);
				Node n2 = new Node(0,1,1);
				Node n3 = new Node(1,0,2);
				Node n4 = new Node(1,1,3);
				Node n5 = new Node(20,20,4);
				
				uneZone= new ZoneGeo();
				uneZone.setWarehouse(0);
				uneZone.addNode(n1);
				uneZone.addNode(n2);
				uneZone.addNode(n3);
				uneZone.addNode(n4);
				uneZone.addNode(n5);
				
				uneZone.addArc(0, 1, 10, 10, "n1");
				uneZone.addArc(0, 2, 10, 10, "n2");
				uneZone.addArc(1, 3, 10, 10, "n3");
				uneZone.addArc(2, 4, 10, 10, "n4");
				uneZone.addArc(4, 0, 10, 10, "n5");
				
	}

	@Test
	public void testGetter() 
	{
		int sucess=0;

		if(uneZone.getNodes().size()==5) sucess ++;
		if(uneZone.getArcs().size()==5) sucess ++;
		if(uneZone.getWarehouse().getID()==0) sucess ++;
		if(uneZone.getNode(0).getID()==0) sucess ++;
		
		if(uneZone.getXmin()==0) sucess ++;
		if(uneZone.getYmin()==0) sucess ++;
		if(uneZone.getXmax()==20) sucess ++;
		if(uneZone.getYmax()==20) sucess ++;
		if(uneZone.getWidth()==20) sucess ++;
		if(uneZone.getHeight()==20) sucess ++;


		//Toutes les verifications se sont bien passees
		if(sucess == 10)
			return;
		fail("Fail test ZoneGeo");

	}

}
