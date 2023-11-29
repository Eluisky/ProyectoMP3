package com.example.proyectomp3;

public class Reproducción extends Thread{

    Controlador controlador;

    public Reproducción(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void run() {
        while(true) controlador.sumarTiempo();
    }
}
