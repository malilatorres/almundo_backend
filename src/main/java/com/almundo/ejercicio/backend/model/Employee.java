package com.almundo.ejercicio.backend.model;

import com.almundo.ejercicio.backend.model.constants.RoleEnum;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representa los empleados del callcenter quienes reciben llamadas 
 * Tiene un rol y un estado (ocupado o desocupado)
 */
public class Employee implements Runnable {

    /**
     * Rol del empleado
     */
    private RoleEnum role;
    
    /**
     * Indica si el empleado esta desocupado o no
     */
    private boolean free;
    
    /** 
     * Nombre del empleado
     */
    private String name;

    /** 
     * NUmero de llamadas atendidas
     */
    private int attendedCalls;
    
    /** 
     * Cola de llamadas del empleado
     */
    private ConcurrentLinkedDeque<Call> callsQueue;
    
    private static final Logger logger = LoggerFactory.getLogger(Employee.class);
    
 

    public Employee(String name, RoleEnum role, boolean free) {
        this.name = name;
        this.role = role;
        this.free = free;
        this.callsQueue = new ConcurrentLinkedDeque<>();
        this.attendedCalls=0;
    }

    public void start(Call call) {
        getCallsQueue().add(call);
        logger.info("Empleado {} - {} recibió la llamada  {}  de {} segundos",
                getName(), getRole(), call.getId(), call.getTime());
        
    }

    /**
     * El empleado contesta y atiende las llamadas de su cola de llmadas
     */
    @Override
    public void run() {
        logger.info("Inicia el empleado {} - {}", getName(), getRole());
        do {
            if (!getCallsQueue().isEmpty()) {
                Call call = getCallsQueue().poll();
                setFree(false);
                setAttendedCalls(getAttendedCalls()+1);
                logger.info("Llamada contestada por el empleado {} - {}", getName(), getRole());
                try {
                    TimeUnit.SECONDS.sleep(call.getTime());
                } catch (InterruptedException e) {
                    logger.error("Llamada  {} terminada con error", call.getId());
                } finally {
                    setFree(true);
                }
                logger.info("Llamada {} terminada despues de {} segundos. Empleado {}", 
                        call.getId(), call.getTime(), getName());
                
                
            }
        } while (true);

    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConcurrentLinkedDeque<Call> getCallsQueue() {
        return callsQueue;
    }

    public void setCallsQueue(ConcurrentLinkedDeque<Call> callsQueue) {
        this.callsQueue = callsQueue;
    }

    public int getAttendedCalls() {
        return attendedCalls;
    }

    public void setAttendedCalls(int attendedCalls) {
        this.attendedCalls = attendedCalls;
    }

    
}
