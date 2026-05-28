package ar.edu.ungs.billetera.modelo.cuenta;

public abstract class Cuenta {

    // Atributos base, todas las cuentas deben tener:
    protected String cvu;
    protected String alias;
    protected double saldo;

    public Cuenta(String cvu, String alias, double saldo) {
        this.cvu = cvu;
        this.alias = alias;
        this.saldo = saldo;
    }

    // Metodo abstracto, cada subclase resuelve
    public abstract boolean puedeOperar(double monto);

    public abstract String getTipo();

    // Getters
    public String getCvu() { return cvu; }
    public String getAlias() { return alias; }
    public double getSaldo() { return saldo; }

    // Para modificar el saldo (transferencias e inversiones)
    public void debitar(double monto) {
        this.saldo -= monto;
    }

    public void acreditar(double monto) {
        this.saldo += monto;
    }

    @Override
    public String toString() {
        return "CVU: " + cvu + " | Alias: " + alias + " | Saldo: " + saldo;
    }
}