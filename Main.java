package sample;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.itextpdf.text.*;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Projections.*;
import static java.util.Arrays.asList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Test");

        GenerateReport generateReport = new GenerateReport();

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase database = mongoClient.getDatabase("Student");
        MongoCollection<org.bson.Document> studentsCollection = database.getCollection("studentInfo");
        ArrayList<String> key_details = new ArrayList<String>();
        ArrayList<Object> value_details = new ArrayList<Object>();

        String keys[] = new String[]{"SNo","Name","Age","Roll_No"};
        for(int i = 0 ; i<keys.length ; i++)
        {
            key_details.add(keys[i]);
        }
        Object row = "";
        //String path = "D:\\JAVA codes\\BELsample.txt";
        BufferedReader txtReader = new BufferedReader(new FileReader("C:\\Users\\vijayamb\\IdeaProjects\\StudentDB\\BELsample.txt"));
        while((row = txtReader.readLine()) != null)
        {
            Object[] data = ((String)row).split("\t");
            int index = 0;
            for(Object value:data){
                value_details.add(value);
            }
            Document doc = new Document();
            for(int i=0 ; i<key_details.size() ; i++)
            {
                doc.append(key_details.get(i),value_details.get(i));
            }
            studentsCollection.insertOne(doc);

            value_details.clear();

        }

        MongoCollection<org.bson.Document> fileCollection = database.getCollection("FileInfo");

        Document doc = new Document("File Name", "BELsample.txt");
        doc.append("Path", "C:\\Users\\vijayamb\\IdeaProjects\\StudentDB\\BELsample.txt");
        doc.append("File Size", "21.5 MB");
        doc.append("Creation Time", new Timestamp(System.currentTimeMillis()));

//        docs.add(d1);

        fileCollection.insertOne(doc);


        generateReport.pdfGeneration("StudentReport.pdf");

        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }






    public static void main(String[] args) {
        launch(args);
    }
}
