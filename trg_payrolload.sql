create or replace NONEDITIONABLE TRIGGER trg_payrollload
    BEFORE INSERT ON payroll_load
    FOR EACH ROW
    
DECLARE
    v_payable NUMBER := 2050;
    v_payrollexpense NUMBER := 4045;
BEGIN
    INSERT INTO new_transactions
    VALUES (wkis_seq.NEXTVAL, :NEW.payroll_date, 'PAYROLL_LOAD Payroll Processed', v_payable, 'C', :NEW.amount);

    INSERT INTO new_transactions
    VALUES (wkis_seq.CURRVAL, :NEW.payroll_date, 'PAYROLL_LOAD Payroll Processed', v_payrollexpense, 'D', :NEW.amount);

    :NEW.status := 'G';

    EXCEPTION 
        WHEN OTHERS THEN
            :NEW.status := 'B';
END;