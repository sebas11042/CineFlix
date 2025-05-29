DROP DATABASE CineFlix;
CREATE DATABASE CineFlix;
USE CineFlix;
USE BARES;
INSERT INTO Pelicula (titulo, sinopsis, duracion_min, trailer_url, imagen_url, fecha_salida) VALUES
('Avengers: Endgame',
 'El desenlace �pico de los Vengadores contra Thanos.',
 180,
 'https://www.youtube.com/watch?v=TcMBFSGVi1c',
 'https://cdn.marvel.com/content/1x/ae_digital_packshot.jpg',
 '2019-04-26'),

('Interstellar',
 'Exploraci�n espacial y teor�as del tiempo y el amor.',
 169,
 'https://www.youtube.com/watch?v=LYS2O1nl9iM',
 'https://s3.amazonaws.com/nightjarprod/content/uploads/sites/130/2021/08/19085635/gEU2QniE6E77NI6lCU6MxlNBvIx-scaled.jpg',
 '2014-11-07'),

('Destino Final: Lazos de Sangre',
 'Una nueva generaci�n enfrenta una muerte inevitable tras una visi�n premonitoria.',
 95,
 'https://www.youtube.com/watch?v=8FudANSsWNQ',
 'https://periodicocorreo.com.mx/core/correo/assets/images/2025/05/21/destino-final-L7D2GL1wl.jpg',
 '2025-06-20'),

('Lilo & Stitch',
 'Una ni�a hawaiana adopta a un experimento alien�gena buscado por la galaxia.',
 85,
 'https://www.youtube.com/watch?v=9JIyINjMfcc',
 'https://www.musicmundial.com/en/wp-content/uploads/2024/08/Disney-unveils-the-first-peek-into-the-upcoming-Lilo-Stitch-live-action.jpg',
 '2025-07-12'),

('Misi�n Imposible: Sentencia Mortal',
 'Ethan Hunt debe impedir que una inteligencia artificial caiga en manos equivocadas.',
 163,
 'https://www.youtube.com/watch?v=A6VZHeeZGgk',
 'https://www.loslunesseriefilos.com/wp-content/uploads/2025/05/mision-imposible-sentencia-final-pelicula.jpg',
 '2025-07-25'),

('El Padrino',
 'La cr�nica de la familia mafiosa Corleone en el Nueva York de los a�os 40.',
 175,
 'https://www.youtube.com/watch?v=iOyQx7MXaz0',
 'https://image.tmdb.org/t/p/original/jiTiRWEZDOoggZsOpM7t7WEMDym.jpg',
 '1972-03-24'),

('Elemental',
 'En Ciudad Elemento, una joven de fuego y un chico de agua desaf�an las normas.',
 101,
 'https://www.youtube.com/watch?v=hXzcyx9V0xw',
 'https://i.pinimg.com/originals/1f/53/5b/1f535bc62ce58144a5107eef5d6d458b.jpg',
 '2023-06-16'),

('Cars',
 'Rayo McQueen aprende lecciones de humildad y amistad en un pueblo olvidado.',
 117,
 'https://www.youtube.com/watch?v=W_H7_tDHFE8',
 'https://wallpapercave.com/wp/wp2740013.jpg',
 '2006-06-09'),

('Karate Kid: Legends',
 'Una nueva generaci�n de luchadores se enfrenta a desaf�os personales y rivalidades en un legado que comenz� con el Sr. Miyagi.',
 130,
 'https://www.youtube.com/watch?v=LhRXf-yEQqA',
 'https://wushu-herald.co/wp-content/uploads/2025/05/Karate-Kid-Legends.png',
 '2025-08-15');

 -- Pel�cula estrenada hace 10 d�as (deber�a aparecer en los resultados)
INSERT INTO Pelicula (titulo, sinopsis, duracion_min, trailer_url, imagen_url, fecha_salida)
VALUES (
    'Guardianes del Multiverso',
    'Un nuevo equipo de h�roes se enfrenta a una amenaza interdimensional.',
    145,
    'https://youtu.be/SBnf4P109dg?si=t1XrNaBoRsTmqvNo',
    'https://preview.redd.it/9agzc1oxjps71.jpg?auto=webp&s=6fd60a685a41064fe099cec8d642ded83a033604',
    CAST(GETDATE() - 10 AS DATE)
);

-- Pel�cula que se estrenar� en 5 d�as (no deber�a aparecer en los resultados)
INSERT INTO Pelicula (titulo, sinopsis, duracion_min, trailer_url, imagen_url, fecha_salida)
VALUES (
    'La Era del Renacer',
    'Una historia �pica de redenci�n y esperanza en un mundo post-apocal�ptico.',
    130,
    'https://www.youtube.com/watch?v=renacer_trailer',
    'https://example.com/renacer.jpg',
    CAST(GETDATE() + 5 AS DATE)
);

delete from pelicula where id_pelicula = 10;

 SELECT * FROM Pelicula;