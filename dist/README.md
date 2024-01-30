Copyright © JasonPercus Systems, Inc - All Rights Reserved
# **Introduction**

Ce projet a pour but de renvoyer la liste des projets NetBeans ouverts et de préciser lequel d'entre eux est actif ou pas.

# **Exécution**

Pour exécuter l'application, il suffit simplement de la lancer avec ```cmd.exe``` avec la commande suivante: ```NbExplorerProject.exe```.

# **Résultats**

Voici à quoi ressemble les résultats:
```
[*] C:\Users\You\Desktop\ProjectA
[ACTIVE] C:\Users\You\Documents\ProjectB
[*] D:\ProjectB
```
> On peut constater que c'est le ```ProjectB``` qui est en cours d'édition parmi la liste des projets.

> Il est possible d'extraire facilement les informations de chaque ligne avec la regex java ```^\[(?<active>.*)\] (?<path>.*)$```

# **Licence**
Le projet est sous licence "MIT License"