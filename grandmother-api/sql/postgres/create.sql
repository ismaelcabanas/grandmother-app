create table account (
        id  bigserial not null,
        account_number varchar(255),
        balance numeric(19, 2),
        total numeric(19,2),
        primary key (id)
    );
create table payment_type (
        id  bigserial not null,
        name varchar(255) not null,
        primary key (id)
    );
create table person (
        id  bigserial not null,
        name varchar(255) not null,
        primary key (id)
    );
create table transaction (
        movement_type varchar(31) not null,
        id  bigserial not null,
        amount numeric(19, 2) not null,
        date_of_movement timestamp not null,
        description varchar(255) not null,
        account_id int8 not null,
        person_id int8,
        charge_type_id int8,
        primary key (id)
    );
alter table transaction
        add constraint FK6g20fcr3bhr6bihgy24rq1r1b
        foreign key (account_id)
        references account;
alter table transaction
        add constraint FK1u8obagvpekj00se05b1qsawk
        foreign key (person_id)
        references person;
alter table transaction
        add constraint FKreqv6agh66o9bn7a8hclww3jy
        foreign key (charge_type_id)
        references payment_type;
