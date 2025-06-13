USE [CineFlix]
GO
/****** Object:  StoredProcedure [dbo].[ocupar_asientos_funcion]    Script Date: 12/6/2025 15:55:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
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