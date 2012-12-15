package command;

import java.util.ArrayList;

public class Commandes {

	private ArrayList<Commande> commandes = new ArrayList<Commande>();
	private int indice = 0;

	public Commandes() {
	}

	public void add(Commande c) {
		int taille = commandes.size();
		for (int i = indice ; i < taille  ; i++) {
			commandes.remove(indice);
		}
		commandes.add(c);
		indice++;
	}
	
	public void clear() {
		commandes.clear();
		indice = 0;
	}

	public boolean undo() {
		if (indice <= 0) {
			System.out.println("(undo impossible)");
			return false;
		}
		indice--;
		commandes.get(indice).undo();
		return true;
	}

	public boolean redo() {
		if (indice >= commandes.size()) {
			System.out.println("(redo impossible)");
			return false;
		}
		commandes.get(indice).redo();
		indice++;
		return true;
	}
	
	public int size() {
		return commandes.size();
	}
	
	public int getIndice() {
		return indice;
	}
}
