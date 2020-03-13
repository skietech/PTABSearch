package com.ptab.sky;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "classpath:com/ptab/sky/config/PtabSkyDataContext.xml" })
public class TestWordDoc {
	
	
	public void replaceDoc()
	{
		XWPFDocument doc;
		try {
			doc = new XWPFDocument(OPCPackage.open("/Users/raviramani/Documents/PTABProj/WordTemplates/inter+partes+reexamination+appeal+docketing+notice+3rd+party+Template.docx"));
		
	   flow: for (XWPFParagraph p : doc.getParagraphs()) {
	        List<XWPFRun> runs = p.getRuns();
	        if (runs != null) {
	            for (XWPFRun r : runs) {
	                String text = r.getText(0);
	                if (text != null && text.contains("REQUESTER")) {
	                    text = text.replace("REQUESTER", "RaviRamani");
	                    r.setText(text, 0);
	                    break flow;
	                    
	                }
	            }
	        }
	    }
	    for (XWPFTable tbl : doc.getTables()) {
	        for (XWPFTableRow row : tbl.getRows()) {
	            for (XWPFTableCell cell : row.getTableCells()) {
	                for (XWPFParagraph p : cell.getParagraphs()) {
	                    for (XWPFRun r : p.getRuns()) {
	                        String text = r.getText(0);
	                        if (text != null && text.contains("REQUESTER")) {
	                            text = text.replace("REQUESTER", "Aakash");
	                            r.setText(text, 0);
	                        }
	                    }
	                }
	            }
	        }
	    }

	        doc.write(new FileOutputStream("/Users/raviramani/Documents/PTABProj/WordTemplates/output.docx"));
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
