-- =============================
-- EMPLOYEES
-- =============================
insert into employee (name) values
    ('Sahione');
UPDATE employee set name = 'Laryssa' where name = 'Larissa';

-- =============================
-- HOUSES
-- =============================
insert into house (name, short_name, client_id, price_per_clean) values
    ('SP2', 'SP2', 4, 21.00),
    ('SP1', 'SP1', 4, 25.00),
    ('BB10', 'BB10', 4, 21.00);

UPDATE house set short_name = 'Duque 3' where name = 'Duque 3 Luxury Apartment Downtown Porto near Metro - Rua Duque de Loulé';
UPDATE house set short_name = 'Lovely Nest' where short_name = 'Lovely';
UPDATE house set short_name = 'Haven' where short_name = 'Wood';
UPDATE house set short_name = 'Alves Veiga' where short_name = 'AV';


