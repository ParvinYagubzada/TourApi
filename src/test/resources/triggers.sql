CREATE OR REPLACE FUNCTION populate_user_requests_function()
    RETURNS trigger
    LANGUAGE plpgsql
AS
'
DECLARE
    row record;
BEGIN
    IF (TG_OP = ''INSERT'') THEN
        FOR row IN SELECT username, agency_name FROM users
            LOOP
                INSERT INTO user_requests(agency_name, uuid, is_archived, status, customer_id)
                VALUES (row.agency_name, new.uuid, FALSE, 0, NULL);
            END LOOP;
    END IF;
    RETURN new;
END;
';

DROP TRIGGER IF EXISTS populate_user_requests_trigger ON requests;

CREATE TRIGGER populate_user_requests_trigger
    AFTER INSERT
    ON requests
    FOR EACH ROW
EXECUTE PROCEDURE populate_user_requests_function();

CREATE OR REPLACE FUNCTION update_all_user_requests()
    RETURNS trigger
    LANGUAGE plpgsql
AS
'
DECLARE
    row record;
BEGIN
    IF (TG_OP = ''UPDATE'') THEN
        IF (new.status = FALSE AND old.status = TRUE) THEN
            FOR row IN SELECT username, agency_name FROM users
                LOOP
                    UPDATE user_requests
                    SET status = 3, is_archived = true
                    WHERE uuid = old.uuid AND status = 0;
                END LOOP;
        END IF;
    END IF;
    RETURN new;
END';

DROP TRIGGER IF EXISTS update_all_user_trigger ON requests;

CREATE TRIGGER update_all_user_trigger
    AFTER UPDATE
    ON requests
    FOR EACH ROW
EXECUTE PROCEDURE update_all_user_requests();