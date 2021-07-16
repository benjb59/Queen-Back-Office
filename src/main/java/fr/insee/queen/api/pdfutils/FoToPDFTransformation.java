package fr.insee.queen.api.pdfutils;

import org.apache.fop.apps.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class FoToPDFTransformation {
    static Logger LOGGER = LoggerFactory.getLogger(FoToPDFTransformation.class);

    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;

    public FoToPDFTransformation() {
        InputStream isXconf = Constants.getInputStreamFromPath(Constants.FOP_CONF);
        URI folderBase;
        try {
            folderBase = FoToPDFTransformation.class.getResource("/pdf/").toURI();
            LOGGER.info("URI folderBase : "+folderBase);
            FopFactoryBuilder confBuilder = new FopConfParser(isXconf, folderBase).getFopFactoryBuilder();
            FopFactory fopFactory = confBuilder.build();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            this.fopFactory = fopFactory;
            this.foUserAgent = foUserAgent;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public File transformFoToPdf(File foFile) throws Exception {
        File outFilePDF = File.createTempFile("pdf-file",".pdf");
        try{
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outFilePDF));
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source src = new StreamSource(foFile);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
            out.close();
            LOGGER.info("End of pdf transformation");
        } catch (Exception e){
            LOGGER.error("Error during fo to pdf transformation :"+e.getMessage());
        }
        return outFilePDF;
    }
}
