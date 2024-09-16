-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) AS num_of_records FROM retail;

-- Number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) AS num_of_clients FROM retail;

-- Invoice date range (e.g. max/min dates)
SELECT  MIN(invoice_date) AS min, MAX(invoice_date) AS max FROM retail;
    
-- Number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) AS num_of_SKU FROM retail;
    
-- Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT AVG(invoice_amount) FROM (
    SELECT SUM(unit_price * quantity) AS invoice_amount
    FROM retail
    GROUP BY invoice_no
    HAVING SUM(unit_price * quantity) >= 0) AS avg_invoice;
    
-- Calculate total revenue (e.g. sum of unit_price * quantity)
SELECT SUM(unit_price * quantity) AS invoice_amount
FROM retail;
    
-- Calculate total revenue by YYYYMM
SELECT TO_CHAR(invoice_date, 'YYYYMM') AS year_month,
    SUM(unit_price * quantity) AS total_revenue
FROM retail
GROUP BY TO_CHAR(invoice_date, 'YYYYMM')
ORDER BY year_month;

-- Number of cancelled orders
SELECT COUNT(*) 
FROM retail
WHERE invoice_no LIKE 'C%'; 