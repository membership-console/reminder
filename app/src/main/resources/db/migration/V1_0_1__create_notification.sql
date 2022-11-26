CREATE TABLE IF NOT EXISTS `notification`
(
    `id`             INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    `title`          VARCHAR(255)  NOT NULL,
    `body`           VARCHAR(1023) NOT NULL,
    `contributor_id` INT           NOT NULL,
    `posted_date`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_at`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB