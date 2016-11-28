ALTER TABLE `product` 
DROP COLUMN `standard_label`,
DROP COLUMN `cash_register_label`,
CHANGE COLUMN `extended_label` `name` VARCHAR(255) NULL DEFAULT NULL;
