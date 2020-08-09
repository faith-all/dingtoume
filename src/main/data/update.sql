-- 新增基金组合扩展数据
ALTER TABLE `user_fund_group`
ADD COLUMN `extra` JSON NULL AFTER `status`;

-- demo数据更新
UPDATE `user_fund_group` SET `extra` = '{\"maxPrice\":10000000}' WHERE (`fund_group_id` = '1');
UPDATE `user_fund_group` SET `extra` = '{\"maxPrice\":10000000}' WHERE (`fund_group_id` = '2');
