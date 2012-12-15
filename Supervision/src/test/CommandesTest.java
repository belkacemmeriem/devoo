package test;

import static org.junit.Assert.*;
import org.junit.Test;

import command.Commande;
import command.Commandes;

public class CommandesTest {
	
	/*
	 * Checks that adding command works.
	 */
	@Test
	public void testAdd() {
		Commandes com = new Commandes();
		/*Commande a = new Commande("a");
		Commande b = new Commande("b");
		com.add(a);
		com.add(b);*/
		assertEquals(2, com.size());
		assertEquals(2, com.getIndice());
	}
}
