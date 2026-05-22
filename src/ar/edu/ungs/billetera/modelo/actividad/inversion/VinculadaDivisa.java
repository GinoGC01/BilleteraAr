package ar.edu.ungs.billetera.modelo.actividad.inversion;

import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;

public class VinculadaDivisa extends Inversion implements Precancelable {

    private String divisa;
    private double tasaInteres;

    public VinculadaDivisa(LocalDate fecha, Cuenta cuentaOrigen, LocalDate fechaConstitucion, int plazo, double monto, String divisa, double tasaInteres) {
        super(fecha, cuentaOrigen, fechaConstitucion, plazo, monto);
        this.divisa = divisa;
        this.tasaInteres = tasaInteres;
    }

    @Override
    public double calcularResultado() {
        return getMonto() * tasaInteres * getPlazo();
    }

    @Override
    public double calcularResultadoPrecancelado() {
        return calcularResultado() / 2;
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