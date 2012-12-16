package test;

import command.Command;

public class DummyCommand extends Command {
	
	private String s;

	public DummyCommand(String arg) {
		s = arg;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		System.out.println("Undo : " + s);
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		System.out.println("Redo : " + s);
	}

}
