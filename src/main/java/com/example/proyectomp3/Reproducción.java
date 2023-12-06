package com.example.proyectomp3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Reproducción extends Thread{
    private  int segundos = 0;
    private  int minutos = 0;
    private Label tiempoAvanzando;

    public Reproducción(Label tiempoAvanzando) {
        this.tiempoAvanzando = tiempoAvanzando;
    }

    public synchronized void sumarTiempo(javafx.scene.control.Label tiempoAvanzado){
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
            Thread.sleep(1000);
            tiempoAvanzado.setText(tiempo);
        } catch (NullPointerException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(true) sumarTiempo(tiempoAvanzando);
    }
}
