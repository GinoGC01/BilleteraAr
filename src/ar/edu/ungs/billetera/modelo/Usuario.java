package ar.edu.ungs.billetera.modelo;

import ar.edu.ungs.billetera.modelo.cuenta.Cuenta;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String dni;
    private String nombre;
    private String email;
    private String telefono;
    private List<Cuenta> cuentas;
    private double totalInvertido;

    public Usuario(String dni, String nombre, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.cuentas = new ArrayList<>();
        this.totalInvertido = 0;
        this.email = email;
        this.telefono = telefono;
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public void registrarInversion(double monto) {
        this.totalInvertido += monto;
    }

    public String getId() { return dni; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public List<Cuenta> getCuentas() { return cuentas; }
    public double getTotalInvertido() { return totalInvertido; }

    @Override
    public String toString() {
        return "Usuario | DNI: " + dni + " | Nombre: " + nombre +
                " | Total invertido: $" + totalInvertido;
    }
}