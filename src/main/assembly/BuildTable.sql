create table blog_content
(
	cid bigint auto_increment
		primary key,
	title varchar(255) null comment '标题',
	slug varchar(255) null,
	created bigint null comment '创建人id',
	modified bigint null comment '最近修改人id',
	content text null comment '内容',
	type varchar(16) null comment '类型',
	tags varchar(200) null comment '标签',
	categories varchar(200) null comment '分类',
	hits int(5) null,
	comments_num int(5) default '0' null comment '评论数量',
	allow_comment int(1) default '0' null comment '开启评论',
	allow_ping int(1) default '0' null comment '允许ping',
	allow_feed int(1) default '0' null comment '允许反馈',
	status int(1) null comment '状态',
	author varchar(100) null comment '作者',
	gtm_create datetime null comment '创建时间',
	gtm_modified datetime null comment '修改时间'
)
comment '文章内容'
;

create table channel_info
(
	channel_id int null,
	channel_name varchar(255) null,
	create_time datetime null,
	update_time datetime null
)
;

create table dap_qmanager
(
	id int auto_increment
		primary key,
	qname varchar(128) not null comment '队列名',
	qaddr varchar(128) not null comment 'IP地址',
	ctime timestamp null comment '创建时间'
)
comment '分析平台的重启队列功能所用，存放 的是需要重启的队列信息。'
;

create table db_info
(
	db_id int null,
	db_name varchar(765) null,
	create_time datetime null,
	update_time datetime null,
	origin varchar(60) null,
	channel_id varchar(60) null
)
;

create table highland_finup_core_info
(
	core_lend_request_id varchar(100) null,
	lend_contract_code varchar(100) null,
	loan_time varchar(100) null,
	pay_time varchar(100) null,
	recharge_time varchar(100) null,
	service_fee varchar(100) null,
	done_service_fee varchar(100) null,
	request_system_code varchar(100) null,
	result varchar(100) null,
	etl_date varchar(100) null
)
;

create table lable_info
(
	lable_id int auto_increment
		primary key,
	lable_name varchar(765) null,
	lable_type_name varchar(765) null,
	lable_status int null,
	lable_sql text null,
	create_time datetime null,
	update_time datetime null,
	channel_id varchar(96) null,
	lable_remark varchar(384) null,
	is_mongo_sql int default '0' null,
	is_verify int(2) default '1' null,
	del_flag int(1) default '1' null
)
;

create table lable_info_201806062030
(
	lable_id int default '0' not null,
	lable_name varchar(765) null,
	lable_type_name varchar(765) null,
	lable_status int null,
	lable_sql text null,
	create_time datetime null,
	update_time datetime null,
	channel_id varchar(96) null,
	lable_remark varchar(384) null,
	is_mongo_sql int default '0' null,
	is_verify int(2) default '1' null,
	del_flag int(1) default '1' null
)
;

create table lable_info_copy
(
	lable_id int auto_increment
		primary key,
	lable_name varchar(765) null,
	lable_type_name varchar(765) null,
	lable_status int null,
	lable_sql text null,
	create_time datetime null,
	update_time datetime null,
	channel_id varchar(96) null,
	lable_remark varchar(384) null,
	is_mongo_sql int default '0' null
)
;

create table loan_refection_custmoer_level
(
	order_id bigint default '0' not null comment ' 工单id '
		primary key,
	identity_num varchar(20) null comment '身份证号码',
	gender_level varchar(10) null comment '性别等级',
	age_level varchar(10) null comment '年龄等级',
	customer_level varchar(10) null comment '客户等级',
	create_time varchar(20) null comment '数据日期',
	status int(2) default '0' null comment '处理状态 0.未处理 1.已处理'
)
comment '钱站拒贷客户等级表'
;

create table log_label_call
(
	id int auto_increment comment '自增主键'
		primary key,
	log_id varchar(38) null comment '日志ID，UUID',
	req_time bigint null comment '用户请求时间',
	st bigint null comment '接收到请求的时间',
	type tinyint null comment '标签类型',
	value varchar(255) null comment '标签类型对应的名称',
	ip varchar(128) null comment '请求来源',
	channel varchar(16) null comment '渠道号',
	service_name varchar(128) null comment '调用的服务类.方法名',
	operation varchar(32) null comment '服务描述(注解上的描述)',
	params_str text null comment '请求调用入参',
	gmt_create timestamp default CURRENT_TIMESTAMP null comment '数据记录时间',
	pad1 varchar(64) null comment '备用字段1',
	constraint logid_unique
		unique (log_id)
)
;

create index channel_index
	on log_label_call (channel)
;

create index date_index
	on log_label_call (gmt_create)
;

create table log_label_resp
(
	id int auto_increment comment '自增主键'
		primary key,
	log_id varchar(64) null comment '日志ID，UUID',
	et bigint null comment '计算完返回时间',
	total_ms int null comment '耗时(Millis)',
	result_str text null comment '返回值的字符串形式',
	ip varchar(128) null comment '服务器地址',
	status_code int null comment 'http响应状态码',
	gmt_create timestamp default CURRENT_TIMESTAMP null comment '数据记录时间',
	pad1 varchar(64) null comment '备用字段1',
	constraint logid_unique
		unique (log_id)
)
comment '标签调用返回值存放表。'
;

create index date_index
	on log_label_resp (gmt_create)
;

create table log_label_throws
(
	id int auto_increment comment '自增主键'
		primary key,
	log_id varchar(64) null comment '日志ID，UUID',
	et bigint null comment '计算完返回时间',
	total_ms int null comment '耗时',
	error_str varchar(10240) null comment '9000字的错误信息',
	ip varchar(128) null comment '服务器地址',
	status_code int null comment 'http响应状态码（该表中为-1）',
	gmt_create timestamp default CURRENT_TIMESTAMP null comment '数据记录时间',
	pad1 varchar(64) null comment '备用字段1',
	constraint logid_unique
		unique (log_id)
)
comment '标签调用 发生异常时（无论请求、响应）信息存入此表。'
;

create index date_index
	on log_label_throws (gmt_create)
;

create table merge_result
(
	database_name varchar(50) null,
	table_name varchar(50) null,
	insert_time timestamp default CURRENT_TIMESTAMP not null,
	from_date varchar(10) null,
	to_date varchar(10) null,
	remark varchar(100) null,
	create_success varchar(20) null,
	rename_success varchar(20) null,
	delete_success varchar(20) null,
	success varchar(20) null,
	ID int(10) auto_increment
		primary key
)
;

create table monitor_config_info
(
	channel_id varchar(32) null,
	group_id varchar(32) null
)
;

create table oa_notify
(
	id bigint auto_increment comment '编号'
		primary key,
	type char null comment '类型',
	title varchar(200) null comment '标题',
	content varchar(2000) null comment '内容',
	files varchar(2000) null comment '附件',
	status char null comment '状态',
	create_by bigint null comment '创建者',
	create_date datetime null comment '创建时间',
	update_by varchar(64) null comment '更新者',
	update_date datetime null comment '更新时间',
	remarks varchar(255) null comment '备注信息',
	del_flag char default '0' null comment '删除标记'
)
comment '通知通告'
;

create index oa_notify_del_flag
	on oa_notify (del_flag)
;

create table oa_notify_record
(
	id bigint auto_increment comment '编号'
		primary key,
	notify_id bigint null comment '通知通告ID',
	user_id bigint null comment '接受人',
	is_read tinyint(1) default '0' null comment '阅读标记',
	read_date date null comment '阅读时间'
)
comment '通知通告发送记录'
;

create index oa_notify_record_notify_id
	on oa_notify_record (notify_id)
;

create index oa_notify_record_read_flag
	on oa_notify_record (is_read)
;

create index oa_notify_record_user_id
	on oa_notify_record (user_id)
;

create table param_info
(
	channel_id varchar(32) null,
	lable_id int not null
		primary key,
	lable_name varchar(255) not null,
	lable_kv text not null,
	lable_param text not null,
	create_time datetime not null
)
;

create table qianzhan_channel_selfemployed_data_flow_result1
(
	create_time text null,
	update_time text null,
	id_no text null,
	mobile text null,
	refused_codes text null,
	current_channel text null,
	refuse_time text null,
	abandon_reason text null,
	abandon_describe text null,
	status text null,
	dt text null
)
;

create table request_result_test
(
	lable_id int not null
		primary key,
	request_content text not null,
	begin_time int not null,
	cast_time int not null
)
;

create table slow_request_log
(
	id int(20) auto_increment comment '主键'
		primary key,
	url varchar(100) null comment '请求访问地址',
	request_desc text null comment '请求参数',
	type varchar(100) null comment '请求',
	timeconsuming int null comment '查询消耗时间',
	create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
;

create table slow_sql_log
(
	id int(20) auto_increment comment '主键'
		primary key,
	lable_id int null comment '标签主键',
	slow_desc text null comment '带参数sql或者请求参数',
	type varchar(100) null comment '标识是sql 还是 请求',
	timeconsuming int null comment '查询消耗时间',
	create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
;

create table sys_dept
(
	dept_id bigint auto_increment
		primary key,
	parent_id bigint null comment '上级部门ID，一级部门为0',
	name varchar(50) null comment '部门名称',
	`desc` varchar(255) null,
	order_num int null comment '排序',
	del_flag tinyint default '0' null comment '是否删除  -1：已删除  0：正常'
)
comment '部门管理'
;

create table sys_dict
(
	id bigint(64) auto_increment comment '编号'
		primary key,
	name varchar(100) null comment '标签名',
	value varchar(100) null comment '数据值',
	type varchar(100) null comment '类型',
	description varchar(100) null comment '描述',
	sort decimal null comment '排序（升序）',
	parent_id bigint(64) default '0' null comment '父级编号',
	create_by int(64) null comment '创建者',
	create_date datetime null comment '创建时间',
	update_by bigint(64) null comment '更新者',
	update_date datetime null comment '更新时间',
	remarks varchar(255) null comment '备注信息',
	del_flag char default '0' null comment '删除标记'
)
comment '字典表'
;

create index sys_dict_del_flag
	on sys_dict (del_flag)
;

create index sys_dict_label
	on sys_dict (name)
;

create index sys_dict_value
	on sys_dict (value)
;

create table sys_file
(
	id bigint auto_increment
		primary key,
	type int null comment '文件类型',
	url varchar(200) null comment 'URL地址',
	create_date datetime null comment '创建时间'
)
comment '文件上传'
;

create table sys_log
(
	id bigint auto_increment
		primary key,
	user_id bigint null comment '用户id',
	username varchar(50) null comment '用户名',
	operation varchar(50) null comment '用户操作',
	time int null comment '响应时间',
	method varchar(200) null comment '请求方法',
	params varchar(5000) null comment '请求参数',
	ip varchar(64) null comment 'IP地址',
	gmt_create datetime null comment '创建时间'
)
comment '系统日志'
;

create table sys_menu
(
	menu_id bigint auto_increment
		primary key,
	parent_id bigint null comment '父菜单ID，一级菜单为0',
	name varchar(50) null comment '菜单名称',
	url varchar(200) null comment '菜单URL',
	perms varchar(500) null comment '授权(多个用逗号分隔，如：user:list,user:create)',
	type int null comment '类型   0：目录   1：菜单   2：按钮',
	icon varchar(50) null comment '菜单图标',
	order_num int null comment '排序',
	gmt_create datetime null comment '创建时间',
	gmt_modified datetime null comment '修改时间'
)
comment '菜单管理'
;

create table sys_menu_copy
(
	menu_id bigint auto_increment
		primary key,
	parent_id bigint null comment '父菜单ID，一级菜单为0',
	name varchar(50) null comment '菜单名称',
	url varchar(200) null comment '菜单URL',
	perms varchar(500) null comment '授权(多个用逗号分隔，如：user:list,user:create)',
	type int null comment '类型   0：目录   1：菜单   2：按钮',
	icon varchar(50) null comment '菜单图标',
	order_num int null comment '排序',
	gmt_create datetime null comment '创建时间',
	gmt_modified datetime null comment '修改时间'
)
comment '菜单管理'
;

create table sys_role
(
	role_id bigint auto_increment
		primary key,
	role_name varchar(100) null comment '角色名称',
	role_sign varchar(100) null comment '角色标识',
	remark varchar(100) null comment '备注',
	user_id_create bigint(255) null comment '创建用户id',
	gmt_create datetime null comment '创建时间',
	gmt_modified datetime null comment '创建时间'
)
comment '角色'
;

create table sys_role_menu
(
	id bigint auto_increment
		primary key,
	role_id bigint null comment '角色ID',
	menu_id bigint null comment '菜单ID'
)
comment '角色与菜单对应关系'
;

create table sys_task
(
	id bigint auto_increment
		primary key,
	cron_expression varchar(255) null comment 'cron表达式',
	method_name varchar(255) null comment '任务调用的方法名',
	is_concurrent varchar(255) null comment '任务是否有状态',
	description varchar(255) null comment '任务描述',
	update_by varchar(64) null comment '更新者',
	bean_class varchar(255) null comment '任务执行时调用哪个类的方法 包名+类名',
	create_date datetime null comment '创建时间',
	job_status varchar(255) null comment '任务状态',
	job_group varchar(255) null comment '任务分组',
	update_date datetime null comment '更新时间',
	create_by varchar(64) null comment '创建者',
	spring_bean varchar(255) null comment 'Spring bean',
	job_name varchar(255) null comment '任务名'
)
;

create table sys_user
(
	user_id bigint auto_increment
		primary key,
	username varchar(50) null comment '用户名',
	name varchar(100) null,
	password varchar(50) null comment '密码',
	dept_id int(20) null,
	email varchar(100) null comment '邮箱',
	`desc` varchar(255) null comment '描述',
	mobile varchar(100) null comment '手机号',
	status tinyint(255) null comment '状态 0:禁用，1:正常',
	user_id_create bigint(255) null comment '创建用户id',
	gmt_create datetime null comment '创建时间',
	gmt_modified datetime null comment '修改时间'
)
;

create table sys_user_role
(
	id bigint auto_increment
		primary key,
	user_id bigint null comment '用户ID',
	role_id bigint null comment '角色ID'
)
comment '用户与角色对应关系'
;

create table table_column_info
(
	table_id int null comment '表ID',
	`index` int null comment '列ID',
	colType varchar(255) null comment '列类型',
	originType varchar(255) null comment '列值原始类型',
	time timestamp default CURRENT_TIMESTAMP null comment '日期',
	optionFlag varchar(255) default '' null comment '操作类型',
	name varchar(255) null comment '列名',
	column_id int null,
	`desc` varchar(255) null comment '描述'
)
;

create table table_column_info_20180619
(
	table_id int null comment '表ID',
	`index` int null comment '列ID',
	colType varchar(255) null comment '列类型',
	originType varchar(255) null comment '列值原始类型',
	time timestamp default CURRENT_TIMESTAMP null comment '日期',
	optionFlag varchar(255) default '' null comment '操作类型',
	name varchar(255) null comment '列名',
	column_id int null,
	`desc` varchar(255) null comment '描述'
)
;

create table table_column_mysql
(
	id int auto_increment
		primary key,
	db_name varchar(255) null,
	table_name varchar(255) null,
	column_name varchar(255) null,
	data_type varchar(255) null,
	column_comment varchar(255) null,
	column_key varchar(255) null
)
;

create table table_distinct_info
(
	id int auto_increment comment '主键'
		primary key,
	db_name varchar(765) null,
	table_name varchar(765) null,
	distinct_key varchar(1765) null,
	sort_key varchar(765) null,
	channel_id varchar(200) null
)
;

create table table_info
(
	table_id int null,
	table_hbase_name varchar(765) null,
	create_time datetime null,
	update_time datetime null,
	db_id int null,
	table_mongo_name varchar(765) null
)
;

create table table_info_20180619
(
	table_id int null,
	table_hbase_name varchar(765) null,
	create_time datetime null,
	update_time datetime null,
	db_id int null,
	table_mongo_name varchar(765) null
)
;

create table user_finance_weight
(
	mobile text null comment '手机号',
	aqj_mobile text null comment '爱钱进手机号',
	aqj_create_time_dt text null comment '爱钱进创建时间',
	aqj_id_no text null comment '爱钱进身份证',
	aqj_age text null comment '爱钱进年龄',
	aqj_sex text null comment '爱钱进性别',
	aqj_amount_z text null comment '爱钱进贷款额',
	aqj_signed_amount_z text null comment '爱钱进合同额',
	aqj_telcity text null comment '爱钱进手机归属地',
	aqj_idnocity text null comment '爱钱进身份证归属地',
	benew_mobile text null comment '会牛手机号',
	benew_create_time_dt text null comment '会牛创建时间',
	benew_id_no text null comment '会牛身份证',
	benew_age text null comment '会牛年龄',
	benew_sex text null comment '会牛性别',
	benew_amount_z text null comment '会牛贷款额',
	benew_signed_amount_z text null comment '会牛合同额',
	benew_telcity text null comment '会牛手机归属地',
	benew_idnocity text null comment '会牛身份证归属地',
	car_mobile text null comment '车贷手机号',
	car_create_time_dt text null comment '车贷创建时间',
	car_id_no text null comment '车贷身份证',
	car_age text null comment '车贷年龄',
	car_sex text null comment '车贷性别',
	car_amount_z text null comment '车贷贷款额',
	car_signed_amount_z text null comment '车贷合同额',
	car_telcity text null comment '车贷手机归属地',
	car_idnocity text null comment '车贷身份证归属地',
	bestbuy_mobile text null comment '任买手机号',
	bestbuy_create_time_dt text null comment '任买创建时间',
	bestbuy_id_no text null comment '任买身份证',
	bestbuy_age text null comment '任买年龄',
	bestbuy_sex text null comment '任买性别',
	bestbuy_amount_z text null comment '任买贷款额',
	bestbuy_signed_amount_z text null comment '任买合同额',
	bestbuy_telcity text null comment '任买手机归属地',
	bestbuy_idnocity text null comment '任买身份证归属地',
	finup_mobile text null comment '凡普信贷手机号',
	finup_create_time_dt text null comment '凡普信贷创建时间',
	finup_id_no text null comment '凡普信贷身份证',
	finup_age text null comment '凡普信贷年龄',
	finup_sex text null comment '凡普信贷性别',
	finup_amount_z text null comment '凡普信贷贷款额',
	finup_signed_amount_z text null comment '凡普信贷合同额',
	finup_telcity text null comment '凡普信贷手机归属地',
	finup_idnocity text null comment '凡普信贷身份证归属地',
	jiea_mobile text null comment '钱站自营手机号',
	jiea_create_time_dt text null comment '钱站自营创建时间',
	jiea_id_no text null comment '钱站自营身份证',
	jiea_age text null comment '钱站自营年龄',
	jiea_sex text null comment '钱站自营性别',
	jiea_amount_z text null comment '钱站自营贷款额',
	jiea_signed_amount_z text null comment '钱站自营合同额',
	jiea_telcity text null comment '钱站自营手机归属地',
	jiea_idnocity text null comment '钱站自营身份证归属地',
	jieas_mobile text null comment '钱站渠道手机号',
	jieas_create_time_dt text null comment '钱站渠道创建时间',
	jieas_id_no text null comment '钱站渠道身份证',
	jieas_age text null comment '钱站渠道年龄',
	jieas_sex text null comment '钱站渠道性别',
	jieas_amount_z text null comment '钱站渠道贷款额',
	jieas_signed_amount_z text null comment '钱站渠道合同额',
	jieas_telcity text null comment '钱站渠道手机归属地',
	jieas_idnocity text null comment '钱站渠道身份证归属地',
	kaniu_mobile text null comment '卡牛手机号',
	kaniu_create_time_dt text null comment '卡牛创建时间',
	kaniu_id_no text null comment '卡牛身份证',
	kaniu_age text null comment '卡牛年龄',
	kaniu_sex text null comment '卡牛性别',
	kaniu_amount_z text null comment '卡牛贷款额',
	kaniu_signed_amount_z text null comment '卡牛合同额',
	kaniu_telcity text null comment '卡牛手机归属地',
	kaniu_idnocity text null comment '卡牛身份证归属地',
	ldyz_mobile text null comment '来点银子手机号',
	ldyz_create_time_dt text null comment '来点银子创建时间',
	ldyz_id_no text null comment '来点银子身份证',
	ldyz_age text null comment '来点银子年龄',
	ldyz_sex text null comment '来点银子性别',
	ldyz_amount_z text null comment '来点银子贷款额',
	ldyz_signed_amount_z text null comment '来点银子合同额',
	ldyz_telcity text null comment '来点银子手机归属地',
	ldyz_idnocity text null comment '来点银子身份证归属地',
	nirvana_mobile text null comment '涅槃手机号',
	nirvana_create_time_dt text null comment '涅槃创建时间',
	nirvana_id_no text null comment '涅槃身份证',
	nirvana_age text null comment '涅槃年龄',
	nirvana_sex text null comment '涅槃性别',
	nirvana_amount_z text null comment '涅槃贷款额',
	nirvana_signed_amount_z text null comment '涅槃合同额',
	nirvana_telcity text null comment '涅槃手机归属地',
	nirvana_idnocity text null comment '涅槃身份证归属地',
	payday_mobile text null comment '月光侠手机号',
	payday_create_time_dt text null comment '月光侠创建时间',
	payday_id_no text null comment '月光侠身份证',
	payday_age text null comment '月光侠年龄',
	payday_sex text null comment '月光侠性别',
	payday_amount_z text null comment '月光侠贷款额',
	payday_signed_amount_z text null comment '月光侠合同额',
	payday_telcity text null comment '月光侠手机归属地',
	payday_idnocity text null comment '月光侠身份证归属地',
	qc_mobile text null comment '秋成手机号',
	qc_create_time_dt text null comment '秋成创建时间',
	qc_id_no text null comment '秋成身份证',
	qc_age text null comment '秋成年龄',
	qc_sex text null comment '秋成性别',
	qc_amount_z text null comment '秋成贷款额',
	qc_signed_amount_z text null comment '秋成合同额',
	qc_telcity text null comment '秋成手机归属地',
	qc_idnocity text null comment '秋成身份证归属地',
	wacai_mobile text null comment '挖财手机号',
	wacai_create_time_dt text null comment '挖财创建时间',
	wacai_id_no text null comment '挖财身份证',
	wacai_age text null comment '挖财年龄',
	wacai_sex text null comment '挖财性别',
	wacai_amount_z text null comment '挖财贷款额',
	wacai_signed_amount_z text null comment '挖财合同额',
	wacai_telcity text null comment '挖财手机归属地',
	wacai_idnocity text null comment '挖财身份证归属地'
)
;

create table ut_users
(
	id int(10) auto_increment
		primary key,
	uname varchar(32) not null,
	upass varchar(32) not null,
	tname varchar(32) null,
	empno varchar(10) null,
	deptno varchar(10) null,
	ct timestamp default CURRENT_TIMESTAMP not null,
	channel_id varchar(255) null
)
;

create table xiaoai_index_mobile
(
	mobile varchar(255) default '' not null,
	id_no varchar(128) null,
	lendrecord020 varchar(255) null,
	lendrecord021 varchar(255) null,
	etl_date varchar(32) not null,
	primary key (mobile, etl_date)
)
;

create index xiaoai_m_idno
	on xiaoai_index_mobile (id_no)
;

create table xiaoai_index_result
(
	id_no varchar(255) default '' not null,
	lendrecord001 text null,
	lendrecord002 text null,
	lendrecord003 text null,
	lendrecord004 text null,
	lendrecord005 text null,
	lendrecord006 text null,
	lendrecord007 text null,
	lendrecord008 text null,
	lendrecord009 text null,
	lendrecord010 text null,
	lendrecord011 text null,
	lendrecord012 text null,
	lendrecord013 text null,
	lendrecord014 text null,
	lendrecord015 text null,
	lendrecord016 text null,
	lendrecord017 text null,
	lendrecord018 text null,
	lendrecord019 text null,
	lendrecord020 varchar(255) null,
	lendrecord021 varchar(255) null,
	lendrecord022 text null,
	etl_date varchar(32) default '' not null,
	primary key (id_no, etl_date)
)
;

create table rely_info
(
	rely_id int auto_increment comment '标识列'
		primary key,
	rely_name varchar(255) not null comment '''依赖计算名''',
	tag_id int not null comment '''标签id''',
	rely_result varchar(30) not null comment '''标签结果类型''',
	rely_in varchar(255) null comment '''入参替换关系''',
	rely_judge varchar(255) null comment '''入参判断规则,动态脚本''',
	rely_parent varchar(10) not null comment '''父依赖''',
	rely_child varchar(10) not null comment '''子依赖''',
	operation_user varchar(50) null comment '''操作用户''',
	create_time date null comment '''操作时间''',
	update_time date null comment '''修改时间''',
	comment varchar(2000) null comment '''备注''',
	constraint rely_info_rely_id_uindex
		unique (rely_id)
)
comment '复杂标签依赖关系表'
;