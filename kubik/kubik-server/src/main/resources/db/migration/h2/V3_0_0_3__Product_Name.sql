ALTER TABLE product DROP COLUMN standard_label, cash_register_label;
ALTER TABLE product ALTER COLUMN extended_label RENAME TO name;