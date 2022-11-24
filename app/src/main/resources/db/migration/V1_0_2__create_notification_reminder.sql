CREATE TABLE IF NOT EXISTS `notification_reminder`
(
    `id`              INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `notification_id` INT UNSIGNED NOT NULL,
    `reminder_date`   DATETIME     NOT NULL,
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `fk_notification_reminder_notification_id_idx` (`notification_id` ASC) VISIBLE,
    CONSTRAINT `fk_notification_reminder_notification_id`
        FOREIGN KEY (`notification_id`)
            REFERENCES `notification` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB