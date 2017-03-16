/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_engine;

/**
 *
 * @author jonathan
 */
public class Bitboard {          
    final public static void toBitboard(long number){   
        String bs = Long.toBinaryString(number);  
        int count = 64 - bs.length();
        String  repeated = new String(new char[count]).replace("\0", "0");
        String binary = repeated.concat(bs);              
        for (int i = 0; i<8; ++i){
            String row  = binary.substring(i*8, ((i+1)*8));
            System.out.print(row + "\n");        
        }         
    }    
   final public static int searchPosition(long bitboard){
        if (bitboard == 0) {
            return 0;
        }
        int bound = 32;
        int scope = bound;
        boolean stop = false;        
        while (stop == false){
            scope = scope/2;
            if (bitboard == Math.pow((double)2,(double)bound)) {
                stop = true;
            }else if (bitboard > Math.pow((double)2,(double)bound)){
                bound = bound + scope;
            }else {
                bound = bound - scope;
                if (scope== 0) {
                    bound = bound - 1;
                }
            }
            if (scope == 0) {
                stop = true;
            }
        }
        return bound;
    }
   
 /*  long movesVertical(Piece piece, long bitboards){
       
       long bitboard = piece.bitboard;
       int position = piece.position;

   }
   */
    
}
