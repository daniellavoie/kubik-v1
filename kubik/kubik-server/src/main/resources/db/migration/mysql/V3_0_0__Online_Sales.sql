create table invoice_confirmation (id bigint not null auto_increment, created_date datetime, error text, processed_date datetime, status VARCHAR(255), invoice_id integer, primary key (id));
alter table invoice_confirmation add constraint FKahp2413d6pm4ijhpg7xp999y7 foreign key (invoice_id) references invoice (id);

create table product_property (name varchar(255) not null, type varchar(255), value varchar(255), product_id integer not null, primary key (name, product_id));
alter table product_property add constraint FKcv0vb20d624kn1oe2h25rrxry foreign key (product_id) references product (id);

alter table error MODIFY exception text;
alter table error MODIFY message text;
alter table kos_notification MODIFY error text;