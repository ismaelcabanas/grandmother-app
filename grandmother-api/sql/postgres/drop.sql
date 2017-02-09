alter table transaction drop constraint FK6g20fcr3bhr6bihgy24rq1r1b;
alter table transaction drop constraint FK1u8obagvpekj00se05b1qsawk;
alter table transaction drop constraint FKreqv6agh66o9bn7a8hclww3jy;
drop table if exists account cascade;
drop table if exists payment_type cascade;
drop table if exists person cascade;
drop table if exists transaction cascade;