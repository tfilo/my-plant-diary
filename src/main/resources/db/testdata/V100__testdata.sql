INSERT INTO pd_user (username, email, password, enabled) VALUES ('user', 'user@user.sk', '$2a$10$XioxKdEgEC43FJCf06zxgOZKi8F6RhdNPbyhV5FLeuhljdzApF//S', true);
INSERT INTO pd_user (username, email, password, enabled) VALUES ('user2', 'user2@user.sk', '$2a$10$XioxKdEgEC43FJCf06zxgOZKi8F6RhdNPbyhV5FLeuhljdzApF//S', false);
INSERT INTO pd_user (username, email, password, enabled) VALUES ('user3', 'user3@user.sk', '$2a$10$XioxKdEgEC43FJCf06zxgOZKi8F6RhdNPbyhV5FLeuhljdzApF//S', true);

INSERT INTO pd_location (id, name, owner_username) VALUES (1, 'Test location 1', 'user');

INSERT INTO pd_plant (id, name, owner_username, deleted) VALUES (1, 'Test plant 1', 'user', false);
INSERT INTO pd_plant (id, name, owner_username, location_id, deleted) VALUES (2, 'Test plant 2', 'user3', 1, false);
INSERT INTO pd_plant (id, name, owner_username, location_id, deleted) VALUES (3, 'Test plant 3', 'user3', 1, false);
INSERT INTO pd_plant (id, name, owner_username, location_id, deleted) VALUES (4, 'Test plant 4', 'user3', null, false);
INSERT INTO pd_plant (id, name, owner_username, location_id, deleted) VALUES (5, 'Test plant 5', 'user3', null, true);
INSERT INTO pd_plant (id, name, owner_username, location_id, deleted) VALUES (6, 'Test plant 6', 'user3', null, true);
INSERT INTO pd_plant (id, name, owner_username, location_id, deleted) VALUES (7, 'Test plant 7', 'user3', 1, true);

SELECT pg_catalog.setval('public.pd_plant_seq', 7, true);
SELECT pg_catalog.setval('public.pd_location_seq', 1, true);