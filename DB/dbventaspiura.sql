-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 30-06-2022 a las 15:43:10
-- Versión del servidor: 10.4.11-MariaDB
-- Versión de PHP: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `dbventaspiura`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_I_Credito` (IN `pidtipodocumento` INT, IN `pidcliente` INT, IN `pidempleado` INT, IN `pserie` VARCHAR(5) CHARSET utf8, IN `pnumero` VARCHAR(20) CHARSET utf8, IN `pfecha` DATE, IN `ptotalcredito` DECIMAL(8,2), IN `pdescuento` DECIMAL(8,2), IN `psubtotal` DECIMAL(8,2), IN `pigv` DECIMAL(8,2), IN `ptotalpagar` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN		
		INSERT INTO credito(idtipodocumento,idcliente,idempleado,serie,numero,fecha,totalcredito,descuento,subtotal,igv,totalpagar,estado)
		VALUES(pidtipodocumento,pidcliente,pidempleado,pserie,pnumero,pfecha,ptotalcredito,pdescuento,psubtotal,pigv,ptotalpagar,pestado);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_S_Credito` ()  BEGIN
		SELECT v.IdCredito,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalCredito,v.Descuento,v.SubTotal,
		v.Igv,v.TotalPagar,v.Estado
		FROM credito AS v 
		INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
		INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
		INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
		ORDER BY v.IdCredito;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_S_CreditoMensual` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pfecha_ini` VARCHAR(20) CHARSET utf8, IN `pfecha_fin` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "consultar" THEN
				SELECT CONCAT(UPPER(MONTHNAME(v.Fecha))," ",YEAR(v.Fecha)) AS Fecha,SUM(v.TotalCredito) AS Total,
				ROUND((SUM(v.TotalCredito)*100)/(SELECT SUM(v.TotalPagar) AS TotalCredito FROM credito AS v WHERE ((date_format(v.Fecha,'%Y-%m') >= pfecha_ini) AND (date_format(v.Fecha,'%Y-%m') <= pfecha_fin)) AND v.Estado="EMITIDO")) AS Porcentaje
				FROM credito AS v
				WHERE ((date_format(v.Fecha,'%Y-%m') >= pfecha_ini) AND (date_format(v.Fecha,'%Y-%m') <= pfecha_fin)) AND v.Estado="EMITIDO" GROUP BY v.Fecha;			
								
			END IF; 
			

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_S_CreditoPorDetalle` (IN `pcriterio` VARCHAR(30) CHARSET utf8, IN `pfechaini` DATE, IN `pfechafin` DATE)  BEGIN
		IF pcriterio = "consultar" THEN
			SELECT p.Codigo,p.Nombre AS Producto,c.Descripcion AS Categoria,dv.Costo,dv.Precio,
			SUM(dv.Cantidad) AS Cantidad,SUM(dv.Total) AS Total,
			SUM(TRUNCATE((Total-(dv.Costo*dv.Cantidad)),2)) AS Ganancia FROM credito AS v
			INNER JOIN detallecredito AS dv ON v.IdCredito=dv.IdCredito
			INNER JOIN producto AS p ON dv.IdProducto=p.IdProducto
			INNER JOIN categoria AS c ON p.IdCategoria=c.IdCategoria
			WHERE (v.Fecha>=pfechaini AND v.Fecha<=pfechafin) AND v.Estado="EMITIDO" GROUP BY p.IdProducto
			ORDER BY v.IdCredito DESC;
		END IF;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_S_CreditoPorFecha` (IN `pcriterio` VARCHAR(30) CHARSET utf8, IN `pfechaini` DATE, IN `pfechafin` DATE, IN `pdocumento` VARCHAR(30) CHARSET utf8)  BEGIN
		IF pcriterio = "anular" THEN
			SELECT v.IdCredito,c.Nombre AS Cliente,v.Fecha,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,td.Descripcion AS TipoDocumento,v.Serie,v.Numero,
			v.Estado,v.TotalPagar  FROM credito AS v
			INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
			INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
			INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
			WHERE (v.Fecha>=pfechaini AND v.Fecha<=pfechafin) AND td.Descripcion=pdocumento ORDER BY v.IdCredito DESC;
		ELSEIF pcriterio = "consultar" THEN
		   SELECT v.IdCredito,c.Nombre AS Cliente,v.Fecha,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,td.Descripcion AS TipoDocumento,v.Serie,v.Numero,
			v.Estado,v.TotalCredito,v.Descuento,v.TotalPagar  FROM credito AS v
			INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
			INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
			INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
			WHERE (v.Fecha>=pfechaini AND v.Fecha<=pfechafin) ORDER BY v.IdCredito DESC;
		ELSEIF pcriterio = "caja" THEN	
		   SELECT SUM(dv.Cantidad) AS Cantidad,p.Nombre AS Producto,dv.Precio,
			SUM(dv.Total) AS Total, SUM(TRUNCATE((Total-(dv.Costo*dv.Cantidad)),2)) AS Ganancia,v.Fecha FROM credito AS v
			INNER JOIN detallecredito AS dv ON v.IdCredito=dv.IdCredito
			INNER JOIN producto AS p ON dv.IdProducto=p.IdProducto
			INNER JOIN categoria AS c ON p.IdCategoria=c.IdCategoria
			WHERE v.Fecha=pfechaini AND v.Estado="EMITIDO" GROUP BY p.IdProducto
			ORDER BY v.IdCredito DESC;
		END IF;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_S_CreditoPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "id" THEN
				SELECT v.IdCredito,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalCredito,v.Descuento,v.SubTotal,
				v.Igv,v.TotalPagar,v.Estado  FROM credito AS v
				INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
				INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
				INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
				WHERE v.IdCredito=pbusqueda ORDER BY v.IdCredito;
			ELSEIF pcriterio = "documento" THEN
				SELECT v.IdCredito,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalCredito,v.Descuento,v.SubTotal,
				v.Igv,v.TotalPagar,v.Estado  FROM credito AS v
				INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
				INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
				INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
				WHERE td.Descripcion=pbusqueda ORDER BY v.IdCredito;
			END IF; 
			

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_S_UltimoIdCredito` ()  SELECT MAX(IdCredito) AS id FROM credito$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_U_ActualizarCreditoEstado` (IN `pidcredito` INT, IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN
		UPDATE credito SET
			estado=pestado
		WHERE idcredito = pidcredito;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `000_SP_U_Credito` (IN `pidcredito` INT, IN `pidtipodocumento` INT, IN `pidcliente` INT, IN `pidempleado` INT, IN `pserie` VARCHAR(5), IN `pnumero` VARCHAR(20), IN `pfecha` DATE, IN `ptotalcredito` DECIMAL(8,2), IN `pdescuento` DECIMAL(8,2), IN `psubtotal` DECIMAL(8,2), IN `pigv` DECIMAL(8,2), IN `ptotalpagar` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN
		UPDATE credito SET
			idtipodocumento=pidtipodocumento,
			idcliente=pidcliente,
			idempleado=pidempleado,
			serie=pserie,
			numero=pnumero,
			fecha=pfecha,
			totalcredito=ptotalcredito,
			descuento=pdescuento,
			subtotal=psubtotal,
			igv=pigv,
			totalpagar=ptotalpagar,
			estado=pestado
		WHERE idcredito = pidcredito;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `001_SP_I_DetalleCredito` (IN `pidcredito` INT, IN `pidproducto` INT, IN `pcantidad` DECIMAL(8,2), IN `pcosto` DECIMAL(8,2), IN `pprecio` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2))  BEGIN		
		INSERT INTO detallecredito(idcredito,idproducto,cantidad,costo,precio,total)
        VALUES(pidcredito,pidproducto,pcantidad,pcosto,pprecio,ptotal);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `001_SP_S_DetalleCreditoPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
IF pcriterio = "id" THEN
SELECT dc.IdCredito,p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,dc.Cantidad,dc.Precio,dc.Total  FROM detallecredito AS dc
INNER JOIN producto AS p ON dc.IdProducto=p.IdProducto
WHERE dc.IdCredito=pbusqueda ORDER BY dc.IdCredito;
			
END IF; 
			
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `001_SP_U_DetalleCredito` (IN `pidcredito` INT, IN `pidproducto` INT, IN `pcantidad` DECIMAL(8,2), IN `pcosto` DECIMAL(8,2), IN `pprecio` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2))  BEGIN
		UPDATE credito SET
			idcredito=pidcredito,
			idproducto=pidproducto,
			cantidad=pcantidad,
			costo=pcosto,
			precio=pprecio,
			total=ptotal
		WHERE idventa = pidventa;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Categoria` (IN `pdescripcion` VARCHAR(100) CHARSET utf8)  BEGIN		
		INSERT INTO categoria(descripcion)
		VALUES(pdescripcion);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Cliente` (IN `pnombre` VARCHAR(100) CHARSET utf8, IN `pruc` VARCHAR(11) CHARSET utf8, IN `pdni` VARCHAR(8) CHARSET utf8, IN `pdireccion` VARCHAR(50) CHARSET utf8, IN `ptelefono` VARCHAR(15) CHARSET utf8, IN `pobsv` TEXT CHARSET utf8)  BEGIN		
		INSERT INTO cliente(nombre,ruc,dni,direccion,telefono,obsv)
		VALUES(pnombre,pruc,pdni,pdireccion,ptelefono,pobsv);
        SELECT LAST_INSERT_ID() as clienteId;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Compra` (IN `pidtipodocumento` INT, IN `pidproveedor` INT, IN `pidempleado` INT, IN `pnumero` VARCHAR(20) CHARSET utf8, IN `pfecha` DATE, IN `psubtotal` DECIMAL(8,2), IN `pigv` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2), IN `pestado` VARCHAR(30))  BEGIN		
		INSERT INTO compra(idtipodocumento,idproveedor,idempleado,numero,fecha,subtotal,igv,total,estado)
		VALUES(pidtipodocumento,pidproveedor,pidempleado,pnumero,pfecha,psubtotal,pigv,ptotal,pestado);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_DetalleCompra` (IN `pidcompra` INT, IN `pidproducto` INT, IN `pcantidad` DECIMAL(8,2), IN `pprecio` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2))  BEGIN		
		INSERT INTO detallecompra(idcompra,idproducto,cantidad,precio,total)
		VALUES(pidcompra,pidproducto,pcantidad,pprecio,ptotal);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_DetalleVenta` (IN `pidventa` INT, IN `pidproducto` INT, IN `pcantidad` DECIMAL(8,2), IN `pcosto` DECIMAL(8,2), IN `pprecio` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2))  BEGIN		
		INSERT INTO detalleventa(idventa,idproducto,cantidad,costo,precio,total)
		VALUES(pidventa,pidproducto,pcantidad,pcosto,pprecio,ptotal);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Empleado` (IN `pnombre` VARCHAR(50) CHARSET utf8, IN `papellido` VARCHAR(80) CHARSET utf8, IN `psexo` VARCHAR(1) CHARSET utf8, IN `pfechanac` DATE, IN `pdireccion` VARCHAR(100) CHARSET utf8, IN `ptelefono` VARCHAR(10) CHARSET utf8, IN `pcelular` VARCHAR(15) CHARSET utf8, IN `pemail` VARCHAR(80) CHARSET utf8, IN `pdni` VARCHAR(8) CHARSET utf8, IN `pfechaing` DATE, IN `psueldo` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8, IN `pusuario` VARCHAR(20) CHARSET utf8, IN `pcontrasenia` TEXT CHARSET utf8, IN `pidtipousuario` INT)  BEGIN		
		INSERT INTO empleado(nombre,apellido,sexo,fechanac,direccion,telefono,celular,email,dni,fechaing,sueldo,estado,usuario,contrasenia,idtipousuario)
		VALUES(pnombre,papellido,psexo,pfechanac,pdireccion,ptelefono,pcelular,pemail,pdni,pfechaing,psueldo,pestado,pusuario,pcontrasenia,pidtipousuario);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Producto` (IN `pcodigo` VARCHAR(50) CHARSET utf8, IN `pnombre` VARCHAR(100) CHARSET utf8, IN `pdescripcion` TEXT CHARSET utf8, IN `pstock` DECIMAL(8,2), IN `pstockmin` DECIMAL(8,2), IN `ppreciocosto` DECIMAL(8,2), IN `pprecioventa` DECIMAL(8,2), IN `putilidad` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8, IN `pidcategoria` INT, IN `pimagen` VARCHAR(50) CHARSET utf8, IN `pfechavencimiento` DATE)  BEGIN		
		INSERT INTO producto(codigo,nombre,descripcion,stock,stockmin,preciocosto,precioventa,utilidad,estado,idcategoria,imagen, fechavencimiento)
		VALUES(pcodigo,pnombre,pdescripcion,pstock,pstockmin,ppreciocosto,pprecioventa,putilidad,pestado,pidcategoria,pimagen, pfechavencimiento);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Proveedor` (IN `pnombre` VARCHAR(100) CHARSET utf8, IN `pruc` VARCHAR(11) CHARSET utf8, IN `pdni` VARCHAR(8) CHARSET utf8, IN `pdireccion` VARCHAR(100) CHARSET utf8, IN `ptelefono` VARCHAR(10) CHARSET utf8, IN `pcelular` VARCHAR(15) CHARSET utf8, IN `pemail` VARCHAR(80) CHARSET utf8, IN `pcuenta1` VARCHAR(50) CHARSET utf8, IN `pcuenta2` VARCHAR(50) CHARSET utf8, IN `pestado` VARCHAR(30) CHARSET utf8, IN `pobsv` TEXT CHARSET utf8)  BEGIN		
		INSERT INTO proveedor(nombre,ruc,dni,direccion,telefono,celular,email,cuenta1,cuenta2,estado,obsv)
		VALUES(pnombre,pruc,pdni,pdireccion,ptelefono,pcelular,pemail,pcuenta1,pcuenta2,pestado,pobsv);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_TipoDocumento` (IN `pdescripcion` VARCHAR(80) CHARSET utf8)  BEGIN		
		INSERT INTO tipodocumento(descripcion)
		VALUES(pdescripcion);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_TipoUsuario` (IN `pdescripcion` VARCHAR(80) CHARSET utf8, IN `pp_venta` INT, IN `pp_compra` INT, IN `pp_producto` INT, IN `pp_proveedor` INT, IN `pp_empleado` INT, IN `pp_cliente` INT, IN `pp_categoria` INT, IN `pp_tipodoc` INT, IN `pp_tipouser` INT, IN `pp_anularv` INT, IN `pp_anularc` INT, IN `pp_estadoprod` INT, IN `pp_ventare` INT, IN `pp_ventade` INT, IN `pp_estadistica` INT, IN `pp_comprare` INT, IN `pp_comprade` INT, IN `pp_pass` INT, IN `pp_respaldar` INT, IN `pp_restaurar` INT, IN `pp_caja` INT)  BEGIN		
		INSERT INTO tipousuario(descripcion,p_venta,p_compra,p_producto,p_proveedor,p_empleado,p_cliente,p_categoria,p_tipodoc,p_tipouser,p_anularv,p_anularc,
		p_estadoprod,p_ventare,p_ventade,p_estadistica,p_comprare,p_comprade,p_pass,p_respaldar,p_restaurar,p_caja)
		VALUES(pdescripcion,pp_venta,pp_compra,pp_producto,pp_proveedor,pp_empleado,pp_cliente,pp_categoria,pp_tipodoc,pp_tipouser,pp_anularv,pp_anularc,
		pp_estadoprod,pp_ventare,pp_ventade,pp_estadistica,pp_comprare,pp_comprade,pp_pass,pp_respaldar,pp_restaurar,pp_caja);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_Venta` (IN `pidtipodocumento` INT, IN `pidcliente` INT, IN `pidempleado` INT, IN `pserie` VARCHAR(5) CHARSET utf8, IN `pnumero` VARCHAR(20) CHARSET utf8, IN `pfecha` DATE, IN `ptotalventa` DECIMAL(8,2), IN `pdescuento` DECIMAL(8,2), IN `psubtotal` DECIMAL(8,2), IN `pigv` DECIMAL(8,2), IN `ptotalpagar` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN		
		INSERT INTO venta(idtipodocumento,idcliente,idempleado,serie,numero,fecha,totalventa,descuento,subtotal,igv,totalpagar,estado)
		VALUES(pidtipodocumento,pidcliente,pidempleado,pserie,pnumero,pfecha,ptotalventa,pdescuento,psubtotal,pigv,ptotalpagar,pestado);
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Categoria` ()  BEGIN
		SELECT * FROM categoria;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_CategoriaPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT c.IdCategoria,c.Descripcion FROM categoria AS c WHERE c.IdCategoria=pbusqueda;
	ELSEIF pcriterio = "descripcion" THEN
		SELECT c.IdCategoria,c.Descripcion FROM categoria AS c WHERE c.Descripcion LIKE CONCAT("%",pbusqueda,"%");
	ELSE
		SELECT c.IdCategoria,c.Descripcion FROM categoria AS c;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Cliente` ()  BEGIN
		SELECT * FROM cliente;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ClientePorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT c.IdCliente,c.Nombre,c.Ruc,c.Dni,c.Direccion,c.Telefono,c.Obsv FROM cliente AS c WHERE c.IdCliente=pbusqueda;
	ELSEIF pcriterio = "nombre" THEN
		SELECT c.IdCliente,c.Nombre,c.Ruc,c.Dni,c.Direccion,c.Telefono,c.Obsv FROM cliente AS c WHERE c.Nombre LIKE CONCAT("%",pbusqueda,"%");
	ELSEIF pcriterio = "ruc" THEN
		SELECT c.IdCliente,c.Nombre,c.Ruc,c.Dni,c.Direccion,c.Telefono,c.Obsv FROM cliente AS c WHERE c.Ruc LIKE CONCAT("%",pbusqueda,"%");
   ELSEIF pcriterio = "dni" THEN
		SELECT c.IdCliente,c.Nombre,c.Ruc,c.Dni,c.Direccion,c.Telefono,c.Obsv FROM cliente AS c WHERE c.Dni LIKE CONCAT("%",pbusqueda,"%");
	ELSE
		SELECT c.IdCliente,c.Nombre,c.Ruc,c.Dni,c.Direccion,c.Telefono,c.Obsv FROM cliente AS c;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Compra` ()  BEGIN
		SELECT c.IdCompra,td.Descripcion AS TipoDocumento,p.Nombre AS Proveedor,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,c.Numero,c.Fecha,c.SubTotal,c.Igv,c.Total,c.Estado
		FROM compra AS c
		INNER JOIN tipodocumento AS td ON c.IdTipoDocumento=td.IdTipoDocumento	 
		INNER JOIN proveedor AS p ON c.IdProveedor=p.IdProveedor		
		INNER JOIN empleado AS e ON c.IdEmpleado=e.IdEmpleado
		ORDER BY c.IdCompra;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_CompraPorDetalle` (IN `pcriterio` VARCHAR(30) CHARSET utf8, IN `pfechaini` DATE, IN `pfechafin` DATE)  BEGIN
		IF pcriterio = "consultar" THEN
			SELECT p.Codigo,p.Nombre AS Producto,ca.Descripcion AS Categoria,dc.Precio,
			SUM(dc.Cantidad) AS Cantidad,SUM(dc.Total) AS Total FROM compra AS c
			INNER JOIN detallecompra AS dc ON c.IdCompra=dc.IdCompra
			INNER JOIN producto AS p ON dc.IdProducto=p.IdProducto
			INNER JOIN categoria AS ca ON p.IdCategoria=ca.IdCategoria
			WHERE (c.Fecha>=pfechaini AND c.Fecha<=pfechafin) AND c.Estado="NORMAL" GROUP BY p.IdProducto
			ORDER BY c.IdCompra DESC;
		END IF;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_CompraPorFecha` (IN `pcriterio` VARCHAR(30) CHARSET utf8, IN `pfechaini` DATE, IN `pfechafin` DATE, IN `pdocumento` VARCHAR(30) CHARSET utf8)  BEGIN
		IF pcriterio = "anular" THEN
			SELECT c.IdCompra,p.Nombre AS Proveedor,c.Fecha,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,td.Descripcion AS TipoDocumento,c.Numero,
			c.Estado,c.Total FROM compra AS c
			INNER JOIN tipodocumento AS td ON c.IdTipoDocumento=td.IdTipoDocumento
			INNER JOIN proveedor AS p ON c.IdProveedor=p.IdProveedor
			INNER JOIN empleado AS e ON c.IdEmpleado=e.IdEmpleado
			WHERE (c.Fecha>=pfechaini AND c.Fecha<=pfechafin) AND td.Descripcion=pdocumento ORDER BY c.IdCompra DESC;
		ELSEIF pcriterio = "consultar" THEN
		   SELECT c.IdCompra,p.Nombre AS Proveedor,c.Fecha,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,td.Descripcion AS TipoDocumento,c.Numero,
			c.Estado,c.Total FROM compra AS c
			INNER JOIN tipodocumento AS td ON c.IdTipoDocumento=td.IdTipoDocumento
			INNER JOIN proveedor AS p ON c.IdProveedor=p.IdProveedor
			INNER JOIN empleado AS e ON c.IdEmpleado=e.IdEmpleado
			WHERE c.Fecha>=pfechaini AND c.Fecha<=pfechafin ORDER BY c.IdCompra DESC;
		END IF;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ConsultaStock` ()  select p.IdProducto,p.Codigo,p.Nombre,
c.descripcion as Categoria,p.Stock,p.StockMin,
p.PrecioCosto,p.PrecioVenta,p.utilidad
from producto p inner join categoria c
on p.idcategoria=c.idcategoria
where p.stockmin>=stock$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_DetalleCompra` ()  BEGIN
		SELECT * FROM detallecompra;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_DetalleCompraPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "id" THEN
				SELECT dc.IdCompra,p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,dc.Cantidad,dc.Precio,dc.Total  FROM detallecompra AS dc
				INNER JOIN producto AS p ON dc.IdProducto=p.IdProducto
				WHERE dc.IdCompra=pbusqueda ORDER BY dc.IdCompra;
			
			END IF; 
			
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_DetalleVenta` ()  BEGIN
		SELECT * FROM detalleventa;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_DetalleVentaPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "id" THEN
				SELECT dv.IdVenta,p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,dv.Cantidad,dv.Precio,dv.Total  FROM detalleventa AS dv
				INNER JOIN producto AS p ON dv.IdProducto=p.IdProducto
				WHERE dv.IdVenta=pbusqueda ORDER BY dv.IdVenta;
			
			END IF; 
			
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Empleado` ()  BEGIN
		SELECT e.IdEmpleado,e.Nombre,e.Apellido,e.Sexo,e.FechaNac,e.Direccion,e.Telefono,e.Celular,e.Email,
		e.Dni,e.FechaIng,e.Sueldo,e.Estado,e.Usuario,e.Contrasenia,tu.Descripcion AS TipoUsuario
		FROM empleado AS e INNER JOIN tipousuario AS tu ON e.IdTipoUsuario=tu.IdTipoUsuario
		ORDER BY e.IdEmpleado;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_EmpleadoPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT e.IdEmpleado,e.Nombre,e.Apellido,e.Sexo,e.FechaNac,e.Direccion,e.Telefono,e.Celular,e.Email,
		e.Dni,e.FechaIng,e.Sueldo,e.Estado,e.Usuario,e.Contrasenia,tu.Descripcion AS TipoUsuario
		FROM empleado AS e INNER JOIN tipousuario AS tu ON e.IdTipoUsuario = tu.IdTipoUsuario 
		WHERE e.IdEmpleado=pbusqueda;
	ELSEIF pcriterio = "nombre" THEN
		SELECT e.IdEmpleado,e.Nombre,e.Apellido,e.Sexo,e.FechaNac,e.Direccion,e.Telefono,e.Celular,e.Email,
		e.Dni,e.FechaIng,e.Sueldo,e.Estado,e.Usuario,e.Contrasenia,tu.Descripcion AS TipoUsuario
		FROM empleado AS e INNER JOIN tipousuario AS tu ON e.IdTipoUsuario = tu.IdTipoUsuario 
		WHERE ((e.Nombre LIKE CONCAT("%",pbusqueda,"%")) OR (e.Apellido LIKE CONCAT("%",pbusqueda,"%")));
	ELSEIF pcriterio = "dni" THEN
		SELECT e.IdEmpleado,e.Nombre,e.Apellido,e.Sexo,e.FechaNac,e.Direccion,e.Telefono,e.Celular,e.Email,
		e.Dni,e.FechaIng,e.Sueldo,e.Estado,e.Usuario,e.Contrasenia,tu.Descripcion AS TipoUsuario
		FROM empleado AS e INNER JOIN tipousuario AS tu ON e.IdTipoUsuario = tu.IdTipoUsuario 
		WHERE e.Dni LIKE CONCAT("%",pbusqueda,"%");
	ELSE
	   SELECT e.IdEmpleado,e.Nombre,e.Apellido,e.Sexo,e.FechaNac,e.Direccion,e.Telefono,e.Celular,e.Email,
		e.Dni,e.FechaIng,e.Sueldo,e.Estado,e.Usuario,e.Contrasenia,tu.Descripcion AS TipoUsuario
		FROM empleado AS e INNER JOIN tipousuario AS tu ON e.IdTipoUsuario = tu.IdTipoUsuario;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_KardexValorizado` ()  select p.IdProducto,p.Codigo,p.Nombre,
c.descripcion as Categoria,

sum(dc.cantidad) as cantidadingreso,
avg(dc.precio) as precioingreso,
sum(dc.cantidad*dc.precio) as gastoingreso,

sum(dv.cantidad) as cantidadventa,
avg(dv.precio) as precioventa,
sum(dv.cantidad*dv.precio) as gastoventa,

sum(p.stock) as stock,
p.preciocosto as preciocompra,
sum(p.stock*p.preciocosto) as totalvalorizado

from producto p inner join categoria c
on p.idcategoria=c.idcategoria
inner join detallecompra dc 
on p.idproducto=dc.idproducto
inner join detalleventa dv
on p.idproducto=dv.idproducto
group by p.IdProducto,p.Codigo,p.Nombre,
c.descripcion$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Login` (IN `pusuario` VARCHAR(20) CHARSET utf8, IN `pcontrasenia` TEXT CHARSET utf8, IN `pdescripcion` VARCHAR(80) CHARSET utf8)  BEGIN
	
		SELECT e.IdEmpleado,e.Nombre,e.Apellido,e.Sexo,e.FechaNac,e.Direccion,e.Telefono,e.Celular,e.Email,
		e.Dni,e.FechaIng,e.Sueldo,e.Estado,e.Usuario,e.Contrasenia,tu.Descripcion
		FROM empleado AS e INNER JOIN tipousuario AS tu ON e.IdTipoUsuario = tu.IdTipoUsuario WHERE e.Usuario = pusuario AND e.Contrasenia = pcontrasenia AND tu.Descripcion=pdescripcion;
		
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_LoginPermisos` (IN `pnombre_usuario` VARCHAR(20) CHARSET utf8, IN `pdescripcion_tipousuario` VARCHAR(80) CHARSET utf8)  BEGIN
	
		SELECT tu.IdTipoUsuario,e.Usuario,tu.Descripcion,tu.p_venta,tu.p_compra,tu.p_producto,tu.p_proveedor,tu.p_empleado,tu.p_cliente,tu.p_categoria,tu.p_tipodoc,tu.p_tipouser,
		tu.p_anularv,tu.p_anularc,tu.p_estadoprod,tu.p_ventare,tu.p_ventade,tu.p_estadistica,tu.p_comprare,tu.p_comprade,tu.p_pass,tu.p_respaldar,tu.p_restaurar,tu.p_caja
		FROM tipousuario AS tu INNER JOIN empleado AS e ON tu.IdTipoUsuario = e.IdTipoUsuario WHERE e.Usuario = pnombre_usuario AND tu.Descripcion=pdescripcion_tipousuario;
		
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Producto` ()  BEGIN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,
		c.Descripcion AS categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria=c.IdCategoria
		ORDER BY p.IdProducto;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ProductoActivo` ()  BEGIN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria=c.IdCategoria WHERE p.Estado="Activo"
		ORDER BY p.IdProducto;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ProductoActivoPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(50) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.IdProducto=pbusqueda AND p.Estado="Activo";
 	ELSEIF pcriterio = "codigo" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Codigo=pbusqueda AND p.Estado="Activo";
	ELSEIF pcriterio = "nombre" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Nombre LIKE CONCAT("%",pbusqueda,"%") AND p.Estado="Activo";
	ELSEIF pcriterio = "descripcion" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Descripcion LIKE CONCAT("%",pbusqueda,"%") AND p.Estado="Activo";
	ELSEIF pcriterio = "categoria" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE c.Descripcion LIKE CONCAT("%",pbusqueda,"%") AND p.Estado="Activo";
	ELSE
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria WHERE p.Estado="Activo";
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ProductoPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(50) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.IdProducto=pbusqueda;
	ELSEIF pcriterio = "codigo" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Codigo=pbusqueda;
	ELSEIF pcriterio = "nombre" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Nombre LIKE CONCAT("%",pbusqueda,"%");
	ELSEIF pcriterio = "descripcion" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Descripcion LIKE CONCAT("%",pbusqueda,"%");
	ELSEIF pcriterio = "categoria" THEN
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE c.Descripcion LIKE CONCAT("%",pbusqueda,"%");
	ELSE
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ProductoVerificarCodigoBar` (IN `pbusqueda` VARCHAR(50) CHARSET utf8)  BEGIN
	
		SELECT p.IdProducto,p.Codigo,p.Nombre,p.Descripcion,p.Stock,p.StockMin,p.PrecioCosto,p.PrecioVenta,p.Utilidad,p.Estado,c.Descripcion AS Categoria,p.imagen, p.FechaVencimiento
		FROM producto AS p INNER JOIN categoria AS c ON p.IdCategoria = c.IdCategoria
		WHERE p.Codigo=pbusqueda;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Proveedor` ()  BEGIN
		SELECT * FROM proveedor;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_ProveedorPorParametro` (IN `pcriterio` VARCHAR(20), IN `pbusqueda` VARCHAR(20))  BEGIN
	IF pcriterio = "id" THEN
		SELECT p.IdProveedor,p.Nombre,p.Ruc,p.Dni,p.Direccion,p.Telefono,p.Celular,p.Email,p.Cuenta1,p.Cuenta2,p.Estado,p.Obsv FROM proveedor AS p WHERE p.IdProveedor=pbusqueda;
	ELSEIF pcriterio = "nombre" THEN
		SELECT p.IdProveedor,p.Nombre,p.Ruc,p.Dni,p.Direccion,p.Telefono,p.Celular,p.Email,p.Cuenta1,p.Cuenta2,p.Estado,p.Obsv FROM proveedor AS p WHERE p.Nombre LIKE CONCAT("%",pbusqueda,"%");
	ELSEIF pcriterio = "ruc" THEN
		SELECT p.IdProveedor,p.Nombre,p.Ruc,p.Dni,p.Direccion,p.Telefono,p.Celular,p.Email,p.Cuenta1,p.Cuenta2,p.Estado,p.Obsv FROM proveedor AS p WHERE p.Ruc LIKE CONCAT("%",pbusqueda,"%");
   ELSEIF pcriterio = "dni" THEN
		SELECT p.IdProveedor,p.Nombre,p.Ruc,p.Dni,p.Direccion,p.Telefono,p.Celular,p.Email,p.Cuenta1,p.Cuenta2,p.Estado,p.Obsv FROM proveedor AS p WHERE p.Dni LIKE CONCAT("%",pbusqueda,"%");
	ELSE
		SELECT p.IdProveedor,p.Nombre,p.Ruc,p.Dni,p.Direccion,p.Telefono,p.Celular,p.Email,p.Cuenta1,p.Cuenta2,p.Estado,p.Obsv FROM proveedor AS p;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_TipoDocumento` ()  BEGIN
		SELECT * FROM tipodocumento;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_TipoDocumentoPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT td.IdTipoDocumento,td.Descripcion FROM tipodocumento AS td WHERE td.IdTipoDocumento=pbusqueda;
	ELSEIF pcriterio = "descripcion" THEN
		SELECT td.IdTipoDocumento,td.Descripcion FROM tipodocumento AS td WHERE td.Descripcion LIKE CONCAT("%",pbusqueda,"%");
	ELSE
		SELECT td.IdTipoDocumento,td.Descripcion FROM tipodocumento AS td;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_TipoUsuario` ()  BEGIN
		SELECT * FROM tipousuario;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_TipoUsuarioPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
	IF pcriterio = "id" THEN
		SELECT * FROM tipousuario AS tp WHERE tp.IdTipoUsuario=pbusqueda;
	ELSEIF pcriterio = "descripcion" THEN
		SELECT * FROM tipousuario AS tp WHERE tp.Descripcion LIKE CONCAT("%",pbusqueda,"%");
	ELSE
		SELECT * FROM tipousuario AS tp;
	END IF; 
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_UltimoIdCompra` ()  BEGIN
		SELECT MAX(IdCompra) AS id FROM compra;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_UltimoIdVenta` ()  BEGIN
		SELECT MAX(IdVenta) AS id FROM venta;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Venta` ()  BEGIN
		SELECT v.IdVenta,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalVenta,v.Descuento,v.SubTotal,
		v.Igv,v.TotalPagar,v.Estado
		FROM venta AS v 
		INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
		INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
		INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
		ORDER BY v.IdVenta;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_VentaMensual` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pfecha_ini` VARCHAR(20) CHARSET utf8, IN `pfecha_fin` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "consultar" THEN
				SELECT CONCAT(UPPER(MONTHNAME(v.Fecha))," ",YEAR(v.Fecha)) AS Fecha,SUM(v.TotalVenta) AS Total,
				ROUND((SUM(v.TotalVenta)*100)/(SELECT SUM(v.TotalPagar) AS TotalVenta FROM venta AS v WHERE ((date_format(v.Fecha,'%Y-%m') >= pfecha_ini) AND (date_format(v.Fecha,'%Y-%m') <= pfecha_fin)) AND v.Estado="EMITIDO")) AS Porcentaje
				FROM venta AS v
				WHERE ((date_format(v.Fecha,'%Y-%m') >= pfecha_ini) AND (date_format(v.Fecha,'%Y-%m') <= pfecha_fin)) AND v.Estado="EMITIDO" GROUP BY v.Fecha;			
								
			END IF; 
			

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_VentaPorDetalle` (IN `pcriterio` VARCHAR(30) CHARSET utf8, IN `pfechaini` DATE, IN `pfechafin` DATE)  BEGIN
		IF pcriterio = "consultar" THEN
			SELECT p.Codigo,p.Nombre AS Producto,c.Descripcion AS Categoria,dv.Costo,dv.Precio,
			SUM(dv.Cantidad) AS Cantidad,SUM(dv.Total) AS Total,
			SUM(TRUNCATE((Total-(dv.Costo*dv.Cantidad)),2)) AS Ganancia FROM venta AS v
			INNER JOIN detalleventa AS dv ON v.IdVenta=dv.IdVenta
			INNER JOIN producto AS p ON dv.IdProducto=p.IdProducto
			INNER JOIN categoria AS c ON p.IdCategoria=c.IdCategoria
			WHERE (v.Fecha>=pfechaini AND v.Fecha<=pfechafin) AND v.Estado="EMITIDO" GROUP BY p.IdProducto
			ORDER BY v.IdVenta DESC;
		END IF;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_VentaPorFecha` (IN `pcriterio` VARCHAR(30) CHARSET utf8, IN `pfechaini` DATE, IN `pfechafin` DATE, IN `pdocumento` VARCHAR(30) CHARSET utf8)  BEGIN
		IF pcriterio = "anular" THEN
			SELECT v.IdVenta,c.Nombre AS Cliente,v.Fecha,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,td.Descripcion AS TipoDocumento,v.Serie,v.Numero,
			v.Estado,v.TotalPagar  FROM venta AS v
			INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
			INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
			INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
			WHERE (v.Fecha>=pfechaini AND v.Fecha<=pfechafin) AND td.Descripcion=pdocumento ORDER BY v.IdVenta DESC;
		ELSEIF pcriterio = "consultar" THEN
		   SELECT v.IdVenta,c.Nombre AS Cliente,v.Fecha,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,td.Descripcion AS TipoDocumento,v.Serie,v.Numero,
			v.Estado,v.TotalVenta,v.Descuento,v.TotalPagar  FROM venta AS v
			INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
			INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
			INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
			WHERE (v.Fecha>=pfechaini AND v.Fecha<=pfechafin) ORDER BY v.IdVenta DESC;
		ELSEIF pcriterio = "caja" THEN	
		   SELECT SUM(dv.Cantidad) AS Cantidad,p.Nombre AS Producto,dv.Precio,
			SUM(dv.Total) AS Total, SUM(TRUNCATE((Total-(dv.Costo*dv.Cantidad)),2)) AS Ganancia,v.Fecha FROM venta AS v
			INNER JOIN detalleventa AS dv ON v.IdVenta=dv.IdVenta
			INNER JOIN producto AS p ON dv.IdProducto=p.IdProducto
			INNER JOIN categoria AS c ON p.IdCategoria=c.IdCategoria
			WHERE v.Fecha=pfechaini AND v.Estado="EMITIDO" GROUP BY p.IdProducto
			ORDER BY v.IdVenta DESC;
		END IF;

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_VentaPorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "id" THEN
				SELECT v.IdVenta,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalVenta,v.Descuento,v.SubTotal,
				v.Igv,v.TotalPagar,v.Estado  FROM venta AS v
				INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
				INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
				INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
				WHERE v.IdVenta=pbusqueda ORDER BY v.IdVenta;
			ELSEIF pcriterio = "documento" THEN
				SELECT v.IdVenta,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalVenta,v.Descuento,v.SubTotal,
				v.Igv,v.TotalPagar,v.Estado  FROM venta AS v
				INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
				INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
				INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
				WHERE td.Descripcion=pbusqueda ORDER BY v.IdVenta;
			END IF; 
			

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_S_Venta_DetallePorParametro` (IN `pcriterio` VARCHAR(20) CHARSET utf8, IN `pbusqueda` VARCHAR(20) CHARSET utf8)  BEGIN
			IF pcriterio = "id" THEN
				SELECT v.IdVenta,td.Descripcion AS TipoDocumento,c.Nombre AS Cliente,CONCAT(e.Nombre," ",e.Apellido) AS Empleado,v.Serie,v.Numero,v.Fecha,v.TotalVenta,v.Descuento,v.SubTotal,
				v.Igv,v.TotalPagar,v.Estado,p.Codigo,p.Nombre,dv.Cantidad,p.PrecioVenta,dv.Total  FROM venta AS v
				INNER JOIN tipodocumento AS td ON v.IdTipoDocumento=td.IdTipoDocumento
				INNER JOIN cliente AS c ON v.IdCliente=c.IdCliente
				INNER JOIN empleado AS e ON v.IdEmpleado=e.IdEmpleado
				INNER JOIN detalleventa AS dv ON v.IdVenta=dv.IdVenta
				INNER JOIN producto AS p ON dv.IdProducto=p.IdProducto
				WHERE v.IdVenta=pbusqueda ORDER BY v.IdVenta;
			
			END IF; 
			

	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_ActualizarCompraEstado` (IN `pidcompra` INT, IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN
		UPDATE compra SET
			estado=pestado
		WHERE idcompra = pidcompra;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_ActualizarProductoStock` (IN `pidproducto` INT, IN `pstock` DECIMAL(8,2))  BEGIN
		UPDATE producto SET
			stock=pstock
		WHERE idproducto = pidproducto;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_ActualizarVentaEstado` (IN `pidventa` INT, IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN
		UPDATE venta SET
			estado=pestado
		WHERE idventa = pidventa;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_CambiarPass` (IN `pidempleado` INT, IN `pcontrase&ntilde;a` TEXT CHARSET utf8)  BEGIN
		UPDATE empleado SET
			contraseña=pcontraseña
		WHERE idempleado = pidempleado;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Categoria` (IN `pidcategoria` INT, IN `pdescripcion` VARCHAR(100) CHARSET utf8)  BEGIN
		UPDATE categoria SET
			descripcion=pdescripcion	
		WHERE idcategoria = pidcategoria;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Cliente` (IN `pidcliente` INT, IN `pnombre` VARCHAR(100) CHARSET utf8, IN `pruc` VARCHAR(11) CHARSET utf8, IN `pdni` VARCHAR(8) CHARSET utf8, IN `pdireccion` VARCHAR(50) CHARSET utf8, IN `ptelefono` VARCHAR(15) CHARSET utf8, IN `pobsv` TEXT CHARSET utf8)  BEGIN
		UPDATE cliente SET
			nombre=pnombre,
			ruc=pruc,
			dni=pdni,
			direccion=pdireccion,
			telefono=ptelefono,
			obsv=pobsv
		WHERE idcliente = pidcliente;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Compra` (IN `pidcompra` INT, IN `pidtipodocumento` INT, IN `pidproveedor` INT, IN `pidempleado` INT, IN `pnumero` VARCHAR(20) CHARSET utf8, IN `pfecha` DATE, IN `psubtotal` DECIMAL(8,2), IN `pigv` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN
		UPDATE compra SET
			idtipodocumento=pidtipodocumento,
			idproveedor=pidproveedor,
			idempleado=pidempleado,
			numero=pnumero,
			fecha=pfecha,
			subtotal=psubtotal,
			igv=pigv,
			total=ptotal,
			estado=pestado
		WHERE idcompra = pidcompra;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_DetalleCompra` (IN `pidcompra` INT, IN `pidproducto` INT, IN `pcantidad` DECIMAL(8,2), IN `pprecio` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2))  BEGIN
		UPDATE venta SET
			idcompra=pidcompra,
			idproducto=pidproducto,
			cantidad=pcantidad,
			precio=pprecio,
			total=ptotal
		WHERE idcompra = pidcompra;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_DetalleVenta` (IN `pidventa` INT, IN `pidproducto` INT, IN `pcantidad` DECIMAL(8,2), IN `pcosto` DECIMAL(8,2), IN `pprecio` DECIMAL(8,2), IN `ptotal` DECIMAL(8,2))  BEGIN
		UPDATE venta SET
			idventa=pidventa,
			idproducto=pidproducto,
			cantidad=pcantidad,
			costo=pcosto,
			precio=pprecio,
			total=ptotal
		WHERE idventa = pidventa;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Empleado` (IN `pidempleado` INT, IN `pnombre` VARCHAR(50) CHARSET utf8, IN `papellido` VARCHAR(80) CHARSET utf8, IN `psexo` VARCHAR(1) CHARSET utf8, IN `pfechanac` DATE, IN `pdireccion` VARCHAR(100) CHARSET utf8, IN `ptelefono` VARCHAR(10) CHARSET utf8, IN `pcelular` VARCHAR(15) CHARSET utf8, IN `pemail` VARCHAR(80) CHARSET utf8, IN `pdni` VARCHAR(8) CHARSET utf8, IN `pfechaing` DATE, IN `psueldo` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8, IN `pusuario` VARCHAR(20) CHARSET utf8, IN `pcontrasenia` TEXT CHARSET utf8, IN `pidtipousuario` INT)  BEGIN
		UPDATE empleado SET
			nombre=pnombre,
			apellido=papellido,
			sexo=psexo,
			fechanac=pfechanac,
			direccion=pdireccion,
			telefono=ptelefono,
			celular=pcelular,
			email=pemail,
			dni=pdni,
			fechaing=pfechaing,
			sueldo=psueldo,
			estado=pestado,
			usuario=pusuario,
			contrasenia=pcontrasenia,
			idtipousuario=pidtipousuario			
		WHERE idempleado = pidempleado;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Producto` (IN `pidproducto` INT, IN `pcodigo` VARCHAR(50) CHARSET utf8, IN `pnombre` VARCHAR(100) CHARSET utf8, IN `pdescripcion` TEXT CHARSET utf8, IN `pstock` DECIMAL(8,2), IN `pstockmin` DECIMAL(8,2), IN `ppreciocosto` DECIMAL(8,2), IN `pprecioventa` DECIMAL(8,2), IN `putilidad` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8, IN `pidcategoria` INT, IN `pimagen` VARCHAR(50) CHARSET utf8, IN `pfechavencimiento` DATE)  BEGIN
		UPDATE producto SET
			codigo=pcodigo,
			nombre=pnombre,
			descripcion=pdescripcion,
			stock=pstock,
			stockmin=pstockmin,
			preciocosto=ppreciocosto,
			precioventa=pprecioventa,
			utilidad=putilidad,
			estado=pestado,
			idcategoria=pidcategoria,
			imagen=pimagen,
            fechavencimiento = pfechavencimiento
			
		WHERE idproducto = pidproducto;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Proveedor` (IN `pidproveedor` INT, IN `pnombre` VARCHAR(100) CHARSET utf8, IN `pruc` VARCHAR(11) CHARSET utf8, IN `pdni` VARCHAR(8) CHARSET utf8, IN `pdireccion` VARCHAR(100) CHARSET utf8, IN `ptelefono` VARCHAR(10) CHARSET utf8, IN `pcelular` VARCHAR(15) CHARSET utf8, IN `pemail` VARCHAR(80) CHARSET utf8, IN `pcuenta1` VARCHAR(50) CHARSET utf8, IN `pcuenta2` VARCHAR(50) CHARSET utf8, IN `pestado` VARCHAR(30) CHARSET utf8, IN `pobsv` TEXT CHARSET utf8)  BEGIN
		UPDATE proveedor SET
			nombre=pnombre,
			ruc=pruc,
			dni=pdni,
			direccion=pdireccion,
			telefono=ptelefono,
			celular=pcelular,
			email=pemail,
			cuenta1=pcuenta1,
			cuenta2=pcuenta2,
			estado=pestado,
			obsv=pobsv
		WHERE idproveedor = pidproveedor;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_TipoDocumento` (IN `pidtipodocumento` INT, IN `pdescripcion` VARCHAR(80) CHARSET utf8)  BEGIN
		UPDATE tipodocumento SET
			descripcion=pdescripcion	
		WHERE idtipodocumento = pidtipodocumento;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_TipoUsuario` (IN `pidtipousuario` INT, IN `pdescripcion` VARCHAR(80) CHARSET utf8, IN `pp_venta` INT, IN `pp_compra` INT, IN `pp_producto` INT, IN `pp_proveedor` INT, IN `pp_empleado` INT, IN `pp_cliente` INT, IN `pp_categoria` INT, IN `pp_tipodoc` INT, IN `pp_tipouser` INT, IN `pp_anularv` INT, IN `pp_anularc` INT, IN `pp_estadoprod` INT, IN `pp_ventare` INT, IN `pp_ventade` INT, IN `pp_estadistica` INT, IN `pp_comprare` INT, IN `pp_comprade` INT, IN `pp_pass` INT, IN `pp_respaldar` INT, IN `pp_restaurar` INT, IN `pp_caja` INT)  BEGIN
		UPDATE tipousuario SET
			descripcion=pdescripcion,
			p_venta=pp_venta,
			p_compra=pp_compra,
			p_producto=pp_producto,
			p_proveedor=pp_proveedor,
			p_empleado=pp_empleado,
			p_cliente=pp_cliente,
			p_categoria=pp_categoria,
			p_tipodoc=pp_tipodoc,
			p_tipouser=pp_tipouser,
			p_anularv=pp_anularv,
			p_anularc=pp_anularc,
			p_estadoprod=pp_estadoprod,
			p_ventare=pp_ventare,
			p_ventade=pp_ventade,
			p_estadistica=pp_estadistica,
			p_comprare=pp_comprare,
			p_comprade=pp_comprade,
			p_pass=pp_pass,
			p_respaldar=pp_respaldar,
			p_restaurar=pp_restaurar,
			p_caja=pp_caja
		WHERE idtipousuario = pidtipousuario;
	END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_U_Venta` (IN `pidventa` INT, IN `pidtipodocumento` INT, IN `pidcliente` INT, IN `pidempleado` INT, IN `pserie` VARCHAR(5) CHARSET utf8, IN `pnumero` VARCHAR(20) CHARSET utf8, IN `pfecha` DATE, IN `ptotalventa` DECIMAL(8,2), IN `pdescuento` DECIMAL(8,2), IN `psubtotal` DECIMAL(8,2), IN `pigv` DECIMAL(8,2), IN `ptotalpagar` DECIMAL(8,2), IN `pestado` VARCHAR(30) CHARSET utf8)  BEGIN
		UPDATE venta SET
			idtipodocumento=pidtipodocumento,
			idcliente=pidcliente,
			idempleado=pidempleado,
			serie=pserie,
			numero=pnumero,
			fecha=pfecha,
			totalventa=ptotalventa,
			descuento=pdescuento,
			subtotal=psubtotal,
			igv=pigv,
			totalpagar=ptotalpagar,
			estado=pestado
		WHERE idventa = pidventa;
	END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `IdCategoria` int(11) NOT NULL,
  `Descripcion` varchar(100) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`IdCategoria`, `Descripcion`) VALUES
(5, 'BEBIDAS'),
(6, 'BEBIDASALCHOLICAS'),
(7, 'COMESTIBLES'),
(8, 'CIGARRILLOS'),
(9, 'VERDURAS');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `IdCliente` int(11) NOT NULL,
  `Nombre` varchar(100) CHARACTER SET utf8 NOT NULL,
  `Ruc` varchar(11) CHARACTER SET utf8 DEFAULT NULL,
  `Dni` varchar(8) CHARACTER SET utf8 DEFAULT NULL,
  `Direccion` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `Telefono` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  `Obsv` mediumtext CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`IdCliente`, `Nombre`, `Ruc`, `Dni`, `Direccion`, `Telefono`, `Obsv`) VALUES
(1, 'PUBLICO GENERAL', '10477157770', '', 'Chiclayo', '979026684', ''),
(2, 'FULANO', '456897', '65475785', 'Calle las calles entre calle y otra calle', '43869547', 'Ninguna observación'),
(3, 'Fulano Fulanes Fuentes', '', '45872455', 'Calle las calles entre calle y otra calle', '45216635', 'Estas son las observaciones de este cliente'),
(11, 'nuevo cliente', 'aasasas', 'asasas', 'asasas', 'asasas', 'asasas'),
(12, 'nuevo cliente 2', 'asasas', 'asasasas', 'asasas', 'asasas', 'asasas'),
(13, 'Gonzalo', 'asaas', 'asasas', 'asasas', 'asasas', 'asasas'),
(14, 'Fernando', 'asasasa', 'asasas', 'asasas', 'asasas', 'asasas'),
(16, 'Felipe', 'asasas', 'asasas', 'asasas', 'asasas', 'asasas'),
(28, 'Carlos Cardenas', 'asasas', '6564788', 'Calle calle entre calle y otra calle', '78546955', 'asasasas'),
(29, 'Valentin', 'asasas', 'asas', 'asasas', 'asasas', 'asasasasasasa'),
(30, 'Damaris Cordoba Claros', '7878877878', '7878', 'aasasas', '79658745', 'asasasasas'),
(31, 'Gustavo Adolfo Bequer', '5656331', '65478552', 'Calle las clalles', '4562365', 'Observaciones de Gustavo'),
(32, 'Thalia', '45266224', '', 'Calle los llantos', '45632565', 'Estas son las observaciones'),
(33, 'Sandra', '454545', '4554545', 'dirección', '4556981', 'Descripción'),
(34, 'Este es el nombre o razón social', '565613384', '', 'Calle x en y y z', '4587659', 'Ningúna observación'),
(35, 'Fulano Fulanes', '565664646', '', 'Calle las calles', '4565898', 'Esta es la observación'),
(36, 'Gonzalo Fuentes', '', '65478754', 'Calle las calles', '4563265', 'Estas son las observaciones'),
(37, 'Sergio', '45656565', '', 'Calle las calles', '4569856', 'ninguna'),
(38, 'Rolando Rojas Jaramillo', '', '6548798', 'Calle clas calles', '7859654', 'onbs');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compra`
--

CREATE TABLE `compra` (
  `IdCompra` int(11) NOT NULL,
  `IdTipoDocumento` int(11) NOT NULL,
  `IdProveedor` int(11) NOT NULL,
  `IdEmpleado` int(11) NOT NULL,
  `Numero` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `SubTotal` decimal(8,2) DEFAULT NULL,
  `Igv` decimal(8,2) DEFAULT NULL,
  `Total` decimal(8,2) DEFAULT NULL,
  `Estado` varchar(30) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `compra`
--

INSERT INTO `compra` (`IdCompra`, `IdTipoDocumento`, `IdProveedor`, `IdEmpleado`, `Numero`, `Fecha`, `SubTotal`, `Igv`, `Total`, `Estado`) VALUES
(1, 1, 2, 1, 'C00001', '2022-02-13', '254.24', '45.76', '300.00', 'ANULADO'),
(2, 3, 1, 1, 'C00002', '2022-02-13', '0.00', '0.00', '0.00', 'ANULADO'),
(3, 1, 1, 1, 'C00003', '2022-02-13', '5.08', '0.91', '6.00', 'NORMAL');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `credito`
--

CREATE TABLE `credito` (
  `IdCredito` int(11) NOT NULL,
  `IdTipoDocumento` int(11) NOT NULL,
  `IdCliente` int(11) NOT NULL,
  `IdEmpleado` int(11) NOT NULL,
  `Serie` varchar(5) CHARACTER SET utf8 DEFAULT NULL,
  `Numero` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `Fecha` date NOT NULL,
  `TotalCredito` decimal(8,2) NOT NULL,
  `Descuento` decimal(8,2) NOT NULL,
  `SubTotal` decimal(8,2) NOT NULL,
  `Igv` decimal(8,2) NOT NULL,
  `TotalPagar` decimal(8,2) NOT NULL,
  `Estado` varchar(30) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `credito`
--

INSERT INTO `credito` (`IdCredito`, `IdTipoDocumento`, `IdCliente`, `IdEmpleado`, `Serie`, `Numero`, `Fecha`, `TotalCredito`, `Descuento`, `SubTotal`, `Igv`, `TotalPagar`, `Estado`) VALUES
(1, 1, 3, 1, '001', 'C00001', '2022-06-22', '50.00', '0.00', '42.37', '7.63', '50.00', 'EMITIDO'),
(2, 1, 1, 1, '001', 'C00002', '2022-06-22', '30.00', '0.00', '25.42', '4.58', '30.00', 'EMITIDO'),
(3, 1, 1, 1, '001', 'C00003', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(4, 1, 1, 1, '001', 'C00004', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(5, 1, 2, 1, '001', 'C00005', '2022-06-22', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(6, 1, 3, 1, '001', 'C00006', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(7, 1, 1, 1, '001', 'C00007', '2022-06-22', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(8, 1, 1, 1, '001', 'C00008', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(9, 1, 2, 1, '001', 'C00009', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(10, 1, 2, 1, '001', 'C00010', '2022-06-23', '10.00', '0.00', '8.47', '1.52', '10.00', 'ANULADO'),
(11, 1, 3, 1, '001', 'C00011', '2022-06-23', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(12, 1, 1, 1, '001', 'C00012', '2022-06-23', '50.00', '0.00', '42.37', '7.63', '50.00', 'EMITIDO'),
(13, 1, 1, 1, '001', 'C00013', '2022-06-23', '30.00', '0.00', '25.42', '4.58', '30.00', 'ANULADO'),
(14, 1, 2, 1, '001', 'C00014', '2022-06-23', '20.00', '0.00', '16.95', '3.05', '20.00', 'ANULADO'),
(15, 1, 29, 1, '001', 'C00015', '2022-06-23', '70.00', '0.00', '59.32', '10.68', '70.00', 'EMITIDO'),
(16, 1, 31, 1, '001', 'C00016', '2022-06-23', '70.00', '0.00', '59.32', '10.68', '70.00', 'EMITIDO'),
(17, 1, 1, 1, '001', 'C00017', '2022-06-25', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(18, 2, 2, 1, '001', 'C00018', '2022-06-25', '40.00', '0.00', '33.90', '6.10', '40.00', 'EMITIDO'),
(19, 2, 3, 1, '001', 'C00019', '2022-06-25', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(20, 1, 34, 1, '001', 'C00020', '2022-06-25', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(21, 2, 3, 1, '001', 'C00021', '2022-06-25', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(22, 3, 28, 1, '001', 'C00022', '2022-06-25', '29.00', '0.00', '24.58', '4.42', '29.00', 'EMITIDO'),
(23, 1, 2, 1, '001', 'C00023', '2022-06-26', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallecompra`
--

CREATE TABLE `detallecompra` (
  `IdCompra` int(11) NOT NULL,
  `IdProducto` int(11) NOT NULL,
  `Cantidad` decimal(8,2) NOT NULL,
  `Precio` decimal(8,2) NOT NULL,
  `Total` decimal(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `detallecompra`
--

INSERT INTO `detallecompra` (`IdCompra`, `IdProducto`, `Cantidad`, `Precio`, `Total`) VALUES
(1, 2, '50.00', '6.00', '300.00'),
(3, 3, '1.00', '6.00', '6.00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallecredito`
--

CREATE TABLE `detallecredito` (
  `IdCredito` int(11) NOT NULL,
  `IdProducto` int(11) NOT NULL,
  `Cantidad` decimal(8,2) NOT NULL,
  `Costo` decimal(8,2) NOT NULL,
  `Precio` decimal(8,2) NOT NULL,
  `Total` decimal(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `detallecredito`
--

INSERT INTO `detallecredito` (`IdCredito`, `IdProducto`, `Cantidad`, `Costo`, `Precio`, `Total`) VALUES
(7, 1, '1.00', '6.00', '10.00', '10.00'),
(8, 1, '2.00', '6.00', '10.00', '20.00'),
(9, 1, '2.00', '6.00', '10.00', '20.00'),
(10, 3, '1.00', '6.00', '10.00', '10.00'),
(11, 2, '1.00', '6.00', '10.00', '10.00'),
(12, 2, '5.00', '6.00', '10.00', '50.00'),
(13, 3, '3.00', '6.00', '10.00', '30.00'),
(14, 3, '2.00', '6.00', '10.00', '20.00'),
(15, 3, '7.00', '6.00', '10.00', '70.00'),
(16, 2, '3.00', '6.00', '10.00', '30.00'),
(16, 1, '4.00', '6.00', '10.00', '40.00'),
(17, 1, '2.00', '6.00', '10.00', '20.00'),
(18, 3, '4.00', '6.00', '10.00', '40.00'),
(19, 2, '2.00', '6.00', '10.00', '20.00'),
(20, 3, '1.00', '6.00', '10.00', '10.00'),
(21, 3, '1.00', '6.00', '10.00', '10.00'),
(22, 5, '1.00', '5.00', '9.00', '9.00'),
(22, 1, '2.00', '6.00', '10.00', '20.00'),
(23, 3, '1.00', '6.00', '10.00', '10.00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalleventa`
--

CREATE TABLE `detalleventa` (
  `IdVenta` int(11) NOT NULL,
  `IdProducto` int(11) NOT NULL,
  `Cantidad` decimal(8,2) NOT NULL,
  `Costo` decimal(8,2) NOT NULL,
  `Precio` decimal(8,2) NOT NULL,
  `Total` decimal(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `detalleventa`
--

INSERT INTO `detalleventa` (`IdVenta`, `IdProducto`, `Cantidad`, `Costo`, `Precio`, `Total`) VALUES
(1, 3, '1.00', '6.00', '10.00', '10.00'),
(2, 1, '1.00', '6.00', '10.00', '10.00'),
(3, 1, '1.00', '6.00', '10.00', '10.00'),
(3, 3, '19.00', '6.00', '10.00', '114.00'),
(4, 1, '1.00', '6.00', '10.00', '10.00'),
(5, 1, '1.00', '6.00', '10.00', '10.00'),
(8, 1, '2.00', '6.00', '10.00', '20.00'),
(9, 1, '2.00', '6.00', '10.00', '20.00'),
(10, 3, '4.00', '6.00', '10.00', '40.00'),
(12, 2, '10.00', '6.00', '10.00', '100.00'),
(13, 3, '45.00', '6.00', '10.00', '450.00'),
(14, 5, '1.00', '5.00', '9.00', '9.00'),
(15, 3, '2.00', '6.00', '10.00', '20.00'),
(16, 3, '5.00', '6.00', '10.00', '50.00'),
(17, 1, '2.00', '6.00', '10.00', '20.00'),
(18, 3, '1.00', '6.00', '10.00', '10.00'),
(19, 1, '2.00', '6.00', '10.00', '20.00'),
(19, 3, '1.00', '6.00', '10.00', '10.00'),
(20, 3, '1.00', '6.00', '10.00', '10.00'),
(21, 3, '1.00', '6.00', '10.00', '10.00'),
(22, 3, '1.00', '6.00', '10.00', '10.00'),
(22, 4, '1.00', '5.00', '8.00', '8.00'),
(23, 1, '2.00', '6.00', '10.00', '12.00'),
(23, 4, '1.00', '5.00', '8.00', '8.00'),
(24, 2, '2.00', '6.00', '10.00', '20.00'),
(25, 3, '7.00', '6.00', '10.00', '70.00'),
(26, 1, '7.00', '6.00', '10.00', '70.00'),
(27, 4, '4.00', '5.00', '8.00', '32.00'),
(27, 3, '7.00', '6.00', '10.00', '70.00'),
(27, 1, '7.00', '6.00', '10.00', '70.00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `IdEmpleado` int(11) NOT NULL,
  `Nombre` varchar(50) CHARACTER SET utf8 NOT NULL,
  `Apellido` varchar(80) CHARACTER SET utf8 NOT NULL,
  `Sexo` varchar(1) CHARACTER SET utf8 NOT NULL,
  `FechaNac` date NOT NULL,
  `Direccion` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `Telefono` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `Celular` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  `Email` varchar(80) CHARACTER SET utf8 DEFAULT NULL,
  `Dni` varchar(8) CHARACTER SET utf8 DEFAULT NULL,
  `FechaIng` date NOT NULL,
  `Sueldo` decimal(8,2) DEFAULT NULL,
  `Estado` varchar(30) CHARACTER SET utf8 NOT NULL,
  `Usuario` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `Contrasenia` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `IdTipoUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`IdEmpleado`, `Nombre`, `Apellido`, `Sexo`, `FechaNac`, `Direccion`, `Telefono`, `Celular`, `Email`, `Dni`, `FechaIng`, `Sueldo`, `Estado`, `Usuario`, `Contrasenia`, `IdTipoUsuario`) VALUES
(1, 'Admin', 'Autor', 'M', '2013-06-15', '', '4444', '4444', '', '', '2013-06-15', '750.00', 'ACTIVO', 'admin', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 1),
(2, 'DEMO', 'DEMO', 'M', '2022-01-31', '', '', '', '', '202020', '2022-01-31', '0.00', 'ACTIVO', 'DEMO', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 4),
(3, 'sadsadsadsadsad', 'asdsadsad', 'M', '2022-02-10', '', 'dcv', 's', '', '12321323', '2022-02-10', '0.00', 'ACTIVO', 'qweqwe', '3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `IdProducto` int(11) NOT NULL,
  `Codigo` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `Nombre` varchar(100) CHARACTER SET utf8 NOT NULL,
  `Descripcion` mediumtext CHARACTER SET utf8 DEFAULT NULL,
  `Stock` decimal(8,2) DEFAULT NULL,
  `StockMin` decimal(8,2) DEFAULT NULL,
  `PrecioCosto` decimal(8,2) DEFAULT NULL,
  `PrecioVenta` decimal(8,2) DEFAULT NULL,
  `Utilidad` decimal(8,2) DEFAULT NULL,
  `Estado` varchar(30) CHARACTER SET utf8 NOT NULL,
  `IdCategoria` int(11) NOT NULL,
  `Imagen` varchar(50) CHARACTER SET utf8 NOT NULL,
  `FechaVencimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`IdProducto`, `Codigo`, `Nombre`, `Descripcion`, `Stock`, `StockMin`, `PrecioCosto`, `PrecioVenta`, `Utilidad`, `Estado`, `IdCategoria`, `Imagen`, `FechaVencimiento`) VALUES
(1, '0001', 'Coca - cola 2 litros no retornable', 'Cocacola 2 litros no retornable', '4958.00', '10.00', '6.00', '10.00', '4.00', 'ACTIVO', 5, 'imagen.png', NULL),
(2, '0002', 'Sprite 2 litros no retornable', 'sprite 2 litros no retornable', '4977.00', '10.00', '6.00', '10.00', '4.00', 'ACTIVO', 5, 'sprite_1.jpg', NULL),
(3, '0003', 'fanta 2 litros no retornable', 'fanta 2 litros no retornable, económico', '4911.00', '10.00', '6.00', '10.00', '4.00', 'ACTIVO', 5, 'imagen.png', NULL),
(4, '0004', 'lechuga hidroponica', 'lechuga hidroponica', '4994.00', '10.00', '5.00', '8.00', '3.00', 'ACTIVO', 9, 'imagen.png', NULL),
(5, '0005', 'cerveza en lata pace?a peque?a', 'cerveza pace?a en lata peque?a', '4998.00', '10.00', '5.00', '9.00', '4.00', 'ACTIVO', 6, 'imagen.png', '2025-06-01'),
(23, '315417', 'fanta 2 litros no retornable', 'fanta 2 litros no retornable, económico', '4911.00', '10.00', '6.00', '10.00', '4.00', 'ACTIVO', 5, 'imagen.png', NULL),
(25, '441016', 'Coca - cola 2 litros no retornable', 'Cocacola 2 litros no retornable', '4958.00', '10.00', '6.00', '10.00', '4.00', 'INACTIVO', 5, 'imagen.png', NULL),
(26, '445540', 'Coca - cola 2 litros no retornable', 'Cocacola 2 litros no retornable', '4958.00', '10.00', '6.00', '10.00', '4.00', 'ACTIVO', 5, 'imagen.png', NULL),
(27, '323499', 'Coca - cola 2 litros no retornable', 'Cocacola 2 litros no retornable', '4958.00', '10.00', '6.00', '10.00', '4.00', 'INACTIVO', 5, 'imagen.png', '2023-06-11');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `IdProveedor` int(11) NOT NULL,
  `Nombre` varchar(100) CHARACTER SET utf8 NOT NULL,
  `Ruc` varchar(11) CHARACTER SET utf8 DEFAULT NULL,
  `Dni` varchar(8) CHARACTER SET utf8 DEFAULT NULL,
  `Direccion` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `Telefono` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `Celular` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  `Email` varchar(80) CHARACTER SET utf8 DEFAULT NULL,
  `Cuenta1` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `Cuenta2` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `Estado` varchar(30) CHARACTER SET utf8 NOT NULL,
  `Obsv` mediumtext CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`IdProveedor`, `Nombre`, `Ruc`, `Dni`, `Direccion`, `Telefono`, `Celular`, `Email`, `Cuenta1`, `Cuenta2`, `Estado`, `Obsv`) VALUES
(1, 'SIN PROVEEDOR', '', '', '', '', '', '', '', '', 'ACTIVO', ''),
(2, 'DISTRIBUIDOR EYJ', '', '', '', '', '', '', '', '', 'ACTIVO', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipodocumento`
--

CREATE TABLE `tipodocumento` (
  `IdTipoDocumento` int(11) NOT NULL,
  `Descripcion` varchar(80) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `tipodocumento`
--

INSERT INTO `tipodocumento` (`IdTipoDocumento`, `Descripcion`) VALUES
(1, 'BOLETA'),
(2, 'FACTURA'),
(3, 'TICKET');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipousuario`
--

CREATE TABLE `tipousuario` (
  `IdTipoUsuario` int(11) NOT NULL,
  `Descripcion` varchar(20) CHARACTER SET utf8 NOT NULL,
  `p_venta` int(11) DEFAULT NULL,
  `p_compra` int(11) DEFAULT NULL,
  `p_producto` int(11) DEFAULT NULL,
  `p_proveedor` int(11) DEFAULT NULL,
  `p_empleado` int(11) DEFAULT NULL,
  `p_cliente` int(11) DEFAULT NULL,
  `p_categoria` int(11) DEFAULT NULL,
  `p_tipodoc` int(11) DEFAULT NULL,
  `p_tipouser` int(11) DEFAULT NULL,
  `p_anularv` int(11) DEFAULT NULL,
  `p_anularc` int(11) DEFAULT NULL,
  `p_estadoprod` int(11) DEFAULT NULL,
  `p_ventare` int(11) DEFAULT NULL,
  `p_ventade` int(11) DEFAULT NULL,
  `p_estadistica` int(11) DEFAULT NULL,
  `p_comprare` int(11) DEFAULT NULL,
  `p_comprade` int(11) DEFAULT NULL,
  `p_pass` int(11) DEFAULT NULL,
  `p_respaldar` int(11) DEFAULT NULL,
  `p_restaurar` int(11) DEFAULT NULL,
  `p_caja` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `tipousuario`
--

INSERT INTO `tipousuario` (`IdTipoUsuario`, `Descripcion`, `p_venta`, `p_compra`, `p_producto`, `p_proveedor`, `p_empleado`, `p_cliente`, `p_categoria`, `p_tipodoc`, `p_tipouser`, `p_anularv`, `p_anularc`, `p_estadoprod`, `p_ventare`, `p_ventade`, `p_estadistica`, `p_comprare`, `p_comprade`, `p_pass`, `p_respaldar`, `p_restaurar`, `p_caja`) VALUES
(1, 'ADMINISTRADOR', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
(2, 'CAJERO', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1),
(3, 'VENDEDOR', 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0),
(4, 'DEMO', 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `IdVenta` int(11) NOT NULL,
  `IdTipoDocumento` int(11) NOT NULL,
  `IdCliente` int(11) NOT NULL,
  `IdEmpleado` int(11) NOT NULL,
  `Serie` varchar(5) CHARACTER SET utf8 DEFAULT NULL,
  `Numero` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `Fecha` date NOT NULL,
  `TotalVenta` decimal(8,2) NOT NULL,
  `Descuento` decimal(8,2) NOT NULL,
  `SubTotal` decimal(8,2) NOT NULL,
  `Igv` decimal(8,2) NOT NULL,
  `TotalPagar` decimal(8,2) NOT NULL,
  `Estado` varchar(30) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Volcado de datos para la tabla `venta`
--

INSERT INTO `venta` (`IdVenta`, `IdTipoDocumento`, `IdCliente`, `IdEmpleado`, `Serie`, `Numero`, `Fecha`, `TotalVenta`, `Descuento`, `SubTotal`, `Igv`, `TotalPagar`, `Estado`) VALUES
(1, 2, 1, 1, '001', 'C00001', '2022-02-12', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(2, 1, 1, 1, '001', 'C00002', '2022-02-13', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(3, 1, 16, 1, '001', 'C00003', '2022-06-21', '124.00', '0.00', '105.08', '18.91', '124.00', 'EMITIDO'),
(4, 1, 11, 1, '001', 'C00004', '2022-06-21', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(5, 1, 1, 1, '001', 'C00005', '2022-06-21', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(8, 1, 1, 1, '001', 'C00006', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(9, 1, 3, 1, '001', 'C00009', '2022-06-22', '20.00', '0.00', '16.95', '3.05', '20.00', 'ANULADO'),
(10, 1, 28, 1, '001', 'C00010', '2022-06-23', '40.00', '0.00', '33.90', '6.10', '40.00', 'EMITIDO'),
(11, 1, 30, 1, '001', 'C00011', '2022-06-23', '0.00', '0.00', '0.00', '0.00', '0.00', 'EMITIDO'),
(12, 1, 31, 1, '001', 'C00012', '2022-06-23', '100.00', '0.00', '84.75', '15.25', '100.00', 'EMITIDO'),
(13, 1, 32, 1, '001', 'C00013', '2022-06-23', '450.00', '0.00', '381.36', '68.64', '450.00', 'EMITIDO'),
(14, 1, 34, 1, '001', 'C00014', '2022-06-24', '9.00', '0.00', '7.63', '1.37', '9.00', 'EMITIDO'),
(15, 1, 35, 1, '001', 'C00015', '2022-06-24', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(16, 1, 1, 1, '001', 'C00016', '2022-06-24', '50.00', '0.00', '42.37', '7.63', '50.00', 'EMITIDO'),
(17, 1, 1, 1, '001', 'C00017', '2022-06-25', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(18, 1, 1, 1, '001', 'C00018', '2022-06-25', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(19, 1, 33, 1, '001', 'C00019', '2022-06-25', '30.00', '0.00', '25.42', '4.58', '30.00', 'EMITIDO'),
(20, 2, 13, 1, '001', 'C00020', '2022-06-25', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(21, 3, 1, 1, '001', 'C00021', '2022-06-25', '10.00', '0.00', '8.47', '1.52', '10.00', 'EMITIDO'),
(22, 1, 1, 1, '001', 'C00022', '2022-06-26', '18.00', '0.00', '15.25', '2.75', '18.00', 'EMITIDO'),
(23, 1, 1, 1, '001', 'C00023', '2022-06-26', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(24, 1, 1, 1, '001', 'C00024', '2022-06-26', '20.00', '0.00', '16.95', '3.05', '20.00', 'EMITIDO'),
(25, 1, 2, 1, '001', 'C00025', '2022-06-27', '70.00', '0.00', '59.32', '10.68', '70.00', 'EMITIDO'),
(26, 1, 35, 1, '001', 'C00026', '2022-06-28', '70.00', '0.00', '59.32', '10.68', '70.00', 'EMITIDO'),
(27, 1, 32, 1, '001', 'C00027', '2022-06-29', '172.00', '0.00', '145.76', '26.24', '172.00', 'EMITIDO');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`IdCategoria`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`IdCliente`);

--
-- Indices de la tabla `compra`
--
ALTER TABLE `compra`
  ADD PRIMARY KEY (`IdCompra`),
  ADD KEY `fk_Compra_Proveedor1_idx` (`IdProveedor`),
  ADD KEY `fk_Compra_Empleado1_idx` (`IdEmpleado`),
  ADD KEY `fk_Compra_TipoDocumento1_idx` (`IdTipoDocumento`);

--
-- Indices de la tabla `credito`
--
ALTER TABLE `credito`
  ADD PRIMARY KEY (`IdCredito`),
  ADD KEY `fk_Venta_TipoDocumento1_idx` (`IdTipoDocumento`),
  ADD KEY `fk_Venta_Cliente1_idx` (`IdCliente`),
  ADD KEY `fk_Venta_Empleado1_idx` (`IdEmpleado`);

--
-- Indices de la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  ADD KEY `fk_DetalleCompra_Compra1_idx` (`IdCompra`),
  ADD KEY `fk_DetalleCompra_Producto1_idx` (`IdProducto`);

--
-- Indices de la tabla `detallecredito`
--
ALTER TABLE `detallecredito`
  ADD KEY `fk_DetalleVenta_Producto1_idx` (`IdProducto`),
  ADD KEY `fk_DetalleVenta_Venta1_idx` (`IdCredito`);

--
-- Indices de la tabla `detalleventa`
--
ALTER TABLE `detalleventa`
  ADD KEY `fk_DetalleVenta_Producto1_idx` (`IdProducto`),
  ADD KEY `fk_DetalleVenta_Venta1_idx` (`IdVenta`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`IdEmpleado`),
  ADD KEY `fk_Empleado_TipoUsuario1_idx` (`IdTipoUsuario`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`IdProducto`),
  ADD KEY `fk_Producto_Categoria_idx` (`IdCategoria`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`IdProveedor`);

--
-- Indices de la tabla `tipodocumento`
--
ALTER TABLE `tipodocumento`
  ADD PRIMARY KEY (`IdTipoDocumento`);

--
-- Indices de la tabla `tipousuario`
--
ALTER TABLE `tipousuario`
  ADD PRIMARY KEY (`IdTipoUsuario`);

--
-- Indices de la tabla `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`IdVenta`),
  ADD KEY `fk_Venta_TipoDocumento1_idx` (`IdTipoDocumento`),
  ADD KEY `fk_Venta_Cliente1_idx` (`IdCliente`),
  ADD KEY `fk_Venta_Empleado1_idx` (`IdEmpleado`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `IdCategoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `IdCliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT de la tabla `compra`
--
ALTER TABLE `compra`
  MODIFY `IdCompra` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `credito`
--
ALTER TABLE `credito`
  MODIFY `IdCredito` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `IdEmpleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `IdProducto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `IdProveedor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `tipodocumento`
--
ALTER TABLE `tipodocumento`
  MODIFY `IdTipoDocumento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipousuario`
--
ALTER TABLE `tipousuario`
  MODIFY `IdTipoUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `venta`
--
ALTER TABLE `venta`
  MODIFY `IdVenta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `compra`
--
ALTER TABLE `compra`
  ADD CONSTRAINT `fk_Compra_Empleado1` FOREIGN KEY (`IdEmpleado`) REFERENCES `empleado` (`IdEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Compra_Proveedor1` FOREIGN KEY (`IdProveedor`) REFERENCES `proveedor` (`IdProveedor`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Compra_TipoDocumento1` FOREIGN KEY (`IdTipoDocumento`) REFERENCES `tipodocumento` (`IdTipoDocumento`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `credito`
--
ALTER TABLE `credito`
  ADD CONSTRAINT `credito_ibfk_1` FOREIGN KEY (`IdCliente`) REFERENCES `cliente` (`IdCliente`),
  ADD CONSTRAINT `credito_ibfk_2` FOREIGN KEY (`IdEmpleado`) REFERENCES `empleado` (`IdEmpleado`),
  ADD CONSTRAINT `credito_ibfk_3` FOREIGN KEY (`IdTipoDocumento`) REFERENCES `tipodocumento` (`IdTipoDocumento`);

--
-- Filtros para la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  ADD CONSTRAINT `fk_DetalleCompra_Compra1` FOREIGN KEY (`IdCompra`) REFERENCES `compra` (`IdCompra`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_DetalleCompra_Producto1` FOREIGN KEY (`IdProducto`) REFERENCES `producto` (`IdProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `detallecredito`
--
ALTER TABLE `detallecredito`
  ADD CONSTRAINT `detallecredito_ibfk_1` FOREIGN KEY (`IdCredito`) REFERENCES `credito` (`IdCredito`),
  ADD CONSTRAINT `detallecredito_ibfk_2` FOREIGN KEY (`IdProducto`) REFERENCES `producto` (`IdProducto`);

--
-- Filtros para la tabla `detalleventa`
--
ALTER TABLE `detalleventa`
  ADD CONSTRAINT `fk_DetalleVenta_Producto1` FOREIGN KEY (`IdProducto`) REFERENCES `producto` (`IdProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_DetalleVenta_Venta1` FOREIGN KEY (`IdVenta`) REFERENCES `venta` (`IdVenta`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `fk_Empleado_TipoUsuario1` FOREIGN KEY (`IdTipoUsuario`) REFERENCES `tipousuario` (`IdTipoUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `fk_Producto_Categoria` FOREIGN KEY (`IdCategoria`) REFERENCES `categoria` (`IdCategoria`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `fk_Venta_Cliente1` FOREIGN KEY (`IdCliente`) REFERENCES `cliente` (`IdCliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Venta_Empleado1` FOREIGN KEY (`IdEmpleado`) REFERENCES `empleado` (`IdEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Venta_TipoDocumento1` FOREIGN KEY (`IdTipoDocumento`) REFERENCES `tipodocumento` (`IdTipoDocumento`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
