package com.example.ormcoursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.NumberFormat;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/sign-in.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        enableWindowDragging(stage, root);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        applyRoundedCorners(root, 20);
        stage.show();
    }


    private void applyRoundedCorners(Parent root, double radius) {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(radius * 2);
        clip.setArcHeight(radius * 2);

        root.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        root.setClip(clip);
    }


    private void enableWindowDragging(Stage stage, Parent root) {

        final double[] offsetX = {0};
        final double[] offsetY = {0};

        root.setOnMousePressed(event -> {
            offsetX[0] = event.getSceneX();
            offsetY[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX[0]);
            stage.setY(event.getScreenY() - offsetY[0]);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}