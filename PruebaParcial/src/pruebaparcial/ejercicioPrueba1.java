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
public class ejercicioPrueba1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Ingrese un numero: ");
        int n = entrada.nextInt();
        if(esPrimo(n)){
            System.out.println( n + " es primo");
        }else{
            System.out.println(n + " no es primo");
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
