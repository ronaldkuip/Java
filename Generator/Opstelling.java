package Generator;

public class Opstelling {
 
    static final int SIZE          = 10;
    int[][] bord                   = new int[SIZE][SIZE];

    final int aantalTePlaatsenSlagschepen = 1;
    final int aantalTePlaatsenKruisers    = 2;
    final int aantalTePlaatsenFregatten   = 3;
    
    int geplaatstAantalSlagschepen = 0;
    int geplaatstAantalKruisers    = 0;
    int geplaatstAantalFregatten   = 0;
    
    // Maak een value voor value kopie van het object

    public Opstelling copyAll() {
       
       Opstelling kopie = new Opstelling();
       for( int col=0; col<SIZE; col++ )
          for( int row=0; row<SIZE; row++ ) {
            kopie.bord[col][row] = this.bord[col][row];
          }
       kopie.geplaatstAantalSlagschepen = this.geplaatstAantalSlagschepen;
       kopie.geplaatstAantalKruisers    = this.geplaatstAantalKruisers;
       kopie.geplaatstAantalFregatten   = this.geplaatstAantalFregatten;
       return kopie;
    }

    private boolean heeftRuimte( Schip schip, Anker anker, Richting richting, int scheepsLengte ) {
         
      if ( richting == Richting.HORIZONTAAL ) {
         int test = 0;
         if ( anker.col + scheepsLengte > (SIZE -1) ) return false; // past niet op het bord
         for( int col = anker.col; col < anker.col + scheepsLengte; col++ ) test = test + bord[col][anker.row];
         return test == 0;
      };
      if ( anker.row + scheepsLengte > (SIZE-1) ) return false; // past niet op het bord
      int test = 0;
      for( int row = anker.row; row < anker.row + scheepsLengte; row++ ) test = test + bord[anker.col][row];
      return test == 0;
    }
    
    private boolean geenBuren( Schip schip, Anker anker, Richting richting, int scheepsLengte ) {

      if ( richting == Richting.HORIZONTAAL ) {
         int test = 0;
         if ( anker.col + scheepsLengte > (SIZE-1) ) 
            return false; // past niet op het bord
         for( int col = anker.col - 1 ; col < anker.col + scheepsLengte + 1; col++ ) test = test + bord[col][anker.row - 1];
         if ( test != 0 ) return false;
         for( int col = anker.col - 1 ; col < anker.col + scheepsLengte + 1; col++ ) test = test + bord[col][anker.row + 1];
         if ( test != 0 ) return false;
         if ( ( bord[anker.col - 1][anker.row] !=0 ) || ( bord[anker.col + scheepsLengte][anker.row] !=0 ) ) return false;
         return true;
      };
      int test = 0;
      if ( anker.row + scheepsLengte > (SIZE-1) ) 
         return false; // past niet op het bord
      for( int row = anker.row - 1 ; row < anker.row + scheepsLengte + 1; row++ ) test = test + bord[anker.col - 1][row];
      if ( test != 0 ) return false;
      for( int row = anker.row - 1 ; row < anker.row + scheepsLengte + 1; row++ ) test = test + bord[anker.col + 1][row];
      if ( test != 0 ) return false;
      if ( ( bord[anker.col][anker.row - 1] !=0 ) || ( bord[anker.col][anker.row + scheepsLengte] !=0 ) ) return false;
      return true;
   }

   public int bepaalScheepsLengte( Schip schip ) {

      switch (schip) {
         case SLAGSCHIP : return 4;
         case KRUISER   : return 3;
         case FREGAT    : return 2;
      }
      return 0;
   }

   public String listBoard() {

      String x = "[";
      for( int i=1; i<(SIZE-1); i++) {
        for( int j=1; j<(SIZE-1); j++ ) { 
          x = x + (char) (bord[i][j] + 48);
        }
      }
      return x + "]";
   }
   
   public boolean bordCompleet() {

      return ( ( geplaatstAantalSlagschepen == aantalTePlaatsenSlagschepen ) && 
               ( geplaatstAantalKruisers == aantalTePlaatsenKruisers )       && 
               ( geplaatstAantalFregatten == aantalTePlaatsenFregatten ) );
   }

   public Schip volgendTePlaatsenSchip() {

       if ( geplaatstAantalSlagschepen  < aantalTePlaatsenSlagschepen ) return Schip.SLAGSCHIP;
       if ( geplaatstAantalKruisers     < aantalTePlaatsenKruisers    ) return Schip.KRUISER;
       if ( geplaatstAantalFregatten    < aantalTePlaatsenFregatten   ) return Schip.FREGAT;
       return null;
   }

   public boolean plaatsSchip( Schip schip, Anker anker, Richting richting ) {
 

      int scheepsLengte = bepaalScheepsLengte( schip ); 
      if ( heeftRuimte( schip, anker, richting, scheepsLengte ) ) { 
         if ( geenBuren( schip, anker, richting, scheepsLengte ) ) { 
            if ( richting == Richting.HORIZONTAAL ) { 
               for( int col=anker.col; col<anker.col + scheepsLengte; col++) {
                 bord[col][anker.row] = scheepsLengte;
               }
            } else {  
               for( int row=anker.row; row<anker.row + scheepsLengte; row++) {
                 bord[anker.col][row] = scheepsLengte;
                }
            }
            switch (schip) {
               case SLAGSCHIP : geplaatstAantalSlagschepen++; break;
               case KRUISER   : geplaatstAantalKruisers++; break;
               case FREGAT    : geplaatstAantalFregatten++; break;  
            }
            return true;
         }
      }
      return false;
    }
    
    public void buildFromString( String formatie ) {
        for( int col=1; col<SIZE-1; col++ )
          for( int row=1; row<SIZE-1; row++ ) 
            bord[col][row] = formatie.charAt( ( col - 1 ) * 8 + row ) - '0';          
    }

    public Boolean isCorrectedlyGenerated() {
        for( int col=1; col<SIZE-1; col++ )
          for( int row=1; row<SIZE-1; row++ ) {
             // reasons to fail
             if ( bord[ col ][ row ] != 0 ) { // Er mag nu niet iets anders rondom deze coordinaat staan
                if ( bord[ col -1 ][ row -1 ] != 0 ) return false;
                if ( bord[ col -1 ][ row    ] != 0 ) return false;
                if ( bord[ col -1 ][ row +1 ] != 0 ) return false;
                if ( bord[ col    ][ row -1 ] != bord[ col ][ row ] ) return false;
                if ( bord[ col    ][ row +1 ] != bord[ col ][ row ] ) return false;
                if ( bord[ col +1 ][ row -1 ] != 0 ) return false;
                if ( bord[ col +1 ][ row    ] != 0 ) return false;
                if ( bord[ col +1 ][ row +1 ] != 0 ) return false;
             } 
          }
        return true;
    }

    public Opstelling() {
        for( int col=0; col<SIZE; col++ )
          for( int row=0; row<SIZE; row++ )
            bord[col][row] = 0;
    }
}
 
