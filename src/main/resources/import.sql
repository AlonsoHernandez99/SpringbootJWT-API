INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Alonso','Hernandez','luisalonso123421@gmail.com','','2019-04-05');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Sofia','Deras','sofiaivettederas@gmail.com','','2019-04-05');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Christian','Hernandez','chlandaverde@gmail.com','','2019-04-04');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Osmin','Orellana','chumin@gmail.com','','2019-04-03');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Juan Pueblo','Orellana','jpueblo@gmail.com','','2019-04-03');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Ana','Prendas','aprendas@gmail.com','','2019-04-03');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Elias','Sosa','esosa@gmail.com','','2019-04-03');
INSERT INTO clientes (nombre,apellido,email,foto,create_at) VALUES('Armando','Casa','acasa@gmail.com','','2019-04-03');


INSERT INTO productos (nombre,precio,create_at) VALUES('Pantalla LCD',300,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('Refrigeradora',500,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('Samsung Galaxy S10 Plus',1100,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('Huawei P30 Pro',1100,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('One Plus 7 Pro',800,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('PC Asus',1300,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('Licuadora Mabe',110,NOW());	
INSERT INTO productos (nombre,precio,create_at) VALUES('Horno Mabe',300,NOW());	

INSERT INTO facturas (descripcion,observacion,cliente_id,create_at) VALUES('Venta de productos varios','',1,NOW());	
INSERT INTO facturas (descripcion,observacion,cliente_id,create_at) VALUES('Venta de productos informaticos','No pago el wey!',1,NOW());	

INSERT INTO detalles_factura (cantidad,producto_id,factura_id) VALUES(10,1,1);	
INSERT INTO detalles_factura (cantidad,producto_id,factura_id) VALUES(1,2,1);	
INSERT INTO detalles_factura (cantidad,producto_id,factura_id) VALUES(3,8,1);	

INSERT INTO detalles_factura (cantidad,producto_id,factura_id) VALUES(2,3,2);	
INSERT INTO detalles_factura (cantidad,producto_id,factura_id) VALUES(1,5,2);	
INSERT INTO detalles_factura (cantidad,producto_id,factura_id) VALUES(3,6,2);	




