package my.latterdayward.service

import com.lowagie.text.*
import com.lowagie.text.pdf.CMYKColor
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import my.latterdayward.data.Agenda
import my.latterdayward.data.ProgramContent
import my.latterdayward.data.ProgramItem
import my.latterdayward.data.WardAnnouncement
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse
import kotlin.math.roundToInt

@Component
class PdfService {

    private val pageWidth = 410
    private val darkBlue = CMYKColor(100, 80, 0 ,100)
    private val dividerFont = Font(Font.COURIER, 14f, Font.BOLD, darkBlue)
    private val titleFont = Font(Font.COURIER, 20f, Font.BOLD, darkBlue)
    private val dateFont = Font(Font.COURIER, 12f, Font.NORMAL, darkBlue)
    private val boldFont = Font(Font.HELVETICA, 12f, Font.BOLD)
    private val normalFont = Font(Font.HELVETICA, 12f, Font.NORMAL)

    fun generatePdf(res: HttpServletResponse, agenda: Agenda, wardTitle: String?) {
        setHeaders(res)
        val output = ByteArrayOutputStream()
        val document = Document()
        PdfWriter.getInstance(document, output)
        document.open()
        createAgenda(agenda, wardTitle, document)
        output.close()
        document.close()
        res.outputStream.write(output.toByteArray())
    }

    private fun setHeaders(res: HttpServletResponse) {
        res.contentType = "application/pdf"
        res.setHeader("Content-Disposition", "attachment; filename=sacrament_agenda.pdf")
    }

    private fun createAgenda(agenda: Agenda, wardTitle: String?, document: Document) {
        val programTable = PdfPTable(1)
        titleRow(wardTitle ?: "Sacrament Meeting Program", programTable, titleFont)
        agenda.date?.let {
            titleRow(formattedDate(it), programTable, dateFont)
        }

        contentRow(agenda.presiding, programTable)
        contentRow(agenda.conducting, programTable)
        contentRow(agenda.organist, programTable)
        contentRow(agenda.chorister, programTable)
        contentRow(agenda.openHymn, programTable)
        contentRow(agenda.invocation, programTable)
        divider("Ward Business", programTable)
        contentRow(agenda.sacramentHymn, programTable)
        divider("Administration of the Sacrament", programTable)
        // Set the program speakers
        agenda.programContent?.sortedBy { it.order }?.forEach { p ->
            contentRow(p, programTable)
        }
        contentRow(agenda.closingHymn, programTable)
        contentRow(agenda.benediction, programTable)
        document.add(programTable)

        val announcementTable = PdfPTable(1)
        announcementTable.setSpacingBefore(30f)
        divider("Announcements", announcementTable)
        agenda.wardAnnouncement?.forEach {
            announcementRow(it, announcementTable)
        }
        document.newPage()
        document.add(announcementTable)
    }

    private fun formattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
        return date.format(formatter)
    }

    private fun agendaRow(itemTitle: String?, itemName: String?, table: PdfPTable) {
        val title = Chunk(itemTitle)
        val name = Chunk(itemName)
        val titleWidth = title.widthPoint.roundToInt()
        val nameWidth = name.widthPoint.roundToInt()
        val phrase = Phrase(title)
        phrase.add(repeatChar(titleWidth + nameWidth))
        phrase.add(name)
        val cell = PdfPCell(phrase)
        cell.paddingTop = 25f
        cell.border = PdfPCell.NO_BORDER
        table.addCell(cell)
    }

    private fun contentRow(item: ProgramItem?, table: PdfPTable) {
        agendaRow(item?.title, item?.name, table)
    }

    private fun contentRow(item: ProgramContent?, table: PdfPTable) {
        agendaRow(item?.title, item?.name, table)
    }

    private fun titleRow(text: String, table: PdfPTable, font: Font) {
        val titleCell = PdfPCell(Paragraph(text, font))
        titleCell.border = PdfPCell.NO_BORDER
        titleCell.setLeading(15f, 0f)
        titleCell.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(titleCell)
    }

    private fun announcementRow(item: WardAnnouncement, table: PdfPTable) {
        val title = Paragraph(Chunk(item.title, boldFont))
        val descr = Paragraph(Chunk(item.description, normalFont))
        val cell = PdfPCell()
        cell.addElement(title)
        cell.addElement(descr)
        cell.paddingTop = 10f
        cell.border = PdfPCell.NO_BORDER
        table.addCell(cell)
    }

    private fun repeatChar(width: Int, char: String = " ."): String {
        val ellipse = Chunk(char)
        return char.repeat(((pageWidth - width) / ellipse.widthPoint).roundToInt())
    }

    private fun divider(title: String, table: PdfPTable) {
        val text = Chunk(title, dividerFont)
        val repeatedChar = Chunk("---------- ", dividerFont)
        repeatedChar.setCharacterSpacing(-3f)
        val phrase = Phrase(repeatedChar)
        phrase.add(text)
        phrase.add(repeatedChar)
        val cell = PdfPCell(phrase)
        cell.border = PdfPCell.NO_BORDER
        cell.setLeading(15f, 0f)
        cell.paddingTop = 25f
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(cell)
    }
}