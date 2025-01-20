# Objetivo
**El objetivo del ejercicio es que se desarrolle una pequeña aplicación que interactúe con un usuario por consola para la gestión de juegos. Además de la implementación, se deben responder a una serie de preguntas teóricas para asegurar el cumplimiento de todos los criterios de evaluación.**

## Especificaciones

### 1. Aplicación
**Crea una base de datos en MongoDB con tu nombre y crea una colección de juegos, almacenando la siguiente información sobre cada uno: título, género, precio y fecha de lanzamiento.**

**Crea una aplicación que permita conectar con la base de datos que has creado de forma que se pueda listar, en una tabla, la información de los juegos introducidos.**

**Añade a la aplicación la posibilidad de realizar una búsqueda de todos los juegos del mismo género y ordénalos por título.**

**Añade a la aplicación la posibilidad de registrar nuevos juegos, considerando el título como campo obligatorio.**

**Añade a la funcionalidad de registrar nuevos juegos un control para que no sea posible registrar dos juegos con el mismo nombre. En ese caso se mostrará un mensaje de error al usuario y tendrá que proporcionar otro nombre diferente.**

**Añade una nueva funcionalidad que permita eliminar todos los juegos de un mismo género.**

**Añade una funcionalidad que permita modificar los datos de un juego.**

### 2. Teoría
**Responde, usando tus palabras, a las siguientes preguntas.**

**a) ¿Qué ventajas e inconvenientes encuentras al usar una base de datos documental con MongoDB?**

ventajas, que encuentros:

facilidad de crear las querys

flexibilidad de guardar informacion añadiendo campos(EJ: en los campos juegos, puedo poner registros extras como por ejemplo, Edad minima y puede tenerlo algunos registros y otros no)

facilidad de localizar registros ya sea por id o por x campo

desventajas:

orden, tipo cuando ya tenga 5-20 GB de datos con 100 documentos y 50 colecciones, se volveria insostenible sin un buen plan de organizacion

relaciones imaginarias o manuales, sin una documentacion que me diga como es el cuerpo por defecto, campos relacionados, puede ser un caos

**b) ¿Cuáles han sido los principales problemas que has tenido al desarrollar la aplicación?**

el orden de como guardar los campo, por que los diferentes registros , solo tiene en comun el id, todo lo demas es altamente cambiable o que tiende a fallar por que algunos registros existirian en la BD y no en la clase local, falta de orgaizacion, poca planifiacion, el objeto de ConexionMongo me ha resultado de gran ayuda y agilizo la tarea de desarrollo, el .env es  nuevo y es facil pero si te equivocas en algun caracter no funciona, drivers da algunos fallos, y problemas de logica de poner titulos en vez de titulo y el programa funcionaba bien pero no obtenida x juego

**c) ¿Cómo solucionarías el problema de la inconsistencia de datos en una base de datos documental? (El hecho de que en los documentos de una colección no sea obligatorio seguir un esquema único)**

con un buen plan de organizacion planificando toda la estructura interna del sistema, con sus relaciones ficticias, afianzando muy bien la proyeccion del proyecto y como escalaria detro de 20-50 años y mirando algun bot o tarea automatizada que revise todas las colecciones para o tener informacion repetida