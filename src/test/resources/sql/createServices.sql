create table Services 
(
ID bigint generated always primary key, 
parentId bigint, 
Name varchar(255), 
Enabled bit
) 