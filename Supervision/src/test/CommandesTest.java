package test;

import static org.junit.Assert.*;
import org.junit.Test;

import command.Command;
import command.CommandList;

public class CommandesTest {
	
	/*
	 * Checks that adding command works.
	 */
	@Test
	public void testAdd() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		assertEquals(2, com.size());
		assertEquals(2, com.getIndice());
	}
	
	/*
	 * Checks that undoing command works.
	 */
	@Test
	public void testUndo() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		com.undo();
		assertEquals(2, com.size());
		assertEquals(1, com.getIndice());
	}
	
	/*
	 * Checks that redoing command works.
	 */
	@Test
	public void testRedo() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		com.undo();
		com.redo();
		assertEquals(2, com.size());
		assertEquals(2, com.getIndice());
	}
	
	/*
	 * Checks that undoing does not overflow.
	 */
	@Test
	public void testUndoOverflow() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		com.undo();
		com.undo();
		com.undo();
		assertEquals(2, com.size());
		assertEquals(0, com.getIndice());
	}
	
	/*
	 * Checks that redoing does not overflow.
	 */
	@Test
	public void testRedoOverflow() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		com.undo();
		com.redo();
		com.redo();
		assertEquals(2, com.size());
		assertEquals(2, com.getIndice());
	}
	
	/*
	 * Checks that clear works.
	 */
	@Test
	public void testClear() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		com.clear();
		assertEquals(0, com.size());
		assertEquals(0, com.getIndice());
	}
	
	/*
	 * Checks that adding a command deletes the redoable ones.
	 */
	@Test
	public void testAddDeletesRedoable() {
		CommandList com = new CommandList();
		DummyCommand a = new DummyCommand("a");
		DummyCommand b = new DummyCommand("b");
		com.add(a);
		com.add(b);
		com.undo();
		com.undo();
		com.add(a);
		assertEquals(1, com.size());
		assertEquals(1, com.getIndice());
	}
}
