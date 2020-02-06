CREATE OR REPLACE PROCEDURE proc_populate (inputpath IN VARCHAR2, filename IN VARCHAR2) AS
--    aliass VARCHAR2(32767) := UPPER(inputpath);
    f UTL_FILE.FILE_TYPE;
    CURSOR c_new IS 
        SELECT * FROM new_transactions;
BEGIN
    f := UTL_FILE.FOPEN(inputpath, filename, 'W', 2000);
    FOR r_new IN c_new LOOP
        UTL_FILE.PUT_LINE(f, r_new.transaction_no || ',' || 
                            r_new.transaction_date || ',' ||
                            r_new.description || ',' ||
                            r_new.account_no || ',' ||
                            r_new.transaction_type || ',' ||
                            r_new.transaction_amount);
    END LOOP;
    UTL_FILE.FCLOSE(f);
    
    EXCEPTION
        WHEN OTHERS THEN
            UTL_FILE.FCLOSE(f);
            RAISE;
            DBMS_OUTPUT.PUT_LINE(SQLCODE || SQLERRM);
END;