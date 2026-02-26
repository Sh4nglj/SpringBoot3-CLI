USE `example`;

DROP TABLE IF EXISTS `repair_order`;
CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`                    BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_no`              VARCHAR(50)      DEFAULT NULL COMMENT '工单编号',
    `device_type`           TINYINT UNSIGNED DEFAULT NULL COMMENT '设备类型',
    `device_location`       VARCHAR(200)     DEFAULT NULL COMMENT '设备位置',
    `fault_type`            TINYINT UNSIGNED DEFAULT NULL COMMENT '故障类型',
    `fault_description`     VARCHAR(1000)    DEFAULT NULL COMMENT '故障描述',
    `fault_images`          VARCHAR(2000)    DEFAULT NULL COMMENT '故障图片，多图用英文逗号分隔',
    `status`                TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待处理，1-维修中，2-已完成',
    `priority`              TINYINT UNSIGNED DEFAULT 1 COMMENT '优先级：0-低，1-中，2-高，3-紧急',
    `assignment_method`     TINYINT UNSIGNED DEFAULT NULL COMMENT '分配方式：0-自动分配，1-手动分配',
    `assignee_id`           BIGINT UNSIGNED  DEFAULT NULL COMMENT '指派人ID',
    `assigned_time`         DATETIME         DEFAULT NULL COMMENT '分配时间',
    `started_time`          DATETIME         DEFAULT NULL COMMENT '开始维修时间',
    `completed_time`        DATETIME         DEFAULT NULL COMMENT '完成时间',
    `repair_result`         VARCHAR(1000)    DEFAULT NULL COMMENT '维修结果',
    `creator_id`            BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`           DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`            BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`           DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`               TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`            TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX idx_order_no (`order_no`),
    INDEX idx_status (`status`),
    INDEX idx_creator_id (`creator_id`),
    INDEX idx_assignee_id (`assignee_id`),
    INDEX idx_create_time (`create_time`)
) COMMENT '报修工单表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_record`;
CREATE TABLE IF NOT EXISTS `repair_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `repair_order_id` BIGINT UNSIGNED  DEFAULT NULL COMMENT '工单ID',
    `record_type`     TINYINT UNSIGNED DEFAULT NULL COMMENT '记录类型：0-接单，1-开始维修，2-暂停，3-继续，4-完成，5-备注',
    `content`         VARCHAR(2000)    DEFAULT NULL COMMENT '记录内容',
    `images`          VARCHAR(2000)    DEFAULT NULL COMMENT '图片，多图用英文逗号分隔',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX idx_repair_order_id (`repair_order_id`),
    INDEX idx_create_time (`create_time`)
) COMMENT '维修记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_evaluation`;
CREATE TABLE IF NOT EXISTS `repair_evaluation`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `repair_order_id` BIGINT UNSIGNED  DEFAULT NULL COMMENT '工单ID',
    `rating`          TINYINT UNSIGNED DEFAULT NULL COMMENT '评分，1-5星',
    `content`         VARCHAR(2000)    DEFAULT NULL COMMENT '评价内容',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    UNIQUE KEY uk_repair_order_id (`repair_order_id`)
) COMMENT '报修评价表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE IF NOT EXISTS `sys_notification`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`         BIGINT UNSIGNED  DEFAULT NULL COMMENT '接收用户ID',
    `type`            TINYINT UNSIGNED DEFAULT NULL COMMENT '通知类型：0-系统通知，1-维修完成，2-工单分配',
    `title`           VARCHAR(200)     DEFAULT NULL COMMENT '通知标题',
    `content`         VARCHAR(2000)    DEFAULT NULL COMMENT '通知内容',
    `related_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联ID',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-未读，1-已读',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `read_time`       DATETIME         DEFAULT NULL COMMENT '阅读时间',
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`),
    INDEX idx_create_time (`create_time`)
) COMMENT '站内信通知表' COLLATE = utf8mb4_unicode_ci;
