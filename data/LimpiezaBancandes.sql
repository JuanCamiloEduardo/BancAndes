--- Sentencias SQL para la creación del esquema de parranderos
--- Las tablas tienen prefijo A_ para facilitar su acceso desde SQL Developer

-- USO
-- Copie el contenido deseado de este archivo en una pestaña SQL de SQL Developer
-- Ejecútelo como un script - Utilice el botón correspondiente de la pestaña utilizada
    
-- Eliminar todas las tablas de la base de datos
DROP TABLE USUARIO CASCADE CONSTRAINTS;
DROP TABLE OFICINA CASCADE CONSTRAINTS;
DROP TABLE PUNTODEATENCION CASCADE CONSTRAINTS;
DROP TABLE CUENTA CASCADE CONSTRAINTS;
DROP TABLE PRESTAMO CASCADE CONSTRAINTS;
COMMMIT;

-- Eliminar el contenido de todas las tablas de la base de datos
-- El orden es importante. Por qué?
delete from prestamo;
delete from puntodeatencion;
delete from oficina;
delete from cuenta;
delete from usuario;
commit;

