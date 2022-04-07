import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonToXML {

    private static String nameTemp;
    static List<Sale> info = new ArrayList<Sale>();
    static Sale recordSave = new Sale();

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {

        FileReader fileReader = null;
        try {
            fileReader = new FileReader("sales_array.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(fileReader);

        // we call the handle object method to handle the full json object. This
        // implies that the first token in JsonToken.BEGIN_OBJECT, which is
        // always true.
        try {
            //handleObject(reader);
            handleArray(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(info);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        //root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("sales_doc");
        doc.appendChild(rootElement);

        // CREAR XML
        for (int i = 0; i < info.size(); i++) {

            Element saRe = doc.createElement("sale_record");
            rootElement.appendChild(saRe);

            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(String.valueOf(info.get(i).getId())));
            saRe.appendChild(idElement);

            Element firstNameElement = doc.createElement("first_name");
            firstNameElement.appendChild(doc.createTextNode(info.get(i).getFirstName()));
            saRe.appendChild(firstNameElement);

            Element lastNameElement = doc.createElement("last_name");
            lastNameElement.appendChild(doc.createTextNode(info.get(i).getLastName()));
            saRe.appendChild(lastNameElement);

            Element salesElement = doc.createElement("sales");
            salesElement.appendChild(doc.createTextNode(String.valueOf(info.get(i).getSales())));
            saRe.appendChild(salesElement);

            Element stateElement = doc.createElement("state");
            stateElement.appendChild(doc.createTextNode(info.get(i).getState()));
            saRe.appendChild(stateElement);

            Element departmentElement = doc.createElement("department");
            departmentElement.appendChild(doc.createTextNode(info.get(i).getDepartment()));
            saRe.appendChild(departmentElement);

        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("output.xml"));

        transformer.transform(source, result);

    }

    private static void handleObject(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
            else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
                return;
            } else
                handleNonArrayToken(reader, token);
        }

    }

    public static void handleArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                handleObject(reader);
            } else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
            } else
                handleNonArrayToken(reader, token);
        }
    }

    public static void handleNonArrayToken(JsonReader reader, JsonToken token) throws IOException {
        if (token.equals(JsonToken.NAME)) {
            nameTemp = reader.nextName();
        } else if (token.equals(JsonToken.STRING)) {
            switch (nameTemp) {
                case "first_name":
                    recordSave.setFirstName(reader.nextString());
                    break;
                case "last_name":
                    recordSave.setLastName(reader.nextString());
                    break;
                case "state":
                    recordSave.setState(reader.nextString());
                    break;
                case "department":
                    recordSave.setDepartment(reader.nextString());
                    info.add(new Sale(recordSave.getId(), recordSave.getFirstName(), recordSave.getLastName(), recordSave.getSales(), recordSave.getState(), recordSave.getDepartment()));
                    break;
            }

        } else if (token.equals(JsonToken.NUMBER)) {
            switch (nameTemp) {
                case "id":
                    recordSave.setId(reader.nextInt());
                    break;
                case "sales":
                    recordSave.setSales(reader.nextDouble());
                    break;
            }
        } else {
            reader.skipValue();
        }
    }


}