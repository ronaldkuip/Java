import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import EliminatieMachine.Block;
import Generator.Database;
import Generator.GenereerMogelijkeOpstellingen;
import Generator.Opstelling;
import Generator.Schip;

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
   
        boolean[] flagsUp = new boolean[10];
        for(int i=1; i < ( 10 - 1 ); i++) flagsUp[i] = false;
        flagsUp[0] = true;
        flagsUp[9] = true;
        
        new Block(flagsUp, Schip.SLAGSCHIP, 0, 5, 8);

        for( int i=0; i < 256; i++ ) {

          Block b = Block.eliminatieMachine[0][i]; // b as shorthand

          if ( b != null ) {
             System.out.print( "State: " + i + " ");
             for( int j=1; j<9; j++ ) {
                System.out.print( b.posities[j] + "(" + b.transitie[j] + " , " + ( b.getCombinaties() - Block.eliminatieMachine[0][b.transitie[j]].getCombinaties() ) + ")" );
             }
             System.out.println( " aantal mogelijkheden: " + b.getCombinaties() );
             
          }
        }
        
        /*
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
         */
    }
}
 