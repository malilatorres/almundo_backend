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

  
}
