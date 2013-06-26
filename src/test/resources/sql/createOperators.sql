create table Operators 
(
ID bigint generated always primary key, 
FIO varchar(255), 
FIOShort varchar(255), 
Password varchar(50), 
IsControler bit,
IsAdmin bit,
Enabled bit
) 