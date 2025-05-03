Proyecto: Firma y Verificación de Remitos con Blockchain (POC)

Descripción general:
Este proyecto es una Prueba de Concepto (POC) de una aplicación web construida con Spring Boot que permite a las empresas gestionar remitos digitales con un nivel adicional de seguridad e integridad basado en tecnología blockchain.
El objetivo es simular cómo se podría aplicar blockchain para garantizar que los documentos no sean modificados, almacenando un hash único generado desde los datos del remito.

Tecnologías utilizadas:
Java 17
Spring Boot
H2 Database
Lombok
Swagger/OpenAPI para pruebas
Hashing SHA-256 local


¿Qué funcionalidades implementa?
Crear remitos digitales a través de una API REST.
Calcular un hash SHA-256 a partir de los campos críticos del remito.
Guardar el hash junto al remito en base de datos.
Verificar la integridad de un remito: el sistema recalcula el hash y lo compara con el original.
Listar todos los remitos registrados.

¿Dónde entra Blockchain?
Actualmente el hash se calcula y se guarda localmente (POC), pero:
Esta arquitectura permite que en una versión real se publique el hash en una blockchain pública o privada.
De esta forma se obtiene un respaldo inmutable y auditable, ideal para uso en seguridad e higiene, logística, mantenimiento, etc.

Endpoints disponibles:
POST /remitos → Crear un nuevo remito.
GET /remitos → Listar todos los remitos.
GET /remitos/{id}/verificar → Verificar integridad por ID.

¿Cómo se ejecuta?
Cloná el proyecto.
Ejecutá la clase principal Application.java.

Accedé a Swagger UI en:
http://localhost:8080/swagger-ui.html
