/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author pablo
 */
public class Batalla{
    private Personaje jugador1, oponente;
    private Personaje j1Original, oponenteOriginal;
    private JTextArea area;
    private volatile boolean activa = true;
    
    public Batalla(Personaje p1, Personaje oponente, JTextArea area){
        this.jugador1 = p1.clonar(); //Usa clones para no modificar a los originales
        this.oponente = oponente.clonar();
        this.j1Original = jugador1;
        this.oponenteOriginal = oponente;
        this.area = area;
    }
    
    public void iniciar(){
        agregarTexto("=== BATALLA INICIADA ===");
        agregarTexto(jugador1.getNombre() + " vs " + oponente.getNombre());
        agregarTexto("");
        
        //Hilo para el jugador 1
        Thread hilo1 = new Thread(() -> atacar(jugador1, oponente));
        //Hilo para el oponente
        Thread hilo2 = new Thread(() -> atacar(oponente, jugador1));
        
        hilo1.start();
        hilo2.start();
        
        try{
            hilo1.join();
            hilo2.join();
        }catch(InterruptedException e){
            System.out.println("Batalla Interrumpida");
        }
        
        if(jugador1.estaVivo()){
            agregarTexto("\n ¡GANADOR: " + jugador1.getNombre() + "!");
            j1Original.incrementarBatallasGanadas();
            oponenteOriginal.incrementarBatallasPerdidas();
        }else{
            agregarTexto("\n ¡GANADOR: " + oponente.getNombre() + "!");
            oponenteOriginal.incrementarBatallasGanadas();
            j1Original.incrementarBatallasPerdidas();
        }
        
        agregarTexto("=== BATALLA FINALIZADA ===");
    }
    
    private void atacar(Personaje atacante, Personaje defensor){
        while(atacante.estaVivo() && defensor.estaVivo()){
            //Simular tiempo entre ataques basado en velocidad
            try{
                Thread.sleep(1000 / atacante.getVelocidad()); //Mayor vel. menos espera
            }catch(InterruptedException e){
                break;
            }
            
            if(!activa) break;
            
            synchronized(this){
                if(!defensor.estaVivo() || !activa) break;
                
                //Verificar si el defensor esquiva
            if(Math.random() * 10 < defensor.getAgilidad()){
                agregarTexto(atacante.getNombre() + " ataca a " + defensor.getNombre() + " - ¡ESQUIVADO!");
            }else{
                //Calcular daño
                int dano = atacante.getAtaque();
                int danoReal = dano - defensor.getDefensa();
                if(danoReal < 0) danoReal = 0;
                
                defensor.setHp(defensor.getHp() - danoReal);
                agregarTexto(atacante.getNombre() + " ataca a " + defensor.getNombre() + " - " + danoReal + " de daño");
                
                if(!defensor.estaVivo()){
                    agregarTexto("¡" + defensor.getNombre() + " ha sido derrotado!");
                    activa = false;
                }
            }
            
            //Mostrar estado actual
            agregarTexto("❤️ " + atacante.getNombre() + ": " + atacante.getHp() + " HP | " + defensor.getNombre() + ": " + defensor.getHp() + " HP");
            agregarTexto("");
        }
    }
}
    private void agregarTexto(String texto){
        SwingUtilities.invokeLater(() -> {
            area.append(texto + "\n");
        });
    }
}
    

