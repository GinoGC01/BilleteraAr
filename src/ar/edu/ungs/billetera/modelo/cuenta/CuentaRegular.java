package ar.edu.ungs.billetera.modelo.cuenta;

public class CuentaRegular extends Cuenta {

    private static final double SALDO_MAXIMO = 5_000_000;

    public CuentaRegular(String cvu, String alias, double saldo) {
        super(cvu, alias, saldo);
        if(saldo > SALDO_MAXIMO) {
            throw new IllegalStateException("El saldo máximo es 5M");
        }
    }

    @Override
    public boolean puedeOperar(double monto) {
        return (obtenerSaldo() + monto) <= SALDO_MAXIMO;
    }

    public String obtenerTipo() {
        return "Regular";
    }

    @Override
    public String toString() {
        return "CuentaRegular | " + super.toString();
    }
}