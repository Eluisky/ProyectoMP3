package com.example.proyectomp3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Reproducción extends Thread{
    private  int segundos = 0;
    private  int minutos = 0;
    public Label tiempoAvanzado;

    public Reproducción(Label tiempoAvanzado) {
        this.tiempoAvanzado = tiempoAvanzado;
    }


    public synchronized String sumarTiempo(){
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
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
        return tiempo;
    }

    @Override
    public void run() {
        tiempoAvanzado.setText("0:00/");
        Platform.runLater(() -> {
            while(true) sumarTiempo();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
