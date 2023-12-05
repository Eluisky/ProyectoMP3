package com.example.proyectomp3;

import java.io.File;

public class ControladorMaster {

    public static String[] recorrerMusica(String carpeta){
        String[] canciones;
        File carpetaMusica = new File(carpeta);
        canciones = carpetaMusica.list();
        if (canciones != null){
            for (int i = 0; i < canciones.length; i++) {
                System.out.println(canciones[i]);
            }
        }
        return canciones;
    }

}
