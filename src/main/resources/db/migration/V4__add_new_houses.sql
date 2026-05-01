-- =============================
-- HOUSES
-- =============================
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 01';
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 04';
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 11';
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 14';
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 15';
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 16';
UPDATE house set price_per_clean = 25 where short_name = 'Quarto 21';

insert into house (name, short_name, client_id, price_per_clean) values
    ('Casa S. Miguel 6 – Porto Old Town Center Unesco','Casa 6',1,80),
    ('Pharmacia','Pharmacia',1,32.20);


