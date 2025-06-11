use CineFlix;
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
