DROP TABLE PDB_MEDIA;

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