package ar.edu.ungs.billetera.modelo.actividad;

import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;

public class Transferencia extends Actividad {

    private double monto;
    private Cuenta cuentaDestino;

    public Transferencia(LocalDate fecha, Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) {
        super(fecha, cuentaOrigen);
        this.monto = monto;
        this.cuentaDestino = cuentaDestino;
    }

    public void ejecutar() {
        if (!obtenerCuentaOrigen().puedeOperar(monto)) {
            throw new IllegalStateException("Saldo insuficiente en cuenta origen.");
        }
        if (!obtenerCuentaDestino().puedeOperar(monto)) {
            throw new IllegalStateException("La cuenta destino superaría el límite permitido.");
        }
        obtenerCuentaOrigen().debitar(monto);
        cuentaDestino.acreditar(monto);
    }

    public double obtenerMonto() { return monto; }
    public Cuenta obtenerCuentaDestino() { return cuentaDestino; }

    @Override
    public String toString() {
        return "Transferencia | " + super.toString() +
                " | Destino: " + cuentaDestino.obtenerCvu() +
                " | Monto: $" + monto;
    }
}