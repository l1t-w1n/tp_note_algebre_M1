
Résumé du Travail

Ce que j’ai fait :

Vecteur et Vecteur2D : Ces classes permettent la manipulation de vecteurs 2D pour des opérations mathématiques simples comme le calcul de l'angle, de la norme, etc.

Matrice : Permet des opérations de calcul matriciel, notamment la covariance, l'inverse, et le produit de matrices.

Trace : Ajout d'une méthode de resampling dans la classe Trace pour réduire le nombre de points visibles tout en maintenant la forme de la trace. Cela permet de simplifier les traces en éliminant les points trop proches;
        Ajout de la possibilité de calculer la longueur totale de la trace ainsi que le nombre de points qu’elle contient.

Resampling : Programme principal qui itère sur plusieurs fichiers de traces, applique un resampling avec un seuil de distance (epsilon), et exporte les traces modifiées.

Ce que je n'ai pas fait :
Toutes les fonctionnalités principales sont implémentées.
Je n’ai pas ajouté de gestion d'exceptions avancées ou de traitement d'erreurs approfondi pour des cas inhabituels, tels que des fichiers corrompus ou mal formatés.

Commentaires sur le code :
Le code est largement commenté pour faciliter la compréhension de chaque méthode et classe. Chaque fonctionnalité clé est expliquée dans le code source avec des commentaires décrivant les objectifs et les mécanismes.
Le resampling est réalisé via la méthode resample(double epsilon) dans la classe Trace, qui permet de simplifier les traces tout en gardant leur forme générale.
L'exportation des traces après resampling se fait via la méthode export.

Origine du Code Source
L'essentiel du code a été écrit par moi-même, mais pour certaines idées concernant la structure et l'organisation du code, j'ai utilisé ChatGPT comme source d'inspiration.
À part cela, aucune autre source externe n'a été utilisée pour ce projet.


Features à Utiliser dans l’Algorithme Réduit
Pour l'implémentation de l'algorithme de Rubine, j'ai sélectionné les quatre fonctionnalités suivantes comme étant les plus importantes pour une version réduite de l'algorithme :

Cosinus de l'angle de début (cos alpha) : donne une indication sur la direction initiale du geste.
Longueur totale de la courbe : permet de capturer la taille globale du geste, indépendamment de sa forme.
Somme des angles entre deux vecteurs consécutifs du tracé : utile pour comprendre la complexité du geste en termes de changements de direction.
Distance entre le premier et le dernier point (||dmax||) : mesure la distance droite entre les points de départ et d'arrivée, utile pour évaluer la globalité du geste.
Cependant, je souhaiterais utiliser toutes les 13 fonctionnalités disponibles dans une version complète de l'algorithme.
