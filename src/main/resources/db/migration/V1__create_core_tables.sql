-- =============================
-- CLIENTS
-- =============================
create table client (
    id        serial primary key,
    name      varchar(200) not null unique,
    nif       varchar(20),
    address   varchar(500),
    email    varchar(200)
);

-- =============================
-- EMPLOYEES
-- =============================
create table employee (
    id   serial primary key,
    name varchar(200) not null unique
);

-- =============================
-- HOUSES
-- =============================
create table house (
    id              serial primary key,
    name            varchar(250) not null,
    short_name      varchar(80)  not null,
    client_id       int not null references client(id),
    price_per_clean numeric(10,2) not null
);

create index idx_house_client on house(client_id);

-- =============================
-- CLEANING SHIFT (one team on one day with many houses)
-- =============================
create table cleaning_shift (
    id            serial primary key,
    cleaning_date date not null,
    created_at    timestamptz not null default now()
);

create index idx_cleaning_shift_date on cleaning_shift(cleaning_date);

-- =============================
-- SHIFT TEAM MEMBERS
-- =============================
create table cleaning_shift_employee (
    cleaning_shift_id int not null references cleaning_shift(id) on delete cascade,
    employee_id       int not null references employee(id),
    primary key (cleaning_shift_id, employee_id)
);

create index idx_shift_employee_employee on cleaning_shift_employee(employee_id);

-- =============================
-- SHIFT HOUSES
-- We store cleaning_date here too to enforce "one house per day" with a simple unique index.
-- =============================
create table cleaning_shift_house (
    cleaning_shift_id int not null references cleaning_shift(id) on delete cascade,
    house_id          int not null references house(id),
    cleaning_date     date not null,
    primary key (cleaning_shift_id, house_id)
);

-- A house can only be assigned once per day (prevents duplicates)
create unique index uq_house_per_day
    on cleaning_shift_house (house_id, cleaning_date);

create index idx_shift_house_date on cleaning_shift_house(cleaning_date);
create index idx_shift_house_house on cleaning_shift_house(house_id);