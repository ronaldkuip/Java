package EliminatieMachine;

import Generator.Schip;

public class Block {
    
    byte  state;
    Schip schip;
    int   schipGrootte;
    public int   mogelijkheden = 0;
    public int   vrijeCoordinaten = 0;
    static final int SIZE = 10;
    boolean[] posities   = new boolean[SIZE];

    public Block( Schip schip ) {
        
        posities[0] = true;
        for(int i=1; i< (SIZE-1); i++) posities[i] = false;
        posities[SIZE-1] = true;

        this.schip = schip;
        state = 0;

        schipGrootte = 4;

        eliminateAll(6, posities);
    }

    private void eliminateAll(int at, boolean posities[]) {

        int anker = 0;
        int positie = 0;
        posities[at] = true;
        
        while ( anker < SIZE - 1) {

            while ( !posities[ ++positie ] ) {}; // we hebben de 'vrije plekken overgeslagen' 

            if ( positie - anker <= schipGrootte ) for(int i=anker+1; i<positie; i++) posities[i] = true; // eliminatie
            else {
                mogelijkheden += positie - anker - schipGrootte; // meta admin
                vrijeCoordinaten += positie - anker - 1;  // meta admin
            }

            anker = positie; // volgende scan voorbereiden
        }

        // new block aanmaken en toekennen aan array
    }



}
 