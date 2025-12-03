-- 修改ai_image_template表的字符集为utf8mb4以支持emoji
-- 在MySQL中执行此脚本

-- 方案1：删除表，让JPA重新创建（推荐，最简单）
DROP TABLE IF EXISTS ai_image_template;

-- 方案2：修改现有表的字符集（如果表中已有重要数据）
-- ALTER TABLE ai_image_template CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 可选：修改整个数据库的默认字符集
-- ALTER DATABASE botCore CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

