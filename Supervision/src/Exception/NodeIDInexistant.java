/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Sherlock
 */
public class NodeIDInexistant extends Exception {
        /** 
	* Crée une nouvelle instance de NodeIDInexistant 
	*/  
	public NodeIDInexistant() {}  
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	* @param message Le message détaillant exception 
	*/  
	public NodeIDInexistant(String message) {  
		super(message); 
	}  
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public NodeIDInexistant(Throwable cause) {  
		super(cause); 
	}  
	/** 
	* Crée une nouvelle instance de NodeIDInexistant 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public NodeIDInexistant(String message, Throwable cause) {  
		super(message, cause); 
	} 
}
