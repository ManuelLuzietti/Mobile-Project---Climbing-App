-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.1              
-- * Generator date: Dec  4 2018              
-- * Generation date: Sun Sep 18 17:00:46 2022 
-- * LUN file: D:\Mobile-Project---Climbing-App\DBforServer\mobile db .lun 
-- * Schema: logical step /SQL 
-- ********************************************* 


-- Database Section
-- ________________ 

create database if not exists climbing_app_db ;
use climbing_app_db;


-- Tables Section
-- _____________ 

create table Boulder (
     id int not null,
     name varchar(30) not null,
     rating int not null,
     grade varchar(2) not null,
     date date not null,
     isOfficial char not null,
     img varchar(40) not null,
     constraint ID_Boulder_ID primary key (id));

create table Comment (
     id int not null,
     text varchar(300) not null,
     rating int not null,
     grade varchar(2) not null,
     user_id int not null,
     constraint ID_Comment_ID primary key (id));

create table CompletedBoulder (
     id int not null,
     id_comment int not null,
     date date not null,
     user_id int not null,
     boulder_id int not null,
     constraint ID_CompletedBoulder_ID primary key (id),
     constraint SID_Compl_Comme_ID unique (id_comment),
     constraint SID_CompletedBoulder_ID unique (user_id, boulder_id));

create table TracciaturaBoulder (
     id_tracciatura int not null,
     id_boulder int not null,
     id_tracciatore int not null,
     constraint ID_TracciaturaBoulder_ID primary key (id_tracciatura),
     constraint SID_Tracc_Bould_ID unique (id_boulder));

create table User (
     id int not null,
     username varchar(30) not null,
     first_name varchar(30) not null,
     last_name varchar(30) not null,
     constraint ID_User_ID primary key (id));


-- Constraints Section
-- ___________________ 

-- Not implemented
-- alter table Boulder add constraint ID_Boulder_CHK
--     check(exists(select * from TracciaturaBoulder
--                  where TracciaturaBoulder.id_boulder = id)); 

-- Not implemented
-- alter table Comment add constraint ID_Comment_CHK
--     check(exists(select * from CompletedBoulder
--                  where CompletedBoulder.id_comment = id)); 

alter table Comment add constraint REF_Comme_User_FK
     foreign key (user_id)
     references User (id);

alter table CompletedBoulder add constraint REF_Compl_User
     foreign key (user_id)
     references User (id);

alter table CompletedBoulder add constraint REF_Compl_Bould_FK
     foreign key (boulder_id)
     references Boulder (id);

alter table CompletedBoulder add constraint SID_Compl_Comme_FK
     foreign key (id_comment)
     references Comment (id);

alter table TracciaturaBoulder add constraint REF_Tracc_User_FK
     foreign key (id_tracciatore)
     references User (id);

alter table TracciaturaBoulder add constraint SID_Tracc_Bould_FK
     foreign key (id_boulder)
     references Boulder (id);


-- Index Section
-- _____________ 

create unique index ID_Boulder_IND
     on Boulder (id);

create unique index ID_Comment_IND
     on Comment (id);

create index REF_Comme_User_IND
     on Comment (user_id);

create unique index ID_CompletedBoulder_IND
     on CompletedBoulder (id);

create index REF_Compl_Bould_IND
     on CompletedBoulder (boulder_id);

create unique index SID_Compl_Comme_IND
     on CompletedBoulder (id_comment);

create unique index SID_CompletedBoulder_IND
     on CompletedBoulder (user_id, boulder_id);

create unique index ID_TracciaturaBoulder_IND
     on TracciaturaBoulder (id_tracciatura);

create index REF_Tracc_User_IND
     on TracciaturaBoulder (id_tracciatore);

create unique index SID_Tracc_Bould_IND
     on TracciaturaBoulder (id_boulder);

create unique index ID_User_IND
     on User (id);

