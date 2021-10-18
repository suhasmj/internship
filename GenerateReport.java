package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.client.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GenerateReport {

    private static String FILE;
    Document document;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font subTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    private static Font subTableTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);
    private static Font subHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);



    private static void addMetaData (com.itextpdf.text.Document document){
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Madhvesh");
        document.addCreator("Madhvesh");
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addFileData(Document document) throws DocumentException
    {


        Paragraph preface = new Paragraph();
        Chunk c1 = new Chunk("File Info: ", subHeadingFont);

        DateFormat df = new SimpleDateFormat("MMM dd YYYY HH:mm:ss");
        Date dateObj = new Date();

        Chunk c2 = new Chunk(FILE, subTextFont);


        preface.add(c1);
        preface.add(c2);

        document.add(preface);

        preface = new Paragraph();
        addEmptyLine(preface, 1);

        document.add(preface);


    }

    private static void addReportHeading(Document document) throws DocumentException
    {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Student Report", catFont));

        addEmptyLine(preface, 2);

        preface.setAlignment(Element.ALIGN_CENTER);


        document.add(preface);
    }

    private static void addTable(Document document)
            throws DocumentException, JSONException {
        PdfPTable table = new PdfPTable(4);

        float[] columnWidths = new float[]{50f, 100f, 90f, 100f};

        table.setWidths(columnWidths);

        PdfPCell c1 = new PdfPCell(new Phrase("SNo", subTableTextFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        ArrayList<String> columnsList = new ArrayList<String>();

        columnsList.add("");

        String Keys[] = new String[]{"SNo","Name","Age","Roll_No"};
        String columns[] = new String[]{"SNo","Name","Age","Roll_No"};

        ArrayList <String> arrayList = new ArrayList<String>();

        for(int i=0; i<columns.length;i++){
            arrayList.add(columns[i]);
        }

        for(int i=0;i<Keys.length;i++)
        {

            c1 = new PdfPCell(new Phrase(Keys[i], subTableTextFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        table.setHeaderRows(1);

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase database = mongoClient.getDatabase("Student");
        MongoCollection<org.bson.Document> collection = database.getCollection("studentInfo");
        MongoCursor<org.bson.Document> cursor = collection.find().iterator();

        while (cursor.hasNext()) {
            org.bson.Document docx = cursor.next();
            for(int i = 0;i<columns.length;i++){
                table.addCell(new Phrase((String) docx.get(arrayList.get(i)), subTableTextFont));
            }
        }

        document.add(table);

    }

    public static void pdfGeneration(String fileName){
        try {
            Document document = new Document();
            FILE = fileName;
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            addMetaData(document);
            addReportHeading(document);
            addFileData(document);
            addTable(document);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
