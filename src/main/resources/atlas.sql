CREATE DATABASE IF NOT EXISTS `atlas` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE `atlas`;

DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `appid` varchar(255) NOT NULL COMMENT '运维申请应用ID',
  `name` varchar(45) NOT NULL COMMENT '应用名称',
  `env_urls` varchar(1024) DEFAULT NULL COMMENT '各个env访问域名',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `org_id` bigint(20) NOT NULL COMMENT '应用所属组织',
  `developers` varchar(512) DEFAULT NULL COMMENT '开发人员',
  `tests` varchar(512) DEFAULT NULL COMMENT '测试人员',
  `extensions` text COMMENT '扩展项目',
  `zone_type` varchar(45) NOT NULL DEFAULT 'default' COMMENT '部署区域类型',
  `enable_ha` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活HA',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insert_by` varchar(45) DEFAULT NULL COMMENT '插入人',
  `update_by` varchar(45) DEFAULT NULL COMMENT '更新者',
  `isactive` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `appid_index` (`appid`),
  UNIQUE KEY `app_name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用信息表';

DROP TABLE IF EXISTS `app_quota`;
CREATE TABLE `app_quota` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appid` varchar(45) NOT NULL COMMENT 'ops appid',
  `app_name` varchar(45) DEFAULT NULL COMMENT '应用名',
  `org_id` bigint(20) NOT NULL COMMENT '组织 id',
  `env_id` bigint(20) NOT NULL COMMENT '环境 id',
  `spectype_id` bigint(20) NOT NULL COMMENT '规格id',
  `number` bigint(20) NOT NULL COMMENT 'appid-envid-specttypeid 规格数量',
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `class_method` varchar(128) DEFAULT NULL COMMENT '请求方法',
  `class_method_args` varchar(2048) DEFAULT NULL COMMENT '请求参数',
  `class_method_return` varchar(2048) DEFAULT NULL COMMENT '请求返回结果',
  `client_ip` varchar(32) DEFAULT NULL COMMENT '发送请求的客户端ip 地址',
  `http_method` varchar(64) DEFAULT NULL COMMENT 'http 请求方法',
  `http_uri` varchar(256) DEFAULT NULL COMMENT 'http 请求路径',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(32) DEFAULT NULL COMMENT '登录后的用户名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作审计表';

DROP TABLE IF EXISTS `env`;
CREATE TABLE `env` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL COMMENT '环境名称',
  `description` varchar(200) DEFAULT NULL COMMENT '说明',
  `consul` varchar(200) NOT NULL COMMENT 'dockyard 镜像仓库地址',
  `nginx` varchar(255) DEFAULT NULL COMMENT 'nginx地址',
  `dns` varchar(255) DEFAULT NULL COMMENT 'dns地址',
  `dockeryard` varchar(255) DEFAULT NULL COMMENT '镜像仓库地址',
  `extensions` text COMMENT '扩展项',
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用环境表';

DROP TABLE IF EXISTS `locks`;
CREATE TABLE `locks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `version` bigint(20) NOT NULL DEFAULT '0' COMMENT '版本',
  `possessor` varchar(45) DEFAULT NULL COMMENT '持有者',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='锁';

DROP TABLE IF EXISTS `org`;
CREATE TABLE `org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `name` varchar(45) NOT NULL COMMENT '组织名',
  `org_code` varchar(50) NOT NULL COMMENT '组织编号',
  `parent_org_id` bigint(20) DEFAULT NULL COMMENT '上级机构ID',
  `user_work_number` varchar(50) NOT NULL COMMENT '组织负责人工号',
  `description` varchar(200) DEFAULT NULL COMMENT '组织描述',
  `extensions` text,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织信息表';

DROP TABLE IF EXISTS `quota`;
CREATE TABLE `quota` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用主键自增 id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织 id',
  `org_name` varchar(45) DEFAULT NULL COMMENT '组织名称',
  `env_id` bigint(20) NOT NULL COMMENT '资源池id',
  `cpu` bigint(8) NOT NULL COMMENT 'cpu 数量',
  `memory` bigint(8) NOT NULL COMMENT 'memory 数量',
  `disk` bigint(8) NOT NULL COMMENT 'disk 数量',
  `description` varchar(200) DEFAULT NULL,
  `extensions` text,
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配额表';


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色自增 ID',
  `name` varchar(45) NOT NULL COMMENT '角色名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `extensions` text COMMENT '扩展项',
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

DROP TABLE IF EXISTS `spec_type`;
CREATE TABLE `spec_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格表自增 id',
  `name` varchar(45) DEFAULT NULL COMMENT '规格类型名称',
  `cpu` float(8,2) DEFAULT NULL COMMENT 'cpu 数量',
  `memory` float(8,2) DEFAULT NULL COMMENT '内存容量',
  `disk` float(8,2) DEFAULT NULL COMMENT '硬盘大小',
  `description` varchar(200) DEFAULT NULL,
  `extensions` text,
  `create_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  `insert_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配额种类，没类 不同CPU、内存等数量';

DROP TABLE IF EXISTS `user_ext`;
CREATE TABLE `user_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户关联表 自增id',
  `user_work_number` varchar(50) NOT NULL COMMENT '用户 工号',
  `org_id` bigint(20) NOT NULL COMMENT '用户所在 部门 id',
  `insert_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  `user_name` varchar(255) DEFAULT NULL COMMENT '关联的user_info表unique字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单独出用户信息扩展表';

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户自增 id',
  `work_number` varchar(45) DEFAULT NULL COMMENT '工号',
  `real_name` varchar(45) NOT NULL COMMENT '用户真实姓名',
  `user_name` varchar(45) NOT NULL COMMENT '用户登录名昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `source` varchar(20) DEFAULT NULL COMMENT '来源',
  `ldap_insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '从 ldap 中插入时间',
  `ldap_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '从ldap 更新数据的时间',
  `extensions` varchar(200) DEFAULT NULL,
  `insert_by` varchar(200) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_visit_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  KEY `work_number` (`work_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同步 ldap 用户表';

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_work_number` varchar(50) NOT NULL COMMENT '用户工号',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `extensions` text,
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色映射表';

DROP TABLE IF EXISTS `zone`;
CREATE TABLE `zone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) DEFAULT NULL COMMENT 'zone名称',
  `env_id` bigint(20) NOT NULL COMMENT '环境id',
  `env_name` varchar(45) DEFAULT NULL COMMENT '环境名称',
  `k8s` varchar(200) DEFAULT NULL COMMENT 'k8s地址',
  `k8s_version` varchar(45) DEFAULT NULL COMMENT 'k8s版本',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `extensions` text COMMENT '扩展项',
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='zone表';

DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `type` varchar(128) NOT NULL COMMENT '申请类型',
  `status` varchar(128) NOT NULL COMMENT '申请状态',
  `request` text DEFAULT NULL COMMENT '申请参数json',
  `result` text DEFAULT NULL COMMENT '申请结果json',
  `apply_user` varchar(64) NOT NULL COMMENT '申请人',
  `apply_department` varchar(64) NOT NULL COMMENT '申请部门',
  `op_user` varchar(64) DEFAULT NULL COMMENT '操作人',
  `apply_time` timestamp NOT NULL COMMENT '申请时间',
  `insert_by` varchar(45) DEFAULT NULL,
  `update_by` varchar(45) DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isactive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='zone表';