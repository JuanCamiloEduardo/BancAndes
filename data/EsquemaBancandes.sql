CREATE TABLE Usuario (
    id NUMBER,
    login VARCHAR(20),
    clave VARCHAR(20),
    numeroDocumento NUMBER,
    tipoDocumento VARCHAR(20),
    nombre VARCHAR(20),
    nacionalidad VARCHAR(20),
    direccionFisica VARCHAR(20),
    direccionElectronica VARCHAR(20),
    telefono NUMBER,
    ciudad VARCHAR(20),
    tipo VARCHAR(20),
    codigoPostal NUMBER,
    CONSTRAINT Usuario_PK PRIMARY KEY (login)
); 

INSERT INTO Usuario(
    id,
    login,
    clave,
    numeroDocumento,
    tipoDocumento,
    nombre,
    nacionalidad,
    direccionFisica,
    direccionElectronica,
    telefono,
    ciudad,
    tipo,
    codigoPostal
) VALUES (
    1,
    'j.burbanon',
    '456',
    1001001010,
    'CC',
    'Juan Camilo',
    'Colombiana',
    'Calle 1 Avenida 1',
    'j.burbanon@uniandes',
    3003003030,
    'Bogota',
    'adminbanc',
    1001010
);

ALTER TABLE Usuario
    ADD CONSTRAINT Un_Documento_Usuarios
    UNIQUE (numeroDocumento)
ENABLE;

ALTER TABLE Usuario
    ADD CONSTRAINT CK_Usuarios
    CHECK (tipo in ('cliente', 'cajero', 'gerenteoficina', 'gerentegeneral', 'adminbanc'))
ENABLE;



CREATE TABLE Oficina (
    id NUMBER,
    nombre VARCHAR(20),
    direccion VARCHAR(20),
    gerenteUsuario VARCHAR(20),
    puntosDeAtencion NUMBER,
    CONSTRAINT Oficina_PK PRIMARY KEY (id)
);

ALTER TABLE Oficina 
    ADD CONSTRAINT FK_Oficina 
    FOREIGN KEY (gerenteUsuario) REFERENCES Usuario(login)
ENABLE;



CREATE TABLE PuntoDeAtencion (
    id NUMBER,
    tipo VARCHAR(20),
    localizacion VARCHAR(20),
    oficina VARCHAR(20),
    CONSTRAINT PuntoDeAtencion_PK PRIMARY KEY (id)
);

ALTER TABLE PuntoDeAtencion
ADD CONSTRAINT CK_PuntoDeAtencion
CHECK ((tipo = 'fisico' AND oficina !=NULL) OR (tipo = 'virtual' AND localizacion = 'Bancandes' AND oficina = NULL))
ENABLE;



CREATE TABLE Cuenta(
    id NUMBER,
    tipo VARCHAR(20),
    saldo NUMBER,
    cliente VARCHAR(20),
    gerente VARCHAR(20),
    CONSTRAINT Cuenta_PK PRIMARY KEY (id)
);

ALTER TABLE Cuenta
    ADD CONSTRAINT FK_Cuenta_Gerente
    FOREIGN KEY (gerente) REFERENCES Usuario(login)
ENABLE;

ALTER TABLE Cuenta
    ADD CONSTRAINT FK_Cuenta_Cliente
    FOREIGN KEY (cliente) REFERENCES Usuario(login)
ENABLE;



CREATE TABLE PRESTAMO
   (ID NUMBER, 
    TIPO VARCHAR2(20), 
    ESTADO VARCHAR2(20), 
	NOMBRE VARCHAR2(20), 
    MONTO NUMBER,
    INTERES NUMBER,
    NUMEROCUOTAS NUMBER,
    DIAPAGA VARCHAR2(20), 
    VALORCUOTA NUMBER,
	CONSTRAINT PRESTAMO_PK PRIMARY KEY (ID)
);

ALTER TABLE PRESTAMO
    ADD CONSTRAINT FK_Prestamo
    FOREIGN KEY (NOMBRE) REFERENCES CLIENTE(nombre)
ENABLE;

CREATE TABLE CONSIGNA
   (
    JEFE VARCHAR2(20), 
    IDJEFE NUMBER,
    EMPLEADO VARCHAR2(20), 
    IDEMPLEADO NUMBER,
	MONTO NUMBER, 
    FECHA VARCHAR2(20),
    FRECUENCIA VARCHAR(20)
);

ALTER TABLE CONSIGNA
    ADD CONSTRAINT FK_CONSIGNA_1
    FOREIGN KEY (IDJEFE) REFERENCES CUENTA(id)
ENABLE;

ALTER TABLE CONSIGNA
    ADD CONSTRAINT FK_CONSIGNA_2
    FOREIGN KEY (IDEMPLEADO) REFERENCES CUENTA(id)
ENABLE;

COMMIT;