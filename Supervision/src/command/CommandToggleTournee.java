package command;

import supervision.Controleur;

/**
 * Renvoie une commande pour annuler ou répéter le switch entre les modes remplissage et modification.
 *
 * @param  ctrl  le controleur
 */
public class CommandToggleTournee extends Command {

	private Controleur ctrl;
	
	public CommandToggleTournee(Controleur ctrl) {
		this.ctrl = ctrl;
	}
	
	private void toggleDo() {
		ctrl.toggleGenererTournee(false);
	}

	@Override
	public void undo() {
		toggleDo();
	}

	@Override
	public void redo() {
		toggleDo();
	}

}
