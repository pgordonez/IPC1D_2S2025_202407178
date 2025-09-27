/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

/**
 *
 * @author pablo
 */
public class GestorPersonajes {
    private Personaje[] personajes;
    private int cantidad;
    private int siguienteId;
    
    public GestorPersonajes(int capacidad){
        personajes = new Personaje[capacidad];
        cantidad = 0;
        siguienteId = 1;
    }
    
    public boolean agregarPersonaje(String nombre, String arma, int hp, int ataque, int velocidad, int agilidad, int defensa) {
        if(!validarRangos(hp, ataque, velocidad, agilidad, defensa)){
            return false;
        }
        if(buscarPorNombre(nombre) != null){
            return false;
        }
        if(cantidad >= personajes.length){
            return false;
        }
        Personaje nuevo = new Personaje(siguienteId++, nombre, arma, hp, ataque, velocidad, agilidad, defensa);
        personajes[cantidad] = nuevo;
        cantidad++;
        return true; 
    }
    
    public boolean modificarPersonaje(String nombre, String arma, int hp, int ataque, int velocidad, int agilidad, int defensa){
        if(!validarRangos(hp, ataque, velocidad, agilidad, defensa)){
            return false;
        }
        
        Personaje personaje = buscarPorNombre(nombre);
        if (personaje == null) {
            return false;
        }
        
        personaje.setArma(arma);
        personaje.setHp(hp);
        personaje.setAtaque(ataque);
        personaje.setVelocidad(velocidad);
        personaje.setAgilidad(agilidad);
        personaje.setDefensa(defensa);
        return true;
    }
    
    public boolean eliminarPersonaje(String nombre){
        for (int i = 0; i < cantidad; i++) {
            if (personajes[i].getNombre().equalsIgnoreCase(nombre)) {
                for (int j = i; j < cantidad - 1; j++) {
                    personajes[j] = personajes[j + 1];
                }
                personajes[cantidad - 1] = null;
                cantidad--;
                return true;
            }
        }
        return false;
    }
    
    public Personaje buscarPorNombre(String nombre){
        for (int i = 0; i < cantidad; i++) {
            if (personajes[i].getNombre().equalsIgnoreCase(nombre)) {
                return personajes[i];
            }
        }
        return null;
    }
    
    private boolean validarRangos(int hp, int ataque, int velocidad, int agilidad, int defensa){
        return (hp >= 100 && hp <= 500) &&
               (ataque >= 10 && ataque <= 100) &&
               (velocidad >= 1 && velocidad <= 10) &&
               (agilidad >= 1 && agilidad <= 10) &&
               (defensa >= 1 && defensa <= 50);
    }
    
    public Personaje[] getPersonajes(){
        return personajes;
    }
    public int getCantidad(){
        return cantidad;
    }
}

    
    
