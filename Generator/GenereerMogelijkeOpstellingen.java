package Generator;

import java.io.IOException;

/* 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;

*/
import java.io.Writer;

//import java.sql.*;



public class GenereerMogelijkeOpstellingen {
    
    Long aantalBorden = 0L;
    final int SIZE = 10; 

    public Long getAantalBorden() { return aantalBorden; }

    public void generate( int colStart, int rowStart, Writer output, Opstelling opstelling, Schip schip, int level, Database database ) throws IOException{
        
        Opstelling plaatsHorizontaal;
        Opstelling plaatsVerticaal; 



        int        newColStart = 0;             // initialisatie is willekeurig
        int        newRowStart = 0;             // initialisatie is willekeurig

        Schip      nextSchip = Schip.SLAGSCHIP; // initialisatie is willekeurig

        for( int col=colStart; col < (SIZE-1); col++) {
          for( int row=rowStart; row < (SIZE-1); row++) {

             plaatsHorizontaal = opstelling.copyAll();
             plaatsVerticaal = opstelling.copyAll();
             
             Anker anker = new Anker(col, row);
               
             if ( plaatsHorizontaal.plaatsSchip( schip, anker, Richting.HORIZONTAAL ) ) {
                if ( plaatsHorizontaal.bordCompleet() ) {
                  aantalBorden++; 
                  output.append( plaatsHorizontaal.listBoard() + "\n");
                  database.insertFormatie( aantalBorden, plaatsHorizontaal.listBoard() );
                }
                else {
                  nextSchip = plaatsHorizontaal.volgendTePlaatsenSchip();
                  if ( schip != nextSchip ) {
                     newColStart = 1; newRowStart = 1;
                  } else {
                     newColStart = col; newRowStart = row;
                  }
                  generate( newColStart, newRowStart, output, plaatsHorizontaal, nextSchip, (level + 1), database );   
                }  
             } 
         
             if ( plaatsVerticaal.plaatsSchip( schip, anker, Richting.VERTICAAL ) ) {
                if ( plaatsVerticaal.bordCompleet() ) {
                  aantalBorden++;
                  output.append( plaatsVerticaal.listBoard() + "\n");
                  database.insertFormatie( aantalBorden, plaatsVerticaal.listBoard() );
                }
                else {
                  nextSchip = plaatsVerticaal.volgendTePlaatsenSchip();
                  if ( schip != nextSchip ) {
                     newColStart = 1; 
                     newRowStart = 1;
                  } else {
                     newColStart = col;
                     newRowStart = row;
                  }
                  generate( newColStart, newRowStart, output, plaatsVerticaal, nextSchip, (level + 1), database );
                }
              }
           }                   
       }
    }
    

    public static void main( String[] args) throws IOException {
    }
}

