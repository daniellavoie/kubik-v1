INSERT INTO `kubik`.`address` (`id`, `city`, `state`, `street_line1`, `zip_code`) VALUES ('1', 'Nanterre', 'Ile de France', '589 Terrasses De L\'Arche', '92000');

INSERT INTO `kubik`.`customer` (`company_name`, `ean13`, `email`, `first_name`, `fixed_phone`, `internal_note`, `last_name`, `mobile_phone`, `address_id`, `creation_date`) VALUES ('CSP Informatique', '1234567891234', 'dlavoie@live.ca', 'Daniel', '01 45 97 73 33', 'Yo mama so fat', 'Lavoie', '06 45 97 73 33', 1, NOW());
