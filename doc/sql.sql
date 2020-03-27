create schema zmall collate utf8mb4_bin;

create table t_category
(
	id bigint not null
		primary key,
	parent_id bigint default 0 not null comment '0代码顶级分类',
	name varchar(45) not null comment '品类名',
	level int not null comment '分类等级',
	priority int default 999 not null comment '优先级',
	detail varchar(245) null,
	createdBy bigint not null,
	createdDate bigint not null,
	modifiedBy bigint not null,
	modifiedDate bigint not null,
	status tinyint not null
)
comment '品类';

create table t_goods
(
	id bigint not null
		primary key,
	product_id bigint not null,
	name varchar(45) not null,
	code varchar(45) not null,
	price decimal(8,2) not null,
	stock varchar(45) not null,
	createdBy bigint not null,
	createdDate bigint not null,
	modifiedBy bigint not null,
	modifiedDate bigint not null,
	status tinyint not null
)
comment 'sku商品';

create table t_goods_value
(
	id bigint not null
		primary key,
	goods_id bigint not null,
	product_option_value_id bigint not null,
	createdBy bigint not null,
	createdDate bigint not null,
	modifiedBy bigint not null,
	modifiedDate bigint not null,
	status tinyint not null
);

create table t_option
(
	id bigint not null
		primary key,
	category_id varchar(45) not null comment '品类ID',
	name varchar(45) not null comment '规格名称',
	createdBy bigint not null,
	createdDate bigint not null,
	modifiedBy bigint not null,
	modifiedDate bigint not null,
	status tinyint not null
)
comment '规格';

create table t_product
(
	id bigint not null
		primary key,
	category_id bigint not null comment '品类ID',
	name varchar(45) not null,
	`desc` varchar(245) not null,
	createdBy bigint not null,
	createdDate bigint not null,
	modifiedBy bigint not null,
	modifiedDate bigint not null,
	status tinyint not null
)
comment 'spu产品';

create table t_product_option_value
(
	id bigint not null
		primary key,
	product_id bigint not null,
	option_id bigint not null,
	value varchar(45) not null,
	createdBy bigint not null,
	createdDate bigint not null,
	modifiedBy bigint not null,
	modifiedDate bigint not null,
	status tinyint not null
);

