import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Generator.GenereerMogelijkeOpstellingen;
import Generator.Opstelling;
import Generator.Schip;
import Generator.Database;
import java.sql.*;

import EliminatieMachine.Block;

public class Applicatie{

    public void alleFormaties() throws IOException {

        Database database = new Database();

        Writer output = new BufferedWriter(new FileWriter("C:\\Projects\\Java\\Generator\\Data\\Opstellingen.csv"));;
        System.out.println("We gaan alle mogelijkheden genereren" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

        GenereerMogelijkeOpstellingen spel = new GenereerMogelijkeOpstellingen();
        spel.generate( 1,1, output, new Opstelling(), Schip.SLAGSCHIP, 0, database );

        database.insertFormatie(0L,""); // flush remaining records to db

        System.out.println( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "Totaal aantal borden: " + spel.getAantalBorden() );
        output.close();
    }

    public static void main(String[] args) throws IOException {
   

        Block werk = new Block(Schip.SLAGSCHIP);

        System.out.println( "Aantal mogelijkheden: " + werk.mogelijkheden);
        
        Database database = new Database();
        ResultSet rs = database.queryVlootFormatie();
        //Long bord = 0L;
        //String formatie = "";
        Opstelling teValideren = new Opstelling();
     
        try {
          while (rs.next()) {
            //bord = rs.getLong("Formatie");
            //formatie = rs.getString("Codering");
            // Process the data here
            teValideren.buildFromString( rs.getString("Codering") );
            if ( !teValideren.isCorrectedlyGenerated() ) System.out.println( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "Probleem met bordnr" + rs.getLong("Formatie") );
          }
          rs.close();
         } catch (SQLException e) {
              System.out.println( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "Er gaat iets fout tijdens het verifieren: ");
         }
    }
}
 