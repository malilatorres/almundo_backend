package com.almundo.ejercicio.backend.init;

import com.almundo.ejercicio.backend.model.Call;
import com.almundo.ejercicio.backend.service.Dispatcher;
import com.almundo.ejercicio.backend.util.Util;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RunDispatcher {


    /**
     * Tiempo máximo de llamada en segundos
     */
    private static final int MAX_TIME = 10;
    /**
     * Tiempo mínimo de llamada en segundos
     */
    private static final int MIN_TIME = 5;
    /**
     * Número de operadores
     */
    private static final int OPERATORS = 5;
    /**
     * Número de supervisores
     */
    private static final int SUPERVISORS = 2;
    /**
     * Número de directores
     */
    private static final int DIRECTORS = 1;

    /**
     * Número de llamadas
     */
    private static final int TOTAL_CALLS = 10;

    /**
     * Número de empreados
     */
    private static final int TOTAL_EMPLOYEES = 10;
    

    private static final Logger logger = LoggerFactory.getLogger(RunDispatcher.class);
    private static Util util = Util.getInstance();

    public static void main(String[] args) throws InterruptedException {
        List<Call> calls = util.buildCallsList(MIN_TIME, MAX_TIME, TOTAL_CALLS);
        Dispatcher dispatcher = new Dispatcher(util.buildEmployeeList(OPERATORS, SUPERVISORS, DIRECTORS));
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        for (Call call : calls ){
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }

        try {
            executorService.awaitTermination(MAX_TIME * (TOTAL_CALLS/TOTAL_EMPLOYEES) , TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        dispatcher.stop();
        System.exit(0);
    }

}
