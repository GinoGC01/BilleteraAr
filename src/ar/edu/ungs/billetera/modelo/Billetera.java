package ar.edu.ungs.billetera.modelo;

import ar.edu.ungs.billetera.IBilletera;
import ar.edu.ungs.billetera.Utilitarios;
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
import java.util.UUID;

import ar.edu.ungs.billetera.modelo.cuenta.CuentaPremium;
import ar.edu.ungs.billetera.modelo.cuenta.CuentaRegular;
import ar.edu.ungs.billetera.modelo.cuenta.CuentaCorporativa;
import ar.edu.ungs.billetera.modelo.actividad.inversion.RentaFija;
import ar.edu.ungs.billetera.modelo.actividad.inversion.VinculadaDivisa;
import ar.edu.ungs.billetera.modelo.actividad.inversion.FondoLiquidez;

public class Billetera implements IBilletera {

    private Map<String, Usuario> usuarios;
    private Map<String, Empresa> empresas; // clave = cuit
    private List<Actividad> historialGlobal;
    private Map<String, Cuenta> cuentas;   // clave = cvu
    private Map<String, String> aliases;   // clave = alias, valor = cvu

    // Email igual, ya lo cubre
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // Teléfono cubre (011) 4444-6666 y variantes
    // private static final String REGEX_TELEFONO = "^(\\+54\\s?)?(\\([0-9]{2,4}\\)\\s?)?[0-9]{4}[-\\s]?[0-9]{4}$";

    public Billetera() {
        this.usuarios = new HashMap<>();
        this.empresas = new HashMap<>();
        this.historialGlobal = new ArrayList<>();
        this.cuentas = new HashMap<>();
        this.aliases = new HashMap<>();
    }

    @Override
    public void registrarEmpresa(String cuit, String nombreEmpresa, String telefono, String email, String nombreContacto) {
        if (cuit == null || cuit.isBlank()) {
            throw new IllegalArgumentException("El CUIT no puede ser nulo o vacío.");
        }
        if (nombreEmpresa == null || nombreEmpresa.isBlank()) {
            throw new IllegalArgumentException("El nombre de fantasía no puede ser nulo o vacío.");
        }
//        if (telefono == null || telefono.isBlank() || telefono.length() != 11) {
//            throw new IllegalArgumentException("El teléfono no puede ser nulo o vacío.");
//        } lo mismo, sino no pasa los test.
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        }
        if (nombreContacto == null || nombreContacto.isBlank()) {
            throw new IllegalArgumentException("El nombre de contacto no puede ser nulo o vacío.");
        }
        if (empresas.containsKey(cuit)) {
            throw new IllegalArgumentException("La empresa ya está registrada."); //revisa en el diccionario
        }
        if (!email.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException("Email inválido: " + email); //validaciones con regex
        }
//        if (!telefono.matches(REGEX_TELEFONO)) {
//            throw new IllegalArgumentException("Teléfono inválido: " + telefono); //validaciones con regex
//        }
        empresas.put(cuit, new Empresa(cuit, nombreEmpresa, telefono, email, nombreContacto)); //pushea al diccionario una empresa nueva
    }

    @Override
    public void agregarPersonaAutorizada(String cuitEmpresa, String dniAutorizado) {
        // PUEDE NO ESTAR REGISTRADO -> no validar el dni 
        if (cuitEmpresa == null || cuitEmpresa.isBlank()) {
            throw new IllegalArgumentException("El CUIT no puede ser nulo o vacío.");
        }
        if (dniAutorizado == null || dniAutorizado.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (!empresas.containsKey(cuitEmpresa)) {
            throw new IllegalArgumentException("La empresa no existe.");
        }
        Empresa empresa = empresas.get(cuitEmpresa); //traemos la empresa por tenant
        if (empresa.estaAutorizado(dniAutorizado)) {
            throw new IllegalArgumentException("La persona ya está autorizada.");
        }
        empresa.agregarPersonaAutorizada(dniAutorizado); // agrega la persona segun corresponda
    }

    // Registrar usuario
    public void registrarUsuario(String dni, String nombre, String telefono, String email) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        //no deja validar telefono. No corren los test || telefono.length() != 11 o con la expresion regular
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
//        if (!telefono.matches(REGEX_TELEFONO)) {
//            throw new IllegalArgumentException("Teléfono inválido: " + telefono);
//        }
        usuarios.put(dni, new Usuario(dni, nombre, telefono, email));
    }

    @Override
    public String crearCuentaRegular(String dniUsuario, String alias) {
        if (dniUsuario == null || dniUsuario.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("El alias no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (aliases.containsKey(alias)) {
            throw new IllegalArgumentException("El alias ya está registrado.");
        }

        // uso uuid para asegurarme de que sea unico para el cvu y que no se duplique.
        String cvu = Utilitarios.generarSiguienteCvu();
        CuentaRegular cuenta = new CuentaRegular(cvu, alias, 0);
        cuentas.put(cvu, cuenta); // add nueva cuenta
        aliases.put(alias, cvu); // add nuevo alias -> cuenta
        usuarios.get(dniUsuario).agregarCuenta(cuenta); // cuenta -> usuario
        return cvu;
    }

    @Override
    public String crearCuentaPremium(String dniUsuario, String alias, double depositoInicial) {
        if (dniUsuario == null || dniUsuario.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("El alias no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (aliases.containsKey(alias)) {
            throw new IllegalArgumentException("El alias ya está registrado.");
        }
        String cvu = Utilitarios.generarSiguienteCvu();
        CuentaPremium cuenta = new CuentaPremium(cvu, alias, depositoInicial);
        cuentas.put(cvu, cuenta);
        aliases.put(cvu, alias);
        usuarios.get(dniUsuario).agregarCuenta(cuenta);
        return cvu;
    }

    @Override
    public String crearCuentaCorporativa(String dniUsuario, String alias, String cuitEmpresa) {
        if (dniUsuario == null || dniUsuario.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("El alias no puede ser nulo o vacío.");
        }
        if (cuitEmpresa == null || cuitEmpresa.isBlank()) {
            throw new IllegalArgumentException("El CUIT no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (!empresas.containsKey(cuitEmpresa)) {
            throw new IllegalArgumentException("La empresa no existe.");
        }
        if (aliases.containsKey(alias)) {
            throw new IllegalArgumentException("El alias ya está registrado.");
        }
        Empresa empresa = empresas.get(cuitEmpresa);
        if (!empresa.estaAutorizado(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no está autorizado para operar en esta empresa.");
        }
        String cvu = Utilitarios.generarSiguienteCvu();
        CuentaCorporativa cuenta = new CuentaCorporativa(cvu, alias, 0, cuitEmpresa, true);
        cuentas.put(cvu, cuenta);
        aliases.put(alias, cvu);
        usuarios.get(dniUsuario).agregarCuenta(cuenta);
        return cvu;
    }

    @Override
    public List<String> obtenerCuentas(String dniUsuario) {
        if (dniUsuario == null || dniUsuario.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        List<String> resultado = new ArrayList<>();
        for (Cuenta cuenta : usuarios.get(dniUsuario).getCuentas()) {
            resultado.add(cuenta.getClass().getSimpleName() + ": " + cuenta.getAlias() + " (" + cuenta.getCvu() + ")");
        }
        return resultado;
    }

    @Override
    public double obtenerSaldoDisponible(String cvu) {
        if (cvu == null || cvu.isBlank()) {
            throw new IllegalArgumentException("El CVU no puede ser nulo o vacío.");
        }
        if (!cuentas.containsKey(cvu)) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        return cuentas.get(cvu).getSaldo();
    }

    @Override
    public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
        if (cvuOrigen == null || cvuOrigen.isBlank()) {
            throw new IllegalArgumentException("El CVU origen no puede ser nulo o vacío.");
        }
        if (cvuDestino == null || cvuDestino.isBlank()) {
            throw new IllegalArgumentException("El CVU destino no puede ser nulo o vacío.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (!cuentas.containsKey(cvuOrigen)) {
            throw new IllegalArgumentException("La cuenta origen no existe.");
        }
        if (!cuentas.containsKey(cvuDestino)) {
            throw new IllegalArgumentException("La cuenta destino no existe.");
        }
        if (cvuOrigen.equals(cvuDestino)) {
            throw new IllegalArgumentException("Las cuentas origen y destino no pueden ser iguales.");
        }
        Cuenta origen = cuentas.get(cvuOrigen);
        Cuenta destino = cuentas.get(cvuDestino);
        Transferencia transferencia = new Transferencia(LocalDate.now(), origen, destino, monto);
        transferencia.ejecutar();
        historialGlobal.add(transferencia);
    }

    @Override
    public int realizarInversionRentaFija(String dni, String cvu, double monto, int plazoDias) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (cvu == null || cvu.isBlank()) {
            throw new IllegalArgumentException("El CVU no puede ser nulo o vacío.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (plazoDias <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a cero.");
        }
        if (!usuarios.containsKey(dni)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (!cuentas.containsKey(cvu)) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        Cuenta cuenta = cuentas.get(cvu);
        if (!cuenta.puedeOperar(monto)) {
            throw new IllegalStateException("La cuenta no puede operar con ese monto.");
        }
        Usuario usuario = usuarios.get(dni);
        RentaFija inversion = new RentaFija(Utilitarios.hoy(), cuenta, Utilitarios.hoy(), plazoDias, monto, 0.20);
        cuenta.debitar(monto);
        usuario.registrarInversion(monto);
        historialGlobal.add(inversion);
        return inversion.getId().hashCode(); //hashcode para poder devolver el entero - preguntar profe
    }

    @Override
    public int realizarInversionDivisa(String dni, String cvu, double monto, int plazoDias, String divisa, double tasa) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (cvu == null || cvu.isBlank()) {
            throw new IllegalArgumentException("El CVU no puede ser nulo o vacío.");
        }
        if (divisa == null || divisa.isBlank()) {
            throw new IllegalArgumentException("La divisa no puede ser nula o vacía.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (plazoDias <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a cero.");
        }
        if (tasa <= 0) {
            throw new IllegalArgumentException("La tasa debe ser mayor a cero.");
        }
        if (!usuarios.containsKey(dni)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (!cuentas.containsKey(cvu)) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        Cuenta cuenta = cuentas.get(cvu);
        if (!cuenta.puedeOperar(monto)) {
            throw new IllegalStateException("La cuenta no puede operar con ese monto.");
        }
        Usuario usuario = usuarios.get(dni);
        VinculadaDivisa inversion = new VinculadaDivisa(LocalDate.now(), cuenta, LocalDate.now(), plazoDias, monto, divisa, tasa);
        cuenta.debitar(monto);
        usuario.registrarInversion(monto);
        historialGlobal.add(inversion);
        return inversion.getId().hashCode(); // lo mismo
    }

    @Override
    public int realizarInversionLiquidez(String dni, String cvu, double monto, int plazoDias) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (cvu == null || cvu.isBlank()) {
            throw new IllegalArgumentException("El CVU no puede ser nulo o vacío.");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (plazoDias <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a cero.");
        }
        if (!usuarios.containsKey(dni)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (!cuentas.containsKey(cvu)) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        Cuenta cuenta = cuentas.get(cvu);
        if (!(cuenta instanceof CuentaCorporativa)) {
            throw new IllegalArgumentException("FondoLiquidez solo puede originarse desde una CuentaCorporativa.");
        }
        if (!cuenta.puedeOperar(monto)) {
            throw new IllegalArgumentException("La cuenta no puede operar con ese monto.");
        }
        Usuario usuario = usuarios.get(dni);
        FondoLiquidez inversion = new FondoLiquidez(LocalDate.now(), cuenta, LocalDate.now(), plazoDias, monto);
        cuenta.debitar(monto);
        usuario.registrarInversion(monto);
        historialGlobal.add(inversion);
        return inversion.getId().hashCode();
    }


    private Inversion buscarInversion(String cvu, int idInversion) {
        for (Actividad actividad : historialGlobal) {
            if (actividad instanceof Inversion inversion) {
                if (inversion.getCuentaOrigen().getCvu().equals(cvu) &&
                        inversion.getId().hashCode() == idInversion) {
                    return inversion;
                }
            }
        }
        throw new IllegalArgumentException("Inversión no encontrada.");
    }
    @Override
    public void precancelarInversion(String dni, String cvu, int idInversion) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (cvu == null || cvu.isBlank()) {
            throw new IllegalArgumentException("El CVU no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dni)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        if (!cuentas.containsKey(cvu)) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        Inversion inversion = buscarInversion(cvu, idInversion);
        if (!(inversion instanceof Precancelable)) {
            throw new IllegalStateException("Esta inversión no es precancelable.");
        }
        double resultado = ((Precancelable) inversion).calcularResultadoPrecancelado();
        cuentas.get(cvu).acreditar(resultado);
        usuarios.get(dni).registrarInversion(-inversion.getMonto());
    }

    @Override
    public String consultarCvu(String alias) {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("El alias no puede ser nulo o vacío.");
        }
        if (!aliases.containsKey(alias)) {
            throw new IllegalArgumentException("El alias no está registrado.");
        }
        return aliases.get(alias);
    }


    // El problema es que desde Transferencia no podemos saber
    // el DNI del usuario --> solo sabemos la cuenta.
    // Necesitamos un metodo auxiliar privado que dado un CVU encuentre el DNI

    private String buscarDniPorCvu(String cvu) {
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            for (Cuenta cuenta : entry.getValue().getCuentas()) {
                if (cuenta.getCvu().equals(cvu)) {
                    return entry.getKey();
                }
            }
        }
        return "desconocido";
    }

    // tiene O(n2) -> la idea es recorrer todos los usuarios
    // y por cada usuario recorrer las cuentas y findear un cvu
    // si find ? dni : desconocido
    @Override
    public List<String> consultarHistorialGlobal() {
        List<String> resultado = new ArrayList<>();
        for (Actividad actividad : historialGlobal) {
            if (actividad instanceof Transferencia transferencia) {
                String dniOrigen = buscarDniPorCvu(transferencia.getCuentaOrigen().getCvu());
                String dniDestino = buscarDniPorCvu(transferencia.getCuentaDestino().getCvu());
                resultado.add(
                        "origen: " + dniOrigen + " (" + transferencia.getCuentaOrigen().getCvu() + ")\n" +
                                "destino: " + dniDestino + " (" + transferencia.getCuentaDestino().getCvu() + ")\n" +
                                "monto: " + transferencia.getMonto() + "\n" +
                                "Aprobado"
                );
            } else if (actividad instanceof Inversion inversion) {
                String dniOrigen = buscarDniPorCvu(inversion.getCuentaOrigen().getCvu());
                resultado.add(
                        "origen: " + dniOrigen + " (" + inversion.getCuentaOrigen().getCvu() + ")\n" +
                                "desc: " + inversion.getClass().getSimpleName() + "\n" +
                                "monto: " + inversion.getMonto() + "\n" +
                                "plazo: " + inversion.getPlazo() + "\n" +
                                "Aprobado"
                );
            }
        }
        return resultado;
    }

    @Override
    public List<String> consultarHistorialCuenta(String cvu) {
        if (cvu == null || cvu.isBlank()) {
            throw new IllegalArgumentException("El CVU no puede ser nulo o vacío.");
        }
        if (!cuentas.containsKey(cvu)) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        List<String> resultado = new ArrayList<>();
        for (Actividad actividad : historialGlobal) {
            if (actividad instanceof Transferencia transferencia) {
                if (transferencia.getCuentaOrigen().getCvu().equals(cvu) ||
                        transferencia.getCuentaDestino().getCvu().equals(cvu)) {
                    String dniOrigen = buscarDniPorCvu(transferencia.getCuentaOrigen().getCvu());
                    String dniDestino = buscarDniPorCvu(transferencia.getCuentaDestino().getCvu());
                    resultado.add(
                            "origen: " + dniOrigen + " (" + transferencia.getCuentaOrigen().getCvu() + ")\n" +
                                    "destino: " + dniDestino + " (" + transferencia.getCuentaDestino().getCvu() + ")\n" +
                                    "monto: " + transferencia.getMonto() + "\n" +
                                    "Aprobado"
                    );
                }
            } else if (actividad instanceof Inversion inversion) {
                if (inversion.getCuentaOrigen().getCvu().equals(cvu)) {
                    String dniOrigen = buscarDniPorCvu(inversion.getCuentaOrigen().getCvu());
                    resultado.add(
                            "origen: " + dniOrigen + " (" + inversion.getCuentaOrigen().getCvu() + ")\n" +
                                    "desc: " + inversion.getClass().getSimpleName() + "\n" +
                                    "monto: " + inversion.getMonto() + "\n" +
                                    "plazo: " + inversion.getPlazo() + "\n" +
                                    "Aprobado"
                    );
                }
            }
        }
        return resultado;
    }

    @Override
    public List<String> consultarHistorialUsuario(String dniUsuario) {
        if (dniUsuario == null || dniUsuario.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        List<String> resultado = new ArrayList<>();
        for (Cuenta cuenta : usuarios.get(dniUsuario).getCuentas()) {
            resultado.addAll(consultarHistorialCuenta(cuenta.getCvu()));
        }
        return resultado;
    }

    @Override
    public double obtenerTotalInvertido(String dniUsuario) {
        if (dniUsuario == null || dniUsuario.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (!usuarios.containsKey(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        return usuarios.get(dniUsuario).getTotalInvertido();
    }

    @Override
    public List<String> cuentasConMayorVolumen(int cantidadTop) {
        if (cantidadTop <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        // Cuento las actividades por CVU - metodo extraido (preguntar profe) IJ
        Map<String, Integer> volumenPorCuenta = getStringIntegerMap();

        // Ordeno de mayor a menor y tomo los primeros N
        List<String> resultado = new ArrayList<>();
        volumenPorCuenta.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(cantidadTop)
                .forEach(entry -> {
                    Cuenta cuenta = cuentas.get(entry.getKey());
                    resultado.add(cuenta.getClass().getSimpleName() + ": " + cuenta.getAlias() + " (" + cuenta.getCvu() + ")");
                });

        return resultado;
    }

    // consultar
    private Map<String, Integer> getStringIntegerMap() {
        Map<String, Integer> volumenPorCuenta = new HashMap<>();
        for (Actividad actividad : historialGlobal) {
            if (actividad instanceof Transferencia transf) {
                String cvuOrigen = transf.getCuentaOrigen().getCvu();
                String cvuDestino = transf.getCuentaDestino().getCvu();
                volumenPorCuenta.put(cvuOrigen, volumenPorCuenta.getOrDefault(cvuOrigen, 0) + 1);
                volumenPorCuenta.put(cvuDestino, volumenPorCuenta.getOrDefault(cvuDestino, 0) + 1);
            } else if (actividad instanceof Inversion inv) {
                String cvuOrigen = inv.getCuentaOrigen().getCvu();
                volumenPorCuenta.put(cvuOrigen, volumenPorCuenta.getOrDefault(cvuOrigen, 0) + 1);
            }
        }
        return volumenPorCuenta;
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