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

    public String getCuit() { return cuit; }
    public String getNombreFantasia() { return nombreFantasia; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getNombreContacto() { return nombreContacto; }
    public List<String> getPersonasAutorizadas() { return personasAutorizadas; }

    @Override
    public String toString() {
        return "Empresa | CUIT: " + cuit + " | Nombre: " + nombreFantasia;
    }
}