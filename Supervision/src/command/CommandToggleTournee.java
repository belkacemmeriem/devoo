package command;

import supervision.Controler;

/**
 * Renvoie une commande pour annuler ou répéter le switch entre les modes remplissage et modification.
 *
 * @param  ctrl  le controleur
 */
public class CommandToggleTournee extends Command {

	private Controler ctrl;
	
	public CommandToggleTournee(Controler ctrl) {
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
