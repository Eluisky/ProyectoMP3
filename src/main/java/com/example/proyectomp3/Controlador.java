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

import static com.example.proyectomp3.Main.carpetaMusica;
import static com.example.proyectomp3.Main.listaCanciones;;


public class Controlador {
    private static String[] canciones = listaCanciones;
    @FXML
    public  Label nombreCancion;
    @FXML
    public  Label artista;
    @FXML
    public  ImageView cover;
    @FXML
    private ProgressBar barra;
    private static int numeroCancion = 0;
    private  String cancion;
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
        if (canciones.length == 0) bibliotecaVacia();
        else {
            cancion = carpetaMusica + canciones[numeroCancion];
            if (!estaReproduciendo) {
                try {
                    ControladorMaster.recorrerAtributosCancion(cancion,nombreCancion,artista,cover);
                    if (cancionReproducida != null && reproductor != null) throw new CancionYaReproduciendose("");
                    cancionReproducida = new Media(new File(cancion).toURI().toString());
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
                ControladorMaster.barraDuracion(cancion,barra,etiquetaDuracionCancion);
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
        cancion = "";
        nombreCancion.setText("La biblioteca");
        artista.setText("está vacía");
    }



    @FXML
    private void avanzar() {
        //reproducción.interrupt();
        numeroCancion++;
        if (numeroCancion == canciones.length) numeroCancion = 0;
        cancion = carpetaMusica + canciones[numeroCancion];
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
        numeroCancion--;
        if (numeroCancion < 0) numeroCancion = canciones.length - 1;
        cancion = carpetaMusica + canciones[numeroCancion];
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
            //ControladorVentanaCancion.atributosCancion();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaCancion.fxml"));
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(fxmlLoader.load(), 300, 500);
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:ico.png"));
            stage.setTitle("Detalles de la canción");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}