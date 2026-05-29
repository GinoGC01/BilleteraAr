package ar.edu.ungs.billetera.modelo.actividad.inversion;

import ar.edu.ungs.billetera.Utilitarios;
import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import ar.edu.ungs.billetera.modelo.cuenta.CuentaCorporativa;
import java.time.LocalDate;

public class FondoLiquidez extends Inversion {

    private static final double MONTO_MINIMO = 20_000_000;

    public FondoLiquidez(LocalDate fecha, Cuenta cuentaOrigen, LocalDate fechaConstitucion, int plazo, double monto) {
        super(fecha, cuentaOrigen, fechaConstitucion, plazo, monto);
        if (!(cuentaOrigen instanceof CuentaCorporativa)) {
            throw new IllegalArgumentException("FondoLiquidez solo puede originarse desde una CuentaCorporativa.");
        }
        if (monto < MONTO_MINIMO) {
            throw new IllegalArgumentException("FondoLiquidez requiere un monto mínimo de $20.000.000.");
        }
    }

    @Override
    public double calcularResultado() {
        long diasTranscurridos = Utilitarios.hoy().toEpochDay() - obtenerFechaConstitucion().toEpochDay();
        double cotizacionFle = Utilitarios.consultarCotizacion("FLE");
        return obtenerMonto() * (0.08 / 365.0) * diasTranscurridos * cotizacionFle;
    }

    @Override
    public String toString() {
        return "FondoLiquidez | " + super.toString();
    }
}