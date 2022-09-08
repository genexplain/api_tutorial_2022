# Platform tools and parameters

There are three main types of platform applications

1. Analysis tools that cover functionality like data preparation, conversion, or data analysis
2. Importers that import certain data types into the platform workspace
3. Exporters that export data from the platform workspace

The programming and commandline interfaces provide functions to list available analysis tools,
importers and exporters for a given platform server and user account and furthermore to
retrieve information about their input options/parameters.

In the following, we focus on geneXplainR and commandline utilities provided by the
genexplain-api package. Corresponding methods of the Java API are listed
[here](api_overview.md#main-api-methods).

We recommend to use genexplain-api commandline tools for tool listings and parameters
also when working mainly with geneXplainR.


## Listing tools and parameters using geneXplainR

The example code below shows the geneXplainR functions to
list available applications and to get information about parameters
for a platform tool, `gx.analysis.list` and `gx.analysis.parameters`.

The output of `gx.analysis.list` is a table containing tool names in the *Name*
column and a column with the program group which corresponds to the name
of the analysis tool folder in which a tool can be found in the graphical
web interface.

The `gx.analysis.parameters` output, shown below for *Annotate table*, is
a table with parameter names as row ids and a column with short descriptions.

For importers and exporters, corresponding functionality is implemented by
`gx.importers` and `gx.exporters` which list available import and export functions,
respectively, and by `gx.import.parameters` as well as `gx.export.parameters` which
extract parameter information for specified importers and exporters,
respectively.

```R
> library(geneXplainR)

# Server, username and password need to be replaced with valid input values.
#
> gx.login("https://platform.genexplain.com", "someuser@email.io", "12345")
> gx.analysis.list()
              Group                              Name
1 Data manipulation                  Annotate diagram
2 Data manipulation                    Annotate table
3 Data manipulation         Annotate track with genes
4 Data manipulation               CR cluster selector
5 Data manipulation Calculate weighted mutation score
6 Data manipulation                      Check quotas
...

> gx.analysis.parameters("Annotate table")
                                                                                                                         description
inputTablePath                                                                                    Table with experimental(test) data
species                                                                           Species to be used during matching (if applicable)
annotationCollectionPath                                                                            Data collection with annotations
annotationColumns                                                                                       Names for annotation columns
replaceDuplicates        If input table was already annotated by this source, old annotation columns will be removed from the result
outputTablePath                                                                                                         Output table
```

## Listing tools and parameters using the commandline program *apps*

The commandline program [apps](api_overview.md#apps-listing-available-tools) can retrieve
the list of available platform tools as well as an extended table that additionally contains
information about tool parameters.

For a simple listing of tools the input JSON file may look as follows, where `server`, `user`
and `password` properties need to be replaced with valid input values.

```JSON
{
    "server":          "https://platform.genexplain.com",
    "user":            "someuser@email.io",
    "password":        "12345",
    "withParameters":  false,
    "withGalaxy":      false
}
```

The corresponding shell command is as follows, assuming the JSON object above
is stored in a file named *simple_tool_listing.json*.

```bash
java -jar genexplain-api.jar apps simple_tool_listing.json
```

The output consists of a list of analysis tools preceded by the program group to which a tool
is assigned, e.g. *Statistical analysis*. The group name corresponds to the
analysis tool folder in the graphical web interface where a tool can be found.
Please note that in function calls only the tool name without the program group name is required.

```
Data manipulation/Annotate diagram
Data manipulation/Annotate table
Data manipulation/Annotate track with genes
Data manipulation/CR cluster selector
Data manipulation/Calculate weighted mutation score
Data manipulation/Check quotas
Data manipulation/Composite module to proteins
Data manipulation/Convert table
Data manipulation/Convert table to VCF track
...
Statistical analysis/Train random forest
Statistical analysis/Up and Down Identification
Statistical analysis/Variance filter
Statistical analysis/t-SNE
miRNA analysis/Analyze miRNA target enrichment
miRNA analysis/Create miRNA promoters
miRNA analysis/Get miRNA targets
miRNA analysis/miRNA feed forward loops
```

The output can be extended to [Galaxy](https://usegalaxy.org) tools integrated
in the specified platform instance by setting the `withGalaxy` property to `true`.
Furthermore, the output can include information about tool parameters by setting
the `withParameters` property to `true`.

An example output with parameters is shown [below](basics.md#output-from-apps-including-parameters).
The output is a table with the following columns.

| Column name      | Description                           |
| ---------------- | ------------------------------------- |
| Tool folder/name | String consisting of program group and tool name separated by **/**. The program group corresponds to the graphical interface folder where the tool can be found. |
| API name         | The tool name that needs to be specified in API calls, e.g. gx.analysis of geneXplainR |
| Parameter name   | The parameter name that needs to be specified in API calls, e.g. in a parameter list for gx.analysis |
| Short description | Short description of the parameter |
| Type              | Data type of the input, e.g. `data-element-path` expects a string which is a platform path of data element |
| Class             | Java class of a data element, e.g. ru.biosoft.table.TableDataCollection expects a path to refer to a tabular data |
| Required          | Indicates if a value must be specified |
| Description       | Possibly longer description of the parameter |



### Output from *apps* including parameters


| Tool folder/name | API name | Parameter name | Short description | Type | Class | Required | Description |
| ---------------- | -------- | -------------- | ----------------- | ---- | ----- | -------- | ----------- |
| Data manipulation/Annotate diagram | Annotate diagram | inputDiagram | Input diagram | data-element-path | biouml.model.Diagram | true | Diagram to annotate |
| Data manipulation/Annotate diagram | Annotate diagram | table | Annotation table | data-element-path | ru.biosoft.table.TableDataCollection | true | Table with diagram ids and annotations |
| Data manipulation/Annotate diagram | Annotate diagram | column | Annotation column | code-string |  | false | Column with annotations |
| Data manipulation/Annotate diagram | Annotate diagram | outputDiagram | Output diagram | data-element-path | biouml.model.Diagram | false | Path to store annotated diagram |
| Data manipulation/Annotate table | Annotate table | inputTablePath | Experiment | data-element-path | ru.biosoft.table.TableDataCollection | true | Table with experimental(test) data |
| ... | ... | ... | ... | ... | ... | ... | ... |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | miRNAs | miRNAs | data-element-path | ru.biosoft.table.TableDataCollection | true | miRNAs |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | targetGenes | targetGenes | data-element-path | ru.biosoft.table.TableDataCollection | true | targetGenes |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | siteModelCollection | siteModelCollection | data-element-path | ru.biosoft.bsa.SiteModelCollection | true | siteModelCollection |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | ensembl | ensembl | code-string |  | false | ensembl |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | scoreType | scoreType | code-string |  | false | scoreType |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | backgroundGenes | backgroundGenes | data-element-path | ru.biosoft.table.TableDataCollection | true | backgroundGenes |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | from | from | code-string |  | false | from |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | to | to | code-string |  | false | to |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | targetScanDB | targetScanDB | data-element-path | ru.biosoft.access.SqlDataCollection | true | targetScanDB |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | miRNAPromoterScoreThreshold | miRNAPromoterScoreThreshold | code-string |  | false | miRNAPromoterScoreThreshold |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | targetGenePromoterScoreThreshold | targetGenePromoterScoreThreshold | code-string |  | false | targetGenePromoterScoreThreshold |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | miRNATargetScoreThreshold | miRNATargetScoreThreshold | code-string |  | false | miRNATargetScoreThreshold |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | scoreThreshold | scoreThreshold | code-string |  | false | scoreThreshold |
| miRNA analysis/miRNA feed forward loops | miRNA feed forward loops | outTable | outTable | data-element-path | ru.biosoft.table.TableDataCollection | false | outTable |


## Fetching parameter information for specific tools using the *parameters* utility

The program *parameters* of the genexplain-api package extracts parameter
information for specified platform tools.

The following example JSON fetches parameter descriptions for *Annotate table* and
the Galaxy tool *variant_effect_predictor*. The `server`, `user`
and `password` properties need to be set with valid input values.


```JSON
{
    "server":          "https://platform.genexplain.com",
    "user":            "someuser@email.io",
    "password":        "12345",
    "tools":           ["Annotate table", "variant_effect_predictor"]
}
```

The following shell invokes *parameters* with the JSON object above
stored in a file named *specific_tool_parameters.json*.

```bash
java -jar genexplain-api.jar parameters specific_tool_parameters.json
```

The [output](#output-produced-by-parameters-for-platform-tools) is printed to standard output (optionally, an additional JSON parameter `outfile` can
be provided to specify a local output file) and is (partially) shown below. It consists of a JSON object with
one property for each platform tool that was specified in the *tools* list.

The input options of a platform tool are contained in a JSON array, each represented by an object describing
the input option. The order of input parameters in this array is the same as in the graphical web interface.

The `displayName` property shows the parameter title that appears in the graphical web interface. **However**,
the parameter name that needs to be specified in an API call is the `name` property.
E.g. the first parameter of *Annotate table* with `displayName` *Experiment* has the `name` *inputTablePath*. The latter
needs to be used in function calls.

An input option object provides further valuable information such as the type of data that is expected. E.g.
the *Experiment* needs to be provided as a `type` *data-element-path*, which means a path for a
data element in the platform workspace and data element on that path must be a table (`elementClass` *...TableDataCollection*).

If an input option expects a value from a predefined list, the available values to select are also listed, see
e.g. output and description for [Functional classification](first_example.md#output-for-the-functional-classification-tool).

The *geneXplainR* method to fetch parameter information for platform tools is implemented in the
function `gx.analysis.parameters`. However, the *parameters* commandline tool provides more details
about each input option. Therefore, we recommend its use also for geneXplainR projects.


### Output produced by *parameters* for platform tools

```JSON
{
    "Annotate table": [
        {
            "canBeNull": false,
            "promptOverwrite": false,
            "displayName": "Experiment",
            "name": "inputTablePath",
            "icon": "ru.biosoft.table:ru/biosoft/table/resources/table.gif",
            "description": "Table with experimental(test) data",
            "readOnly": false,
            "elementClass": "ru.biosoft.table.TableDataCollection",
            "type": "data-element-path",
            "value": "(select element)",
            "elementMustExist": true,
            "multiSelect": false
        },
        
        "...",
        
        {
            "displayName": "Remove duplicate annotations",
            "name": "replaceDuplicates",
            "description": "If input table was already annotated by this source, old annotation columns will be removed from the result",
            "readOnly": false,
            "type": "bool",
            "value": "true"
        },
        {
            "auto": "on",
            "displayName": "Output table",
            "icon": "ru.biosoft.table:ru/biosoft/table/resources/table.gif",
            "description": "Output table",
            "readOnly": false,
            "type": "data-element-path",
            "canBeNull": false,
            "promptOverwrite": false,
            "name": "outputTablePath",
            "elementClass": "ru.biosoft.table.TableDataCollection",
            "value": "(select element)",
            "elementMustExist": false,
            "multiSelect": false
        }
    ],
    "variant_effect_predictor": [
        {
            "canBeNull": false,
            "promptOverwrite": false,
            "displayName": "Input vcf file",
            "name": "input",
            "icon": "ru.biosoft.bsa:ru/biosoft/bsa/resources/track.gif",
            "description": "",
            "readOnly": false,
            "elementClass": "ru.biosoft.bsa.Track",
            "type": "data-element-path",
            "value": "(select element)",
            "elementMustExist": true,
            "multiSelect": false
        },
        "..."
    ]
}
```
