# almundo_backend

Contiene la solucion de la prueba backend para Almundo

La solucion consta de una clase Dispatcher que realiza el manejo de una cola de llamadas asignandolas a los empleados disponibles.

La clase Dispatcher pone las llamadas en una cola de espera cada vez que llegan con el metodo dispatchCall, esto lo hace de manera secuencial.
Una vez se ejecuta la tarea de la clase Dispatcher, revisa si hay llamadas en la cola de espera y busca en una lista un empleado libre de acuerdo a los  criterios de negocio (Operador-> Supervisor -> Director) y le asigna la primera llamada de la cola al mismo; si no encuentra un empleado deja la llamada en la cola para que sea atendida posteriormente. Dispatcher continua ejecutando la misma secuencia de paso hasta que sea "apagado".

La clase Employee se inicia cuando se inicia el Dispatcher, dispatcher crea un pool de 10 hilos para ejecutar la tarea que realiza Employee concurrentemente

Cada empleado Employee tiene una cola de llamadas la cual revisa si tiene alguna llamada  y las atiende demorandose el tiempo que tiene definido la llamada el cual se calculo aleatoriamente, una vez termine con la llamada queda en estado libre (free) para atender mas llamadas. 
El Employee continua realizando la misma secuencia hasta que termine la aplicacion.


Se crea una prueba unitaria para 10 llamadas, con un numero fijo de empleados.

Para los puntos extras:

 - Como se menciono antes; si se recibe un llamada y no hay empleados libres, se indica que no hay disponibilidad y se pone la llamada nuevamente en  la cola (de primeras) para que sea atendida posteriormente cuando se libere algun empleado. Se crea un test para este caso.
 - Cuando entran mas de 10 llamadas concurrentemente estas quedan en cola de espera hasta que los empleados se vayan desocupando y las atiendan. Se crea un test para este caso.
 - Se incluyen test unitarios de las difernetes clases
 - Se incluye documentacion en el codigo para hacerlo mas claro, se documenta cada paso y variable en la aplicación.
 
