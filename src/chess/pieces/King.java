package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	private ChessMatch match;
	
	public King() {
	}

	public King(Board board, Color color, ChessMatch match) {
		super(board, color);
		this.match = match;
	}
		
	@Override
	public String toString() {
		return "K";
	}

	private boolean camMove(Position position) { //ira testar de o rei pode ser movido para essa posição 
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor(); //o rei poderar andar se somenete se ouver uma peça 
			//e a peça seja de cor difrente 
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		//left
		Position p = new Position(position.getRow(), position.getColumn()-1);
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//above
		p.setValues(position.getRow() -1, position.getColumn());
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//right
		p.setValues(position.getRow(), position.getColumn() + 1);
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//below
		p.setValues(position.getRow()+1, position.getColumn());
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//nw
		p.setValues(position.getRow() -1, position.getColumn()-1);
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//ne
		p.setValues(position.getRow() -1, position.getColumn() +1);
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//sw
		p.setValues(position.getRow() +1, position.getColumn() -1);
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//se
		p.setValues(position.getRow() +1, position.getColumn() +1);
		if(getBoard().positionExists(p) && camMove(p))
			mat[p.getRow()][p.getColumn()] = true;
		
		//Specialmove castling 
		if (getMoveCount() == 0 && !match.getCheck()) {
			//kingside rook 
			Position positionOfRook = new Position(position.getRow(), position.getColumn()+3);
			if (testRookCastling(positionOfRook)) {
				Position p1 = new Position(position.getRow(), position.getColumn()+1);
				Position p2 = new Position(position.getRow(), position.getColumn()+2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[p2.getRow()][p2.getColumn()] = true;
				}
			}
			//Queenside rook 
			positionOfRook.setColumn(position.getColumn()-4);
			if (testRookCastling(positionOfRook)) {
				Position p1 = new Position(position.getRow(), position.getColumn()-1);
				Position p2 = new Position(position.getRow(), position.getColumn()-2);
				Position p3 = new Position(position.getRow(), position.getColumn()-3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[p3.getRow()][p3.getColumn()] = true;
				}
			}
		}	
		return mat;
	}
	
}
