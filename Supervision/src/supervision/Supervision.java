/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supervision;

import java.text.ParseException;
import model.ZoneGeo;
import parsexml.ParseMapXML;
import supervision.Fenetre;
import views.ViewMain;

/**
 *
 * @author Mignot
 */
public class Supervision extends javax.swing.JPanel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
    	// controleur
        Controleur ctrl = new Controleur();
        
        // fenetre et vues
        Fenetre fenetre = new Fenetre();
        Dessin d = fenetre.getDessin();
        ViewMain vm = d.getViewMain();
        
        ctrl.setViewMain(vm);
        vm.setControleur(ctrl);
        fenetre.setControleur(ctrl);
        ctrl.setFenetre(fenetre);
        d.setControleur(ctrl);
        
        fenetre.update();
        fenetre.setVisible(true);
    }
}
