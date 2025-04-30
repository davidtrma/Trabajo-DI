CREATE DATABASE IF NOT EXISTS BDReservaHotel;

USE BDReservaHotel;

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    dni VARCHAR(15) UNIQUE,
    email VARCHAR(100),
    telefono VARCHAR(20),
    usuario VARCHAR(50) UNIQUE,
    contraseña VARCHAR(100)
);


CREATE TABLE hoteles (
    id_hotel INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    direccion VARCHAR(200),
    ciudad VARCHAR(100)
);


CREATE TABLE habitaciones (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    id_hotel INT,
    numero VARCHAR(10),
    tamaño VARCHAR(20),
    calidad VARCHAR(50),
    precio DECIMAL(10,2),
    FOREIGN KEY (id_hotel) REFERENCES hoteles(id_hotel)
);

CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    id_habitacion INT,
    fecha DATE,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
);

CREATE TABLE servicios (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    precio DECIMAL (10,2)
);


CREATE TABLE reserva_servicio (
    id_reserva INT,
    id_servicio INT,
    PRIMARY KEY (id_reserva, id_servicio),
    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva),
    FOREIGN KEY (id_servicio) REFERENCES servicios(id_servicio)
);


CREATE TABLE stock (
    id_stock INT AUTO_INCREMENT PRIMARY KEY,
    id_hotel INT,
    producto VARCHAR(100),
    cantidad INT,
    FOREIGN KEY (id_hotel) REFERENCES hoteles(id_hotel)
);


INSERT INTO hoteles (nombre, direccion, ciudad) VALUES
('Hotel Paraíso', 'Avenida del Sol 123', 'Valencia');


INSERT INTO habitaciones (id_hotel, numero, tamaño, calidad, precio) VALUES
(1, '101', 'Pequeña', 'Económica', 50.00),
(1, '102', 'Mediana', 'Estándar', 75.00),
(1, '103', 'Grande', 'Lujo', 120.00),
(1, '201', 'Mediana', 'Estándar', 80.00),
(1, '202', 'Grande', 'Suite', 150.00);


INSERT INTO stock (id_hotel, producto, cantidad) VALUES
(1, 'Champú', 100),
(1, 'Jabón', 150),
(1, 'Toallas', 80),
(1, 'Papel higiénico', 200),
(1, 'Cepillos de dientes', 50);


INSERT INTO servicios (nombre , precio) VALUES
('Limpieza diaria',10.00 ),
('Comida buffet' , 10.00),
('Wi-Fi gratuito' , 1.00),
('Spa y masajes' , 25.00),
('Servicio a la habitación' , '15.00');
