-- 创建AI图片模板表，使用utf8mb4字符集支持emoji
-- 在MySQL中执行此脚本

CREATE TABLE IF NOT EXISTS `ai_images_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` VARCHAR(255) NOT NULL COMMENT '模板标题',
  `preview` VARCHAR(500) DEFAULT NULL COMMENT '预览图URL',
  `prompt` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '提示词内容（支持emoji）',
  `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
  `link` VARCHAR(500) DEFAULT NULL COMMENT '来源链接',
  `mode` VARCHAR(50) DEFAULT NULL COMMENT '模式（generate/edit）',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
  `sub_category` VARCHAR(50) DEFAULT NULL COMMENT '子分类',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI图片模板表';
