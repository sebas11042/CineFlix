
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
