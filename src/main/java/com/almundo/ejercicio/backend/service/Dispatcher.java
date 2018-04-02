package com.almundo.ejercicio.backend.service;

import com.almundo.ejercicio.backend.model.Call;
import com.almundo.ejercicio.backend.model.Employee;
import com.almundo.ejercicio.backend.model.constants.RoleEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dispatcher recibe las llamadas y las reparte a los empleados disponibles
 * segun las reglas de negocio
 */
public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * Número de llamadas concurrentes
     */
    private static final int CONCURRENT_CALLS = 10;
    /**
     * Cola de llamadas en espera
     */
    private ConcurrentLinkedDeque<Call> awaitingCalls;

    /**
     * Lista de empleados
     */
    private List<Employee> employeesList;

    /**
     * Indica si el Dispatcher esta prendido o apagado
     */
    private boolean running;
    
    private ExecutorService executor;
    
    /**
     * Numero de llamadas que tuvieron que esperar a ser atendidas 
     */
    private int totalCallsAwait;

    public Dispatcher() {

    }

    /**
     *
     * @param employees
     * @param maxCalls
     */
    public Dispatcher(List<Employee> employees) {
        if (!employees.isEmpty()) {
            this.executor = Executors.newFixedThreadPool(CONCURRENT_CALLS);
            this.awaitingCalls = new ConcurrentLinkedDeque<>();
            this.employeesList = Collections.synchronizedList(employees);
            this.running = false;
            this.totalCallsAwait=0;
        }
    }

    /**
     * Recibe una llamada y la pone en la cola de espera para ser atendida
     *
     * @param call
     */
    public synchronized void dispatchCall(Call call) {
        logger.info("Dispatcher recibe llamada {}", call.getId());
        getAwaitingCalls().add(call);
    }

    /**
     * Inicia el ExecutorService de empleados
     */
    public synchronized void start() {
        setRunning(true);
        for (Employee emp : getEmployeesList()) {
            getExecutor().execute(emp);
        }
    }

    /**
     * Detiene el ExecutorService de empleados
     */
    public synchronized void stop() {
        setRunning(false);
        getExecutor().shutdown();
    }

    /**
     * Asigna una llamada al empleadoque este disponible
     */
    @Override
    public void run() {
        logger.info("Inicia el Dispatcher");
        do {
            if (!getAwaitingCalls().isEmpty()) {

                Call call = getAwaitingCalls().poll();
                logger.info("Buscando asesor para llamada {}", call.getId());
                Employee employee = availableEmployee();
                if (employee == null) {
                    call.setHadWait(true);
                    getAwaitingCalls().addFirst(call);
                    continue;
                }
                try {
                    if(call.isHadWait()){
                        setTotalCallsAwait(getTotalCallsAwait()+1);
                    }
                    employee.start(call);
                } catch (Exception err) {
                    getAwaitingCalls().addFirst(call);
                    logger.error("Error inesperado: {}", err);
                }
            }
        } while (isRunning());
    }

    /**
     * Selecciona un empleado libre para responder una llamada
     *
     * @return selectedEmployee
     */
    private Employee availableEmployee() {
        List<Employee> operatorList = new ArrayList<>();
        List<Employee> supervisoList = new ArrayList<>();
        List<Employee> directorList = new ArrayList<>();
        Employee selectedEmployee = null;

        if (getEmployeesList() != null) {
            for (Employee emp : getEmployeesList()) {
                if (emp.isFree() && RoleEnum.OPERADOR.equals(emp.getRole())) {
                    operatorList.add(emp);
                } else if (emp.isFree() && RoleEnum.SUPERVISOR.equals(emp.getRole())) {
                    supervisoList.add(emp);
                } else if (emp.isFree() && RoleEnum.DIRECTOR.equals(emp.getRole())) {
                    directorList.add(emp);
                }
            }
        }
        if (operatorList.isEmpty() && supervisoList.isEmpty() && directorList.isEmpty()) {
            logger.info("No hay asesores disponibles, por favor espere");
            return null;
        }

        /* de los disponibles se selecciona un empleado de acuerdo a las reglas de negocio*/
        if (!operatorList.isEmpty()) {
            selectedEmployee = operatorList.get(0);
        } else if (!supervisoList.isEmpty()) {
            selectedEmployee = supervisoList.get(0);
        } else if (!directorList.isEmpty()) {
            selectedEmployee = directorList.get(0);
        }

        logger.info("Empleado seleccionado {} - {} ", selectedEmployee.getName(), selectedEmployee.getRole());
        return selectedEmployee;
    }

    public ConcurrentLinkedDeque<Call> getAwaitingCalls() {
        return awaitingCalls;
    }

    public void setAwaitingCalls(ConcurrentLinkedDeque<Call> awaitingCalls) {
        this.awaitingCalls = awaitingCalls;
    }

    public List<Employee> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(List<Employee> employeesList) {
        this.employeesList = employeesList;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    } 

    public int getTotalCallsAwait() {
        return totalCallsAwait;
    }

    public void setTotalCallsAwait(int totalCallsAwait) {
        this.totalCallsAwait = totalCallsAwait;
    }

}
