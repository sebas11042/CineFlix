use CineFlix;
GO
--procedimiento para proximos estrenos
CREATE PROCEDURE ObtenerPeliculasProximas
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        id_pelicula,
        titulo,
        sinopsis,
        duracion_min,
        trailer_url,
        imagen_url,
        fecha_salida
    FROM 
        Pelicula
    WHERE 
        fecha_salida > CAST(GETDATE() AS DATE)
    ORDER BY 
        fecha_salida;
END;

EXEC ObtenerPeliculasProximas;

--Estrenos
CREATE PROCEDURE ObtenerPeliculasEnEstreno
AS
BEGIN
    SELECT *
    FROM Pelicula
    WHERE fecha_salida <= CAST(GETDATE() AS DATE)
      AND fecha_salida >= DATEADD(DAY, -30, CAST(GETDATE() AS DATE));
END;

EXEC ObtenerPeliculasEnEstreno;

IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'ObtenerPeliculasEstreno')
BEGIN
    DROP PROCEDURE ObtenerPeliculasEnEstreno;
END;

--Cartelera
CREATE PROCEDURE ObtenerPeliculasNoEstrenoNiProximas
AS
BEGIN
    SELECT *
    FROM Pelicula
    WHERE fecha_salida < DATEADD(DAY, -30, CAST(GETDATE() AS DATE));
END;

EXEC ObtenerPeliculasNoEstrenoNiProximas;
GO

--PeliculaDetalle
CREATE PROCEDURE ObtenerPeliculaPorId
    @IdPelicula INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT id_pelicula,
           titulo,
           sinopsis,
           duracion_min,
           trailer_url,
           imagen_url,
           fecha_salida
    FROM Pelicula
    WHERE id_pelicula = @IdPelicula;
END;

EXEC ObtenerPeliculaPorId @IdPelicula = 1


--procedimiento ocupados 2.
CREATE OR ALTER PROCEDURE ObtenerAsientosOcupados
    @idSala INT
AS
BEGIN
    SELECT id_asiento, id_sala, fila, columna, tipo
    FROM Asiento
    WHERE id_sala = @idSala
      AND ocupado = 1
    ORDER BY fila, columna;
END;
GO

-- Ejemplo de uso
EXEC ObtenerAsientosOcupados @idSala = 1;


-- Procedimiento para el paso2
CREATE OR ALTER PROCEDURE ObtenerAsientosOcupados
    @idSala INT
AS
BEGIN
    SELECT id_asiento, fila, columna, tipo
    FROM Asiento
    WHERE id_sala = @idSala AND ocupado = 1
    ORDER BY fila, columna;
END;

EXEC ObtenerAsientosOcupados @idSala = 1;

--POR FUNCION
CREATE OR ALTER PROCEDURE ObtenerAsientosOcupadosPorFuncion
    @idFuncion INT
AS
BEGIN
    SELECT a.id_asiento, a.fila, a.columna, a.tipo
    FROM AsientoFuncion af
    JOIN Asiento a ON af.id_asiento = a.id_asiento
    WHERE af.id_funcion = @idFuncion AND af.ocupado = 1
    ORDER BY a.fila, a.columna;
END;


EXEC ObtenerAsientosOcupadosPorFuncion @idFuncion = 1

select * from asientofuncion;

ALTER TABLE Sala
ADD cantidadAsientos INT NOT NULL DEFAULT 0;

UPDATE Sala
SET cantidadAsientos = filas * columnas;

SELECT * FROM SALA;

CREATE OR ALTER PROCEDURE ObtenerAsientosPorSala
    @idSala INT
AS
BEGIN
    SELECT 
        id_asiento,
        id_sala,
        fila,
        columna,
        tipo
    FROM Asiento
    WHERE id_sala = @idSala
    ORDER BY fila, columna
END
GO



DECLARE @fila CHAR(1), @col INT, @id_sala INT
SET @id_sala = 2
SET @fila = 'A'

WHILE @fila <= 'J'
BEGIN
    SET @col = 1

    -- Determinar el número de asientos por fila
    DECLARE @maxCol INT
    SET @maxCol = CASE 
                    WHEN @fila <= 'E' THEN 10 -- filas A a E → 10 asientos
                    ELSE 14                  -- filas F a J → 14 asientos
                  END

    WHILE @col <= @maxCol
    BEGIN
        INSERT INTO Asiento (id_sala, fila, columna, tipo)
        VALUES (@id_sala, @fila, @col, 'tradicional')

        SET @col = @col + 1
    END

    SET @fila = CHAR(ASCII(@fila) + 1)
END


--asientos ocupados
DECLARE @fila CHAR(1), @col INT, @id_sala INT
SET @id_sala = 1
SET @fila = 'A'

WHILE @fila <= 'J'
BEGIN
    SET @col = 1

    -- Determinar el número de asientos por fila
    DECLARE @maxCol INT
    SET @maxCol = CASE 
                    WHEN @fila <= 'E' THEN 10 -- filas A a E → 10 asientos
                    ELSE 14                  -- filas F a J → 14 asientos
                  END

    WHILE @col <= @maxCol
    BEGIN
        -- Simular ocupación de algunos asientos (por ejemplo: A3, B5, F7, J10)
        DECLARE @ocupado BIT
        SET @ocupado = CASE 
                         WHEN (@fila = 'A' AND @col = 3) OR
                              (@fila = 'B' AND @col = 5) OR
                              (@fila = 'F' AND @col = 7) OR
                              (@fila = 'G' AND @col = 8) OR
                              (@fila = 'J' AND @col = 10)
                         THEN 1
                         ELSE 0
                       END

        INSERT INTO Asiento (id_sala, fila, columna, tipo, ocupado)
        VALUES (@id_sala, @fila, @col, 'tradicional', @ocupado)

        SET @col = @col + 1
    END

    SET @fila = CHAR(ASCII(@fila) + 1)
END

ALTER TABLE Asiento
ADD ocupado BIT DEFAULT 0;



SELECT * FROM ASIENTO;

SELECT * FROM FUNCION;

SELECT * FROM ASIENTOFUNCION;


DECLARE @idFuncion INT
DECLARE @idAsiento INT
DECLARE @ocupado BIT

-- Recorremos las funciones deseadas
DECLARE @funciones TABLE (id INT)
INSERT INTO @funciones VALUES (1), (4)

DECLARE funcion_cursor CURSOR FOR SELECT id FROM @funciones
OPEN funcion_cursor
FETCH NEXT FROM funcion_cursor INTO @idFuncion

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Recorremos todos los asientos de la sala 1
    DECLARE asiento_cursor CURSOR FOR
        SELECT id_asiento FROM Asiento WHERE id_sala = 1

    OPEN asiento_cursor
    FETCH NEXT FROM asiento_cursor INTO @idAsiento

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Simular ocupación aleatoria (por ejemplo, 30% de probabilidad)
        SET @ocupado = CASE WHEN RAND() < 0.3 THEN 1 ELSE 0 END

        INSERT INTO AsientoFuncion (id_funcion, id_asiento, ocupado)
        VALUES (@idFuncion, @idAsiento, @ocupado)

        FETCH NEXT FROM asiento_cursor INTO @idAsiento
    END

    CLOSE asiento_cursor
    DEALLOCATE asiento_cursor

    FETCH NEXT FROM funcion_cursor INTO @idFuncion
END

CLOSE funcion_cursor
DEALLOCATE funcion_cursor

-- Codigo para probar despues de lo que adelanto Jona

SELECT COLUMN_NAME 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Asiento' AND COLUMN_NAME = 'ocupado';

ALTER TABLE Asiento ADD ocupado BIT DEFAULT 0;

EXEC ObtenerAsientosOcupadosPorFuncion @idFuncion = 1;

SELECT TOP 5 * FROM AsientoFuncion;

SELECT * FROM AsientoFuncion WHERE ocupado = 1


DECLARE @idFuncion INT
DECLARE @idAsiento INT
DECLARE @ocupado BIT

-- Recorremos las funciones deseadas
DECLARE @funciones TABLE (id INT)
INSERT INTO @funciones VALUES (1) -- Podés agregar más funciones si querés

DECLARE funcion_cursor CURSOR FOR SELECT id FROM @funciones
OPEN funcion_cursor
FETCH NEXT FROM funcion_cursor INTO @idFuncion

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Recorremos todos los asientos de la sala 1
    DECLARE asiento_cursor CURSOR FOR
        SELECT id_asiento FROM Asiento WHERE id_sala = 1

    OPEN asiento_cursor
    FETCH NEXT FROM asiento_cursor INTO @idAsiento

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- 30% de probabilidad de ocupar el asiento
        SET @ocupado = CASE WHEN RAND() < 0.3 THEN 1 ELSE 0 END

        INSERT INTO AsientoFuncion (id_funcion, id_asiento, ocupado)
        VALUES (@idFuncion, @idAsiento, @ocupado)

        FETCH NEXT FROM asiento_cursor INTO @idAsiento
    END

    CLOSE asiento_cursor
    DEALLOCATE asiento_cursor

    FETCH NEXT FROM funcion_cursor INTO @idFuncion
END

CLOSE funcion_cursor
DEALLOCATE funcion_cursor

EXEC ObtenerAsientosOcupadosPorFuncion @idFuncion = 1

DELETE FROM AsientoFuncion
WHERE id_funcion = 1;


-- CODIGO PARA LO QUE NECESITA EL PASO3

USE CineFlix;
GO

CREATE OR ALTER PROCEDURE registrar_pago
    @monto_total DECIMAL(10,2),
    @metodo_pago NVARCHAR(50),
    @fecha_pago DATETIME
AS
BEGIN
    SET NOCOUNT ON;

    -- Crear tabla temporal para recibir el OUTPUT
    DECLARE @output TABLE (id_pago INT);

    -- Insertar en Pago y capturar el ID insertado
    INSERT INTO Pago (monto_total, metodo_pago, fecha_pago)
    OUTPUT INSERTED.id_pago INTO @output(id_pago)
    VALUES (@monto_total, @metodo_pago, @fecha_pago);

    -- Devolver el ID insertado
    SELECT id_pago FROM @output;
END;







-- ESTE ES EL NUEVO CODIGO PARA AGREGAR JONA

CREATE OR ALTER PROCEDURE registrar_boletos
    @idFuncion INT,
    @idUsuario INT,
    @idPago INT,
    @idTipoPrecio INT,
    @estado NVARCHAR(50),
    @asientos NVARCHAR(MAX) -- formato: '5,6,7'
AS
BEGIN
    SET NOCOUNT ON;

    -- Convertir la lista de IDs a tabla temporal usando XML
    DECLARE @xml XML = CAST('<root><id>' + 
        REPLACE(@asientos, ',', '</id><id>') + 
        '</id></root>' AS XML);

    -- Insertar boletos por cada id_asiento
    INSERT INTO Boleto (id_funcion, id_asiento, id_usuario, id_tipo_precio, id_pago, estado)
    SELECT 
        @idFuncion,
        x.value('.', 'INT') AS id_asiento,
        @idUsuario,
        @idTipoPrecio,
        @idPago,
        @estado
    FROM @xml.nodes('/root/id') AS t(x);
END;


-- Este es el que va a hacer que cuando se paguen los boletos, esos asientos queden ocupados para futuras reservas
ALTER PROCEDURE [dbo].[ocupar_asientos_funcion]
    @idFuncion INT,
    @asientos NVARCHAR(MAX) -- Ej: '12,13,14'
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @xml XML = CAST('<root><id>' +
        REPLACE(@asientos, ',', '</id><id>') +
        '</id></root>' AS XML);

    -- Insertar si no existe
    INSERT INTO AsientoFuncion (id_funcion, id_asiento, ocupado)
    SELECT
        @idFuncion,
        x.value('.', 'INT'),
        1
    FROM @xml.nodes('/root/id') AS t(x)
    WHERE NOT EXISTS (
        SELECT 1
        FROM AsientoFuncion af
        WHERE af.id_funcion = @idFuncion
          AND af.id_asiento = x.value('.', 'INT')
    );

    -- Por si ya existían y estaban en false, asegurarse de marcarlos ocupados
    UPDATE af
    SET af.ocupado = 1
    FROM AsientoFuncion af
    INNER JOIN (
        SELECT x.value('.', 'INT') AS id_asiento
        FROM @xml.nodes('/root/id') AS t(x)
    ) parsed ON af.id_asiento = parsed.id_asiento
    WHERE af.id_funcion = @idFuncion;
END;






SELECT name, is_disabled
FROM sys.triggers
WHERE parent_id = OBJECT_ID('dbo.Pago');

DISABLE TRIGGER trg_no_pago_invalido ON dbo.Pago;


SELECT TOP 10 * FROM Boleto ORDER BY id_boleto DESC;


SELECT * FROM AsientoFuncion WHERE id_funcion = 5;

SELECT * FROM Usuario;




-- *** NUEVOS BLOQUES AGREGADOS DIA 11/6/25 HORA 4:07 p.m ***

--ESTOS BLOQUES SON LOS NECESARIOS PARA CONTROL CONCURRENCIA BLOQUE DE ASIENTOS

-- Esta tabla mantendrá los asientos “bloqueados” temporalmente para evitar que otros usuarios los seleccionen.
CREATE TABLE ReservaTemporal (
    id_funcion INT NOT NULL,
    id_asiento INT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    PRIMARY KEY (id_funcion, id_asiento)
);



-- Este procedimiento recibe una lista de asientos y los bloquea solo si no están ya bloqueados
CREATE OR ALTER PROCEDURE bloquear_asientos_temporalmente
    @idFuncion INT,
    @asientos NVARCHAR(MAX) -- '5,6,7'
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @xml XML = CAST('<root><id>' +
        REPLACE(@asientos, ',', '</id><id>') +
        '</id></root>' AS XML);

    INSERT INTO ReservaTemporal (id_funcion, id_asiento, fecha_reserva)
    SELECT
        @idFuncion,
        x.value('.', 'INT'),
        GETDATE()
    FROM @xml.nodes('/root/id') AS t(x)
    WHERE NOT EXISTS (
        SELECT 1 FROM ReservaTemporal
        WHERE id_funcion = @idFuncion
          AND id_asiento = x.value('.', 'INT')
    );
END;



-- Este se encarga de limpiar la tabla ReservaTemporal eliminando asientos bloqueados hace más de 5 minutos.
CREATE OR ALTER PROCEDURE liberar_reservas_expiradas
AS
BEGIN
    DELETE FROM ReservaTemporal
    WHERE fecha_reserva < DATEADD(MINUTE, -5, GETDATE());
END;



-- Consulta general de asientos ocupados para una función:
SELECT id_asiento FROM AsientoFuncion
WHERE id_funcion = @idFuncion AND ocupado = 1

UNION

SELECT id_asiento FROM ReservaTemporal
WHERE id_funcion = @idFuncion;



-- Permite limpiar los asientos por ejemplo en caso de que el usuario cierre el navegador
CREATE OR ALTER PROCEDURE liberar_reserva_usuario
    @idFuncion INT,
    @asientos NVARCHAR(MAX) -- '5,6,7'
AS
BEGIN
    DECLARE @xml XML = CAST('<root><id>' +
        REPLACE(@asientos, ',', '</id><id>') +
        '</id></root>' AS XML);

    DELETE FROM ReservaTemporal
    WHERE id_funcion = @idFuncion
      AND id_asiento IN (
          SELECT x.value('.', 'INT')
          FROM @xml.nodes('/root/id') AS t(x)
      );
END;


EXEC bloquear_asientos_temporalmente @idFuncion = 1, @asientos = '5,6,7';

SELECT * FROM ReservaTemporal;

-- *** Aca termina el sql de control de concurrencia con bloqueo temporal de asientos***



-- PROCEDIMIENTOS PARA REPORTES --

-- Reporte de las ventas por pelicula y fecha
CREATE OR ALTER PROCEDURE reporte_ventas_por_pelicula_y_fecha
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        p.titulo AS Pelicula,
        CONVERT(DATE, pg.fecha_pago) AS Fecha,
        COUNT(b.id_boleto) AS AsientosVendidos,
        SUM(tp.precio) AS RecaudadoPorPelicula,
        (SELECT SUM(p2.monto_total)
         FROM Pago p2
         WHERE CONVERT(DATE, p2.fecha_pago) = CONVERT(DATE, pg.fecha_pago)) AS TotalDelDia
    FROM Boleto b
    JOIN Funcion f ON b.id_funcion = f.id_funcion
    JOIN Pelicula p ON f.id_pelicula = p.id_pelicula
    JOIN TipoPrecio tp ON b.id_tipo_precio = tp.id_tipo_precio
    JOIN Pago pg ON b.id_pago = pg.id_pago
    GROUP BY 
        p.titulo,
        CONVERT(DATE, pg.fecha_pago)
    ORDER BY 
        Fecha DESC,
        RecaudadoPorPelicula DESC;
END;

EXEC reporte_ventas_por_pelicula_y_fecha;



-- **TRIGGERS**

-- Evita que se registre un pago si el monto_total es menor o igual a 0.
CREATE OR ALTER TRIGGER trg_no_pago_invalido
ON Pago
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    -- Validación de monto inválido
    IF EXISTS (
        SELECT 1
        FROM inserted
        WHERE monto_total IS NULL OR monto_total <= 0
    )
    BEGIN
        RAISERROR('❌ No se permite registrar un pago con monto cero, negativo o nulo.', 16, 1);
        RETURN;
    END

    -- Si el monto es válido, hacer el insert real
    INSERT INTO Pago (monto_total, metodo_pago, fecha_pago)
    SELECT monto_total, metodo_pago, fecha_pago
    FROM inserted;
END;


    -- Insertar los pagos válidos
    INSERT INTO Pago (monto_total, metodo_pago, fecha_pago)
    SELECT monto_total, metodo_pago, fecha_pago
    FROM inserted;
END;

-- Habilitar el trigger ya que esta desabilitado

INSERT INTO Pago (monto_total, metodo_pago, fecha_pago)
VALUES (0, 'paypal', GETDATE());


-- ROLES DE USUARIOS Y PERMISOS --

-- Crear roles internos para control de seguridad
CREATE ROLE AdminApp;
CREATE ROLE PublicoWeb;

-- Crear usuarios simulados sin login (solo para demostración del esquema)
CREATE USER admin_user WITHOUT LOGIN;
CREATE USER web_user WITHOUT LOGIN;

-- Asignarlos a los roles
ALTER ROLE AdminApp ADD MEMBER admin_user;
ALTER ROLE PublicoWeb ADD MEMBER web_user;

-- Permisos para el administrador (AdminApp)
GRANT SELECT, INSERT, UPDATE, DELETE ON Pelicula TO AdminApp;
GRANT SELECT, INSERT, UPDATE, DELETE ON Funcion TO AdminApp;
GRANT SELECT, INSERT, UPDATE, DELETE ON TipoPrecio TO AdminApp;
GRANT SELECT, INSERT, UPDATE, DELETE ON Sala TO AdminApp;
GRANT EXECUTE ON reporte_ventas_por_pelicula_y_fecha TO AdminApp;

-- Permisos para el usuario público (PublicoWeb)
GRANT SELECT ON Pelicula TO PublicoWeb;
GRANT SELECT ON Funcion TO PublicoWeb;
GRANT SELECT ON Sala TO PublicoWeb;
GRANT SELECT ON TipoPrecio TO PublicoWeb;
GRANT EXECUTE ON registrar_pago TO PublicoWeb;
GRANT EXECUTE ON registrar_boletos TO PublicoWeb;
GRANT EXECUTE ON ocupar_asientos_funcion TO PublicoWeb;




-- **ÍNDICES RECOMENDADOS PARA CONSULTAS COMUNES**

CREATE INDEX idx_funcion_id_pelicula ON Funcion(id_pelicula);
CREATE INDEX idx_boleto_funcion_asiento ON Boleto(id_funcion, id_asiento);
CREATE INDEX idx_funcion_fecha ON Funcion(fecha);
CREATE INDEX idx_tipoprecio_formato ON TipoPrecio(formato);
CREATE INDEX idx_asientofuncion_funcion_ocupado ON AsientoFuncion(id_funcion, ocupado);
CREATE INDEX idx_pago_fecha_pago ON Pago(fecha_pago);
CREATE UNIQUE INDEX idx_usuario_correo ON Usuario(correo);


-- DETERMINAR QUE DATOS ALMACENADOS DEBEN ESTAR ENCRIPTADOS

/*Se identificaron los siguientes campos como sensibles y deben ser encriptados o protegidos:

Usuario.correo: Información personal del usuario.

Usuario.contrasena_hash: Aunque ya está cifrada con hash, debe almacenarse con algoritmos seguros como bcrypt o SHA-512.

Pago.metodo_pago: Aunque actualmente almacena texto como 'paypal' o 'tarjeta', si se llegara a guardar información de tarjetas o tokens de pago, debe ser cifrada.

La protección de estos datos es fundamental para asegurar la privacidad del usuario y cumplir con prácticas de seguridad estándar.*/


SELECT TOP 10 * FROM Pago ORDER BY id_pago DESC;



EXEC registrar_pago 0, 'paypal', GETDATE();


EXEC registrar_pago 4950, 'tarjeta', GETDATE()

EXEC sp_helptext registrar_pago;

DECLARE @monto_total DECIMAL(10,2) = 0;
DECLARE @metodo_pago NVARCHAR(50) = 'tarjeta';
DECLARE @fecha_pago DATETIME = GETDATE();

EXEC registrar_pago @monto_total = @monto_total, @metodo_pago = @metodo_pago, @fecha_pago = @fecha_pago;



SELECT * FROM Pago WHERE id_pago = 0;


--REGISTRAR PAGO NUEVO
DROP TRIGGER IF EXISTS trg_no_pago_invalido;


-- PROCEDIMIENTO LARGO
CREATE OR ALTER PROCEDURE registrar_pago  
    @monto_total DECIMAL(10,2),  
    @metodo_pago NVARCHAR(50),  
    @fecha_pago DATETIME  
AS  
BEGIN  
    SET NOCOUNT ON;

    -- ✅ Validar el monto directamente
    IF @monto_total <= 0
    BEGIN
        RAISERROR('❌ No se permite registrar un pago con monto cero o negativo.', 16, 1);
        RETURN;
    END

    -- Crear tabla temporal para recibir el OUTPUT  
    DECLARE @output TABLE (id_pago INT);  

    -- Insertar en Pago y capturar el ID insertado  
    INSERT INTO Pago (monto_total, metodo_pago, fecha_pago)  
    OUTPUT INSERTED.id_pago INTO @output(id_pago)  
    VALUES (@monto_total, @metodo_pago, @fecha_pago);  

    -- Devolver el ID insertado  
    SELECT id_pago FROM @output;  
END;


SELECT TOP 10 * FROM Pago ORDER BY id_pago DESC;

SELECT * FROM ReservaTemporal;

EXEC bloquear_asientos_temporalmente 1, '5,6,7';

EXEC liberar_reservas_expiradas;


CREATE OR ALTER PROCEDURE obtener_asientos_temporalmente_bloqueados
    @idFuncion INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT id_asiento
    FROM ReservaTemporal
    WHERE id_funcion = @idFuncion
      AND DATEADD(MINUTE, 5, fecha_reserva) > GETDATE(); -- aún dentro de los 5 minutos
END;