/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas;

import control.Db4oConnectionManager;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import localContainers.CitationContainer;
import modelo.*;

/**
 *
 * @author alos
 */
public class RellenadorDeCitas {
    public static void main(String[] args) {
        CitationContainer refCont = new CitationContainer();

        Citation c1 = new Citation();

        Author a1= new Author("Michael Shumager");
        Author a2 = new Author("Ana Kournikova");
        Author a3 = new Author("Alos");
        LinkedList<Author> listaDeAutores = new LinkedList<Author>();
        listaDeAutores.add(a1);
        listaDeAutores.add(a2);
        listaDeAutores.add(a3);

        Title t1 = new Title ("Driving tennis and computing");
        Pages p1 = new Pages("1590-2044");
        Volume v1 = new Volume("Vol. 18");
        Location l1= new Location("Mexico");
        Publisher pu1 = new Publisher("O'Rally");
        String extra = "[1er edicion]";

        modelo.Date fecha1= new modelo.Date();
        GregorianCalendar gc = new GregorianCalendar();
        fecha1.setDate(gc);


        modelo.MetaData md = new modelo.MetaData();
        md.setIsbn("312kl");
        md.setIssn("as890");
        md.setKeywords("tennis, cars, computers");
        md.setLanguage(Language.ENGLISH);
        md.setMagazineTitle("The importance of tennis cars and computers");
        md.setMaker("Acrobat Pro");
        md.setTheme("Madness");



        c1.setAuthors(listaDeAutores);
        c1.setDate(fecha1);
        c1.setTitle(t1);
        c1.setPages(p1);
        c1.setVolume(v1);
        c1.setLocation(l1);
        c1.setPublisher(pu1);
        c1.setExtra(extra);
        c1.setMetaData(md);
        c1.setType(Type.MAGAZINEARTICLE);

        refCont.saveItems(c1);

//=============================================
        Citation c2 = new Citation();

         a1= new Author("Octavio Paz");
         a2 = new Author("William Shakespare");
         a3 = new Author("Brian Weiss");
         listaDeAutores = new LinkedList<Author>();
         listaDeAutores.add(a1);
         listaDeAutores.add(a2);
         listaDeAutores.add(a3);

         t1 = new Title ("The wheels on the bus go round and round");
         p1 = new Pages("15-20");
         v1 = new Volume("Vol. 2");
         l1= new Location("Paris");
         pu1 = new Publisher("Omega");
         extra = "[2er edicion]";

        fecha1= new modelo.Date();
        gc = new GregorianCalendar();
        fecha1.setDate(gc);


        md = new modelo.MetaData();
        md.setIsbn("ADS3432");
        md.setIssn("23324D");
        md.setKeywords("busses, wheels, computers");
        md.setLanguage(Language.ENGLISH);
        md.setMagazineTitle("Dinosaurios");
        md.setMaker("Acrobat Pro 3");
        md.setTheme("Cosas locas.");



        c2.setAuthors(listaDeAutores);
        c2.setDate(fecha1);
        c2.setTitle(t1);
        c2.setPages(p1);
        c2.setVolume(v1);
        c2.setLocation(l1);
        c2.setPublisher(pu1);
        c2.setExtra(extra);
        c2.setMetaData(md);
        c2.setType(Type.MAGAZINEARTICLE);

        refCont.saveItems(c2);
       //==========================
        Db4oConnectionManager.closeDB();

    }
}
