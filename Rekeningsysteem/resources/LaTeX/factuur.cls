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

\newenvironment{aangenomenTable}[1][]{
	\newcommand*{\aangenomenRow}[4]{
		##1 & ##2 & ##3 & ##4 \\
	}

	\newcommand*{\subtotalRow}[1]{
		\hline\hline\hline
		{\bf Subtotaal} & & & ##1 \\
	}

	\newcommand*{\btwRow}[4]{
%		\ifthenelse{\equal{##1}{##3}
%		}{
%			{\bf BTW ##1\%} & & & \FPeval{\result}{round((##2+##4):2)} \format{\result} \\
%		}{
			{\bf BTW materiaal ##1\%} & & & ##2 \\
			{\bf BTW arbeid ##3\%} & & & ##4 \\
%		}
	}

	\newcommand*{\totalRow}[1]{
		{\bf Totaal in #1} & & & ##1 \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{8.1cm} r r r}
		\hline\hline
		{\bf OMSCHRIJVING} & {\bf ARBEID} & {\bf MATERIAAL} & {\bf TOTAAL} \\*
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
		\hline\hline\hline
		{\bf Totaal in euro} & & ##1 \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{3cm} p{1.5cm} r}
		\hline\hline
		{\bf OMSCHRIJVING} & {\bf NUMMER} & {\bf PRIJS} \\*
		\hline\hline
		\endhead
}{
	\hline\hline\hline
	& \multicolumn{2}{r}{BTW verlegd} \\
	\end{longtable}
}

\newenvironment{particulierTable}[1][]{
	\newcommand*{\particulierRow}[5]{
		##1 & ##2 & ##3 & ##4 & ##5 \\
	}

	\newcommand*{\loonRow}[4]{
		& ##1 & ##2 & ##3 & ##4 \\
	}

	\newcommand*{\subtotalRow}[1]{
		\hline\hline\hline
		& {\bf Subtotaal} & & & ##1 \\
	}

	\newcommand*{\btwRow}[4]{
%		\ifthenelse{\equal{##1}{##3}
%		}{
%			& {\bf BTW ##1\%} & & & \FPeval{\result}{round((##2+##4):2)} \format{\result} \\
%		}{
			& {\bf BTW materiaal ##1\%} & & & ##2 \\
			& {\bf BTW arbeid ##3\%} & & & ##4 \\
%		}
	}

	\newcommand*{\totalRow}[1]{
		& {\bf Totaal in #1} & & & ##1 \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{1.5cm} p{6cm} p{1cm} p{1cm} r}
		\hline\hline
		{\bf ARTIKELNR.} & {\bf OMSCHRIJVING} & {\bf AANTAL} & {\bf EENHEID} & {\bf TOTAAL} \\*
		\hline\hline
		\endhead
}{
	\hline\hline\hline
	\end{longtable}
}

\newenvironment{reparatiesTable}[1][]{
	\newcommand*{\reparatiesRow}[5]{
		##1 & ##2 & ##3 & ##4 & ##5 \\
	}

	\newcommand*{\totalRow}[1]{
		\hline\hline\hline
		{\bf Totaal in #1} & & & & ##1 \\
	}

	\renewcommand{\tabcolsep}{3ex}
	\setlength\LTleft{0pt}
	\setlength\LTright{0pt}
	\begin{longtable}[c]{@{\extracolsep{\tabcolsep}} p{3cm} p{1.5cm} r r r}
		\hline\hline
		{\bf OMSCHRIJVING} & {\bf NUMMER} & {\bf LOON} & {\bf MATERIAAL} & {\bf TOTAAL} \\*
		\hline\hline
		\endhead
}{
	\hline\hline\hline
	& & & \multicolumn{2}{r}{BTW verlegd} \\
	\end{longtable}
}
