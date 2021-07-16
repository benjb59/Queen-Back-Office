package fr.insee.queen.api.pdfutils;

import org.apache.fop.apps.*;
import org.apache.fop.apps.io.InternalResourceResolver;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.layoutmgr.LayoutManagerMaker;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageSessionContext;
import org.apache.xmlgraphics.io.ResourceResolver;
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
import java.util.Map;
import java.util.Set;

public class FoToPDFTransformation {
    static Logger LOGGER = LoggerFactory.getLogger(FoToPDFTransformation.class);

    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;

    public FoToPDFTransformation() {
        InputStream isXconf = Constants.getInputStreamFromPath(Constants.FOP_CONF);
        URI folderBase = null;
        try {
            folderBase = FoToPDFTransformation.class.getResource("/pdf/").toURI();
            LOGGER.info("URI folderBase : "+folderBase);
            FopFactory fopFactory = FopFactory.newInstance(folderBase, isXconf);
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
