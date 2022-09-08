# ChIP-seq data analysis using geneXplainR

The [R script below](#r-code-for-the-tutorial) implements the [tutorial workflow](chipseq_analysis_intro.md).
The described steps are indicated in code comments for reference.

The R code is provided with the tutorial material as *genexplain_tutorial_chipseq_analysis.R*.
Please note that some parts require editing before running the script, including username, password,
project name.

## R code for the tutorial

```R
library(geneXplainR)

# Please note that for successful login, username and password have to
# belong to a valid account on that server.
server   <- "https://platform.genexplain.com"
user     <- "someuser@email.io"
password <- "12345"

gx.login(server, user, password)

apiProjectName <- "api2022_tutorial"
apiProjectPath <- paste0("data/Projects/", apiProjectName)

# Create a new platform project
# This step should be conducted only once for a new project.
# The gx.createProject function returns an error if the project
# already exists.
gx.createProject(apiProjectName, description = "API 2022 tutorial project")

folderPath <- paste0(apiProjectPath, "/Data/chipseq_analysis_workflow")

# Within a project, folders for data elements need to be created within the
# "Data" folder or its subfolders.
gx.createFolder("data/Projects/api2022_tutorial/Data", "chipseq_analysis_workflow")


#
# Step 1. Import and mapping of TAL1-bound genomic regions
#

# Import the BED file with TAL1-bound regions into the destination folder
gx.import("../data/GSM614003_jurkat.tal1.bed", folderPath, "BED format (*.bed)", list(dbSelector = "Ensembl 52.36n Human (hg18)"))

# After data imports it is recommendable to shortly pause in order to
# allow server processes to finish before using the data
Sys.sleep(1)

gx.analysis.parameters("liftOver1")
#
# Output
#                                                                       description
# input                                                                            
# mapping                                                                          
# minMatch        Recommended values: same species = 0.95, different species = 0.10
# multiple|choice    Recommended values: same species = No, different species = Yes
# out_file1                                                                        
# out_file2
#

bedPath      <- paste0(folderPath, "/GSM614003_jurkat.tal1")
mappedPath   <- paste0(folderPath, "/jurkat_chipseq_hg38")
unmappedPath <- paste0(folderPath, "/jurkat_chipseq_hg38_unmapped")
mapping      <- "hg18->hg38"

# Run Liftover to map hg18 coordinates to hg38
gx.analysis("liftOver1", list(input = bedPath, 
                              mapping = mapping, 
                              minMatch = 0.95, 
                              out_file1 = mappedPath, 
                              out_file2 = unmappedPath),
            TRUE, TRUE)


#
# Step 2. Mapping TAL1-bound genomic regions to nearby genes
#

species <- "Human (Homo sapiens)"
mappedGenePath <- paste0(mappedPath, " Genes")

# Use gx.trackToGeneSet function to map genomic coordinates to genes
gx.trackToGeneSet(mappedPath, species, -5000, 2000, destPath = mappedGenePath)


#
# Step 3. Functional enrichment analysis of genes near TAL1-bound regions
#

# Enrichment of genes associated with Gene Ontology terms using
# "Functional classification"
funclassResultPath <- paste0(mappedGenePath," GO")
gx.analysis("Functional classification", list(sourcePath = mappedGenePath,
                                              species    = species,
                                              bioHub     = "Full gene ontology classification",
                                              minHits    = 1,
                                              pvalueThreshold = 1,
                                              outputTable = funclassResultPath),
           TRUE, TRUE)

# Export analysis result to local file
# The default exporter is for data tables, so that it is sufficient
# to specify the data element path and the local output file
gx.export(funclassResultPath, target.file="functional_classification_result_GO.tsv")

# Enrichment of genes associated with Reactome pathways
funclassResultPath <- paste0(mappedGenePath," Reactome")
gx.analysis("Functional classification", list(sourcePath = mappedGenePath,
                                              species    = species,
                                              bioHub     = "Reactome pathways (74)",
                                              minHits    = 1,
                                              pvalueThreshold = 1,
                                              outputTable = funclassResultPath),
            TRUE, TRUE)

# Export analysis result to local file
gx.export(funclassResultPath, target.file="functional_classification_result_Reactome.tsv")


# Enrichment of genes associated with human diseases based on
# HumanPSD disease biomarker annotation
funclassResultPath <- paste0(mappedGenePath," Human disease biomarkers")
gx.analysis("Functional classification", list(sourcePath = mappedGenePath,
                                              species    = species,
                                              bioHub     = "HumanPSD(TM) disease (2022.1)",
                                              minHits    = 1,
                                              pvalueThreshold = 1,
                                              outputTable = funclassResultPath),
            TRUE, TRUE)

# Export analysis result to local file
gx.export(funclassResultPath, target.file="functional_classification_result_HumanPSD.tsv")


#
# Step 4. Sampling genomic regions not bound by TAL1
#

mealrBackgroundTrack <- paste0(mappedPath, " random 1000")

# Create random track not overlapping with TAL1-bound regions
gx.analysis("Create random track", list(inputTrackPath      = mappedPath,
                                        dbSelector          = "Ensembl 104.38 Human (hg38)",
                                        species             = species,
                                        standardChromosomes = TRUE,
                                        seqNumber           = 1000,
                                        seqLength           = 0,
                                        from                = 0,
                                        to                  = 0,
                                        withOverlap         = FALSE,
                                        randomShift         = FALSE,
                                        outputTrackPath     = mealrBackgroundTrack,
                                        randSeed            = 123),
            TRUE, TRUE)


#
# Step 5. Import and mapping of TAL1 binding site subset
#

#
# Data upload and lifting as in Step 1 for 1000 TAL1 sites sampled
# from the original BED file
gx.import("../data/GSM614003_jurkat.tal1_1000.bed", folderPath, "BED format (*.bed)", list(dbSelector = "Ensembl 52.36n Human (hg18)"))

# After data imports it is recommendable to shortly pause in order to
# allow server processes to finish before using the data
Sys.sleep(1)

bedPath      <- paste0(folderPath, "/GSM614003_jurkat.tal1_1000")
mappedPath   <- paste0(folderPath, "/jurkat_chipseq_hg38_1000")
unmappedPath <- paste0(folderPath, "/jurkat_chipseq_hg38_1000_unmapped")

# Map hg18 coordinates to hg38
gx.analysis("liftOver1", list(input = bedPath, 
                              mapping = mapping, 
                              minMatch = 0.95, 
                              out_file1 = mappedPath, 
                              out_file2 = unmappedPath),
            TRUE, TRUE)


#
# Step 6. Selection of important PWM models using MEALR
#

gx.analysis.parameters("MEALR (tracks)")
#
# Output
#                                                                                              description
# yesSetPath                                                Study track / Track with intervals of interest
# noSetPath                                                Background track / Track of non-bound intervals
# dbSelector                                                   Select a deployed or custom sequence source
# profilePath                                                                   Profile of weight matrices
# maxPosCoef      Maximum number of positive coefficients (motifs of factors with positive binding effect)
# maxComplexity                                                    Maximal complexity paramater to explore
# complexityInc                                         Step-size of incrementing the complexity parameter
# maxUnimproved                                     Maximal number of iterations without improved accuracy
# scoresWithNoSet                                   Include scores for No sequences in score table outputs
# output                                                                                Output folder path
#

mealrOutputPath <- paste0(mappedPath, " MEALR")
transfacProfile <- "databases/TRANSFAC(R) 2022.1/Data/profiles/vertebrate_human_p0.05_non3d"

# Analyze target and background genomic regions using MEALR
gx.analysis("MEALR (tracks)", list(yesSetPath      = mappedPath,
                                   noSetPath       = mealrBackgroundTrack,
                                   dbSelector      = "Ensembl 104.38 Human (hg38)",
                                   profilePath     = transfacProfile,
                                   maxPosCoef      = 150,
                                   maxComplexity   = 0.5,
                                   complexityInc   = 0.02,
                                   maxUnimproved   = 20,
                                   scoresWithNoSet = FALSE,
                                   output          = mealrOutputPath),
            TRUE, TRUE)


#
# Step 7. Extraction of binding transcription factors
#

gx.analysis.parameters("Select top rows")
#
# Output
#                        description
# inputTable             Input table
# column                      Column
# types                         Type
# topPercent             Top percent
# topCount             Top max count
# topMinCount          Top min count
# topTable          Top table output
# middlePercent       Middle percent
# middleCount       Middle max count
# middleMinCount    Middle min count
# middleTable    Middle table output
# bottomPercent       Bottom percent
# bottomCount       Bottom max count
# bottomMinCount    Bottom min count
# bottomTable    Bottom table output
#

mealrMotifPath <- paste0(mealrOutputPath, "/MEALR_positive_coefficients")
mealrTopPath <- paste0(mealrMotifPath, " Top 50")

# Extract top 50 PWMs ranked by logistic regression coefficient
gx.analysis("Select top rows", list(inputTable  = mealrMotifPath,
                                    column      = "Coefficient",
                                    topPercent  = 100.0,
                                    topCount    = 50,
                                    topMinCount = 50,
                                    topTable    = mealrTopPath),
            TRUE, TRUE)

gx.analysis.parameters("Matrices to molecules")
#
# Output
#                                                                                                                                         description
# sitesCollection                          Select table with the results of "Site search on gene set". Such table contains site model ID in each row.
# siteModelsCollection                     Select the profile that was used for site search. In most of the cases, profile is selected automatically.
# species                               Select arabidopsis, nematoda, zebrafish, fruit fly, human, mouse, rat, baker s yeast or fission yeast species
# targetType                                                                                       Select type of identifiers for the resulting table
# ignoreNaNInAggregator                                                                                    Ignore empty values during aggregator work
# aggregator            Select one of the rules to treat values in the numerical columns of the table when several rows are merged into a single one.
# columnName                                                        Select the column with numerical values to apply one of the rules described above
# outputTable                                                                                           Path to store the resulting table in the tree
#

mealrTopGenePath <- paste0(mealrTopPath, " Genes")

# Convert PWMs to factor genes
gx.analysis("Matrices to molecules", list(sitesCollection       = mealrTopPath,
                                          siteModelsCollection  = transfacProfile,
                                          species               = species,
                                          targetType            = "Genes: Ensembl",
                                          outputTable           = mealrTopGenePath),
            TRUE, TRUE)


#
# Step 8. Intersection of potentially TAL1-regulated genes and MEALR TFs
#

gx.analysis.parameters("Venn diagrams")
#
# Output
#                                                                           description
# table1Path                         Table which will be represented as left-top circle
# table1Name     Name for the left table on the diagram (leave empty to use table name)
# circle1Color                             Color for the left-top circle on the diagram
# table2Path                        Table which will be represented as right-top circle
# table2Name    Name for the right table on the diagram (leave empty to use table name)
# circle2Color                            Color for the right-top circle on the diagram
# table3Path                    Table which will be represented as center-bottom circle
# table3Name   Name for the center table on the diagram (leave empty to use table name)
# circle3Color                        Color for the center-bottom circle on the diagram
# simple                                                   All circles has equal radius
# output                                               Folder name to store the results
#

mappedNearbyGenePath   <- paste0(folderPath, "/jurkat_chipseq_hg38 Genes")
mealrTopVennPath <- paste0(mealrTopPath, " Venn")

# Intersect factors identified by MEALR and genes with nearby TAL1 ChIP-seq
# sites
gx.analysis("Venn diagrams", list(table1Path   = mappedNearbyGenePath,
                                  table1Name   = "Genes near TAL1 sites",
                                  table2Path   = mealrTopGenePath,
                                  table2Name   = "MEALR transcription factors",
                                  simple       = TRUE,
                                  output       = mealrTopVennPath),
            TRUE, TRUE)


#
# Step 9. Prediction of binding sites of identified TFs in TAL1-bound genomic regions
#

grnFactorPath <- paste0(mealrTopVennPath, "/Rows present in both tables")

# Load potential GRN factors into R data frame
tfs <- gx.get(grnFactorPath)
# tfs
#                 Gene symbol jurkat_chipseq_hg38: Count
# ENSG00000081059        TCF7                          1
# ENSG00000118513         MYB                          1
# ENSG00000124813       RUNX2                          4
# ENSG00000125952         MAX                          2
# ENSG00000138795        LEF1                          2
# ENSG00000157554         ERG                          1
# ENSG00000159216       RUNX1                          4
# ENSG00000179348       GATA2                          1
#                                            Site model ID Coefficient
# ENSG00000081059                                V$TCF1_09  0.05875131
# ENSG00000118513                                V$CMYB_01  0.02749087
# ENSG00000124813 V$AML_Q6,V$OSF2_Q6,V$RUNX2_03,V$RUNX2_04  0.08663064
# ENSG00000125952                              V$MYCMAX_01  0.02998472
# ENSG00000138795                      V$LEF1_10,V$LEF1_17  0.03285110
# ENSG00000157554                                 V$ERG_07  0.04488260
# ENSG00000159216                       V$AML1_02,V$AML_Q6  0.08663064
# ENSG00000179348                               V$GATA2_09  0.04911769
#

# Extract PWM ids
grnPwms <- unlist(strsplit(tfs[,3], ",", fixed = TRUE))

# Create PWM table for import
write.table(cbind(PWM = grnPwms, Num = 1:length(grnPwms)), file = "grn_pwms.tsv", quote = FALSE, sep = "\t", row.names = FALSE)

# Import PWM table
gx.importTable("grn_pwms.tsv", mealrOutputPath, 
               "MEALR_positive_coefficients Top 50 GRN PWMs", columnForID = "PWM", 
               tableType = "Matrices: TRANSFAC", species = species)

# After data imports it is recommendable to shortly pause in order to
# allow server processes to finish before using the data
Sys.sleep(1)

grnPwmPath <- paste0(mealrOutputPath, "/MEALR_positive_coefficients Top 50 GRN PWMs")

gx.analysis.parameters("Create profile from site model table")
#
# Output
#                                                                                    description
# table                                                Table containing site models as row names
# profile                                                        Profile to copy the values from
# thresholdsColumn Column containing cutoff values (use 'none' to copy cutoffs from the profile)
# outputProfile                                 Specify the path whether to store output profile
#

transfacProfile <- "databases/TRANSFAC(R) 2022.1/Data/profiles/vertebrate_human_p0.001_non3d"
grnPwmProfile   <- paste0(grnPwmPath, " profile")

# Create Match(TM) profile for PWMs of potential GRN factors
gx.analysis("Create profile from site model table", list(table   = grnPwmPath,
                                                         profile = transfacProfile,
                                                         outputProfile = grnPwmProfile),
            TRUE, TRUE)

gx.analysis.parameters("TRANSFAC(R) Match(TM) for tracks")
#
# Output
#                                                                         description
# sequencePath                                               Sequence track to search
# dbSelector                              Select a deployed or custom sequence source
# profilePath                                                                 Profile
# withoutDuplicates Ensure to report sites in overlapping genomic intervals only once
# ignoreCore               Core scores are not calculated and not used for filtering.
# output                                                            Output track path
#

grnMatchPath <- paste0(grnPwmProfile, " Match")

# Predict binding sites of GRN factors in TAL1-bound regions
gx.analysis("TRANSFAC(R) Match(TM) for tracks", list(sequencePath = paste0(folderPath, "/jurkat_chipseq_hg38"),
                                                     dbSelector   = "Ensembl 104.38 Human (hg38)",
                                                     profilePath  = grnPwmProfile,
                                                     withoutDuplicates = TRUE,
                                                     ignoreCore = TRUE,
                                                     output = grnMatchPath),
            TRUE, TRUE)
         

#
# Step 10. Prediction of binding sites of identified TFs around TAL1 transcription start site
#

# Create TAL1 gene table for import
write.table(cbind(ID = c("ENSG00000162367"), Symbol = c("TAL1")), file = "tal1.tsv", quote = FALSE, sep = "\t", row.names = FALSE)

# Import TAL1 gene
gx.importTable("tal1.tsv", mealrOutputPath,
               "TAL1 gene", columnForID = "ID", 
               tableType = "Genes: Ensembl", species = species)

# After data imports it is recommendable to shortly pause in order to
# allow server processes to finish before using the data
Sys.sleep(1)

tal1GenePath <- paste0(mealrOutputPath, "/TAL1 gene")

gx.analysis.parameters("Gene set to track")
#
# Output
#                                                      description
# sourcePath                                 Table of source genes
# species                                      Taxonomical species
# from                      From position (relative to gene start)
# to                          To position (relative to gene start)
# overlapMergingMode How to handle TSS located close to each other
# destPath                                             Output name
#

tal1TrackPath <- paste0(tal1GenePath, " promoter")

# Create track of genomic region around TAL1 TSS (promoter)
gx.analysis("Gene set to track", list(sourcePath = tal1GenePath,
                                      species    = species,
                                      from       = 2000,
                                      to         = 1000,
                                      destPath   = tal1TrackPath),
            TRUE, TRUE)

tal1MatchPath <- paste0(grnPwmProfile, " TAL1 Match")

# Predict binding sites of GRN factors in TAL1 promoter
gx.analysis("TRANSFAC(R) Match(TM) for tracks", list(sequencePath = tal1TrackPath,
                                                     dbSelector   = "Ensembl 104.38 Human (hg38)",
                                                     profilePath  = grnPwmProfile,
                                                     withoutDuplicates = TRUE,
                                                     ignoreCore = TRUE,
                                                     output = tal1MatchPath),
            TRUE, TRUE)

# Export genomic locations of predicted sites for GRN factors around TAL1 TSS
gx.export(tal1MatchPath, exporter = "BED format (*.bed)", target.file="TAL1_grn_pwm_sites.bed")

gx.logout()
```
