package reports

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable

class GeneratePDFDocument {
    def proccessIssues(data,document,typeVul,aplication){
        def txtColor = ""
        def bgColor = ""
        if(data.size() >0) {
            switch (typeVul) {
                case "Bloqueantes":
                case "Críticas  ":
                    txtColor = BaseColor.WHITE
                    bgColor = BaseColor.RED
                    break
                case "Criticas":
                case "Altas":
                    txtColor = BaseColor.BLACK
                    bgColor = BaseColor.ORANGE
                    break
                case "Importantes":
                case "Medias":
                    txtColor = BaseColor.BLACK
                    bgColor = BaseColor.YELLOW
                    break
                case "Menores":
                case "Bajas":
                    txtColor = BaseColor.BLACK
                    bgColor = BaseColor.GREEN
                    break
                case "Información":
                case "Desconocidas":
                    txtColor = BaseColor.WHITE
                    bgColor = BaseColor.BLUE
                    break
            }
            Font font = new Font(FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, txtColor))
            Chunk c = new Chunk(" Detalle de vulnerabilidades de tipo: " + typeVul + " ", font)
            c.setBackground(bgColor)
            Paragraph p = new Paragraph(c)
            document.add(new Paragraph(Chunk.NEWLINE))
            document.add(p)
            def flag = 1
            if (aplication.equals("SONARQUBE")) {
                for (iss in data.issues) {
                    document.add(new Paragraph("    " + flag + ". Componente: " + iss.component.split(":")[1]))
                    document.add(new Paragraph("        - Linea: " + iss.line))
                    document.add(new Paragraph("        - Acciones: " + iss.message))
                    document.add(new Paragraph(Chunk.NEWLINE))
                    flag++
                }
            }
            if (aplication.equals("TRIVY")) {
                for (iss in data) {
                    document.add(new Paragraph("    " + flag + ". Librería: " + iss.PkgName))
                    document.add(new Paragraph("        - Versión: " + iss.InstalledVersion))
                    document.add(new Paragraph("        - Id de vulnerabilidad: " + iss.VulnerabilityID))
                    document.add(new Paragraph("        - Mensaje: " + iss.Description))
                    document.add(new Paragraph("        - Fuente: " + iss.PrimaryURL))
                    document.add(new Paragraph(Chunk.NEWLINE))
                    flag++
                }
            }
            if (aplication.equals("RAPIDSCAN")) {
                for (iss in data) {
                    def vul = iss.split('___')
                    document.add(new Paragraph("    " + flag + ". Clasificacion: " + vul[0].split(':')[1]))
                    document.add(new Paragraph("        - Vulnerabilidad: " + vul[1].split(':')[1]))
                    document.add(new Paragraph("        - Recomendación: " + vul[2].split(':')[1]))
                    document.add(new Paragraph(Chunk.NEWLINE))
                    flag++
                }
            }
        }
    }

    static def generateTableContent(){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(8)
        table.getDefaultCell().setBorderColor(BaseColor.WHITE)

        PdfPCell cellTable = new PdfPCell(new Phrase("Tabla de Contenido", FontFactory.getFont(FontFactory.HELVETICA,20, Font.NORMAL)))
        cellTable.setColspan(8)
        cellTable.setPadding(5)
        cellTable.setUseAscender(true)
        cellTable.setUseDescender(true)
        cellTable.setHorizontalAlignment(Element.ALIGN_LEFT)
        cellTable.setBorderColor(BaseColor.WHITE)
        table.addCell(cellTable)

        //------------
        PdfPCell cellContenido = new PdfPCell()
        cellContenido.addElement(new Phrase("Tabla de Contenido", fontAndSize11Bol))
        cellContenido.setPadding(5)
        cellContenido.setColspan(7)
        cellContenido.setUseAscender(true)
        cellContenido.setUseDescender(true)
        cellContenido.setHorizontalAlignment(Element.ALIGN_CENTER)
        cellContenido.setBorderColor(BaseColor.WHITE)
        table.addCell(cellContenido)

        PdfPCell cellPag2 = new PdfPCell()
        cellPag2.addElement(new Phrase("2"))
        cellPag2.setPadding(5)
        cellPag2.setUseAscender(true)
        cellPag2.setUseDescender(true)
        cellPag2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag2.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag2)

        //------------
        PdfPCell cell1 = new PdfPCell()
        cell1.addElement(new Phrase("1. Acuerdo de confidencialidad del documento.",fontAndSize11Bol))
        cell1.setPadding(5)
        cell1.setColspan(7)
        cell1.setUseAscender(true)
        cell1.setUseDescender(true)
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell1.setBorderColor(BaseColor.WHITE)
        table.addCell(cell1)

        PdfPCell cellPag31 = new PdfPCell()
        cellPag31.addElement(new Phrase("3",fontAndSize11Bol))
        cellPag31.setPadding(5)
        cellPag31.setUseAscender(true)
        cellPag31.setUseDescender(true)
        cellPag31.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag31.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag31)

        //------------
        PdfPCell cellDesc = new PdfPCell()
        cellDesc.addElement(new Phrase("    1.1 Declaración de confidencialidad",fontAndSize11))
        cellDesc.setPadding(5)
        cellDesc.setColspan(7)
        cellDesc.setUseAscender(true)
        cellDesc.setUseDescender(true)
        cellDesc.setHorizontalAlignment(Element.ALIGN_CENTER)
        cellDesc.setBorderColor(BaseColor.WHITE)
        table.addCell(cellDesc)

        PdfPCell cellPag32 = new PdfPCell()
        cellPag32.addElement(new Phrase("3"))
        cellPag32.setPadding(5)
        cellPag32.setUseAscender(true)
        cellPag32.setUseDescender(true)
        cellPag32.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag32.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag32)

        //------------
        PdfPCell cell2 = new PdfPCell()
        cell2.addElement(new Phrase("2. Descripción de las pruebas.",fontAndSize11Bol))
        cell2.setPadding(5)
        cell2.setColspan(7)
        cell2.setUseAscender(true)
        cell2.setUseDescender(true)
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell2.setBorderColor(BaseColor.WHITE)
        table.addCell(cell2)

        PdfPCell cellPag33 = new PdfPCell()
        cellPag33.addElement(new Phrase("4",fontAndSize11Bol))
        cellPag33.setPadding(5)
        cellPag33.setUseAscender(true)
        cellPag33.setUseDescender(true)
        cellPag33.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag33.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag33)

        //------------
        PdfPCell cell21 = new PdfPCell()
        cell21.addElement(new Phrase("    2.1 SAST",fontAndSize11))
        cell21.setPadding(5)
        cell21.setColspan(7)
        cell21.setUseAscender(true)
        cell21.setUseDescender(true)
        cell21.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell21.setBorderColor(BaseColor.WHITE)
        table.addCell(cell21)

        PdfPCell cellPag41 = new PdfPCell()
        cellPag41.addElement(new Phrase("4"))
        cellPag41.setPadding(5)
        cellPag41.setUseAscender(true)
        cellPag41.setUseDescender(true)
        cellPag41.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag41.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag41)

        //------------
        PdfPCell cell22 = new PdfPCell()
        cell22.addElement(new Phrase("    2.2 IaC Security.",fontAndSize11))
        cell22.setPadding(5)
        cell22.setColspan(7)
        cell22.setUseAscender(true)
        cell22.setUseDescender(true)
        cell22.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell22.setBorderColor(BaseColor.WHITE)
        table.addCell(cell22)

        PdfPCell cellPag42 = new PdfPCell()
        cellPag42.addElement(new Phrase("4"))
        cellPag42.setPadding(5)
        cellPag42.setUseAscender(true)
        cellPag42.setUseDescender(true)
        cellPag42.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag42.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag42)

        //------------
        PdfPCell cell23 = new PdfPCell()
        cell23.addElement(new Phrase("    2.3 Vulnerability Checks.",fontAndSize11))
        cell23.setPadding(5)
        cell23.setColspan(7)
        cell23.setUseAscender(true)
        cell23.setUseDescender(true)
        cell23.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell23.setBorderColor(BaseColor.WHITE)
        table.addCell(cell23)

        PdfPCell cellPag43 = new PdfPCell()
        cellPag43.addElement(new Phrase("4"))
        cellPag43.setPadding(5)
        cellPag43.setUseAscender(true)
        cellPag43.setUseDescender(true)
        cellPag43.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag43.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag43)

        //------------
        PdfPCell cell3 = new PdfPCell()
        cell3.addElement(new Phrase("3. Historial de Revisiones al documento",fontAndSize11Bol))
        cell3.setPadding(5)
        cell3.setColspan(7)
        cell3.setUseAscender(true)
        cell3.setUseDescender(true)
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell3.setBorderColor(BaseColor.WHITE)
        table.addCell(cell3)

        PdfPCell cellPag51 = new PdfPCell()
        cellPag51.addElement(new Phrase("4",fontAndSize11Bol))
        cellPag51.setPadding(5)
        cellPag51.setUseAscender(true)
        cellPag51.setUseDescender(true)
        cellPag51.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag51.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag51)

        //------------
        PdfPCell cell4 = new PdfPCell()
        cell4.addElement(new Phrase("4. Datos del proyecto.",fontAndSize11Bol))
        cell4.setPadding(5)
        cell4.setColspan(7)
        cell4.setUseAscender(true)
        cell4.setUseDescender(true)
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell4.setBorderColor(BaseColor.WHITE)
        table.addCell(cell4)

        PdfPCell cellPag52 = new PdfPCell()
        cellPag52.addElement(new Phrase("4",fontAndSize11Bol))
        cellPag52.setPadding(5)
        cellPag52.setUseAscender(true)
        cellPag52.setUseDescender(true)
        cellPag52.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag52.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag52)

        //------------
        PdfPCell cell5 = new PdfPCell()
        cell5.addElement(new Phrase("5. Hallazgos.",fontAndSize11Bol))
        cell5.setPadding(5)
        cell5.setColspan(7)
        cell5.setUseAscender(true)
        cell5.setUseDescender(true)
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell5.setBorderColor(BaseColor.WHITE)
        table.addCell(cell5)

        PdfPCell cellPag61 = new PdfPCell()
        cellPag61.addElement(new Phrase("4",fontAndSize11Bol))
        cellPag61.setPadding(5)
        cellPag61.setUseAscender(true)
        cellPag61.setUseDescender(true)
        cellPag61.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag61.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag52)


        //------------
        PdfPCell cell51 = new PdfPCell()
        cell51.addElement(new Phrase("    5.1 Resumen de Hallazgos.",fontAndSize11))
        cell51.setPadding(5)
        cell51.setColspan(7)
        cell51.setUseAscender(true)
        cell51.setUseDescender(true)
        cell51.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell51.setBorderColor(BaseColor.WHITE)
        table.addCell(cell51)

        PdfPCell cellPag62 = new PdfPCell()
        cellPag62.addElement(new Phrase("6"))
        cellPag62.setPadding(5)
        cellPag62.setUseAscender(true)
        cellPag62.setUseDescender(true)
        cellPag62.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag62.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag62)
        //------------
        PdfPCell cell52 = new PdfPCell()
        cell52.addElement(new Phrase("    5.2 Resumen de Hallazgos.",fontAndSize11))
        cell52.setPadding(5)
        cell52.setColspan(7)
        cell52.setUseAscender(true)
        cell52.setUseDescender(true)
        cell52.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell52.setBorderColor(BaseColor.WHITE)
        table.addCell(cell52)

        PdfPCell cellPag71 = new PdfPCell()
        cellPag71.addElement(new Phrase("7"))
        cellPag71.setPadding(5)
        cellPag71.setUseAscender(true)
        cellPag71.setUseDescender(true)
        cellPag71.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag71.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag71)
        //------------
        PdfPCell cell511 = new PdfPCell()
        cell511.addElement(new Phrase("      5.2.1 SAST (SonarQube).",fontAndSize11))
        cell511.setPadding(5)
        cell511.setColspan(7)
        cell511.setUseAscender(true)
        cell511.setUseDescender(true)
        cell511.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell511.setBorderColor(BaseColor.WHITE)
        table.addCell(cell511)

        PdfPCell cellPag63 = new PdfPCell()
        cellPag63.addElement(new Phrase("7"))
        cellPag63.setPadding(5)
        cellPag63.setUseAscender(true)
        cellPag63.setUseDescender(true)
        cellPag63.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag63.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag63)
        //------------
        PdfPCell cell512 = new PdfPCell()
        cell512.addElement(new Phrase("      5.2.2 IaC Security (Trivy).",fontAndSize11))
        cell512.setPadding(5)
        cell512.setColspan(7)
        cell512.setUseAscender(true)
        cell512.setUseDescender(true)
        cell512.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell512.setBorderColor(BaseColor.WHITE)
        table.addCell(cell512)

        PdfPCell cellPag64 = new PdfPCell()
        cellPag64.addElement(new Phrase(""))
        cellPag64.setPadding(5)
        cellPag64.setUseAscender(true)
        cellPag64.setUseDescender(true)
        cellPag64.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag64.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag64)
        //------------
        PdfPCell cell513 = new PdfPCell()
        cell513.addElement(new Phrase("      5.2.3 Vulnerability Checks (Rapidscan).",fontAndSize11))
        cell513.setPadding(5)
        cell513.setColspan(7)
        cell513.setUseAscender(true)
        cell513.setUseDescender(true)
        cell513.setHorizontalAlignment(Element.ALIGN_CENTER)
        cell513.setBorderColor(BaseColor.WHITE)
        table.addCell(cell513)

        PdfPCell cellPag65 = new PdfPCell()
        cellPag65.addElement(new Phrase(""))
        cellPag65.setPadding(5)
        cellPag65.setUseAscender(true)
        cellPag65.setUseDescender(true)
        cellPag65.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT)
        cellPag65.setBorderColor(BaseColor.WHITE)
        table.addCell(cellPag65)
        //------------


        return table
    }

    static def generateRevDocument(fecha){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(3)

        PdfPCell cellrev = new PdfPCell()
        cellrev.addElement(new Phrase("Número de revisión",fontAndSize11Bol))
        cellrev.setPadding(5)
        cellrev.setUseAscender(true)
        cellrev.setUseDescender(true)
        cellrev.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellrev)

        PdfPCell cellFech = new PdfPCell(new Phrase("Fecha de Revisión",fontAndSize11Bol))
        cellFech.setPadding(5)
        cellFech.setUseAscender(true)
        cellFech.setUseDescender(true)
        cellFech.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellFech)

        PdfPCell cellSumm = new PdfPCell(new Phrase("Resumen de cambios",fontAndSize11Bol))
        cellSumm.setPadding(5)
        cellSumm.setUseAscender(true)
        cellSumm.setUseDescender(true)
        cellSumm.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSumm)

        //------

        PdfPCell cellrev1 = new PdfPCell(new Phrase("1.0",fontAndSize11))
        cellrev1.setPadding(5)
        cellrev1.setUseAscender(true)
        cellrev1.setUseDescender(true)
        cellrev1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellrev1)

        PdfPCell cellFech1 = new PdfPCell(new Phrase(fecha,fontAndSize11))
        cellFech1.setPadding(5)
        cellFech1.setUseAscender(true)
        cellFech1.setUseDescender(true)
        cellFech1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellFech1)

        PdfPCell cellSumm1 = new PdfPCell(new Phrase("Creación del\n" +"documento\n",fontAndSize11))
        cellSumm1.setPadding(5)
        cellSumm1.setUseAscender(true)
        cellSumm1.setUseDescender(true)
        cellSumm1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSumm1)


        return table
    }

    static def generateDatosProyect(nomproyect, rproyect, catproyect, tpproyect){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)

        PdfPCell cellNomProyect = new PdfPCell()
        cellNomProyect.addElement(new Phrase("Nombre de proyecto",fontAndSize11Bol))
        cellNomProyect.setPadding(5)
        cellNomProyect.setUseAscender(true)
        cellNomProyect.setUseDescender(true)
        table.addCell(cellNomProyect)

        PdfPCell cellNomProyectVal = new PdfPCell()
        cellNomProyectVal.addElement(new Phrase(nomproyect,fontAndSize11))
        cellNomProyectVal.setPadding(5)
        cellNomProyectVal.setUseAscender(true)
        cellNomProyectVal.setUseDescender(true)
        table.addCell(cellNomProyectVal)

        //------------

        PdfPCell cellRepoProyect = new PdfPCell()
        cellRepoProyect.addElement(new Phrase("Repositorio",fontAndSize11Bol))
        cellRepoProyect.setPadding(5)
        cellRepoProyect.setUseAscender(true)
        cellRepoProyect.setUseDescender(true)
        table.addCell(cellRepoProyect)

        PdfPCell cellRepoProyectVal = new PdfPCell()
        cellRepoProyectVal.addElement(new Phrase(rproyect,fontAndSize11))
        cellRepoProyectVal.setPadding(5)
        cellRepoProyectVal.setUseAscender(true)
        cellRepoProyectVal.setUseDescender(true)
        table.addCell(cellRepoProyectVal)

        //------------

        PdfPCell cellCatProyect = new PdfPCell()
        cellCatProyect.addElement(new Phrase("Categoría",fontAndSize11Bol))
        cellCatProyect.setPadding(5)
        cellCatProyect.setUseAscender(true)
        cellCatProyect.setUseDescender(true)
        table.addCell(cellCatProyect)

        PdfPCell cellCatProyectVal = new PdfPCell()
        cellCatProyectVal.addElement(new Phrase(catproyect,fontAndSize11))
        cellCatProyectVal.setPadding(5)
        cellCatProyectVal.setUseAscender(true)
        cellCatProyectVal.setUseDescender(true)
        table.addCell(cellCatProyectVal)

        //------------

        PdfPCell cellTpRepoProyect = new PdfPCell()
        cellTpRepoProyect.addElement(new Phrase("Tipo de repositorio",fontAndSize11Bol))
        cellTpRepoProyect.setPadding(5)
        cellTpRepoProyect.setUseAscender(true)
        cellTpRepoProyect.setUseDescender(true)
        table.addCell(cellTpRepoProyect)

        PdfPCell cellTpRepoProyectVal = new PdfPCell()
        cellTpRepoProyectVal.addElement(new Phrase(tpproyect,fontAndSize11))
        cellTpRepoProyectVal.setPadding(5)
        cellTpRepoProyectVal.setUseAscender(true)
        cellTpRepoProyectVal.setUseDescender(true)
        table.addCell(cellTpRepoProyectVal)



        return table
    }

    static def generateSummSonarQube(cantSev1, cantSev2, cantSev3, cantSev4, cantSev5){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)
        PdfPCell cellTittle = new PdfPCell(new Phrase("SAST (SonarQube)",fontAndSize11Bol))
        cellTittle.setColspan(2)
        cellTittle.setPadding(5)
        cellTittle.setUseAscender(true)
        cellTittle.setUseDescender(true)
        cellTittle.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellTittle)

        //------------

        PdfPCell cellSevtit = new PdfPCell()
        cellSevtit.addElement(new Phrase("Severidad",fontAndSize11Bol))
        cellSevtit.setPadding(5)
        cellSevtit.setUseAscender(true)
        cellSevtit.setUseDescender(true)
        cellSevtit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSevtit)

        PdfPCell cellCanttit = new PdfPCell()
        cellCanttit.addElement(new Phrase("Cantidad",fontAndSize11Bol))
        cellCanttit.setPadding(5)
        cellCanttit.setUseAscender(true)
        cellCanttit.setUseDescender(true)
        cellCanttit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellCanttit)

        //------------

        PdfPCell cellVul1 = new PdfPCell()
        cellVul1.addElement(new Phrase("Bloqueantes",fontAndSize11))
        cellVul1.setPadding(5)
        cellVul1.setUseAscender(true)
        cellVul1.setUseDescender(true)
        cellVul1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1)

        PdfPCell cellVul1Val = new PdfPCell()
        cellVul1Val.addElement(new Phrase(Integer.toString(cantSev1),fontAndSize11))
        cellVul1Val.setPadding(5)
        cellVul1Val.setUseAscender(true)
        cellVul1Val.setUseDescender(true)
        cellVul1Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1Val)

        //------------

        PdfPCell cellVul2 = new PdfPCell()
        cellVul2.addElement(new Phrase("Críticas",fontAndSize11))
        cellVul2.setPadding(5)
        cellVul2.setUseAscender(true)
        cellVul2.setUseDescender(true)
        cellVul2.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2)

        PdfPCell cellVul2Val = new PdfPCell()
        cellVul2Val.addElement(new Phrase(Integer.toString(cantSev2),fontAndSize11))
        cellVul2Val.setPadding(5)
        cellVul2Val.setUseAscender(true)
        cellVul2Val.setUseDescender(true)
        cellVul2Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2Val)

        //------------

        PdfPCell cellVul3 = new PdfPCell()
        cellVul3.addElement(new Phrase("Importantes",fontAndSize11))
        cellVul3.setPadding(5)
        cellVul3.setUseAscender(true)
        cellVul3.setUseDescender(true)
        cellVul3.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3)

        PdfPCell cellVul3Val = new PdfPCell()
        cellVul3Val.addElement(new Phrase(Integer.toString(cantSev3),fontAndSize11))
        cellVul3Val.setPadding(5)
        cellVul3Val.setUseAscender(true)
        cellVul3Val.setUseDescender(true)
        cellVul3Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3Val)

        //------------

        PdfPCell cellVul4 = new PdfPCell()
        cellVul4.addElement(new Phrase("Menores",fontAndSize11))
        cellVul4.setPadding(5)
        cellVul4.setUseAscender(true)
        cellVul4.setUseDescender(true)
        cellVul4.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4)

        PdfPCell cellVul4Val = new PdfPCell()
        cellVul4Val.addElement(new Phrase(Integer.toString(cantSev4),fontAndSize11))
        cellVul4Val.setPadding(5)
        cellVul4Val.setUseAscender(true)
        cellVul4Val.setUseDescender(true)
        cellVul4Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4Val)

        //------------

        PdfPCell cellVul5 = new PdfPCell()
        cellVul5.addElement(new Phrase("Información",fontAndSize11))
        cellVul5.setPadding(5)
        cellVul5.setUseAscender(true)
        cellVul5.setUseDescender(true)
        cellVul5.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5)

        PdfPCell cellVul5Val = new PdfPCell()
        cellVul5Val.addElement(new Phrase(Integer.toString(cantSev5),fontAndSize11))
        cellVul5Val.setPadding(5)
        cellVul5Val.setUseAscender(true)
        cellVul5Val.setUseDescender(true)
        cellVul5Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5Val)

        return table
    }

    static def generateSummSonarQubeWthDesc(cantSev1, cantSev2, cantSev3, cantSev4, cantSev5){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)
        PdfPCell cellTittle = new PdfPCell(new Phrase("SAST (SonarQube)",fontAndSize11Bol))
        cellTittle.setColspan(2)
        cellTittle.setPadding(5)
        cellTittle.setUseAscender(true)
        cellTittle.setUseDescender(true)
        cellTittle.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellTittle)

        //------------

        PdfPCell cellDescVal = new PdfPCell()
        cellDescVal.setColspan(2)
        cellDescVal.addElement(new Phrase("Descripción: SAST (Static Application Security Testing), es el control necesario para la detección de vulnerabilidades en el código fuente.",fontAndSize11))
        cellDescVal.setPadding(5)
        cellDescVal.setUseAscender(true)
        cellDescVal.setUseDescender(true)
        cellDescVal.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellDescVal)

        //------------

        PdfPCell cellSevtit = new PdfPCell()
        cellSevtit.addElement(new Phrase("Severidad",fontAndSize11Bol))
        cellSevtit.setPadding(5)
        cellSevtit.setUseAscender(true)
        cellSevtit.setUseDescender(true)
        cellSevtit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSevtit)

        PdfPCell cellCanttit = new PdfPCell()
        cellCanttit.addElement(new Phrase("Cantidad",fontAndSize11Bol))
        cellCanttit.setPadding(5)
        cellCanttit.setUseAscender(true)
        cellCanttit.setUseDescender(true)
        cellCanttit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellCanttit)

        //------------

        PdfPCell cellVul1 = new PdfPCell()
        cellVul1.addElement(new Phrase("Bloqueantes",fontAndSize11))
        cellVul1.setPadding(5)
        cellVul1.setUseAscender(true)
        cellVul1.setUseDescender(true)
        cellVul1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1)

        PdfPCell cellVul1Val = new PdfPCell()
        cellVul1Val.addElement(new Phrase(Integer.toString(cantSev1),fontAndSize11))
        cellVul1Val.setPadding(5)
        cellVul1Val.setUseAscender(true)
        cellVul1Val.setUseDescender(true)
        cellVul1Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1Val)

        //------------

        PdfPCell cellVul2 = new PdfPCell()
        cellVul2.addElement(new Phrase("Críticas",fontAndSize11))
        cellVul2.setPadding(5)
        cellVul2.setUseAscender(true)
        cellVul2.setUseDescender(true)
        cellVul2.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2)

        PdfPCell cellVul2Val = new PdfPCell()
        cellVul2Val.addElement(new Phrase(Integer.toString(cantSev2),fontAndSize11))
        cellVul2Val.setPadding(5)
        cellVul2Val.setUseAscender(true)
        cellVul2Val.setUseDescender(true)
        cellVul2Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2Val)

        //------------

        PdfPCell cellVul3 = new PdfPCell()
        cellVul3.addElement(new Phrase("Importantes",fontAndSize11))
        cellVul3.setPadding(5)
        cellVul3.setUseAscender(true)
        cellVul3.setUseDescender(true)
        cellVul3.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3)

        PdfPCell cellVul3Val = new PdfPCell()
        cellVul3Val.addElement(new Phrase(Integer.toString(cantSev3),fontAndSize11))
        cellVul3Val.setPadding(5)
        cellVul3Val.setUseAscender(true)
        cellVul3Val.setUseDescender(true)
        cellVul3Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3Val)

        //------------

        PdfPCell cellVul4 = new PdfPCell()
        cellVul4.addElement(new Phrase("Menores",fontAndSize11))
        cellVul4.setPadding(5)
        cellVul4.setUseAscender(true)
        cellVul4.setUseDescender(true)
        cellVul4.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4)

        PdfPCell cellVul4Val = new PdfPCell()
        cellVul4Val.addElement(new Phrase(Integer.toString(cantSev4),fontAndSize11))
        cellVul4Val.setPadding(5)
        cellVul4Val.setUseAscender(true)
        cellVul4Val.setUseDescender(true)
        cellVul4Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4Val)

        //------------

        PdfPCell cellVul5 = new PdfPCell()
        cellVul5.addElement(new Phrase("Información",fontAndSize11))
        cellVul5.setPadding(5)
        cellVul5.setUseAscender(true)
        cellVul5.setUseDescender(true)
        cellVul5.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5)

        PdfPCell cellVul5Val = new PdfPCell()
        cellVul5Val.addElement(new Phrase(Integer.toString(cantSev5),fontAndSize11))
        cellVul5Val.setPadding(5)
        cellVul5Val.setUseAscender(true)
        cellVul5Val.setUseDescender(true)
        cellVul5Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5Val)

        return table
    }

    static def generateSummTrivy(cantSev1, cantSev2, cantSev3, cantSev4, cantSev5){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)
        PdfPCell cellTittle = new PdfPCell(new Phrase("IaC Security (Trivy)",fontAndSize11Bol))
        cellTittle.setColspan(2)
        cellTittle.setPadding(5)
        cellTittle.setUseAscender(true)
        cellTittle.setUseDescender(true)
        cellTittle.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellTittle)

        //------------

        PdfPCell cellSevtit = new PdfPCell()
        cellSevtit.addElement(new Phrase("Severidad",fontAndSize11Bol))
        cellSevtit.setPadding(5)
        cellSevtit.setUseAscender(true)
        cellSevtit.setUseDescender(true)
        cellSevtit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSevtit)

        PdfPCell cellCanttit = new PdfPCell()
        cellCanttit.addElement(new Phrase("Cantidad",fontAndSize11Bol))
        cellCanttit.setPadding(5)
        cellCanttit.setUseAscender(true)
        cellCanttit.setUseDescender(true)
        cellCanttit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellCanttit)

        //------------

        PdfPCell cellVul1 = new PdfPCell()
        cellVul1.addElement(new Phrase("Críticas",fontAndSize11))
        cellVul1.setPadding(5)
        cellVul1.setUseAscender(true)
        cellVul1.setUseDescender(true)
        cellVul1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1)

        PdfPCell cellVul1Val = new PdfPCell()
        cellVul1Val.addElement(new Phrase(Integer.toString(cantSev1),fontAndSize11))
        cellVul1Val.setPadding(5)
        cellVul1Val.setUseAscender(true)
        cellVul1Val.setUseDescender(true)
        cellVul1Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1Val)

        //------------

        PdfPCell cellVul2 = new PdfPCell()
        cellVul2.addElement(new Phrase("Altas",fontAndSize11))
        cellVul2.setPadding(5)
        cellVul2.setUseAscender(true)
        cellVul2.setUseDescender(true)
        cellVul2.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2)

        PdfPCell cellVul2Val = new PdfPCell()
        cellVul2Val.addElement(new Phrase(Integer.toString(cantSev2),fontAndSize11))
        cellVul2Val.setPadding(5)
        cellVul2Val.setUseAscender(true)
        cellVul2Val.setUseDescender(true)
        cellVul2Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2Val)

        //------------

        PdfPCell cellVul3 = new PdfPCell()
        cellVul3.addElement(new Phrase("Medias",fontAndSize11))
        cellVul3.setPadding(5)
        cellVul3.setUseAscender(true)
        cellVul3.setUseDescender(true)
        cellVul3.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3)

        PdfPCell cellVul3Val = new PdfPCell()
        cellVul3Val.addElement(new Phrase(Integer.toString(cantSev3),fontAndSize11))
        cellVul3Val.setPadding(5)
        cellVul3Val.setUseAscender(true)
        cellVul3Val.setUseDescender(true)
        cellVul3Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3Val)

        //------------

        PdfPCell cellVul4 = new PdfPCell()
        cellVul4.addElement(new Phrase("Bajas",fontAndSize11))
        cellVul4.setPadding(5)
        cellVul4.setUseAscender(true)
        cellVul4.setUseDescender(true)
        cellVul4.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4)

        PdfPCell cellVul4Val = new PdfPCell()
        cellVul4Val.addElement(new Phrase(Integer.toString(cantSev4),fontAndSize11))
        cellVul4Val.setPadding(5)
        cellVul4Val.setUseAscender(true)
        cellVul4Val.setUseDescender(true)
        cellVul4Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4Val)

        //------------

        PdfPCell cellVul5 = new PdfPCell()
        cellVul5.addElement(new Phrase("Desconocidas",fontAndSize11))
        cellVul5.setPadding(5)
        cellVul5.setUseAscender(true)
        cellVul5.setUseDescender(true)
        cellVul5.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5)

        PdfPCell cellVul5Val = new PdfPCell()
        cellVul5Val.addElement(new Phrase(Integer.toString(cantSev5),fontAndSize11))
        cellVul5Val.setPadding(5)
        cellVul5Val.setUseAscender(true)
        cellVul5Val.setUseDescender(true)
        cellVul5Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5Val)

        return table
    }

    static def generateSummTrivyWthDesc(cantSev1, cantSev2, cantSev3, cantSev4, cantSev5){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)
        PdfPCell cellTittle = new PdfPCell(new Phrase("IaC Security (Trivy)",fontAndSize11Bol))
        cellTittle.setColspan(2)
        cellTittle.setPadding(5)
        cellTittle.setUseAscender(true)
        cellTittle.setUseDescender(true)
        cellTittle.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellTittle)

        //------------

        PdfPCell cellDescVal = new PdfPCell()
        cellDescVal.setColspan(2)
        cellDescVal.addElement(new Phrase("Descripción: Seguridad de la infraestructura como código es un test que asimila la infraestructura del cliente para obtener las vulnerabilidades presentes en el servidor.",fontAndSize11))
        cellDescVal.setPadding(5)
        cellDescVal.setUseAscender(true)
        cellDescVal.setUseDescender(true)
        cellDescVal.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellDescVal)

        //------------

        PdfPCell cellSevtit = new PdfPCell()
        cellSevtit.addElement(new Phrase("Severidad",fontAndSize11Bol))
        cellSevtit.setPadding(5)
        cellSevtit.setUseAscender(true)
        cellSevtit.setUseDescender(true)
        cellSevtit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSevtit)

        PdfPCell cellCanttit = new PdfPCell()
        cellCanttit.addElement(new Phrase("Cantidad",fontAndSize11Bol))
        cellCanttit.setPadding(5)
        cellCanttit.setUseAscender(true)
        cellCanttit.setUseDescender(true)
        cellCanttit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellCanttit)

        //------------

        PdfPCell cellVul1 = new PdfPCell()
        cellVul1.addElement(new Phrase("Críticas",fontAndSize11))
        cellVul1.setPadding(5)
        cellVul1.setUseAscender(true)
        cellVul1.setUseDescender(true)
        cellVul1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1)

        PdfPCell cellVul1Val = new PdfPCell()
        cellVul1Val.addElement(new Phrase(Integer.toString(cantSev1),fontAndSize11))
        cellVul1Val.setPadding(5)
        cellVul1Val.setUseAscender(true)
        cellVul1Val.setUseDescender(true)
        cellVul1Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1Val)

        //------------

        PdfPCell cellVul2 = new PdfPCell()
        cellVul2.addElement(new Phrase("Altas",fontAndSize11))
        cellVul2.setPadding(5)
        cellVul2.setUseAscender(true)
        cellVul2.setUseDescender(true)
        cellVul2.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2)

        PdfPCell cellVul2Val = new PdfPCell()
        cellVul2Val.addElement(new Phrase(Integer.toString(cantSev2),fontAndSize11))
        cellVul2Val.setPadding(5)
        cellVul2Val.setUseAscender(true)
        cellVul2Val.setUseDescender(true)
        cellVul2Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2Val)

        //------------

        PdfPCell cellVul3 = new PdfPCell()
        cellVul3.addElement(new Phrase("Medias",fontAndSize11))
        cellVul3.setPadding(5)
        cellVul3.setUseAscender(true)
        cellVul3.setUseDescender(true)
        cellVul3.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3)

        PdfPCell cellVul3Val = new PdfPCell()
        cellVul3Val.addElement(new Phrase(Integer.toString(cantSev3),fontAndSize11))
        cellVul3Val.setPadding(5)
        cellVul3Val.setUseAscender(true)
        cellVul3Val.setUseDescender(true)
        cellVul3Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3Val)

        //------------

        PdfPCell cellVul4 = new PdfPCell()
        cellVul4.addElement(new Phrase("Bajas",fontAndSize11))
        cellVul4.setPadding(5)
        cellVul4.setUseAscender(true)
        cellVul4.setUseDescender(true)
        cellVul4.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4)

        PdfPCell cellVul4Val = new PdfPCell()
        cellVul4Val.addElement(new Phrase(Integer.toString(cantSev4),fontAndSize11))
        cellVul4Val.setPadding(5)
        cellVul4Val.setUseAscender(true)
        cellVul4Val.setUseDescender(true)
        cellVul4Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4Val)

        //------------

        PdfPCell cellVul5 = new PdfPCell()
        cellVul5.addElement(new Phrase("Desconocidas",fontAndSize11))
        cellVul5.setPadding(5)
        cellVul5.setUseAscender(true)
        cellVul5.setUseDescender(true)
        cellVul5.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5)

        PdfPCell cellVul5Val = new PdfPCell()
        cellVul5Val.addElement(new Phrase(Integer.toString(cantSev5),fontAndSize11))
        cellVul5Val.setPadding(5)
        cellVul5Val.setUseAscender(true)
        cellVul5Val.setUseDescender(true)
        cellVul5Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5Val)

        return table
    }

    static def generateSummRapidScan(cantSev1, cantSev2, cantSev3, cantSev4, cantSev5){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)
        PdfPCell cellTittle = new PdfPCell(new Phrase("Vulnerability Checks (Rapidscan)",fontAndSize11Bol))
        cellTittle.setColspan(2)
        cellTittle.setPadding(5)
        cellTittle.setUseAscender(true)
        cellTittle.setUseDescender(true)
        cellTittle.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellTittle)

        //------------

        PdfPCell cellSevtit = new PdfPCell()
        cellSevtit.addElement(new Phrase("Severidad",fontAndSize11Bol))
        cellSevtit.setPadding(5)
        cellSevtit.setUseAscender(true)
        cellSevtit.setUseDescender(true)
        cellSevtit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSevtit)

        PdfPCell cellCanttit = new PdfPCell()
        cellCanttit.addElement(new Phrase("Cantidad",fontAndSize11Bol))
        cellCanttit.setPadding(5)
        cellCanttit.setUseAscender(true)
        cellCanttit.setUseDescender(true)
        cellCanttit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellCanttit)

        //------------

        PdfPCell cellVul1 = new PdfPCell()
        cellVul1.addElement(new Phrase("Críticas",fontAndSize11))
        cellVul1.setPadding(5)
        cellVul1.setUseAscender(true)
        cellVul1.setUseDescender(true)
        cellVul1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1)

        PdfPCell cellVul1Val = new PdfPCell()
        cellVul1Val.addElement(new Phrase(Integer.toString(cantSev1),fontAndSize11))
        cellVul1Val.setPadding(5)
        cellVul1Val.setUseAscender(true)
        cellVul1Val.setUseDescender(true)
        cellVul1Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1Val)

        //------------

        PdfPCell cellVul2 = new PdfPCell()
        cellVul2.addElement(new Phrase("Altas",fontAndSize11))
        cellVul2.setPadding(5)
        cellVul2.setUseAscender(true)
        cellVul2.setUseDescender(true)
        cellVul2.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2)

        PdfPCell cellVul2Val = new PdfPCell()
        cellVul2Val.addElement(new Phrase(Integer.toString(cantSev2),fontAndSize11))
        cellVul2Val.setPadding(5)
        cellVul2Val.setUseAscender(true)
        cellVul2Val.setUseDescender(true)
        cellVul2Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2Val)

        //------------

        PdfPCell cellVul3 = new PdfPCell()
        cellVul3.addElement(new Phrase("Medias",fontAndSize11))
        cellVul3.setPadding(5)
        cellVul3.setUseAscender(true)
        cellVul3.setUseDescender(true)
        cellVul3.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3)

        PdfPCell cellVul3Val = new PdfPCell()
        cellVul3Val.addElement(new Phrase(Integer.toString(cantSev3),fontAndSize11))
        cellVul3Val.setPadding(5)
        cellVul3Val.setUseAscender(true)
        cellVul3Val.setUseDescender(true)
        cellVul3Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3Val)

        //------------

        PdfPCell cellVul4 = new PdfPCell()
        cellVul4.addElement(new Phrase("Bajas",fontAndSize11))
        cellVul4.setPadding(5)
        cellVul4.setUseAscender(true)
        cellVul4.setUseDescender(true)
        cellVul4.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4)

        PdfPCell cellVul4Val = new PdfPCell()
        cellVul4Val.addElement(new Phrase(Integer.toString(cantSev4),fontAndSize11))
        cellVul4Val.setPadding(5)
        cellVul4Val.setUseAscender(true)
        cellVul4Val.setUseDescender(true)
        cellVul4Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4Val)

        //------------

        PdfPCell cellVul5 = new PdfPCell()
        cellVul5.addElement(new Phrase("Información",fontAndSize11))
        cellVul5.setPadding(5)
        cellVul5.setUseAscender(true)
        cellVul5.setUseDescender(true)
        cellVul5.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5)

        PdfPCell cellVul5Val = new PdfPCell()
        cellVul5Val.addElement(new Phrase(Integer.toString(cantSev5),fontAndSize11))
        cellVul5Val.setPadding(5)
        cellVul5Val.setUseAscender(true)
        cellVul5Val.setUseDescender(true)
        cellVul5Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5Val)

        return table
    }

    static def generateSummRapidScanWthDesc(cantSev1, cantSev2, cantSev3, cantSev4, cantSev5){
        def fontAndSize11Bol = FontFactory.getFont(FontFactory.HELVETICA,11, Font.BOLD)
        def fontAndSize11 = FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL)
        PdfPTable table = new PdfPTable(2)
        PdfPCell cellTittle = new PdfPCell(new Phrase("Vulnerability Checks (Rapidscan)",fontAndSize11Bol))
        cellTittle.setColspan(2)
        cellTittle.setPadding(5)
        cellTittle.setUseAscender(true)
        cellTittle.setUseDescender(true)
        cellTittle.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellTittle)

        //------------

        PdfPCell cellDescVal = new PdfPCell()
        cellDescVal.setColspan(2)
        cellDescVal.addElement(new Phrase("Descripción: Escaneo  de vulnerabilidades de cualquier aplicación, sistema o red en busca de cualquier posible vulnerabilidad, como ataques de SQL Injection, XSS, Ataques DoS, búsqueda de DNS o Firewall, entre otros.",fontAndSize11))
        cellDescVal.setPadding(5)
        cellDescVal.setUseAscender(true)
        cellDescVal.setUseDescender(true)
        cellDescVal.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellDescVal)

        //------------

        PdfPCell cellSevtit = new PdfPCell()
        cellSevtit.addElement(new Phrase("Severidad",fontAndSize11Bol))
        cellSevtit.setPadding(5)
        cellSevtit.setUseAscender(true)
        cellSevtit.setUseDescender(true)
        cellSevtit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellSevtit)

        PdfPCell cellCanttit = new PdfPCell()
        cellCanttit.addElement(new Phrase("Cantidad",fontAndSize11Bol))
        cellCanttit.setPadding(5)
        cellCanttit.setUseAscender(true)
        cellCanttit.setUseDescender(true)
        cellCanttit.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellCanttit)

        //------------

        PdfPCell cellVul1 = new PdfPCell()
        cellVul1.addElement(new Phrase("Críticas",fontAndSize11))
        cellVul1.setPadding(5)
        cellVul1.setUseAscender(true)
        cellVul1.setUseDescender(true)
        cellVul1.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1)

        PdfPCell cellVul1Val = new PdfPCell()
        cellVul1Val.addElement(new Phrase(Integer.toString(cantSev1),fontAndSize11))
        cellVul1Val.setPadding(5)
        cellVul1Val.setUseAscender(true)
        cellVul1Val.setUseDescender(true)
        cellVul1Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul1Val)

        //------------

        PdfPCell cellVul2 = new PdfPCell()
        cellVul2.addElement(new Phrase("Altas",fontAndSize11))
        cellVul2.setPadding(5)
        cellVul2.setUseAscender(true)
        cellVul2.setUseDescender(true)
        cellVul2.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2)

        PdfPCell cellVul2Val = new PdfPCell()
        cellVul2Val.addElement(new Phrase(Integer.toString(cantSev2),fontAndSize11))
        cellVul2Val.setPadding(5)
        cellVul2Val.setUseAscender(true)
        cellVul2Val.setUseDescender(true)
        cellVul2Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul2Val)

        //------------

        PdfPCell cellVul3 = new PdfPCell()
        cellVul3.addElement(new Phrase("Medias",fontAndSize11))
        cellVul3.setPadding(5)
        cellVul3.setUseAscender(true)
        cellVul3.setUseDescender(true)
        cellVul3.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3)

        PdfPCell cellVul3Val = new PdfPCell()
        cellVul3Val.addElement(new Phrase(Integer.toString(cantSev3),fontAndSize11))
        cellVul3Val.setPadding(5)
        cellVul3Val.setUseAscender(true)
        cellVul3Val.setUseDescender(true)
        cellVul3Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul3Val)

        //------------

        PdfPCell cellVul4 = new PdfPCell()
        cellVul4.addElement(new Phrase("Bajas",fontAndSize11))
        cellVul4.setPadding(5)
        cellVul4.setUseAscender(true)
        cellVul4.setUseDescender(true)
        cellVul4.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4)

        PdfPCell cellVul4Val = new PdfPCell()
        cellVul4Val.addElement(new Phrase(Integer.toString(cantSev4),fontAndSize11))
        cellVul4Val.setPadding(5)
        cellVul4Val.setUseAscender(true)
        cellVul4Val.setUseDescender(true)
        cellVul4Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul4Val)

        //------------

        PdfPCell cellVul5 = new PdfPCell()
        cellVul5.addElement(new Phrase("Información",fontAndSize11))
        cellVul5.setPadding(5)
        cellVul5.setUseAscender(true)
        cellVul5.setUseDescender(true)
        cellVul5.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5)

        PdfPCell cellVul5Val = new PdfPCell()
        cellVul5Val.addElement(new Phrase(Integer.toString(cantSev5),fontAndSize11))
        cellVul5Val.setPadding(5)
        cellVul5Val.setUseAscender(true)
        cellVul5Val.setUseDescender(true)
        cellVul5Val.setHorizontalAlignment(Element.ALIGN_CENTER)
        table.addCell(cellVul5Val)

        return table
    }

    static def makePortada(document, fecha){
        /*****************************************************************************************************/
        /*                                    PORTADA                                                       */
        /****************************************************************************************************/
        for (i in 0..<12) {
            document.add(new Paragraph(Chunk.NEWLINE ))
        }
        Paragraph tittleDocument1 = new Paragraph("INFORME DE", FontFactory.getFont(FontFactory.HELVETICA,29, Font.BOLD))
        Paragraph tittleDocument2 = new Paragraph("VULNERABILIDADES", FontFactory.getFont(FontFactory.HELVETICA,29, Font.BOLD))
        tittleDocument1.setAlignment(Element.ALIGN_CENTER)
        tittleDocument2.setAlignment(Element.ALIGN_CENTER)
        tittleDocument2.setSpacingAfter(50)
        document.add(tittleDocument1)
        document.add(tittleDocument2)

        Paragraph fechaRev = new Paragraph("Fecha de Revisión: "+fecha, FontFactory.getFont(FontFactory.HELVETICA,15, Font.NORMAL))
        fechaRev.setAlignment(Element.ALIGN_RIGHT)
        document.add(fechaRev)
        Paragraph autores = new Paragraph("Autores: Estudiantes Instituto CIISA", FontFactory.getFont(FontFactory.HELVETICA,15, Font.NORMAL))
        autores.setAlignment(Element.ALIGN_RIGHT)
        document.add(autores)
        document.newPage()
    }

    static def makeIndice(document){
        /*****************************************************************************************************/
        /*                                           INDICE                                                  */
        /****************************************************************************************************/
        document.add(generateTableContent())
        document.newPage()
    }

    static def makeSeccion1(document){
        /*****************************************************************************************************/
        /*                                           SECCION 1                                               */
        /****************************************************************************************************/
        Paragraph hsecc1 = new Paragraph("1. Acuerdo de confidencialidad del documento.", FontFactory.getFont(FontFactory.HELVETICA,20, Font.NORMAL))
        document.add(hsecc1)
        document.add(new Paragraph(Chunk.NEWLINE ))
        Paragraph tsecc1 = new Paragraph("1.1 Declaración de confidencialidad.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc1.setSpacingAfter(5)
        document.add(tsecc1)
        Paragraph psecc11 = new Paragraph("Toda la información contenida en este documento se proporciona con la confianza de que el único propósito es describir y evidenciar hallazgos en pruebas de seguridad y no se utilizará para ningún otro propósito, y no podrá ser publicado o divulgado en su totalidad o en parte, a cualquier otra persona o empresa sin el permiso previo de cliente por escrito. Estas obligaciones no se aplicarán a la información que se publica o ya se conoce legítimamente de otra fuente del cliente.\n" +
                "© 2021 Proyecto IADSO Instituto Profesional CIISA\n", FontFactory.getFont(FontFactory.HELVETICA,12, Font.NORMAL))
        psecc11.setAlignment(Element.ALIGN_JUSTIFIED)
        document.add(psecc11)
        document.newPage()
    }

    static def makeSeccion2(document){
        /*****************************************************************************************************/
        /*                                           SECCION 2                                               */
        /****************************************************************************************************/
        Paragraph hsecc2 = new Paragraph("2. Descripción de las pruebas.", FontFactory.getFont(FontFactory.HELVETICA,20, Font.NORMAL))
        hsecc2.setSpacingAfter(5)
        document.add(hsecc2)
        Paragraph psecc2 = new Paragraph("Para la revisión de vulnerabilidades se ejecutaron las siguientes pruebas", FontFactory.getFont(FontFactory.HELVETICA,12, Font.NORMAL))
        document.add(psecc2)
        psecc2.setSpacingAfter(5)
//------------
        Paragraph tsecc21 = new Paragraph("2.1 SAST.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc21.setSpacingAfter(5)
        document.add(tsecc21)
        Paragraph psecc21 = new Paragraph("Herramienta a utilizar: SonarQube.\n" +
                "Descripccion:\n" +
                "El análisis consiste en examinar el código fuente para detectar y reportar las debilidades que pueden conducir a vulnerabilidades de seguridad. La herramienta analiza el código fuente detectando vulnerabilidades de seguridad tales como “SQL Injection”, “Log Injection” o “X-site scripting” y otras vulnerabilidades que puedan estar presentes. El análisis incluye la verificación de cumplimiento de estándares de desarrollo y reglas de diseño basandose en el OWASP TOP 10 de vulnerabilidades, para mas referencias en https://owasp.org/www-project-top-ten/. \n", FontFactory.getFont(FontFactory.HELVETICA,12, Font.NORMAL))
        psecc21.setAlignment(Element.ALIGN_JUSTIFIED)
        document.add(psecc21)
        document.add(new Paragraph(Chunk.NEWLINE ))

//------------
        Paragraph tsecc22 = new Paragraph("2.2 IaC Security.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc22.setSpacingAfter(5)
        document.add(tsecc22)
        Paragraph psecc22 = new Paragraph("Herramienta a utilizar: Trivy.\n" +
                "Descripción:\n" +
                "El análisis  consiste en revisar la imagen de contenedores Dockers para encontrar vulnerabilidades frecuentes en los servidores, este contendor asimila la infraestructura física de las dependencias del cliente por lo que las vulnerabilidades serán referenciales.\n", FontFactory.getFont(FontFactory.HELVETICA,12, Font.NORMAL))
        psecc22.setAlignment(Element.ALIGN_JUSTIFIED)
        document.add(psecc22)
        document.add(new Paragraph(Chunk.NEWLINE ))
//------------
        Paragraph tsecc23 = new Paragraph("2.3 Vulnerability Checks.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc23.setSpacingAfter(5)
        document.add(tsecc23)
        Paragraph psecc23 = new Paragraph("Herramienta a utilizar: Rapidscan.\n" +
                "Descripción:\n" +
                "El análisis consiste en la detección de vulnerabilidades presentes en los servidores, este análisis contempla lo siguiente puntos acordados: \n", FontFactory.getFont(FontFactory.HELVETICA,12, Font.NORMAL))
        psecc23.setAlignment(Element.ALIGN_JUSTIFIED)
        document.add(psecc23)
        Paragraph psecc23l = new Paragraph(
                "   - Identificar presencia de DNS/HTTP Load Balancers & Web Application Firewalls.\n" +
                        "   - Verificaciones para Joomla, WordPress and Drupal\n" +
                        "   - Vulnerabilidades relacionadas con SSL (HEARTBLEED, FREAK, POODLE, CCS Injection, OGJAM, \n" +
                        "     OCSP Stapling).\n" +
                        "   - Puertos comúnmente abiertos.\n" +
                        "   - Transferencias de zona DNS utilizando múltiples herramientas (Fierce, DNSWalk, DNSRecon, \n" +
                        "     DNSEnum).\n" +
                        "   - Fuerza bruta de subdominios (DNSMap, amass, nikto)\n" +
                        "   - Abrir directorio / archivo de fuerza bruta.\n" +
                        "   - Banners superficiales XSS, SQLi y BSQLi.\n" +
                        "   - Ataque DoS de Slow-Loris, LFI (inclusión de archivos locales), RFI (Remote File Inclusion) & RCE \n" +
                        "     (Remote Code Execution).\n", FontFactory.getFont(FontFactory.HELVETICA,11, Font.NORMAL))
        document.add(psecc23l)
        document.newPage()
    }

    static def makeSeccion34(document, fecha, nomproyect, rproyect, catproyect, tpproyect){
        /*****************************************************************************************************/
        /*                                           SECCION 3 & 4                                           */
        /****************************************************************************************************/
        Paragraph hsecc3 = new Paragraph("3. Historial de Revisiones al documento.", FontFactory.getFont(FontFactory.HELVETICA,20, Font.NORMAL))
        document.add(hsecc3)
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateRevDocument(fecha))
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(new Paragraph(Chunk.NEWLINE ))

//------------
        Paragraph hsecc4 = new Paragraph("4. Datos del proyecto.", FontFactory.getFont(FontFactory.HELVETICA,20, Font.NORMAL))
        document.add(hsecc4)
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateDatosProyect(nomproyect,rproyect,catproyect,tpproyect))
        document.newPage()
    }

    def makeSeccion5(document,trivyData,trivyVulSevCritical,trivyVulSevHigh,trivyVulSevMedium,trivyVulSevLow,trivyVulSevUnk,vulBlocker,vulCritical,vulMajor,vulMinor,vulInfo,rapidScanVulCritical,rapidScanVulHigh,rapidScanVulMedium,rapidScanVulLow,rapidScanVulInfo){
        /*****************************************************************************************************/
        /*                                           SECCION 5                                              */
        /****************************************************************************************************/
        Paragraph hsecc5 = new Paragraph("5. Hallazgos.", FontFactory.getFont(FontFactory.HELVETICA,20, Font.NORMAL))
        hsecc5.setSpacingAfter(5)
        document.add(hsecc5)
        Paragraph tsecc51 = new Paragraph("5.1 Resumen de Hallazgos.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc51.setSpacingAfter(5)
        document.add(tsecc51)
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateSummSonarQubeWthDesc(vulBlocker.total,vulCritical.total,vulMajor.total,vulMinor.total,vulInfo.total))
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateSummTrivyWthDesc(trivyVulSevCritical.size(),trivyVulSevHigh.size(),trivyVulSevMedium.size(),trivyVulSevLow.size(),trivyVulSevUnk.size()))
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateSummRapidScanWthDesc(rapidScanVulCritical.size(),rapidScanVulHigh.size(),rapidScanVulMedium.size(),rapidScanVulLow.size(),rapidScanVulInfo.size()))
        document.newPage()
        Paragraph tsecc52 = new Paragraph("5.2 Detalle de Hallazgos.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc52.setSpacingAfter(5)
        document.add(tsecc52)
        Paragraph tsecc521 = new Paragraph("5.2.1 SonarQube.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc521.setSpacingAfter(5)
        document.add(tsecc521)
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateSummSonarQube(vulBlocker.total,vulCritical.total,vulMajor.total,vulMinor.total,vulInfo.total))
        document.add(new Paragraph(Chunk.NEWLINE ))

//PROCESAMIENTO DE VULNERABILIDADES
        proccessIssues(vulBlocker,document,"Bloqueantes","SONARQUBE")
        proccessIssues(vulCritical,document,"Criticas","SONARQUBE")
        proccessIssues(vulMajor,document,"Importantes","SONARQUBE")
        proccessIssues(vulMinor,document,"Menores","SONARQUBE")
        proccessIssues(vulInfo,document,"Información","SONARQUBE")
        document.newPage()

        Paragraph tsecc522 = new Paragraph("5.2.2 Trivy.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc522.setSpacingAfter(5)
        document.add(tsecc522)
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateSummTrivy(trivyVulSevCritical.size(),trivyVulSevHigh.size(),trivyVulSevMedium.size(),trivyVulSevLow.size(),trivyVulSevUnk.size()))
        document.add(new Paragraph(Chunk.NEWLINE ))

        proccessIssues(trivyVulSevCritical,document,"Críticas  ","TRIVY")
        proccessIssues(trivyVulSevHigh,document,"Altas","TRIVY")
        proccessIssues(trivyVulSevMedium,document,"Medias","TRIVY")
        proccessIssues(trivyVulSevLow,document,"Bajas","TRIVY")
        proccessIssues(trivyVulSevUnk,document,"Desconocidas","TRIVY")
        document.newPage()

        Paragraph tsecc523 = new Paragraph("5.2.3 Rapidscan.", FontFactory.getFont(FontFactory.HELVETICA,16, Font.NORMAL))
        tsecc523.setSpacingAfter(5)
        document.add(tsecc523)
        document.add(new Paragraph(Chunk.NEWLINE ))
        document.add(generateSummRapidScan(rapidScanVulCritical.size(),rapidScanVulHigh.size(),rapidScanVulMedium.size(),rapidScanVulLow.size(),rapidScanVulInfo.size()))
        document.add(new Paragraph(Chunk.NEWLINE ))

        proccessIssues(rapidScanVulCritical,document,"Críticas  ","RAPIDSCAN")
        proccessIssues(rapidScanVulHigh,document,"Altas","RAPIDSCAN")
        proccessIssues(rapidScanVulMedium,document,"Medias","RAPIDSCAN")
        proccessIssues(rapidScanVulLow,document,"Bajas","RAPIDSCAN")
        proccessIssues(rapidScanVulInfo,document,"Información","RAPIDSCAN")
    }
}
