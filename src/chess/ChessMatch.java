package chess;

import boardgame.Board;
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
	private void placeNewPiece(char coolumn, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(coolumn, row).toPosition());
	}
	
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook(board, Color.WHITE));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 1,new King(board, Color.WHITE));
	}
	
}
