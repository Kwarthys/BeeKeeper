# ![(French flag)](https://upload.wikimedia.org/wikipedia/en/thumb/c/c3/Flag_of_France.svg/50px-Flag_of_France.svg.png) Modèle de thèse Doctorat Bretagne Loire

*Explications en français*

Ce dépôt contient un modèle latex pour le manuscrit de thèse supportant toutes les écoles doctorales de l'[École des docteurs Bretagne Loire](https://www.doctorat-bretagneloire.fr/).

Ce modèle a pour but principal de fournir une première et une quatrième de couverture du manuscrit de thèse intégralement écrites en latex.
Ces couvertures ont été (manuellement) calquées sur le modèle original au format M$ Word fourni par MathSTIC en 2018, puis généralisé à toutes les écoles doctorales DBL.
Tandis que la couverture du manuscrit se doit de respecter le format établi par DBL, la disposition du contenu interne du manuscrit est elle plus flexible.
La disposition proposée dans ce modèle est donc donnée à titre d'exemple mais il n'est cependant pas obligatoire de la respecter.


### Structure du dépôt

- `main.tex` contient le squelette du document, aucun texte du manuscrit n'est présent dans ce fichier
- `these-dbl.cls` contient les dépendances, les paramètres de la bibliographie dont le style de citation et les paramètres de mise en page globale du manuscrit et plus particulièrement des deux couvertures
- `Couverture-these/pagedegarde.tex` contient les variables à remplir par l'auteur pour compléter la page de garde, ces variables sont utilisées par `\maketitle` redéfini dans `these-dbl.cls`
- `Couverture-these/resume.tex` contient les variables à remplir par l'auteur pour compléter la quatrième de couverture, ces variables sont utilisées par les macros définies dans `these-dbl.cls`
- Le `Makefile` vous aide à compiler le latex et la bibliographie en un pdf (détails plus bas)
- Les autres dossiers contiennent chacun un chapitre du document


### Remplir la première et quatrième de couverture

Les informations de la page de garde doivent être renseignées dans les variables du fichier `Couverture-these/pagedegarde.tex`.
Il suffit de modifier les lignes appelant les commandes `\ecoledoctorale{}` et `\etablissement{}` pour adapter les couvertures à l'école doctorale et à l'établissement, ou les établissements, délivrant le diplôme (par défaut MathSTIC et Université de Rennes 1, respectivement).
Le fichier `Couverture-these/README-LISTE-CHOIX.MD` liste Les écoles doctorales et établissements supportés ainsi que des liens vers les listes des spécialités et unités de recherche de chaque école doctorale pour aider à compléter la page de garde (commandes `\spec{}` et `\uniterecherche{}`).
Modifier la disposition des éléments de la page de garde présente dans `these-dbl.cls` ne devrait  être nécessaire que pour rajouter quelques `\vspace` pour préserver la structure original après avoir renseigné les différents champs (e.g., spécialité, composition du jury).

Les variables relatives à la quatrième de couverture sont à renseigner dans `Couverture-these/resume.tex`.


### Dépendances

Une distribution LaTeX comme texlive est nécessaire pour compiler le document. À noter que certains paquets nécessaires à ce document ne sont pas toujours directement inclus dans une installation de base de texlive.

Paquets additionnels nécessaires :

- Fedora (installer avec `dnf install`)
	- texlive-abstract
	- texlive-wallpaper
- Autres distributions
	- TODO (contributions bienvenues)


### Compiler le latex en pdf

Le `Makefile` fournis vous aide à compiler votre document.
Il utilise `pdflatex` et `biber` pour générer le fichier pdf et peut l'afficher grâce à `evince` sur Linux et `open` sur MacOS.

Compiler votre document avec `pdflatex/biber` :

    make

Afficher le pdf généré :

    make viewpdf

Suppimer tous les fichiers générés, pdf inclus :

    make clean


### Spécificités d'un document multilingue

La liste des langues utilisées est définie dans `these-dbl.cls` où le paquet `babel` est importé.
Etant donné que la quatrième de couverture contient du français et de l'anglais, il est nécessaire de conserver au moins ces deux langues dans la liste.
Il faut utiliser `\selectlanguage{x}` (où x est `french` ou `english`) pour changer de langue après son insertion.

Si la langue principale du contenu du document est l'anglais, vous devez effectuer quelques modifications au modèle :

- remplacer `\selectlanguage{french}` par `\selectlanguage{english}` dans `main.tex`
- modifier la ligne 488 de `these-dbl.cls` pour remplacer `Partie` par `Part` dans les entêtes
- inclure un résumé en français d'au moins 4 pages


### Changer la police du contenu

La police imposée pour les couvertures est Arial mais vous pouvez utiliser une autre police pour le contenu de la thèse en redéfinissant les commandes `\rmdefault` et `\sfdefault` comme commenté dans `these-dbl.cls`.
Actuellement la police par défaut de latex est celle utilisée pour le contenu.


-----

# ![(UK flag)](https://upload.wikimedia.org/wikipedia/en/thumb/a/ae/Flag_of_the_United_Kingdom.svg/50px-Flag_of_the_United_Kingdom.svg.png) Thesis template for Doctorat Bretagne Loire

*English explanations*

This repository contains a template for the thesis manuscript supporting all doctoral schools of the [École des docteurs Bretagne Loire](https://www.doctorat-bretagneloire.fr/).

The main goal of this template is to provide both front and back covers of the thesis manuscript entirely written in latex.
These covers have been (manually) reproduced from the original M$ Word model provided by MathSTIC in 2018, then generalized to all doctoral schools.
While the manuscript covers must follow the rules of DBL, the internal layout of the content is more flexible.
The content layout provided in this template is therefore given as an exemple rather than as a  mandatory framework.


#### Structure of the repository

- `main.tex` contains the backbone structure of the document, no content is present in this file
- `these-dbl.cls` contains the package dependencies, bibliography parameters including citation style and overall layout specifications including both front and back cover layouts
- `Couverture-these/pagedegarde.tex` contains the variables that must be filled by the author to complete the front cover, these variables are used in `\maketitle` redefined in `these-dbl.cls`
- `Couverture-these/resume.tex` contains the variables that must be filled by the author to complete the back cover, these variables are used in the macros defined in `these-dbl.cls`
- The `Makefile` helps you compile the latex and bibliography into a pdf (details below)
- The rest of the directories each contain one chapter of the document


#### Fill the front and back cover

The front cover details must be provided by filling the variables in `Couverture-these/pagedegarde.tex`.
The lines calling the `\ecoledoctorale{}` and `\etablissement{}` (i.e., institution) commands must be modified to adapt the covers to the doctoral school and institution(s) delivering the diplome (by default set to MathSTIC and Université de Rennes 1, respectively).
The file `Couverture-these/README-LISTE-CHOIX.MD` lists the supported doctoral schools and institutions, and contains links pointing to lists of domains and labs/faculties, for each doctoral school, that are needed to fill the front cover (commands `\spec{}` and `\uniterecherche{}`).
Modifying the front cover layout defined in `these-dbl.cls` should only be necessary to preserve the original layout using a few `\vspace` after filling the front cover (e.g., domain, jury section).

The back cover variables that must be filled are in `Couverture-these/resume.tex`.


#### Dependencies

A LaTeX distribution such as texlive is necessary in order to compile your document. Please note some necessary packages are not directly included in a base texlive installation.

Required additional packages:

- Fedora (install using `dnf install`)
	- texlive-abstract
	- texlive-wallpaper
- Other distributions
	- TODO (contributions welcome)


#### Compile latex into pdf

A `Makefile` is provided to help you compile your document. It uses `pdflatex` and`biber` to generate the pdf file and can display it by using `evince` on Linux or `open` on MacOS.

Compile your document with `pdflatex/biber`:

	make

Display the generated pdf:

	make viewpdf

Remove all generated files, pdf included:

	make clean


#### Particularities of a multilanguage document

The list of used languages is defined in `these-dbl.cls` where the package `babel` is imported.
As the back cover contains both French and English, it is necessary to keep at least both these languages in the list.
Use `\selectlanguage{x}` (where x is `french` or `english`) to switch language after its usage.

If the main language of your document is English, you must apply the following changes to the provided template:

- replace `\selectlanguage{french}` by `\selectlanguage{english}` in `main.tex`
- edit line 488 of `these-dbl.cls` to replace `Partie` by `Part` in the headers
- include a summary in French of at least 4 pages


### Change the content font

The required font for the covers is Arial but you can use another font for the content of the thesis by redefining the commands `\rmdefault` and `\sfdefault` as commented out in `these-dbl.cls`.
Currently the latex default font is the one used for the content.


-----

# ![(git logo)](https://www.inf.usi.ch/postdoc/roman/img/logo_git_50x50.png) Contribute

To propose changes, (1) you first have to request access to this repository (top page link, [HOWTO](https://docs.gitlab.com/ee/user/project/members/#project-membership-and-requesting-access)), (2) push your branch to this repository once access is granted, and finally (3) create a pull request with your proposed changes. Or just create an issue.

Maintainers: Pierre-Louis Roman (romanp@usi.ch).

Contributors: Louiza Yala (original & main developer), Josquin Debaz, Pierre-Louis Roman, Lucas Bourneuf, Corentin Guezenoc, Clément Elbaz, Florian Arrestier, Alexandre Honorat.

Upstream git repository: https://gitlab.inria.fr/ed-mathstic/latex-template

Previous upstream git repository (2019-2020): https://gitlab.inria.fr/proman/mathstic-thesis-template

Alternative repositories with a squashed history:
- https://github.com/remolaz/PhD_Thesis_Template_MathSTIC
- https://github.com/ahonorat/mathSTICtemplatePhD
