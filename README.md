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

**Gino Ciancia · <Tomas Clauser>** _no contesto mas lo mensajes :(_**

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

### Cuentas

| Clase | Regla principal |
|---|---|
| `CuentaRegular` | Saldo máximo $5.000.000 |
| `CuentaPremium` | Depósito mínimo $500.000 al abrir |
| `CuentaCorporativa` | Requiere empresa y usuario autorizado |

### Actividades

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

---

# Informe Técnico del Sistema billete.ar

## Billetera Virtual — Programación II

**Universidad Nacional de General Sarmiento**  
Instituto del Desarrollo Humano  
Licenciatura en Sistemas / Programación II  
1.° Cuatrimestre, 2026

---

**Autores:**  
Gino Ciancia  
\<Tomas Clauser\>

**Materia:** Programación II  
**Docentes:** Cátedra de Programación II  
**Fecha de entrega:** Mayo 2026

---

## Índice

1. [Introducción](#1-introducción)
2. [Decisiones de diseño](#2-decisiones-de-diseño)
   - 2.1 [Separación entre estado e historial](#21-consideración-sobre-la-separación-entre-estado-e-historial)
   - 2.2 [Decisiones de implementación](#22-decisiones-de-implementación)
3. [Descripción de las clases](#3-descripción-de-las-clases)
4. [Interfaz del sistema](#4-interfaz-del-sistema)
5. [Análisis de complejidad — realizarTransferencia](#5-análisis-de-complejidad--realizartransferencia)
6. [IREP de las clases](#6-irep-de-las-clases)
7. [Diagrama de clases](#7-diagrama-de-clases)
8. [Conceptos OOP aplicados](#8-conceptos-oop-aplicados)
9. [Diseños alternativos descartados](#9-diseños-alternativos-descartados)
10. [Documentación técnica complementaria](#10-documentación-técnica-complementaria)

---

## 1. Introducción

El presente informe documenta la implementación del sistema **billete.ar**, una billetera virtual que permite a sus usuarios administrar dinero a través de múltiples tipos de cuentas y modalidades de inversión. Este trabajo corresponde a la segunda parte del Trabajo Práctico Integrador de la materia Programación II.

La implementación se realizó en **Java 16+**, aplicando los principios de **programación orientada a objetos** —herencia, polimorfismo, abstracción e interfaces— impartidos durante la cursada. Se utilizaron las estructuras de datos de la biblioteca estándar de Java (`HashMap`, `ArrayList`, `List`, `Map`), priorizando la eficiencia en las operaciones más frecuentes.

El diseño presentado en la primera parte del trabajo práctico fue mantenido en su estructura general, incorporando las consideraciones indicadas por los docentes y las precisiones del enunciado de la segunda parte. La lógica de negocio se desacopla de cualquier vista a través de la interfaz pública `IBilletera`, que define el contrato del sistema sin exponer detalles de implementación.

El presente informe incluye las decisiones de diseño tomadas durante la implementación, la descripción de cada clase, la interfaz del sistema, el IREP de cada tipo de dato modelado, el diagrama de clases actualizado, el detalle de los conceptos OOP aplicados, el análisis de complejidad de la operación de transferencia y los diseños alternativos descartados.

---

## 2. Decisiones de diseño

### 2.1 Consideración sobre la separación entre estado e historial

Durante la corrección de la primera parte, los docentes señalaron que modelar `Inversion` heredando directamente de `Actividad` podría generar una tensión conceptual: una inversión representa un estado vivo (capital inmovilizado), mientras que una actividad representa la ocurrencia inmutable de un evento en el tiempo.

Luego de analizar esta observación, el grupo decidió **mantener el diseño original** por las siguientes razones:

1. **Coherencia con el dominio:** En el contexto de este sistema, toda inversión nace de un evento concreto: la decisión del usuario de inmovilizar capital en una fecha determinada. Ese evento es el que se registra en el historial. La inversión no existe independientemente de ese acto, por lo que modelarla como subclase de `Actividad` resulta coherente con el dominio modelado.

2. **Consistencia de datos:** Separar el estado de la inversión de su registro implicaría duplicar información (monto, fecha, cuenta de origen) en dos objetos distintos, introduciendo el riesgo de inconsistencias.

3. **Simplificación de interfaces:** El atributo `cuentaOrigen` heredado de `Actividad` permite que las subclases de `Inversion` calculen su resultado sin necesidad de parámetros adicionales, lo cual simplifica la interfaz de cada clase y evita redundancias.

4. **Recorrido polimórfico:** El historial global de `Billetera` almacena objetos de tipo `Actividad`, lo que permite recorrerlo de forma polimórfica sin distinguir entre transferencias e inversiones. Separar ambos conceptos requeriría mantener dos listas distintas o introducir una clase de registro adicional, complejizando el diseño sin beneficio aparente para los requerimientos del sistema.

Por estas razones, se considera que el diseño elegido es adecuado para la escala y los requerimientos del sistema modelado.

### 2.2 Decisiones de implementación

| Decisión | Justificación |
|---|---|
| **HashMap para usuarios, cuentas y aliases** | Se utilizan tres mapas en `Billetera` para acceder en **O(1)** a usuarios por DNI, cuentas por CVU y CVUs por alias. Esto optimiza todas las operaciones que reciben estos identificadores como parámetro. |
| **Acumulador `totalInvertido` en `Usuario`** | El total invertido por un usuario se mantiene como campo mutable que se actualiza en cada operación de inversión y precancelación. Esto permite consultar el total en **O(1)** sin recorrer cuentas ni historial. |
| **CVU generado por `Utilitarios.generarSiguienteCvu()`** | Garantiza identificadores únicos y secuenciales de 22 dígitos, consistentes con el formato esperado por el sistema (`"00000031%014d"`). |
| **`Precancelable` como interfaz** | La precancelabilidad no se modela como método booleano sino como interfaz Java. El sistema detecta la capacidad con `instanceof Precancelable`, sin necesidad de métodos de consulta adicionales. La ausencia de la interfaz es en sí misma la respuesta. |
| **`StringBuilder` en métodos de historial** | La construcción de los strings de cada actividad se realiza con `StringBuilder` para modificar el string de forma eficiente, evitando la creación de objetos intermedios innecesarios en cada concatenación. |
| **Validaciones en constructores** | Las precondiciones de cada tipo (saldo mínimo en `CuentaPremium`, monto mínimo y tipo de cuenta en `FondoLiquidez`) se validan en el constructor. Si no se cumplen, el objeto directamente no se crea, lanzando una excepción. La restricción vive en la clase, no en el sistema externo. |

---

## 3. Descripción de las clases

### `Billetera`
Clase principal del sistema, implementa `IBilletera`. Administra tres mapas: `usuarios` por DNI, `cuentas` por CVU y `aliases` por nombre. Mantiene el historial global de actividades como `List<Actividad>`. Provee todas las operaciones definidas en la interfaz pública.

### `Usuario`
Representa a una persona registrada en la plataforma. Almacena DNI, nombre, teléfono, email, lista de cuentas y el acumulador `totalInvertido`. Permite agregar cuentas y actualizar el total invertido en **O(1)** mediante el método `registrarInversion(double monto)`, que acepta montos positivos (constitución) y negativos (precancelación).

### `Empresa`
Representa una empresa registrada en el sistema. Almacena CUIT, nombre de fantasía, teléfono, email, nombre de contacto y la lista de DNIs de personas autorizadas a operar en su nombre.

### `Cuenta` (abstracta)
Clase abstracta que encapsula los atributos comunes a todo tipo de cuenta: CVU, alias y saldo. Define el contrato polimórfico `puedeOperar(double)` y el método abstracto `obtenerTipo()`. Implementa `debitar()` y `acreditar()` como operaciones comunes a todas las subclases.

### `CuentaRegular`
Subclase de `Cuenta`. Sobrescribe `puedeOperar(double)` validando que el saldo resultante no supere los **$5.000.000** (límite de acreditación). El saldo inicial debe ser ≤ 5M, validado en el constructor.

### `CuentaPremium`
Subclase de `Cuenta`. Valida en el constructor que el depósito inicial sea de al menos **$500.000**. Sobrescribe `puedeOperar(double)` verificando saldo suficiente para el débito. Puede operar aunque el saldo baje por operaciones posteriores.

### `CuentaCorporativa`
Subclase de `Cuenta` para uso empresarial. Almacena el CUIT de la empresa vinculada y la bandera `usuarioAutorizado`. Sobrescribe `puedeOperar(double)` verificando que el usuario esté autorizado **y** que haya saldo suficiente para el débito. Es la única desde la cual se pueden constituir inversiones de tipo `FondoLiquidez`.

### `Actividad` (abstracta)
Clase abstracta que representa toda operación registrada en el sistema. Define los atributos comunes: `id` (generado con `UUID.randomUUID()`), `fecha` (`LocalDate`) y `cuentaOrigen` (`Cuenta`).

### `Transferencia`
Subclase de `Actividad`. Representa el movimiento de fondos entre dos cuentas. El método `ejecutar()` valida el saldo de la cuenta origen y el límite de la cuenta destino (a través de `puedeOperar()`) antes de realizar el débito y el crédito. Almacena la `cuentaDestino` como atributo adicional.

### `Inversion` (abstracta)
Subclase de `Actividad`. Define los atributos comunes a toda inversión: `fechaConstitucion`, `plazo` (en días) y `monto`. Declara el método abstracto `calcularResultado()` que cada subclase implementa de forma polimórfica.

### `Precancelable` (interfaz)
Define el contrato `calcularResultadoPrecancelado()`. Las clases que la implementan se comprometen a devolver el capital más la mitad de la rentabilidad generada hasta la fecha de precancelación.

### `RentaFija`
Subclase de `Inversion` que implementa `Precancelable`. Almacena una `tasaInteres` (TNA 20% por defecto). Calcula el resultado usando la TNA sobre el monto y los días transcurridos desde la constitución, consultando la fecha actual a `Utilitarios.hoy()`. El resultado precancelado retorna el capital más la mitad de los intereses devengados.

### `VinculadaDivisa`
Subclase de `Inversion` que implementa `Precancelable`. Almacena `divisa`, `tasaInteres` y `cotizacionInicial` (consultada a `Utilitarios` al momento de constitución). Convierte el monto a divisas usando la cotización inicial, calcula intereses en esa divisa, y reconvierte a pesos usando la cotización actual de `Utilitarios` al momento del cálculo.

### `FondoLiquidez`
Subclase de `Inversion` **exclusiva para cuentas corporativas**. Requiere un monto mínimo de **$20.000.000** y que la `cuentaOrigen` sea instancia de `CuentaCorporativa`, ambos validados en el constructor. Utiliza el activo "FLE" con una tasa del **8% anual**. **No implementa** `Precancelable`.

---

## 4. Interfaz del sistema

| # | Operación | Descripción |
|---|---|---|
| 1 | `registrarUsuario(dni, nombre, telefono, email)` | Registra un nuevo usuario. Valida formato de email con regex. |
| 2 | `registrarEmpresa(cuit, nombreFantasia, telefono, email, nombreContacto)` | Registra una nueva empresa. |
| 3 | `agregarPersonaAutorizada(cuitEmpresa, dniAutorizado)` | Agrega un DNI autorizado a operar en la empresa. |
| 4 | `crearCuentaRegular(dniUsuario, alias)` | Crea una cuenta regular con saldo $0. Genera CVU automáticamente. |
| 5 | `crearCuentaPremium(dniUsuario, alias, depositoInicial)` | Crea una cuenta premium con depósito inicial mínimo de **$500.000**. |
| 6 | `crearCuentaCorporativa(dniUsuario, alias, cuitEmpresa)` | Crea una cuenta corporativa validando autorización del usuario en la empresa. |
| 7 | `obtenerCuentas(dniUsuario)` | Devuelve la lista de cuentas en formato `"Tipo: alias (CVU)"`. |
| 8 | `obtenerSaldoDisponible(cvu)` | Devuelve el saldo disponible de una cuenta. |
| 9 | `realizarTransferencia(cvuOrigen, cvuDestino, monto)` | Transfiere fondos entre cuentas validando límites de ambas. |
| 10 | `realizarInversionRentaFija(dni, cvu, monto, plazoDias)` | Constituye una inversión de renta fija con TNA del 20%. |
| 11 | `realizarInversionDivisa(dni, cvu, monto, plazoDias, divisa, tasa)` | Constituye una inversión vinculada a divisa. |
| 12 | `realizarInversionLiquidez(dni, cvu, monto, plazoDias)` | Constituye un fondo de liquidez desde cuenta corporativa. |
| 13 | `precancelarInversion(dni, cvu, idInversion)` | Precancela una inversión activa devolviendo capital e intereses parciales. |
| 14 | `consultarCvu(alias)` | Devuelve el CVU asociado a un alias. |
| 15 | `consultarHistorialGlobal()` | Lista todas las actividades del sistema. |
| 16 | `consultarHistorialCuenta(cvu)` | Lista las actividades de una cuenta específica. |
| 17 | `consultarHistorialUsuario(dniUsuario)` | Lista las actividades de todas las cuentas de un usuario. |
| 18 | `obtenerTotalInvertido(dniUsuario)` | Devuelve el total invertido por un usuario en **O(1)**. |
| 19 | `cuentasConMayorVolumen(cantidadTop)` | Devuelve las N cuentas con mayor cantidad de transacciones. |

---

## 5. Análisis de complejidad — realizarTransferencia

### Código analizado

```java
public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
    if (cvuOrigen == null || cvuOrigen.isBlank()) { ... }          // O(1)
    if (cvuDestino == null || cvuDestino.isBlank()) { ... }        // O(1)
    if (monto <= 0) { ... }                                        // O(1)
    if (!cuentas.containsKey(cvuOrigen)) { ... }                   // O(1)
    if (!cuentas.containsKey(cvuDestino)) { ... }                  // O(1)
    if (cvuOrigen.equals(cvuDestino)) { ... }                      // O(1)
    Cuenta origen = cuentas.get(cvuOrigen);                        // O(1)
    Cuenta destino = cuentas.get(cvuDestino);                      // O(1)
    Transferencia transferencia = new Transferencia(...);           // O(1)
    transferencia.ejecutar();                                      // O(1)
    historialGlobal.add(transferencia);                            // O(1)
}
```

### Análisis línea por línea

#### Validaciones iniciales
Las primeras tres validaciones (`null`, `isBlank()`, `monto <= 0`) son operaciones de comparación directa sobre valores primitivos o referencias. No dependen del tamaño de ninguna colección ni recorren estructura alguna. Su costo es siempre el mismo sin importar el estado del sistema: **O(1)**.

#### Búsqueda en cuentas con `containsKey()` y `get()`
Esta es la operación más relevante del análisis. `cuentas` es un `HashMap<String, Cuenta>` donde la clave es el CVU.

Un `HashMap` funciona aplicando una función de hash a la clave para determinar en qué posición del arreglo interno buscar el valor. En el caso ideal — sin colisiones — esto implica:

1. Calcular `hash(cvuOrigen)` → operación aritmética de costo fijo.
2. Acceder a la posición `hash % capacidad` del arreglo interno → acceso directo por índice.
3. Comparar la clave encontrada con la buscada → comparación de strings de longitud acotada.

Ninguno de estos pasos depende de cuántas cuentas haya registradas en el sistema. Si hay 10 cuentas o 10.000.000, el acceso sigue siendo directo. Por esta razón, `containsKey()` y `get()` sobre un `HashMap` son **O(1) amortizado**.

El término "amortizado" significa que en casos excepcionales de colisión de hash, el costo puede subir temporalmente, pero promediado sobre muchas operaciones el costo por operación se mantiene constante. Java maneja esto automáticamente redimensionando el arreglo interno cuando la cantidad de colisiones supera un umbral.

Si en su lugar se hubiera utilizado una `List<Cuenta>`, buscar una cuenta por CVU requeriría recorrer la lista comparando uno por uno hasta encontrarla, resultando en un costo de **O(n)** donde *n* es la cantidad de cuentas. Con `HashMap` ese costo se elimina completamente.

#### Comparación `cvuOrigen.equals(cvuDestino)`
Compara dos strings de longitud acotada (los CVUs tienen siempre 22 caracteres, generados por `Utilitarios`). Al ser longitud fija, la comparación es **O(1)**.

#### Construcción de `Transferencia`
El constructor de `Transferencia` asigna referencias a los objetos recibidos como parámetros y genera un `UUID` como identificador. La generación del UUID es una operación de costo fijo que no depende del estado del sistema: **O(1)**.

#### `transferencia.ejecutar()`
Este método llama a `puedeOperar()` en la cuenta origen y en la cuenta destino, y luego realiza un débito y un crédito. Tanto `puedeOperar()` como `debitar()` y `acreditar()` son operaciones aritméticas sobre valores `double` sin recorridos: **O(1)**.

#### `historialGlobal.add(transferencia)`
`historialGlobal` es un `ArrayList`. Agregar un elemento al final de un `ArrayList` es **O(1) amortizado**: en la mayoría de los casos simplemente se asigna el elemento en la siguiente posición disponible. Ocasionalmente, cuando el arreglo interno está lleno, Java lo redimensiona duplicando su capacidad — operación de costo O(n) — pero esto ocurre con frecuencia decreciente, por lo que el costo amortizado por inserción se mantiene en **O(1)**.

### Álgebra de Órdenes

Sea *T(n)* la complejidad total del método, donde *n* es la cantidad de cuentas registradas en el sistema. Sumando el costo de cada operación:

```
T(n) = O(1) + O(1) + O(1)     ← validaciones iniciales
     + O(1) + O(1)             ← containsKey x2
     + O(1)                    ← equals
     + O(1) + O(1)             ← get x2
     + O(1)                    ← new Transferencia
     + O(1)                    ← ejecutar
     + O(1)                    ← historialGlobal.add
```

Aplicando la propiedad de la suma en el Álgebra de Órdenes:

```
O(f) + O(g) = O(max(f, g))
```

Como todas las operaciones son O(1):

```
T(n) = O(max(1, 1, 1, ..., 1))
T(n) = O(1)
```

Aplicando la propiedad de la constante multiplicativa:

```
k · O(1) = O(1)    para cualquier constante k
```

### Complejidad lograda: O(1)

La complejidad de `realizarTransferencia` es **constante**. El método realiza siempre la misma cantidad de operaciones independientemente de cuántas cuentas, usuarios o actividades existan en el sistema. La decisión de usar `HashMap` para la colección `cuentas` es la que hace posible este resultado: garantiza que la búsqueda y el acceso por CVU sean siempre **O(1)**, sin importar el volumen de datos.

---

## 6. IREP de las clases

### `Billetera`
- `usuarios != null` y no contiene nulos.
- `empresas != null` y no contiene nulos.
- `cuentas != null` y no contiene nulos.
- `aliases != null`, no contiene nulos, y cada alias mapea a un CVU existente en `cuentas`.
- `historialGlobal != null` y no contiene nulos.

### `Usuario`
- `id != null` y no vacío.
- `nombre != null` y no vacío.
- `cuentas != null`, no contiene nulos, y no hay dos cuentas con el mismo CVU.
- `totalInvertido >= 0` y es consistente con la suma de montos de inversiones activas.

### `Empresa`
- `cuit != null` y no vacío.
- `nombreFantasia != null` y no vacío.
- `personasAutorizadas != null` y no contiene nulos ni duplicados.

### `Cuenta` (abstracta)
- `cvu != null` y no vacío.
- `alias != null` y no vacío.
- `saldo >= 0`.

### `CuentaRegular`
- Hereda IREP de `Cuenta`.
- `saldo <= 5.000.000` después de cada acreditación.

### `CuentaPremium`
- Hereda IREP de `Cuenta`.
- El depósito inicial `>= 500.000` se valida al momento de apertura. El saldo puede bajar por operaciones posteriores.

### `CuentaCorporativa`
- Hereda IREP de `Cuenta`.
- `cuit != null` y no vacío.
- `usuarioAutorizado == true` para poder realizar operaciones.

### `Actividad` (abstracta)
- `id != null` y no vacío.
- `fecha != null`.
- `cuentaOrigen != null` y es una cuenta existente en el sistema.

### `Transferencia`
- Hereda IREP de `Actividad`.
- `monto > 0`.
- `cuentaDestino != null`, distinta de `cuentaOrigen`, y es una cuenta existente en el sistema.

### `Inversion` (abstracta)
- Hereda IREP de `Actividad`.
- `plazo > 0`.
- `monto > 0`.
- `fechaConstitucion != null`.

### `RentaFija`
- Hereda IREP de `Inversion`.
- `tasaInteres > 0`.
- Implementa `Precancelable`.

### `VinculadaDivisa`
- Hereda IREP de `Inversion`.
- `divisa != null` y no vacía.
- `tasaInteres > 0`.
- `cotizacionInicial > 0`.
- Implementa `Precancelable`.

### `FondoLiquidez`
- Hereda IREP de `Inversion`.
- `monto >= 20.000.000`.
- La cuenta de origen debe ser de tipo `CuentaCorporativa`, validado en el constructor.
- **No implementa** `Precancelable`.

---

## 7. Diagrama de clases

```
                        +------------------+
                        |    Billetera     |
                        | implements       |
                        | IBilletera       |
                        +------------------+
                        | -usuarios: Map   |
                        | -empresas: Map   |
                        | -cuentas: Map    |
                        | -aliases: Map    |
                        | -historial: List |
                        +------------------+
                               |  |
               +---------+     |  |     +---------+
               | Usuario |<----+  +---->| Empresa |
               +---------+              +---------+
               | -dni    |              | -cuit   |
               | -nombre |              | -nombre |
               | -cuentas|              | -autori.|
               +---------+              +---------+
                    |
                    | (tiene)
                    v
          +------------------+
          |   Cuenta         |  <<abstract>>
          +------------------+
          | -cvu: String     |
          | -alias: String   |
          | -saldo: double   |
          +------------------+
          | #puedeOperar()   |  <<abstract>>
          | #obtenerTipo()   |  <<abstract>>
          | +debitar()       |
          | +acreditar()     |
          +------------------+
                 / | \
                /  |  \
               /   |   \
    +--------+ +-------+ +-------------+
    | Regular| |Premium| |Corporativa  |
    +--------+ +-------+ +-------------+
    |MAX:5M  | |MIN:500k| |-cuit       |
    +--------+ +-------+ | -autori.    |
                          +-------------+


          +------------------+
          |   Actividad      |  <<abstract>>
          +------------------+
          | -id: UUID        |
          | -fecha: LocalDate|
          | -cuentaOrigen    |
          +------------------+
                 /    \
                /      \
               /        \
    +--------------+  +------------------+
    | Transferencia|  |   Inversion      |  <<abstract>>
    +--------------+  +------------------+
    | -monto       |  | -fechaConst.     |
    | -cuentaDest. |  | -plazo: int      |
    | +ejecutar()  |  | -monto: double   |
    +--------------+  | #calcResultado() |  <<abstract>>
                      +------------------+
                           /    |    \
                          /     |     \
                         /      |      \
               +---------+ +--------+ +-------------+
               |RentaFija| |Vinculada| |FondoLiquidez|
               +---------+ |Divisa  | +-------------+
               |TNA:20%  | +--------+ |MIN: 20M     |
               |Precanc. | |Precanc. | |activo: FLE  |
               +---------+ +--------+ |8% anual     |
                    |            |     +-------------+
                    |            |
                    +------+-----+
                           |
                  +--------+--------+
                  | Precancelable   |  <<interface>>
                  +-----------------+
                  | calcResultado() |
                  | Precancelado()  |
                  +-----------------+
```

---

## 8. Conceptos OOP aplicados

### Herencia
Se aplica en las tres jerarquías principales del sistema:

- **Jerarquía de cuentas:** `CuentaRegular`, `CuentaPremium` y `CuentaCorporativa` extienden `Cuenta`.
- **Jerarquía de actividades:** `Transferencia` e `Inversion` extienden `Actividad`.
- **Jerarquía de inversiones:** `RentaFija`, `VinculadaDivisa` y `FondoLiquidez` extienden `Inversion`.

En todos los casos, las subclases heredan los atributos y comportamientos comunes definidos en la clase padre y especializan únicamente lo que les es propio.

### Polimorfismo

- **`puedeOperar(double monto)`:** cada subclase de `Cuenta` lo implementa según sus propias reglas de validación. `CuentaRegular` verifica el límite de acreditación de $5M; `CuentaPremium` y `CuentaCorporativa` verifican saldo suficiente para el débito; `CuentaCorporativa` además exige autorización del usuario.
- **`calcularResultado()`:** cada subclase de `Inversion` implementa su propia fórmula de cálculo (TNA lineal, conversión por divisa, cotización de activo).
- **Historial polimórfico:** el historial global almacena `List<Actividad>`, recorriéndose de forma polimórfica sin distinguir tipos en la mayor parte de las operaciones.

### Abstracción

`Cuenta`, `Actividad` e `Inversion` son clases abstractas. No pueden instanciarse directamente porque no existe una "cuenta genérica" ni una "inversión genérica" — siempre son de un tipo concreto. Los métodos abstractos `puedeOperar()`, `calcularResultado()` y `obtenerTipo()` definen el contrato que cada subclase debe cumplir.

### Interfaces

Se utilizan dos interfaces:

- **`IBilletera`:** define el contrato público del sistema, desacoplando la vista de la lógica de negocio. Permite que cualquier cliente (consola, interfaz gráfica, API REST) interactúe con el sistema sin conocer detalles de implementación.
- **`Precancelable`:** actúa como contrato de capacidad. Las clases que la implementan se comprometen a proveer `calcularResultadoPrecancelado()`. Su uso con `instanceof` permite detectar la capacidad sin métodos booleanos adicionales.

### Sobrescritura

Se aplica en `puedeOperar()`, `calcularResultado()`, `calcularResultadoPrecancelado()`, `obtenerTipo()` y `toString()` en todas las clases del modelo. La anotación `@Override` se usa consistentemente para que el compilador verifique que el método efectivamente existe en la clase padre.

### StringBuilder

Se utiliza en los métodos `consultarHistorialGlobal()`, `consultarHistorialCuenta()` y en el `toString()` de `Billetera` para construir strings que se arman de forma incremental. Su uso se justifica en la necesidad de modificar el string durante la construcción, evitando la creación de objetos `String` intermedios innecesarios en cada concatenación.

### Iteradores y Stream API

- Se utilizan ciclos **for-each** para recorrer todas las colecciones del sistema: el historial global de actividades, la lista de cuentas de un usuario, el mapa de usuarios y el mapa de empresas.
- En `cuentasConMayorVolumen` se utiliza además la **API de Stream** con `sorted()`, `limit()` y `forEach()` para ordenar y filtrar el resultado de forma declarativa.
- El método auxiliar `buscarDniPorCvu()` recorre el mapa de usuarios y, para cada usuario, itera sobre sus cuentas hasta encontrar el CVU buscado (complejidad **O(n × m)**).

### instanceof y Pattern Matching

Se utiliza `instanceof` con pattern matching (Java 16+) para:

- Detectar si una `Actividad` es `Transferencia` o `Inversion` en los métodos de consulta de historial.
- Verificar si una `Inversion` implementa `Precancelable` en `precancelarInversion()`.
- Validar que la cuenta de origen sea `CuentaCorporativa` en `FondoLiquidez` y en `realizarInversionLiquidez()`.

---

## 9. Diseños alternativos descartados

### `calcularResultado(cuenta)` con parámetro
Se evaluó pasar la cuenta como parámetro al método `calcularResultado()`. Esta opción fue descartada porque la inversión ya conoce su cuenta de origen a través del atributo `cuentaOrigen` heredado de `Actividad`. Pasar la cuenta como parámetro sería información redundante y crearía la posibilidad de inconsistencias entre el parámetro y el estado interno del objeto.

### `esPrecancelable()` como método booleano
Se evaluó modelar la precancelabilidad como un método `esPrecancelable()` que retorna `true` o `false` en cada subclase. Esta opción fue descartada porque traslada la responsabilidad al llamador. La interfaz `Precancelable` es más robusta: la capacidad es detectable directamente con `instanceof`, y el compilador garantiza que quien la implementa provee el método `calcularResultadoPrecancelado()`.

### `totalInvertido` calculado dinámicamente
Se evaluó calcular el total invertido recorriendo las cuentas e inversiones del usuario en cada consulta, en **O(n)**. Esta opción fue descartada en favor de un campo acumulador en `Usuario`, que se actualiza en cada operación de inversión y precancelación, reduciendo la consulta a **O(1)**.

### Tipo como campo `String` en `Cuenta` e `Inversion`
Se evaluó modelar el tipo como un campo `String`. Esta opción fue descartada porque traslada la lógica de comportamiento a condicionales externos (cadenas de `if-else` o `switch`), rompe el polimorfismo y dificulta la extensión del sistema. La herencia y las clases abstractas permiten que cada tipo encapsule su propio comportamiento sin modificar las clases existentes.

### CVU generado con `UUID.randomUUID()`
Durante las etapas iniciales de implementación se utilizó `UUID.randomUUID()` para generar los CVUs. Esta opción fue reemplazada por `Utilitarios.generarSiguienteCvu()` para respetar el formato de 22 dígitos (con prefijo fijo `"00000031"` y contador secuencial) esperado por el sistema, y para garantizar consistencia con el código cliente provisto por la cátedra.

---

## 10. Documentación técnica complementaria

### 10.1 Estructura del repositorio

```
src/
└── ar/edu/ungs/billetera/
    ├── IBilletera.java              ← Interfaz pública del sistema
    ├── Principal.java               ← Simulación del sistema
    ├── Utilitarios.java             ← Fecha, cotizaciones y generación de CVU
    └── modelo/
        ├── Billetera.java           ← Clase principal (implements IBilletera)
        ├── Usuario.java             ← Modelo de usuario
        ├── Empresa.java             ← Modelo de empresa
        ├── cuenta/
        │   ├── Cuenta.java          ← Clase abstracta base
        │   ├── CuentaRegular.java   ← Saldo máximo $5.000.000
        │   ├── CuentaPremium.java   ← Depósito mínimo $500.000
        │   └── CuentaCorporativa.java ← Requiere empresa y autorización
        └── actividad/
            ├── Actividad.java       ← Clase abstracta base
            ├── Transferencia.java   ← Movimiento entre cuentas
            └── inversion/
                ├── Inversion.java   ← Clase abstracta base
                ├── Precancelable.java ← Interfaz de capacidad
                ├── RentaFija.java   ← TNA 20%, precancelable
                ├── VinculadaDivisa.java ← Rendimiento por divisa, precancelable
                └── FondoLiquidez.java ← Exclusivo corporativo, $20M mínimo

test/
└── ar/edu/ungs/billetera/
    └── BilleteraTest.java           ← Suite de pruebas unitarias
```

### 10.2 Requisitos de entorno

| Componente | Versión |
|---|---|
| Java | 16 o superior |
| JUnit | 4.13.1 |
| IDE | IntelliJ IDEA 2025 |
| Sistema operativo | Independiente (multiplataforma) |

### 10.3 Ejecución

**Simulación:** Ejecutar `Principal.java` en `src/ar/edu/ungs/billetera/`

**Tests:** Ejecutar `BilleteraTest.java` en `test/ar/edu/ungs/billetera/`

### 10.4 Glosario

| Término | Definición |
|---|---|
| **CVU** | Clave Virtual Uniforme. Identificador único de 22 dígitos para cada cuenta. |
| **TNA** | Tasa Nominal Anual. Porcentaje de rendimiento expresado en base anual. |
| **FLE** | Activo financiero de Fondo de Liquidez Empresarial. |
| **Precancelación** | Cancelación anticipada de una inversión antes de su fecha de vencimiento. |
| **IREP** | Invariante de Representación. Condición que debe cumplir el estado interno de un objeto en todo momento. |

### 10.5 Convenciones de código

- Nomenclatura: `camelCase` para métodos y variables, `PascalCase` para clases.
- Métodos getter con prefijo `obtener` (ej.: `obtenerSaldo()`, `obtenerCvu()`).
- Excepciones: `IllegalArgumentException` para parámetros inválidos, `IllegalStateException` para violaciones de reglas de negocio.
- Formato de fechas: `java.time.LocalDate` (ISO 8601).
- Formato de CVU: `"00000031%014d"` (22 dígitos, prefijo fijo + contador secuencial).

---

<div align="center">

*Universidad Nacional de General Sarmiento · Programación II · 1.° Cuatrimestre 2026*

*Documento generado como parte de la entrega del Trabajo Práctico Integrador — Segunda Parte*

</div>
