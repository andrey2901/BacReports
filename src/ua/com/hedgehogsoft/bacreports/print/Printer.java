package ua.com.hedgehogsoft.bacreports.print;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Printer
{
   public void print(JTable table)
   {
      try
      {
         Document document = new Document();
         PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("report.folder")));
         document.open();
         addTitlePage(document);
         document.add(createPdfTable(table));
         document.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private PdfPTable createPdfTable(JTable jTable)
   {
      DefaultTableModel model = (DefaultTableModel) jTable.getModel();
      // a table with three columns
      PdfPTable table = new PdfPTable(model.getColumnCount());
      BaseFont bf = null;
      try
      {
         bf = BaseFont.createFont(System.getProperty("local.fonts"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      }
      catch (IOException | DocumentException e)
      {
         e.printStackTrace();
      }
      Font font = new Font(bf);

      for (int i = 0; i < model.getColumnCount(); i++)
      {
         String columnName = model.getColumnName(i);

         table.addCell(new PdfPCell(new Phrase(columnName, font)));
      }
      for (int row = 0; row < model.getRowCount(); row++)
      {
         for (int column = 0; column < model.getColumnCount(); column++)
         {
            switch (column)
            {
               case 0:
                  table.addCell(new PdfPCell(new Phrase(Integer.toString((int) model.getValueAt(row, column)), font)));
                  break;
               case 1:
                  table.addCell(new PdfPCell(new Phrase((String) model.getValueAt(row, column), font)));
                  break;
               case 2:
                  table.addCell(new PdfPCell(
                        new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font)));
                  break;
               case 3:
                  table.addCell(new PdfPCell(
                        new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font)));
                  break;
               case 4:
                  table.addCell(new PdfPCell(
                        new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font)));
                  break;
               case 5:
                  Date date = (Date) model.getValueAt(row, column);
                  table.addCell(new PdfPCell(new Phrase((String) date.toString(), font)));
                  break;
            }
         }
      }
      return table;
   }

   private void addTitlePage(Document document) throws DocumentException
   {
      Paragraph preface = new Paragraph();
      
      addEmptyLine(preface, 1);
      
      BaseFont bf = null;
      try
      {
         bf = BaseFont.createFont(System.getProperty("local.fonts"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      Font font = new Font(bf);
      
      preface.add(new Paragraph("Это отчет баклаборатории", font));

      addEmptyLine(preface, 5);

      document.add(preface);
   }

   private void addEmptyLine(Paragraph paragraph, int number)
   {
      for (int i = 0; i < number; i++)
      {
         paragraph.add(new Paragraph(" "));
      }
   }
}
