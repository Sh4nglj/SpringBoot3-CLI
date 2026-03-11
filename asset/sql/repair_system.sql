USE `example`;

-- 报修工单表
DROP TABLE IF EXISTS `repair_order`;
CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_no`         VARCHAR(32)      NOT NULL COMMENT '工单编号',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '报修用户ID',
    `device_type`      VARCHAR(50)      NOT NULL COMMENT '设备类型',
    `location`         VARCHAR(200)     NOT NULL COMMENT '设备位置',
    `fault_type`       VARCHAR(50)      NOT NULL COMMENT '故障类型',
    `description`      TEXT             NOT NULL COMMENT '故障描述',
    `priority`         TINYINT UNSIGNED DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `status`           TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消',
    `assignee_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '分配的维修人员ID',
    `assign_type`      TINYINT UNSIGNED DEFAULT 0 COMMENT '分配类型：0-自动，1-手动',
    `assign_time`      DATETIME         DEFAULT NULL COMMENT '分配时间',
    `expect_complete_time` DATETIME     DEFAULT NULL COMMENT '期望完成时间',
    `actual_complete_time` DATETIME     DEFAULT NULL COMMENT '实际完成时间',
    `creator_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_assignee_id` (`assignee_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) COMMENT '报修工单表' COLLATE = utf8mb4_unicode_ci;

-- 故障照片表
DROP TABLE IF EXISTS `repair_photo`;
CREATE TABLE IF NOT EXISTS `repair_photo`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`    BIGINT UNSIGNED  NOT NULL COMMENT '工单ID',
    `photo_url`   VARCHAR(500)     NOT NULL COMMENT '照片URL',
    `photo_name`  VARCHAR(100)     DEFAULT NULL COMMENT '照片名称',
    `sort_order`  INT              DEFAULT 0 COMMENT '排序',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    KEY `idx_order_id` (`order_id`)
) COMMENT '故障照片表' COLLATE = utf8mb4_unicode_ci;

-- 维修过程记录表
DROP TABLE IF EXISTS `repair_process`;
CREATE TABLE IF NOT EXISTS `repair_process`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`    BIGINT UNSIGNED  NOT NULL COMMENT '工单ID',
    `operator_id` BIGINT UNSIGNED  NOT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(50)    NOT NULL COMMENT '操作人名称',
    `action_type` TINYINT UNSIGNED NOT NULL COMMENT '操作类型：1-接单，2-开始维修，3-维修记录，4-完成维修，5-取消工单',
    `content`     TEXT             DEFAULT NULL COMMENT '操作内容/备注',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    KEY `idx_order_id` (`order_id`),
    KEY `idx_operator_id` (`operator_id`)
) COMMENT '维修过程记录表' COLLATE = utf8mb4_unicode_ci;

-- 评价表
DROP TABLE IF EXISTS `repair_rating`;
CREATE TABLE IF NOT EXISTS `repair_rating`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`    BIGINT UNSIGNED  NOT NULL COMMENT '工单ID',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '评价用户ID',
    `rating`      TINYINT UNSIGNED NOT NULL COMMENT '评分：1-5星',
    `content`     TEXT             DEFAULT NULL COMMENT '评价内容',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`)
) COMMENT '评价表' COLLATE = utf8mb4_unicode_ci;

-- 通知表
DROP TABLE IF EXISTS `repair_notification`;
CREATE TABLE IF NOT EXISTS `repair_notification`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID',
    `order_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联工单ID',
    `title`       VARCHAR(100)     NOT NULL COMMENT '通知标题',
    `content`     TEXT             NOT NULL COMMENT '通知内容',
    `type`        TINYINT UNSIGNED DEFAULT 1 COMMENT '通知类型：1-系统通知，2-工单通知',
    `is_read`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time`   DATETIME         DEFAULT NULL COMMENT '阅读时间',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_is_read` (`is_read`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;

-- 维修人员表（如果系统中没有用户表，可以扩展此表）
DROP TABLE IF EXISTS `repair_worker`;
CREATE TABLE IF NOT EXISTS `repair_worker`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `name`        VARCHAR(50)      NOT NULL COMMENT '姓名',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `specialty`   VARCHAR(100)     DEFAULT NULL COMMENT '专长',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-离线，1-在线，2-忙碌',
    `order_count` INT              DEFAULT 0 COMMENT '当前处理工单数量',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY `uk_user_id` (`user_id`)
) COMMENT '维修人员表' COLLATE = utf8mb4_unicode_ci;
