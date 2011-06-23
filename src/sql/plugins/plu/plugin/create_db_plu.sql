/*==============================================================*/
/* Nom de SGBD :  MySQL 4.0                                     */
/* Date de création :  16/06/2011 09:34:40                      */
/*==============================================================*/

drop table if exists historique;

drop table if exists etat_generation;

drop table if exists type_acte_juridique;

drop table if exists fichier;

drop table if exists dossier_version_atome;

drop table if exists version_atome;

drop table if exists atome;

drop table if exists dossier;

drop table if exists plu;

/*==============================================================*/
/* Table : atome                                                */
/*==============================================================*/
create table atome
(
   id_atome                       int(10) unsigned               not null,
   nom                            varchar(255)                   not null,
   titre                          varchar(255)                   not null,
   description                    text                           not null,
   primary key (id_atome)
)
comment = "table des atomes"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : dossier                                              */
/*==============================================================*/
create table dossier
(
   id_dossier                     int(10) unsigned               not null auto_increment,
   id_plu                         int(10) unsigned               not null,
   id_dossier_parent              int(10) unsigned,
   titre                          varchar(255)                   not null,
   description                    text                           not null,
   image                          mediumblob,
   html_specifique                mediumblob,
   primary key (id_dossier)
)
comment = "table de dossiers"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;


/*==============================================================*/
/* Table : dossier_version_atome                                */
/*==============================================================*/
create table dossier_version_atome
(
   id_dossier_version             int(10) unsigned               not null auto_increment,
   id_version                     int(10) unsigned               not null,
   id_dossier                     int(10) unsigned               not null,
   primary key (id_dossier_version)
)
comment = "table de lien entre version_atome et dossier"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : fichier                                              */
/*==============================================================*/
create table fichier
(
   id_atome                       int(10)                        not null,
   ordre_fichier                  int(10)                        not null,
   id_version_atome               int(10)                        not null,
   nom_fichier                    varchar(255)                   not null,
   titre_fichier                  varchar(255)                   not null,
   format                         varchar(30)                    not null,
   fichier                        longblob                       not null,
   taille                         int(10)                        not null,
   est_eps                        char(1)                        not null,
   primary key (id_atome, ordre_fichier, id_version_atome)
)
comment = "table des fichiers"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : historique                                           */
/*==============================================================*/
create table historique
(
   id_histo                       int(10) unsigned               not null auto_increment,
   id_plu                         int(10) unsigned               not null,
   id_dossier                     int(10) unsigned,
   id_atome                       int(10) unsigned,
   date_correction                date                           not null,
   description                    varchar(255)                   not null,
   primary key (id_histo)
)
comment = "historique des corrections"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu                                                  */
/*==============================================================*/
create table plu
(
   id_plu                         int(10) unsigned               not null auto_increment,
   id_type_acte_juridique         int(10) unsigned,
   nom_acte_juridique             varchar(255),
   ref_deliberation               varchar(255),
   dj                             date,
   da                             date,
   date_generation                date,
   id_etat_generation             int(10) unsigned,
   primary key (id_plu)
)
comment = "table des versions de plu"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : version_atome                                        */
/*==============================================================*/
create table version_atome
(
   id_version                     int(10) unsigned               not null auto_increment,
   id_atome                       int(10) unsigned               not null,
   num_version                    int(10) unsigned               not null,
   date_approbation               date,
   date_application               date,
   date_evolution                 date,
   date_archivage                 date,
   primary key (id_version)
)
comment = "table des versions d'atome"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : type_acte_juridique                                  */
/*==============================================================*/
create table type_acte_juridique
(
   id_type                        int(11) unsigned               not null auto_increment,
   type                           varchar(255)                   not null,
   primary key (id_type)
)
comment = "table de reference avec les types de l'acte juridique"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : etat_generation                                      */
/*==============================================================*/
create table etat_generation
(
   id_etat                        int(11) unsigned               not null auto_increment,
   etat                           varchar(255)                   not null,
   primary key (id_etat)
)
comment = "etats de la generation des iso et site"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;