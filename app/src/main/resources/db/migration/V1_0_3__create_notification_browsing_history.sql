CREATE TABLE IF NOT EXISTS `notification_browsing_history`
(
    `notification_id` INT UNSIGNED NOT NULL,
    `user_id`         INT UNSIGNED NOT NULL,
    `viewed_date`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `fk_notification_browsing_history_notification_id_idx` (`notification_id` ASC) VISIBLE,
    CONSTRAINT `fk_notification_browsing_history_notification_id`
        FOREIGN KEY (`notification_id`)
            REFERENCES `notification` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB