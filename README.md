# MasterMind

Groupe n°6

Lien du repo : https://github.com/Lucas-Da-Silveira/SAE-1-2-6-Mastermind

Membres du groupe :
- Lucas DA SILVEIRA (@Lucas-Da-Silveira)
- Théo GASNER (@tgasner)
- Ewan HUMBERT (@Xeway)
- Jessy MOUGAMMADALY (@SunoWhere, @jmouga)
- Chahid YASSINE (@cyassine-iut90)

Possibilité de changer le nombre de rangées (12 de base), _**(et/ou)**_ le nombre de cases (4 de base), _**(et/ou)**_ le nombre de couleurs (4 de base, maximum 5).
<br>
Pour ce faire, utiliser les paramètres suivants :
- `--rows=<nb_rangees>`
- `--cols=<nb_cases>`
- `--colors=<nb_couleurs>`

Pour le choix de la stratégie de l'ordinateur, utiliser le paramètre `--aimode=<n°_strat>` :
<br>
- Stratégie `0` : première stratégie
- Stratégie `1` : deuxième stratégie
- Autre stratégie (pas de paramètre ou autre nombre) : stratégie jouant aléatoirement

Exemple : Lancer une partie joueur vs ordinateur, 10 rangées, 5 cases, 3 couleurs, deuxième stratégie
```
1 --rows=10 --cols=5 --colors=3 --aimode=1
```
