package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import boardgame.BoardException;
import boardgame.Position;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.pieces.Rook;

public class Program {

	public static void main(String[] args) {

		ChessMatch chessMatch = new ChessMatch();
		Scanner scan = new Scanner(System.in);
		
		while(true) {
			try {
								
				UI.printMatch(chessMatch);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(scan);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //com boolean do backgraoud
				
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
			System.out.println(UI.ANSI_BLUE_BACKGROUND+UI.ANSI_YELLOW +"5"+UI.ANSI_RESET);
		}
		
		//scan.close();
	}
 
}
