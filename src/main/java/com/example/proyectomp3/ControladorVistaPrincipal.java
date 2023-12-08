package com.example.proyectomp3;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class ControladorVistaPrincipal {
    @FXML
    public Label nombreCancion;
    @FXML
    public Label artista;
    @FXML
    public ImageView cover;
    @FXML
    private ProgressBar barra;

    private boolean estaReproduciendo = false;
    private Media cancionReproducida;
    private MediaPlayer reproductor;
    private boolean avanzar;
    private boolean retroceder;
    @FXML
    public Label etiquetaDuracionCancion;
    @FXML
    public Label tiempoAvanzado;
    private int segundos = 0;
    private int minutos = 0;
    @FXML
    private de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView iconoBucleUno;
    @FXML
    private de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView iconoBucleInfinito;
    @FXML
    private de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView iconoAleatorio;
    @FXML
    private de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView iconoPlay;
    @FXML
    private de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView iconoPausa;
    private boolean esAleatorio = false;
    private boolean comprobarBucleUnaVez = false;
    private boolean comprobarBucleInfinito = false;

    @FXML
    private void reproducir() {
        iconoPlay.setFill(Color.GREEN);
        iconoPausa.setFill(Color.WHITE);
        if (ControladorMaster.canciones.length == 0) bibliotecaVacia();
        else {
            ControladorMaster.cancion = ControladorMaster.carpetaMusica + ControladorMaster.canciones[ControladorMaster.numeroCancion];
            if (!estaReproduciendo) {
                try {
                    ControladorMaster.recorrerAtributosCancion(ControladorMaster.cancion, nombreCancion, artista, cover);
                    if (cancionReproducida != null && reproductor != null) throw new CancionYaReproduciendose("");
                    cancionReproducida = new Media(new File(ControladorMaster.cancion).toURI().toString());
                    reproductor = new MediaPlayer(cancionReproducida);
                } catch (CancionYaReproduciendose e) {
                } catch (MediaException e) {
                    if (avanzar) avanzar();
                    else if (retroceder) retroceder();
                } finally {
                    if (ControladorMaster.timeline != null) {
                        ControladorMaster.timeline.stop();
                    }
                    barra.setProgress(0.0);
                }
                reproductor.play();
                ControladorMaster.barraDuracion(ControladorMaster.cancion, barra, etiquetaDuracionCancion);
                estaReproduciendo = true;

               /* Reproducción r = new Reproducción(tiempoAvanzado);
                tiempoAvanzado = r.tiempoAvanzado;
                r.start();*/
            }
        }
    }

    @FXML
    public void pausar() {
        iconoPlay.setFill(Color.WHITE);
        iconoPausa.setFill(Color.GREEN);
        reproductor.pause();
        ControladorMaster.timeline.pause();
        estaReproduciendo = false;
    }

    private void bibliotecaVacia() {
        ControladorMaster.cancion = "";
        nombreCancion.setText("La biblioteca");
        artista.setText("está vacía");
    }

    @FXML
    private void bucleUnaVez() {
        if (!comprobarBucleUnaVez)  comprobarBucleUnaVez = true;
        else comprobarBucleUnaVez = false;

        if (iconoBucleUno.getFill().equals(Color.WHITE)) iconoBucleUno.setFill(Color.GREEN);
        else iconoBucleUno.setFill(Color.WHITE);
        iconoBucleInfinito.setFill(Color.WHITE);
        comprobarBucleInfinito = false;

    }
    @FXML
    private void bucleInfinito() {
        if (!comprobarBucleInfinito)  comprobarBucleInfinito = true;
        else comprobarBucleInfinito = false;
        if (iconoBucleInfinito.getFill().equals(Color.WHITE))
            iconoBucleInfinito.setFill(Color.GREEN);
        else iconoBucleInfinito.setFill(Color.WHITE);
        iconoAleatorio.setFill(Color.WHITE);
        esAleatorio = false;
        iconoBucleUno.setFill(Color.WHITE);
        comprobarBucleUnaVez = false;

    }
    @FXML
    private void aleatorio() {
        if (!esAleatorio) esAleatorio = true;
        else esAleatorio = false;
        if (iconoAleatorio.getFill().equals(Color.WHITE))
            iconoAleatorio.setFill(Color.GREEN);
        else iconoAleatorio.setFill(Color.WHITE);
        iconoBucleInfinito.setFill(Color.WHITE);
        comprobarBucleInfinito = false;
    }


    @FXML
    private void avanzar() {
        //comprobar si es aleatorio
         if(esAleatorio && !comprobarBucleUnaVez){
            int noRepetido = ControladorMaster.numeroCancion = (int) (Math.random()*ControladorMaster.canciones.length);
            while (ControladorMaster.numeroCancion == noRepetido){
                noRepetido = (int) (Math.random()*ControladorMaster.canciones.length);
            }
            ControladorMaster.numeroCancion = noRepetido;
        }
        //comprobar si está en bucle
        else if (comprobarBucleUnaVez) {
             //No sumamos uno a la cancion para que vuelva a sonar la misma
            comprobarBucleUnaVez = false;
             iconoBucleUno.setFill(Color.WHITE);
         }
        //comprobar si está en bucle infinito
        else if (comprobarBucleInfinito) ;//No sumamos uno a la cancion para que vuelva a sonar la misma y no cambiamos la variable
        //para que repita siempre la misma cancion

        else ControladorMaster.numeroCancion++;

        if (ControladorMaster.numeroCancion == ControladorMaster.canciones.length) ControladorMaster.numeroCancion = 0;
        ControladorMaster.cancion = ControladorMaster.carpetaMusica + ControladorMaster.canciones[ControladorMaster.numeroCancion];
        retroceder = false;
        avanzar = true;
        try {
            reproductor.stop();
            //Vaciamos el reproductor con null
            reproductor = null;
            cancionReproducida = null;
            estaReproduciendo = false;
            reproducir();
        } catch (NullPointerException e) {
            reproducir();
        }

    }

    @FXML
    private void retroceder() {
        if (esAleatorio){
            int noRepetido = ControladorMaster.numeroCancion = (int) (Math.random()*ControladorMaster.canciones.length);
            while (ControladorMaster.numeroCancion == noRepetido){
               noRepetido = (int) (Math.random()*ControladorMaster.canciones.length);
            }
            ControladorMaster.numeroCancion = noRepetido;
        }
        //comprobar si está en bucle
        else if (comprobarBucleUnaVez) {
            //No sumamos uno a la cancion para que vuelva a sonar la misma
            comprobarBucleUnaVez = false;
            iconoBucleUno.setFill(Color.WHITE);
        }
        else if (comprobarBucleInfinito) ;//No sumamos uno a la cancion para que vuelva a sonar la misma y no cambiamos la variable
            //para que repita siempre la misma cancion
        else ControladorMaster.numeroCancion--;

        if (ControladorMaster.numeroCancion < 0)
            ControladorMaster.numeroCancion = ControladorMaster.canciones.length - 1;
        ControladorMaster.cancion = ControladorMaster.carpetaMusica + ControladorMaster.canciones[ControladorMaster.numeroCancion];
        retroceder = true;
        avanzar = false;
        try {
            reproductor.stop();
            //Vaciamos el reproductor con null
            reproductor = null;
            cancionReproducida = null;
            estaReproduciendo = false;
            reproducir();
        } catch (NullPointerException e) {
            reproducir();
        }
    }

   /* public synchronized void sumarTiempo() {
        String tiempo;
        try {
            if (segundos < 10) {
                tiempo = minutos + ":0" + segundos + "/";
            } else {
                tiempo = minutos + ":" + segundos + "/";
            }
            segundos++;
            if (segundos == 60) {
                minutos++;
                segundos = 0;
            }
            Thread.sleep(1000);
            tiempoAvanzado.setText(tiempo);
        } catch (NullPointerException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }*/


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

    /*@FXML
    public void abrirBiblioteca() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Biblioteca.fxml"));
        Stage stage = new Stage();
        Scene scene;
        Biblioteca controlador = fxmlLoader.getController();
        HBox h = controlador.leerCanciones();
        scene = new Scene(h, 300, 500);
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:ico.png"));
        stage.setTitle("Biblioteca");
        stage.setScene(scene);
        stage.show();
    }*/

}