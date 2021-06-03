package boxgame.javafx.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class Piece {

    private ReadOnlyObjectWrapper<PieceType> type = new ReadOnlyObjectWrapper<>();
    public PieceType getType() {
        return type.get();
    }
    public ReadOnlyObjectProperty<PieceType> typeProperty() {
        return type.getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Position> position = new ReadOnlyObjectWrapper<>();

    public Position getPosition() {
        return position.get();
    }
    public ReadOnlyObjectProperty<Position> positionProperty() {
        return position.getReadOnlyProperty();
    }

    public Piece(PieceType type, Position position) {
        this.type.set(type);
        this.position.set(position);
    }

    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    public String toString() {
        return type.toString() + position.get().toString();
    }

}
