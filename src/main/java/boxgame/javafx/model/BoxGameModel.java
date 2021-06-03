package boxgame.javafx.model;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;

import java.util.*;

public class BoxGameModel {

    public static int BOARD_COLUMN = 16;

    //private final ReadOnlyObjectWrapper<Piece>[][] board = new ReadOnlyObjectWrapper[1][BOARD_COLUMN];

    public final Piece[] pieces;


    public BoxGameModel() {
        this(new Piece(PieceType.RED1, new Position( 0)),
                new Piece(PieceType.BLACK1, new Position(1)),
                new Piece(PieceType.RED2, new Position(2)),
                new Piece(PieceType.BLACK2, new Position(3)),
                new Piece(PieceType.RED3, new Position(4)),
                new Piece(PieceType.BLACK3, new Position(5)));
    }

    public BoxGameModel(Piece... pieces) {
        this.pieces = pieces;
        checkPieces(pieces);
    }

    public int getPieceCount() {
        return pieces.length;
    }

    public Position getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    public PieceType getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    public ReadOnlyObjectProperty<Position> positionProperty(int pieceNumber) {
        return pieces[pieceNumber].positionProperty();
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.col() && position.col() < BOARD_COLUMN;
    }

    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (! isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                Logger.debug("Not on board!");
            }
            seen.add(piece.getPosition());
        }
    }

    public boolean isValidMove(int pieceNumber, Direction direction) {
        if (pieceNumber < 0 || pieceNumber >= pieces.length) {
            Logger.debug("Invalid piece!");
        }
        Position newPosition = pieces[pieceNumber].getPosition().moveTo(direction);
        if (! isOnBoard(newPosition)) {
            return false;
        }
        for (var piece : pieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    public Set<Direction> getValidMoves(int pieceNumber) {
        EnumSet<Direction> validMoves = EnumSet.noneOf(Direction.class);
        for (var direction : Direction.values()) {
            if (isValidMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /*public boolean isSquareEmpty(int pieceNumber, Position position) {
        //EnumSet<PieceType> empty = EnumSet.noneOf(PieceType.class);
        if(!(getPieceType(pieceNumber).toString().isEmpty())) {
            return true;
        }
        return false;
    }*/

    public void move(int pieceNumber, Direction direction) {
        int i = pieceNumber;
        /*PieceType piece_type = getPieceType(i);
        if(piece_type.toString().contains("RED")) {
            if(isSquareEmpty(getPiecePosition(i)) && isSquareEmpty(getPiecePosition(i+1))) {
                pieces[i].moveTo(direction);
                pieces[i+1].moveTo(direction);
            }
        } else {
            if(isSquareEmpty(getPiecePosition(i)) && isSquareEmpty(getPiecePosition(i-1))) {
                pieces[i].moveTo(direction);
                pieces[i-1].moveTo(direction);
            }
        }*/
        /*int j = pieceNumber;
        System.out.println(j);
        System.out.println(pieceNumber);
        System.out.println(getPiecePositions());
        System.out.println(getPiecePosition(j));
        System.out.println(getPiecePosition(j+1));
        System.out.println(isSquareEmpty(j,getPiecePosition(j+1)));
        if(isSquareEmpty(j,getPiecePosition(j+1)) && isSquareEmpty(j+1,getPiecePosition(j+2))) {
            pieces[j].moveTo(direction);
            pieces[j+1].moveTo(direction);
        }*/
        /*pieces[i].moveTo(direction);
        for(int j = i+1; j < BOARD_COLUMN; j++) {

            pieces[j].moveTo(direction);
            break;
        }*/
        /*for(int i = 0; i < BOARD_COLUMN; i++) {
            System.out.println(i);
            if(isSquareEmpty(getPiecePosition(j)) && isSquareEmpty(getPiecePosition(j+1))
                    && !isSquareEmpty(getPiecePosition(i))) {
                pieces[j].moveTo(direction);
                pieces[i].moveTo(direction);
            }
        }*/
        pieces[i].moveTo(direction);
        pieces[i+1].moveTo(direction);
    }

    public List<Position> getPiecePositions() {
        List<Position> positions = new ArrayList<>(pieces.length);
        for (var piece : pieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    public OptionalInt getPieceNumber(Position position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : pieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }

}
