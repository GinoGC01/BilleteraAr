package ar.edu.ungs.billetera.modelo.actividad.inversion;

import ar.edu.ungs.billetera.modelo.actividad.Actividad;
import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;

public abstract class Inversion extends Actividad {

    private LocalDate fechaConstitucion;
    private int plazo;
    private double monto;

    public Inversion(LocalDate fecha, Cuenta cuentaOrigen, LocalDate fechaConstitucion, int plazo, double monto) {
        super(fecha, cuentaOrigen);
        this.fechaConstitucion = fechaConstitucion;
        this.plazo = plazo;
        this.monto = monto;
    }

    public abstract double calcularResultado();

    public LocalDate getFechaConstitucion() { return fechaConstitucion; }
    public int getPlazo() { return plazo; }
    public double getMonto() { return monto; }

    @Override
    public String toString() {
        return "Inversion | " + super.toString() +
                " | Monto: $" + monto +
                " | Plazo: " + plazo + " días";
    }
}