package com.example.proyectomp3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import javax.imageio.ImageIO;



public class Controlador {
    @FXML
    public  Label nombreCancion;
    @FXML
    public  Label artista;
    @FXML
    public  ImageView cover;
    @FXML
    private ProgressBar barra;

    private boolean estaReproduciendo = false;
    private Media cancionReproducida;
    private MediaPlayer reproductor;
    private boolean avanzar;
    private boolean retroceder;
    @FXML
    public  Label etiquetaDuracionCancion;
    @FXML
    public Label tiempoAvanzado;

    /*  @FXML
      public Label tiempoAvanzado;
      private  int segundos = 0;
      private  int minutos = 0;*/
    public Reproducción reproducción = new Reproducción(tiempoAvanzado);


    @FXML
    private void reproducir() {
        ControladorMaster.canciones = ControladorMaster.recorrerMusica();
        if (ControladorMaster.canciones.length == 0) bibliotecaVacia();
        else {
            ControladorMaster.cancion = ControladorMaster.carpetaMusica + ControladorMaster.canciones[ControladorMaster.numeroCancion];
            if (!estaReproduciendo) {
                try {
                    ControladorMaster.recorrerAtributosCancion(ControladorMaster.cancion,nombreCancion,artista,cover);
                    if (cancionReproducida != null && reproductor != null) throw new CancionYaReproduciendose("");
                    cancionReproducida = new Media(new File(ControladorMaster.cancion).toURI().toString());
                    reproductor = new MediaPlayer(cancionReproducida);
                } catch (CancionYaReproduciendose e) {
                }
                catch (MediaException e){
                    if(avanzar) avanzar();
                    else if (retroceder) retroceder();
                }
                finally {
                    if (ControladorMaster.timeline != null) {
                        ControladorMaster.timeline.stop();
                    }
                    barra.setProgress(0.0);
                }
                reproductor.play();
                       /* try {
                            Platform.runLater(() -> this.tiempoAvanzado.setText(sumarTiempo()));
                            Thread.sleep(35);
                        } catch (Exception ex) {
                        }*/
                //reproducción.start();
                ControladorMaster.barraDuracion(ControladorMaster.cancion,barra,etiquetaDuracionCancion);
                estaReproduciendo = true;
            }
        }
    }

    @FXML
    public void pausar() {
        reproductor.pause();
        ControladorMaster.timeline.pause();
        estaReproduciendo = false;
        /*try {
            Main.reproducción.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
    }

    private void bibliotecaVacia() {
        ControladorMaster.cancion = "";
        nombreCancion.setText("La biblioteca");
        artista.setText("está vacía");
    }



    @FXML
    private void avanzar() {
        //reproducción.interrupt();
        ControladorMaster.numeroCancion++;
        if (ControladorMaster.numeroCancion == ControladorMaster.canciones.length) ControladorMaster.numeroCancion = 0;
        ControladorMaster.cancion = ControladorMaster.carpetaMusica + ControladorMaster.canciones[ControladorMaster.numeroCancion];
        retroceder = false;
        avanzar = true;
       try{
           reproductor.stop();
           //Vaciamos el reproductor con null
           reproductor = null;
           cancionReproducida = null;
           estaReproduciendo = false;

           //tiempoAvanzado.setText("0:00");
           reproducir();
       }
       catch (NullPointerException e){
           reproducir();
       }

    }

    @FXML
    private void retroceder() {
        ControladorMaster.numeroCancion--;
        if (ControladorMaster.numeroCancion < 0) ControladorMaster.numeroCancion = ControladorMaster.canciones.length - 1;
        ControladorMaster.cancion = ControladorMaster.carpetaMusica + ControladorMaster.canciones[ControladorMaster.numeroCancion];
        retroceder = true;
        avanzar = false;
        try{
            reproductor.stop();
            //Vaciamos el reproductor con null
            reproductor = null;
            cancionReproducida = null;
            estaReproduciendo = false;

            //tiempoAvanzado.setText("0:00");
            reproducir();
        }
        catch (NullPointerException e){
            reproducir();
        }
    }



    @FXML
    public void abrirVentanaDetalles() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaCancion.fxml"));
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(fxmlLoader.load(), 300, 500);
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:ico.png"));
            stage.setTitle("Detalles de la canción");
            ControladorVentanaCancion controlador = fxmlLoader.getController();
            controlador.atributosCancion();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void abrirBiblioteca() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Biblioteca.fxml"));
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(fxmlLoader.load(), 300, 500);
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:ico.png"));
            stage.setTitle("Biblioteca");
            Biblioteca controlador = fxmlLoader.getController();
            controlador.leerCanciones();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}