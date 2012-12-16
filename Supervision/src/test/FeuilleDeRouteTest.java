package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.ArrayList;

import model.Delivery;
import model.Node;
import model.RoadMap;
import model.Schedule;
import model.StateRoadMap;
import model.ZoneGeo;

import org.junit.BeforeClass;
import org.junit.Test;

import tsp.GraphDelivery;
import Exception.GraphException;

public class FeuilleDeRouteTest
{
	
	static GraphDelivery graphLivraisons;
	static RoadMap feuilleDeRoute;
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
		
		feuilleDeRoute=new RoadMap(listeSchedules, zoneGeo);
		
	}
	
	
	@Test
	public void test()
	{	
		
		feuilleDeRoute.addNode(zoneGeo.getNode(2), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(6), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(1), schedule2);
		feuilleDeRoute.addNode(zoneGeo.getNode(3), schedule2);
		
		assertEquals(feuilleDeRoute.getEtat(), StateRoadMap.INIT);
		
		try {
			feuilleDeRoute.computeWithTSP();
		} catch (GraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		assertEquals(feuilleDeRoute.getEtat(), StateRoadMap.OPTIM);
		
		try 
		{
			feuilleDeRoute.addNode(zoneGeo.getNode(2), schedule1);
			fail("L'exception sur addNode n'est pas déclenchée, alors que feuilleDeRoute est déjà initialisée");
		} catch (Exception e)
		{
			//OK
		}
		Delivery lastOf1 = feuilleDeRoute.getSchedules().get(0).getDeliveries().getLast();
		Delivery firstOf1 = feuilleDeRoute.getSchedules().get(0).getDeliveries().getFirst();
		
	//insertion 4 entre 2 et 6
		feuilleDeRoute.insertNodeBefore(zoneGeo.getNode(4), lastOf1);
		
		Delivery inserted = feuilleDeRoute.getSchedules().get(0).getDeliveries().get(1);
		assertEquals(inserted.getDest(), zoneGeo.getNode(4));
		
		//verif maj chemin 4->6
		assertEquals(lastOf1.getPathToDest().getNoeudDepart(), zoneGeo.getNode(4));
		assertEquals(lastOf1.getPathToDest().getNoeudArrivee(), lastOf1.getDest());
		
		//verif maj chemin 2->4
		assertEquals(inserted.getPathToDest().getNoeudDepart(), firstOf1.getDest());
		assertEquals(inserted.getPathToDest().getNoeudArrivee(), inserted.getDest());
		
	//insertion de 1 entre 2 et l'entrepot
		feuilleDeRoute.insertNodeBefore(zoneGeo.getNode(1), firstOf1);
		
		lastOf1 = feuilleDeRoute.getSchedules().get(0).getDeliveries().getLast();
		firstOf1 = feuilleDeRoute.getSchedules().get(0).getDeliveries().getFirst();
		
		//verif 1 bien 1er Deliv du 1er schedule
		assertEquals(firstOf1.getDest(), zoneGeo.getNode(1));
		//pointeur vers schedule ok?
		assertEquals(firstOf1.getSchedule(), feuilleDeRoute.getSchedules().get(0));
		
		//verif chemin entrepot->1
		assertEquals(firstOf1.getPathToDest().getNoeudDepart(), zoneGeo.getWarehouse());
		assertEquals(firstOf1.getPathToDest().getNoeudArrivee(), firstOf1.getDest());
		
		//schedule 1 contient 4 deliveries
		assertEquals(feuilleDeRoute.getSchedules().get(0).getDeliveries().size(), 4);
		
		//schedule 1 contient 2 deliveries
		assertEquals(feuilleDeRoute.getSchedules().get(1).getDeliveries().size(), 2);
		
		assertEquals(feuilleDeRoute.getEtat(), StateRoadMap.MODIF);
		try {
			feuilleDeRoute.computeWithTSP();
		} catch (GraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(feuilleDeRoute.getEtat(), StateRoadMap.OPTIM);
		

	}

}
