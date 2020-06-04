package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	public King() {
	}

	public King(Board board, Color color) {
		super(board, color);
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
			
		return mat;
	}
	
}
