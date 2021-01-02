INSERT INTO pd_user (username, email, password, enabled) VALUES ('user', 'user@user.sk', '$2a$10$XioxKdEgEC43FJCf06zxgOZKi8F6RhdNPbyhV5FLeuhljdzApF//S', true);
INSERT INTO pd_user (username, email, password, enabled) VALUES ('user2', 'user2@user.sk', '$2a$10$XioxKdEgEC43FJCf06zxgOZKi8F6RhdNPbyhV5FLeuhljdzApF//S', false);
INSERT INTO pd_user (username, email, password, enabled) VALUES ('user3', 'user3@user.sk', '$2a$10$XioxKdEgEC43FJCf06zxgOZKi8F6RhdNPbyhV5FLeuhljdzApF//S', true);

INSERT INTO pd_plant (id, name, owner_username, deleted) VALUES (1, 'Test plant 1', 'user', false);

SELECT pg_catalog.setval('public.pd_plant_seq', 2, true);