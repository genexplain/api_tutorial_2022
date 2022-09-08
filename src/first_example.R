#
# Copyright (c) 2022 geneXplain GmbH, Wolfenbüttel, Germany
#
# Author: Philip Stegmaier, philip.stegmaier@genexplain.com
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY 
# OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
# LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
# FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
# EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
# LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
# ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
# OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

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
