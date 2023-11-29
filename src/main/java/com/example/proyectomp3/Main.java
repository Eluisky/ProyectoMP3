package com.example.proyectomp3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {
    public static String carpetaMusica = "music\\";
    public static Controlador controlador;
    public static Reproducción reproducción = new Reproducción(controlador);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mp3Visual.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);

        controlador = fxmlLoader.getController();
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:ico.png"));
        stage.setTitle("Reproductor MP3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        controlador.recorrerMusica(carpetaMusica);
        launch();
    }
}