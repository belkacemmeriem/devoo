package supervision;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import views.ViewMain;
import views.ViewZoneGeo;

import model.Node;
import model.ZoneGeo;

public class Dessin extends JPanel implements MouseListener, MouseMotionListener {
	
	protected ViewMain viewmain;
	protected Controleur controleur;
        protected Fenetre fenetre;
	
	public Dessin() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		viewmain = new ViewMain(this);
	}
	
	public ViewMain getViewMain() {
		return viewmain;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int id = controleur.click(e.getX(), e.getY(), e.getButton());
		fenetre.nodeClicked(id);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		controleur.highlight(e.getX(), e.getY());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		viewmain.paint(g);
	}

	public void setControleur(Controleur ctrl) {
		controleur = ctrl;
	}

    public void setFenetre(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
