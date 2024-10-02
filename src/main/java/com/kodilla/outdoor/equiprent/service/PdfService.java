package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.dto.Invoice;
import com.kodilla.outdoor.equiprent.dto.ReportDto;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {
    public byte[] generatePdfReport(ReportDto reportDto) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Create content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),20);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(100, 700);

                // Add report title
                contentStream.showText(ReportDto.REPORT_TITLE);
                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 15);
                contentStream.newLine();

                // Add report dates
                contentStream.showText(ReportDto.START_DATE + ": " + reportDto.getReportStartDate());
                contentStream.newLine();
                contentStream.showText(ReportDto.END_DATE + ": " + reportDto.getReportEndDate());

                // Add report details
                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText(ReportDto.TOTAL_RENTALS + ": " + reportDto.getTotalRentals());
                contentStream.newLine();
                contentStream.showText(ReportDto.TOTAL_RETURNS +  ": " + reportDto.getTotalReturns());
                contentStream.newLine();
                contentStream.showText(ReportDto.OVERDUE_RENTALS + ": " + reportDto.getOverdueRentals());
                contentStream.newLine();

                contentStream.endText();
            }

            // Write document to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] generatePdfInvoice(Invoice invoice) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Create content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(100, 700);

                // Add invoice title
                contentStream.showText(Invoice.INVOICE_TITLE);
                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 15);
                contentStream.newLine();

                // Add invoice details
                contentStream.showText(Invoice.RENTAL_START_DATE + ": " + invoice.getRentalStartDate());
                contentStream.newLine();
                contentStream.showText(Invoice.RENTAL_END_DATE + ": " + invoice.getRentalEndDate());
                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
                contentStream.newLine();
                contentStream.showText(Invoice.RENTER_DETAILS + ": " + invoice.getRenter().getFirstName() + " " + invoice.getRenter().getLastName() + ", " + invoice.getRenter().getAddress());
                contentStream.newLine();
                contentStream.showText(Invoice.RENTED_EQUIPMENT_DETAILS + ": " + invoice.getEquipment().getName() + " (" + invoice.getEquipment().getCategory() + ")");
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText(Invoice.TOTAL + ": " + invoice.getTotalPrice() + " " + invoice.getCurrencyCode());
                contentStream.newLine();
                contentStream.endText();
            }

            // Write document to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
