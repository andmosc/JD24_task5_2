import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String TAG_ID = "id";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_AGE = "age";

    public static void main(String[] args) throws Exception {
        Document doc = buildDocument("data/data.xml");
        String fileJson = "data/Employee.json";
        List<Employee> listEmployee = parseXML(doc);

        String json = listToJson(listEmployee);
        createJasonFile(json, fileJson);
    }

    private static List<Employee> parseXML(Document doc) {

        List<Employee> employeeList = new ArrayList<>();

        NodeList rootChild = doc.getFirstChild().getChildNodes();

        for (int i = 0; i < rootChild.getLength(); i++) {
            if (rootChild.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Node employeeNode = rootChild.item(i);
            NodeList employeeChild = employeeNode.getChildNodes();

            Employee employee = parseElement(employeeChild);

            employeeList.add(employee);
        }
        return employeeList;
    }

    private static Employee parseElement(NodeList employeeChild) {

        int id = 0;
        String lastName = null;
        String firstName = null;
        String country = null;
        int age = 0;

        for (int i = 0; i < employeeChild.getLength(); i++) {
            if (employeeChild.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            switch (employeeChild.item(i).getNodeName()) {
                case TAG_ID: {
                    id = Integer.parseInt(employeeChild.item(i).getTextContent());
                    break;
                }
                case TAG_LASTNAME: {
                    lastName = employeeChild.item(i).getTextContent();
                    break;
                }
                case TAG_FIRSTNAME: {
                    firstName = employeeChild.item(i).getTextContent();
                    break;
                }
                case TAG_COUNTRY: {
                    country = employeeChild.item(i).getTextContent();
                    break;
                }
                case TAG_AGE: {
                    age = Integer.parseInt(employeeChild.item(i).getTextContent());
                    break;
                }
            }
        }
        return new Employee(id, firstName, lastName, country, age);
    }

    private static Document buildDocument(String fileCSV) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder().parse(new File(fileCSV));
    }


    private static String listToJson(List<Employee> listEmploee) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listEmploee);
        return json;
    }

    private static void createJasonFile(String json, String fileDir) {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileDir))
        ) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
