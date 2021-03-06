UPDATE `plu_sequences` SET next_val= 1 WHERE sequence_name="plu_historique_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_iso ) FROM plu_iso ) +1 WHERE sequence_name="plu_iso_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_plu ) FROM plu_plu ) +1 WHERE sequence_name="plu_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_dossier ) FROM plu_dossier ) +1 WHERE sequence_name="plu_dossier_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_dossier_version ) FROM plu_dossier_version_atome ) +1 WHERE sequence_name="plu_dossier_version_atome_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_version ) FROM plu_version_atome ) +1 WHERE sequence_name="plu_version_atome_sequence";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_version ) FROM plu_version_atome ) +1 WHERE sequence_name="plu_version_atome_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_fichier ) FROM plu_fichier ) +1 WHERE sequence_name="plu_fichier_id";
UPDATE `plu_sequences` SET next_val= ( SELECT max( id_fichier_contenu ) FROM plu_fichier_contenu ) +1 WHERE sequence_name="plu_fichier_contenu_id";