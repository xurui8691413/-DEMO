-- database init
-- create database;
CREATE DATABASE secoundKill;
--use database
use secoundKill;
--create secound kill table
CREATE TABLE seckill(
  seckill_id number(18,0) NOT NULL enable PRIMARY KEY  ,
  name varchar(120) NOT NULL enable  ,
  seckill_no number(18,0) NOT NULL enable  ,
  start_time TIMESTAMP (6) NOT NULL        ,
  end_time TIMESTAMP(6) NOT NULL   ,
  create_time TIMESTAMP(6) NOT NULL    
  );
  
  CREATE  INDEX "idx_start_time" ON seckill (start_time) ;
  CREATE  INDEX "idx_end_time" ON seckill (end_time) ;
  CREATE  INDEX "idx_create_time" ON seckill(create_time) ;
  
  comment on column seckill.seckill_id is 'product id';
  comment on column seckill.name is 'product name';
  comment on column seckill.seckill_no is 'product number';
  comment on column seckill.start_time is 'seckill start time';
  comment on column seckill.end_time is 'seckill end time';
  comment on column seckill.create_time is 'create time';
  
   INSERT INTO seckill (seckill_id,name,seckill_no,start_time,end_time,create_time)
VALUES (1,'888 yuan secound kill',888,to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'),to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'),to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'));


   INSERT INTO seckill (seckill_id,name,seckill_no,start_time,end_time,create_time)
VALUES (2,'777 yuan secound kill',777,to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'),to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'),to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'));


   INSERT INTO seckill (seckill_id,name,seckill_no,start_time,end_time,create_time)
VALUES (3,'666 yuan secound kill',666,to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'),to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'),to_date('27-05-2017 00:00:00','dd-MM-yyyy hh24:mi:ss'));

--secound kill detail
--user login information
 CREATE TABLE success_kill(
  seckill_id number(18,0)  NOT NULL enable    ,
user_phone number(18,0)  NOT NULL enable  ,
status number(1,0)  DEFAULT -1 check(status=-1 or status=0 or status=1 or status=2)  NOT NULL   ,
create_time TIMESTAMP NOT NULL enable,
CONSTRAINT PK_success_kill PRIMARY KEY (seckill_id,user_phone)
  );

 CREATE UNIQUE INDEX "idx_create_time" ON success_kill (create_time) ; 
  comment on column success_kill.seckill_id is 'product id';
  comment on column success_kill.user_phone is 'user mobile phone';
  comment on column success_kill.status is 'status: -1:secound kill not success 0:secound kill success,1:paied,2:have bean sen';
  comment on column success_kill.create_time is 'create time';