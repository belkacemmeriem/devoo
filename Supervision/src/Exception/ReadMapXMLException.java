/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Sherlock
 */
public class ReadMapXMLException extends Exception {
	private static final long serialVersionUID = 1L;
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	*/  
	public ReadMapXMLException() {}  
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	* @param message Le message détaillant exception 
	*/  
	public ReadMapXMLException(String message) {  
		super(message); 
	}  
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public ReadMapXMLException(Throwable cause) {  
		super(cause); 
	}  
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public ReadMapXMLException(String message, Throwable cause) {  
		super(message, cause); 
	} 
}
