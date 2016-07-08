alter table category add column parent_category_id integer;
alter table category add constraint FK_f82dv7p1qn6jysxl4vadnb4mn foreign key (parent_category_id) references category (id);

UPDATE category set parent_category_id = (SELECT category_id FROM category_child_categories where category.id = child_categories_id);

DROP table category_child_categories;