/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

/**
 *
 * @author pablo
 */
public class Personaje {
    //Atributos del personaje
    private int id;
    private String nombre;
    private String arma;
    private int hp;
    private int ataque;
    private int velocidad;
    private int agilidad;
    private int defensa;
    private int batallasGanadas;
    private int batallasPerdidas;

    //Constructor    
    public Personaje(int id, String nombre, String arma, int hp, int ataque, int velocidad, int agilidad, int defensa) {
        this.id = id;
        this.nombre = nombre;
        this.arma = arma;
        this.hp = hp;
        this.ataque = ataque;
        this.velocidad = velocidad;
        this.agilidad = agilidad;
        this.defensa = defensa;
        this.batallasGanadas = batallasGanadas = 0;
        this.batallasPerdidas = batallasPerdidas = 0;        
    }
    
    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArma() {
        return arma;
    }

    public void setArma(String arma) {
        this.arma = arma;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getAgilidad() {
        return agilidad;
    }

    public void setAgilidad(int agilidad) {
        this.agilidad = agilidad;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getBatallasGanadas() {
        return batallasGanadas;
    }

    public void setBatallasGanadas(int batallasGanadas) {
        this.batallasGanadas = batallasGanadas;
    }

    public int getBatallasPerdidas() {
        return batallasPerdidas;
    }

    public void setBatallasPerdidas(int batallasPerdidas) {
        this.batallasPerdidas = batallasPerdidas;
    }
    
//Metodo para calcular dano recibido
public int recibirDano(int danoBase){
    int danoReal = danoBase - this.defensa;
    if(danoReal < 0) danoReal = 0;
    this.hp -= danoReal;
    if(this.hp < 0) this.hp = 0;
    return danoReal;
}

//Método para verificar si esta vivo
public boolean estaVivo(){
    return this.hp > 0;
}

//Método para clonar personaje (útil para batallas)
public Personaje clonar(){
    Personaje clon = new Personaje(this.id, this.nombre, this.arma, this.hp, this.ataque,
            this.velocidad, this.agilidad, this.defensa);
    clon.setBatallasGanadas(this.batallasGanadas);
    clon.setBatallasPerdidas(this.batallasPerdidas);
    return clon;
}

//Método para incrementar estadisticas de batallas
public void incrementarBatallasGanadas(){
    this.batallasGanadas++;
}

public void incrementarBatallasPerdidas(){
    this.batallasPerdidas++;
}

//Método para mostrar informacion del personaje
public String toString(){
    return "ID: " + id + " | " + nombre + " | HP: " + hp + " | Ataque: " + ataque + 
               " | Velocidad: " + velocidad + " | Defensa: " + defensa;
    }   
}
