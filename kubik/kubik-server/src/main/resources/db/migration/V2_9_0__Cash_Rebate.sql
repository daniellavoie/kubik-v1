ALTER TABLE invoice ADD COLUMN rebate_type VARCHAR(255) NULL AFTER `user_id`;
UPDATE invoice set rebate_type = 'PERCENT' where rebate_type is null;