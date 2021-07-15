package fr.insee.queen.api.pdfutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class PDFDepositProofService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFDepositProofService.class);

    private GenerateFoService generateFoService = new GenerateFoServiceImpl();
    private FoToPDFTransformation foToPDFTransformation = new FoToPDFTransformation();


    public File generatePdf(String date, String campaignLabel, String idec) throws Exception {
        File foFile = generateFoService.generateFo(date, campaignLabel, idec);
        //File pdfFile = foToPDFTransformation.transformFoToPdf(foFile);
        //foFile.delete();
        //LOGGER.info("Returning file : {}",pdfFile.getAbsolutePath());
        return foFile;
    }
}
