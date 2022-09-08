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
