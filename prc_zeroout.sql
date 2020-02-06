create or replace NONEDITIONABLE PROCEDURE PRC_ZEROOUT 
IS
    v_revenue VARCHAR2(2) := 'RE';
    v_expense VARCHAR2(2) := 'EX';
    v_oe_acct NUMBER := 5555;
    CURSOR c_acct IS
        SELECT * FROM ACCOUNT;
BEGIN
    FOR r_new IN c_acct LOOP
        IF (r_new.account_type_code = v_revenue) THEN
            INSERT INTO new_transactions 
            VALUES (wkis_seq.NEXTVAL, SYSDATE, 'PRC_ZEROOUT ' || r_new.account_name, r_new.account_no, 'D', r_new.account_balance);
            
            INSERT INTO new_transactions 
            VALUES (wkis_seq.NEXTVAL, SYSDATE, 'PRC_ZEROOUT ' || r_new.account_name, v_oe_acct, 'C', r_new.account_balance);
        ELSIF (r_new.account_type_code = v_expense) THEN
            INSERT INTO new_transactions 
            VALUES (wkis_seq.NEXTVAL, SYSDATE, 'PRC_ZEROOUT ' || r_new.account_name, r_new.account_no, 'C', r_new.account_balance);
            
            INSERT INTO new_transactions 
            VALUES (wkis_seq.NEXTVAL, SYSDATE, 'PRC_ZEROOUT ' || r_new.account_name, v_oe_acct, 'D', r_new.account_balance);
        END IF;
    END LOOP;
END PRC_ZEROOUT;