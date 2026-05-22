package ar.edu.ungs.billetera.modelo.cuenta;

public class CuentaPremium extends Cuenta {

    private static final double SALDO_MINIMO_APERTURA = 500_000;

    public CuentaPremium(String cvu, String alias, double saldo) {
        super(cvu, alias, saldo);
        if (saldo < SALDO_MINIMO_APERTURA) {
            throw new IllegalArgumentException("CuentaPremium requiere un saldo mínimo de $500.000 al abrir.");
        }
    }

    @Override
    public boolean puedeOperar(double monto) {
        return (getSaldo() - monto) >= 0;
    }

    @Override
    public String toString() {
        return "CuentaPremium | " + super.toString();
    }
}