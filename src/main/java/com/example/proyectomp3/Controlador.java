package com.example.proyectomp3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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


public class Controlador {
    private static String[] canciones;
    @FXML
    private Label songname;
    @FXML
    private Label artist;
    @FXML
    private ImageView cover;
    private int numeroCancion = 0;
    private String cancion = carpetaMusica+canciones[numeroCancion];
    private boolean estaReproduciendo = false;
    private Media cancionReproducida;
    private MediaPlayer reproductor;

    @FXML
    private void reproducir() {
        if (!estaReproduciendo){
            recorrerAtributosCancion(cancion);
            cancionReproducida = new Media(new File(cancion).toURI().toString());
            reproductor = new MediaPlayer(cancionReproducida);
            reproductor.play();
            estaReproduciendo = true;
        }
    }

    public static void recorrerMusica(String carpeta){
        File carpetaMusica = new File(carpeta);
        canciones = carpetaMusica.list();
        for (int i = 0; i < canciones.length; i++) {
            System.out.println(canciones[i]);
        }
    }
    private void recorrerAtributosCancion(String cancion) {
        File archivo = new File(cancion);
        try {
            AudioFile f = AudioFileIO.read(archivo);
            Tag tag = f.getTag();
            String nombre = tag.getFirst(FieldKey.TITLE);
            String artista = tag.getFirst(FieldKey.ARTIST);
            //comprobar titulo cancion
            if (!nombre.equals("")) songname.setText(nombre);
            else songname.setText("Sin título");
            //comprobar artista cancion
            if (!artista.equals("")) artist.setText(artista);
            else artist.setText("Artista desconocido");
            //comprobar cover
            if (tag.getFirst(FieldKey.COVER_ART).equals("")){
                BufferedImage bImage = ImageIO.read(new File("sample.jpg"));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos);
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
        estaReproduciendo = false;
        reproducir();
    }
    @FXML
    private void retroceder() {
        numeroCancion--;
        if (numeroCancion < 0) numeroCancion = canciones.length-1;
        cancion = carpetaMusica+canciones[numeroCancion];
        reproductor.stop();
        estaReproduciendo = false;
        reproducir();
    }
}