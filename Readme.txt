Résumé

Ce projet implémente un classifieur gestuel basé sur l'algorithme de Rubine.
Voici les principales étapes de ce travail :

Calcul des features : J'ai implémenté les méthodes nécessaires pour calculer les onze premiers features définis dans l'article de Rubine,
en les intégrant dans la classe Trace. Ces caractéristiques permettent de représenter chaque tracé sous la forme d'un vecteur de dimension 11.

La méthode initFeatures() de la classe Trace calcule les valeurs de ces 11 features, et les stocke dans un attribut privé de type Vecteur.
La méthode getFeatureVector() retourne le vecteur de dimension 11 contenant les valeurs calculées pour le tracé.

Implémentation des estimateurs : La classe Geste a été enrichie avec des méthodes permettant de calculer les estimateurs nécessaires à la reconnaissance des gestes, tels que :

Matrice de covariance : Calculée à partir des vecteurs de features représentant les tracés du geste appelant.
Espérance : Calculée comme vecteur moyen des vecteurs de features du geste appelant.
Vecteur de poids : Calculé en utilisant l'inverse de la matrice eotccm.
Biais : Calculé comme indiqué dans l'article de Rubine.

Les méthodes initFeatures() et initEstimators() sont utilisées pour initialiser ces estimateurs.
Les accesseurs getCovMatrix(), getEsperance(), getWeightVector(), et getBias() permettent de récupérer ces valeurs pour chaque geste.

Implémentation du classifieur : La classe Rubine a été conçue pour implémenter l'interface Recognizer et contient les méthodes suivantes :

init(Lexique l) : Initialise le lexique en utilisant les méthodes de la classe Lexique pour charger les données de test et de formation. Calcule également la matrice de covariance globale et son inverse.
squaredMahalanobis(Vecteur t, Vecteur g) : Calcule le carré de la distance de Mahalanobis entre un tracé représenté par son vecteur de features et un geste représenté par son espérance.
recognize(Trace t) : Retourne le geste qui est reconnu à partir d'un tracé donné, en utilisant les estimateurs calculés.
test(Lexique lexicon) : Teste pour chaque tracé du lexique le geste qui lui correspond, et retourne un tableau des taux de reconnaissance pour chaque geste.

Tuning : J'ai expérimenté avec différents ensembles de features pour optimiser la reconnaissance.
J'ai comparé les résultats en utilisant uniquement quatre nouveaux features, puis en combinant ces quatre avec les sept premiers pour déterminer les meilleures performances.
La méthode initFeatures() a été modifiée pour inclure des features supplémentaires liés aux angles et aux distances accumulées, afin d'améliorer la précision du classifieur.

Difficultés rencontrées:
Débogage de la classe Rubine et des algorithmes associés : Je remercie Alexandre NGUYEN Tai pour son aide précieuse dans le débogage et l'amélioration des algorithmes.

Origine du code source

Sources extérieures : Aucune source extérieure n'a été utilisée pour le code, à l'exception de discussions générées avec une IA (ChatGPT) pour m'aider à comprendre certains concepts mathématiques
et pour m'assister dans la correction de bugs complexes.
De plus, j'ai mis mon code en accès libre sur GitHub pour permettre à mes camarades de le comparer au leur, afin de les aider à identifier et corriger d'éventuels bugs.

Résultats des tests
J'ai testé le classifieur sur le lexique en utilisant la méthode test(Lexique lexicon) de la classe Rubine.
Les résultats montrent que le taux de reconnaissance varie en fonction de la complexité des gestes et de la qualité des tracés d'apprentissage.

Voici un résumé des taux de reconnaissance obtenus pour chaque geste, réalisés avec des tracés de haute qualité :
Gesture 0 recognition rate: 100.00%
Gesture 1 recognition rate: 90.00%
Gesture 2 recognition rate: 70.00%
Gesture 3 recognition rate: 90.00%
Gesture 4 recognition rate: 90.00%
Gesture 5 recognition rate: 100.00%
Gesture 6 recognition rate: 80.00%
Gesture 7 recognition rate: 90.00%
Gesture 8 recognition rate: 100.00%
Gesture 9 recognition rate: 100.00%
Gesture 10 recognition rate: 90.00%
Gesture 11 recognition rate: 90.00%
Gesture 12 recognition rate: 100.00%
Gesture 13 recognition rate: 100.00%
Gesture 14 recognition rate: 100.00%

Et voici les résultats avec les tracés fournis de qualité moindre :
Gesture 0 recognition rate: 81.82%
Gesture 1 recognition rate: 72.73%
Gesture 2 recognition rate: 54.55%
Gesture 3 recognition rate: 36.36%
Gesture 4 recognition rate: 63.64%
Gesture 5 recognition rate: 36.36%
Gesture 6 recognition rate: 100.00%
Gesture 7 recognition rate: 36.36%
Gesture 8 recognition rate: 72.73%
Gesture 9 recognition rate: 18.18%
Gesture 10 recognition rate: 27.27%
Gesture 11 recognition rate: 54.55%
Gesture 12 recognition rate: 27.27%
Gesture 13 recognition rate: 80.00%
Gesture 14 recognition rate: 60.00%
Gesture 15 recognition rate: 18.18%

Ces résultats indiquent que les gestes plus simples ou plus distinctifs sont mieux reconnus, tandis que ceux présentant des similarités ou des variations importantes ont des taux de reconnaissance plus faibles.

Exercice 4 : Comparaison des features

Résultats avec 7 features initiales :
Gesture 0 recognition rate: 100.00%
Gesture 1 recognition rate: 90.00%
Gesture 2 recognition rate: 20.00%
Gesture 3 recognition rate: 20.00%
Gesture 4 recognition rate: 60.00%
Gesture 5 recognition rate: 70.00%
Gesture 6 recognition rate: 20.00%
Gesture 7 recognition rate: 90.00%
Gesture 8 recognition rate: 10.00%
Gesture 9 recognition rate: 60.00%
Gesture 10 recognition rate: 70.00%
Gesture 11 recognition rate: 90.00%
Gesture 12 recognition rate: 80.00%
Gesture 13 recognition rate: 20.00%
Gesture 14 recognition rate: 88.89%

Résultats avec seulement 4 features supplémentaires :
Gesture 0 recognition rate: 90.00%
Gesture 1 recognition rate: 90.00%
Gesture 2 recognition rate: 70.00%
Gesture 3 recognition rate: 90.00%
Gesture 4 recognition rate: 50.00%
Gesture 5 recognition rate: 100.00%
Gesture 6 recognition rate: 80.00%
Gesture 7 recognition rate: 80.00%
Gesture 8 recognition rate: 40.00%
Gesture 9 recognition rate: 90.00%
Gesture 10 recognition rate: 90.00%
Gesture 11 recognition rate: 50.00%
Gesture 12 recognition rate: 90.00%
Gesture 13 recognition rate: 90.00%
Gesture 14 recognition rate: 100.00%

Ces résultats montrent que les 4 features alternatifs suffisent dans certains cas, mais en les combinant avec les 7 features d'origine, on obtient des résultats plus précis.

Conclusion sur le tuning
Les tests ont montré que l'utilisation de toutes les caractéristiques disponibles (les 11 features) permettait de maximiser la reconnaissance des gestes, bien que cela augmente le coût de calcul.
Pour des cas spécifiques où la simplicité des calculs est privilégiée, il est possible de sélectionner un sous-ensemble optimal de features sans trop compromettre la précision.
L'utilisation d'une combinaison des 7 features d'origine et de 4 nouvelles a donné de bons résultats dans la plupart des cas,
suggérant que ces features sont complémentaires et permettent d'améliorer la reconnaissance sans ajouter trop de redondance.