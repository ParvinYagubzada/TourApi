CREATE OR REPLACE FUNCTION populate_user_requests_function()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$BODY$
DECLARE
    row record;
BEGIN
    IF (TG_OP = 'INSERT') THEN
        FOR row IN SELECT username, agency_name FROM users
            LOOP
                INSERT INTO user_requests(agency_name, uuid, is_archived, status, customer_id)
                VALUES (row.agency_name, new.uuid, FALSE, 0, NULL);
            END LOOP;
    END IF;
    RETURN new;
END;
$BODY$;

CREATE TRIGGER populate_user_requests_trigger
    AFTER INSERT
    ON requests
    FOR EACH ROW
EXECUTE PROCEDURE populate_user_requests_function();

CREATE OR REPLACE FUNCTION update_all_user_requests()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$BODY$
DECLARE
    row record;
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        IF (new.status = FALSE AND old.status = TRUE) THEN
            FOR row IN SELECT username, agency_name FROM users
                LOOP
                    UPDATE user_requests
                    SET status = 3
                    WHERE uuid = old.uuid;
                END LOOP;
        END IF;
    END IF;
    RETURN new;
END;
$BODY$;

CREATE TRIGGER update_all_user_trigger
    AFTER UPDATE
    ON requests
    FOR EACH ROW
EXECUTE PROCEDURE update_all_user_requests();