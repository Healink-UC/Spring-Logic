# Usa una imagen oficial de Maven con una versión específica de JDK como base.
# Esto asegura que las herramientas de compilación de Java y Maven estén disponibles.
FROM maven:3.9.6-eclipse-temurin-17

# Crea un usuario no-root llamado "appuser" para mejorar la seguridad,
# evitando ejecutar procesos como root.
RUN useradd -ms /bin/bash appuser

# Establece el directorio de trabajo a /home/appuser.
# Los comandos subsecuentes se ejecutarán en este directorio.
WORKDIR /home/appuser

# Cambia al usuario no-root que acabamos de crear.
USER appuser
