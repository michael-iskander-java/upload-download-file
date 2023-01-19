


CREATE TABLE public.permission_group(
   id SERIAL PRIMARY KEY,
   group_name VARCHAR(20) UNIQUE NOT NULL
);


DROP TYPE IF EXISTS public.access_type;

CREATE TYPE public.access_type AS ENUM ('VIEW','EDIT');

CREATE TABLE public.permission(
   id SERIAL PRIMARY KEY,
   user_email VARCHAR(255) UNIQUE NOT NULL,
   permission_level access_type NOT NULL,
   group_id INT,
   CONSTRAINT fk_permission_group
      FOREIGN KEY(group_id) 
	  REFERENCES permission_group(id)
);


INSERT INTO public.permission_group (group_name) VALUES ('admin');

INSERT INTO public.permission_group (group_name) VALUES ('user');




INSERT INTO public.permission (user_email,permission_level,group_id) VALUES ('abc@google.com','VIEW', (select id from permission_group where group_name='admin') );
INSERT INTO public.permission (user_email,permission_level,group_id) VALUES ('efg@google.com','EDIT',(select id from permission_group where group_name='admin'));

INSERT INTO public.permission (user_email,permission_level,group_id) VALUES ('abc@yahoo.com','VIEW',(select id from permission_group where group_name='user') );
INSERT INTO public.permission (user_email,permission_level,group_id) VALUES ('efg@yahoo.com','VIEW',(select id from permission_group where group_name='user') );




DROP TYPE IF EXISTS public.item_type;

CREATE TYPE public.item_type AS ENUM ('Space','Folder','File');


CREATE TABLE public.item(
   id BIGSERIAL PRIMARY KEY,
   type item_type NOT NULL,
   name VARCHAR(256) NOT NULL,
   permission_group_id INT,
   item_id BIGINT,
   CONSTRAINT fk_permission_group FOREIGN KEY(permission_group_id) REFERENCES permission_group(id),
   CONSTRAINT fk_item
      FOREIGN KEY(item_id) 
	  REFERENCES item(id)
);


CREATE TABLE public.file(
   id BIGSERIAL PRIMARY KEY,
   binary_file bytea NOT NULL,
   item_id BIGINT,
   CONSTRAINT fk_item
      FOREIGN KEY(item_id) 
	  REFERENCES item(id)
);