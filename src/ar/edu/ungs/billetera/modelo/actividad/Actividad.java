package ar.edu.ungs.billetera.modelo.actividad;

import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Actividad {

    private LocalDate fecha;
    private Cuenta cuentaOrigen;
    private String id;

    public Actividad(LocalDate fecha, Cuenta cuentaOrigen) {
        this.fecha = fecha;
        this.cuentaOrigen = cuentaOrigen;
        this.id = UUID.randomUUID().toString(); //uuid unico
    }

    public String getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public Cuenta getCuentaOrigen() { return cuentaOrigen; }

    @Override
    public String toString() {
        return "Fecha: " + fecha + " | Origen: " + cuentaOrigen.getCvu();
    }
}