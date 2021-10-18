package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase database = mongoClient.getDatabase("student");
        MongoCollection<org.bson.Document> collection = database.getCollection("studentInfo");
        ArrayList <String> key_details = new ArrayList<String>();
        ArrayList<Object> value_details = new ArrayList<Object>();

        String keys[] = new String[]{"sno.","Name","Age","Roll no."};
        for(int i = 0 ; i<keys.length ; i++)
        {
            key_details.add(keys[i]);
        }
        Object row = "";
        //String path = "D:\\JAVA codes\\BELsample.txt";
        BufferedReader txtReader = new BufferedReader(new FileReader("D:\\JAVA codes\\BELsample.txt"));
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
            collection.insertOne(doc);
            value_details.clear();

        }
    }
}
