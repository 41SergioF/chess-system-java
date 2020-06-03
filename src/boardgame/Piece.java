package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece() {
		
	}

	public Piece(Board board) {
		//uma peça redem criada tera o valor null, indicando que a peça não está no tabulero
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	} 
	public abstract boolean[][] possibleMoves(); //método abstrado que retorna uma matriz de boleanos
	
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i = 0; i<mat.length; i++) {
			for (int j = 0; j<mat.length; j++) {
				if (mat[i][j] == true)
					return true;
			}
		}
		return false;
	}
}