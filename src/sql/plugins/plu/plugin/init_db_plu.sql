/*==============================================================*/
/* Init des donn�es types acte juridique                        */
/*==============================================================*/
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (1,'Jugement');
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (2,'Modification');
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (3,'R�vision g�n�rale');
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (4,'R�vision simplifi�e');
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (5,'Mise � jour');
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (6,'Mise en compatibilit�');
insert into `plu_type_acte_juridique`(`id_type`,`type`) values (7,'Autre');

/*==============================================================*/
/* Init des donn�es etat g�n�ration                             */
/*==============================================================*/
insert into `plu_etat_generation`(`id_etat`,`etat`) values (1,'Demande en cours');
insert into `plu_etat_generation`(`id_etat`,`etat`) values (2,'En cours');
insert into `plu_etat_generation`(`id_etat`,`etat`) values (3,'OK');
insert into `plu_etat_generation`(`id_etat`,`etat`) values (4,'KO/A relancer');
insert into `plu_etat_generation`(`id_etat`,`etat`) values (5,'A relancer');

/*==============================================================*/
/* Init des donn�es plu s�qences								*/
/*==============================================================*/
insert into `plu_sequences` (`sequence_name`,`next_val`) values ('plu_dossier_id',521);
insert into `plu_sequences` (`sequence_name`,`next_val`) values ('plu_id',21);
insert into `plu_sequences` (`sequence_name`,`next_val`) values ('plu_version_atome_id',801);
insert into `plu_sequences` (`sequence_name`,`next_val`) values ('plu_dossier_version_atome_id',5757);