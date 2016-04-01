package org.orbisgis.wpsservice;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.opengis.ows.v_2_0.CodeType;
import net.opengis.wps.v_2_0.DescribeProcess;
import org.junit.Assert;
import org.junit.Test;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * This test class perform tests about groovy wps scripts.
 * It loads several script in the wpsService and then test the DescribeProcess request.
 *
 * @author Sylvain PALOMINOS
 */
public class DescribeProcessScriptTest {
    WpsService wpsService;

    /**
     * Test the DataStore script DescribeProcess request.
     */
    @Test
    public void testDataStoreScript(){
        //Start the wpsService
        initWpsService();
        //Build the DescribeProcess object
        DescribeProcess describeProcess = new DescribeProcess();
        describeProcess.setLang("fr");
        List<CodeType> identifierList = new ArrayList<>();
        CodeType dataStoreId = new CodeType();
        dataStoreId.setValue("orbisgis:test:datastore");
        identifierList.add(dataStoreId);
        describeProcess.setIdentifier(identifierList);
        //Marshall the DescribeProcess object into an OutputStream
        try {
            Marshaller marshaller = JaxbContainer.JAXBCONTEXT.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(describeProcess, out);
            //Write the OutputStream content into an Input stream before sending it to the wpsService
            InputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
            ByteArrayOutputStream xml = (ByteArrayOutputStream)wpsService.callOperation(in);
            //Get back the result of the DescribeProcess request as a BufferReader
            InputStream resultXml = new ByteArrayInputStream(xml.toByteArray());
            //Unmarshall the result and check that the object is the same as the resource unmashalled xml.
            Unmarshaller unmarshaller = JaxbContainer.JAXBCONTEXT.createUnmarshaller();
            Object resultObject = unmarshaller.unmarshal(resultXml);
            File f = new File(this.getClass().getResource("DataStoreProcessOfferings.xml").getFile());
            Object ressourceObject = unmarshaller.unmarshal(f);
            String message = "Error on unmarshalling the WpsService answer, the object is not the one expected.\n\n";

            //Get the result xml
            resultXml.reset();
            BufferedReader result = new BufferedReader(new InputStreamReader(resultXml));
            String responseLine;
            while ((responseLine = result.readLine()) != null) {
                message+=responseLine+"\n";
            }

            Assert.assertTrue(message, ressourceObject.equals(resultObject));
        } catch (JAXBException | IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }

    }

    /**
     * Initialise a wpsService to test the scripts.
     * The initialised wpsService can't execute the processes.
     */
    private void initWpsService() {
        if (wpsService == null) {
            //Start the WpsService
            LocalWpsServiceImplementation localWpsService = new LocalWpsServiceImplementation();
            localWpsService.initTest();
            //Try to load the groovy scripts
            try {
                URL url = this.getClass().getResource("DataStore.groovy");
                if (url != null) {
                    File f = new File(url.toURI());
                    localWpsService.addLocalScript(f, null, false);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            wpsService = localWpsService;
        }
    }
}
