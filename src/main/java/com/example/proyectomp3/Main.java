package com.example.proyectomp3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {



    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mp3Visual.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:ico.png"));
        stage.setTitle("Reproductor MP3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}