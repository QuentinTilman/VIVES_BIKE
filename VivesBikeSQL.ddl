CREATE TABLE IF NOT EXISTS `Lid` (
  `rijksregisternummer` VARCHAR(11) NOT NULL,
  `voornaam` VARCHAR(255) NOT NULL,
  `naam` VARCHAR(255) NOT NULL,
  `geslacht` enum('M','V') NOT NULL,
  `telnr` VARCHAR(15) NOT NULL, 
  `emailadres` VARCHAR(255) NOT NULL,
  `start_lidmaatschap` DATE NOT NULL,
  `einde_lidmaatschap` DATE NULL,
  `opmerkingen` TEXT NULL,
  PRIMARY KEY (`rijksregisternummer`));


CREATE TABLE IF NOT EXISTS `Fiets` (
  `registratienummer` INT NOT NULL AUTO_INCREMENT,
  `status` ENUM('actief', 'herstel','uit_omloop') NOT NULL,
  `standplaats` ENUM('Oostende', 'Brugge', 'Tielt','Torhout', 'Roeselare','Kortrijk') NOT NULL,
  `opmerkingen` TEXT NULL,
  PRIMARY KEY (`registratienummer`));


CREATE TABLE IF NOT EXISTS `Rit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `starttijd` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `eindtijd` TIMESTAMP(3) NULL,
  `prijs` DECIMAL(2) NULL,
  `lid_rijksregisternummer` VARCHAR(11) NOT NULL,
  `fiets_registratienummer` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_fiets_registratienummer_idx` (`fiets_registratienummer` ASC),
  INDEX `fk_lid_rijksregisternummer_idx` (`lid_rijksregisternummer` ASC),
  CONSTRAINT `fk_fiets_registratienummer`
    FOREIGN KEY (`fiets_registratienummer`)
    REFERENCES `Fiets` (`registratienummer`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lid_rijksregisternummer`
    FOREIGN KEY (`lid_rijksregisternummer`)
    REFERENCES `Lid` (`rijksregisternummer`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION);