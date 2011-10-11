/*==============================================================*/
/* Nom de SGBD :  MySQL 4.0                                     */
/* Date de création :  29/08/2011 14:10:00                      */
/*==============================================================*/

drop table if exists plu_iso;

drop table if exists plu_historique;

drop table if exists plu_etat_generation;

drop table if exists plu_type_acte_juridique;

drop table if exists plu_decision;

drop table if exists plu_fichier_contenu;

drop table if exists plu_fichier;

drop table if exists plu_dossier_version_atome;

drop table if exists plu_version_atome;

drop table if exists plu_atome;

drop table if exists plu_dossier;

drop table if exists plu_sequences;

drop table if exists plu_plu;

/*==============================================================*/
/* Table : plu_sequences										*/
/*==============================================================*/
create table plu_sequences
(
  sequence_name		 			varchar(255)		default null,
  next_val						int(11) 			default null
)
comment = "table de séquence"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_atome                                            */
/*==============================================================*/
create table plu_atome
(
   id_atome                       int(11) unsigned               not null,
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
/* Table : plu_dossier                                           */
/*==============================================================*/
create table plu_dossier
(
   id_dossier                     int(11) unsigned               not null auto_increment,
   id_plu                         int(11) unsigned               not null,
   id_dossier_parent              int(11) unsigned,
   titre                          varchar(255)                   not null,
   description                    text                           not null,
   image                          mediumblob,
   nom_image					  varchar(255),
   html_specifique_c              mediumblob,
   html_specifique_i              mediumblob,
   primary key (id_dossier)
)
comment = "table de dossiers"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;


/*==============================================================*/
/* Table : plu_dossier_version_atome                            */
/*==============================================================*/
create table plu_dossier_version_atome
(
   id_dossier_version             int(11) unsigned               not null auto_increment,
   id_version                     int(11) unsigned               not null,
   id_dossier                     int(11) unsigned               not null,
   primary key (id_dossier_version)
)
comment = "table de lien entre version_atome et dossier"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_fichier                                          */
/*==============================================================*/
create table plu_fichier
(
   id_fichier                     int(11)                        not null auto_increment,
   id_fichier_contenu			  int(11) 						 not null default '0',
   id_atome                       int(11)                        not null default '0',
   ordre_fichier                  int(11)                        not null default '0',
   id_version_atome               int(11)                        not null default '0',
   nom_fichier                    varchar(255)                   not null,
   titre_fichier                  varchar(255)                   not null,
   format                         varchar(30)                    not null,
   taille                         int(11)                        not null default '0',
   utilisation                    char(1)                        not null default '',
   primary key (id_fichier)
)
comment = "table des fichiers"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_fichier_contenu                                  */
/*==============================================================*/
create table plu_fichier_contenu
(
   id_fichier_contenu             int(11)                        not null auto_increment,
   fichier                        longblob                       not null,
   primary key (id_fichier_contenu)
)
comment = "table contenu des fichiers"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;


/*==============================================================*/
/* Table : plu_historique                                       */
/*==============================================================*/
create table plu_historique
(
   id_histo                       int(11) unsigned               not null auto_increment,
   id_plu                         int(11) unsigned               not null,
   id_dossier                     int(11) unsigned,
   id_atome                       int(11) unsigned,
   date_correction                date                           not null,
   description                    varchar(255)                   not null,
   primary key (id_histo)
)
comment = "historique des corrections"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_decision                                         */
/*==============================================================*/
create table plu_decision
(
   id_decision                    int(11) unsigned               not null auto_increment,
   id_plu                         int(11) unsigned,
   id_type_acte_juridique         int(11) unsigned,
   nom_acte_juridique             varchar(2048),
   ref_deliberation               varchar(65536),
   dj                             date,
   primary key (id_decision)
)
comment = "table des decisions de plu"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_plu                                              */
/*==============================================================*/
create table plu_plu
(
   id_plu                         int(11) unsigned               not null auto_increment,
   da                             date,
   date_generation                date,
   id_etat_generation             int(11) unsigned,
   primary key (id_plu)
)
comment = "table des versions de plu"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_iso                                              */
/*==============================================================*/
create table plu_iso
(
   id_iso                         int(11) unsigned               not null auto_increment,
   id_plu                         int(11) unsigned               not null,
   iso_c_nom                      varchar(255)                    not null,
   iso_i_nom                      varchar(255)                    not null,
   iso_date                       date                           not null,
   iso_c_taille                   bigint unsigned               not null,
   iso_i_taille                   bigint unsigned               not null,
   primary key (id_iso)
)
comment = "table contient les informations des fichiers iso"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_version_atome                                    */
/*==============================================================*/
create table plu_version_atome
(
   id_version                     int(11) unsigned               not null auto_increment,
   id_atome                       int(11) unsigned               not null default '0',
   num_version                    int(11) unsigned               not null default '0',
   date_approbation               date,
   date_application               date,
   date_evolution                 date,
   date_archivage                 date,
   a_archiver 					  char(1) 						default 'N',
   primary key (id_version)
)
comment = "table des versions d'atome"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

/*==============================================================*/
/* Table : plu_type_acte_juridique                              */
/*==============================================================*/
create table plu_type_acte_juridique
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
/* Table : plu_etat_generation                                  */
/*==============================================================*/
create table plu_etat_generation
(
   id_etat                        int(11) unsigned               not null auto_increment,
   etat                           varchar(255)                   not null,
   primary key (id_etat)
)
comment = "etats de la generation des iso et site"
engine = innodb
default charset=utf8 collate=utf8_unicode_ci
row_format = dynamic;

