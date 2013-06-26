create table Receptions 
(
ID bigint generated always primary key, 
ServiceId bigint, 
ApplicatorId bigint, 
OperatorId bigint, 
OpenDate date
) 