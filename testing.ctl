LOAD DATA
INFILE 'C:\Users\785444\OneDrive - Southern Alberta Institute of Technology\Year 2-elf8it221470\Database Prog\Assignments\payroll.txt' 
INSERT INTO TABLE payroll_load
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(payroll_date DATE "Month dd, yyyy",
 employee_id,
 amount,
 status)