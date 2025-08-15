/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1;

/**
 *
 * @author pablo
 */
public class Personaje {
    int id;
    String nombre;
    String arma;
    String[] habilidades = new String[Practica1.max_habilidades];
    int cantHabilidades;
    int nivel;


    public Personaje(int id, String nombre, String arma, String[] habilidades, int cantHabilidades, int nivel) {
        this.id = id;
        this.nombre = nombre;
        this.arma = arma;
        this.cantHabilidades = cantHabilidades;
        for (int i = 0; i < cantHabilidades; i++) {
            this.habilidades[i] = habilidades[i];
        }
        this.nivel = nivel;
    }

    
    

}
