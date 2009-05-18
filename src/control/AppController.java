/*
 * This file is part of Louhi.

    Louhi is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License.

    Louhi is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Louhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package control;

import exceptions.DataBaseNotFoundException;
import cloudContainers.TitleContainer;
import org.pdfbox.exceptions.CryptographyException;
import org.pdfbox.exceptions.InvalidPasswordException;

import convertidores.*;
import java.io.File;
import modelo.*;
import interfaz.*;
import java.util.LinkedList;
import java.util.StringTokenizer;
import cloudContainers.AuthorContainer;
import localContainers.Container;
import cloudContainers.LocationContainer;
import cloudContainers.PublisherContainer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.sql.*;
import conexionOracle.Connect;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import cloudContainers.MetaDataContainer;
import modelo.MetaData;

public class AppController {
    private Interfaz gui;
    public EntidadPDF elPDF;
    private DescriptorManager descManager = new DescriptorManager();
    String referencias ="";
    AuthorContainer authorContainer = new AuthorContainer();
    PublisherContainer publisherContainer = new PublisherContainer();
    LocationContainer locationContainer = new LocationContainer();
    TitleContainer titleContainer = new TitleContainer();
    private UserNameWindow userWindow;
    MetaData md=new MetaData();

    public AppController() {
        this.gui= new Interfaz(this);        
        this.gui.setVisible(true);
        this.gui.setEnabled(false);
    }


    /**
	 * Convierte el arcvhivo elegido a PDF dejandolo en el mismo directorio
	 * @param archivo
	 * @throws InvalidPasswordException
	 * @throws CryptographyException
	 */
	public void  convertirPDFToXML(String archivo)throws NoSePudoException, CryptographyException, InvalidPasswordException{
		PDFToModelo convertidorAModelo = new PDFToModelo(this);
		ModeloToXML convertidorAXML = new ModeloToXML();
		EntidadPDF unPDF = convertidorAModelo.convertirAModelo(archivo);
		convertidorAXML.convertirAXML(unPDF);
	}

    /**
     * Convierte el archivo elegido a un PDF
     * @param archivo
     * @return
     * @throws control.NoSePudoException
     * @throws org.pdfbox.exceptions.CryptographyException
     * @throws org.pdfbox.exceptions.InvalidPasswordException
     */
    public EntidadPDF convertirPDFAModelo(File archivo)throws NoSePudoException, CryptographyException, InvalidPasswordException{
        PDFToModelo convertidorAModelo = new PDFToModelo(this);
		elPDF = convertidorAModelo.convertirAModelo(archivo);
        if(elPDF==null){
               return null;
        }
        this.gui.setStatus("Termine convirtiendo: " + elPDF.getTitulo());
        return elPDF;
    }

     public EntidadPDF convertirPDFAModelo(File archivo, String pwd)throws NoSePudoException, CryptographyException, InvalidPasswordException{
        PDFToModelo convertidorAModelo = new PDFToModelo(this);
		elPDF = convertidorAModelo.convertirAModelo(archivo, pwd);
        if(elPDF==null){
            return null;
        }
         this.gui.setStatus("Termine convirtiendo: " + elPDF.getTitulo());
        return elPDF;
    }


     public void setStatus(String aString){
        this.gui.setStatus(aString);
     }


    public LinkedList<TemporalReference> findCitations(LinkedList<String> listaDeCitas, String probableType) {
        return descManager.getCitations(listaDeCitas, probableType);
    }

    /**
     * Gets the DescriptorManager to extract the needed information
     * @param content
     * @return
     */
    public String findReferences(String content) throws DataBaseNotFoundException {
       this.referencias = descManager.getReferences(content);
       return this.referencias;
    }

    

    /**
     * Gets a EntidadPDF object
     * @return
     */
    public EntidadPDF getThePDF() {
        return elPDF;
    }
	/**
	 * Itera sobre todos los archivos .PDF que estan en el directorio y los convierte dejandolos en el mismo directorio
	 * @param directorio
	 */
	public void batchConvertirPDFToXML(String directorio) {

	}

    /**
     * guarda un autor
     * @param value
     */
    public void saveAuthor(String value){
        Author atr = new Author();
        atr.setName(value);
        authorContainer.saveAuthor(atr);
    }

    /**
     * guarda un editorial
     * @param value
     */
    public void savePublisher(String value) {
        Publisher pbl = new Publisher();
        pbl.setName(value);
        publisherContainer.savePublisher(pbl);
    }

    /**
     * guarda un titulo
     * @param value
     */
    public void saveTitle(String value) {
        Title tit = new Title();
        tit.setATitle(value);
        titleContainer.saveTitle(tit);
    }

    /**
     * guarda un lugar
     * @param value
     */
    public void saveLocation(String value) {
        Location loc = new Location();
        loc.setNameOfLocation(value);
        locationContainer.saveLocation(loc);
    }

    /**
     * recupera todos los autores
     * @return
     */
    public LinkedList<Author> retrieveAllAuthors(){
        LinkedList<Author> lista = authorContainer.retrieveAllAuthors();
         return lista;
    }

    /**
     * recupera todas las editoriales de la base
     * @return
     */
    public LinkedList<Publisher> retrieveAllPublishers() {
        LinkedList<Publisher> lista = publisherContainer.retrieveAllPublishers();
        return lista;
    }

    /**
     * recupera todos los lugares de la base
     * @return
     */
    public LinkedList<Location> retrieveAllLocations() {
        LinkedList<Location> lista = locationContainer.retrieveAllLocations();
        return lista;
    }

    /**
     * recupera todos los titulos de la base
     * @return
     */
    public LinkedList<Title> retrieveAllTitles() {
        new Container();
        LinkedList<Title> lista = titleContainer.retrieveAllTitles();
        return lista;
    }

    /**
     * borra un editorial
     * @param valueSelected
     */
    public void deletePublisher(String valueSelected) {
        Publisher pbl = new Publisher();
        pbl.name = valueSelected;
        publisherContainer.deletePublisher(pbl);
    }

    /**
     * borra un autor 
     * @param valueSelected
     */
    public void deleteAuthor(String valueSelected) {
        Author atr = new Author();
        atr.name = valueSelected;
        authorContainer.deleteAuthor(atr);
    }

    /**
     * borra un titulo
     * @param valueSelected
     */
    public void deleteTitle(String valueSelected) {
        Title tit = new Title();
        tit.title = valueSelected;
        titleContainer.deleteTitle(tit);
    }

    /**
     *boora un lugar
     * @param valueSelected
     */
     public void deleteLocation(String valueSelected) {
        Location loc = new Location();
        loc.nameOfLocation = valueSelected;
        locationContainer.deleteLocation(loc);
    }
     
    /**
     *valida la direccion ip
     * @param hostDireccion
     * @return
     */
     public boolean validaIP(String hostDireccion) {
        StringTokenizer st = new StringTokenizer( hostDireccion, "." );

         //verifica que sean 4 elementos
         if ( st.countTokens() != 4 ) {
             return false;
        }

         //verifica los 4 elementos de la ip, de i a 3 digitos y que esten entre 0 y 255
         while ( st.hasMoreTokens() ) {
             String nro = st.nextToken();
             if ( ( nro.length() > 3 ) || ( nro.length() < 1 ) ) {
                 return false;
             }
             int nroInt = 0;
             try {
                 nroInt = Integer.parseInt( nro );
             }
             catch ( NumberFormatException s ) {
                 return false;
             }
             if ( ( nroInt < 0 ) || ( nroInt > 255 ) ) {
                 return false;
             }
         }
         return true;
    }

     /**
      *compara el pais de la cita con el pais de la revista origen
      * para determinar si la cita es nacional o no nacional
      * @param nameMagazine
      * @param location
      * @return
      */
    public static int isNacional(String nameMagazine, String location) {
        int resp=0;
        Connection con= Connect.getConnection();
        ResultSet rset=null;
        int clavePais=0;
        String nombrePais="";
        try{
            Statement stmt = con.createStatement();
            //obtenemos la clave del pais segun el nombre de revista
            rset = stmt.executeQuery("select cveentpai from tblentrev where nomentrev='"+nameMagazine+"'");
            if(rset.next())
                clavePais = rset.getInt(1);
            //ahora utilizamos la clave para obtener el nombre del pais
            rset = stmt.executeQuery("select nomentapi from tblentpai where cveentpai="+clavePais+"");
            if(rset.next())
                nombrePais=rset.getString(1);
            stmt.close();
            
        }catch(Exception e){
            if(!nombrePais.equals(""))
                resp=2;
        }
        //System.out.println("clavePais "+clavePais);
        //System.out.println("nombrePais "+nombrePais);
        //cerramos la conexion a la base de datos
        Connect.CloseConnection(con);
        //ahora comparamos el pais de la revista con el pais de la cita
        if(!nombrePais.equals("")){
            //en caso de que sean iguales regresa 1
            if(nombrePais.compareToIgnoreCase(location)==0)
                resp=1;
            //sino son iguales regresa 2
            else
                resp=2;
        }        
        return resp;
    }

    /**
     * Compares 2 Strings (magazine names) to know if they are the same
     * Returns 0 for not the same
     * Returns 1 for the same magazine
     * Returns 2 for undetermined
     * @param first
     * @param second
     * @return
     */
    public int autocitationCheck(String first, String second){
        if (!(first.length()>3 && second.length()>3) ){
            return 0;
        }
        if (first.compareToIgnoreCase(second)==0 ){
            return 1;
        }else{
            if(first.startsWith(second.substring(0,( (int) (second.length()/3)))) ||
                 first.endsWith(second.substring( second.length()- (int) (second.length()/3) ) )||
                 first.contains(second.substring( ( (int) (second.length()/3)), second.length()-(int) (second.length()/3) ) )
                 ) {
                return 2;
            }
        }
        return 0;
    }

/**
 * Saves the citation list that it recives
 * @param listaCitas
 */
    public void saveCitations(LinkedList<TemporalReference> listaCitas){
        cloudContainers.CitationContainer contenedorDeCitas = new cloudContainers.CitationContainer();
               
        for(TemporalReference cit : listaCitas){
            Citation meh = new Citation(cit);
            contenedorDeCitas.saveItems(meh);
        }
    }

    public void saveMetadata(){
        MetaDataContainer contenedorMD= new MetaDataContainer();
        contenedorMD.saveMD(md);
    }

/**
 * regresa un string con la ip local
 * @return
 */
    public String getIPLocal(){
        String ip = "";
        try {
            for (Enumeration ni = NetworkInterface.getNetworkInterfaces(); ni.hasMoreElements();) {
                NetworkInterface theNI = (NetworkInterface) ni.nextElement();
                for (Enumeration ia = theNI.getInetAddresses(); ia.hasMoreElements();) {
                    InetAddress anAddress = (InetAddress) ia.nextElement();
                    if (!anAddress.getHostAddress().equals("127.0.0.1") && anAddress.getHostAddress().length() < 16) {
                        ip = anAddress.getHostAddress();
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("error al obtener la ip");
        }
        return ip;
    }

    /**
     * regresa un gregorianCalendar con la fecha y hora local
     * @return
     */
    public GregorianCalendar getCommitDate(){
        GregorianCalendar cal = new GregorianCalendar();
        return cal;
    }

	public static void main(String arg[]){
		AppController control= new AppController();
	}

    @Override
    protected void finalize() throws Throwable {
        Db4oConnectionManager.closeDB();
        Db4oLocalConnectionManager.closeDB();
        super.finalize();
    }

    /**
     * Converts the temporal citations to an XML file
     * @param tempRefList
     */
    public void convertToXML(LinkedList<TemporalReference> tempRefList) throws IOException{
        XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new convertidores.ReferenceConverter());
        xStream.alias("referencia", Citation.class);
        String xml = "<"+tempRefList.getFirst().getArticleID()+">\n";
        for(TemporalReference tr : tempRefList){
            Citation meh = new Citation(tr);
            xml =  xml + xStream.toXML(meh) + "\n";
            
        }
        xml = xml +"</"+tempRefList.getFirst().getArticleID()+">";
        this.writeXMLToFile(tempRefList.getFirst().getArticleID()+".xml", xml);
    };

     private void writeXMLToFile(String filename, String xml) throws IOException {

        System.out.println("Escribiendo el archivo XML...");

        FileWriter fstream = new FileWriter(filename);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(xml);
        //Close the output stream
        out.close();


        System.out.println("Se termino de escribir el XML...el raton esta feliz!");
    }

}
