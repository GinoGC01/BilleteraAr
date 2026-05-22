package ar.edu.ungs.billetera.modelo.cuenta;

public class CuentaCorporativa extends Cuenta {

    private String cuit;
    private boolean usuarioAutorizado;

    public CuentaCorporativa(String cvu, String alias, double saldo, String cuit, boolean usuarioAutorizado) {
        super(cvu, alias, saldo);
        this.cuit = cuit;
        this.usuarioAutorizado = usuarioAutorizado;
    }

    @Override
    public boolean puedeOperar(double monto) {
        return usuarioAutorizado && (getSaldo() - monto) >= 0;
    }

    public String getCuit() { return cuit; }
    public boolean isUsuarioAutorizado() { return usuarioAutorizado; }

    @Override
    public String toString() {
        return "CuentaCorporativa | CUIT: " + cuit + " | " + super.toString();
    }
}