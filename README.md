# Rekeningsysteem Bouwbedrijf Mackloet
The purpose of this project is to make an application which can generate invoices and tenders. Once made, these can be saved as an *.xml (raw data) or *.pdf file. The content of an invoice or tender is entered via the applications user interface or drawn from a database.

##Build
To build this project a dependency to JavaLatexReport (*jlr.jar*) is needed. However, this is not a Maven dependency. This jar has to be [downloaded](http://www.nixo-soft.de/en/category/Downloads/page/libs/JavaLatexReport.php) or can be found in this [project's repository](https://github.com/rvanheest/Rekeningsysteem-Bouwbedrijf-Mackloet/blob/master/Rekeningsysteem/lib/jlr.jar) and has to be installed via the following maven command:

`mvn install:install-file -Dfile=<path-to-jar> -DgroupId="jlr" -DartifactId="jlr" -Dversion="0" -Dpackaging="jar" -DlocalRepositoryPath="${basedir}/jlrrepo"`

Here `<path-to-jar>` is the location of the downloaded jar. This creates a private repository that resolves the jlr dependency in this project's `pom.xml`.

To generate the runnable jar-with-dependencies, run `mvn package`.

##Generating PDF
In order to generate the pdf invoices, this application depends on the previously mentioned JavaLatexReport. This library demands you to have LaTeX installed on your computer. During the testing phase of this application, [MikTex](http://miktex.org/) was used. **Install this before running the application.**

##Database
The database will automatically be created when the application is run for the first time. If the database already exists, it is checked whether it is up to date and is updated as necessary.
