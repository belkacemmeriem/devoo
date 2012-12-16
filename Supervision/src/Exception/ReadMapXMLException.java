/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 * <b>ParseDelivTimeXML est la classe représentant une exception liée à la lecture du fichier XML.</b>
 * 
 * @see Exception
 * 
 * @author H4404
 */
public class ReadMapXMLException extends Exception {
        /** 
	* Crée une nouvelle instance de ReadMapXMLException 
	*/  
	public ReadMapXMLException() {}  
	/** 
	* Crée une nouvelle instance de ReadMapXMLException 
	* @param message Le message détaillant exception 
	*/  
	public ReadMapXMLException(String message) {  
		super(message); 
	}  
	/** 
	* Crée une nouvelle instance de ReadMapXMLException 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public ReadMapXMLException(Throwable cause) {  
		super(cause); 
	}  
	/** 
	* Crée une nouvelle instance de ReadMapXMLException 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public ReadMapXMLException(String message, Throwable cause) {  
		super(message, cause); 
	} 
}
