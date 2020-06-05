package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer; 
	private Board board;	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
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
	
	public boolean[][] possibleMoves(ChessPosition sourceposition){
		Position position = sourceposition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece perfomaChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {//move as peças recebendo as posições de xadrez
		Position source = sourcePosition.toPosition(); //chama o método para converção  
		Position target = targetPosition.toPosition(); //de posição de xadrez para posição de matriz  
		validateSourcePosition(source); //valida a exixtencia da peça na matriz (caso contrario exception)
		validateTargetPosition(source, target); //
		Piece capturedPiece = makeMove(source, target); //move a peça na matriz  recemeno a peça cabiturada 
		nextTurn();
		
		return (ChessPiece)capturedPiece; //faz um downcasting e retorna a peça 
	}
	
	public Piece makeMove(Position source, Position target) { //move as peças no tabulero. trabalhando com as posições de matriz
		Piece pMove = board.removePiece(source); //retira a peça que está na origem 
		Piece capturedPiece = board.removePiece(target); //retira as peças que está no destino
		board.placePiece(pMove, target); //coloca a peça de origem no destino 
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece; //retorna a peça capturada 
	}
	 
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {//teste se tem um a peça nessa posição (entra no if endo false) 
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {//teste se esse peça é do jogador em questão 
			throw new ChessException("The chosen piece isn't yours");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There's no possible moves for the chosen piece");
		}	
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn(){//proximo turno
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE; 
	}
	
	private void placeNewPiece(char coolumn, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(coolumn, row).toPosition());
		piecesOnTheBoard.add(piece); //coloca na lista de peças no tabuleiro	
	}
	
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
	
}
