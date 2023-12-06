package com.example.proyectomp3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ControladorMaster {
    public static Timeline timeline;
    public static String carpetaMusica = "music\\";
    public static String cancion;
    public static int numeroCancion = 0;
    public static String[] canciones;

    public static String[] recorrerMusica(){
        File carpeta = new File(carpetaMusica);
        canciones = carpeta.list();
        if (canciones != null){
            for (int i = 0; i < canciones.length; i++) {
                System.out.println(canciones[i]);
            }
        }
        return canciones;
    }

    public static void recorrerAtributosCancion(String cancion,javafx.scene.control.Label nombreCancion, javafx.scene.control.Label artista, ImageView cover) {
        String nombre;
        String artistaPrincipal;
        try {
            File archivo = new File(cancion);
            AudioFile f = AudioFileIO.read(archivo);
            Tag tag = f.getTag();
             nombre = tag.getFirst(FieldKey.TITLE);
             artistaPrincipal = tag.getFirst(FieldKey.ARTIST);
            //comprobar titulo cancion
            if (!nombre.equals("")) {
                nombreCancion.setText(nombre);
            }
            else{
                nombreCancion.setText("Sin título");
            }
            //comprobar artista cancion
            if (!artistaPrincipal.equals("")){
                artista.setText(artistaPrincipal);
            }
            else{
                artista.setText("Artista desconocido");
            }
            //comprobar cover
            if (tag.getFirst(FieldKey.COVER_ART).equals("")) {
                BufferedImage bImage = ImageIO.read(new File("sample.png"));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", bos);
                byte[] data = bos.toByteArray();
                Image img = new Image(new ByteArrayInputStream(data));
                cover.setImage(img);
                cover.setFitHeight(200);
                cover.setFitWidth(200);
            } else {
                byte[] b = tag.getFirstArtwork().getBinaryData();
                Image img = new Image(new ByteArrayInputStream(b));
                cover.setImage(img);
                cover.setFitHeight(200);
                cover.setFitWidth(200);
            }

        } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | NullPointerException |
                 InvalidAudioFrameException e) {
        }

    }
    public static void barraDuracion(String cancion, ProgressBar barra, javafx.scene.control.Label etiquetaDuracionCancion ) {
        int duracionCancion;
        File archivo = new File(cancion);
        try {
            AudioFile audioFile = AudioFileIO.read(archivo);
            duracionCancion = audioFile.getAudioHeader().getTrackLength();
            etiquetaDuracionCancion.setText(comprobarDuracionCancion(duracionCancion));
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), event -> {
                        // Inicialización de la animación, se tiene que ir actualizacion con cada bucle
                        //marcado en setCycleCount, que es la duración de la canción
                        barra.setProgress(barra.getProgress() + 0.0);
                    }),
                    new KeyFrame(Duration.seconds(1), event -> {
                        // Incremento de la ProgressBar cada segundo
                        //Se divide entre la duracion de la cancion para que cada segundo muestre el porcentaje
                        //que llevaria la canción en la barra
                        barra.setProgress(barra.getProgress() + 1.0 / duracionCancion);
                    }));
            timeline.setCycleCount(duracionCancion);
            timeline.play();
        } catch (CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException |
                 IOException e) {
            e.getMessage();
        }
    }
    public static String comprobarDuracionCancion(int duracion) {
        int minutos = 0;
        int segundos = 0;
        String tiempoTotal = "";
        while (duracion > 0) {
            if (duracion - 60 >= 0) {
                minutos++;
                duracion -= 60;
            } else if (duracion - 60 < 0) {
                segundos = duracion;
                duracion -= 60;
            }
        }
        if (segundos < 10) {
            tiempoTotal = minutos + ":0" + segundos;
        } else {
            tiempoTotal = minutos + ":" + segundos;
        }
        return tiempoTotal;
    }

}
