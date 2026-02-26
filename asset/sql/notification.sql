USE `example`;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID',
    `title`       VARCHAR(100)     NOT NULL COMMENT '通知标题',
    `content`     VARCHAR(1000)    DEFAULT NULL COMMENT '通知内容',
    `type`        TINYINT UNSIGNED DEFAULT 1 COMMENT '通知类型：1-站内信，2-短信',
    `is_read`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `order_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联工单ID',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0-未删除，1-已删除',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_order_id` (`order_id`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;
