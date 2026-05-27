package ar.edu.ungs.billetera.modelo.actividad.inversion;

import ar.edu.ungs.billetera.Utilitarios;
import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;

public class VinculadaDivisa extends Inversion implements Precancelable {

    private String divisa;
    private double tasaInteres;
    private double cotizacionInicial;

    public VinculadaDivisa(LocalDate fecha, Cuenta cuentaOrigen, LocalDate fechaConstitucion, int plazo, double monto, String divisa, double tasaInteres) {
        super(fecha, cuentaOrigen, fechaConstitucion, plazo, monto);
        this.divisa = divisa;
        this.tasaInteres = tasaInteres;
        this.cotizacionInicial = Utilitarios.consultarCotizacion(divisa);
    }

    @Override
    public double calcularResultado() {
        long diasTranscurridos = Utilitarios.hoy().toEpochDay() - getFechaConstitucion().toEpochDay();
        double montoEnDivisas = getMonto() / cotizacionInicial;
        double interesesEnDivisas = montoEnDivisas * (tasaInteres / 365.0) * diasTranscurridos;
        return (montoEnDivisas + interesesEnDivisas) * Utilitarios.consultarCotizacion(divisa);
    }

    @Override
    public double calcularResultadoPrecancelado() {
        long diasTranscurridos = Utilitarios.hoy().toEpochDay() - getFechaConstitucion().toEpochDay();
        double montoEnDivisas = getMonto() / cotizacionInicial;
        double interesesEnDivisas = montoEnDivisas * (tasaInteres / 365.0) * diasTranscurridos / 2;
        return (montoEnDivisas + interesesEnDivisas) * Utilitarios.consultarCotizacion(divisa);
    }

    public String getDivisa() { return divisa; }
    public double getTasaInteres() { return tasaInteres; }

    @Override
    public String toString() {
        return "VinculadaDivisa | " + super.toString() +
                " | Divisa: " + divisa +
                " | Tasa: " + tasaInteres;
    }
}