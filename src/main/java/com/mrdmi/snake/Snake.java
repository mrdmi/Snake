package com.mrdmi.snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class Snake extends Application {
    private static final Color FIELD_COLOR = Color.LIGHTGRAY;
    private static final int FRAME_TIME = 1000;

    public enum Direction {
        LEFT, DOWN, RIGHT, UP
    }

    private static final int PIXEL_SIZE = 40;
    private static final int HORIZONTAL_PIXEL_COUNT = 15;
    private static final int VERTICAL_PIXEL_COUNT = 10;
    private static final Rectangle[][] FIELD = new Rectangle[VERTICAL_PIXEL_COUNT][HORIZONTAL_PIXEL_COUNT];
    private Deque<Rectangle> snake = new ArrayDeque<>();
    private Deque<Direction> keystrokeStack = new ArrayDeque<>();
    private int xHeadPos = 3;
    private int yHeadPos = 1;
    private static final Color BODY_COLOR = Color.BLACK;
    private static final Color HEAD_COLOR = Color.DARKGRAY;
    private static final Color FOOD_COLOR = Color.RED;
    private Timeline timeline;


    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createField());
        paintSnake();
        play();
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP -> {
                    if (keystrokeStack.getLast() != Direction.DOWN)
                        keystrokeStack.add(Direction.UP);
                }
                case DOWN -> {
                    if (keystrokeStack.getLast() != Direction.UP)
                        keystrokeStack.add(Direction.DOWN);
                }
                case LEFT -> {
                    if (keystrokeStack.getLast() != Direction.RIGHT)
                        keystrokeStack.add(Direction.LEFT);
                }
                case RIGHT -> {
                    if (keystrokeStack.getLast() != Direction.LEFT)
                        keystrokeStack.add(Direction.RIGHT);
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private void play() {
        keystrokeStack.add(Direction.RIGHT);

        timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_TIME), event -> {

            snake.getLast().setFill(BODY_COLOR);

            if (keystrokeStack.size() > 1)
                keystrokeStack.removeFirst();

            switch (keystrokeStack.getFirst()) {
                case UP -> {
                    if (yHeadPos == 0) {
                        yHeadPos = VERTICAL_PIXEL_COUNT - 1;
                    } else {
                        yHeadPos--;
                    }
                }
                case LEFT -> {
                    if (xHeadPos == 0) {
                        xHeadPos = HORIZONTAL_PIXEL_COUNT - 1;
                    } else {
                        xHeadPos--;
                    }
                }
                case DOWN -> {
                    if (yHeadPos == VERTICAL_PIXEL_COUNT - 1) {
                        yHeadPos = 0;
                    } else {
                        yHeadPos++;
                    }
                }
                case RIGHT -> {
                    if (xHeadPos == HORIZONTAL_PIXEL_COUNT - 1) {
                        xHeadPos = 0;
                    } else {
                        xHeadPos++;
                    }
                }
            }

            var pixel = FIELD[yHeadPos][xHeadPos];
            snake.getFirst().setFill(FIELD_COLOR);
            snake.removeFirst();
            pixel.setFill(HEAD_COLOR);
            if (snake.contains(pixel))
                timeline.stop();
            snake.add(pixel);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void paintSnake() {

        snake.add(FIELD[1][1]);
        snake.add(FIELD[1][2]);
        snake.add(FIELD[1][3]);

        for (var segment : snake) {
            segment.setFill(BODY_COLOR);
        }
        snake.getLast().setFill(HEAD_COLOR);
    }

    private Parent createField() {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < VERTICAL_PIXEL_COUNT; i++) {
            for (int j = 0; j < HORIZONTAL_PIXEL_COUNT; j++) {
                var pixel = new Rectangle(PIXEL_SIZE, PIXEL_SIZE);
                pixel.setFill(Color.LIGHTGRAY);
                FIELD[i][j] = pixel;
                gridPane.add(pixel, j, i);
            }
        }
        return gridPane;
    }

    public static void main(String[] args) {
        launch();
    }
}
