alter table category add column available_online BIT(1);

UPDATE category set available_online = 1;