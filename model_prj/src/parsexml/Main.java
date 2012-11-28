/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsexml;

import model.*;
import parsexml.*;

/**
 *
 * @author Sherlock
 */
public class Main {
    
    static ZoneGeo zonegeo =  new ZoneGeo();
    static ParseXML parseXml;
    static String filePath = "C:/Users/Sherlock/Desktop/GitHub/devoo/bordel/servifhome/plan25.xml";
    
    public static void main(String[] args) {
        parseXml = new ParseXML(filePath, zonegeo);
        parseXml.afficheALL();
    }
}
