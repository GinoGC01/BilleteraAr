package ar.edu.ungs.billetera.modelo.actividad.inversion;

import ar.edu.ungs.billetera.Utilitarios;
import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;

public class RentaFija extends Inversion implements Precancelable {

    private double tasaInteres;

    public RentaFija(LocalDate fecha, Cuenta cuentaOrigen, LocalDate fechaConstitucion, int plazo, double monto, double tasaInteres) {
        super(fecha, cuentaOrigen, fechaConstitucion, plazo, monto);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public double calcularResultado() {
        long diasTranscurridos = Utilitarios.hoy().toEpochDay() - obtenerFechaConstitucion().toEpochDay();
        return obtenerMonto() * (tasaInteres / 365.0) * diasTranscurridos;
    }

    @Override
    public double calcularResultadoPrecancelado() {
        return obtenerMonto() + (calcularResultado() / 2);
    }

    public double obtenerTasaInteres() { return tasaInteres; }

    @Override
    public String toString() {
        return "RentaFija | " + super.toString() +
                " | Tasa: " + tasaInteres;
    }
}