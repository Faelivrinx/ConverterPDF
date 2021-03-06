package convert.impl;

import convert.ConvertStrategy;
import convert.Convertable;
import io.reactivex.Completable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.docx4j.samples.AbstractSample;
import org.docx4j.services.client.ConversionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class DocxToPDF extends AbstractSample implements ConvertStrategy {

    @Override
    public Completable convert(Convertable converter) {
        return Completable.create(emitter -> {
            AbstractSample.inputfilepath = converter.getFileInputPath();
            AbstractSample.outputfilepath = converter.getFileOutputPath();
            try {
                boolean saveFo = false;

                WordprocessingMLPackage wordMLPackage;
                System.out.println("Loading file from " + AbstractSample.inputfilepath);
                wordMLPackage = WordprocessingMLPackage.load(new java.io.File(AbstractSample.inputfilepath));

                FieldUpdater updater = new FieldUpdater(wordMLPackage);
                updater.update(true);

                if (AbstractSample.outputfilepath == null || AbstractSample.outputfilepath.isEmpty()) {
                    AbstractSample.outputfilepath = AbstractSample.inputfilepath + ".pdf";
                }

                OutputStream os = new java.io.FileOutputStream(AbstractSample.outputfilepath);
                if (!Docx4J.pdfViaFO()) {

                    // Since 3.3.0, Plutext's PDF Converter is used by default

                    System.out.println("Using Plutext's PDF Converter; add docx4j-export-fo if you don't want that");

                    try {
                        Docx4J.toPDF(wordMLPackage, os);
                    } catch (Docx4JException e) {

                        e.printStackTrace();
                        // What did we write?
                        IOUtils.closeQuietly(os);
                        System.out.println(
                                FileUtils.readFileToString(new File(AbstractSample.outputfilepath)));
                        if (e.getCause() != null
                                && e.getCause() instanceof ConversionException) {

                            ConversionException ce = (ConversionException) e.getCause();
                            ce.printStackTrace();
                        }
                        emitter.onError(e);
                        return;
                    }
                    System.out.println("Saved: " + AbstractSample.outputfilepath);
                    emitter.onComplete();
                    return;
                }

                System.out.println("Attempting to use XSL FO");

                /**
                 * Demo of PDF output.
                 *
                 * PDF output is via XSL FO.
                 * First XSL FO is created, then FOP
                 * is used to convert that to PDF.
                 *
                 * Don't worry if you get a class not
                 * found warning relating to batik. It
                 * doesn't matter.
                 *
                 * If you don't have logging configured,
                 * your PDF will say "TO HIDE THESE MESSAGES,
                 * TURN OFF debug level logging for
                 * org.docx4j.convert.out.pdf.viaXSLFO".  The thinking is
                 * that you need to be able to be warned if there
                 * are things in your docx which the PDF output
                 * doesn't support...
                 *
                 * docx4j used to also support creating
                 * PDF via iText and via HTML. As of docx4j 2.5.0,
                 * only viaXSLFO is supported.  The viaIText and
                 * viaHTML source code can be found in src/docx4j-extras directory
                 *
                 */


                /*
                 * NOT WORKING?
                 *
                 * If you are getting:
                 *
                 *   "fo:layout-master-set" must be declared before "fo:page-sequence"
                 *
                 * please check:
                 *
                 * 1.  the jaxb-xslfo jar is on your classpath
                 *
                 * 2.  that there is no stack trace earlier in the logs
                 *
                 * 3.  your JVM has adequate memory, eg
                 *
                 *           -Xmx1G -XX:MaxPermSize=128m
                 *
                 */


                // Set up font mapper (optional)
                Mapper fontMapper = new IdentityPlusMapper();
                wordMLPackage.setFontMapper(fontMapper);

                // .. example of mapping font Times New Roman which doesn't have certain Arabic glyphs
                // eg Glyph "ي" (0x64a, afii57450) not available in font "TimesNewRomanPS-ItalicMT".
                // eg Glyph "ج" (0x62c, afii57420) not available in font "TimesNewRomanPS-ItalicMT".
                // to a font which does
                PhysicalFont font
                        = PhysicalFonts.get("Arial Unicode MS");
                // make sure this is in your regex (if any)!!!
//		if (font!=null) {
//			fontMapper.put("Times New Roman", font);
//			fontMapper.put("Arial", font);
//		}
//		fontMapper.put("Libian SC Regular", PhysicalFonts.get("SimSun"));

                // FO exporter setup (required)
                // .. the FOSettings object
                FOSettings foSettings = Docx4J.createFOSettings();
                if (saveFo) {
                    foSettings.setFoDumpFile(new java.io.File(AbstractSample.inputfilepath + ".fo"));
                }
                foSettings.setWmlPackage(wordMLPackage);

                // Document format:
                // The default implementation of the FORenderer that uses Apache Fop will output
                // a PDF document if nothing is passed via
                // foSettings.setApacheFopMime(apacheFopMime)
                // apacheFopMime can be any of the output formats defined in org.apache.fop.apps.MimeConstants eg org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
                // FOSettings.INTERNAL_FO_MIME if you want the fo document as the result.
                //foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);

                // Specify whether PDF export uses XSLT or not to create the FO
                // (XSLT takes longer, but is more complete).

                // Don't care what type of exporter you use
                Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

                // Prefer the exporter, that uses a xsl transformation
                // Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

                // Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
                // .. faster, but not yet at feature parity
                // Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

                System.out.println("Saved: " + AbstractSample.outputfilepath);

                // Clean up, so any ObfuscatedFontPart temp files can be deleted
                if (wordMLPackage.getMainDocumentPart().getFontTablePart() != null) {
                    wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
                }
                // This would also do it, via finalize() methods
                updater = null;
                foSettings = null;
                wordMLPackage = null;
                emitter.onComplete();
                return;

            } catch (Docx4JException e) {
                e.printStackTrace();
                emitter.onError(e);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                emitter.onError(e);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
                return;
            }
        });

    }
}
