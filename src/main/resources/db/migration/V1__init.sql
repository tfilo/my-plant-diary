-- pg_dumpall -O -x -s --inserts --no-comments -l plantdiary -U user   > dump.sql

CREATE TABLE pd_event (
    id bigint NOT NULL,
    date_time timestamp without time zone NOT NULL,
    description character varying(1000),
    plant_id bigint NOT NULL,
    event_type_id bigint NOT NULL
);

CREATE SEQUENCE pd_event_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_event_type (
    id bigint NOT NULL,
    code character varying(80) NOT NULL,
    schedulable boolean NOT NULL
);

CREATE SEQUENCE pd_event_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_location (
    id bigint NOT NULL,
    name character varying(80) NOT NULL,
    owner_username character varying(25) NOT NULL
);

CREATE SEQUENCE pd_location_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_photo (
    id bigint NOT NULL,
    data oid NOT NULL,
    thumbnail oid NOT NULL,
    description character varying(200),
    uploaded timestamp without time zone NOT NULL,
    plant_id bigint
);

CREATE SEQUENCE pd_photo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_plant (
    id bigint NOT NULL,
    description character varying(1000),
    name character varying(100) NOT NULL,
    location_id bigint,
    owner_username character varying(25) NOT NULL,
    plant_type_id bigint,
    deleted boolean NOT NULL DEFAULT false
);

CREATE SEQUENCE pd_plant_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_plant_type (
    id bigint NOT NULL,
    code character varying(80) NOT NULL
);

CREATE SEQUENCE pd_plant_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_schedule (
    id bigint NOT NULL,
    auto_update boolean NOT NULL,
    next timestamp without time zone NOT NULL,
    repeat_every integer,
    plant_id bigint NOT NULL,
    event_type_id bigint NOT NULL
);

CREATE SEQUENCE pd_schedule_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE pd_user (
    enabled boolean NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    password character varying(255) NOT NULL,
    username character varying(25) NOT NULL,
    email character varying(255) NOT NULL
);

ALTER TABLE ONLY pd_event
    ADD CONSTRAINT pd_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_event_type
    ADD CONSTRAINT pd_event_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_location
    ADD CONSTRAINT pd_location_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_photo
    ADD CONSTRAINT pd_photo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_plant
    ADD CONSTRAINT pd_plant_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_plant_type
    ADD CONSTRAINT pd_plant_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_schedule
    ADD CONSTRAINT pd_schedule_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pd_user
    ADD CONSTRAINT pd_user_pkey PRIMARY KEY (username);

ALTER TABLE ONLY pd_plant_type
    ADD CONSTRAINT uk_pd_plant_type_code UNIQUE (code);

ALTER TABLE ONLY pd_event_type
    ADD CONSTRAINT uk_pd_event_type_code UNIQUE (code);

ALTER TABLE ONLY pd_location
    ADD CONSTRAINT uk_pd_location_name UNIQUE (name);

ALTER TABLE ONLY pd_user
    ADD CONSTRAINT uk_pd_user_email UNIQUE (email);

ALTER TABLE ONLY pd_plant
    ADD CONSTRAINT fk_pd_plant_plant_type_id FOREIGN KEY (plant_type_id) REFERENCES pd_plant_type(id);

ALTER TABLE ONLY pd_schedule
    ADD CONSTRAINT fk_pd_schedule_plant_id FOREIGN KEY (plant_id) REFERENCES pd_plant(id);

ALTER TABLE ONLY pd_plant
    ADD CONSTRAINT fk_pd_plant_location_id FOREIGN KEY (location_id) REFERENCES pd_location(id);

ALTER TABLE ONLY pd_plant
    ADD CONSTRAINT fk_pd_plant_owner_username FOREIGN KEY (owner_username) REFERENCES pd_user(username);

ALTER TABLE ONLY pd_location
    ADD CONSTRAINT fk_pd_location_owner_username FOREIGN KEY (owner_username) REFERENCES pd_user(username);

ALTER TABLE ONLY pd_photo
    ADD CONSTRAINT fk_pd_photo_plant_id FOREIGN KEY (plant_id) REFERENCES pd_plant(id);

ALTER TABLE ONLY pd_event
    ADD CONSTRAINT fk_pd_event_event_type_id FOREIGN KEY (event_type_id) REFERENCES pd_event_type(id);

ALTER TABLE ONLY pd_event
    ADD CONSTRAINT fk_pd_event_plant_id FOREIGN KEY (plant_id) REFERENCES pd_plant(id);

ALTER TABLE ONLY pd_schedule
    ADD CONSTRAINT pd_schedule_event_type_id FOREIGN KEY (event_type_id) REFERENCES pd_event_type(id);

-- BASIC DATA
INSERT INTO pd_event_type VALUES (1, 'WATER', true); -- zaliatie
INSERT INTO pd_event_type VALUES (2, 'FERTILIZE', true); -- pohnojenie
INSERT INTO pd_event_type VALUES (3, 'REPOT', false); -- presadenie
INSERT INTO pd_event_type VALUES (4, 'TRIM', false); -- strihanie
INSERT INTO pd_event_type VALUES (5, 'GRAFT', false); -- štepenie
INSERT INTO pd_event_type VALUES (6, 'COLLECTION', false); -- zber plodov/vnate
INSERT INTO pd_event_type VALUES (7, 'SOW', false); -- vysievanie
INSERT INTO pd_event_type VALUES (8, 'CUTTING', false); -- sadenie z odrezkov
INSERT INTO pd_event_type VALUES (9, 'PURCHASE', false); -- zakúpenie
INSERT INTO pd_event_type VALUES (10, 'MIST', false); -- rosenie
INSERT INTO pd_event_type VALUES (11, 'OTHER', false); -- ostatné -- uviesť v poznámke

INSERT INTO pd_plant_type VALUEs (1, 'ORNAMENTAL'); -- dekoratívna rastlina
INSERT INTO pd_plant_type VALUEs (2, 'INDOOR'); -- izbová raslina
INSERT INTO pd_plant_type VALUEs (3, 'TROPICAL'); -- tropická rastlina
INSERT INTO pd_plant_type VALUEs (4, 'SUCCULENT'); -- sukulent
INSERT INTO pd_plant_type VALUEs (5, 'CACTUS'); -- kaktus
INSERT INTO pd_plant_type VALUEs (6, 'VEGETABLE'); -- zelenina
INSERT INTO pd_plant_type VALUEs (7, 'FRUIT'); -- ovocie
INSERT INTO pd_plant_type VALUEs (8, 'BUSH'); -- krík
INSERT INTO pd_plant_type VALUEs (9, 'CONIFER'); -- ihličnan
INSERT INTO pd_plant_type VALUEs (10, 'LEAFY'); -- listnatý strom
INSERT INTO pd_plant_type VALUEs (11, 'OTHER'); -- iný

SELECT pg_catalog.setval('public.pd_event_type_seq', 12, true);
SELECT pg_catalog.setval('public.pd_plant_type_seq', 12, true);
