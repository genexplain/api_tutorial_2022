# Overview of R, Java and JSON interfaces

## Main API methods

The programming and JSON interfaces provide a common set of functionality to carry out data 
management and analysis tasks. The [following table](#methods-in-r-java-apis-and-json-interface) gives an
overview of available functions.

### Methods in R, Java APIs and JSON interface

| geneXplainR            | genexplain-api Java method                 | JSON executor                       | Description |
|------------------------|--------------------------------------------|-------------------------------------|----------------------------------|
| gx.analysis            | GxHttpClient.analyze (`isWorkflow: false`) | analyze (`workflow: false`)         | Runs a platform analysis method with specified parameters |
| gx.analysis.list       | GxHttpClient.listApplications              | apps, listItems (`type applications`)     | Lists available platform tools |
| gx.analysis.parameters | GxHttpClient.getAnalysisParameters         | itemParameters (`type application`) | Lists parameters for a platform tool |
| gx.createFolder        | GxHttpClient.createFolder                  | createFolder                        | Creates a folder in the platform workspace |
| gx.createProject       | GxHttpClient.createProject                 |                                     | Creates a project in the platform workspace |
| gx.delete              | GxHttpClient.deleteElement                 |                                     | Deletes specified folder or data item   |
| gx.export              | GxHttpClient.export                        | export                              | Exports data using a dedicated exporter |
| gx.export.parameters   | GxHttpClient.getExporterParameters         | exporter (with specified `exporter`), itemParameters (`type exporter`)    | Lists parameters for an exporter and the export item |
| gx.exporters           | GxHttpClient.listExporters                 | exporter, listItems (`type exporters`)        | Lists available exporters |
| gx.get                 | GxHttpClient.getTable                      | get                                 | Gets specified table item |
| gx.import              | GxHttpClient.imPort                        | imPort                              | Imports a data file using a dedicated importer |
| gx.import.parameters   | GxHttpClient.getImporterParameters         | importer (with specified `importer`), itemParameters (`type importer`)    | Lists parameters for an importer and the import folder |
| gx.importers           | GxHttpClient.listImporters                 | importer, listItems (`type importers`)        | Lists available importers |
| gx.isElement           | GxHttpClient.existsElement                 |                                     | Checks whether a workspace element exists given its platform path |
| gx.job.info            | GxHttpClient.getJobStatus                  | jobStatus                           | Gets the status for a job id |
| gx.login               | GxHttpConnection.login                     |                                     | Signs into a platform account to start a session |
| gx.logout              | GxHttpConnection.logout                    |                                     | Signs out of a platform account to end a session |
| gx.ls                  | GxHttpClient.list                          |                                     | Lists contents of a platform folder |
| gx.put                 | GxHttpClient.putTable                      | put                                 | Uploads a data table into specified folder |
| gx.workflow            | GxHttpClient.analyze (`isWorkflow: true`)  | analyze (`workflow: true`)          | Runs a platform workflow |


### geneXplainR-specific functions

The following functions were specifically implemented in the geneXplainR package.

| Function                              | Description                              |
|---------------------------------------|------------------------------------------|
| gx.classifyChipSeqTargets             | Runs the workflow [ChIP-Seq - Identify and classify target genes (TRANSPATH(R))](https://platform.genexplain.com/#de=analyses/Workflows/TRANSPATH/ChIP-Seq%20-%20Identify%20and%20classify%20target%20genes%20(TRANSPATH(R)))|
| gx.ebarraysWorkflow                   | Runs the workflow [Compute differentially expressed genes using EBarrays](https://platform.genexplain.com/#de=analyses/Workflows/Common/Compute%20differentially%20expressed%20genes%20using%20EBarrays) |
| gx.enrichedTFBSGenes                  | Runs the workflow [Identify enriched motifs in promoters (TRANSFAC(R))](https://platform.genexplain.com/#de=analyses/Workflows/TRANSFAC/Identify%20enriched%20motifs%20in%20promoters%20(TRANSFAC(R))) |
| gx.enrichedUpstreamAnalysis           | Runs the workflow [Enriched upstream analysis (TRANSFAC(R) and TRANSPATH(R))](https://platform.genexplain.com/#de=analyses/Workflows/TRANSFAC%20and%20TRANSPATH/Enriched%20upstream%20analysis%20(TRANSFAC(R)%20and%20TRANSPATH(R))) |
| gx.explainMyGenes                     | Runs the workflow [Explain my genes](https://platform.genexplain.com/#de=analyses/Workflows/Common/Explain%20my%20genes) |
| gx.importBedFile                      | Imports a BED file into the platform workspace |
| gx.importTable                        | Imports a data table into the platform workspace |
| gx.limmaWorkflow                      | Computes differentially expressed genes between all condition pairs using Limma |
| gx.mapGenesToOntologies               | Runs the workflow [Mapping to ontologies (Gene table)](https://platform.genexplain.com/#de=analyses/Workflows/Common/Mapping%20to%20ontologies%20(Gene%20table)) |
| gx.searchRegulators                   | Searches for signal transduction regulators of input proteins |
| gx.trackToGeneSet                     | Maps one or more tracks to genes of the most recent Ensembl release |
| gx.upstreamAnalysisTransfacGeneWays   | Runs the workflow [Upstream analysis (TRANSFAC(R) and GeneWays)](https://platform.genexplain.com/#de=analyses/Workflows/TRANSFAC/Upstream%20analysis%20(TRANSFAC(R)%20and%20GeneWays)) |
| gx.upstreamAnalysisTransfacTranspath  | Runs the workflow [Upstream analysis (TRANSFAC(R) and TRANSPATH(R))](https://platform.genexplain.com/#de=analyses/Workflows/TRANSFAC%20and%20TRANSPATH/Upstream%20analysis%20(TRANSFAC(R)%20and%20TRANSPATH(R))) |
| gx.vennDiagrams                       | Creates a Venn diagram for up to three tables |


### genexplain-api-specific Java method

The Java API provides the method `com.genexplain.api.core.GxHttpClient.importTable` to conveniently 
upload a data table into the platform workspace.


## Commandline applications of genexplain-api

Contents of this section can also be found in the [genexplain-api documentation](https://genexplain.github.io/genexplain-api/). 
To focus on general utilities, this presentation does not cover the *example* and *regulator-search* applications
which are described [here](https://genexplain.github.io/genexplain-api/json_interface/#example-example-applications-using-the-java-api) and 
[here](https://genexplain.github.io/genexplain-api/json_interface/#regulator-search-regulator-and-effector-search-tool).

### apps - Listing available tools

The application _apps_ produces a listing of the available analysis tools on a certain server. It takes a single argument that specifies
a file containing a JSON object with several properties of which only the _server_ property is required. Example output:

```Bash
genexplain-api$ java -jar build/libs/genexplain-api-1.0.jar apps input.json 
INFO  com.genexplain.api.app.APIRunner - Running command apps
Data manipulation/Annotate diagram
Data manipulation/Annotate table
Data manipulation/Annotate track with genes
Data manipulation/Composite module to proteins
Data manipulation/Convert table
Data manipulation/Convert table to track
Data manipulation/Convert table via homology
Data manipulation/Create folder
Data manipulation/Create miRNA promoters
Data manipulation/Create random track
Data manipulation/Create tissue-specific promoter track
Data manipulation/Create transcript region track
Data manipulation/Filter one track by another
Data manipulation/Filter table
Data manipulation/Filter track by condition
Data manipulation/Gene set to track
[...]
```

The parameters that can be specified with the JSON input file are described in the following table.

| Parameter                                    | Description                                           |
|----------------------------------------------|-------------------------------------------------------|
| **server**                                   | Server URL to connect to                              |
| **user**                                     | Username to use for connection                        |
| **password**                                 | Password that belongs to the username                 |
| **withParameters**                           | If `true` will produce tsv-table of tools and their parameters |
| **withGalaxy**                               | If `true` will also include tools integrated from Galaxy |
| **outfile**                                  | Output file. Prints to standard output if `outfile` is absent or empty |
| **connection**                               | Package and name to locate a Java class to use for connection. This class must implement _com.genexplain.api.core.GxHttpConnection_. |
| **client**                                   | Package and name to locate a Java class to use as platform client. This class must implement _com.genexplain.api.core.GxHttpClient_. |



### parameters - Parameter descriptions for platform and Galaxy tools

The _parameters_ application fetches parameter descriptions in JSON format for one or more tools from a platform server. It takes a single argument that specifies
a file containing a JSON object with several properties of which only the _server_ property is required. The output is a JSON object containing the tool names
as keys as the retrieved parameter descriptions as values.


| Parameter                                    | Description                                           |
|----------------------------------------------|-------------------------------------------------------|
| **server**                                   | Server URL to connect to                              |
| **user**                                     | Username to use for connection                        |
| **password**                                 | Password that belongs to the username                 |
| **tools**                                    | JSON array with tool names                            |
| **outfile**                                  | Output file. Prints to standard output is `outfile` is absent or empty |
| **connection**                               | Package and name to locate a Java class to use for connection. This class must implement _com.genexplain.api.core.GxHttpConnection_. |
| **client**                                   | Package and name to locate a Java class to use as platform client. This class must implement _com.genexplain.api.core.GxHttpClient_. |



### importer and exporter - Descriptions of data importers, exporters and their parameters

The _importer_ and _exporter_ tools present lists of available data importers or exporters or parameters
for specified importer/exporter types. If provided with the _path_ as well as _importer_ or _exporter_ names,
the platform parameters for the specified type on given path are extracted. Otherwise (one of the parameters missing or empty)
the list of available importers and exporters is printed to standard output or, if an _outfile_ is specified, to
an output file.

| Parameter                                    | Description                                           |
|----------------------------------------------|-------------------------------------------------------|
| **server**                                   | Server URL to connect to                              |
| **user**                                     | Username to use for connection                        |
| **password**                                 | Password that belongs to the username                 |
| **path**                                     | Platform path. Exporters: the item to export, Importers: the path (usually a data folder) to import to |
| **importer/exporter**                        | Name of the importer/exporter to get parameters for   |
| **outfile**                                  | Output file. Prints to standard output is `outfile` is absent or empty |
| **connection**                               | Package and name to locate a Java class to use for connection. This class must implement _com.genexplain.api.core.GxHttpConnection_. |
| **client**                                   | Package and name to locate a Java class to use as platform client. This class must implement _com.genexplain.api.core.GxHttpClient_. |



### exec - Executing tasks using the JSON interface

The _exec_ application provides a rich interface to interact with platform servers using JSON documents to configure tasks. 
It is possible to create complex workflows including re-usable workflow templates, loops and conditional branch points.
The [Hello world-tutorial](https://genexplain.github.io/genexplain-api/json_hello_world/) demonstrates several ways of how to make use of this interface.
The _exec_ interface provides a set of executor functions that can be invoked within a task list. The available executors
are described in detail below.

| Parameter                                    | Description                                           |
|----------------------------------------------|-------------------------------------------------------|
| **server**                                   | Server URL to connect to                              |
| **user**                                     | Username to use for connection                        |
| **password**                                 | Password that belongs to the username                 |
| **verbose**                                  | Set `true` get more progress info                     |
| **reconnect**                                | Set `true` to allow attempts to reconnect in case the connection is interrupted |
| **connection-class**                         | Package and name of a Java class that implements com.genexplain.api.core.GxHttpConnection and will be used instead of the standard class |
| **client-class**                             | Package and name of a Java class that implements com.genexplain.api.core.GxHttpClient and will be used instead of the standard class |
| **credentials**                              | A file containing a JSON object with `user` and `password` properties |
| **withoutConnect**                           | Set `true` to execute tasks without connecting to a platform server |
| **replaceStrings**                           | A JSON array of two-element arrays defining string replacements |
| **loadTasks**                                | A JSON object of task templates or an array of file paths containing task template definitions |
| **nextTask**                                 | A JSON object or array of the next task to perform |



#### Main executors

The following table describes available executors. Most of them correspond to public methods of the [Java API](#methods-in-r-java-apis-and-json-interface), 
but some are only provided by the JSON interface, e.g. _branch_ or _external_. 
Each executor and its JSON configuration is described in more detail in the following sections. 
Their usage is also demonstrated by several examples.

| Name                                         | Description                                           |
|----------------------------------------------|-------------------------------------------------------|
| **analyze**                                  | Calls the analysis method with specified parameters   |
| **branch**                                   | Executes a branch point                               |
| **createFolder**                             | Creates a folder                                      |
| **export**                                   | Export data using a dedicated exporter                |
| **external**                                 | Runs an external tool                                 |
| **get**                                      | Gets specified table                                  |
| **imPort**                                   | Import a data file using a dedicated importer         |
| **itemParameters**                           | Lists parameters for specified application, importer, or exporter |
| **jobStatus**                                | Gets the status for a job id                          |
| **listItems**                                | Lists available application, importer, or exporter items |
| **list**                                     | Lists contents of specified folder                    |
| **put**                                      | Puts table into specified folder                      |
| **setParameters**                            | Sets/adds/removes parameter strings                   |


#### analyze

The _analyze_ executor runs an analysis tool or workflow on the remote server.

- **method** - Name of the platform tool or workflow, where orkflows are specified by their platform path.
- **parameters** - A JSON object with parameters of the analysis tool or workflow.
- **workflow** - Set `true` if the called method is a workflow.
- **wait** - Set `true` to wait for the analysis to finish.
- **progress**  - Set `true` to obtain more progress information.

```JSON
{
	"do":         "analyze",
	"method":     "name of tool or path to workflow",
	"parameters": { },
	"workflow":   false,
	"wait":       false,
	"progress":   false
}
```

#### branch

Selects the next task or task set using a branch selector. 

- **branchSelector** - The canonical name of the Java class that implements the executor.

The JSON document can contain further properties that configure the selector. In addition, the JSON configuration that was used to invoke the _exec_ application is handed to the selector. Please see example selector implementations in this source repository.

```JSON
{
	"do":             "branch",
	"branchSelector": "com.branch.selector.Class",
	"other parameters": "further properties used by the selector"
}
```

#### createFolder

- **path** - Path of the parent folder.
- **name** - Name of the new folder.

```JSON
{
	"do":   "createFolder",
	"path": "platform path",
	"name": "name of new folder"
}
```


#### export

Exports an item from the platform workspace to a local file.

- **file** - Local file to create for the export.
- **path** - Path of the platform item to export.
- **exporter** - Name of the exporter to apply, e.g. `Tab-separated text (*.txt)` for a text table.
- **parameters** - Parameters to be specified to the exporter.

```JSON
{
	"do":         "export",
	"file":       "local file path to store export",
	"path":       "platform path to export",
	"exporter":   "name of exporter",
	"parameters": "JsonObject with exporter parameters"
}
```

#### external

This executor invokes an external program, e.g. a C++ application or an R script.

- **bin** - The command to be executed.
- **params** - List of parameters to be specified to the external program.
- **showOutput** - Set `true` to get output of the external program printed to standard output.

```JSON
{
	"do":         "external",
	"bin":        "command to execute",
	"params":     "simple string or array of parameters to add to commandline",
	"showOutput": "set true to observe standard output of invoked tool"
}
```

#### get

Downloads a table from the platform workspace and optionally stores it in a local file and/or prints it to standard output.

- **table** - Platform path of the table to download.
- **toFile** - Path of file to which to write table.
- **toStdout** - Set `true` to get table printed on standard output.

```JSON
{
	"do":    "get",
	"table": "platform path of table to download",
	"toFile": "path of local file",
	"toStdout": "to print table object to standard output"
}
```

#### imPort

Imports a local file to the platform workspace.

- **file** - Local file to import.
- **path** - Platform path including the name of imported item.
- **importer** - The importer to apply, e.g. `Tabular (*.txt, *.xls, *.tab, etc.)` for tables.
- **parameters** - Parameters for the importer.

```JSON
{
	"do":         "imPort",
	"file":       "local file to import",
	"path":       "the designated import location in the platform",
	"importer":   "name of importer",
	"parameters": "JsonObject with importer parameters"
}
```

#### itemParameters

This is a collectiv function that can get information about the available parameters for an analysis tool (application), exporter or importer. If the _item_ is an exporter or importer, one needs to specify the corresponding target platform path to export or import.

- **name** - Item name which may be the name of an analysis tool, an exporter or an importer name.
- **type** - Type of item, one of application, exporter or importer.
- **path** - If the item is an exporter or importer, the corresponding platform path is required to determine possible context-dependent parameters.

```JSON
{
	"do":   "itemParameters",
	"name": "name of item for which to get parameters",
	"type": "type of item for which to get parameters: application, exporter, importer",
	"path": "if exporter or importer, path for which to get parameters in context with ex-/importer"
}
```

#### jobStatus

Returns the status of a running analysis job.

- **jobId** - Id of the job whose status is requested.
- **toFile** - Path of file to which to write output.
- **toStdout** - Set `true` to get output printed on standard output.

```JSON
{
	"do":     "jobStatus",
	"jobId":  "id of platform job to request status",
	"toFile": "path of local file",
	"toStdout": "to print output to standard output"
}
```

#### listItems

Gets listings of available applications, importers or exporters.

- **type** - Type of items to list, one of application, exporter or importer.
- **toFile** - Path of file to which to write output.
- **toStdout** - Set `true` to get output printed on standard output.

```JSON
{
	"do":   "listItems",
	"type": "type of items to list: applications, importers, or exporters"
	"toFile": "path of local file",
	"toStdout": "to print output to standard output"
}
```

#### list

Gets the listing of specified folder.

- **folder** - Platform folder to get listing for.
- **toFile** - Path of file to which to write output.
- **toStdout** - Set `true` to get output printed on standard output.

```JSON
{
	"do":     "list",
	"folder": "platform folder to get listing for"
	"toFile": "path of local file",
	"toStdout": "to print output to standard output"
}
```

#### put

Uploads a table from specified local file into the platform workspace.

- ***file*** - Local file with data table to upload.
- ***skip*** - Number of lines to skip in the beginning of the file.
- ***delimiter*** - Column delimiter string.
- ***table*** - JSON array of arrays with data columns. Note that the table has no title row, but columns are specified separately.
- ***columns*** - Column definition by an array of two-element arrays giving column name and type, where type is one of `Integer, Float, Boolean, or Text`.
- ***path*** - Platform path to put table which includes the name of the table item in the platform workspace.

```JSON
{
	"do":        "put",
	"file":      "file with data table to put into platform",
	"skip":      "number of lines to skip in the beginning of input file",
	"delimiter": "delimiter of table columns in input file",
	"table":     "array of arrays with data to put into platform",
	"columns":   "array of two-element arrays specifying column names and type. The latter can be Integer, Float, Boolean, Text",
	"path":      "destination path of table in platform"
}
```

#### setParameters

Sets/adds/removes string replacements in the parameter object that will modify parameters of subsequent tasks.

- ***set*** - a JSON object whose keys can be found in the _replaceStrings_ array of the parameter object and whose value will replace the current one.
- ***remove*** - a JSON object whose keys will be removed from the _replaceStrings_ array of the parameter object.
- ***before*** - an array of two-element arrays that are inserted in the beginning of the _replaceStrings_ array.
- ***after*** - an array of two-element arrays that are appended to the _replaceStrings_ array.

```JSON
{
	"do":     "setParameters",
	"set":    "sets existing parameter to specified value",
	"remove": "removes existing parameter",
	"before": "add parameter before others in the parameter array",
	"after":  "add parameter at the end of the parameter array"
}
```
