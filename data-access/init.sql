CREATE DATABASE `stock` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */

CREATE TABLE `stock_history_kline` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `high` decimal(20,2) NOT NULL COMMENT '最高',
  `low` decimal(20,2) NOT NULL COMMENT '最低',
  `open` decimal(20,2) NOT NULL COMMENT '开盘价',
  `close` decimal(20,2) NOT NULL COMMENT '收盘价',
  `time_key` varchar(50) DEFAULT NULL COMMENT '时间格式：yyyy-MM-dd HH:mm:ss 港股和 A 股市场默认是北京时间',
  `pe_ratio` decimal(20,6) DEFAULT NULL COMMENT '市盈率',
  `turnover_rate` decimal(20,6) DEFAULT NULL COMMENT '换手率',
  `volume` bigint DEFAULT NULL COMMENT '成交量',
  `turnover` decimal(20,6) DEFAULT NULL COMMENT '成交额',
  `change_rate` decimal(20,6) DEFAULT NULL COMMENT '涨跌幅-振幅',
  `last_close` float DEFAULT NULL COMMENT '昨收价',
  `create_time` bigint NOT NULL,
  `timestamp` bigint NOT NULL COMMENT '当前时间time_key字段的时间戳，因为str数据排序会有问题，所以有个专门的时间戳进行排序型查询',
  `day_int_key` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code_time_key` (`code`,`time_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2447 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='历史K线';


create table stock_basic_info
(
    id            bigint      not null
        primary key,
    security_name varchar(50) null comment '证券名称',
    security_code varchar(50) null comment '证券代码',
    lot_size      int         null comment '一手数量',
    market_code   varchar(50) null comment '市场代码',
    is_delisting  int         null comment '是否已经下架',
    exch_type     int         null comment '交易所类型',
    listing_time  varchar(50) null comment '上市时间',
    sec_type      varchar(50) null comment '证券类型'
);

create index idx_market_security_code
    on stock_basic_info (market_code, security_code);

CREATE TABLE `stock_feature_store` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `security_code` varchar(45) NOT NULL COMMENT '股票代码',
  `strategy_id` varchar(45) DEFAULT NULL COMMENT '策略编码',
  `strategy_version` varchar(45) DEFAULT NULL COMMENT '策略版本号',
  `feature_json_store` text COMMENT '特征KV存储',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  `day_int_key` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_STOCK_STRATEGY_VERSION` (`security_code`,`strategy_id`,`strategy_version`,`day_int_key`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=2450 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='股票特征存储';

CREATE TABLE `stock_trade_execute` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `strategy_id` varchar(50) NOT NULL,
  `trade_action` int NOT NULL,
  `sec_amount` bigint DEFAULT NULL,
  `money_amount` bigint DEFAULT NULL,
  `market_type` varchar(45) NOT NULL,
  `sec_code` varchar(45) NOT NULL,
  `create_time` bigint NOT NULL,
  `flow_no` varchar(200) NOT NULL,
  `execute_status` int(10) unsigned zerofill NOT NULL,
  `price` decimal(20,2) DEFAULT NULL,
  `order_no` bigint DEFAULT NULL COMMENT '证券平台上的订单号，用来跟踪当前的挂单状态的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='记录当前等待执行的交易动作列表，以及交易当前的执行情况。';


CREATE TABLE `stock_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `strategy_id` varchar(45) NOT NULL,
  `money_hold` bigint NOT NULL,
  `sec_hold` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





