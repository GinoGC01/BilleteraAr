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
        return usuarioAutorizado && (obtenerSaldo() - monto) >= 0;
    }

    public String obtenerCuit() { return cuit; }
    public String obtenerTipo() {
        return "Corporativa";
    }
    public boolean isUsuarioAutorizado() { return usuarioAutorizado; }

    @Override
    public String toString() {
        return "CuentaCorporativa | CUIT: " + cuit + " | " + super.toString();
    }

}