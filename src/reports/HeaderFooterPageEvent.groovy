package reports

import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.codec.Base64

import java.time.LocalDate


class HeaderFooterPageEvent extends PdfPageEventHelper {

    private PdfTemplate t
    private Image total

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16)
        try {
            total = Image.getInstance(t)
            total.setRole(PdfName.ARTIFACT)
        } catch (DocumentException de) {
            throw new ExceptionConverter(de)
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer)
        addFooter(writer)
    }

    private void addHeader(PdfWriter writer){
        PdfPTable header = new PdfPTable(2)
        try {
            // set defaults
            header.setWidths(new int[]{35, 60})
            header.setTotalWidth(527)
            header.setLockedWidth(true)
            header.getDefaultCell().setFixedHeight(40)
            header.getDefaultCell().setBorder(Rectangle.BOTTOM)
            header.getDefaultCell().setBorderColor(BaseColor.WHITE)

            // add image
            Image logo = Image.getInstance(HeaderFooterPageEvent.class.getResource("/drawables/logo.png"))
            //String b64Image =  "iVBORw0KGgoAAAANSUhEUgAAASwAAACECAYAAAAqRRZCAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAFQ5JREFUeNrsnU9sXEcdxyeocCFVNgJEIrVkAQU4ULopHFq1qdcSSDgc4iBSGqnC64BUBDSJL0WkINsiTUUvdhxAVIJ4g5BIG4TtQ5sDSN40reBQ6i30UBrabChSwqF0Q8OFC7zv88z6t7Pz/u5b+633+5GevPv2vZl5zzvf/f1+7zczShFCCCGEEEIIGVC2dHPyB35197L3p8zbmA1vP/THLbwLhATzHt4CQggFixBCKFiEEAoWIYRQsAghhIJFCKFgEUIIBYsQQihYhBAKFiGEULAIIYSCRQihYBFCCAWLEEIoWIQQChYhhFCwCCGEgkUIoWARQggFixBC8itY9374LrX0+Z/4f7MqKwu2vW+r+uXQj/gNISRH3JKHRvhC420v/vNlde7N59Sv33w20fmHPvYl9ehnvq5uf//OzNp0+p4fqH233e+XnbQ9hJABcAkhXKfv+b5aGf2tLxRRFtDDn/qqfyzOyVKs0A6IFTjx2WN+XYQQCpYTiA9E6I2Dv/Msp2+0CQZeY9/L+xfU456YZClUpvwfe3XL96iHEELBihSPR+/4ui9On96+2998EfP29crqefhTD3aI4IOetZdFjI0QsokFSwrXtvfd6m+9BIIIMXQhrS5CCAVrwwlz/WB1wRUlhFCwNpxDMdy+hz/5VfWRjGNmhBAKVmKX80SMwDqOO03XkBAK1ka7gnGD+DLlgRCyvtwy6DcAAvRgRM6XDZJKX1w6oG789ya/QYIjxx8reX8K+m1j7uTjjRy2cVm8nfDaWOd/joLVN6R5+reabvEN9difZvkNamfG28r69bS3Ta2zGEEsS/ptM0CMyuJ1wTq/6P0p9kJwY7aN0CUMBk/90iaeIsueuVm5A4KwrLeZFOdXxPmVnLWNDLKFhad9QTlXcUHsq/zc1/gt6iM8y2YL7wIFKzUY8HzX4pfVgx//kjr0sX2pLZ63/nNN/ey1p9WFt56PdXwWT/v8RFPPSnvyzz/nNymhO+TtGxX7QM3bX3OcCxdt1Hbf4LLpcxrClZPlFbz9ZdsFE/uUqU/E3naJ83eJYxu6nlC3zorh1b3PmzHa1uZ66jpGhWtqrnUR5VGwcsDfPbFBp8cGEfim526N3DYU+eQOQe9zbz7rz6bw6juXY9d3KMOhNsjNOvfGs/41kEh3yO98XqdUVocEk97+aa9TTonOC7dsPqRcEyfDcZMhdUKYhvVrGXQ31paMvUn3sGLVE1SmcpQzrI+Jalsr1qdFfN4hzn7Z3ud4SFClYOUICM93/nDCe3XCF5aR2+/vSCN47h/P+yKBv2l58i+/yKzNt2/dScGKTzHks0nVHqi3Yz113ZmLm9AKLYaIldL7573jGi5LlIKVA36trSdpaX38/Be6Tifg/FYbCtwaWAlL6HjajVoRHbcsOqTsvNuDXCJtlU155x4TIgdxmxB1BuKdP6xdsRlhVaGNZ4VLlgq0zSt7Vpfrapsp+6i4XuyDNbWo78+8cCnHtOVGwcorzHfaVCC2MyE6NOJZNYdLZlPxjqtGxHFkTKmZxBLR8aarYtfVrCwZXXZU22SMyxcrcX/GhajDbRwf1C8PM91JnqlZ7uEVr/POaPdps1EKEF5lBfcLg/yFoGCRPHPA6rzorMeEcG2mzlsQAtXgv56CRfoMuFLetke7QHZmuIxXEQoWIbkRrqoWLmyL4qPKJrrMliDrQLsS7wv8FlCwSP8JFwL2cBPzkEBZiLkvLtINHLU+q7iEbRC5hd1gFYwNjDMF86v/er2r/C8SH8+ymNLiJDtpOaYwFF2Z7hFIIRxyZKNLUSkhjub9vaFWM+RxbDHmpRUdZS8JoUISLcrEU8tt2v01LFGwNhiM68PQHBc3/vuueuq1p2OLS5qsc2S9x10ZBykWnFpm3ZiM+HzWssCQ19VUa8mlYVnpLmqWMBpR8bPR9fCcmtifZDklKXaybabsqlc2crFKIe5u3b5mCtYGgEzxsIHId2zfrbPfV6eDCZu/CmMTkwpWkilmzLJfpj2kp9RV++N+aQmdksN4BMjxmknjnumcp9kIIUL5C6pzrN9ZhzUky4bYmbYFMaw/d42drKrV/KyBHk/Y1cj1D/zq7mUVnfAXy8KJWmL+nM5Oj5psb//vv+2LVlwweDnNrA1J64nD2w/9kTMJuF1DOeg41jxVQYOLU9RXd4mEGOicqPw4ZVvlq0EeitO3gtULIYErurzvl6nWOEQdqIuCRcj6MdBPCU98Lv0y9BDZQwmnViaEULBSC063i0mc+Oyxnq1ATQihYLXIYiVnM7c7IYSC1TOQFnF7RguioixMOkgIoWD1hDsyFpg7tn+C3yRCBkWw3rp5LdEUx0GgjFffeT3yuCf//IvMEj9RJycEJGR9yM2c7vt//y219PmfpnavIBwoI44Qob6n/vp016vmAK5NGA89P3tRv63mYQoVPfTHJyAJlfeQguUGQpNWtDC275E//DCR1fTUa+e6WqUHIJk16+TRTQym9i3r1zXVxbTDEZ26rOsqqc5J8UyGvJnxQQ79meI9pEuYSrTOJXCxcOzXLn43sYuH4x97abarttK6yp0FAtExi6DaQ3pKurOXeKdoYWUqWmacXtQwHIhVN2P6YJnBQkqz5NeTf/k5B0DnS6xGlXuwdNhKO9N9dpkYr3hRv24M4v85t9PLRIkWrBt7Foc0POLV8/LobxOdg0Vbs6h7AEQE1kwzzVi7FOPnpFjB9TsQVUbcuJWeQ74Qc4oa17mxpz3WLq1zfGLcNQnNBIBx22sWe+2HMYu5GEsYBvKc7KlfIDJZPplLOgC6FwOfwWYYSygsnTDXa1h2Di1S+CfLpa6MFYGY02wMkXtH7BqP07m985aFGAw7ypTLfkmLbVrEwczxOG5Mvz2Fr4nqnHVhVq4YFOOeQXhbi6fq+bfMMRNiNeuiWlvE1bVC9llbnCP+T3U9wystrKTAkvm353qZpeWzFqvVOs75gf5t740eZoOnkQy0h4rVQopTlwM6jt8RMZmdq6MLSmkskaAfWy1WV5R7ihrUtYClt6x6iqK8oB/xY955N6zVrcPume3KlkTZBavu0YAy8NmkXoC1Kiywhbj3k4KVECNQSEfohVggFoXAPemaGfuXXbw/6hIAbZmUhEWBTvWKt92p1uaWQkc/m2LW0G6uoyDKO6WvZ0hYXBDSxZD5qXA8rMir2toy14jXUwH3zFz/Df1+V8xYFc6bFecpUZdcgNUI7KhtMeoyCirnDyX6ZopkJmf2RbyqKDrQHtmZvc+HAiyPMfF6QlotsEZEbGpMxZvPPIs5z0ct99WUiVlBlRatgj7OZc21uX4QNrW2EGrJjh2J8/akya3S7as7/idVbSmGWX01y71dzPP3jItQkKwoWDGQuJZOOcSVq623m2IC7MZKclh1ck71O4OMdoegBLlrUjgaWV5LSHkNy01d1q5p7uEiFCRPVtpyiAimEc40SBEpRLSplKN7V1HtiaVhQmbPH49zyohzKUeAnoJFSIS11QVZikhB9fgpeEZiNaWiF+ywwVNR+8ksxBoB+v3aFc7d/PEULJInwla2aSbowMWM3Cu4chNZtKnHHBWvD8iYlHcv/hdgZaHtU3rRjYouoyhEH/tyN5SDgkV6QRK3rCmOr6f5VdfLe8ld6Gxp3RoZb1rPZMpiFvfbzg+Lce/ME8ZZnedlnszuz6NgMehOskJaNFhk9Jj4lS+FdEgpCDOuZdlhMcVYrr0qXsOtmUfWuGMrxujA5loQwwpqU6nbJeQtMSzK2SPktXfhKiY992oOrUdaWCR7HIuMzuhf7CiQqzUqLKOKZS1JdzHM2plW7ZnlFeVejHQ6hvWFNpmYEIT3WMo2xaEq2gmhnUzR3ob5QfDOX9FWYlEFJ8Zi/7JqX1XbzsHK5QrTtLBIlkwk/WXWLkzXroeOWQ1nYRnop2TVdbxn3eaOyUHcJv5UjmEpmYcKZUusqglGC9DCIn1rZWHl5I/qDjOkOwQ640VtBbRiVdZ5E955p/R5u4T72NBuSj1Op9b5Ttv1I/47Ved8WDcsIRoOKWtct2lUl1WwyrHbVBXWViPAGgtyQfc42uxq74TrHuo0BdQ5pu8dXmO0wKI+3nZdce64PnabrrOpz6nleRB07gc/DxJcSJUQuoSEEAoWIYRQsAghhIJFCKFgEUIIBYsQQihYhJC+hImjJBP0eMFEg57TrEKT4+uXOYnDYcmXegzivLhfZ7PILE/SBgoWGXRmVLIkYnSm4QEUdoiUXHQjt8NgNp1gfeglepRZ8jZvwSAwb4nVOG/JOgnWB1/iSBLSQq5KDDAmsCLe26ssNwbUbX5Fb82o9RaTYq+tSMEiJLizVK3OWZaCFTZPuO7IGGQ8pHf5A6bDJqPT5ZfFORBADJRuDd614mpmyhVTjxnsWw2anVQvzDAkLCIIcj3pJHnCFSyrtcHdDT1n2GLY7Kh6UHRRXKeSPwzWGodt12tPhqjnx6o47lnDWq3I3Nttqn0w9lV9v5ox2uksm4JF+t3qmFed81ahs2AOKnSSYUfHc51jQIep6dcyrtZQnZMIQpCOeuW1La/liDPJdik979eBuLOj6s4s1zqUzDgWZZWLnRYd55TFa/lDIK+3ba4u3Yb5kGZWhVgth9Q5Ke9XyL3qKDsLGIQiGylWU5bwyAnllO4E8xHnxKUYsB8d7qi1byGiA5ZV+wKoYddYVO1PBF3Ma6GQgrmgups22bZG5zP6t6FtcpLBiuqcxqemt8zdflpYZCORi6iOW0upL+vOMWotKjFm/XJPWAu2BnVyuHFnjTtnzV9esiwbacFMmFiTduGMUGFm1IkYVtZRqzMf0LOzFi1hPCosolHVvijtAZmiELSwRMz73GqD657perY4RE9ee8kSMMN0r5cIo4VFNsq6kvO816VLpPOzqpbr5rKUOgQjJB50yoo9LYVYTy2Rk4Fx/dq2AKMoWR26Ido5HlCvvMZqBvlUzjbEuGdBbl3Qde+XlmIvoIVFNoqCw20J+rwgRK7lPvZo3TxZ7yuOz+uqfQHSKDGR4mMLRV3MFS/rHYpoQzeCFSl+4gFBHEGuChfRt4z17KenVECAnhYW6XeMCyi3SoSYbFSW/NWExxctyzEpjSwbHyUg+kfhinZXJ63NVZ6xFJvWNcN9vKKftFKwyKaiqdYCta6tEWWh9YkbnKbN632dC6JO3PtpsQWJIKysj+pjGlbbF7J0E+kSkjxQj5n0mDR+1C3bHPuGElp5NbUWnyqp9lSDQsh1ynMWu7yOpnCryxExsaJwuYctwZ2MsNym1Opq0hVtkZmy9qvul0PrXwtr69at6uTJJ9TIyL6Oz3bv3q3m5k77f0mukb/EsRYl1Z1CPhEs96BdUoRGZbv061JCd026SnZ7KwH1SrdzrNsFW62yyyn+P0ndzqrqXHpsMC0siJURpL179/r7Llx4rk2szDFHjjyiLl++TGnIIfrRvglgo0OueO9hSdywLJyS9Uu/KDr6gj7nqrSIsGxYF02rCYukqNt1VpctF2ptxIxJLam1p5xIuoSFhkx1e+jSknWNM8LiWdGB7GZKF3FJtSd+7rJEcchh4Za0pdTQde4PcHPLouy6biPaLNM5Lg6sYB0//lib9XT8+HH1t79d9oXJiJUUtpGRL1Id8guEZVl0zGMxzpErPBdUZ3C+K9cDVhzyq9RaomVRuQPO4zHLw5qBY6JTlx1WDjr6rCXm41Ybil1cFiyeMWHpVGK6sHGSTcsB90damNWsvjB95xJev36t7f3NmzfVu+/e9F9fu9b+mf2erCt2IN3VmbF/OCRG07TPFSs8L8Zwf2TWdTOkfXWHSzMcEKNCva65pgLr0tbLtMPNMp152JFPVhXX2YjpqjnboMse1nU1I+7ZAcf/q6Hb7/p/NlRwLM/cq0ZWX6quplu47757N2QhVVhZIyMjvlhJt0+6i9iHz3BMv/DCCy8O9PQXIkbUiPslN7lZvZwMUNfRzKrjaTeqniZHSWelXxFitCVlGcWohNRu2tkr+lKwwMGDD6h6faUjRgXReuCBB9QzzzzTV2JFwSK2SNoirAV9UrjOtUGYUkbSt2kN588/49wPkTpz5gy/8aTfWRFZ8EFcHLSbkhvB2rNnj5+msGPHDv/19evX/RjUhQsXWk8BwY4dO9W+fSOt99euXW99jvOwtf7jKyv+JkEdcCd37tzp12XquXTpkr/ZMTJYbDgHTyTx2gT8US7EEefI9hGyTsz2eqAxBSvI/D1yxHfxJBATI14HDx70nwZCTHbu3KHGxw+3iZIULPmZUmdagiXjW0H1oB2w3Obm5lrlId/LPHm0BRZAyCCAiJcRkiHb1VrKh537VcsykE3BSgCEyhYrG4jMyZMn1eHD6ae/hvDESSaFNWUEC9abS6xc4oVrCHJTCUmKDnTX9NtF3pGcCNbhw4c7nXfPKpLuF0CAPS0oS7qKAOKCelDHffftbdV1/Pj3WsfAooNLCgsKwX20AW6gcROlmMHSomARsokFCyJiWzCwosyTP4gC3MHTp+c6YlFJsC0rxK2MFYUYFIL0pi12PXNzp3whsp9G4jy4mAbExAghm1ywJBAFKQyITWUR0LbFBjGr8+d/4wfbjeUGoYII2cCiMufb7bXLJIRscpfQFodelQtBkoIjg+1gfHzV8oJLKAXOBPLDxIoQsj4MzHxYcCujBBECBjcPqRMAcSm8p1gRQsFaV2A1HTz4FT92BWsL1pQLxLHMLBBHjrQvpoJzTG5XNzE1QkgfuoRm0LLBDlxDPLAvqyliUBYC6PJpHqwp5F8ZkQK33rrV3y/jUhCr8fFKm5V26dIL/AYRMigWlp2qAIGAeECoIBjInTpzZr61rxswYBpl2S4eUhfs7PZVcesMokuxisodI4RsMgvLPBWUaQeuRFK837v3ft+lSwPORy4VWB2+c7r19M8M0bHbZbt88smiKYcQMkAWFnjiiZOxng52EzNyWUsmmdQWK5negKRRW7Ts8YqEkAESLDNvVVAQHMzPn/Hcw8dT14FAO9IVwuowAiUz3ZE0GhQ/Y9CdkPUnV/Nh2dYLxvJduvR8mwVmD9mRiZ2Ie0lrCufb8SmcWyrt8QPr8rjVJ4fuGUoRkJd1mqeE2Gdia7IdaeF8WIT0kWANOhQsQnLuEhJCCAWLEELBIoQQChYhhFCwCCEULEIIoWARQggFixBCwSKEEEIIIYQQ0p/8X4ABAIoMT1fuZYvnAAAAAElFTkSuQmCC"
            // byte[] decoded = Base64.decodeBase64(b64Image.getBytes())
            //Image logo = Image.getInstance(Base64.decode(b64Image))
            logo.scalePercent(800f)
            header.addCell(logo)

            // add text
            PdfPCell text = new PdfPCell()
            text.setPaddingBottom(15)
            text.setPaddingLeft(10)
            text.setBorderColor(BaseColor.WHITE)
            header.addCell(text)

            // write content
            header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent())
        } catch(DocumentException de) {
            throw new ExceptionConverter(de)
        } catch (MalformedURLException e) {
            throw new ExceptionConverter(e)
        } catch (IOException e) {
            throw new ExceptionConverter(e)
        }
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3)
        try {
            // set defaults
            footer.setWidths(new int[]{24, 2, 1})
            footer.setTotalWidth(527)
            footer.setLockedWidth(true)
            footer.getDefaultCell().setFixedHeight(40)
            footer.getDefaultCell().setBorder(Rectangle.TOP)
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY)

            // add copyright
            footer.addCell(new Phrase("\u00A9 2021 Proyecto IADSO Instituto Profesional CIISA " + LocalDate.now().getYear(), new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)))

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT)
            footer.addCell(new Phrase(String.format("Pag. %d de", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)))

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total)
            totalPageCount.setBorder(Rectangle.TOP)
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY)
            footer.addCell(totalPageCount)

            // write page
            PdfContentByte canvas = writer.getDirectContent()
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT)
            footer.writeSelectedRows(0, -1, 34, 50, canvas)
            canvas.endMarkedContentSequence()
        } catch(DocumentException de) {
            throw new ExceptionConverter(de)
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length()
        int totalWidth = totalLength * 5
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0)
    }
}