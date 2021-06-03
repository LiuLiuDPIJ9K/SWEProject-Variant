package boxgame.javafx.controller;

import java.util.ArrayList;
import java.util.List;

import boxgame.javafx.model.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

public class BoxGameController {

    private enum MoveCircle {
        MOVE_FROM,
        MOVE_TO;

        public MoveCircle alter() {
            return switch (this) {
                case MOVE_FROM -> MOVE_TO;
                case MOVE_TO -> MOVE_FROM;
            };
        }
    }

    private MoveCircle moveCircle = MoveCircle.MOVE_FROM;

    @FXML
    private GridPane board;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpButton;

    @FXML
    private Label stepsLabel;

    private BoxGameModel model = new BoxGameModel();

    private final List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private IntegerProperty steps = new SimpleIntegerProperty();

    @FXML
    private void initialize() {
        createBoard();
        createPieces();
        setSelectablePositions();
        showSelectablePositions();
        steps.set(0);
        stepsLabel.textProperty().bind(steps.asString());
    }

    private void createBoard() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = new Circle(30);
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
            if (i % 2 == 0) {
                piece.setFill(Color.RED);
            } else {
                piece.setFill(Color.BLACK);
            }
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var col = GridPane.getColumnIndex(square);
        System.out.printf("Click on square %d and %d\n", col, col+1);
        var position = new Position(col);
        handleClickOnSquare(position);
    }

    private void handleClickOnSquare(Position position) {
        switch (moveCircle) {
            case MOVE_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterMoveCircle();
                }
            }
            case MOVE_TO -> {
                if (selectablePositions.contains(position)) {
                    var pieceNumber = model.getPieceNumber(selected).getAsInt();
                    var direction = Direction.of(position.col() - selected.col());
                    //Logger.debug("Moving piece {} {}", pieceNumber, direction);
                    model.move(pieceNumber, direction);
                    deselectPosition();
                    alterMoveCircle();
                    steps.set(steps.get() + 1);
                }
            }
        }

    }

    /*public void move(int pieceNumber, Direction direction) {
        int i = pieceNumber;
        for(int j = 0; j < board.getColumnCount(); j++) {
            if(!(getSquare(selectablePositions.get(i+2)).getChildren().contains(pieceNumber))
            && !(getSquare(selectablePositions.get(i+3)).getChildren().contains(pieceNumber))) {
                model.pieces[i].moveTo(direction);
                model.pieces[i+1].moveTo(direction);
            } else {
                System.out.println("not movable");
            }
        }
    }*/

    private void alterMoveCircle() {
        moveCircle = moveCircle.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    private void deselectPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (moveCircle) {
            case MOVE_FROM -> selectablePositions.addAll(model.getPiecePositions());
            case MOVE_TO -> {
                var pieceNumber = model.getPieceNumber(selected).getAsInt();
                for (var direction : model.getValidMoves(pieceNumber)) {
                    selectablePositions.add(selected.moveTo(direction));
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

    public void handleResetButton(ActionEvent actionEvent) {
        Logger.info("Resetting Game");
        moveCircle = MoveCircle.MOVE_FROM;
        board.getChildren().clear();
        model = new BoxGameModel();
        initialize();
    }

}
