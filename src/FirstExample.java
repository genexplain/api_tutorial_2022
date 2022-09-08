/**
 * Copyright (C) 2022 geneXplain GmbH, Wolfenbuettel, Germany
 *
 * Author: Philip Stegmaier
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY 
 * OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
