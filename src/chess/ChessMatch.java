package chess;

import java.beans.beancontext.BeanContext;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer; 
	private Board board;	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private boolean check; //informa se a partida está em check
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	
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
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
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
	
	public ChessPiece ReplacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There's no piece to BeanContext promoted");
		}
		
		if (!type.equals("B") && !type.equals("Q") && !type.equals("R") && !type.equals("N")) {
			return promoted;
		}
		
		Position position = promoted.getChessPosition().toPosition();//pegar a posiçao do promoute
		Piece piece = board.removePiece(position);
		piecesOnTheBoard.remove(position);
		
		ChessPiece newChessPiece = newPiece(type, promoted.getColor());
		board.placePiece(newChessPiece, position);//colocar no tabuleiro
		piecesOnTheBoard.add(newChessPiece);//adicionar na lista 
		
		return newChessPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("R")) return new Rook(board, color);
		return new Queen(board, color);
	}
	
	public ChessPiece perfomaChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {//move as peças recebendo as posições de xadrez
		Position source = sourcePosition.toPosition(); //chama o método para converção  
		Position target = targetPosition.toPosition(); //de posição de xadrez para posição de matriz  
		validateSourcePosition(source); //valida a exixtencia da peça na matriz (caso contrario exception)
		validateTargetPosition(source, target); //
		Piece capturedPiece = makeMove(source, target); //move a peça na matriz  recemeno a peça cabiturada 
		
		//cor do jogador do momento
		if (testCheck(currentPlayer)) {//teste de esse movimentto deixa seu rei em check
			undoMove(source, target, capturedPiece);//se sim! des faz a jogada 
			//System.out.println("/////////////////////////////");
			throw new ChessException("You can't put yourself in check");//lança um exceção
		}
		//se o opponete de jogador atual estiver em check, a parteda está em check 
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		promoted = null;
		if (movedPiece instanceof Pawn) {///se apeça que foi movida for uma pião 
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || 
					(movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);//pega a peça que será promovida 
				promoted = ReplacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		if (testCheckMate(opponent(currentPlayer)))
			checkMate = true;
		else
			nextTurn();
		
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2)) {
			enPassantVulnerable = movedPiece;
		}else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece; //faz um downcasting e retorna a peça 
	}
	
	public Piece makeMove(Position source, Position target) { //move as peças no tabulero. trabalhando com as posições de matriz
		ChessPiece pMove = (ChessPiece)board.removePiece(source); //retira a peça que está na origem 
		pMove.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target); //retira as peças que está no destino
		board.placePiece(pMove, target); //coloca a peça de origem no destino 
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		//movimento especial rock pequeno
		if (pMove instanceof King && target.getColumn() == source.getColumn()+2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()+3);
			Position targetT = new Position(source.getRow(), source.getColumn()+1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			//board.placePiece(board.removePiece(sourceT), targetT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		if (pMove instanceof King && target.getColumn() == source.getColumn()-2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()-4);
			Position targetT = new Position(source.getRow(), source.getColumn()-1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			//board.placePiece(board.removePiece(sourceT), targetT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		//Specialmove En passant 
		if(pMove instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if(pMove.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow()+1, target.getColumn());
				}else {
					pawnPosition = new Position(target.getRow()-1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece; //retorna a peça capturada 
	}
	
	public void undoMove(Position source, Position target, Piece captured) {//ira desfazer o movimento feito 
		ChessPiece piece = (ChessPiece)board.removePiece(target);//pega a peça colocada na posição de destino 
		board.placePiece(piece, source); //desolve a peça para posição de origem
		piece.decreasementMoveCount();
		
		if (captured != null) {//se uma peça foi capiturada no movimento 
			board.placePiece(captured, target); //colocamos a peça o lugar de volta 
			capturedPieces.remove(captured);    //removemos ela da lista de peças capiturada 
			piecesOnTheBoard.add(captured);     //colocamos de volta ela na lista do tabuleiro 
		}
		//movimento especial rock pequeno
		if (piece instanceof King && target.getColumn() == source.getColumn()+2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()+3);
			Position targetT = new Position(source.getRow(), source.getColumn()+1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			//board.placePiece(board.removePiece(sourceT), targetT);
			board.placePiece(rook, sourceT);
			rook.decreasementMoveCount();
		}
		
		if (piece instanceof King && target.getColumn() == source.getColumn()-2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()-4);
			Position targetT = new Position(source.getRow(), source.getColumn()-1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			//board.placePiece(board.removePiece(sourceT), targetT);
			board.placePiece(rook, sourceT);
			rook.decreasementMoveCount();
		}
		//Specialmove En passant 
		if(piece instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && captured == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.piece(target);
				Position pawnPosition;
				if(piece.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}	
		}
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
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	//método para retorna o rei de uma determinada cor 
	private ChessPiece theKing(Color color) {
		List<Piece> listAux = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece aux : listAux) {
			if (aux instanceof King) {
				return (ChessPiece)aux;
			}
		}
		throw new IllegalStateException("There is no "+color+" king on the board");
	}
	
	private boolean testCheck(Color color) {
		/* procura o rei na lista de peças no tabuleeiro:
		 * retorna o objeto chessposition:connverte de chesspositon para position */
		Position kingPosition = theKing(color).getChessPosition().toPosition();//pegar a posição do rei
		//filtra da PiecesOnTheBoard as peças oponentes dela
		/*criar um lista de oponenste a parte das peças do tabulero*/
		List<Piece> opponentsPieces = piecesOnTheBoard.stream().filter(x-> ((ChessPiece)x).
				getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece auxPiece : opponentsPieces) { //caminha por toda lista de oponentes 
			boolean[][] mat = auxPiece.possibleMoves(); //matriz de posibilidades
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {//se a posição do rei for true 
				return 	true;
			}
		}
		return false;
	}
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {//apos fazer essse movimento a peça não estara em check
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
             
	}

	
}
