% Cette classe de document est basee sur [report].
% Assurez vous d'avoir installe tous les paquets utiles
%%%%%% Licence%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% la classe LaTeX these-dbl
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% This program is free software: you can redistribute it and/or modify
% it under the terms of the GNU General Public License as published by
% the Free Software Foundation
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% PROPOSITION DE TEMPLATE POUR THESE DE DBL par L.YALA (Doctorante
% IRISA-UR1, Juin 2018)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Revision des en-tetes et prise en compte de [twoside]
% Revision de la gestion de la bibliographie avec BiblaTex
% Ajouts de boites et encarts grises
% revision de l'enchainement des chapitres
% Mise aux normes typographiques suivant les Regles Typographiques
% en usage \`{a} l'Imprimerie Nationale
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{these-dbl}[15/10/2020 v5.0]

\LoadClass[twoside,multicol,12pt,a4paper]{report}	% notez le "twoside", essentiellement pour alterner les en-tete et pieds de page gauche et droit si on veut utliser les fancyhead. 

\RequirePackage[utf8]{inputenc} 
\RequirePackage[english,french]{babel} % Last language is the default, use \selectlanguage{english} to switch
\RequirePackage{ifpdf}
\RequirePackage[usenames,dvipsnames]{color} % pour les couleurs
\RequirePackage{textcomp}
\RequirePackage{titlesec}
\RequirePackage{parcolumns}
\RequirePackage{multicol}
\RequirePackage[T1]{fontenc} % permet de sp\'{e}cifier \`{a} LaTeX l'utilisation du codage de caract\`{e}res T1, nouvelle norme LaTeX non utilis\'{e}e par d\'{e}faut pour des raisons de compatibilit\'{e} avec les anciens documents LaTeX.
\RequirePackage{lmodern} % Assurer une bonne impression!
\RequirePackage{tikz} % tikz est utilise pour tracer des boites, par exemple
\RequirePackage{fix-cm}
\RequirePackage{abstract}
\RequirePackage{graphicx} % Pour ins\'{e}rer des images. Utiliser le format jpg pour plus de simplicit\'{e}.
\RequirePackage{sectsty} % obligatoire, pour jouer avec l'apparence des titres
\RequirePackage{shorttoc} % pour le sommaire, au debut.
\RequirePackage{fancyhdr} % Afin de r\'{e}aliser soi-même les en-têtes et pieds de page, voir chaque d\'{e}but de chapitre.
\RequirePackage{pifont} % Pour utiliser des symboles divers.
\RequirePackage{color}
\RequirePackage{comment}
\RequirePackage{wallpaper}
\RequirePackage[inner=30mm,outer=20mm,top=30mm,bottom=20mm]{geometry}
\RequirePackage{setspace}
%Ou bien : \RequirePackage[top=4 cm, bottom=4 cm, left=3 cm, right=3 cm]{geometry} % On peut modifier ces valeurs pour augmenter ou r\'{e}duire les marges. Attention aux en-têtes de chapitres. 
\RequirePackage{epigraph} % \cleartoevenpage

\RequirePackage{setspace} % permet d'utiliser les commandes \spacing, doublespace (double interligne), singlespace (simple interligne) et onehalfspace (un interligne et demi)

\RequirePackage{hyperref} %Utiliser pour une bibliographie interactive + sommaire et TOC interactifs (pour une sortie PDF, par exemple).
\hypersetup{colorlinks=true, citecolor=black, filecolor=black, linkcolor=black, urlcolor=black}

\RequirePackage{eso-pic} % For \AddToShipoutPicture used for cover backgrounds
\RequirePackage{framed} % utilise pour coloriser les encarts

\onehalfspacing %Interligne 1,5

\RequirePackage{ifthen} % Entrer valeurs bool\'{e}ennes et autres options
\RequirePackage{csquotes} % Assurer les guillemets français
\frenchspacing
\FrenchFootnotes

% Define commands to set fonts throughout the document
\newcommand*{\selectfontfrontcover}{\fontfamily{phv}\selectfont}  % Font style used in front cover 
\newcommand*{\selectfontbackcover}{\fontfamily{phv}\selectfont}   % Font style used in back cover 
\newcommand*{\selectfontchapheads}{\fontfamily{phv}\selectfont} % Font style used chapter headings

% Override default font here if you want
%\renewcommand{\rmdefault}{qpl}
%\renewcommand{\sfdefault}{qpl} 



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% En-tete chap %%%%%%%%%%%%%%%%

\makeatletter
\def\thickhrulefill{\leavevmode \leaders \hrule height 1ex \hfill \kern \z@}
\def\@makechapterhead#1{%
  \vspace*{-30\p@}%
  {\parindent \z@ \raggedleft \reset@font
            \scshape \@chapapp{} \thechapter
        \par\nobreak
        \interlinepenalty\@M
    \selectfontchapheads \Huge \bfseries #1\par\nobreak
    %\vspace*{1\p@}%
    \hrulefill
    \par\nobreak
    \vskip 50\p@
  }}
\def\@makeschapterhead#1{%
 \vspace*{-50\p@}%
  {\parindent \z@ \raggedleft \reset@font
            \scshape \vphantom{\@chapapp{} \thechapter}
        \par\nobreak
        \interlinepenalty\@M
    \selectfontchapheads \Huge \bfseries #1 \par\nobreak
    %\vspace*{1\p@}%
    \hrulefill
    \par\nobreak
    \vskip 30\p@
  }}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% Tete de parties %%%%%%%%%%%%%%%%

\makeatletter
\newcommand*{\jolipart}[1]{%
	\begin{center}
		\begin{Huge}\color{black}
			#1
		\end{Huge}
		\end{center}
		\vspace{1cm}		
	\begin{center}
%\begin{Huge}
		%\ding{167}
	%\end{Huge}	
	\hrulefill
	\end{center}		
	}

\parttitlefont{\jolipart}

\partnumberfont{\sc\normalsize}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% REGLAGE DE LA BIBLIOGRAPHIE %%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% ATTENTION, on utilise Biblatex %%%%%%%%%%%%%

% Les dernieres versions de biblatex pr\'{e}conisent l'option backend=biber
% pour profiter des derni\`{e}res nouveaut\'{e}s. Si vous choisissez cette
% option, il vous faut simplement compiler avec $biber nomdufichier (sans le
% .tex). Pour utiliser bibtex, remplacer par backend=bibtex.
%\RequirePackage[hyperref,backend=biber,
  % Exemples de styles: alphabetic, ieee, nature, numeric, verbose-trad1 (en utilisant \footcite{}).
  % https://www.overleaf.com/learn/latex/Biblatex_bibliography_styles
%  style=verbose-trad1
  %style=numeric
%  ]{biblatex}

% Style biblatex avec gestion des mots cl\'{e}s des entr\'{e}es bibliographiques
% + les abbr\'{e}viations idem, ibidem, op cit. Pr\'{e}voir un fichier bib
% contenant ces mots-cl\'{e}s, ici primary et secondary.
%\defbibheading{primary}{\section*{Sources primaires}}
%\defbibheading{secondary}{\section*{Sources secondaires}}


%%%% Ajustements pour la bibliographie

%\renewcommand*{\newunitpunct}{\addcomma\space} % Virgule entre les parties d'une reference (merci a Josquin Debaz)

%\DeclareFieldFormat[article]{volume}{\textbf{#1}}  %Le numero de volume en gras
%\DeclareFieldFormat[article]{number}{\textit{#1}} %Le numero dans le volume en italique
%\DeclareFieldFormat{pages}{page(s): #1} % page(s) en toutes lettres, si on veut...

% Locutions latines en italique (comme ibid, loc.cit. , etc.) Merci \`{a} Josquin Debaz
%\renewcommand{\mkibid}[1]{\emph{#1}}

% Et pour mettre le in en italique dans la ref\'{e}rence biblio (merci encore \`{a} Josquin Debaz)
%\DefineBibliographyStrings{english}{%
%        in = {\emph{in}}%
%}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%% Interligne simple pour citations et vers %%%%%%%%%%%%%%%%%%
%% Environnements: QUOTE, QUOTATION, VERSE
% Copyright (C) 2002 Mike Nolta <mrnolta@princeton.edu>, GPL V. 2.0 and
% later version. http://www.physics.princeton.edu/cosmology/computing/PrincetonThesis.cls
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\let\orig@quote\quote
\let\endorig@quote\endquote
\renewenvironment*{quote}
	{\begin{orig@quote}\begin{singlespace}}
	{\end{singlespace}\end{orig@quote}}

\let\old@quotation\quotation
\let\endold@quotation\endquotation
\renewenvironment*{quotation}
	{\begin{old@quotation}\begin{singlespace}}
        {\end{singlespace}\end{old@quotation}}

\let\old@verse\verse
\let\endold@verse\endverse
\renewenvironment*{verse}
	{\begin{old@verse}\begin{singlespace}}
        {\end{singlespace}\end{old@verse}}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% VARIABLES PAGE DE GARDE %%%%%%%%%%%%%%%%

%%%%% Dossier contenant les info de l'ecole doctorale
\newcommand*{\direcole}[1]{\gdef\vdirecole{./Couverture-these/ecoles-doctorales/#1}}
\direcole{}

%%%%% Nom ecole, une variable par ligne
\newcommand{\nomecoleA}[1]{\gdef\@nomecoleA{#1}}
\nomecoleA{}
\newcommand{\nomecoleB}[1]{\gdef\@nomecoleB{#1}}
\nomecoleB{}

%%%%% Numero ecole doctorale
\newcommand{\numeroecole}[1]{\gdef\@numeroecole{#1}}
\numeroecole{}

%%%% Etablissement delivrant le diplome, une variable par ligne
\newcommand{\nometablissementA}[1]{\gdef\vnometablissementA{#1}}
\nometablissementA{}
\newcommand{\nometablissementB}[1]{\gdef\vnometablissementB{#1}}
\nometablissementB{}
\newcommand{\nometablissementC}[1]{\gdef\vnometablissementC{#1}}
\nometablissementC{}
\newcommand{\nometablissementD}[1]{\gdef\vnometablissementD{#1}}
\nometablissementD{}
\newcommand{\nometablissementE}[1]{\gdef\vnometablissementE{#1}}
\nometablissementE{}

%%%% Logos etablissement delivrant le diplome, supporte deuble affiliation
\newcommand*{\logoetablissementA}[1]{\gdef\vlogoetablissementA{#1}}
\logoetablissementA{}
\newcommand*{\logoetablissementB}[1]{\gdef\vlogoetablissementB{#1}}
\logoetablissementB{}

%%%% Hauteur des logos, variable selon les (double) affiliations
\newcommand*{\hauteurlogoecole}[1]{\gdef\vhauteurlogoecole{#1}}
\hauteurlogoecole{2.4cm}
\newcommand*{\hauteurlogoetablissementA}[1]{\gdef\vhauteurlogoetablissementA{#1}}
\hauteurlogoetablissementA{}
\newcommand*{\hauteurlogoetablissementB}[1]{\gdef\vhauteurlogoetablissementB{#1}}
\hauteurlogoetablissementB{2.4cm}

%%%% Eventuel sous-titre
\newcommand{\lesoustitre}[1]{\gdef\@lesoustitre{#1}}
\lesoustitre{}

%%%% Discipline
\newcommand{\discipline}[1]{\gdef\@discipline{#1}}
\discipline{}

%%%% Jury
\newcommand{\jury}[1]{\gdef\@jury{#1}}
\jury{}

%%%%% Sp\'{e}cialit\'{e}
\newcommand{\spec}[1]{\gdef\@spec{#1}}
\spec{}

%%% Ville de soutenance
\newcommand{\lieu}[1]{\gdef\@lieu{#1}}
\lieu{}

%%% Unite de recherche: laboratoire / department / unit\'{e}
\newcommand{\uniterecherche}[1]{\gdef\@uniterecherche{#1}}
\uniterecherche{}

%%% Num\'{e}ro de la th\`{e}se
\newcommand{\numthese}[1]{\gdef\@numthese{#1}}
\numthese{}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% PAGE DE GARDE %%%%%%%%%%%%%%%%

% Define some font sizes specific to the covers, supposed to be in 12pt
\newcommand{\HugeTwelve}{\fontsize{26}{31}\selectfont} % 12pt \Huge
\newcommand{\LARGETwelve}{\fontsize{20.74}{25}\selectfont} % 12pt \LARGE
\newcommand{\LargeTwelve}{\fontsize{16}{19}\selectfont} % 12pt \Large
\newcommand{\largeTwelve}{\fontsize{14.4}{17}\selectfont} % 12pt \large
\newcommand{\normalTwelve}{\fontsize{12}{13.2}\selectfont} % 12pt \normalsize
\newcommand{\smallTwelve}{\fontsize{11}{13.5}\selectfont} % 12pt \small
\newcommand{\footnotesizeTwelve}{\fontsize{9.5}{11}\selectfont} % 12pt \footnotesize

% Affiche les logos sur les pages de couverture
\newcommand{\displayLogos}{%
  \thispagestyle{empty}
  \begin{tikzpicture}[remember picture,overlay,line width=0mm]
    \node[xshift=6.2cm,yshift=2cm] {
    \parbox{\textwidth}{
      $\vcenter{\hbox{%
        \includegraphics[keepaspectratio,height=\vhauteurlogoecole,%width=7cm
        ]{\vdirecole/logo}%
      }}$
      \hfill
      {\if\vlogoetablissementA\empty \else
      $\vcenter{\hbox{%
        \includegraphics[keepaspectratio,height=\vhauteurlogoetablissementA,width=7cm
        ]{./Couverture-these/logos-etablissements/\vlogoetablissementA}%
      }}$
      \fi}
      \hspace{3mm}
      $\vcenter{\hbox{%
        \includegraphics[keepaspectratio,height=\vhauteurlogoetablissementB,width=7cm
        ]{./Couverture-these/logos-etablissements/\vlogoetablissementB}%
      }}$
      }
  };
  \end{tikzpicture}
  \par\nobreak
}

%mise en page de la page de garde
\makeatletter
\def\maketitle{%
  \thispagestyle{empty}
  \clearpage
  %background image of the front cover
  \AddToShipoutPicture*{%
    \put(0,0){%
    \parbox[b][42.6cm]{\paperwidth}{%
        \vfill
        \includegraphics[width=\paperwidth,keepaspectratio,trim={0 5pt 0 0}]{\vdirecole/image-fond-garde} % Must trim white border off of bottom
        \begin{tikzpicture}
            \fill[color-ecole] (0,0) rectangle (\paperwidth,4.4);
        \end{tikzpicture}
        \vfill
  }}}
  \displayLogos
  %
  \begin{tikzpicture}[remember picture,overlay,line width=0mm]
   \node at (current page.center)
{\parbox{17.6cm}{
\vspace{3.6cm}

\selectfontfrontcover % Set font style for front cover page

{\HugeTwelve \textsc{Th\`{e}se de doctorat de} \\}

% \vspace{5mm}
{\normalTwelve \if\@nomecoleB\empty ~\\ \else \fi} % To compensate the 2 lines of MathSTIC
{\setlength{\baselineskip}{0.9\baselineskip}
{\largeTwelve \if\vnometablissementA\empty ~ \else \vnometablissementA \fi} \\
{\largeTwelve \if\vnometablissementB\empty ~ \else \vnometablissementB \fi} \\
{\largeTwelve \if\vnometablissementC\empty ~ \else \vnometablissementC \fi} \\
{\largeTwelve \if\vnometablissementD\empty ~ \else \vnometablissementD \fi} \\
{\largeTwelve \vnometablissementE} \\
\par}
\vspace{0.01cm}
{\setlength{\baselineskip}{0.7\baselineskip}
{\smallTwelve \textsc{\'{E}cole Doctorale \No \@numeroecole}} \\
{\normalTwelve \textit{\@nomecoleA}} \\
{\normalTwelve \if\@nomecoleB\empty \else \textit{\@nomecoleB} \\ \fi}
{\normalTwelve Sp\'{e}cialit\'{e} : \textit{\@spec}}

%\fontsize{12}{10}\selectfont
\vspace{0.5cm}
\hspace{0.6cm}{\normalTwelve Par \vspace{0.15cm}}
\par}
\hspace{0.6cm}{\LARGETwelve \textbf{\@author}} \vspace{0.5cm}

{\LargeTwelve \textbf{\@title}} \vspace{0.5cm}
	
{\largeTwelve \@lesoustitre} \vspace{0.5cm}
\begin{spacing}{1}
	 \smallTwelve
	 \textbf{Th\`{e}se pr\'{e}sent\'{e}e et soutenue \`{a} \@lieu, le \@date} \\
	 \textbf{Unit\'{e} de recherche : \@uniterecherche} \\
	 \textbf{\if\@numthese\empty \else Th\`{e}se \No : \@numthese \fi} % Hide line if no number provided
\end{spacing}
\vspace{1.3cm}
	\begin{small}
	\begin{spacing}{1}
		 \@jury
	\end{spacing}
	\end{small}
}
};
\end{tikzpicture}
}

\makeatother



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% QUATRIEME DE COUVERTURE %%%%%%%%%%%%%%%%

\newcommand{\backcoverheader}{%
\thispagestyle{empty}
\AddToShipoutPicture*{%
    \put(0,0){%
    \parbox[t][\paperheight]{\paperwidth}{%
        \vspace{-29.6cm}
        \includegraphics[width=\paperwidth,height=\paperheight,keepaspectratio]{\vdirecole/image-fond-dos}%
    }}
    \put(0,0){%
    \parbox[t][\paperheight]{\paperwidth}{%
        \vspace{-14.5cm}
        \includegraphics[width=\paperwidth,height=\paperheight,keepaspectratio]{\vdirecole/image-fond-dos2}%
    }}
}
\hspace{9mm}
\displayLogos
}

\newcommand{\titleFR}[1]{%
\vspace{1cm}
{\centering \noindent \textcolor{color-ecole}{\rule{\textwidth}{0.2cm}}}
\vspace{-1cm}
\selectlanguage{french}
\section*{\selectfontbackcover\smallTwelve \textcolor{color-ecole}{Titre : }{\selectfontbackcover\mdseries{#1}}} % In this particular case, font style needs to get re-selected locally
}

\newcommand{\keywordsFR}[1]{%
\vspace{-0.2cm}
\noindent{\smallTwelve \textbf{Mot cl\'{e}s : }#1}
}

\newcommand{\abstractFR}[1]{%
\vspace{-0.2cm}
\begin{multicols}{2}
\begin{spacing}{1}
	\noindent\smallTwelve \textbf{R\'{e}sum\'{e} : }#1
\end{spacing}
\end{multicols}
}

\newcommand{\titleEN}[1]{%
\vspace{0.5cm}
{\centering \noindent \textcolor{color-ecole}{\rule{\textwidth}{0.2cm}}}
\vspace{-1cm}
\selectlanguage{english}
\section*{\selectfontbackcover\smallTwelve \textcolor{color-ecole}{Title: }{\selectfontbackcover\mdseries{#1}}} % In this particular case, font style needs to get re-selected locally
}

\newcommand{\keywordsEN}[1]{%
\vspace{-0.2cm}
\noindent{\smallTwelve \textbf{Keywords: }#1}
}

\newcommand{\abstractEN}[1]{%
\vspace{-0.2cm}
\begin{multicols}{2}
\begin{spacing}{1}
	\noindent\smallTwelve \textbf{Abstract: }#1
\end{spacing}
\end{multicols}
}



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% EN-TETES PAGES %%%%%%%%%%%%%%%%

%%%%%%%%% Pour suppirmer les entetes et pied de page gênants par exemple juste avant un chapitre sur une page de droite
\newcommand{\clearemptydoublepage}{%
   \newpage{\pagestyle{empty}\cleardoublepage}}
%%%% .... et utiliser la commande \clearemptydoublepage juste avant le \chapter


\fancyhf{}                       % on annule le fancy automatique
   

%%%%%%%%%% Gerer les en tetes dans les frontmatter mainmatter et backmatter
\RequirePackage{geometry}
\RequirePackage{etoolbox}

\appto\frontmatter{\pagestyle{fancy}
\renewcommand{\sectionmark}[1]{}
\renewcommand{\chaptermark}[1]{\markboth{\textit{#1}}{}}
\fancyhead[LE,RO]{\small\thepage}
    \fancyhead[RO]{\small\leftmark}   % \rightmark = section courante
    \fancyhead[LE]{\small\leftmark} % \leftmark = chapitre courant
    \fancyfoot[C]{\thepage}               % marque la page au centre
}

\appto\mainmatter{\pagestyle{fancy}
\renewcommand{\sectionmark}[1]{\markright{\textit{\thesection.\ #1}}}
\renewcommand{\chaptermark}[1]{\markboth{\chaptername~\thechapter~--\ \textit{#1}}{}}
\fancyhead[LE,RO]{\small\thepage}
    \fancyhead[RO]{\small\rightmark}   % \rightmark = section courante
    \fancyhead[LE]{\small Partie~\thepart, \leftmark} % \leftmark = chapitre courant
    \fancyfoot[C]{\thepage}               % marque la page au centre
}     

\appto\backmatter{\pagestyle{fancy}
\renewcommand{\sectionmark}[1]{\markright{\thesection.\ #1}}
\renewcommand{\chaptermark}[1]{\markboth{\chaptername~\thechapter~--\ #1}{}}
\fancyhead[LE,RO]{\small\thepage}
    \fancyhead[RO]{}   % \rightmark = section courante
    \fancyhead[LE]{} % \leftmark = chapitre courant
    \fancyfoot[C]{\thepage}               % marque la page au centre

} 



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% FAIRE DES ENCARTS %%%%%%%%%%%%%%%%

\definecolor{fondtitre}{RGB}{85,85,85}
\definecolor{fonddeboite}{RGB}{232,232,232}
\definecolor{shadecolor}{RGB}{232,232,232}

\ProcessOptions

%%%%%%%%%% Boitemagique  %%%%%%%%%%%%%%%%%%%%%%%%%%
\newcommand*{\boitemagique}[2]{
\begin{center}
\begin{tikzpicture}
% la boite
\node[rectangle,draw=fondtitre!100,fill=fonddeboite!100,inner sep=10pt, inner ysep=20pt] (mabox)
{
\begin{minipage}{12cm}
#2
\end{minipage}
};
% le titre de la boite
\node[fill=fondtitre!100, text=white, rectangle] at (mabox.north) {\sffamily\textbf{#1}};
\end{tikzpicture}
\end{center}
}

%%%%%% boitesimple %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\newcommand*{\boitesimple}[1]{%
\begin{center}
\begin{minipage}{12cm}
\begin{shaded}
	#1
\end{shaded}
\end{minipage}
\end{center}
}
