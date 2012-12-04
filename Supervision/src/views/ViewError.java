/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JOptionPane;
import supervision.Fenetre;

/**
 *
 * @author Sherlock
 */
public class ViewError {
    
    public ViewError (Fenetre fenetre, String errorMessage, Boolean fatal)
    {
        JOptionPane.showMessageDialog(null,errorMessage,
                "Une erreur est survenue",JOptionPane.ERROR_MESSAGE);
        if(fatal)
        {
            System.exit(0);
        }
    }
}
