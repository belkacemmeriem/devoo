package test;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import tsp.*;
import model.*;

public class GraphLivraisonsTest {
	GraphLivraisons graphLivraisons;
	static FeuilleDeRoute feuilleDeRoute;
	
	@BeforeClass
	public static void init(){
		feuilleDeRoute=new FeuilleDeRoute();
		Node node;
		for(int i=0;i<6;i++)
		{
			node=new Node(0,0,i);
			feuilleDeRoute.getZoneGeo().addNode(node);
		}
		feuilleDeRoute.getZoneGeo().addArc(0, 1, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(0, 2, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(0, 3, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(1, 2, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(1, 3, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(1, 4, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(1, 5, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(2, 1, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(2, 3, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(2, 4, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(2, 5, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(3, 1, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(3, 2, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(3, 4, 1, 30, "temp");
		feuilleDeRoute.getZoneGeo().addArc(3, 5, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(4, 5, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(4, 0, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(5, 4, 1, 10, "temp");
		feuilleDeRoute.getZoneGeo().addArc(5, 4, 1, 30, "temp");
		
		Schedule schedule=new Schedule(10,12);
		Delivery delivery=new Delivery(feuilleDeRoute.getZoneGeo().getNode(1),schedule);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(feuilleDeRoute.getZoneGeo().getNode(2), schedule);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(feuilleDeRoute.getZoneGeo().getNode(3), schedule);
		schedule.getDeliveries().add(delivery);
		feuilleDeRoute.getTimeZones().add(schedule);
		
		schedule=new Schedule(12,14);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(feuilleDeRoute.getZoneGeo().getNode(4), schedule);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(feuilleDeRoute.getZoneGeo().getNode(5), schedule);
		schedule.getDeliveries().add(delivery);
		feuilleDeRoute.getTimeZones().add(schedule);
		
		Entrepot entrepot=new Entrepot(feuilleDeRoute.getZoneGeo().getNode(0));
	}
	
	@Test
	public void testCreateGraph(){
		
	}
}
