# Rekeningsysteem Bouwbedrijf Mackloet
The purpose of this project is to make an application which can generate invoices and tenders. Once made, these can be saved as an *.xml (raw data) or *.pdf file. The content of an invoice or tender is entered via the applications user interface or drawn from a database.

## Build
To build this project a non-Maven dependency (*jlr.jar*) has to be [downloaded](http://www.nixo-soft.de/en/category/Downloads/page/libs/JavaLatexReport.php) or can be found in this [project's repository](https://github.com/rvanheest/Rekeningsysteem-Bouwbedrijf-Mackloet/blob/master/Rekeningsysteem/lib/jlr.jar) and installed via the following maven command:

```maven
mvn install:install-file -Dfile=<path-to-jar> -DgroupId="jlr" -DartifactId="jlr" -Dversion="0" -Dpackaging="jar" -DlocalRepositoryPath="${basedir}/jlrrepo" 
```

Here `<path-to-jar>` is the location of the downloaded jar. This creates a private repository that resolves the jlr dependency in this project's `pom.xml`.

To create the database needed for this project (AppDB.db) you need to execute `org.rekeningsysteem.io.database.InitDB.java`, which constructs the tables and indexes needed for running the application.  
**Note:** the values inserted into the database are mock-ups, which are not used in practice.
