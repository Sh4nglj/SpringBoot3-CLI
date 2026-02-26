-- 设备报修系统数据库表结构
-- 创建时间: 2025-02-26

-- 报修单表
CREATE TABLE IF NOT EXISTS `repair_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no` VARCHAR(20) NOT NULL COMMENT '报修单号',
    `user_id` BIGINT NOT NULL COMMENT '报修用户ID',
    `device_type` VARCHAR(50) NOT NULL COMMENT '设备类型',
    `location` VARCHAR(200) NOT NULL COMMENT '设备位置',
    `fault_type` VARCHAR(50) NOT NULL COMMENT '故障类型',
    `description` VARCHAR(1000) NOT NULL COMMENT '故障描述',
    `fault_images` VARCHAR(2000) DEFAULT NULL COMMENT '故障照片URL，多个用逗号分隔',
    `status` TINYINT NOT NULL DEFAULT '0' COMMENT '状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消',
    `priority` TINYINT NOT NULL DEFAULT '2' COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `assign_type` TINYINT DEFAULT NULL COMMENT '分配类型：1-自动分配，2-手动分配',
    `repairman_id` BIGINT DEFAULT NULL COMMENT '维修人员ID',
    `assign_time` DATETIME DEFAULT NULL COMMENT '分配时间',
    `accept_time` DATETIME DEFAULT NULL COMMENT '接单时间',
    `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '是否删除：0-否，1-是',
    `version` INT NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_repairman_id` (`repairman_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修单表';

-- 维修记录表
CREATE TABLE IF NOT EXISTS `repair_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT NOT NULL COMMENT '报修单ID',
    `repairman_id` BIGINT NOT NULL COMMENT '维修人员ID',
    `start_time` DATETIME NOT NULL COMMENT '维修开始时间',
    `end_time` DATETIME NOT NULL COMMENT '维修结束时间',
    `repair_content` VARCHAR(2000) NOT NULL COMMENT '维修内容',
    `replaced_parts` VARCHAR(1000) DEFAULT NULL COMMENT '更换配件',
    `repair_cost` DECIMAL(10,2) DEFAULT '0.00' COMMENT '维修费用',
    `repair_result` TINYINT NOT NULL COMMENT '维修结果：1-修复成功，2-无法修复，3-需要返厂',
    `repair_images` VARCHAR(2000) DEFAULT NULL COMMENT '维修后照片URL',
    `remark` VARCHAR(1000) DEFAULT NULL COMMENT '备注',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '是否删除：0-否，1-是',
    `version` INT NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_repairman_id` (`repairman_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修记录表';

-- 维修评价表
CREATE TABLE IF NOT EXISTS `repair_evaluation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT NOT NULL COMMENT '报修单ID',
    `user_id` BIGINT NOT NULL COMMENT '评价用户ID',
    `repairman_id` BIGINT NOT NULL COMMENT '维修人员ID',
    `rating` TINYINT NOT NULL COMMENT '评分：1-5星',
    `content` VARCHAR(1000) DEFAULT NULL COMMENT '评价内容',
    `service_rating` TINYINT DEFAULT NULL COMMENT '服务态度评分',
    `quality_rating` TINYINT DEFAULT NULL COMMENT '维修质量评分',
    `speed_rating` TINYINT DEFAULT NULL COMMENT '响应速度评分',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '是否删除：0-否，1-是',
    `version` INT NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_repairman_id` (`repairman_id`),
    KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修评价表';

-- 维修人员表
CREATE TABLE IF NOT EXISTS `repairman` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `employee_no` VARCHAR(20) NOT NULL COMMENT '工号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
    `skills` VARCHAR(500) DEFAULT NULL COMMENT '专业技能',
    `work_status` TINYINT NOT NULL DEFAULT '0' COMMENT '工作状态：0-离线，1-在线空闲，2-忙碌',
    `current_orders` INT NOT NULL DEFAULT '0' COMMENT '当前工单数',
    `total_completed_orders` INT NOT NULL DEFAULT '0' COMMENT '总完成工单数',
    `average_rating` DECIMAL(2,1) DEFAULT '5.0' COMMENT '平均评分',
    `status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '是否删除：0-否，1-是',
    `version` INT NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_employee_no` (`employee_no`),
    KEY `idx_work_status` (`work_status`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修人员表';

-- 通知记录表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `type` TINYINT NOT NULL COMMENT '通知类型：1-短信，2-邮件，3-站内信，4-推送',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '通知标题',
    `content` VARCHAR(2000) NOT NULL COMMENT '通知内容',
    `biz_type` VARCHAR(50) DEFAULT NULL COMMENT '关联业务类型',
    `biz_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `is_read` TINYINT NOT NULL DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '读取时间',
    `send_status` TINYINT NOT NULL DEFAULT '1' COMMENT '发送状态：0-失败，1-成功',
    `fail_reason` VARCHAR(500) DEFAULT NULL COMMENT '失败原因',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_biz_type_id` (`biz_type`, `biz_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知记录表';

-- 插入示例维修人员数据
INSERT INTO `repairman` (`user_id`, `employee_no`, `name`, `phone`, `skills`, `work_status`, `status`) VALUES
(1, 'R001', '张三', '13800138001', '电脑,打印机,网络设备', 1, 1),
(2, 'R002', '李四', '13800138002', '空调,电器,水电', 1, 1),
(3, 'R003', '王五', '13800138003', '电脑,服务器,网络', 1, 1);
