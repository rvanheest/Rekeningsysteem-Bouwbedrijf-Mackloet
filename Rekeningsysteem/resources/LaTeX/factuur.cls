\ProvidesClass{factuur}

\LoadClass[10pt]{article}

\usepackage[a4paper,hmargin=0.5in,vmargin=0.5in]{geometry}
\usepackage[parfill]{parskip} % Do not indent paragraphs
\usepackage{longtable}
\usepackage{calc}
\usepackage{numprint}
\usepackage[nomessages]{fp}
\usepackage{ifthen}
\usepackage{eurosym}

\pagestyle{empty} % No page numbers

\setlength{\doublerulesep}{\arrayrulewidth} % Double rules look like one thick one
\newcommand{\HRule}{\rule{\linewidth}{0.5mm}}
\newcommand*{\BtwNummer}[2]{\ifthenelse{\equal{#1}{true}}{{\bf BTW.nr.:} \> #2}{}}

\newcommand*{\format}[1]{\npdecimalsign{,}\npthousandsep{.}\npthousandthpartsep{}\numprint{#1}}

\newenvironment{particulierTable}[1][]{
	\newcommand*{\particulierRow}[3]{
		##1 & ##2 & ##3\% \\
	}

	\newcommand*{\loonRow}[3]{
		##1 & ##2 & ##3\% \\
	}

	\newcommand*{\subtotalRow}[1]{
		\hline\hline\hline
		{\bf Subtotaal} & ##1 & \\
	}

	\newcommand*{\btwRow}[3]{
		{\bf BTW ##1\% over #1 ##2} & ##3 & \\
	}

	\newcommand*{\totalRow}[1]{
		\hline\hline
		{\bf Totaal} & ##1 & \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{8.1cm} r r}
		\hline\hline\hline
		{\bf OMSCHRIJVING} & {\bf BEDRAG (#1)} & {\bf BTW (\%)} \\*
		\hline\hline
		\endhead
}{
	\hline\hline\hline
	\end{longtable}
}

\newenvironment{offerteTekst}{
	\newcommand*{\ondertekenen}[1]{
		\ifthenelse{\equal{##1}{true}
		}{
			\begin{minipage}[t]{0.45\textwidth}
				Naam: \\ \\
				Datum:
			\end{minipage}
			\hfill
			\begin{minipage}[t]{0.45\textwidth}
				Handtekening voor akkoord:
			\end{minipage}
		}{%empty string
		}
	}
}

\newenvironment{mutatiesTable}[1][]{
	\newcommand*{\mutatiesRow}[3]{
		##1 & ##2 & ##3 \\
	}

	\newcommand*{\totalRow}[1]{
		\hline\hline
		{\bf Totaal} & & ##1 \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{3cm} p{1.5cm} r}
		\hline\hline\hline
		{\bf OMSCHRIJVING} & {\bf NUMMER} & {\bf BEDRAG (#1)} \\*
		\hline\hline
		\endhead
}{
	\hline\hline\hline
	& \multicolumn{2}{r}{BTW verlegd} \\
	\end{longtable}
}

\newenvironment{reparatiesTable}[1][]{
	\newcommand*{\reparatiesRow}[5]{
		##1 & ##2 & ##3 & ##4 & ##5 \\
	}

	\newcommand*{\totalRow}[1]{
		\hline\hline\hline
		{\bf Totaal} & & & & ##1 \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{3cm} p{1.5cm} r r r}
		\hline\hline\hline
		{\bf OMSCHRIJVING} & {\bf NUMMER} & {\bf LOON (#1)} & {\bf MATERIAAL (#1)} & {\bf TOTAAL (#1)} \\*
		\hline\hline
		\endhead
}{
	\hline\hline\hline
	& & & \multicolumn{2}{r}{BTW verlegd} \\
	\end{longtable}
}
