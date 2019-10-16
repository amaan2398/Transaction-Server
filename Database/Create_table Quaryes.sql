create database bank;
use bank;
create table account
(
account_number int(20) not null,
balance int not null,
primary key(account_number),
unique key(account_number)
);

create table person
(
account_number int(20) not null,
name varchar(20) not null,
age int not null,
birthdate date not null,
foreign key(account_number) references account(account_number),
unique key(account_number)
);

create table userlogin
(
account_number int(20) not null,
pass varchar(255) not null,
foreign key(account_number) references account(account_number),
unique key(account_number)
);

insert into account values(101,10000);
insert into userlogin values(101,md5('aman123'));
insert into person values(101,'Amaan',0,'1999-1-1');

insert into account values(102,2000);
insert into userlogin values(102,md5('ram123'));
insert into person values(102,'Ramesh',0,'1999-2-2');

insert into account values(103,7000);
insert into userlogin values(103,md5('aji123'));
insert into person values(103,'Ajinkya',0,'1999-2-2');



select * from account;
select * from person;
select * from userlogin;

select * from account where account_number=102;
update account set balance=2000 where account_number=102;

