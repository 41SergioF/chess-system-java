package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	public Pawn() {
		
	}

	public Pawn(Board board, Color color) {
		super(board, color);
		
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		if (getColor() == Color.WHITE) { //se a peça for branca 
			p.setValues(position.getRow()-1, position.getColumn());//veremos se ele pode andar um a casa para frente
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
				mat[p.getRow()][p.getColumn()] = true;
				
			//if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			p.setValues(position.getRow()-2, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getMoveCount() == 0 && mat[position.getRow()-1][position.getColumn()])
				mat[p.getRow()][p.getColumn()] = true;
		//}
			
			p.setValues(position.getRow()-1, position.getColumn()-1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;
			
			p.setValues(position.getRow()-1, position.getColumn()+1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;
		}else {
			p.setValues(position.getRow()+1, position.getColumn());//veremos se ele pode andar um a casa para frente
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
				mat[p.getRow()][p.getColumn()] = true;
				
			p.setValues(position.getRow()+2, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getMoveCount() == 0 && mat[position.getRow()+1][position.getColumn()])
				mat[p.getRow()][p.getColumn()] = true;
			
			p.setValues(position.getRow()+1, position.getColumn()-1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;
			
			p.setValues(position.getRow()+1, position.getColumn()+1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
