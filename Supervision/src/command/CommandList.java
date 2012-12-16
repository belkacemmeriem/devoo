package command;

import java.util.ArrayList;

public class CommandList {

	private ArrayList<Command> commandes = new ArrayList<Command>();
	private int index = 0;

	public CommandList() {
	}

	public void add(Command c) {
		int taille = commandes.size();
		for (int i = index ; i < taille  ; i++) {
			commandes.remove(index);
		}
		commandes.add(c);
		index++;
	}
	
	public void clear() {
		commandes.clear();
		index = 0;
	}

	public boolean undo() {
		if (index <= 0) {
			System.out.println("(undo impossible)");
			return false;
		}
		index--;
		commandes.get(index).undo();
		return true;
	}

	public boolean redo() {
		if (index >= commandes.size()) {
			System.out.println("(redo impossible)");
			return false;
		}
		commandes.get(index).redo();
		index++;
		return true;
	}
	
	public int size() {
		return commandes.size();
	}
	
	public int getIndice() {
		return index;
	}
}
