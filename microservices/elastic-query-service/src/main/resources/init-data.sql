CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO public.users(id, username, firstname, lastname)
VALUES ('c94ad600-6721-4068-b807-e4d432d96530', 'app_user', 'Standard', 'User');

INSERT INTO public.users(id, username, firstname, lastname)
VALUES ('39b14daf-7192-4320-a57b-fb7b497867c1', 'app_admin', 'Admin', 'User');

INSERT INTO public.users(id, username, firstname, lastname)
VALUES ('ab3c418a-e317-4465-82ea-4c8532d1ed31', 'app_super_user', 'Super', 'User');

INSERT INTO documents(id, document_id)
VALUES ('c1df7d01-4bd7-40b6-8d6a-7e2ffabf37f7', 2087562205);

INSERT INTO documents(id, document_id)
VALUES ('90573d25-9ad9-409e-bbb6-b94180799138', 1243106234);

INSERT INTO documents(id, document_id)
VALUES ('90573d25-9ad9-409e-bbb6-b94180799139', 2056872673);

INSERT INTO user_permissions(user_permission_id, user_id, document_id, permission_type)
VALUES (uuid_generate_v4(), 'c94ad600-6721-4068-b807-e4d432d96530', 'c1df7d01-4bd7-40b6-8d6a-7e2ffabf37f7', 'READ');

INSERT INTO user_permissions(user_permission_id, user_id, document_id, permission_type)
VALUES (uuid_generate_v4(), 'c94ad600-6721-4068-b807-e4d432d96530', '90573d25-9ad9-409e-bbb6-b94180799138', 'READ');

INSERT INTO user_permissions(user_permission_id, user_id, document_id, permission_type)
VALUES (uuid_generate_v4(), '39b14daf-7192-4320-a57b-fb7b497867c1', '90573d25-9ad9-409e-bbb6-b94180799139', 'READ');

INSERT INTO user_permissions(user_permission_id, user_id, document_id, permission_type)
VALUES (uuid_generate_v4(), 'ab3c418a-e317-4465-82ea-4c8532d1ed31', 'c1df7d01-4bd7-40b6-8d6a-7e2ffabf37f7', 'READ');