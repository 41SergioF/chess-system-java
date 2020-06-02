package boardgame;

public class Piece {

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

}
