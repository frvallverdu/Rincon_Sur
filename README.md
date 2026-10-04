# Segmentación de Flujo de Clientes - Rincón Sur

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Organización:** Tienda Departamental Rincón Sur  
**Fecha de Entrega:** 28-08-2026  
**Repositorio:** https://github.com/frvallverdu/Rincon_Sur.git  

## Integrantes
* Francisco Vallverdu
* Diego Ramos

---

## Descripción del Proyecto
Este proyecto busca resolver la problemática de conteo y segmentación de clientes en la Tienda Departamental Rincón Sur. Actualmente, la tienda cuenta con cuatro accesos de clientes, pero el sistema de conteo entrega un número total agregado sin diferenciar entre personas que utilizan el local como paso peatonal hacia la tienda vecina de mejoramiento del hogar y clientes que efectivamente realizan compras.

La solución contempla una aplicación móvil para uso interno del personal de tienda que permita clasificar los flujos de ingreso por acceso y registrar datos complementarios de segmentación (como rango etario) para apoyar la toma de decisiones comerciales.

---

## Estado del Desarrollo
El proyecto se encuentra en etapa inicial de desarrollo (MVP en progreso).

### Estado Actual
* Configuración inicial del proyecto en Android Studio con Jetpack Compose.
* Desarrollo de la primera pantalla de la aplicación.
* Estructuración inicial de carpetas y paquetes de la arquitectura.

### Próximos Pasos
1. Finalizar la navegación entre pantallas (UI/UX).
2. Implementar la arquitectura MVVM (ViewModels y Estados).
3. Configurar la base de datos local con Room / SQLite.
4. Desarrollar el backend en Spring Boot con microservicios y API REST.
5. Conectar la aplicación móvil con la API REST utilizando Retrofit.

---

## Tecnologías Utilizadas

### Frontend (Móvil)
* Lenguaje: Kotlin
* IDE: Android Studio
* Interfaz de Usuario: Jetpack Compose y Material Design 3
* Arquitectura: MVVM (Model-View-ViewModel)
* Persistencia Local: Room / SQLite
* Conexión HTTP: Retrofit

### Backend & Integración
* Framework: Java Spring Boot (Microservicios y API REST)
* Pruebas: Pruebas unitarias
* Datos: Datos ficticios / sintéticos (sin credenciales ni información confidencial)

---

## Instrucciones para Clonar el Repositorio

```bash
git clone [https://github.com/frvallverdu/Rincon_Sur.git](https://github.com/frvallverdu/Rincon_Sur.git)
cd Rincon_Sur
