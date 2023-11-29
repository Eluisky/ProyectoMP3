package com.example.proyectomp3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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
import javafx.scene.media.MediaPlayer;
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


public class Controlador implements Initializable {
    private static String[] canciones;
    @FXML
    private Label nombreCancion;
    @FXML
    private Label artista;
    @FXML
    private ImageView cover;
    @FXML
    private ProgressBar barra = new ProgressBar(0.0);
    private static  int numeroCancion = 0;
    private static String cancion;
    private boolean estaReproduciendo = false;
    private Media cancionReproducida;
    private MediaPlayer reproductor;
    private int duracionCancion = 0;
    @FXML
    private  Label etiquetaDuracionCancion;
    private Timeline timeline;
    @FXML
    public Label tiempoAvanzado;
    int segundos = 0;
    int minutos = 0;

    public Controlador() {

    }

    @FXML
    private void reproducir() {
                if (canciones.length == 0) bibliotecaVacia();
                else{
                    cancion = carpetaMusica+canciones[numeroCancion];
                    if (!estaReproduciendo){
                       try{
                           recorrerAtributosCancion(cancion);
                           if (cancionReproducida!= null && reproductor!=null) throw new CancionYaReproduciendose("");
                           cancionReproducida = new Media(new File(cancion).toURI().toString());
                           reproductor = new MediaPlayer(cancionReproducida);
                       }catch (CancionYaReproduciendose e){

                       }
                        reproductor.play();
                        barraDuracion(cancion);
                        estaReproduciendo = true;
                    }
                }
                Main.reproducción.start();
    }
    @FXML
    public void pausar(){
        reproductor.pause();
        timeline.pause();
        estaReproduciendo = false;
        /*try {
            Main.reproducción.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void recorrerMusica(String carpeta){
        File carpetaMusica = new File(carpeta);
            canciones = carpetaMusica.list();
            if (canciones != null){
                for (int i = 0; i < canciones.length; i++) {
                    System.out.println(canciones[i]);
                }
            }
    }
    private void bibliotecaVacia(){
            cancion = "";
            nombreCancion.setText("La biblioteca");
            artista.setText("está vacía");
    }
    private void recorrerAtributosCancion(String cancion) {
        File archivo = new File(cancion);
        try {
            AudioFile f = AudioFileIO.read(archivo);
            Tag tag = f.getTag();
            String nombre = tag.getFirst(FieldKey.TITLE);
            String artistaPrincipal = tag.getFirst(FieldKey.ARTIST);
            //comprobar titulo cancion
            if (!nombre.equals("")) nombreCancion.setText(nombre);
            else nombreCancion.setText("Sin título");
            //comprobar artista cancion
            if (!artistaPrincipal.equals("")) artista.setText(artistaPrincipal);
            else artista.setText("Artista desconocido");
            //comprobar cover
            if (tag.getFirst(FieldKey.COVER_ART).equals("")){
                BufferedImage bImage = ImageIO.read(new File("sample.png"));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", bos);
                byte [] data = bos.toByteArray();
                Image img = new Image(new ByteArrayInputStream(data));
                cover.setImage(img);
                cover.setFitHeight(200);
                cover.setFitWidth(200);
            }
            else{
                byte[] b = tag.getFirstArtwork().getBinaryData();
                Image img = new Image(new ByteArrayInputStream(b));
                cover.setImage(img);
                cover.setFitHeight(200);
                cover.setFitWidth(200);
            }

        } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
        }

    }
    @FXML
    private void avanzar() {
        numeroCancion++;
        if (numeroCancion == canciones.length) numeroCancion = 0;
        cancion = carpetaMusica+canciones[numeroCancion];
        reproductor.stop();
        //Vaciamos el reproductor con null
        reproductor = null;
        cancionReproducida = null;
        estaReproduciendo = false;
        tiempoAvanzado.setText("0:00");
        timeline.stop();
        barra.setProgress(0);
        reproducir();
    }
    @FXML
    private void retroceder() {
        numeroCancion--;
        if (numeroCancion < 0) numeroCancion = canciones.length-1;
        cancion = carpetaMusica+canciones[numeroCancion];
        reproductor.stop();
        //Vaciamos el reproductor con null
        reproductor = null;
        cancionReproducida = null;
        estaReproduciendo = false;
        tiempoAvanzado.setText("0:00");
        timeline.stop();
        barra.setProgress(0);
        reproducir();
    }

    public void barraDuracion(String cancion){
        File archivo = new File(cancion);
        try {
            AudioFile audioFile = AudioFileIO.read(archivo);
            duracionCancion = audioFile.getAudioHeader().getTrackLength();
            etiquetaDuracionCancion.setText(comprobarDuracionCancion(duracionCancion));
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), event -> {
                        // Inicialización de la animación, se tiene que ir actualizacion con cada bucle
                        //marcado en setCycleCount, que es la duración de la canción
                        barra.setProgress(barra.getProgress()+0.0);
                    }),
                    new KeyFrame(Duration.seconds(1), event -> {
                        // Incremento de la ProgressBar cada segundo
                        //Se divide entre la duracion de la cancion para que cada segundo muestre el porcentaje
                        //que llevaria la canción en la barra
                        barra.setProgress(barra.getProgress() + 1.0 / duracionCancion);
                    }));
            timeline.setCycleCount(duracionCancion);
            timeline.play();
        }
         catch (CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException | IOException e) {
            e.getMessage();
        }
    }
    private static String comprobarDuracionCancion(int duracion){
        int minutos = 0;
        int segundos = 0;
        String tiempoTotal = "";
        while(duracion>0){
            if (duracion-60>=0){
                minutos++;
                duracion-=60;
            }
            else if (duracion-60<0){
                segundos = duracion;
                duracion-=60;
            }
        }
        if (segundos<10){
            tiempoTotal="/"+minutos+":0"+segundos;
        }
        else tiempoTotal="/"+minutos+":"+segundos;
        return tiempoTotal;
    }
    public synchronized void sumarTiempo(){
        String tiempo;
        try {
            if (segundos<10){
                tiempo=minutos+":0"+segundos;
            }
            else {
                tiempo=minutos+":"+segundos;
            }
            segundos++;
            if (segundos==60){
                minutos++;
                segundos=0;
            }
            tiempoAvanzado.setText(tiempo);
            Thread.sleep(1000);
        } catch (NullPointerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label tiempoAvanzado1 = this.tiempoAvanzado;
    }
}