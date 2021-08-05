-- noinspection SqlResolveForFile

DROP TRIGGER IF EXISTS populate_user_requests ON requests;

DROP TRIGGER IF EXISTS  update_all_user_requests ON requests;

DROP FUNCTION IF EXISTS populate_user_requests();

DROP FUNCTION IF EXISTS update_all_user_requests();