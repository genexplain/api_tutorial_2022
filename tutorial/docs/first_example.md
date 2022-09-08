# A first example analysis - Enrichment of gene functions in a gene set

## Example overview

The analysis consists of the following steps.

1. Prepare a project and a data folder in the platform workspace as a container for analysis data and results
2. Upload a gene set
3. Analyze enrichment of gene function using the *Functional classification* tool


## Platform tool parameters

Platform tools typically require a set of input parameters, e.g. path of input data, a path for
result output, species selection. The APIs provide functions to gather information about
available input options for analysis tools as well as for import and export functions.

Here we show how to use the *genexplain-api* commandline tool [*parameters*](api_overview.md#parameters-parameter-descriptions-for-platform-and-galaxy-tools) to obtain information
about the *Functional classification* platform tool that is going to be applied to calculate
gene functional enrichment in this example analysis.

The console command to run the tool looks as follows. The command invokes the *jar* package of
genexplain-api with the name of the *parameters* tool followed by the path of a JSON file.

```bash
java -jar genexplain-api.jar parameters funclass_parameter_listing.json
```

The JSON file contains a single JSON object with four properties *server*, *user*, *password* and
*tools*. The first three properties configure the platform server to connect with as well as username
and password which need to be replaced with valid account credentials on that server. The *tools*
list contains the name(s) of platform tools for which the program shall fetch parameter information.

```JSON
{
    "server":          "https://platform.genexplain.com",
    "user":            "someuser@email.io",
    "password":        "12345",
    "tools":           ["Functional classification"]
}
```

The [output](#output-for-the-functional-classification-tool) is printed to standard output (optionally, an additional JSON parameter `outfile` can
be provided to specify a local output file) and is (partially) shown below. It consists of a JSON object with
one property for each platform tool that was specified in the *tools* list.

The input options of a platform tool are contained in a JSON array, each represented by an object describing
the input option. The order of input parameters in this array is the same as in the graphical web interface.

The `displayName` property shows the parameter title that appears in the graphical web interface. However,
the parameter name that needs to be specified in an API call is the `name` property.
E.g. the first parameter with `displayName` *Source data set* has the `name` *sourcePath*. The latter
needs to be used in function calls.

An input option object provides further valuable information such as the type of data that is expected. E.g.
the *Source data set* needs to be provided as a `type` *data-element-path*, which means a path for a
data element in the platform workspace, the data element on that path must be a table (`elementClass` *...TableDataCollection*),
and the table row names must be [Ensembl](https://ensembl.org) ids.

If an input option expects a value from a predefined list, the available values to select are also listed.
E.g. the second option *Species* expects one of the species strings listed in `dictionary` and the
default value is given in `value` (*Human (Homo sapiens)*).

The *geneXplainR* method to fetch parameter information for platform tools is implemented in the
function `gx.analysis.parameters`. However, the *parameters* commandline tool provides more details
about each input option. Therefore, we recommend its use also for geneXplainR projects.


### Output for the Functional classification tool

```JSON
{
    "Functional classification": [
        {
            "displayName": "Source data set",
            "icon": "biouml.plugins.ensembl:biouml/plugins/ensembl/tabletype/resources/genes-ensembl.gif",
            "description": "Input table having Ensembl genes as rows.",
            "referenceType": "Genes: Ensembl",
            "readOnly": false,
            "type": "data-element-path",
            "canBeNull": false,
            "promptOverwrite": false,
            "name": "sourcePath",
            "elementClass": "ru.biosoft.table.TableDataCollection",
            "value": "(select element)",
            "elementMustExist": true,
            "multiSelect": false
        },
        {
            "dictionary": [
                [
                    "Human (Homo sapiens)",
                    "Human (Homo sapiens)"
                ],
                [
                    "Mouse (Mus musculus)",
                    "Mouse (Mus musculus)"
                ],
                [
                    "Rat (Rattus norvegicus)",
                    "Rat (Rattus norvegicus)"
                ],
                [
                    "Baker's yeast (Saccharomyces cerevisiae)",
                    "Baker's yeast (Saccharomyces cerevisiae)"
                ],
                [
                    "Fission yeast (Schizosaccharomyces pombe)",
                    "Fission yeast (Schizosaccharomyces pombe)"
                ]
            ],
            "displayName": "Species",
            "name": "species",
            "description": "Species corresponding to the input table.",
            "readOnly": false,
            "type": "code-string",
            "value": "Human (Homo sapiens)"
        },
        {
            "dictionary": [
                [
                    "Full gene ontology classification",
                    "Full gene ontology classification"
                ],
                [
                    "GO (biological process)",
                    "GO (biological process)"
                ],
                [
                    "GO (cellular component)",
                    "GO (cellular component)"
                ],
                [
                    "GO (molecular function)",
                    "GO (molecular function)"
                ],
                [
                    "HumanCyc pathways",
                    "HumanCyc pathways"
                ],
                [
                    "HumanPSD(TM) GO (biological process) (2022.1)",
                    "HumanPSD(TM) GO (biological process) (2022.1)"
                ],
                [
                    "HumanPSD(TM) disease (2022.1)",
                    "HumanPSD(TM) disease (2022.1)"
                ],
                [
                    "Reactome pathways (74)",
                    "Reactome pathways (74)"
                ],
                [
                    "TF classification",
                    "TF classification"
                ],
                [
                    "TRANSPATH Pathways (2022.1)",
                    "TRANSPATH Pathways (2022.1)"
                ],
                [
                    "Repository folder",
                    "Repository folder"
                ]
            ],
            "displayName": "Classification",
            "name": "bioHub",
            "description": "Classification you want to use. List of classifications may differ depending on software version and your subscription. Use 'Repository folder' for custom classification.",
            "readOnly": false,
            "type": "code-string",
            "value": "Full gene ontology classification"
        },
        {
            "displayName": "Minimal hits to group",
            "name": "minHits",
            "description": "Groups with lower number of hits will be filtered out (nmin)",
            "readOnly": false,
            "type": "code-string",
            "value": "2"
        },
        {
            "displayName": "Only over-represented",
            "name": "onlyOverrepresented",
            "description": "If checked, under-represented groups will be excluded from the result",
            "readOnly": false,
            "type": "bool",
            "value": "true"
        },
        {
            "displayName": "P-value threshold",
            "name": "pvalueThreshold",
            "description": "P-value threshold (Pmax)",
            "readOnly": false,
            "type": "code-string",
            "value": "0.05"
        },
        {
            "auto": "on",
            "displayName": "Result name",
            "icon": "biouml.plugins.enrichment:biouml/plugins/enrichment/resources/classify.gif",
            "description": "Name and path for the resulting table",
            "readOnly": false,
            "type": "data-element-path",
            "canBeNull": false,
            "promptOverwrite": false,
            "name": "outputTable",
            "elementClass": "ru.biosoft.table.TableDataCollection",
            "value": "(select element)",
            "elementMustExist": false,
            "multiSelect": false
        }
    ]
}
```



## Functional classification using geneXplainR

The R code of this section is provided with the tutorial material as
*first_example.R*. Please note that some parts require editing before
running the script, including username, password, project name.

### Folder preparation

In the R script we firstly load the `geneXplainR` library and log into
a geneXplain platform account. Then we prepare a project as well as a data folder
for the analysis files. The following source code also shows the function `gx.ls`
that lists the data elements of a specified folder. The *data/Projects* path
contains all projects and associated data folders. The path of a data folder within a project
follows the pattern *data/Projects/&lt;project name&gt;/Data/&lt;folder name&gt;* .

```R
library(geneXplainR)

# Username (email) and password need to be replaced with valid credentials.
#
gx.login("https://platform.genexplain.com", "someuser@email.io", "12345")

# The gx.ls function returns a listing of the specified workspace folder.
#
gx.ls("data/Projects")

# The project name has to be unique on the connected platform server.
#
apiProjectName <- "api2022_tutorial"
apiProjectPath <- paste0("data/Projects/", apiProjectName)

# The gx.createProject function returns an error if the project already
# exists.
#
gx.createProject(apiProjectName, description = "API 2022 tutorial project")

funclassFolder <- "first_example_functional_class"
folderPath     <- paste0(apiProjectPath, "Data/", funclassFolder)

# The gx.createFolder function creates the folder if it does not
# already exist.
#
gx.createFolder(paste0(apiProjectPath, "Data"), funclassFolder)
```


### Data upload

We import the gene list file using the `gx.importTable` function. This is a
specialized function for tabular data. The `gx.import` function can be used 
to import many different data types and formats. The `gx.importers` function
shows available importers and the `gx.import.parameters` function provides
information about importer parameters.

```R
species <- "Human (Homo sapiens)"
dataName <- "funclass_genes_covid-19"

# The gx.importers function returns a list of available importers
# for different data types.
#
gx.importers()

# The available parameters for a specific importer and a destination
# folder can be interrogated using the gx.import.parameters function.
#
gx.import.parameters(folderPath, "Tabular (*.txt, *.xls, *.tab, etc.)")

# The geneXplainR library provides a specialized function to import tabular
# data files.
#
gx.importTable("../data/functional_classification_genes_GSE156063.tsv", folderPath,
               dataName, columnForID = "ID", 
               tableType = "Genes: Ensembl", species = species)
               
# This is the platform workspace path of the imported data.
#
funclassGenePath <- paste0(folderPath, "/", dataName)
```

### Functional classification

The *Functional classification* analysis is invoked by the `gx.analysis` function. Information
about the elements of the parameter list can be obtained by the *parameters* command line tool described
[above](#platform-tool-parameters) or the `gx.analyis.parameters`.

```R
funclassResultPath <- paste0(funclassGenePath," GO")

# The gx.analysis.parameters function shows input option names and 
# descriptions for a specified platform tool.
#
gx.analyis.parameters("Functional classification")

# The gx.analysis function submits analysis tasks to the connected
# platform server.
#
gx.analysis("Functional classification", list(sourcePath = funclassGenePath,
                                              species    = species,
                                              bioHub     = "Full gene ontology classification",
                                              minHits    = 1,
                                              pvalueThreshold = 1,
                                              outputTable = funclassResultPath))

# The gx.get function loads data tables from the platform workspace into
# a data frame.
#
funclassResult <- gx.get(funclassResultPath)
head(funclassResult)
```



## Functional classification using genexplain-api

### Getting importer types and platform tool parameters

The workflow of this example analysis requires uploading a gene list into a platform workspace. The
gene list is then analyzed using a platform analysis tool. Therefore, we need to know how to import
our gene list and which parameters are needed for the import process as well as for the subsequent
analysis. While in the case of this specific analysis we can make use of a dedicated method to import
of tabular data, here we demonstrate how to find out about *importers* and their parameters as well as
the input parameters for the *Functional classification* tool using genexplain-api library
methods.

*Importers* are platform functions that import specific data types and formats, e.g.
tabular data stored in a text file, into a platform workspace. The list of available importers is
extracted using the `listImporters` method of the `client` object, whereas its `getImporterParameters`
method returns information about parameters of a specific importer, in this the importer for tabular
data. Finally, the `getAnalysisParameters` method retrieves input option specifications for the
analysis tool from the connected platform server.

The code of this section is provided with the tutorial material as
*ToolParameters.java*. Please note that some parts require editing before
running the program, including username, password, project name.

The Java class can be compiled and executed with commands like the
following.

```bash
javac -cp .:genexplain-api.jar ToolParameters.java
java -cp .:genexplain-api.jar ToolParameters
```


```Java
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.PrettyPrint;

import com.genexplain.api.core.GxHttpClient;
import com.genexplain.api.core.GxHttpClientImpl;
import com.genexplain.api.core.GxHttpConnection;
import com.genexplain.api.core.GxHttpConnectionImpl;

import java.io.StringWriter;
import java.io.Writer;


public class ToolParameters {
    public static void main(String[] args) throws Exception {
        
        // The GxHttpConnectionImpl holds the connection to the specified
        // platform server. Username and password need to correspond to a
        // valid account on that server.
        //
        GxHttpConnectionImpl con = new GxHttpConnectionImpl();
        con.setServer("https://platform.genexplain.com");
        con.setUsername("someuser@email.io");
        con.setPassword("12345");
        con.setVerbose(true);
        con.login();
        
        // The connection is given to a client object. The client is the
        // main component to interact with the connected platform server.
        //
        GxHttpClientImpl client = new GxHttpClientImpl();
        client.setConnection(con);
        
        System.out.println("--- Available importers ---");
        
        // The listImporters method returns a JSON object with the list
        // of importers contained in the "values" array.
        //
        JsonObject importers = client.listImporters();
        for (JsonValue imp : importers.get("values").asArray()) {
            System.out.println(imp.asString());
        }
        
        
        System.out.println("--- Importer parameters ---");
        
        // The getImporterParameters method returns a JSON object 
        // containing a description of importer parameters. The
        // importer parameters are requested for a target folder.
        //
        JsonObject importParams = client.getImporterParameters(
                        "data/Projects/api2022_tutorial/Data/first_example_functional_class", 
                        "Tabular (*.txt, *.xls, *.tab, etc.)");
        Writer writer = new StringWriter();
        importParams.writeTo(writer, PrettyPrint.indentWithSpaces(4));
        System.out.println(writer.toString());
        
        
        System.out.println("--- Functional classification parameters ---");
        
        // The getAnalysisParameters method returns a JSON object 
        // containing a description of analysis tool parameters.
        //
        JsonObject funClassParams = client.getAnalysisParameters("Functional classification");
        Writer writer = new StringWriter();
        funClassParams.writeTo(writer, PrettyPrint.indentWithSpaces(4));
        System.out.println(writer.toString());
        
        con.logout();
    }
}
```


### Java program for functional classification

The code of this section is provided with the tutorial material as
*FirstExample.java*. Please note that some parts require editing before
running the program, including username, password, project name.

The Java class can be compiled and executed with commands like the
following.

```bash
javac -cp .:genexplain-api.jar FirstExample.java
java -cp .:genexplain-api.jar FirstExample
```

In the Java program we import the core classes and interfaces from `com.genexplain.api.core` and log
into a geneXplain platform account. Then we prepare a project as well as a data folder
for the analysis files. The *data/Projects* path contains all projects and associated data folders.
The path of a data folder within a project follows the pattern 

*data/Projects/&lt;project name&gt;/Data/&lt;folder name&gt;* .

We import the gene list file using the `importTable` function. This is a specialized function for 
tabular data. The *Functional classification* analysis is invoked by the `analyze` method of the
client. Information about the elements of the parameter object can be obtained by the *parameters*
command line tool described [above](#platform-tool-parameters) or as shown 
[here](#getting-importer-types-and-platform-tool-parameters).

```Java
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.PrettyPrint;

import com.genexplain.api.core.GxHttpClient;
import com.genexplain.api.core.GxHttpClientImpl;
import com.genexplain.api.core.GxHttpConnection;
import com.genexplain.api.core.GxHttpConnectionImpl;

import java.io.StringWriter;
import java.io.Writer;

public class FirstExample {
    public static void main(String[] args) throws Exception {
    
        // The GxHttpConnectionImpl holds the connection to the specified
        // platform server. Username and password need to correspond to a
        // valid account on that server.
        //
        GxHttpConnectionImpl con = new GxHttpConnectionImpl();
        con.setServer("https://platform.genexplain.com");
        con.setUsername("someuser@email.io");
        con.setPassword("12345");
        con.setVerbose(true);
        con.login();
        
        // The connection is given to a client object. The client is the 
        // main component to interact with the connected platform server.
        //
        GxHttpClientImpl client = new GxHttpClientImpl();
        client.setConnection(con);
        
        // The createProject method creates a new project in the user
        // workspace. The project name must be new and unique on the 
        // platform instance. The platform path of the new project is 
        // "data/Projects/<project name>".
        //
        Map<String, String> projectParams = new HashMap<>();
        projectParams.put("user", "someuser@email.io");
        projectParams.put("pass", "12345");
        projectParams.put("project", "api2022_tutorial");
        projectParams.put("description", "API 2022 tutorial project");
        client.createProject(projectParams)
        
        // The createFolder function creates the folder if it does not 
        // already exist.
        //
        client.createFolder("data/Projects/api2022_tutorial/Data", "first_example_functional_class");
        
        // The API provides for a specialized method to import tabular
        // data.
        //
        client.importTable("../data/functional_classification_genes_GSE156063.tsv",
                           "data/Projects/api2022_tutorial/Data/first_example_functional_class",
                           "funclass_genes_covid-19",
                           false, 
                           GxHttpClient.ColumnDelimiter.Tab, 
                           1, 2, "", "ID", false, "Genes: Ensembl", "Human (Homo sapiens)");
        
        // After data import it is often advisable to let background 
        // processes on the server finish their work, before proceeding 
        // with analyzing the uploaded data.
        //
        Thread.sleep(1000);
        
        // The analyze function submits analysis tasks to the connected 
        // platform server.
        //
        JsonObject params = new JsonObject()
            .add("sourcePath", "data/Projects/api2022_tutorial/Data/first_example_functional_class/funclass_genes_covid-19")
            .add("species", "Human (Homo sapiens)")
            .add("bioHub", "Full gene ontology classification")
            .add("minHits", 1)
            .add("pvalueThreshold", 1)
            .add("outputTable", "data/Projects/api2022_tutorial/Data/first_example_functional_class/funclass_genes_covid-19 GO");
        client.analyze("Functional classification", params, false, true, true);
        
        con.logout();
    }
}
```


## Functional classification using the commandline

### Commandline and JSON

The [*exec*](api_overview.md#exec-executing-tasks-using-the-json-interface) application, provided by the genexplain-api JAR package, is 
invoked with a configuration file in JSON format.

```bash
java -jar genexplain-api.jar exec first_example.json
```

The content of the JSON file to run the example analysis workflow (named *first_example.json* in 
the example above) is shown [below](#json-configuration-file-for-functional-classification).
It contains a JSON object with server, username and password parameters as well as a task list.
Please note that `server`, `user`, `password` as well as the project name (*api2022_tutorial* used as example) need
to be replaced with proper values. The `reconnect` property instructs the program to try to reconnect in case of interruption.

The JSON configuration is provided with the tutorial material as
*first_example.json*. Please note that some parts require editing before
running the program, including username, password, project name.

The `tasks` property is a JSON array with the sequence of tasks that *exec*
shall perform. Each task is described by an object that firstly defines
an *executor* using the `do` property. The *exec* application has a set
of executors for different kinds of tasks which typically resemble functions
and methods of the R and Java APIs. The executors are described in detail [here](api_overview.md#main-executors).

The example functional classification analysis uses four executors.

1. The [createFolder executor](api_overview.md#createfolder) creates a folder named `first_example_functional_class`
within the `api2022_tutorial` project.
2. The [imPort executor](api_overview.md#import) imports the gene list file into the analysis folder.
3. After the import, [external](api_overview.md#external) executes an external script to wait for one second,
in order to allow background processes on the platform server to finish their work
before proceeding with the analysis.
4. The [analyze executor](api_overview.md#analyze) invokes the platform tool *Functional classification*.

The script invoked by `external` is a simple shell script:

```bash
#/bin/sh

sleep 1
```

Information about the parameters required for importers and analysis tools
can be obtained using the [*import*](api_overview.md#importer-and-exporter-descriptions-of-data-importers-exporters-and-their-parameters)
and [*parameters*](api_overview.md#parameters-parameter-descriptions-for-platform-and-galaxy-tools) applications, respectively,
Another options is to use the [*itemParameters*](api_overview/#itemparameters) executor with *exec*.
The list of available platform tools is provided by [*apps*](api_overview.md#apps-listing-available-tools) or by the
[*listItems*](api_overview/#listitems) executor of *exec*.

```bash
java -jar genexplain-api.jar import (...)
java -jar genexplain-api.jar parameters (...)
java -jar genexplain-api.jar apps (...)
```


### JSON configuration file for functional classification
```JSON
{
    "server":          "https://platform.genexplain.com",
    "user":            "someuser@email.io",
    "password":        "12345",
    "reconnect":       true,
    "tasks": [
        {
            "do": "createFolder",
            "showOutput": true,
            "path": "data/Projects/api2022_tutorial/Data",
            "name": "first_example_functional_class"
        },
        {
            "do": "imPort",
            "file": "../data/functional_classification_genes_GSE156063.tsv",
            "path": "data/Projects/api2022_tutorial/Data/first_example_functional_class",
            "importer": "Tabular (*.txt, *.xls, *.tab, etc.)",
            "parameters": {
                "columnForID": "ID",
                "tableType":   "Genes: Ensembl",
                "species":     "Human (Homo sapiens)"
            }
        },
        {
            "do": "external",
            "showOutput": true,
            "bin": "sh",
            "params": ["waitns.sh"]
        },
        {
            "do": "analyze",
            "method": "Functional classification",
            "verbose": true,
            "parameters": {
                "sourcePath": "data/Projects/api2022_tutorial/Data/first_example_functional_class/functional_classification_genes_GSE156063",
                "species":    "Human (Homo sapiens)",
                "bioHub":     "Full gene ontology classification",
                "minHits":    1,
                "pvalueThreshold": 1,
                "outputTable": "data/Projects/api2022_tutorial/Data/first_example_functional_class/functional_classification_genes_GSE156063 GO"
            }
        }
    ]
}
```
