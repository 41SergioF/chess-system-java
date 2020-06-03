package boardgame;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board() {
		
	}

	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Erro creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) { //retorna a peça pela linha e coluna]
		if (!positionExists(row, column)) {
			throw new BoardException("Position isn't on the board");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {	//retorna a peça da matrix do tabulelo pela posição
		if (!positionExists(position)) {
			throw new BoardException("Position isn't on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException("There's already a piece on position " + position); //caso tenha uma peça nessa posição 
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;//ela prcisa saber que não é mais nula 
	}
	
	public Piece removePiece(Position position) {	///faz a remoção de uma peça retornado nulo 
		if (!positionExists(position)) {//confere a existencia da posição no tabulero . caso 
			throw new BoardException("Position isn't on the board");//caso a peça não exista, será declaradeo uma exeção
		}
		if (piece(position) == null) { //confere se tem um a peça nessa posição 
			return null; // retorna uma posição de peça e declara como nula 
		}
		Piece aux = piece(position);//cria uma aux para apontar para peça 
		aux.position = null;//declara que a posição dessa nela é nulo 
		pieces[position.getRow()][position.getColumn()] = null;//delara que nessa posição na matrix não a peça 
		return aux;//retorna a peça
	}
	
    private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Position isn't on the board");
		}
		return piece(position) != null;		
	}
}
