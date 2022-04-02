CREATE TABLE `b_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `biz_id` varchar(20) NOT NULL COMMENT '业务id',
  `tenant_id` varchar(20) NOT NULL COMMENT '租户id',
  `user_type` smallint(4) NOT NULL DEFAULT '0' COMMENT '用户类型:0:游客;1:注册用户',
  `user_name` varchar(150) NOT NULL COMMENT '用户名',
  `id_number` varchar(255) NOT NULL COMMENT '用户证件号',
  `id_num_type` smallint(4) NOT NULL DEFAULT '0' COMMENT '证件类型.0:身份证.1:护照;2:学生证',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据生成时间',
  `gmt_update` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_sp_0` (
  `SP_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运营商ID',
  `TENANT_ID` bigint(20) NOT NULL COMMENT '分库标识',
  `BIZ_ID` bigint(20) NOT NULL COMMENT '分表标识',
  `SP_NAME` varchar(50) NOT NULL COMMENT '运营商名称',
  `REGISTER_DATE` bigint(20) DEFAULT NULL COMMENT '注册日期',
  `VERSION` bigint(20) DEFAULT '1' COMMENT '数据版本',
  `DEL_FLG` bit(1) DEFAULT b'0' COMMENT '删除标志',
  `ADD_ACTION` varchar(50) DEFAULT NULL COMMENT '插入请求',
  `ADD_ACCOUNT_ID` bigint(20) DEFAULT NULL COMMENT '插入账户',
  `ADD_TERM_IP` varchar(50) DEFAULT NULL COMMENT '插入终端IP',
  `ADD_DT` bigint(20) DEFAULT NULL COMMENT '插入日期',
  `UPD_ACTION` varchar(50) DEFAULT NULL COMMENT '修改请求',
  `UPD_ACCOUNT_ID` bigint(20) DEFAULT NULL COMMENT '修改账户',
  `UPD_TERM_IP` varchar(50) DEFAULT NULL COMMENT '修改终端IP',
  `UPD_DT` bigint(20) DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`SP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8 COMMENT='运营商';
