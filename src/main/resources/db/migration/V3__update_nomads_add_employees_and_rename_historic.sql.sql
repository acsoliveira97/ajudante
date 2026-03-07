-- =============================
-- UPDATE NOMADS CLIENT
-- =============================
UPDATE client SET name = 'Nomads Apartamentos Turísticos, Sociedade Unipessoal LDA' WHERE id = 5;
UPDATE client SET nif = '514760826' WHERE id = 5;
UPDATE client SET email = 'finance@nomadcityflats.com' where id = 5;
UPDATE client SET address = 'Avenida da Boavista 1203, sala 108, 4100-130 Porto' WHERE id = 5;

-- =============================
-- ADD NEW EMPLOYEES
-- =============================
INSERT INTO employee (name) values
    ('Elisabete'),
    ('Larissa');

UPDATE employee SET name = 'Andreza' WHERE name = 'Andresa';

-- =============================
-- ADD CATIVO
-- =============================
UPDATE house SET short_name = 'Cativo' WHERE short_name = 'Historic';
UPDATE house SET short_name = 'Capela' WHERE short_name = 'Chapel';