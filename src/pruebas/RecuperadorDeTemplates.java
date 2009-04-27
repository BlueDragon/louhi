/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas;

import java.util.LinkedList;
import localContainers.TemplateContainer;
import modelo.Template;

/**
 *
 * @author alos
 */
public class RecuperadorDeTemplates {
    public static void main(String[] args) {
        TemplateContainer templateContainer = new TemplateContainer();
        LinkedList<Template> listaDeTemplates = templateContainer.retrieveAllTemplates();
        for(Template t: listaDeTemplates){
            System.out.println(t);
        }

        control.Db4oConnectionManager.closeDB();
    }
}
