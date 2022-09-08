# ChIP-seq data analysis with a Java program based on genexplain-api

The [Java code below](#java-code-for-the-tutorial) implements the [tutorial workflow](chipseq_analysis_intro.md).
The described steps are indicated in code comments for reference.

## Compiling and executing the tutorial

The Java code is provided with the tutorial material as
*GenexplainTutorialChipseqAnalysis.java*. Please note that some parts require editing before
running the program, including username, password, project name.

Assuming the source file is stored as *GenexplainTutorialChipseqAnalysis.java*
and the genexplain-api package is located in the same subfolder, the Java class can be compiled as follows.

```bash
javac -cp .:genexplain-api.jar GenexplainTutorialChipseqAnalysis.java
```

Similarly, the class file can then be executed as

```bash
java -cp .:genexplain-api.jar GenexplainTutorialChipseqAnalysis
```


## Java code for the tutorial workflow

```Java
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.PrettyPrint;

import com.genexplain.api.core.GxHttpClient;
import com.genexplain.api.core.GxHttpClientImpl;
import com.genexplain.api.core.GxHttpConnection;
import com.genexplain.api.core.GxHttpConnectionImpl;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.util.HashMap;
import java.util.Map;



public class GenexplainTutorialChipseqAnalysis {
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
        
        String folderPath = "data/Projects/api2022_tutorial/Data/chipseq_analysis_workflow_java";
        String species    = "Human (Homo sapiens)";
        
        // The createFolder function creates the folder if it does not 
        // already exist. Within a project, folders for data elements 
        // have to be created within the *Data* folder or its subfolders.
        //
        client.createFolder("data/Projects/api2022_tutorial/Data", "chipseq_analysis_workflow_java");
        
        
        
        //
        // Step 1. Import and mapping of TAL1-bound genomic regions
        //

        JsonObject params = new JsonObject().add("dbSelector", "Ensembl 52.36n Human (hg18)");
        
        // Import the BED file with TAL1-bound regions into the destination folder
        client.imPort("../data/GSM614003_jurkat.tal1.bed", folderPath, "BED format (*.bed)", params);
        
        // Sometimes a short interruption is required to allow processes
        // on the server complete their work
        Thread.sleep(1000);
        
        String bedPath      = folderPath + "/GSM614003_jurkat.tal1";
        String mappedPath   = folderPath + "/jurkat_chipseq_hg38";
        String unmappedPath = folderPath + "/jurkat_chipseq_hg38_unmapped";
        String mapping      = "hg18->hg38";
        params = new JsonObject()
              .add("input", bedPath)
              .add("mapping", mapping)
              .add("minMatch", 0.95)
              .add("out_file1", mappedPath)
              .add("out_file2", unmappedPath);
        
        // Run Liftover to map hg18 coordinates to hg38
        client.analyze("liftOver1", params, false, true, true);
        
        
        
        //
        // Step 2. Mapping TAL1-bound genomic regions to nearby genes
        //
        
        String mappedGenePath = mappedPath + " Genes";
        JsonArray sourcePaths = new JsonArray().add(mappedPath);
        params = new JsonObject()
              .add("sourcePaths", sourcePaths)
              .add("species", species)
              .add("from", -5000)
              .add("to", 2000)
              .add("destPath", mappedGenePath);
        
        // Run "Track to gene set" tool to map genomic coordinates to genes
        client.analyze("Track to gene set", params, false, true, true);
        
        
        
        //
        // Step 3. Functional enrichment analysis of genes near TAL1-bound
        // regions
        //
        
        String funclassResultPath = mappedGenePath + " GO";
        params = new JsonObject()
              .add("sourcePath", mappedGenePath)
              .add("species", species)
              .add("bioHub", "Full gene ontology classification")
              .add("minHits", 1)
              .add("pvalueThreshold", 1)
              .add("outputTable", funclassResultPath);
              
        // Enrichment of genes associated with Gene Ontology terms using
        // "Functional classification"
        client.analyze("Functional classification", params, false, true, true);
        
        FileOutputStream fileExport = new FileOutputStream("functional_classification_result_GO.tsv");
        
        // Export analysis result to local file
        client.export(funclassResultPath, "Tab-separated text (*.txt)", fileExport, new JsonObject());
        
        // The output stream is not closed by the platform client.
        fileExport.close();
        
        funclassResultPath = mappedGenePath + " Reactome";
        params.add("bioHub", "Reactome pathways (74)").add("outputTable", funclassResultPath);
        
        // Enrichment of genes associated with Reactome pathways
        client.analyze("Functional classification", params, false, true, true);
        
        fileExport = new FileOutputStream("functional_classification_result_Reactome.tsv");
        
        // Export analysis result to local file
        client.export(funclassResultPath, "Tab-separated text (*.txt)", fileExport, new JsonObject());
        
        fileExport.close();
        
        funclassResultPath = mappedGenePath + " Human disease biomarkers";
        params.add("bioHub", "HumanPSD(TM) disease (2022.1)").add("outputTable", funclassResultPath);
        
        // Enrichment of genes associated with human diseases based on
        // HumanPSD disease biomarker annotation
        client.analyze("Functional classification", params, false, true, true);
        
        fileExport = new FileOutputStream("functional_classification_result_HumanPSD.tsv");
        
        // Export analysis result to local file
        client.export(funclassResultPath, "Tab-separated text (*.txt)", fileExport, new JsonObject());
        
        fileExport.close();
        
        
        
        //
        // Step 4. Sampling genomic regions not bound by TAL1
        //
        
        String mealrBackgroundTrack = mappedPath + " random 1000";
        params = new JsonObject()
              .add("inputTrackPath", mappedPath)
              .add("dbSelector", "Ensembl 104.38 Human (hg38)")
              .add("species", species)
              .add("standardChromosomes", true)
              .add("seqNumber", 1000)
              .add("seqLength", 0)
              .add("from", 0)
              .add("to", 0)
              .add("withOverlap", false)
              .add("randomShift", false)
              .add("outputTrackPath", mealrBackgroundTrack)
              .add("randSeed", 123);
        
        // Create random track not overlapping with TAL1-bound regions
        client.analyze("Create random track", params, false, true, true);
        
        
        
        //
        // Step 5. Import and mapping of TAL1 binding site subset
        //
        
        // Data upload and lifting as in Step 1 for 1000 TAL1 sites sampled
        // from the original BED file

        params = new JsonObject().add("dbSelector", "Ensembl 52.36n Human (hg18)");
        
        // Import sampled TAL1 ChIP-seq sites
        client.imPort("../data/GSM614003_jurkat.tal1_1000.bed", folderPath, "BED format (*.bed)", params);
        
        // Sometimes a short interruption is required to allow processes
        // on the server complete their work
        Thread.sleep(1000);
        
        bedPath      = folderPath + "/GSM614003_jurkat.tal1_1000";
        mappedPath   = folderPath + "/jurkat_chipseq_hg38_1000";
        unmappedPath = folderPath + "/jurkat_chipseq_hg38_1000_unmapped";
        params = new JsonObject()
              .add("input", bedPath)
              .add("mapping", mapping)
              .add("minMatch", 0.95)
              .add("out_file1", mappedPath)
              .add("out_file2", unmappedPath);
        
        // Coordinate mapping to hg38
        client.analyze("liftOver1", params, false, true, true);
        
        
        
        //
        // Step 6. Selection of important PWM models using MEALR
        //
        
        String mealrOutputPath = mappedPath + " MEALR";
        String transfacProfile = "databases/TRANSFAC(R) 2022.1/Data/profiles/vertebrate_human_p0.05_non3d";
        params = new JsonObject()
              .add("yesSetPath", mappedPath)
              .add("noSetPath",  mealrBackgroundTrack)
              .add("dbSelector", "Ensembl 104.38 Human (hg38)")
              .add("profilePath", transfacProfile)
              .add("maxPosCoef", 150)
              .add("maxComplexity", 0.5)
              .add("complexityInc", 0.02)
              .add("maxUnimproved", 20)
              .add("scoresWithNoSet", false)
              .add("output", mealrOutputPath);
              
        // Analyze target and background genomic regions using MEALR
        client.analyze("MEALR (tracks)", params, false, true, true);
        
        
        
        //
        // Step 7. Extraction of binding transcription factors
        //
        
        String mealrMotifPath = mealrOutputPath + "/MEALR_positive_coefficients";
        String mealrTopPath   = mealrMotifPath + " Top 50";
        params = new JsonObject()
              .add("inputTable", mealrMotifPath)
              .add("column", "Coefficient")
              .add("types", new JsonArray().add("Top"))
              .add("topPercent", 100.0)
              .add("topCount", 50)
              .add("topMinCount", 50)
              .add("topTable", mealrTopPath);
        
        // Extract top 50 PWMs ranked by logistic regression coefficient
        client.analyze("Select top rows", params, false, true, true);
        
        String mealrTopGenePath = mealrTopPath + " Genes";
        params = new JsonObject()
              .add("sitesCollection", mealrTopPath)
              .add("siteModelsCollection", transfacProfile)
              .add("species", species)
              .add("targetType", "Genes: Ensembl")
              .add("outputTable", mealrTopGenePath);
        
        // Convert PWMs to factor genes
        client.analyze("Matrices to molecules", params, false, true, true);
        
        
        
        //
        // Step 8. Intersection of potentially TAL1-regulated genes and 
        // MEALR TFs
        //
        
        String mappedNearbyGenePath = folderPath + "/jurkat_chipseq_hg38 Genes";
        String mealrTopVennPath     = mealrTopPath + " Venn";
        params = new JsonObject()
              .add("table1Path", mappedNearbyGenePath)
              .add("table1Name", "Genes near TAL1 sites")
              .add("table2Path", mealrTopGenePath)
              .add("table2Name", "MEALR transcription factors")
              .add("simple", true)
              .add("output", mealrTopVennPath);
        
        // Intersect factors identified by MEALR and genes with nearby
        // TAL1 ChIP-seq sites
        client.analyze("Venn diagrams", params, false, true, true);
        
        
        
        //
        // Step 9. Prediction of binding sites of identified TFs in 
        // TAL1-bound genomic regions
        //
        
        String grnFactorPath = mealrTopVennPath + "/Rows present in both tables";
        
        // Load table with potential GRN factors
        JsonObject tableData = client.getTable(grnFactorPath);
        
        // The JSON object contains the table data under property
        // "data"
        JsonArray topPwms  = tableData.get("data").asArray().get(3).asArray();
        JsonArray topCoefs = tableData.get("data").asArray().get(4).asArray();
        String tpwms;
        String[] pwmids;
        double tcoef;
        Map<String, Double> topPwmData = new HashMap<>();
        
        // Extract PWM ids and coefficients
        for (int t = 0; t < topPwms.size(); ++t) {
            tpwms = topPwms.get(t).asString();
            tcoef = topCoefs.get(t).asDouble();
            pwmids = tpwms.split(",");
            for (String id : pwmids) {
                if (topPwmData.containsKey(id)) {
                    topPwmData.put(id, Math.max(topPwmData.get(id), tcoef));
                } else {
                    topPwmData.put(id, tcoef);
                }
            }
        }
        
        // Create PWM table for upload
        FileWriter fw = new FileWriter("grn_pwms.tsv");
        fw.write("PWM\tCoefficient\n");
        for (String id :  topPwmData.keySet()) {
            fw.write(id + "\t" + topPwmData.get(id) + "\n");
        }
        fw.close();
        
        // Import PWM table
        client.importTable("grn_pwms.tsv", mealrOutputPath, 
                           "MEALR_positive_coefficients Top 50 GRN PWMs", 
                           false, GxHttpClient.ColumnDelimiter.Tab, 1, 2, "", "PWM",
                           false, "Matrices: TRANSFAC", species);
        
        // Sometimes a short interruption is required to allow processes
        // on the server complete their work
        Thread.sleep(1000);
        
        String grnPwmPath = mealrOutputPath + "/MEALR_positive_coefficients Top 50 GRN PWMs";
        transfacProfile = "databases/TRANSFAC(R) 2022.1/Data/profiles/vertebrate_human_p0.001_non3d";
        String grnPwmProfile = grnPwmPath + " profile";
        params = new JsonObject()
              .add("table", grnPwmPath)
              .add("profile", transfacProfile)
              .add("outputProfile", grnPwmProfile);
              
        // Create Match(TM) profile for PWMs of potential GRN factors
        client.analyze("Create profile from site model table", params, false, true, true);
        
        String grnMatchPath = grnPwmProfile + " Match";
        params = new JsonObject()
              .add("sequencePath", folderPath + "/jurkat_chipseq_hg38")
              .add("dbSelector", "Ensembl 104.38 Human (hg38)")
              .add("profilePath", grnPwmProfile)
              .add("withoutDuplicates", true)
              .add("ignoreCore", true)
              .add("output", grnMatchPath);
        
        // Predict binding sites of GRN factors in TAL1-bound regions
        client.analyze("TRANSFAC(R) Match(TM) for tracks", params, false, true, true);
        
        
        
        //
        // Step 10. Prediction of binding sites of identified TFs around 
        // TAL1 transcription start site
        //
        
        // Create TAL1 gene table for import        
        fw = new FileWriter("tal1.tsv");
        fw.write("ID\tSymbol\nENSG00000162367\tTAL1\n");
        fw.close();
        
        // Import TAL1 gene
        client.importTable("tal1.tsv", mealrOutputPath, "TAL1 gene", 
                           false, GxHttpClient.ColumnDelimiter.Tab, 1, 2, "", "ID",
                           false, "Genes: Ensembl", species);
        
        // Sometimes a short interruption is required to allow processes
        // on the server complete their work
        Thread.sleep(1000);
                
        String tal1GenePath = mealrOutputPath + "/TAL1 gene";
        String tal1TrackPath = tal1GenePath + " promoter";
        params = new JsonObject()
              .add("sourcePath", tal1GenePath)
              .add("species", species)
              .add("from", 2000)
              .add("to", 1000)
              .add("destPath", tal1TrackPath);
        
        // Create track of genomic region around TAL1 TSS (promoter)
        client.analyze("Gene set to track", params, false, true, true);
        
        String tal1MatchPath = grnPwmProfile + " TAL1 Match";
        params = new JsonObject()
              .add("sequencePath", tal1TrackPath)
              .add("dbSelector", "Ensembl 104.38 Human (hg38)")
              .add("profilePath", grnPwmProfile)
              .add("withoutDuplicates", true)
              .add("ignoreCore", true)
              .add("output", tal1MatchPath);
        
        // Predict binding sites of GRN factors in TAL1 promoter
        client.analyze("TRANSFAC(R) Match(TM) for tracks", params, false, true, true);
        
        fileExport = new FileOutputStream("TAL1_grn_pwm_sites.bed");
        
        // Export genomic locations of predicted sites for GRN factors
        // around TAL1 TSS
        client.export(tal1MatchPath, "BED format (*.bed)", fileExport, new JsonObject());
        
        fileExport.close();
        
        con.logout();
    }
}
```
