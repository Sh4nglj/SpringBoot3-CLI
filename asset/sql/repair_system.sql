USE `example`;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_account`  VARCHAR(256)       DEFAULT NULL COMMENT '账号',
    `user_password` VARCHAR(512)       DEFAULT NULL COMMENT '密码',
    `user_name`     VARCHAR(256)       DEFAULT NULL COMMENT '用户昵称',
    `user_avatar`   VARCHAR(1024)      DEFAULT NULL COMMENT '用户头像',
    `user_profile`  VARCHAR(512)       DEFAULT NULL COMMENT '用户简介',
    `user_role`     VARCHAR(256)       DEFAULT 'user' COMMENT '用户角色：user/admin/repair/ban',
    `phone`         VARCHAR(20)        DEFAULT NULL COMMENT '联系电话',
    `email`         VARCHAR(100)       DEFAULT NULL COMMENT '邮箱',
    `create_time`   DATETIME           DEFAULT NULL COMMENT '创建时间',
    `update_time`   DATETIME           DEFAULT NULL COMMENT '更新时间',
    `is_deleted`    TINYINT UNSIGNED   DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY `uk_user_account` (`user_account`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

-- 报修单表
DROP TABLE IF EXISTS `repair_order`;
CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_no`         VARCHAR(32)        NOT NULL COMMENT '报修单号',
    `user_id`          BIGINT UNSIGNED    NOT NULL COMMENT '报修用户ID',
    `device_type`      VARCHAR(100)       NOT NULL COMMENT '设备类型',
    `device_location`  VARCHAR(200)       NOT NULL COMMENT '设备位置',
    `fault_type`       VARCHAR(100)       NOT NULL COMMENT '故障类型',
    `description`      TEXT               NOT NULL COMMENT '故障描述',
    `photos`           TEXT               DEFAULT NULL COMMENT '故障照片URL，多个用逗号分隔',
    `priority`         TINYINT UNSIGNED   DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `status`           TINYINT UNSIGNED   DEFAULT 0 COMMENT '状态：0-待处理，1-已分配，2-维修中，3-待确认，4-已完成，5-已取消',
    `assignee_id`      BIGINT UNSIGNED    DEFAULT NULL COMMENT '维修人员ID',
    `assign_type`      TINYINT UNSIGNED   DEFAULT 0 COMMENT '分配类型：0-自动，1-手动',
    `assign_time`      DATETIME           DEFAULT NULL COMMENT '分配时间',
    `accept_time`      DATETIME           DEFAULT NULL COMMENT '接单时间',
    `complete_time`    DATETIME           DEFAULT NULL COMMENT '完成时间',
    `expected_time`    DATETIME           DEFAULT NULL COMMENT '期望完成时间',
    `creator_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME           DEFAULT NULL COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME           DEFAULT NULL COMMENT '更新时间',
    `version`          TINYINT UNSIGNED   DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`       TINYINT UNSIGNED   DEFAULT 0 COMMENT '是否删除',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_assignee_id` (`assignee_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_order_no` (`order_no`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '报修单表' COLLATE = utf8mb4_unicode_ci;

-- 维修记录表
DROP TABLE IF EXISTS `repair_record`;
CREATE TABLE IF NOT EXISTS `repair_record`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`         BIGINT UNSIGNED    NOT NULL COMMENT '报修单ID',
    `repairer_id`      BIGINT UNSIGNED    NOT NULL COMMENT '维修人员ID',
    `action_type`      TINYINT UNSIGNED   NOT NULL COMMENT '操作类型：1-接单，2-开始维修，3-维修记录，4-更换配件，5-完成维修',
    `content`          TEXT               NOT NULL COMMENT '操作内容',
    `photos`           TEXT               DEFAULT NULL COMMENT '照片URL，多个用逗号分隔',
    `cost`             DECIMAL(10,2)      DEFAULT 0 COMMENT '费用',
    `spare_parts`      TEXT               DEFAULT NULL COMMENT '更换配件信息（JSON）',
    `creator_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME           DEFAULT NULL COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME           DEFAULT NULL COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED   DEFAULT 0 COMMENT '是否删除',
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_repairer_id` (`repairer_id`)
) COMMENT '维修记录表' COLLATE = utf8mb4_unicode_ci;

-- 评价表
DROP TABLE IF EXISTS `repair_rating`;
CREATE TABLE IF NOT EXISTS `repair_rating`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`         BIGINT UNSIGNED    NOT NULL COMMENT '报修单ID',
    `user_id`          BIGINT UNSIGNED    NOT NULL COMMENT '用户ID',
    `repairer_id`      BIGINT UNSIGNED    NOT NULL COMMENT '维修人员ID',
    `rating`           TINYINT UNSIGNED   NOT NULL COMMENT '评分：1-5星',
    `content`          TEXT               DEFAULT NULL COMMENT '评价内容',
    `tags`             VARCHAR(500)       DEFAULT NULL COMMENT '评价标签，多个用逗号分隔',
    `creator_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME           DEFAULT NULL COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME           DEFAULT NULL COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED   DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY `uk_order_id` (`order_id`),
    INDEX `idx_repairer_id` (`repairer_id`),
    INDEX `idx_user_id` (`user_id`)
) COMMENT '评价表' COLLATE = utf8mb4_unicode_ci;

-- 通知表
DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`          BIGINT UNSIGNED    NOT NULL COMMENT '接收用户ID',
    `sender_id`        BIGINT UNSIGNED    DEFAULT NULL COMMENT '发送者ID',
    `title`            VARCHAR(200)       NOT NULL COMMENT '通知标题',
    `content`          TEXT               NOT NULL COMMENT '通知内容',
    `type`             TINYINT UNSIGNED   DEFAULT 0 COMMENT '通知类型：0-系统通知，1-工单通知，2-维修通知',
    `related_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '关联ID（如工单ID）',
    `read_status`      TINYINT UNSIGNED   DEFAULT 0 COMMENT '读取状态：0-未读，1-已读',
    `read_time`        DATETIME           DEFAULT NULL COMMENT '读取时间',
    `creator_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME           DEFAULT NULL COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME           DEFAULT NULL COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED   DEFAULT 0 COMMENT '是否删除',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_read_status` (`read_status`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;

-- 维修人员技能表
DROP TABLE IF EXISTS `repairer_skill`;
CREATE TABLE IF NOT EXISTS `repairer_skill`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`          BIGINT UNSIGNED    NOT NULL COMMENT '维修人员ID',
    `device_type`      VARCHAR(100)       NOT NULL COMMENT '设备类型',
    `proficiency`      TINYINT UNSIGNED   DEFAULT 1 COMMENT '熟练度：1-初级，2-中级，3-高级',
    `creator_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME           DEFAULT NULL COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED    DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME           DEFAULT NULL COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED   DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY `uk_user_device` (`user_id`, `device_type`)
) COMMENT '维修人员技能表' COLLATE = utf8mb4_unicode_ci;
