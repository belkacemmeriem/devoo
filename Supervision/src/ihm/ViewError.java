/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

import javax.swing.JOptionPane;

/**
 *
 * @author Sherlock
 */
public class ViewError {
    
    public ViewError (String errorMessage)
    {
        String message = errorMessage;
        JOptionPane.showMessageDialog(null,message,
                "Une erreur est survenue",JOptionPane.ERROR_MESSAGE);
    }
}
