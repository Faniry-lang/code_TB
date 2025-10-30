Je souhaite mettre au point la conception de la base de données pour gérer ces 3 entités principales associés à leurs utilisateur : compte courant, compte dépôt et prêts. Ces 3 entités seront configurables telles que :

Gestion Compte Courant => Un seul compte courant par utilisateur
Gestion Depot => exemple : interet 2% / ANS, retrait: 1 fois par mois, pas + 50 % (ces paramètres seront configurables par client : intérêt, nombre de retrait par mois, somme qu'il peut retirer par retrait )
Gestion Pret => interet 24% / ANS (ces paramètres seront configurables par client : intérêt, périodicité du remboursement )

Conditions : 
Sépare bien dans des tables distinctes les type de mouvement pour chaque entité principale dans cette conception.
Une hitorisation de tous les mouvements est nécessaire dans cette conception.
Un client possède un seul compte courant, un seul compte dépôt et plusieurs prêts.