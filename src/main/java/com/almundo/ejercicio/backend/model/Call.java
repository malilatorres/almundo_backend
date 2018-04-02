package com.almundo.ejercicio.backend.model;


import java.util.Random;

/**
 * Representa una llamada
 * Tiene un tiempo de duración y un id
 */
public class Call {
    
    /**
   * id de llamada
   */
  private int id;

  /**
   * Tiempo de llamada en segundos
   */
  private int time;
  
  /**
   * indica si la llamada tuvo que esperar a ser atendida
   */
    
private boolean hadWait;
  
  public Call(){
      
  }
  
  /**
   * Contructor con tiempo predefinido de llamada
   * @param time 
   */
  public Call (int time) {
      Random randomGenerator = new Random();
      this.time =time;
      this.id  = randomGenerator.nextInt();
      this.hadWait= false;
              
  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isHadWait() {
        return hadWait;
    }

    public void setHadWait(boolean hadWait) {
        this.hadWait = hadWait;
    }

  
}
