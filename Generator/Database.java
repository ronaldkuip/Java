package Generator;

import java.sql.*;

public class Database {
    
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "toverkr8";

        PreparedStatement statement = null;
        Statement queryStatement    = null;
        Connection connection       = null;
        ResultSet resultSet         = null; 

        int count = 0;
        String SQL = "insert into \"VlootFormaties\"(\"Formatie\",\"Codering\") values (?,?)";
        
        public Database() {
            
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.prepareStatement(SQL);
                // statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            } catch (Exception e) {
                System.out.println("Fout in Database constructor: " + e.getMessage());
            }
        }

        public ResultSet queryVlootFormatie() {

            try {
                queryStatement = connection.createStatement();
                resultSet      = queryStatement.executeQuery("select * from \"VlootFormaties\"");
            } catch (Exception e) {
                System.out.println("Fout in queryVlootFormatie: " + e.getMessage());
            }    
            return resultSet;
        }
        
        public void insertFormatie(Long aantalBorden, String formatie) {

            // add lastbatch (can be empty)
            if (formatie.length() == 0) {
                try {
                  statement.executeBatch();
                }  catch (Exception e) {
                System.out.println("Fout in final execute batch: " + e.getMessage());
            }
                return;
            }
 
            try {
               statement.setLong(1, aantalBorden);
               statement.setString(2, formatie);
               statement.addBatch();
               count++;       
            }  catch (Exception e) {
            System.out.println("Fout building batch: " + e.getMessage());
            }

            // execute every 100 rows 
            if (count % 100 == 0) {
                try {
                   statement.executeBatch();
                }  catch (Exception e) {
                System.out.println("Fout in regular execute batch: " + e.getMessage());
            }
        }
    }
}




