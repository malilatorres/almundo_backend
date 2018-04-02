/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almundo.ejercicio.backend.model;

import com.almundo.ejercicio.backend.model.constants.RoleEnum;
import com.almundo.ejercicio.backend.service.Dispatcher;
import com.almundo.ejercicio.backend.util.Util;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Mariale
 */
public class EmployeeTest {
    
    private static Util util = Util.getInstance();
    
    private Employee employee;
     
      /**
     * Test si se asignan n llamadas al Empleado deben ser atendidas 
     * 10 llamadas creadas en la cola del empleado
     * Debe resultar en 10 llamadas atendidas
     * @throws InterruptedException 
     */
    @Test
    public void dispatchMoreThan10Calls() throws InterruptedException {
        //Se crean 10 llamadas entre 1 y 2 segundos
        List<Call> calls = util.buildCallsList(1, 2, 10);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ConcurrentLinkedDeque<Call> callsQueue =  new ConcurrentLinkedDeque<>();  ;
        employee = new Employee("Pepito", RoleEnum.OPERADOR, true);
        addCallsToQueue(callsQueue, calls);
        employee.setCallsQueue(callsQueue);
        TimeUnit.SECONDS.sleep(1);
        executorService.execute(employee);
       
        executorService.awaitTermination(15, TimeUnit.SECONDS);

        assertEquals(10, employee.getAttendedCalls());
    }

    private void addCallsToQueue(ConcurrentLinkedDeque<Call> callsQueue, List<Call> calls) {
        for (Call call : calls){
            callsQueue.add(call);
        }
    }
    
}
