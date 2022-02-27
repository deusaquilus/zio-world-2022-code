
CREATE TABLE Robot(
    id INT,
    model VARCHAR(255),
    assemblyYear INT
);

CREATE TABLE KillerRobot(
    id INT,
    model VARCHAR(255),
    assemblyYear INT,
    series VARCHAR(255)
);

CREATE TABLE Yetti(
    id INT,
    uniqueGruntingSound VARCHAR(255),
    age INT
);

CREATE TABLE Houses(
    zip INT,
    owner INT,
    origin VARCHAR(255),
    hasChargingPort Boolean
);

CREATE TABLE PricingYears(
    startYear INT,
    endYear INT,
    pricing VARCHAR(255),
    insaneMembership VARCHAR(255),
    voltage INT
);

CREATE TABLE Human(
    id INT,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    age INT,
    membership VARCHAR(255),
    segment VARCHAR(255)
);

CREATE TABLE SuperHuman(
    id INT,
    heroName VARCHAR(255),
    age INT,
    side VARCHAR(255)
);


INSERT INTO public.houses (zip, owner, origin, haschargingport) VALUES (123, 1, 'US', true);
INSERT INTO public.houses (zip, owner, origin, haschargingport) VALUES (123, 2, 'UK', true);

INSERT INTO public.human (id, firstname, lastname, age, membership, segment) VALUES (1, 'Joe', 'Smith', 44, 'k', 's');
INSERT INTO public.human (id, firstname, lastname, age, membership, segment) VALUES (2, 'Jack', 'Rolland', 44, 'k', 's');

INSERT INTO public.killerrobot (id, model, assemblyyear, series) VALUES (1, 'T', 1990, '1000');
INSERT INTO public.killerrobot (id, model, assemblyyear, series) VALUES (2, 'T', 1980, '100');

INSERT INTO public.superhuman (id, heroname, age, side) VALUES (3, 'Superman', 55, 'g');

INSERT INTO public.robot (id, model, assemblyyear) VALUES (3, 'R2D2', 1970);

INSERT INTO public.yetti (id, uniquegruntingsound, age) VALUES (4, 'Glaarg', 845);

INSERT INTO public.pricingyears (startyear, endyear, pricing, insanemembership, voltage) VALUES (1900, 2030, 'sane', 'other', 120);

