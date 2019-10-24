  CREATE TABLE `user_file_table` (
  `id` INT NOT NULL,
  `file_key` VARCHAR(256) CHARACTER SET 'utf8mb4' NOT NULL,
  `user_id` INT NOT NULL,
  `first_name` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL,
  `last_name` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL,
  `file_name` VARCHAR(256) CHARACTER SET 'utf8mb4' NOT NULL,
  `created_time` DATETIME NOT NULL,
  `updated_time_stamp` DATETIME NULL,
  `delete_flag` TINYINT NOT NULL DEFAULT 0,
  UNIQUE INDEX `file_key_UNIQUE` (`file_key` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);