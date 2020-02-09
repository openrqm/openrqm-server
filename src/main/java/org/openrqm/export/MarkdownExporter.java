/*
 * openrqm-server
 * SPDX-License-Identifier: GPL-2.0-only
 * Copyright (C) 2019 Marcel Jaehn
 */

package org.openrqm.export;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.NodeTraversor;
import org.openrqm.model.RQMDocument;
import org.openrqm.model.RQMElement;
import org.openrqm.model.RQMElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author Marcel Jaehn <marcel.jaehn@online.de>
 */
public class MarkdownExporter implements Exporter {

    private final static Logger logger = LoggerFactory.getLogger(MarkdownExporter.class);
    
    private final static String TEMPLATE_DIR = "templates/";
    private final static String EXPORT_DIR = "export/";

    @Override
    public Resource export(RQMDocument document, RQMElements elements, String templateName, String exportName) throws Exception {
        // load template file to fill with the document
        logger.info("Load template");
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile(TEMPLATE_DIR + templateName + ".md");
        logger.info("Template loaded successful");
        
        // transform styling in all elements
        elements.forEach((element) -> {transformElement(element);} );
        
        // fill the template file with the content of the document
        logger.info("Filling template");
        Map<String, Object> context = new HashMap<>();
        context.put("rqmelements", elements);
        File generatedFile = new File(EXPORT_DIR + exportName + ".md");
        generatedFile.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(generatedFile)) {
            m.execute(writer, context).flush();
        }
        logger.info("Template filled successful");

        return new FileSystemResource(EXPORT_DIR + exportName + ".md");
    }
    
    public void transformElement(RQMElement element) {
        Document document = Jsoup.parseBodyFragment(element.getContent());
        if (document == null) {
            logger.info("Error while parsing the element content of elementId " + element.getId());
            return;
        }
        MarkdownNodeVisitor transformation = new MarkdownNodeVisitor();
        NodeTraversor.traverse(transformation, document.body());
        element.setContent(transformation.content);
    }
}
