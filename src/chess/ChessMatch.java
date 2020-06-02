package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;	
	
	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}
	//é preciso fazer um downcastin em todas a peças antes 
	public ChessPiece[][] getPieces(){
		ChessPiece[][] matPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				matPieces[i][j] = (ChessPiece)board.piece(i, j);
			}
		}
		return matPieces;
	}
	
	private void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2,1));
		board.placePiece(new King(board, Color.BLACK), new Position(0,4));
		board.placePiece(new King(board, Color.WHITE), new Position(7,4));
	}
	
}
