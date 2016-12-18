drop table product_image;

ALTER TABLE customer_order_detail DROP FOREIGN KEY FK_ovwqssv6vtsyuehx68g0uelkl;
ALTER TABLE customer_order_detail DROP COLUMN product_id, DROP INDEX FK_ovwqssv6vtsyuehx68g0uelkl;

drop table product;

create table product (ean13 varchar(255) not null, brand varchar(255), name varchar(255), price double precision not null, weight integer, available bit not null, category_id integer, primary key (ean13));

alter table customer_order_detail add column product_ean13 varchar(255) not null;
alter table customer_order_detail add constraint FK_ovwqssv6vtsyuehx68g0uelkl foreign key (product_ean13) references product (ean13);