import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class XMLtoJSON extends DefaultHandler {
    private static final String CLASS_NAME = XMLtoJSON.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private SAXParser parser = null;
    private SAXParserFactory spf;

    private double totalSales;

    private boolean inSales;
    private boolean inRecordSales;
    private boolean inId;
    private boolean inFirstName;
    private boolean inLastName;
    private boolean inState;
    private boolean inDepartment;

    private String curreElement;

    private JsonArray array;
    private JsonObject jsonObject;

    public XMLtoJSON() {
        super();
        spf = SAXParserFactory.newInstance();
        // verificar espacios de nombre
        spf.setNamespaceAware(true);
        // validar que el documento este bien formado (well formed)
        spf.setValidating(true);
    }

    private void process(File file) {
        try {
            // obtener un parser para verificar el documento
            parser = spf.newSAXParser();
            LOG.info("Parser object is: " + parser);
        } catch (SAXException | ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nStarting parsing of " + file + "\n");
        try {
            // iniciar analisis del documento
            parser.parse(file, this);
        } catch (IOException | SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        array = new JsonArray();
    }

    @Override
    public void endDocument() throws SAXException {
        String jsonDoc = array.toString();
        System.out.print(jsonDoc);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        switch (localName) {
            case "sale_record":
                inRecordSales = true;
                jsonObject = new JsonObject();
                break;
            case "id":
                inId = true;
                break;
            case "first_name":
                inFirstName = true;
                break;
            case "last_name":
                inLastName = true;
                break;
            case "sales":
                inSales = true;
                break;
            case "state":
                inState = true;
                break;
            case "department":
                inDepartment = true;
                break;
        }
        curreElement = localName;
    }

    @Override
    public void characters(char[] bytes, int start, int length) throws SAXException {

        String data = new String(bytes, start, length);
        switch (curreElement) {
            case "id":
                inId = false;
                jsonObject.addProperty(curreElement, Integer.valueOf(data));
                break;
            case "first_name":
                inFirstName = false;
                jsonObject.addProperty(curreElement, data);
                break;
            case "last_name":
                inLastName = false;
                jsonObject.addProperty(curreElement, data);
                break;
            case "sales":
                inSales = false;
                jsonObject.addProperty(curreElement, Double.valueOf(data));
                break;
            case "state":
                inState = false;
                jsonObject.addProperty(curreElement, data);
                break;
            case "department":
                inDepartment = false;
                jsonObject.addProperty(curreElement, data);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("sale_record")) {
            array.add(jsonObject);
        }
    }


    public static void main(String args[]) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava XmlToJson <filename>");
            return;
        }
        File xmlFile = new File(args[0]);
        XMLtoJSON handler = new XMLtoJSON();
        handler.process(xmlFile);

        try {
            PrintWriter ouput = new PrintWriter("output.json");
            JsonArray array = handler.getArray();
            String jsonDoc = array.toString();
            ouput.print(jsonDoc);
            ouput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonArray getArray() {
        return array;
    }
}