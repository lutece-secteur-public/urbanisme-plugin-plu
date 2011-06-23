/*==============================================================*/
/* Init des donn�es types acte juridique                        */
/*==============================================================*/
insert into `type_acte_juridique`(`id_type`,`type`) values (1,'Jugement');
insert into `type_acte_juridique`(`id_type`,`type`) values (2,'Modification');
insert into `type_acte_juridique`(`id_type`,`type`) values (3,'R�vision g�n�rale');
insert into `type_acte_juridique`(`id_type`,`type`) values (4,'R�vision simplifi�e');
insert into `type_acte_juridique`(`id_type`,`type`) values (5,'Mise � jour');
insert into `type_acte_juridique`(`id_type`,`type`) values (6,'Mise en compatibilit�');
insert into `type_acte_juridique`(`id_type`,`type`) values (7,'Autre');

/*==============================================================*/
/* Init des donn�es etat g�n�ration                             */
/*==============================================================*/
insert into `etat_generation`(`id_etat`,`etat`) values (1,'Demande en cours');
insert into `etat_generation`(`id_etat`,`etat`) values (2,'En cours');
insert into `etat_generation`(`id_etat`,`etat`) values (3,'OK');
insert into `etat_generation`(`id_etat`,`etat`) values (4,'KO/A relancer');
insert into `etat_generation`(`id_etat`,`etat`) values (5,'A relancer');
