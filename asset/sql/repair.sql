USE `example`;

DROP TABLE IF EXISTS `repair_order`;
CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_no`          VARCHAR(50)      NOT NULL COMMENT '工单编号',
    `device_type`       VARCHAR(100)     NOT NULL COMMENT '设备类型',
    `device_location`   VARCHAR(200)     NOT NULL COMMENT '设备位置',
    `fault_type`        VARCHAR(100)     NOT NULL COMMENT '故障类型',
    `fault_description` VARCHAR(1000)    DEFAULT NULL COMMENT '故障描述',
    `fault_images`      TEXT             DEFAULT NULL COMMENT '故障照片URL列表，JSON数组格式',
    `status`            TINYINT UNSIGNED DEFAULT 0 COMMENT '工单状态：0-待处理，1-维修中，2-已完成，3-已取消',
    `priority`          TINYINT UNSIGNED DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `assign_type`       TINYINT UNSIGNED DEFAULT NULL COMMENT '分配方式：1-自动分配，2-手动分配',
    `repairer_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '维修人员ID',
    `assign_time`       DATETIME         DEFAULT NULL COMMENT '分配时间',
    `accept_time`       DATETIME         DEFAULT NULL COMMENT '接单时间',
    `complete_time`     DATETIME         DEFAULT NULL COMMENT '完成时间',
    `user_id`           BIGINT UNSIGNED  NOT NULL COMMENT '报修用户ID',
    `user_phone`        VARCHAR(20)      DEFAULT NULL COMMENT '用户联系电话',
    `creator_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`           TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0-未删除，1-已删除',
    UNIQUE KEY `uk_order_no` (`order_no`)
) COMMENT '报修工单表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_record`;
CREATE TABLE IF NOT EXISTS `repair_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`        BIGINT UNSIGNED  NOT NULL COMMENT '工单ID',
    `record_type`     TINYINT UNSIGNED NOT NULL COMMENT '记录类型：1-接单确认，2-维修过程，3-维修完成',
    `record_content`  VARCHAR(1000)    DEFAULT NULL COMMENT '记录内容',
    `record_images`   TEXT             DEFAULT NULL COMMENT '记录照片URL列表，JSON数组格式',
    `repairer_id`     BIGINT UNSIGNED  NOT NULL COMMENT '维修人员ID',
    `repairer_name`   VARCHAR(50)      DEFAULT NULL COMMENT '维修人员姓名',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0-未删除，1-已删除',
    INDEX `idx_order_id` (`order_id`)
) COMMENT '维修记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_evaluation`;
CREATE TABLE IF NOT EXISTS `repair_evaluation`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`        BIGINT UNSIGNED  NOT NULL COMMENT '工单ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '评价用户ID',
    `repairer_id`     BIGINT UNSIGNED  NOT NULL COMMENT '维修人员ID',
    `rating`          TINYINT UNSIGNED NOT NULL COMMENT '评分：1-5星',
    `content`         VARCHAR(500)     DEFAULT NULL COMMENT '评价内容',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0-未删除，1-已删除',
    UNIQUE KEY `uk_order_id` (`order_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_repairer_id` (`repairer_id`)
) COMMENT '维修评价表' COLLATE = utf8mb4_unicode_ci;
