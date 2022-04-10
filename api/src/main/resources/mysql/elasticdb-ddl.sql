-- MySQL Script generated by MySQL Workbench
-- Sat Apr  9 11:47:53 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Table `elasticdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `elasticdb`.`users` (
                                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                   `name` VARCHAR(45) NULL,
    `email` VARCHAR(45) NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `elasticdb`.`boards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `elasticdb`.`boards` (
                                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                    `title` VARCHAR(45) NULL,
    `comment` BLOB NULL,
    `create_date` DATETIME NULL,
    `modify_date` DATETIME NULL,
    `users_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_boards_users_idx` (`users_id` ASC),
    CONSTRAINT `fk_boards_users`
    FOREIGN KEY (`users_id`)
    REFERENCES `elasticdb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;