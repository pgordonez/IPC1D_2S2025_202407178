/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class Practica1 {
    static Scanner entrada = new Scanner(System.in);
    
    //Constantes
    static final int max_personajes = 50;
    static final int max_habilidades = 5;
    static final int max_peleas = 100;
       
    //Info de los personajes
    static Personaje [] personajes = new Personaje [max_personajes];
    static int npersonajes = 0;
    static int nextid = 1;
       
    //Datos de las peleas
    static int[] idpeleadorA = new int[max_peleas];
    static int[] idpeleadorB = new int[max_peleas];
    static LocalDateTime[] TiempoPelea = new LocalDateTime[max_peleas];
    static int npeleas = 0;
    static int opcion=0;
       

    public static void main(String[] args) {
        //MENU PRINCIPAL
        while(opcion!=9){
            System.out.println("******* MENU PRINCIPAL *******");
            System.out.println("1. Agregar Personaje");
            System.out.println("2. Modificar Personaje");
            System.out.println("3. Eliminar Personaje");
            System.out.println("4. Ver Datos de un Personaje");
            System.out.println("5. Ver Listado de Personajes");
            System.out.println("6. Realizar Pelea entre Personajes");
            System.out.println("7. Ver Historial de Peleas");
            System.out.println("8. Ver Datos de Estudiante");
            System.out.println("9. Salir");
            System.out.println("10. Imprime nombre completo");
            opcion = leerEnteroRango("Seleccione una opcion: ", 1, 10);
            
            switch(opcion){
                case 1: agregarPersonaje(); break;
                case 2: modificarPersonaje(); break;
                case 3: eliminarPersonaje(); break;
                case 4: verPersonaje(); break;
                case 5: listaPersonajes(); break;
                case 6: registrarPelea(); break;
                case 7: verHistorial(); break;
                case 8: datosEstudiante(); break;
                case 9: System.out.println("Saliendo del programa...");
                case 10: System.out.println("Pablo Gabriel Ordonez Escobar");
                return;
                }
            }
        }
    //*********  METODOS *********
    
    //AGREGAR PERSONAJES
    static void agregarPersonaje(){
        if (npersonajes >= max_personajes){
        System.out.println("No se pueden añadir mas personajes.");
            return;
        }
        String nombre;
        do{
            nombre = leerCadenaNoVacia("Ingrese el nombre del personaje: ");
            if(existeNombre(nombre)){
                System.out.println("Error: el nombre ya existe.");
            }
        }while(existeNombre(nombre));
        String arma = leerCadenaNoVacia("Ingrese el arma del personaje: ");
        int numHabs = leerEnteroRango("Ingrese el numero de habilidades entre (0 - 5)", 0, max_habilidades);
        String[] habsTemp = new String[max_habilidades];
        for (int i = 0; i < numHabs; i++) {
            habsTemp[i] = leerCadenaNoVacia("Habilidad "+ (i+1)+ ": ");
        }
        int nivel = leerEnteroRango("Ingrese el nivel entre (0 - 100): ", 1, 100);
        personajes[npersonajes] = new Personaje(nextid++, nombre, arma, habsTemp, numHabs, nivel);
        npersonajes++;
        System.out.println("Tu Personaje ha sido agregado con exito.");
            }
    
    //MODIFICACION DE PERSONAJES
    static void modificarPersonaje(){
        int id = leerEntero("Ingrese el ID del personaje a modificar: ");
        int idx = indicePorId(id);
        if(idx == -1){
            System.out.println("No existe ese personaje.");
            return;
        }
        Personaje p = personajes[idx];
        String arma = leerCadenaOpcional("Arma actual ("+p.arma+"): ");
        if(!arma.isEmpty()) p.arma = arma;
        int numHabs = leerEnteroRango("Nuevo numero de habilidades (0 - 5): ", 0, max_habilidades);
        p.cantHabilidades = numHabs;
        for (int i = 0; i < numHabs; i++) {
            p.habilidades[i] = leerCadenaNoVacia("Habilidad "+(i+1)+ ": ");
        }
        int nivel = leerEnteroRango("Nuevo nivel entre (0 - 100): ", 1, 100);
        p.nivel = nivel;
        System.out.println("Personaje modificado exitosamente!");
    }
    
    //ELIMINACION DE PERSONAJES
    static void eliminarPersonaje(){
        int id = leerEntero("Ingrese el ID del personaje: ");
        int idx = indicePorId(id);
        if(idx == -1){
            System.out.println("No existe el personaje.");
            return;
        }
        System.out.println("¿Esta seguro que desea eliminar? (Y/N): ");
        String conf = entrada.nextLine().trim();
        if(!conf.equalsIgnoreCase("Y")){
            System.out.println("Cancelado.");
            return;
        }
        for (int i = idx; i < npersonajes - 1; i++) {
            personajes[i] = personajes[i + 1];
        }
        npersonajes--;
        System.out.println("Personaje Eliminado.");
    }
    
    //VER PERSONAJE ESPECIFICO
    static void verPersonaje(){
        int id = leerEntero("Ingrese el numero del ID: ");
        int idx = indicePorId(id);
        if(idx == -1){
            System.out.println("No existe el personaje ingresado.");
            return;
        }
        Personaje p = personajes[idx];
        System.out.println("ID: "+ p.id);
        System.out.println("Nombre: "+p.nombre);
        System.out.println("Arma: "+ p.arma);
        System.out.print("Habilidades: ");
        for (int i = 0; i < p.cantHabilidades; i++) {
            System.out.print(p.habilidades[i] + (i < p.cantHabilidades -1 ? ", " : ""));
        }
        System.out.println("\nNivel: " + p.nivel);  
    }
    
    //MOSTRAR LISTA DE TODOS LOS PERSONAJES
    static void listaPersonajes(){
        if (npersonajes == 0){
            System.out.println("Error! No hay personajes registrados.");
            return;
        }
        for (int i = 0; i < npersonajes; i++) {
            Personaje p = personajes[i];
            System.out.println("ID: "+p.id+"   \nNombre:  "+p.nombre+"   \nNivel:  "+p.nivel+"\n-----------------------");
        }
    }
    
    //REGISTRO DE PELEAS
    static void registrarPelea(){
        if(npersonajes<2){
            System.out.println("Se necesitan almenos 2 Personaje para hacer una Pelea.");
            return;
        }
        int idA = leerEntero("Ingrese el ID del primer personaje: ");
        int idxA = indicePorId(idA);
        if(idxA == -1){
            System.out.println("Personaje no encontrado!");
            return;
        }
        int idB = leerEntero("Ingrese el ID del segundo personaje: ");
        int idxB = indicePorId(idB);
        if(idxB == -1){
            System.out.println("Personaje no encontrado!");
            return;
        }
        if(idA == idB){
            System.out.println("El personaje no puede pelear con el mismo!");
            return;
        }
        idpeleadorA[npeleas] = idA;
        idpeleadorB[npeleas] = idB;
        TiempoPelea[npeleas] = LocalDateTime.now();
        npeleas++;
        System.out.println("La Pelea ha sido registrada!");
    }
    
    //VER HISTORIAL DE PELEAS
    static void verHistorial(){
        if(npeleas == 0){
            System.out.println("No hay peleas existentes!");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (int i = 0; i < npeleas; i++) {
            System.out.println(idpeleadorA[i]+" vs "+ idpeleadorB[i]+" -- "+ TiempoPelea[i].format(fmt));
        }
    }
    
    //DATOS DEL ESTUDIANTE
    static void datosEstudiante(){
        System.out.println("NOMBRE: Pablo Gabriel Ordoñez Escobar");
        System.out.println("CARNET: 202407178");
        System.out.println("Seccion: B");
    }
                
    //********* METODOS DE UTILIDADES *********
    static int leerEntero(String mensaje) {
        while(true){
            System.out.println(mensaje);
            try{
                int valor = Integer.parseInt(entrada.nextLine().trim());
                return valor;
            }catch (NumberFormatException error){
                System.out.println("********* Error! ********* \nIngrese una opcion valida.");
            }
        }
    }
    static int leerEnteroRango(String mensaje, int min, int max){
            int valor;
            do{
                valor = leerEntero(mensaje);
                if(valor<min || valor>max){
                    System.out.println("El valor tiene que estar entre "+min+" y "+max +"!!");
                }
            }while(valor<min || valor>max);
            return valor;
        }
    static String leerCadenaNoVacia(String mensaje){
            String texto;
            do{
                System.out.println(mensaje);
                texto = entrada.nextLine().trim();
                if(texto.isEmpty()){
                    System.out.println("Error! \n No puede estar vacio!");
                }
            }while(texto.isEmpty());
            return texto;
        }
    static String leerCadenaOpcional(String mensaje){
        System.out.println(mensaje);
        return entrada.nextLine().trim();
    }
    static int indicePorId(int id){
        for (int i = 0; i < npersonajes; i++) {
            if(personajes[i].id == id) return i;
        }
        return -1;
    }
    static boolean existeNombre(String nombre){
        for (int i = 0; i < npersonajes; i++) {
            if(personajes[i].nombre.equalsIgnoreCase(nombre)) return true;
        }
        return false;
        }
    }
