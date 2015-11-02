package ua.com.hedgehogsoft.bacreports.print;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class RemainsReportPrinter
{
   private static final Logger logger = Logger.getLogger(RemainsReportPrinter.class);

   public void print(JTable table, String date)
   {
      try
      {
         BaseFont bf = null;
         try
         {
            bf = BaseFont.createFont(System.getProperty("local.fonts"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
         }
         catch (IOException e)
         {
            logger.error("The font wasn't loaded.", e);
         }
         Font font = new Font(bf);

         font.setSize(14);

         Document document = new Document(PageSize.A4, 0, 0, 0, 0);

         PdfWriter.getInstance(document,
               new FileOutputStream(System.getProperty("report.folder") + "/Zalyshky_na_" + date + ".pdf"));

         document.open();

         Paragraph paragraph = new Paragraph();
         paragraph.setFont(font);
         paragraph.setSpacingAfter(1);
         paragraph.setSpacingBefore(1);
         paragraph.setAlignment(Element.ALIGN_CENTER);
         paragraph.setIndentationLeft(1);
         paragraph.setIndentationRight(1);

         Paragraph paragraph1 = new Paragraph();
         paragraph1.setFont(font);
         paragraph1.setSpacingAfter(3);
         paragraph1.setSpacingBefore(3);
         paragraph1.setAlignment(Element.ALIGN_CENTER);
         Chunk chunk1 = new Chunk("Залишок");
         paragraph1.add(chunk1);

         Paragraph paragraph2 = new Paragraph();
         paragraph2.setFont(font);
         paragraph2.setSpacingAfter(1);
         paragraph2.setSpacingBefore(1);
         paragraph2.setAlignment(Element.ALIGN_CENTER);
         Chunk chunk2 = new Chunk("поживних середовищ і хімреактивів, лабораторного скла\n"
               + "по Централізованій баклабораторії Лівобережжя КЗ \"Дніпропетровьска міська клінічна лікарня №9\" ДОР\"");
         paragraph2.add(chunk2);

         Paragraph paragraph3 = new Paragraph();
         paragraph3.setFont(font);
         paragraph3.setSpacingAfter(3);
         paragraph3.setSpacingBefore(3);
         paragraph3.setAlignment(Element.ALIGN_CENTER);
         Chunk chunk4 = new Chunk("на " + date);
         paragraph3.add(chunk4);

         paragraph.add(paragraph1);
         paragraph.add(paragraph2);
         paragraph.add(paragraph3);

         document.add(paragraph);

         /*******************************************************************************************/

         font.setSize(10);

         TableModel model = table.getModel();

         PdfPTable pdfTable = new PdfPTable(model.getColumnCount() - 1);

         pdfTable.setWidths(new int[] {2, 21, 4, 4, 4, 4});

         pdfTable.setWidthPercentage(90);

         PdfPCell cell = null;
         cell = new PdfPCell(new Phrase("№ з/п", font));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         cell = new PdfPCell(new Phrase("Найменування предметів закупівель", font));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         cell = new PdfPCell(new Phrase("Одиниця виміру", font));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         cell = new PdfPCell(new Phrase("Ціна, грн./од.", font));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         cell = new PdfPCell(new Phrase("Кількість, од.", font));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         cell = new PdfPCell(new Phrase("Сума, грн.", font));
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

         table.setRowSorter(sorter);

         ArrayList<SortKey> list = new ArrayList<SortKey>();

         list.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));

         ((TableRowSorter<TableModel>) sorter).setSortKeys(list);

         ((TableRowSorter<TableModel>) sorter).sort();

         String group = (String) model.getValueAt(0, 3);

         double common = 0d;

         cell = new PdfPCell(new Phrase(group, font));
         cell.setColspan(6);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         for (int row = 0; row < model.getRowCount(); row++)
         {
            if (!group.equals((String) model.getValueAt(row, 3)))
            {
               cell = new PdfPCell(new Phrase((String) model.getValueAt(row, 3), font));
               cell.setColspan(6);
               cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
               pdfTable.addCell(cell);
               group = (String) model.getValueAt(row, 3);
            }
            for (int column = 0; column < model.getColumnCount(); column++)
            {
               switch (column)
               {
                  case 0:
                     cell = new PdfPCell(new Phrase(Integer.toString((int) model.getValueAt(row, column)), font));
                     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                     pdfTable.addCell(cell);
                     break;
                  case 1:
                  case 2:
                     cell = new PdfPCell(new Phrase((String) model.getValueAt(row, column), font));
                     cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                     pdfTable.addCell(cell);
                     break;
                  case 4:
                  case 5:
                     cell = new PdfPCell(
                           new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font));
                     cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                     pdfTable.addCell(cell);
                     break;
                  case 6:
                     cell = new PdfPCell(
                           new Phrase((String) Double.toString((double) model.getValueAt(row, column)), font));
                     cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                     pdfTable.addCell(cell);
                     common += (double) model.getValueAt(row, column);
                     break;
               }
            }
         }

         cell = new PdfPCell(new Phrase("Всього", font));
         cell.setColspan(5);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         cell = new PdfPCell(new Phrase((String) Double.toString(common), font));
         cell.setColspan(5);
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         pdfTable.addCell(cell);

         document.add(pdfTable);

         /*******************************************************************************************/

         document.add(Chunk.NEWLINE);

         Paragraph subscribeParagraph = new Paragraph();
         subscribeParagraph.setFont(font);

         Paragraph responsibleParagraph = new Paragraph();
         responsibleParagraph.setFont(font);
         responsibleParagraph.setSpacingAfter(0);
         responsibleParagraph.setSpacingBefore(5);
         responsibleParagraph.setAlignment(Element.ALIGN_RIGHT);
         Chunk responsibleChunk = new Chunk(
               "Матеріально відповідальна особа                                                                                         ");
         responsibleParagraph.add(responsibleChunk);

         Paragraph laboratoryParagraph = new Paragraph();
         laboratoryParagraph.setFont(font);
         laboratoryParagraph.setSpacingAfter(3);
         laboratoryParagraph.setSpacingBefore(0);
         laboratoryParagraph.setAlignment(Element.ALIGN_RIGHT);
         Chunk labVacancyChunk = new Chunk("Лаборант з бактеріології      ");
         Chunk labUnderlineChunk = new Chunk("                                       ");
         labUnderlineChunk.setUnderline(0.1f, -0.5f);
         Chunk labNameChunk = new Chunk("          Н.В. Нагорна            ");
         laboratoryParagraph.add(labVacancyChunk);
         laboratoryParagraph.add(labUnderlineChunk);
         laboratoryParagraph.add(labNameChunk);

         Paragraph deputyParagraph = new Paragraph();
         deputyParagraph.setFont(font);
         deputyParagraph.setSpacingAfter(5);
         deputyParagraph.setSpacingBefore(5);
         deputyParagraph.setAlignment(Element.ALIGN_RIGHT);
         Chunk deputyVacancyChunk = new Chunk("Зав.ЦБакЛЛ      ");
         Chunk deputyUnderlineChunk = new Chunk("                                       ");
         deputyUnderlineChunk.setUnderline(0.1f, -0.5f);
         Chunk deputyNameChunk = new Chunk("    Л.М. Москаленко            ");
         deputyParagraph.add(deputyVacancyChunk);
         deputyParagraph.add(deputyUnderlineChunk);
         deputyParagraph.add(deputyNameChunk);

         subscribeParagraph.add(deputyParagraph);
         subscribeParagraph.add(responsibleParagraph);
         subscribeParagraph.add(laboratoryParagraph);

         document.add(subscribeParagraph);

         /*******************************************************************************************/

         document.close();
      }
      catch (Exception e)
      {
         logger.error("Zalyshky_na_" + date + ".pdf wasn't printed.", e);
      }
   }
}
