package EliminatieMachine;

import Generator.Schip;
import Generator.Opstelling;

public class Block {
    
    public static Block eliminatieMachine[][] = new Block[3][256];

    int      state;
    int      combinaties;
    int      vrijePosities; 
    Schip    schip;   
    int      schipGrootte;
      
    static final int SIZE = 10;
    public boolean[] posities    = new boolean[SIZE];
    public int[]     transitie   = new int[SIZE];
    
    public Block( boolean[] posities, Schip schip, int origin, int combinaties, int vrijePosities ) {

        this.schip = schip;
        this.schipGrootte = Opstelling.bepaalScheepsLengte( schip );       
        this.combinaties = combinaties;
        this.state = origin;
        this.vrijePosities = vrijePosities;
       
        eliminatieMachine[ 4 - Opstelling.bepaalScheepsLengte(schip) ][ state ] = this;

        for( int j=0; j< SIZE; j++) this.posities[j] = posities[j];

        boolean[] query = new boolean[10];

        for( int i=1; i<SIZE-1; i++ ) {
           // posities[0] = true;
           for( int j=0; j< SIZE; j++) query[j] = posities[j];
           // posities[SIZE-1] = true;
           eliminateAll(query, i, origin, combinaties );
        }
    }  

    public int getState() {
        return state;
    }

    public int getCombinaties() {
        return combinaties;
    }

    public Schip getSchip() {
        return schip;
    }

    public int getSchipGrootte() {
        return schipGrootte;
    }

    private int calcStateNumber( boolean[] p ) {

        int state = 0;
    
        for(var i=1;i<SIZE-1;i++) if ( p[i] ) state += (int) Math.pow(2, (8-i) );
           
        return state;

    }
    private void eliminateAll(boolean[] posities, int at, int origin, int combinaties) {

        int check = eliminatieMachine[ 4 - Opstelling.bepaalScheepsLengte(schip) ][ origin ].transitie[at];

        if ( eliminatieMachine[ 4 - Opstelling.bepaalScheepsLengte(schip) ][ origin ].transitie[at] != 0 ) {
            System.out.println( "Cancel: " + origin + " shooting at: " + at );
            return; // alleen uitvoeren als er nog geen transitie is berekend voor deze coordinaat
        } else {
            System.out.println( "Proceed: " + origin + " shooting at: " + at );
        }
        int anker = 0;
        int positie = 0;
        int mogelijkheden = 0;
        int vrijeCoordinaten = 0;
      
        System.out.println( "Start elimintatieproces voor: " + calcStateNumber( posities ) + " at " + at + " vrij: " + combinaties );
        posities[at] = true;
        
        while ( anker < SIZE - 1 ) {

            while ( !posities[ ++positie ] ) {}; // we hebben de 'vrije plekken overgeslagen' 

            if ( positie - anker <= schipGrootte ) for(int i=anker+1; i<positie; i++) posities[i] = true; // eliminatie
            else {
                mogelijkheden += positie - anker - schipGrootte; // meta admin
                vrijeCoordinaten += positie - anker - 1;  // meta admin
            }

            anker = positie; // volgende scan voorbereiden
            
        }

        int klad1 = calcStateNumber( posities );
        int klad2 = 4 - Opstelling.bepaalScheepsLengte(schip);
        eliminatieMachine[ 4 - Opstelling.bepaalScheepsLengte(schip) ][ origin ].transitie[at] = calcStateNumber( posities ); // berekende transitie

        // for( var i=1; i<SIZE-1; i++) System.out.print(posities[i]);
        System.out.println("\nOriginal State: "+ origin + "Shot at position: " + at + " leads to state: " + calcStateNumber( posities ) + " aantal mogelijkheden: " + mogelijkheden + " aantal vrije plekken " + vrijeCoordinaten );

        // new block aanmaken en toekennen aan array

        if ( ( mogelijkheden != combinaties ) && ( mogelijkheden != 0 || calcStateNumber( posities ) == 255 ) )
          {
              new Block( posities, this.schip, calcStateNumber( posities ), mogelijkheden, vrijeCoordinaten ); 
          }

    }



}
 