drop table role_customer if exists;
drop table instr_offer if exists;
drop table instrument if exists;
drop table offering if exists;
drop table role if exists;
drop table customer if exists;
drop table transaction if exists;

create table customer (
    id varchar(80) not null,
    name varchar(80) not null,
    family varchar(80) not null,
    email varchar(100) not null,
    password varchar(200) not null,
    balance bigint not null,
    primary key (id)
);

create table role (
    name varchar(80) not null,
    deposit boolean not null,  --can see deposit form ( form for submiting requests for changing balance)
    transaction boolean not null, -- form for buy and sell transaction
    confdeposit boolean not null, -- can see the form for deposit confirmation
    conftransact boolean not null,  -- can see the form for confirming enormous transaction
    addsymbol boolean not null, -- can see add symbol form ( used for symbol's owner)
    transactlimit boolean not null, -- can see place rule for transaction limitation ( for admin)
    confnewsymbol boolean not null, -- can see the form for new symbol confirmation (for admin)
    report boolean not null, -- for admin
    rolemanager boolean not null, -- form for assigning role
    userprofiles boolean not null,
    backup boolean not null,
    primary key (name)
);



create table role_customer (
    customer_id varchar(80) not null,
    role_name varchar(80) not null,
    primary key (customer_id,role_name),
    constraint custom_id_fk foreign key(customer_id) references customer(id) on delete cascade,
    constraint role_name_fk foreign key(role_name) references role(name) on delete cascade
);


create table instrument (
    customer_id varchar(80) not null,
    symbol varchar(100) not null,
    quantity bigint not null,
    for_sale boolean not null,
    primary key (customer_id,symbol),
    constraint customer_id_fk foreign key(customer_id) references customer(id) on delete cascade
);

create table offering(
    db_id bigint IDENTITY PRIMARY KEY,
    customer_id varchar(80) not null,
    price bigint not null,
    quantity bigint not null,
    type varchar(30) not null,
    kind integer not null,
    constraint custome_id_fk foreign key(customer_id) references customer(id) on delete cascade
);

create table instr_offer(
    instr_cust_id varchar(80) not null,
    instr_symbol varchar(100) not null,
    offer_id bigint not null,
    primary key (instr_cust_id, instr_symbol, offer_id ),
    constraint instr_fk foreign key(instr_cust_id, instr_symbol) references instrument(customer_id,symbol) on delete cascade,
    constraint offer_id_fk foreign key(offer_id) references offering(db_id) on delete cascade
);

create table transaction(
    tr_id bigint IDENTITY PRIMARY KEY,
    buyer varchar(80) not null,
    seller varchar(80) not null,
    instrument varchar(80) not null,
    typeOfTrade varchar(80) not null,
    quantity varchar(80) not null,
    price varchar(80) not null,
    time timestamp default now
);

insert into customer values ('1', 'admin','password','stock@bonjol.com','password',0);
insert into instrument values ('1', 'RANA', 200,true);
insert into offering (customer_id, price, quantity, type, kind) values ('1', 100, 20, 'GTC', 0);
insert into instr_offer values ('1', 'RANA', 0);
insert into transaction (buyer, seller, instrument, typeOfTrade, quantity, price) values ('11', '1', 'BENZ', 'GTC', '22', '200');

insert into role values ('admin',true,true,true,true,true,true,true,true,true,true,true);
insert into role values ('typical',true,true,false,false,false,false,false,false,false,false,false);
insert into role values ('officer',false,false,true,true,false,false,false,false,false,false,false);
insert into role values ('owner',false,false,false,false,true,false,false,false,false,false,false);

insert into role_customer values ('1', 'admin');

select * from instrument i where i.for_sale=ture and i.quantity>0
