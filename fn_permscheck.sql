create or replace NONEDITIONABLE FUNCTION PERMSCHECK(username IN VARCHAR2) 
    RETURN CHAR IS
        v_rows NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_rows
    FROM user_tab_privs
    WHERE privilege = 'EXECUTE' AND table_name = 'UTL_FILE' AND grantee = UPPER(username);
    
    IF (v_rows = 1) THEN
        RETURN 'Y';
    ELSE
        RETURN 'N';
    END IF;
END;