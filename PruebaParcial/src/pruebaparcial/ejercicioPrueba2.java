/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebaparcial;

import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class ejercicioPrueba2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ListaPrimos();
    }
    
    static void ListaPrimos(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Ingrese un numero: ");
        int n = entrada.nextInt();
        if(n<2){
            System.out.println("No existen primos en ese rango");
        }else{
            System.out.print("Lista de numeros primos hasta "+n+": ");
            boolean primero = true;
            for (int i = 2; i <= n; i++) {
                if(esPrimo(i)){
                    if(!primero) System.out.print(" ");
                    System.out.print(i + "\n");
                    primero = false;
                }
                
            }
        }
    
    }
    
    static boolean esPrimo(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
}
