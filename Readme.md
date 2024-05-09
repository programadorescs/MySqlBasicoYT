# Acceso a MySql desde Kotlin haciendo uso del Connector/J

Descubre cómo conectar de manera sencilla tu aplicación Android a una base de datos MySQL mediante el uso del connector/j. En este tutorial detallado, te guiaré paso a paso para que puedas implementar un CRUD completo, desde la creación hasta la eliminación de datos. ¡Aprende a integrar tu app con una base de datos de forma sencilla y efectiva!

## Requisitos

- Android Studio Jellyfish | 2023.3.1 o superior.
- Android Gradle Plugin Version 8.4.0
- Gradle Version 8.6
- Kotlin 1.9.22 o superior.

## Base de datos dbDemo en MySql

```sql
CREATE DATABASE `dbdemo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE dbdemo;

CREATE TABLE IF NOT EXISTS `dbdemo`.`producto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(200) NOT NULL,
  `codigobarra` VARCHAR(25) NOT NULL,
  `precio` DOUBLE(13,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `codigobarra` (`codigobarra` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
```

## Pantallazos de la app

![Image text](https://github.com/programadorescs/MySqlBasicoYT/blob/master/app/src/main/assets/MySqlBasicoYT_001.png)
![Image text](https://github.com/programadorescs/MySqlBasicoYT/blob/master/app/src/main/assets/MySqlBasicoYT_002.png)

## Video en YouTube
[![Alt text](https://img.youtube.com/vi/5IMrEkIg1J0/0.jpg)](https://www.youtube.com/watch?v=5IMrEkIg1J0)