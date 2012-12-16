package command;

import java.util.ArrayList;

/**
 * Permet de manipuler un historique de commandes annulables / répétables.
 */
public class CommandList {

	private ArrayList<Command> commandes = new ArrayList<Command>();
	private int index = 0;

	/**
	 * Ajoute une commande à la liste.
	 * Toutes les commandes qui pouvaient être répétées sont détruites.
	 *
	 * @param  c la commande à ajouter.
	 */
	public void add(Command c) {
		int taille = commandes.size();
		for (int i = index ; i < taille  ; i++) {
			commandes.remove(index);
		}
		commandes.add(c);
		index++;
	}
	
	/**
	 * Supprime toutes les commandes de l'historique.
	 */
	public void clear() {
		commandes.clear();
		index = 0;
	}

	/**
	 * Annule une commande.
	 * 
	 * @return vrai si l'annulation a été possible
	 */
	public boolean undo() {
		if (index <= 0) {
			System.out.println("(undo impossible)");
			return false;
		}
		index--;
		commandes.get(index).undo();
		return true;
	}

	/**
	 * Répète une commande.
	 * 
	 * @return vrai si la répétition a été possible
	 */
	public boolean redo() {
		if (index >= commandes.size()) {
			System.out.println("(redo impossible)");
			return false;
		}
		commandes.get(index).redo();
		index++;
		return true;
	}
	
	/**
	 * Renvoie la taille de l'historique.
	 * 
	 * @return la taille de l'historique
	 */
	public int size() {
		return commandes.size();
	}
	
	/**
	 * Renvoie la position actuelle dans l'historique.
	 * 
	 * @return la position actuelle dans l'historique
	 */
	public int getIndice() {
		return index;
	}
}
