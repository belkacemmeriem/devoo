/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JOptionPane;

/**
 *
 * @author Sherlock
 */
public class ViewError {
    
    public ViewError (String errorMessage, Boolean fatal)
    {
        String message = errorMessage;
        if(fatal) message=message+"\n\n Appuyez sur OK pour fermer le programme";
        JOptionPane.showMessageDialog(null,message,
                "Une erreur est survenue",JOptionPane.ERROR_MESSAGE);
        if(fatal)
        {
            System.exit(0);
        }
    }
}
