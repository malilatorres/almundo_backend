package com.almundo.ejercicio.backend.service;

import com.almundo.ejercicio.backend.model.Call;
import com.almundo.ejercicio.backend.model.Employee;
import com.almundo.ejercicio.backend.util.Util;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The Class DispatcherTest.
 *
 */
public class DispatcherTest {

    private static Util util = Util.getInstance();

    private Dispatcher dispatcher;

    /**
     * Test de 10 llamadas concurrentes atendidas por 5 empleados
     * Debe resultar en 10 llamadas atendidas
     * @throws InterruptedException 
     */
    @Test
    public void dispatchTenCalls() throws InterruptedException {
        //Se crean 10 llamadas con duracion entre 5 y 10 segundos
        List<Call> calls = util.buildCallsList(5, 10, 10);
        int attendedCalls = 0;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // Se crean 5 empleados, 3 operadores - 1 supervisores y 1 directores
        List<Employee> employees = util.buildEmployeeList(3, 1, 1);
        dispatcher = new Dispatcher(employees);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);
        for (Call call : calls) {
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        executorService.awaitTermination(15, TimeUnit.SECONDS);
        dispatcher.stop();

        attendedCalls = sumTotalAttendedCalls(employees);
        assertEquals(10, attendedCalls);
    }
    
    /**
     * Test para cuando entran llamadas y todos los empleados estan ocupados
     * Se crean 2 llamadas concurrentes con 1 empleado 
     * Debe resultar en 1 llamadas que tuvieron que esperar a ser atendidas
     * @throws InterruptedException 
     */
    @Test
    public void totalAwaitingCalls() throws InterruptedException {
        //Se crean 2 llamadas con duracion entre 5 y 10 segundos
        List<Call> calls = util.buildCallsList(5, 10, 2);
        int hadWait = 0;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // Se crean 5 empleados, 1 operador
        List<Employee> employees = util.buildEmployeeList(1, 0, 0);
        dispatcher = new Dispatcher(employees);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);
        for (Call call : calls) {
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        dispatcher.stop();

        hadWait = dispatcher.getTotalCallsAwait();
        assertEquals(1, hadWait);
    }
    
    
    /**
     * Test de mas de 10 llamadas concurrentes.
     * 100 llamadas atendidas por 15 empleados, llamadas entre 1 y 2 segs para probar mas rapido
     * Debe resultar en 100 llamadas atendidas
     * @throws InterruptedException 
     */
    @Test
    public void dispatchMoreThan10Calls() throws InterruptedException {
        //Se crean 100 llamadas entre 1 y 2 segundos
        List<Call> calls = util.buildCallsList(1, 2, 100);
        int attendedCalls = 0;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // Se crean 15 empleados, 10 operadores - 3 supervisores y 2 directores
        List<Employee> employees = util.buildEmployeeList(10, 3, 2);
        dispatcher = new Dispatcher(employees);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);
        for (Call call : calls) {
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        dispatcher.stop();

        attendedCalls = sumTotalAttendedCalls(employees);
        assertEquals(100, attendedCalls);
    }
    
    
    private int sumTotalAttendedCalls(List<Employee> employees) {
        int total = 0;
        for (Employee emp : employees) {
            total += emp.getAttendedCalls();
        }
        return total;
    }

}
