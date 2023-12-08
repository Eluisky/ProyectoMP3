package com.example.proyectomp3;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class Biblioteca {
    @FXML
    private ImageView cover;
    @FXML
    private  Label nombreCancion;
    @FXML
    private  Label artista;
    @FXML
    private  Label duracion;

    public HBox leerCanciones(){
        ControladorMaster.recorrerMusica();
        String[] listaCanciones = ControladorMaster.canciones;
        HBox h = new HBox();
        h.setAlignment(Pos.CENTER);
        for (int i = 0; i < listaCanciones.length; i++) {
            ControladorMaster.recorrerAtributosCancion(listaCanciones[i],nombreCancion,artista,cover);
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            v.getChildren().add(cover);
            v.getChildren().add(artista);
            v.getChildren().add(nombreCancion);
            try{
                File archivo = new File(ControladorMaster.cancion);
                AudioFile audioFile = AudioFileIO.read(archivo);
                int duracionCancion = audioFile.getAudioHeader().getTrackLength();
                duracion.setText(ControladorMaster.comprobarDuracionCancion(duracionCancion));
            }catch (IOException | CannotReadException | TagException | ReadOnlyFileException | NullPointerException |
                    InvalidAudioFrameException e) {
            }
            v.getChildren().add(duracion);
            h.getChildren().add(v);

        }
        return h;
    }
}
