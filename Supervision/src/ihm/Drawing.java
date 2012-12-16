package ihm;


import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import supervision.Controler;
import views.ViewMain;
import views.ViewZoneGeo;

import model.Node;
import model.ZoneGeo;

public class Drawing extends JPanel implements MouseListener, MouseMotionListener {
	
	protected ViewMain viewMain;
	protected Controler controler;
    protected Window window;
	
    /**
     * Retourne un JPanel amélioré réagissant aux clics.
     *
     * @return      un JPanel amélioré.
     */
	public Drawing() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		viewMain = new ViewMain(this);
	}
	
	public ViewMain getViewMain() {
		return viewMain;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		controler.click(e.getX(), e.getY(), e.getButton());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		controler.highlight(e.getX(), e.getY());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		viewMain.paint(g);
	}

	public void setControler(Controler c) {
		controler = c;
	}

    public void setWindow(Window w) {
        this.window = w;
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
