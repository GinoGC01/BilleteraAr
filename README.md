<div align="center">

```
                   ██████╗ ██╗██╗     ██╗     ███████╗████████╗███████╗      █████╗ ██████╗
                    ██╔══██╗██║██║     ██║     ██╔════╝╚══██╔══╝██╔════╝     ██╔══██╗██╔══██╗
                    ██████╔╝██║██║     ██║     █████╗     ██║   █████╗       ███████║██████╔╝
                    ██╔══██╗██║██║     ██║     ██╔══╝     ██║   ██╔══╝       ██╔══██║██╔══██╗
                    ██████╔╝██║███████╗███████╗███████╗   ██║   ███████╗ ██╗ ██║  ██║██║  ██║
                    ╚═════╝ ╚═╝╚══════╝╚══════╝╚══════╝   ╚═╝   ╚══════╝ ╚═╝ ╚═╝  ╚═╝╚═╝  ╚═╝
```

### billetera virtual · programación II · UNGS · 2026

![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-4.13.1-green?style=flat-square&logo=junit5&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-2025-purple?style=flat-square&logo=intellijidea&logoColor=white)
![Estado](https://img.shields.io/badge/estado-en_desarrollo-blue?style=flat-square)

**Gino Ciancia · <s>Tomas Clauser</s></b> _no contesto mas lo mensajes :(_**

</div>


## ¿Qué es billete.ar?

Sistema de billetera virtual que permite administrar dinero a través de múltiples tipos de cuentas y modalidades de inversión. Implementado en Java aplicando principios de **programación orientada a objetos**: herencia, polimorfismo, abstracción e interfaces.

> Este proyecto corresponde al Trabajo Práctico Integrador de Programación II, UNGS, 1° cuatrimestre 2026.

---

## Estructura del proyecto

```
src/
└── ar/edu/ungs/billetera/
    ├── IBilletera.java              ← interfaz pública del sistema
    ├── Principal.java               ← simulación del sistema
    ├── Utilitarios.java             ← fecha y cotizaciones
    └── modelo/
        ├── Billetera.java           ← clase principal (implements IBilletera)
        ├── Usuario.java
        ├── Empresa.java
        ├── cuenta/        
        │   ├── Cuenta.java          ← abstracta
        │   ├── CuentaRegular.java
        │   ├── CuentaPremium.java
        │   └── CuentaCorporativa.java
        └── actividad/
            ├── Actividad.java       ← abstracta
            ├── Transferencia.java
            └── inversion/
                ├── Inversion.java   ← abstracta
                ├── Precancelable.java  ← interfaz
                ├── RentaFija.java
                ├── VinculadaDivisa.java
                └── FondoLiquidez.java

test/
└── ar/edu/ungs/billetera/
    └── BilleteraTest.java
```

---

## Jerarquía de clases

### 🏦 Cuentas

| Clase | Regla principal |
|---|---|
| `CuentaRegular` | Saldo máximo $5.000.000 |
| `CuentaPremium` | Depósito mínimo $500.000 al abrir |
| `CuentaCorporativa` | Requiere empresa y usuario autorizado |

### 📋 Actividades

| Clase | Descripción |
|---|---|
| `Transferencia` | Movimiento de fondos entre dos cuentas |
| `RentaFija` | Tasa fija anual (TNA). Precancelable |
| `VinculadaDivisa` | Rendimiento atado a divisa. Precancelable |
| `FondoLiquidez` | Solo cuentas corporativas. Monto mínimo $20.000.000 |

---

## Conceptos OOP aplicados

```java
// Herencia y abstracción
public abstract class Cuenta { ... }
public class CuentaRegular extends Cuenta { ... }

// Polimorfismo
public abstract boolean puedeOperar(double monto);

// Interfaces
public interface Precancelable {
    double calcularResultadoPrecancelado();
}

// instanceof para detección de capacidades
if (inversion instanceof Precancelable p) {
    return p.calcularResultadoPrecancelado();
}
```

| Concepto | Dónde se aplica |
|---|---|
| Herencia | `Cuenta`, `Actividad`, `Inversion` y sus subclases |
| Polimorfismo | `puedeOperar()`, `calcularResultado()` |
| Abstracción | Clases abstractas en los tres niveles de la jerarquía |
| Interfaces | `IBilletera`, `Precancelable` |
| Sobrescritura | `toString()`, `calcularResultado()`, `puedeOperar()` |

---

## Operaciones disponibles

| # | Operación | Descripción |
|---|---|---|
| 1 | `registrarUsuario` | Registra un nuevo usuario |
| 2 | `crearCuentaRegular / Premium / Corporativa` | Crea cuentas de distintos tipos |
| 3 | `obtenerCuentas` | Lista las cuentas de un usuario |
| 4 | `obtenerSaldoDisponible` | Consulta el saldo por CVU |
| 5 | `realizarTransferencia` | Transfiere fondos entre cuentas |
| 6 | `realizarInversionRentaFija / Divisa / Liquidez` | Constituye inversiones |
| 7 | `consultarHistorialGlobal` | Lista todas las actividades |
| 8 | `consultarHistorialCuenta / Usuario` | Historial filtrado |
| 9 | `obtenerTotalInvertido` | Total invertido por usuario en O(1) |
| 10 | `cuentasConMayorVolumen` | Top N cuentas por transacciones |
| 11 | `registrarEmpresa` | Registra una empresa |
| 12 | `agregarPersonaAutorizada` | Autoriza usuarios a operar en una empresa |
| 13 | `precancelarInversion` | Cancela anticipadamente una inversión |
| 14 | `consultarCvu` | Obtiene el CVU asociado a un alias |

---

## Cómo ejecutar

### Requisitos
- Java 16 o superior
- IntelliJ IDEA
- JUnit 4.13.1

### Simulación
```
Ejecutar Principal.java en src/ar/edu/ungs/billetera/
```

### Tests
```
Ejecutar BilleteraTest.java en test/ar/edu/ungs/billetera/
```

---

## Decisiones de diseño destacadas

- **`Precancelable` como interfaz**: la precancelabilidad se detecta con `instanceof`, sin métodos booleanos. La ausencia de la interfaz es la respuesta.
- **`totalInvertido` como acumulador**: se actualiza en cada inversión, permitiendo consulta en **O(1)**.
- **`HashMap` para usuarios y cuentas**: búsqueda por DNI y CVU en **O(1)**.
- **CVU generado por `Utilitarios`**: identificador único secuencial de 22 dígitos.
- **Historial polimórfico**: `List<Actividad>` almacena transferencias e inversiones sin distinguir tipos.
- **`StringBuilder` en historial**: construcción eficiente de strings en los métodos de consulta.

---

<div align="center">

*Universidad Nacional de General Sarmiento · Programación II · 2026*

</div>
