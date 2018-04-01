/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almundo.ejercicio.backend.util;

import com.almundo.ejercicio.backend.model.Call;
import com.almundo.ejercicio.backend.model.Employee;
import com.almundo.ejercicio.backend.model.constants.RoleEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Mariale
 */
public class Util {

    private static Util instance;

    private Util() {

    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();

        }
        return instance;
    }

    /**
     * Genera un tiempo aleatorio de la llamada en un rango entre el tope mínimo
     * y máximo definido
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomTime(int min, int max) {
        int time = 1;
        Random randomGenerator = new Random();
        if (min > -1 && max > min) {
            time = randomGenerator.nextInt(max - min + 1) + min;
        }
        return time;
    }

    /**
     * Cosntruye la lista de empleados segun el numero indicado de cada rol
     *
     * @param operator
     * @param supervisor
     * @param director
     * @return
     */
    public static List<Employee> buildEmployeeList(int operator, int supervisor, int director) {
        List<Employee> employees = new ArrayList<>();

        if (operator > 0) {
            for (int i = 0; i < operator; i++) {
                employees.add(new Employee(RoleEnum.OPERADOR.getRole() + i, RoleEnum.OPERADOR, true));
            }
        }
        if (operator > 0) {
            for (int i = 0; i < supervisor; i++) {
                employees.add(new Employee(RoleEnum.SUPERVISOR.getRole() + i, RoleEnum.SUPERVISOR, true));
            }
        }
        if (operator > 0) {
            for (int i = 0; i < director; i++) {
                employees.add(new Employee(RoleEnum.DIRECTOR.getRole() + i, RoleEnum.DIRECTOR, true));
            }
        }
        return employees;
    }

    /**
     * Construye la lista de llamadas segun el total de llamadas y con tiempos
     * aelatorios para cada una
     *
     * @param minTime
     * @param maxTime
     * @param total
     * @return
     */
    public static List<Call> buildCallsList(int minTime, int maxTime, int total) {
        List<Call> calls = new ArrayList<>();

        if (total > 0) {
            for (int i = 0; i < total; i++) {
                calls.add(new Call(Util.randomTime(minTime, maxTime)));
            }
        }
        return calls;
    }
}
