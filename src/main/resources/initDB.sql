DROP TABLE IF EXISTS PDB_MEDIA;
DROP TABLE IF EXISTS PDB_SPATIAL;
DROP TABLE IF EXISTS PDB_COMPANY;
DROP TABLE IF EXISTS PDB_LOGGING_HISTORY;

CREATE TABLE PDB_MEDIA
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    NAME VARCHAR2(64),
    PHOTO ORDSYS.ORDImage,
    PHOTO_SI ORDSYS.SI_StillImage,
    PHOTO_AC ORDSYS.SI_AverageColor,
    PHOTO_CH ORDSYS.SI_ColorHistogram,
    PHOTO_PC ORDSYS.SI_PositionalColor,
    PHOTO_TX ORDSYS.SI_Texture
);

CREATE TABLE PDB_SPATIAL
(
     ID VARCHAR2(64) NOT NULL,
     OBJECT_TYPE VARCHAR2(16),
     CATEGORY nvarchar2(32),
     GEOMETRY SDO_GEOMETRY,
     NAME nvarchar2(32),
     ADMIN nvarchar2(32),
     OWNER nvarchar2(32),
     NOTE nvarchar2(512),
     VALID_FROM NUMBER(20) NOT NULL,
     VALID_TO NUMBER(20) NOT NULL,
     CREATED DATE,
     MODIFIED DATE,
     SPECIAL_FIELD INTEGER
);

CREATE TABLE PDB_COMPANY
(
    ID VARCHAR(64),
    NAME VARCHAR(100),
    VALID_FROM NUMBER(20),
    VALID_TO NUMBER(20)
);

CREATE TABLE PDB_LOGGING_HISTORY
(
     ID VARCHAR(64),
     COMPANY_ID VARCHAR(64),
     COMPANY_NAME VARCHAR(100),
     LOGGING_AREA VARCHAR(300),
     LOGS_PER_DAY INTEGER,
     VALID_FROM NUMBER(20),
     VALID_TO NUMBER(20)
);