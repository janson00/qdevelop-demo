-- 测试独立用户库

drop database if exists `demo_user_center`;
create database demo_user_center;
use demo_user_center;
drop table if exists `users`;
create table `users` (
  `uid` int(11) not null auto_increment,
  `user_name` varchar(100) not null,
  `mobile` char(11) not null,
  `age` tinyint(2) not null default '0',
  `ctime` datetime default current_timestamp,
  `status` tinyint(2) default '1',
  `utime` datetime default current_timestamp,
  primary key (`uid`)
) engine=innodb auto_increment=1 default charset=utf8;

-- 测试独立商品库

drop database if exists `demo_product_center`;
create database demo_product_center;
use demo_product_center;
drop table if exists `products`;
create table `products` (
  `pid` int(11) not null auto_increment,
  `product_name` varchar(100) not null,
  `uid` int(11) default 0,
  `price` decimal(10,2) not null,
  `store` int(11) default '0',
  `ctime` datetime default current_timestamp,
  `status` tinyint(2) default '1',
  `dg` bigint(20) default '0',
  primary key (`pid`)
) engine=innodb auto_increment=1 default charset=utf8;
drop table if exists `products_log`;
create table `products_log` (
  `plog_id` int(11) not null auto_increment,
  `uid` int(11) not null,
  `pid` int(11) not null,
  `product_name` varchar(100) not null,
  `action` varchar(100) not null,
  `ctime` datetime default current_timestamp,
  primary key (`plog_id`)
) engine=innodb auto_increment=1 default charset=utf8;


