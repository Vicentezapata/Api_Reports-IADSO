import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import groovy.json.JsonSlurper
import reports.GeneratePDFDocument
import reports.HeaderFooterPageEvent
import java.text.SimpleDateFormat


Document document
PdfWriter writer
GeneratePDFDocument pdf = new GeneratePDFDocument()
def date = new Date()
/*
this.NOM_PROYECT      = "IADSO_CIISA_IECS_REPO_TEST"
this.REPO_PROYECT     = "https://github.com/Vicentezapata/IADSO_CIISA_IECS_REPO_TEST.git"
this.CAT_PROYECT      = "WEBPAGE"
this.TP_REPO_PROYECT  = "Público"
this.TRIVY_REPORT     = "C:\\Users\\vicen\\Desktop\\Trivy.txt"
this.RAPIDSCAN_REPORT = "C:\\Users\\vicen\\Desktop\\OutputRapidscan.txt"
this.SONARQUBE_SERVER = "http://localhost:9200"
String username = "admin";
String password = "ciisa2021"
*/
this.NOM_PROYECT      = args[8]
this.REPO_PROYECT     = args[7]
this.CAT_PROYECT      = args[6]
this.TP_REPO_PROYECT  = args[5]
this.TRIVY_REPORT     = args[4]
this.RAPIDSCAN_REPORT = args[3]
this.SONARQUBE_SERVER = args[2]
String username       = args[0]
String password       = args[1]

def sdf = new SimpleDateFormat("dd-MM-yyyy")
this.FECHA            = sdf.format(date)
String projectKey = this.NOM_PROYECT;

//PROCESMIENTO DE INFORMACION
Object trivyData           = cleanTrivyReport(TRIVY_REPORT)
Object trivyVulSevCritical =  getTrivySev(trivyData,"CRITICAL")
Object trivyVulSevHigh     =  getTrivySev(trivyData,"HIGH")
Object trivyVulSevMedium   =  getTrivySev(trivyData,"MEDIUM")
Object trivyVulSevLow      =  getTrivySev(trivyData,"LOW")
Object trivyVulSevUnk      =  getTrivySev(trivyData,"UNKNOWN")

Object vulBlocker  = getData(projectKey,username,password,"BLOCKER",SONARQUBE_SERVER)
Object vulCritical = getData(projectKey,username,password,"CRITICAL",SONARQUBE_SERVER)
Object vulMajor    = getData(projectKey,username,password,"MAJOR",SONARQUBE_SERVER)
Object vulMinor    = getData(projectKey,username,password,"MINOR",SONARQUBE_SERVER)
Object vulInfo     = getData(projectKey,username,password,"INFO",SONARQUBE_SERVER)

List rapidScanData        = cleanRapidscanReport(RAPIDSCAN_REPORT)
List rapidScanVulCritical  = getRapidsSev(rapidScanData,"CRITICAL")
List rapidScanVulHigh = getRapidsSev(rapidScanData,"HIGH")
List rapidScanVulMedium    = getRapidsSev(rapidScanData,"MEDIUM")
List rapidScanVulLow    = getRapidsSev(rapidScanData,"LOW")
List rapidScanVulInfo     = getRapidsSev(rapidScanData,"INFO")

//GENERACION DE COMPONETE
def documento="INFORME DE VULNERABILIDADES - "+this.NOM_PROYECT+".pdf"
document = new Document(PageSize.A4,36,36,90,55);
writer= PdfWriter.getInstance(document, new FileOutputStream(documento));
HeaderFooterPageEvent event = new HeaderFooterPageEvent()
writer.setPageEvent(event)

document.open();
pdf.makePortada(document,FECHA)
pdf.makeIndice(document)
pdf.makeSeccion1(document)
pdf.makeSeccion2(document)
pdf.makeSeccion34(document,FECHA,NOM_PROYECT,REPO_PROYECT,CAT_PROYECT,TP_REPO_PROYECT)
pdf.makeSeccion5(
        document,
        trivyData,trivyVulSevCritical,trivyVulSevHigh,trivyVulSevMedium,trivyVulSevLow,trivyVulSevUnk,
        vulBlocker,vulCritical,vulMajor,vulMinor,vulInfo,
        rapidScanVulCritical,rapidScanVulHigh,rapidScanVulMedium,rapidScanVulLow,rapidScanVulInfo
)
document.close()

def getData(projectKey,username,password,severity,server){
    URL weburl=new URL(server+"/api/issues/search?componentKeys="+projectKey+"&severities="+severity);
    HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("Authorization","Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
    int codeResponse = conn.getResponseCode()
    def object = ""
    if(codeResponse == 200){
        def response    = conn.getInputStream().getText()
        def jsonSlurper = new JsonSlurper()
        object          = jsonSlurper.parseText(response)
    }
    return object
}

def cleanTrivyReport(String FilePath){
    def applicationProperties = new File(FilePath)
    def fileText = applicationProperties.getText('UTF-8')
    def newLine = '';
    fileText.eachLine {
        if(it[0] == "[" || it[0] == "]" || it[0] == " "){
            newLine +=it.trim()
        }
    }
    def object = ""
    def jsonSlurper = new JsonSlurper()
    object          = jsonSlurper.parseText(newLine)
    return object
}

def getTrivySev(Object data,String sev){
    vulneravility = []
    for (vul in data[0].Vulnerabilities) {
        if(vul.Severity.equals(sev)){
            vulneravility.add(vul)
        }
    }
    return vulneravility
}

def cleanRapidscanReport(String FilePath){
    def applicationProperties = new File(FilePath)
    def fileText = applicationProperties.getText('UTF-8')
    def newLine = "";
    def vulnerabilities = [];
    fileText.eachLine {
        if(it.contains("[45m critical")){
            arrayLine = it.split('93m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="CRITICAL:"+arrayLine[1].replace("[0m","")
        }
        if(it.contains("[41m high")){
            arrayLine = it.split('93m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="HIGH:"+arrayLine[1].replace("[0m","")
        }
        if(it.contains("[43m medium")){
            arrayLine = it.split('93m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="MEDIUM:"+arrayLine[1].replace("[0m","")
        }
        if(it.contains("[44m low")){
            arrayLine = it.split('93m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="LOW:"+arrayLine[1].replace("[0m","")
        }
        if(it.contains("[92m info")){
            arrayLine = it.split('93m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="INFO:"+arrayLine[1].replace("[0m","")
        }
        if(it.startsWith("\t\u001B[91m")){
            arrayLine = it.split('91m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="___VULNERABILITY:"+arrayLine[1].replace("[0m","")
        }
        if(it.startsWith("\t\u001B[92m")){
            arrayLine = it.split('92m')
            //document.add(new Paragraph(arrayLine[1].replace("[0m","")))
            newLine+="___RECOMENDATION:"+arrayLine[1].replace("[0m","")
            newLine+="______"
        }
    }
    vulnerabilities = newLine.split('______')
}

def getRapidsSev(data,String sev){
    vulneravility = []
    for (vul in data) {
        if(vul.startsWith(sev)){
            vulneravility.add(vul)
        }
    }
    return vulneravility
}
