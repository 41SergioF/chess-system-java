package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
	
	private Color color;
	
	public ChessPiece() {
		
	}

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public ChessPosition getChessPosition() {//retrorna um chessposition dessa peça
		return ChessPosition.fromPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) { //testa se a peça é opponente
		ChessPiece piece = (ChessPiece)getBoard().piece(position); //dada uma posição ele é retornado uma peça do tabulero
		return piece != null && piece.getColor() != color; //essa peça existindo e sendo de cores diferentes é retornado true
	}
		
}
