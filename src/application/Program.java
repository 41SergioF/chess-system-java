package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
		List<ChessPiece> captured = new ArrayList<ChessPiece>();
		 
		while(!chessMatch.getCheckMate()) {
			try {
								
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(scan);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //com boolean do backgraoud
				
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(scan);
				
							
				ChessPiece capturedPiece = chessMatch.perfomaChessMove(source, target);
				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
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
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
		scan.close();
	}
 
}
