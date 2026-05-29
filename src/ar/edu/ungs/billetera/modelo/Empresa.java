package ar.edu.ungs.billetera.modelo;

import java.util.ArrayList;
import java.util.List;

public class Empresa {

    private String cuit;
    private String nombreFantasia;
    private String telefono;
    private String email;
    private String nombreContacto;
    private List<String> personasAutorizadas; // dni de cada autorizado

    public Empresa(String cuit, String nombreFantasia, String telefono, String email, String nombreContacto) {
        this.cuit = cuit;
        this.nombreFantasia = nombreFantasia;
        this.telefono = telefono;
        this.email = email;
        this.nombreContacto = nombreContacto;
        this.personasAutorizadas = new ArrayList<>();
    }

    public void agregarPersonaAutorizada(String dni) {
        personasAutorizadas.add(dni);
    }

    public boolean estaAutorizado(String dni) {
        return personasAutorizadas.contains(dni);
    }

    public String obtenerCuit() { return cuit; }
    public String obtenerNombreFantasia() { return nombreFantasia; }
    public String obtenerTelefono() { return telefono; }
    public String obtenerEmail() { return email; }
    public String obtenerNombreContacto() { return nombreContacto; }
    public List<String> obtenerPersonasAutorizadas() { return personasAutorizadas; }

    @Override
    public String toString() {
        return "Empresa | CUIT: " + cuit + " | Nombre: " + nombreFantasia;
    }
}