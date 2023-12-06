package com.example.proyectomp3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;


public class ControladorVentanaCancion {
    @FXML
    private  ImageView cover;
    @FXML
    private  Label nombreCancion;
    @FXML
    private  Label artista;
    @FXML
    private  Label duracion;


    public void atributosCancion(){
        ControladorMaster.recorrerAtributosCancion(ControladorMaster.cancion,nombreCancion,artista,cover);
        nombreCancion.setText("Nombre: "+nombreCancion.getText());
        artista.setText("Artista: "+artista.getText());
        try{
            File archivo = new File(ControladorMaster.cancion);
            AudioFile audioFile = AudioFileIO.read(archivo);
            int duracionCancion = audioFile.getAudioHeader().getTrackLength();
            duracion.setText("Duración: "+ControladorMaster.comprobarDuracionCancion(duracionCancion));
        }catch (IOException | CannotReadException | TagException | ReadOnlyFileException | NullPointerException |
                InvalidAudioFrameException e) {
            nombreCancion.setText("No hay ninguna");
            artista.setText("Cancion reproduciendose");
        }
    }
}
