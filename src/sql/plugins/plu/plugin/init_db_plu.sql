/*==============================================================*/
/* Init des données types acte juridique                        */
/*==============================================================*/
insert into `type_acte_juridique`(`id_type`,`type`) values (1,'Jugement');
insert into `type_acte_juridique`(`id_type`,`type`) values (2,'Modification');
insert into `type_acte_juridique`(`id_type`,`type`) values (3,'Révision générale');
insert into `type_acte_juridique`(`id_type`,`type`) values (4,'Révision simplifiée');
insert into `type_acte_juridique`(`id_type`,`type`) values (5,'Mise à jour');
insert into `type_acte_juridique`(`id_type`,`type`) values (6,'Mise en compatibilité');
insert into `type_acte_juridique`(`id_type`,`type`) values (7,'Autre');

/*==============================================================*/
/* Init des données etat génération                             */
/*==============================================================*/
insert into `etat_generation`(`id_etat`,`etat`) values (1,'Demande en cours');
insert into `etat_generation`(`id_etat`,`etat`) values (2,'En cours');
insert into `etat_generation`(`id_etat`,`etat`) values (3,'OK');
insert into `etat_generation`(`id_etat`,`etat`) values (4,'KO/A relancer');
insert into `etat_generation`(`id_etat`,`etat`) values (5,'A relancer');
