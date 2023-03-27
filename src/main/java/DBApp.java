import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;

public class DBApp {
    public void init() {
        //very bri'ish amirit
        //ya m8
        //stop tolkin

    }

    //edit al niggaz
    // following method creates one table only
    // strClusteringKeyColumn is the name of the column that will be the primary
    // key and the clustering column as well. The data type of that column will
    // be passed in htblColNameType
    // htblColNameValue will have the column name as key and the data
    // type as value
    // htblColNameMin and htblColNameMax for passing minimum and maximum values
    // for data in the column. Key is the name of the column
    public void createTable(String strTableName, String strClusteringKeyColumn,
                            Hashtable<String,String> htblColNameType, Hashtable<String,String> htblColNameMin,
                            Hashtable<String,String> htblColNameMax ) throws DBAppException, IOException, CsvValidationException {

        //create instance of Table class
        Table table = new Table(strTableName, strClusteringKeyColumn,
                htblColNameType, htblColNameMin,
                htblColNameMax);

        // save the table to hard disk
        table.serializeTable();
    }


    // following method inserts one row only.
    // htblColNameValue must include a value for the primary key
    public void insertIntoTable(String strTableName, Hashtable<String,Object> htblColNameValue) throws DBAppException, CsvValidationException, IOException {
        // load table data from hard disk
        try {
            Table tTable = Table.loadTable(strTableName);
            tTable.insertIntoTable(htblColNameValue);
            tTable.serializeTable();
        } catch (Exception e) { // if table does not exist or some error happened
            throw new DBAppException(e.getMessage());
        }

    }


    // following method updates one row only
    // htblColNameValue holds the key and new value
    // htblColNameValue will not include clustering key as column name
    // strClusteringKeyValue is the value to look for to find the row to update.
    public void updateTable(String strTableName, String strClusteringKeyValue,
                            Hashtable<String,Object> htblColNameValue ) throws DBAppException {

    }


    // following method could be used to delete one or more rows.
    // htblColNameValue holds the key and value. This will be used in search
    // to identify which rows/tuples to delete.
    // htblColNameValue enteries are ANDED together
    public void deleteFromTable(String strTableName, Hashtable<String,Object> htblColNameValue) throws DBAppException, IOException, CsvException {

        //call isValidForDeletion in try/catch block
        try {
            if (isValidForDeletion(strTableName, htblColNameValue)) {
                //load table
                Table tTable = Table.loadTable(strTableName);

                //delete from table
                tTable.deleteFromTable(strTableName, htblColNameValue);

                //serialize table
                tTable.serializeTable();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public Iterator selectFromTable(SQLTerm[] arrSQLTerms, String[] strarrOperators) throws DBAppException {
        return null;

    }

    //bonus
    public Iterator parseSQL( StringBuffer strbufSQL ) throws DBAppException {
        return null;
    }

    public static void main(String[] args) throws Exception {

        //testing Table class creation
        String strTableName = "Student";
        DBApp dbApp = new DBApp( );

        Hashtable htblColNameType = new Hashtable( );
        htblColNameType.put("id", "java.lang.Integer");
        htblColNameType.put("name", "java.lang.String");
        htblColNameType.put("gpa", "java.lang.Double");

        Hashtable htblColNameMin = new Hashtable( );
        htblColNameMin.put("id", "0");
        htblColNameMin.put("name", "A");
        htblColNameMin.put("gpa", "0.0");

        Hashtable htblColNameMax = new Hashtable( );
        htblColNameMax.put("id", "1000000000");
        htblColNameMax.put("name","ZZZZZZZZZZZ");
        htblColNameMax.put("gpa", "4.0");



        dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );

        // count time
        long startTime = System.currentTimeMillis();
        for (int i = 1000; i >= 0; i--) {
            int finalI = i;
            dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
                put("id", finalI);
                put("name", "n" + finalI);
                put("gpa", 0.9);
            }});
            System.out.println("inserted " + i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Took " + (endTime - startTime)/1000 + " seconds");

        Table t = Table.loadTable("Student");
        t.showPage(0);

//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 2);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 3);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 5);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 4);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 6);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 0);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 12);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});
//
//        dbApp.insertIntoTable(strTableName, new Hashtable<String, Object>() {{
//            put("id", 12);
//            put("name", "AAAAA");
//            put("gpa", 0.34);
//        }});

    }

    public static boolean isValidForDeletion(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException, CsvException, DBAppException {
        //declaring csv reader
        CSVReader reader = new CSVReader(new FileReader("src/main/java/metadata.csv"));

        //check if table exists
        String[] line;
        while ((line = reader.readNext()) != null) {
            if (line[0].equals(strTableName)) {

                //check that column names and types are valid
                for (String key : htblColNameValue.keySet()) {

                    //look in metadata file for column name
                    boolean found = false;
                    for (String[] line2 : reader.readAll()) {
                        //reminder:
                        //line2[0] = table name
                        //line2[1] = column name
                        //line2[2] = column type

                        if (line2[0].equals(strTableName) && line2[1].equals(key)) {
                            found = true;

                            //check that column type is valid
                            //since the column exists in metadata file we are sure the column type should
                            //be one of the following: java.lang.Integer, java.lang.String, java.lang.Double
                            if (!line2[2].equals(htblColNameValue.get(key).getClass().getName())) {
                                throw new DBAppException("Invalid column type for deletion");
                            }
                            break;
                        }
                    }

                    if (!found) {
                        throw new DBAppException("Invalid column name for deletion");
                    }
                }

                //can delete from table
                return true;
            }
        }
        throw new DBAppException("Table does not exist");
    }

    public static boolean isExistingTable(String strTableName) throws IOException, CsvValidationException {
        //declaring csv reader
        CSVReader reader = new CSVReader(new FileReader("src/main/java/metadata.csv"));

        //check if table exists
        String[] line;
        while ((line = reader.readNext()) != null) {
            if (line[0].equals(strTableName)) {
                return true;
            }
        }
        return false;
    }
}
