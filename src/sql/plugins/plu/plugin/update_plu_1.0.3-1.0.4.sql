/* MANTIS 162 - Atome en double dans le dossier "Logement social et protection du commerce et de lartisanat" 
 * du PLU 11 */
DELETE from plu_dossier_version_atome where plu_dossier_version_atome.id_dossier_version=6878;

/* MANTIS 166 - probleme dencodage dans les pages html specifiques */
ALTER TABLE `plu_dossier` CHANGE `html_specifique_c` `html_specifique_c` MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL;
ALTER TABLE `plu_dossier` CHANGE `html_specifique_i` `html_specifique_i` MEDIUMTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL;