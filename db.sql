-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema projects_management
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema projects_management
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `projects_management` DEFAULT CHARACTER SET utf8 ;
USE `projects_management` ;

-- -----------------------------------------------------
-- Table `projects_management`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects_management`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_id` INT NOT NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  `status` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_users_roles_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_roles`
    FOREIGN KEY (`role_id`)
    REFERENCES `projects_management`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects_management`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `date_of_birth` DATE NULL,
  `position` VARCHAR(45) NOT NULL,
  `technologies` VARCHAR(1000) NULL,
  `start_date` DATE NOT NULL,
  `experience_before` FLOAT NULL,
  `photo` MEDIUMBLOB NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_employees_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_employees_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `projects_management`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects_management`.`tickets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`tickets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `assignee_id` INT NOT NULL,
  `reporter_id` INT NOT NULL,
  `ticket_id` INT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(1000) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `due_date` DATETIME NOT NULL,
  `estimated_time` FLOAT NULL,
  `logged_time` FLOAT NULL,
  `status` VARCHAR(100) NOT NULL,
  `type` VARCHAR(100) NOT NULL,
  `severity` INT NULL,
  `git` VARCHAR(200) NOT NULL,
  `order_number` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tickets_tickets1_idx` (`ticket_id` ASC) VISIBLE,
  INDEX `fk_tickets_employees2_idx` (`assignee_id` ASC) VISIBLE,
  INDEX `fk_tickets_employees1_idx` (`reporter_id` ASC) VISIBLE,
  CONSTRAINT `fk_tickets_tickets1`
    FOREIGN KEY (`ticket_id`)
    REFERENCES `projects_management`.`tickets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tickets_employees2`
    FOREIGN KEY (`assignee_id`)
    REFERENCES `projects_management`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tickets_employees1`
    FOREIGN KEY (`reporter_id`)
    REFERENCES `projects_management`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects_management`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`comments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `comment` VARCHAR(1000) NOT NULL,
  `ticket_id` INT NOT NULL,
  `employee_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_comments_tickets1_idx` (`ticket_id` ASC) VISIBLE,
  INDEX `fk_comments_employees1_idx` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `fk_comments_tickets1`
    FOREIGN KEY (`ticket_id`)
    REFERENCES `projects_management`.`tickets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_employees1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `projects_management`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects_management`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `project_id` INT NULL,
  `description` BLOB NOT NULL,
  `create_date` DATE NULL,
  `cost` DECIMAL(12,2) NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_orders_users1_idx` (`client_id` ASC) VISIBLE,
  INDEX `fk_orders_tickets1_idx` (`project_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`client_id`)
    REFERENCES `projects_management`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_tickets1`
    FOREIGN KEY (`project_id`)
    REFERENCES `projects_management`.`tickets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `projects_management`.`projects_has_employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `projects_management`.`projects_has_employees` (
  `tickets_id` INT NOT NULL,
  `employees_id` INT NOT NULL,
  INDEX `fk_tickets_has_employees_employees1_idx` (`employees_id` ASC) VISIBLE,
  INDEX `fk_tickets_has_employees_tickets1_idx` (`tickets_id` ASC) VISIBLE,
  PRIMARY KEY (`tickets_id`, `employees_id`),
  CONSTRAINT `fk_tickets_has_employees_tickets1`
    FOREIGN KEY (`tickets_id`)
    REFERENCES `projects_management`.`tickets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tickets_has_employees_employees1`
    FOREIGN KEY (`employees_id`)
    REFERENCES `projects_management`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
