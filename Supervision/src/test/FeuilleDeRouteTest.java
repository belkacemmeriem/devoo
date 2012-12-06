package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import junit.framework.Assert;

import model.EtatFDR;
import model.FeuilleDeRoute;
import model.Node;
import model.Schedule;
import model.ZoneGeo;

import org.junit.BeforeClass;
import org.junit.Test;

import tsp.GraphLivraisons;

public class FeuilleDeRouteTest
{
	
	static GraphLivraisons graphLivraisons;
	static FeuilleDeRoute feuilleDeRoute;
	static ZoneGeo zoneGeo;
	static Schedule schedule1, schedule2;


	@BeforeClass
	public static void init()
	{
		
		//init zoneGeo
		zoneGeo=new ZoneGeo();
		
		Node node;
		for(int i=0;i<=8;i++)
		{
			node=new Node(0,0,i);
			zoneGeo.addNode(node);
		}
		
		/*	croquis de la zoneGeo
		 
		               8 <------ 7 <-----> 6
			           ^                   ^
			           |                   |
			           v                   v
			           4 <-----> 3 ------> 5
			           |         ^
			           v         |
			0 <------> 1 ------> 2
		
		
		*/
		zoneGeo.addArc(0, 1, 1, 10, "01");
		zoneGeo.addArc(1, 0, 1, 10, "10");
		
		
		zoneGeo.addArc(1, 2, 1, 10, "12");
		zoneGeo.addArc(2, 3, 1, 10, "23");
		zoneGeo.addArc(3, 4, 1, 10, "34");
		zoneGeo.addArc(4, 3, 1, 10, "43");
		zoneGeo.addArc(4, 1, 1, 10, "41");
		
		
		zoneGeo.addArc(3, 5, 1, 10, "35");
		zoneGeo.addArc(5, 6, 1, 10, "56");
		zoneGeo.addArc(6, 7, 1, 10, "67");
		zoneGeo.addArc(7, 6, 1, 10, "76");
		zoneGeo.addArc(7, 8, 1, 10, "78");
		zoneGeo.addArc(8, 4, 1, 10, "84");
		zoneGeo.addArc(4, 8, 1, 10, "48");

		
		//ajout de l'entrepot
		zoneGeo.setWarehouse(0);
		
		
		//init feuilleDeRoute
		ArrayList<Schedule> listeSchedules=new ArrayList<Schedule>();
		schedule1=new Schedule(600,800,Color.GREEN);
		listeSchedules.add(schedule1);
		schedule2=new Schedule(900,1200,Color.RED);
		listeSchedules.add(schedule2);		
		
		feuilleDeRoute=new FeuilleDeRoute(listeSchedules, zoneGeo);
		
	}
	
	
	@Test
	public void test()
	{	
		
		feuilleDeRoute.addNode(zoneGeo.getNode(2), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(6), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(1), schedule2);
		feuilleDeRoute.addNode(zoneGeo.getNode(3), schedule2);
		
		assertEquals(feuilleDeRoute.getEtat(), EtatFDR.INIT);
		
		feuilleDeRoute.computeWithTSP();
		
		assertEquals(feuilleDeRoute.getEtat(), EtatFDR.OPTIM);
		
		try 
		{
			feuilleDeRoute.addNode(zoneGeo.getNode(2), schedule1);
			fail("L'exception sur addNode n'est pas déclenchée, alors que feuilleDeRoute est déjà initialisée");
		} catch (Exception e)
		{
			// OK
		}
		
	}

}
