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
public class Chess_Engine {

    /**
     * @param args the command line arguments
     * 
     */
    public enum Type {
    
    }
    public static void main(String[] args) {
        // TODO code application logic here            
        Bitboard.toBitboard(1);
        
        System.out.print(Bitboard.searchPosition(16));
        
    }
    
}
