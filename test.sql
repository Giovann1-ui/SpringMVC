drop Table user;
drop Table adresse;

create table adresse (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postal_code BIGINT NOT NULL
);

create table IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    adresse_id BIGINT,
    FOREIGN KEY (adresse_id) REFERENCES adresse(id)
);

create table if NOT EXISTS fichier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    extension VARCHAR(20),
    contenu LONGBLOB NOT NULL
);
