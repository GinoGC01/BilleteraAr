package ar.edu.ungs.billetera.modelo;

import ar.edu.ungs.billetera.IBilletera;
import ar.edu.ungs.billetera.modelo.actividad.Actividad;
import ar.edu.ungs.billetera.modelo.actividad.Transferencia;
import ar.edu.ungs.billetera.modelo.actividad.inversion.Inversion;
import ar.edu.ungs.billetera.modelo.actividad.inversion.Precancelable;
import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Billetera implements IBilletera {

    private Map<String, Usuario> usuarios;
    private Map<String, Empresa> empresas; // clave = cuit
    private List<Actividad> historialGlobal;
    private Map<String, Cuenta> cuentas;   // clave = cvu
    private Map<String, String> aliases;   // clave = alias, valor = cvu

    // Email — igual, ya lo cubre
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // Teléfono — cubre (011) 4444-6666 y variantes
    private static final String REGEX_TELEFONO = "^(\\+54\\s?)?(\\([0-9]{2,4}\\)\\s?)?[0-9]{4}[-\\s]?[0-9]{4}$";

    public Billetera() {
        this.usuarios = new HashMap<>();
        this.empresas = new HashMap<>();
        this.historialGlobal = new ArrayList<>();
        this.cuentas = new HashMap<>();
        this.aliases = new HashMap<>();
    }

    @Override
    public void registrarEmpresa(String cuit, String nombreFantasia, String telefono, String email, String nombreContacto) {

    }

    @Override
    public void agregarPersonaAutorizada(String cuitEmpresa, String dniAutorizado) {

    }

    // Registrar usuario
    public void registrarUsuario(String dni, String nombre, String telefono, String email) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede ser nulo o vacío.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        }
        if (usuarios.containsKey(dni)) {
            throw new IllegalArgumentException("El usuario ya está registrado.");
        }
        if (!email.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
        if (!telefono.matches(REGEX_TELEFONO)) {
            throw new IllegalArgumentException("Teléfono inválido: " + telefono);
        }
        usuarios.put(dni, new Usuario(dni, nombre, telefono, email));
    }

    @Override
    public String crearCuentaRegular(String dniUsuario, String alias) {
        return "";
    }

    @Override
    public String crearCuentaPremium(String dniUsuario, String alias, double depositoInicial) {
        return "";
    }

    @Override
    public String crearCuentaCorporativa(String dniUsuario, String alias, String cuitEmpresa) {
        return "";
    }

    @Override
    public List<String> obtenerCuentas(String dniUsuario) {
        return List.of();
    }

    @Override
    public double obtenerSaldoDisponible(String cvu) {
        return 0;
    }

    @Override
    public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {

    }

    @Override
    public int realizarInversionRentaFija(String dni, String cvu, double monto, int plazoDias) {
        return 0;
    }

    @Override
    public int realizarInversionDivisa(String dni, String cvu, double monto, int plazoDias, String divisa, double tasa) {
        return 0;
    }

    @Override
    public int realizarInversionLiquidez(String dni, String cvu, double monto, int plazoDias) {
        return 0;
    }

    @Override
    public void precancelarInversion(String dni, String cvu, int idInversion) {

    }

    @Override
    public String consultarCvu(String alias) {
        return "";
    }

    @Override
    public List<String> consultarHistorialGlobal() {
        return List.of();
    }

    @Override
    public List<String> consultarHistorialCuenta(String cvu) {
        return List.of();
    }

    @Override
    public List<String> consultarHistorialUsuario(String dniUsuario) {
        return List.of();
    }

    @Override
    public double obtenerTotalInvertido(String dniUsuario) {
        return 0;
    }

    @Override
    public List<String> cuentasConMayorVolumen(int cantidadTop) {
        return List.of();
    }

    // Obtener usuario
    public Usuario getUsuario(String id) {
        return usuarios.get(id);
    }

    // Transferir entre cuentas
    public void transferir(Cuenta origen, Cuenta destino, double monto) {
        Transferencia t = new Transferencia(LocalDate.now(), origen, destino, monto);
        t.ejecutar();
        historialGlobal.add(t);
    }

    // Registrar inversión
    public void invertir(Usuario usuario, Inversion inversion) {
        if (!inversion.getCuentaOrigen().puedeOperar(inversion.getMonto())) {
            throw new IllegalStateException("La cuenta no puede operar con ese monto.");
        }
        inversion.getCuentaOrigen().debitar(inversion.getMonto());
        usuario.registrarInversion(inversion.getMonto());
        historialGlobal.add(inversion);
    }

    // Precancelar inversión
    public double precancelar(Inversion inversion) {
        if (!(inversion instanceof Precancelable)) {
            throw new IllegalStateException("Esta inversión no es precancelable.");
        }
        return ((Precancelable) inversion).calcularResultadoPrecancelado();
    }

    // Historial global
    public List<Actividad> getHistorialGlobal() {
        return historialGlobal;
    }

    // Total invertido por usuario
    public double getTotalInvertido(String idUsuario) {
        return usuarios.get(idUsuario).getTotalInvertido();
    }

    @Override
    public String toString() {
        return "Billetera | Usuarios: " + usuarios.size() +
                " | Actividades: " + historialGlobal.size();
    }
}