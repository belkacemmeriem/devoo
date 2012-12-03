package test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Chemin;
import model.Node;

import org.junit.BeforeClass;
import org.junit.Test;



public class CheminTest {
	static Chemin path;
	


	@BeforeClass
	public static void init()
	{
		Node n1 = new Node(0,0,0);
		Node n2 = new Node(0,1,1);
		Node n3 = new Node(1,0,2);
		Node n4 = new Node(1,1,3);
		Node n5 = new Node(20,20,4);
						
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
		nodes.add(n4);
		nodes.add(n5);
		path = new Chemin(nodes, 100);
	}

	@Test
	public void testGetters() 
	{
		int sucess=0;
		if(path.getNoeudDepart().getID()==0) sucess++;
		if(path.getNoeudArrivee().getID()==4) sucess++;
		if(path.getTrajectory().get(0).getID()==0) sucess++;
		if(path.getTrajectory().get(1).getID()==1) sucess++;
		if(path.getTrajectory().get(2).getID()==2) sucess++;
		if(path.getTrajectory().get(3).getID()==3) sucess++;
		if(path.getTrajectory().get(4).getID()==4) sucess++;
		if(path.getDuration()==100) sucess++;


		//Toutes les verifications se sont bien passees
		if(sucess == 8)
			return;
		fail("Test Chemin failed");

	}

}