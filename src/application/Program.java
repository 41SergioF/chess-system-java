package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import boardgame.BoardException;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {

		ChessMatch chessMatch = new ChessMatch();
		Scanner scan = new Scanner(System.in);
		
		while(true) {
			try {
				UI.printBoard(chessMatch.getPieces());
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(scan);
				
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(scan);
				
				ChessPiece capturedPiece = chessMatch.perfomaChessMove(source, target);
				UI.clearScreen();
			}
			catch (ChessException e) { 
				System.out.println(e.getMessage());
				scan.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				scan.nextLine();
			}
			catch (BoardException e) {
				System.out.println(e.getMessage());
				scan.nextLine();
			}
		}
		
		//scan.close();
	}
 
}
