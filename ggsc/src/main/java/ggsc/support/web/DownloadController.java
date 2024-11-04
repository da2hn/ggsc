package ggsc.support.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

import ams.cmm.AMSComm;
import egovframework.com.cmm.util.EgovDateUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ggsc.cnsmng.service.CounselMngService;
import ggsc.cnsr.service.AdminManageService;
import ggsc.report.service.ReportService;
import ggsc.rorgmng.service.RelatedOrganMngService;
import ggsc.stats.service.ResultStatsService;
import ggsc.support.service.ExcelVO;
import ggsc.support.service.PdfVO;
import ggsc.support.service.SupportService;


@Controller

public class DownloadController {
	@Resource(name = "supportService")
	private SupportService supportService;
	
	@Resource(name = "counselMngService")
	private CounselMngService counselMngService;

	@Resource(name = "ReportService")
	private ReportService reportService;

	@Resource(name = "AdminManageService")
	private AdminManageService adminManageService;

	@Resource(name = "RelatedOrganMngService")
	private RelatedOrganMngService relatedOrganMngService;
	
	@Resource(name = "ResultStatsService")
	private ResultStatsService resultStatsService;
	
	@RequestMapping(value = "/pdfDownload.do", method = { RequestMethod.POST, RequestMethod.GET })
	public void pdfDown(HttpServletRequest request, PdfVO vo, HttpServletResponse response) {
	
		vo.setNumber( Integer.parseInt(request.getParameter("Number").toString()));
		vo.setNum( Integer.parseInt(request.getParameter("num").toString()));
		String inputFile = utility.func.pdfTemplatePath + vo.getTemplateName() + ".html";
		String fontPath = utility.func.pdfTemplatePath + "NanumGothic-Bold.ttf";
		String perPstCnsDoc = "";
		List<EgovMap> perPsyCnsDocList;
		
		EgovMap result = supportService.PDFDownload(vo, vo.getTemplateSQLName()); 	
		
		if(vo.getNumber() == 34) {
			perPsyCnsDocList = supportService.getPDFperPsyCnsDocList(vo);
			if(!perPsyCnsDocList.isEmpty()) {
				
				for (int i = 0; i < perPsyCnsDocList.size(); i++) {
					String perPstCnsDocRow = "";
					perPstCnsDocRow = "심리검사명 : " + perPsyCnsDocList.get(i).get("examDocNm") + " 점수 : " +  perPsyCnsDocList.get(i).get("no1") + "\r\n";
					perPstCnsDoc += perPstCnsDocRow;
				}
				
				perPstCnsDoc = perPstCnsDoc.replace("\n", "<br />");
				
			}
		}
		
		System.out.println(perPstCnsDoc);
		
		if(result==null) {
			return;
		}
		
		try {
			InputStream is = new FileInputStream(inputFile);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader buffer = new BufferedReader(isr);
			String tmp, content = "";
			while ((tmp = buffer.readLine()) != null) {
				content += tmp;
			}
			buffer.close();
			isr.close();
			
			if(vo.getTemplateSQLName().equals("PDFperCnsDtl")) {
				content = content.replaceAll("##센터명##", replaceText(result.get("centerNm")))
						.replaceAll("##서명1##", replaceText(result.get("sign1")))
						.replaceAll("##상담상태##", replaceText(result.get("cnsStat")))
						.replaceAll("##상담구분##", replaceText(result.get("cnsGb")))
						.replaceAll("##상담회기##", replaceText(result.get("cnsCnt")))
						.replaceAll("##상담사구분##", replaceText(result.get("cnsrGb")))
						.replaceAll("##상담자명##", replaceText(result.get("cnsrNm")))
						.replaceAll("##내담자명##", replaceText(result.get("cnsleNm")))
						.replaceAll("##생년월일##", replaceText(result.get("birthDt")))
						.replaceAll("##성별##", replaceText(result.get("gender")))
						.replaceAll("##주소##", replaceText(result.get("addr")))
						.replaceAll("##내담자연락처##", replaceText(result.get("mobile")))
						.replaceAll("##긴급연락처##", replaceText(result.get("emgcyTel")))
						.replaceAll("##상담실시장소##", replaceText(result.get("cnsEfftPlace")))
						.replaceAll("##상담예약장소##", replaceText(result.get("cnsRsvtPlace")))
						.replaceAll("##다음상담예약일##", replaceText(result.get("cnsRsvtDt")))
						.replaceAll("##위기긴급성##", replaceText(result.get("emgcyUrgt")))
						.replaceAll("##자살사례##", replaceText(result.get("killsOptn")))
						.replaceAll("##항우울제지원여부##", replaceText(result.get("ahydSuptYn")))
						.replaceAll("##심리검사##", replaceText(perPstCnsDoc))
						.replaceAll("##게시일##", replaceText(result.get("ahydAcptDt")))
						.replaceAll("##종결일##", replaceText(result.get("ahydEndDt")))
						.replaceAll("##학대사례##", replaceText(result.get("abusOptn")))
						.replaceAll("##상담방법##", replaceText(result.get("cnsMethd")))
						.replaceAll("##내담자의관계##", replaceText(result.get("cnsleRel")))
						.replaceAll("##주호소문제##", replaceText(result.get("majorApplCd")))
						.replaceAll("##자살위험성##", replaceText(result.get("killsRskn")))
						.replaceAll("##심리문제##", replaceText(result.get("psycQust")))
						.replaceAll("##대인관계문제##", replaceText(result.get("perRelQust")))
						.replaceAll("##중독##", replaceText(result.get("psng")))
						.replaceAll("##치매##", replaceText(result.get("dmta")))
						.replaceAll("##정신장애##", replaceText(result.get("dsm")))
						.replaceAll("##건강문제##", replaceText(result.get("helthQust")))
						.replaceAll("##경제문제##", replaceText(result.get("econoQust")))
						.replaceAll("##상담내용##", replaceText(result.get("cnsCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##상담개입및연계##", replaceText(result.get("cnsItvtLink"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##상담회기 평가 및 추후계획##", replaceText(result.get("cnsCntEvatCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##사후관리##", replaceText(result.get("daftMng"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##정서적편안함정도##", replaceText(result.get("emotCptvDegr")))
						.replaceAll("##도움받은정도##", replaceText(result.get("helpDegr")))
						.replaceAll("##상담으로인한변화정도##", replaceText(result.get("cnsChngDegr")))
						.replaceAll("##상담일시##", replaceText(result.get("cnsDt")))
						.replaceAll("##요일##", replaceText(result.get("cnsDtWeekCd")))
						.replaceAll("##시##", replaceText(result.get("cnsDtStdHour")))
						.replaceAll("##분##", replaceText(result.get("cnsDtStdMin")))
						.replaceAll("##시2##", replaceText(result.get("cnsDtEndHour")))
						.replaceAll("##분2##", replaceText(result.get("cnsDtEndMin")))
						.replaceAll("##총##", replaceText(result.get("cnsTimeTotMin")))
						.replaceAll("##상담일시2##", replaceText(result.get("cnsRsvtDt")))
						.replaceAll("##요일2##", replaceText(result.get("cnsRsvtWeekCd")))
						.replaceAll("##시3##", replaceText(result.get("cnsRsvtStrtHour")))
						.replaceAll("##분3##", replaceText(result.get("cnsRsvtStrtMin")))
						.replaceAll("##시4##", replaceText(result.get("cnsRsvtEndHour")))
						.replaceAll("##분4##", replaceText(result.get("cnsRsvtEndMin")))
						.replaceAll("##총2##", replaceText(result.get("cnsRsvtTotMin")))
						;
			} else if(vo.getTemplateSQLName().equals("PDFgCnsDtl")) {
				content = content.replaceAll("##센터명##", replaceText(result.get("centerNm")))
						.replaceAll("##상담구분##", replaceText(result.get("cnsGb")))
						.replaceAll("##시군구분##", replaceText(result.get("sigunGb")))
						.replaceAll("##센터구분##", replaceText(result.get("centerGb")))
						.replaceAll("##주호소문제##", replaceText(result.get("majorApplCd")))
						.replaceAll("##프로그램명##", replaceText(result.get("pgrmNm")))
						.replaceAll("##회기##", replaceText(result.get("cnsCnt")))
						.replaceAll("##활동일시##", replaceText(result.get("atvyDt")))
						.replaceAll("##활동장소##", replaceText(result.get("atvyPlace")))
						.replaceAll("##내담자와의관계##", replaceText(result.get("olderRel")))
						.replaceAll("##리더##", replaceText(result.get("leader")))
						.replaceAll("##리더구분##", replaceText(result.get("leaderGb")))
						.replaceAll("##진행자##", replaceText(result.get("anchor")))
						.replaceAll("##작성자##", replaceText(result.get("inputNm")))
						.replaceAll("##참석자##", replaceText(result.get("atdeCnt")))
						.replaceAll("##참석자명##", replaceText(result.get("atdeNm")))
						.replaceAll("##활동주제##", replaceText(result.get("atvyTitle")))
						.replaceAll("##상담종류##", replaceText(result.get("cnsType")))
						.replaceAll("##활동목표##", replaceText(result.get("atvyGoal"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##프로그램내용##", replaceText(result.get("pgrmCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##평가##", replaceText(result.get("evat"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##향후계획##", replaceText(result.get("afterPlan"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##비고##", replaceText(result.get("etc"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##시##", replaceText(result.get("atvyStrtHour")))
						.replaceAll("##분##", replaceText(result.get("atvyStrtMin")))
						.replaceAll("##시2##", replaceText(result.get("atvyEndHour")))
						.replaceAll("##분2##", replaceText(result.get("atvyEndMin")))
						.replaceAll("##총##", replaceText(result.get("atvyTotMin")))
						;
			}else {
				content = content.replaceAll("##센터명##", replaceText(result.get("centerNm")))
						.replaceAll("##caseNo##", replaceText(result.get("caseNo")))
						.replaceAll("##홈페이지##", replaceText(result.get("homepage")))
						.replaceAll("##서명1##", replaceText(result.get("sign1")))
						.replaceAll("##서명2##", replaceText(result.get("sign2")))
						.replaceAll("##날짜##", replaceText(result.get("writeDt")))
						.replaceAll("##날짜2##", replaceText(result.get("endDt")))
						.replaceAll("##사례번호##", replaceText(result.get("rewdNo")))
						.replaceAll("##시간##", replaceText(result.get("stime")))
						.replaceAll("##생년월일##", replaceText(result.get("birthDt")))
						.replaceAll("##성별##", replaceText(result.get("gender")))
						.replaceAll("##종료시간##", replaceText(result.get("etime")))
						.replaceAll("##소요시간##", replaceText(result.get("ttime")))
						.replaceAll("##설문1Y##", replaceText(result.get("survey1y")))
						.replaceAll("##설문1N##", replaceText(result.get("survey1n")))
						.replaceAll("##설문1내용##", replaceText(result.get("survey1str")))
						.replaceAll("##설문2Y##", replaceText(result.get("survey2y")))
						.replaceAll("##설문2N##", replaceText(result.get("survey2n")))
						.replaceAll("##설문2내용##", replaceText(result.get("survey2str")))
						.replaceAll("##설문3Y##", replaceText(result.get("survey3y")))
						.replaceAll("##설문3N##", replaceText(result.get("survey3n")))
						.replaceAll("##설문3내용##", replaceText(result.get("survey3str")))
						.replaceAll("##설문4Y##", replaceText(result.get("survey4y")))
						.replaceAll("##설문4N##", replaceText(result.get("survey4n")))
						.replaceAll("##설문4내용##", replaceText(result.get("survey4str")))
						.replaceAll("##설문5Y##", replaceText(result.get("survey5y")))
						.replaceAll("##설문5N##", replaceText(result.get("survey5n")))
						.replaceAll("##설문6Y##", replaceText(result.get("survey6y")))
						.replaceAll("##설문6N##", replaceText(result.get("survey6n")))
						.replaceAll("##설문7Y##", replaceText(result.get("survey7y")))
						.replaceAll("##설문7N##", replaceText(result.get("survey7n")))
						.replaceAll("##설문8Y##", replaceText(result.get("survey8y")))
						.replaceAll("##설문8N##", replaceText(result.get("survey8n")))
						.replaceAll("##설문9Y##", replaceText(result.get("survey9y")))
						.replaceAll("##설문9N##", replaceText(result.get("survey9n")))
						.replaceAll("##설문10Y##", replaceText(result.get("survey10y")))
						.replaceAll("##설문10N##", replaceText(result.get("survey10n")))
						.replaceAll("##설문11Y##", replaceText(result.get("survey11y")))
						.replaceAll("##설문11N##", replaceText(result.get("survey11n")))
						.replaceAll("##설문12Y##", replaceText(result.get("survey12y")))
						.replaceAll("##설문12N##", replaceText(result.get("survey12n")))
						.replaceAll("##설문13Y##", replaceText(result.get("survey13y")))
						.replaceAll("##설문13N##", replaceText(result.get("survey13n")))
						.replaceAll("##설문14Y##", replaceText(result.get("survey14y")))
						.replaceAll("##설문14N##", replaceText(result.get("survey14n")))
						.replaceAll("##설문15Y##", replaceText(result.get("survey15y")))
						.replaceAll("##설문15N##", replaceText(result.get("survey15n")))
						.replaceAll("##설문16Y##", replaceText(result.get("survey16y")))
						.replaceAll("##설문16N##", replaceText(result.get("survey16n")))
						.replaceAll("##설문17Y##", replaceText(result.get("survey17y")))
						.replaceAll("##설문17N##", replaceText(result.get("survey17n")))
						.replaceAll("##설문18Y##", replaceText(result.get("survey18y")))
						.replaceAll("##설문18N##", replaceText(result.get("survey18n")))
						
						.replaceAll("##설문19Y##", replaceText(result.get("survey19y")))
						.replaceAll("##설문19N##", replaceText(result.get("survey19n")))
						.replaceAll("##설문20Y##", replaceText(result.get("survey20y")))
						.replaceAll("##설문20N##", replaceText(result.get("survey20n")))
						.replaceAll("##설문21Y##", replaceText(result.get("survey21y")))
						.replaceAll("##설문21N##", replaceText(result.get("survey21n")))
						.replaceAll("##설문22Y##", replaceText(result.get("survey22y")))
						.replaceAll("##설문22N##", replaceText(result.get("survey22n")))
						.replaceAll("##설문23Y##", replaceText(result.get("survey23y")))
						.replaceAll("##설문23N##", replaceText(result.get("survey23n")))
						.replaceAll("##설문24Y##", replaceText(result.get("survey24y")))
						.replaceAll("##설문24N##", replaceText(result.get("survey24n")))
						.replaceAll("##설문25Y##", replaceText(result.get("survey25y")))
						.replaceAll("##설문25N##", replaceText(result.get("survey25n")))
						.replaceAll("##설문26Y##", replaceText(result.get("survey26y")))
						.replaceAll("##설문26N##", replaceText(result.get("survey26n")))
						.replaceAll("##설문27Y##", replaceText(result.get("survey27y")))
						.replaceAll("##설문27N##", replaceText(result.get("survey27n")))
						.replaceAll("##설문28Y##", replaceText(result.get("survey28y")))
						.replaceAll("##설문28N##", replaceText(result.get("survey28n")))
						.replaceAll("##설문29Y##", replaceText(result.get("survey29y")))
						.replaceAll("##설문29N##", replaceText(result.get("survey29n")))
						.replaceAll("##설문30Y##", replaceText(result.get("survey30y")))
						.replaceAll("##설문30N##", replaceText(result.get("survey30n")))
						.replaceAll("##설문31Y##", replaceText(result.get("survey31y")))
						.replaceAll("##설문31N##", replaceText(result.get("survey31n")))
						.replaceAll("##설문32Y##", replaceText(result.get("survey32y")))
						.replaceAll("##설문32N##", replaceText(result.get("survey32n")))
						
						.replaceAll("##설문33Y##", replaceText(result.get("survey33y")))
						.replaceAll("##설문33N##", replaceText(result.get("survey33n")))
						.replaceAll("##설문34Y##", replaceText(result.get("survey34y")))
						.replaceAll("##설문34N##", replaceText(result.get("survey34n")))
						.replaceAll("##설문35Y##", replaceText(result.get("survey35y")))
						.replaceAll("##설문35N##", replaceText(result.get("survey35n")))
						.replaceAll("##설문36Y##", replaceText(result.get("survey36y")))
						.replaceAll("##설문36N##", replaceText(result.get("survey36n")))
						.replaceAll("##설문37Y##", replaceText(result.get("survey37y")))
						.replaceAll("##설문37N##", replaceText(result.get("survey37n")))
						.replaceAll("##설문38Y##", replaceText(result.get("survey38y")))
						.replaceAll("##설문38N##", replaceText(result.get("survey38n")))
						.replaceAll("##설문39Y##", replaceText(result.get("survey39y")))
						.replaceAll("##설문39N##", replaceText(result.get("survey39n")))
						.replaceAll("##설문40Y##", replaceText(result.get("survey40y")))
						.replaceAll("##설문40N##", replaceText(result.get("survey40n")))
						.replaceAll("##설문41Y##", replaceText(result.get("survey41y")))
						.replaceAll("##설문41N##", replaceText(result.get("survey41n")))
						.replaceAll("##설문42Y##", replaceText(result.get("survey42y")))
						.replaceAll("##설문42N##", replaceText(result.get("survey42n")))
						.replaceAll("##설문43Y##", replaceText(result.get("survey43y")))
						.replaceAll("##설문43N##", replaceText(result.get("survey43n")))
						.replaceAll("##설문44Y##", replaceText(result.get("survey44y")))
						.replaceAll("##설문44N##", replaceText(result.get("survey44n")))
						.replaceAll("##설문45Y##", replaceText(result.get("survey45y")))
						.replaceAll("##설문45N##", replaceText(result.get("survey45n")))
						.replaceAll("##설문46Y##", replaceText(result.get("survey46y")))
						.replaceAll("##설문46N##", replaceText(result.get("survey46n")))
						
						.replaceAll("##설문47Y##", replaceText(result.get("survey47y")))
						.replaceAll("##설문47N##", replaceText(result.get("survey47n")))
						.replaceAll("##설문48Y##", replaceText(result.get("survey48y")))
						.replaceAll("##설문48N##", replaceText(result.get("survey48n")))
						.replaceAll("##설문49Y##", replaceText(result.get("survey49y")))
						.replaceAll("##설문49N##", replaceText(result.get("survey49n")))
						.replaceAll("##설문50Y##", replaceText(result.get("survey50y")))
						.replaceAll("##설문50N##", replaceText(result.get("survey50n")))
						.replaceAll("##설문51Y##", replaceText(result.get("survey51y")))
						.replaceAll("##설문51N##", replaceText(result.get("survey51n")))
						.replaceAll("##설문52Y##", replaceText(result.get("survey52y")))
						.replaceAll("##설문52N##", replaceText(result.get("survey52n")))
						.replaceAll("##설문53Y##", replaceText(result.get("survey53y")))
						.replaceAll("##설문53N##", replaceText(result.get("survey53n")))
						.replaceAll("##설문54Y##", replaceText(result.get("survey54y")))
						.replaceAll("##설문54N##", replaceText(result.get("survey54n")))
						.replaceAll("##설문55Y##", replaceText(result.get("survey55y")))
						.replaceAll("##설문55N##", replaceText(result.get("survey55n")))
						.replaceAll("##설문56Y##", replaceText(result.get("survey56y")))
						.replaceAll("##설문56N##", replaceText(result.get("survey56n")))
						.replaceAll("##설문57Y##", replaceText(result.get("survey57y")))
						.replaceAll("##설문57N##", replaceText(result.get("survey57n")))
						.replaceAll("##설문58Y##", replaceText(result.get("survey58y")))
						.replaceAll("##설문58N##", replaceText(result.get("survey58n")))
						.replaceAll("##설문59Y##", replaceText(result.get("survey59y")))
						.replaceAll("##설문59N##", replaceText(result.get("survey59n")))
						.replaceAll("##설문60Y##", replaceText(result.get("survey60y")))
						.replaceAll("##설문60N##", replaceText(result.get("survey60n")))
						
						.replaceAll("##설문61Y##", replaceText(result.get("survey61y")))
						.replaceAll("##설문61N##", replaceText(result.get("survey61n")))
						.replaceAll("##설문62Y##", replaceText(result.get("survey62y")))
						.replaceAll("##설문62N##", replaceText(result.get("survey62n")))
						.replaceAll("##설문63Y##", replaceText(result.get("survey63y")))
						.replaceAll("##설문63N##", replaceText(result.get("survey63n")))
						.replaceAll("##설문64Y##", replaceText(result.get("survey64y")))
						.replaceAll("##설문64N##", replaceText(result.get("survey64n")))
						
						.replaceAll("##설문65Y##", replaceText(result.get("survey65y")))
						.replaceAll("##설문65N##", replaceText(result.get("survey65n")))
						.replaceAll("##설문66Y##", replaceText(result.get("survey66y")))
						.replaceAll("##설문66N##", replaceText(result.get("survey66n")))
						.replaceAll("##설문67Y##", replaceText(result.get("survey67y")))
						.replaceAll("##설문67N##", replaceText(result.get("survey67n")))
						.replaceAll("##설문68Y##", replaceText(result.get("survey68y")))
						.replaceAll("##설문68N##", replaceText(result.get("survey68n")))
						.replaceAll("##설문69Y##", replaceText(result.get("survey69y")))
						.replaceAll("##설문69N##", replaceText(result.get("survey69n")))
						.replaceAll("##설문70Y##", replaceText(result.get("survey70y")))
						.replaceAll("##설문70N##", replaceText(result.get("survey70n")))
						.replaceAll("##설문71Y##", replaceText(result.get("survey71y")))
						.replaceAll("##설문71N##", replaceText(result.get("survey71n")))
						.replaceAll("##설문72Y##", replaceText(result.get("survey72y")))
						.replaceAll("##설문72N##", replaceText(result.get("survey72n")))
						.replaceAll("##설문73Y##", replaceText(result.get("survey73y")))
						.replaceAll("##설문73N##", replaceText(result.get("survey73n")))
						.replaceAll("##설문74Y##", replaceText(result.get("survey74y")))
						.replaceAll("##설문74N##", replaceText(result.get("survey74n")))
						.replaceAll("##설문75Y##", replaceText(result.get("survey75y")))
						.replaceAll("##설문75N##", replaceText(result.get("survey75n")))
						.replaceAll("##설문76Y##", replaceText(result.get("survey76y")))
						.replaceAll("##설문76N##", replaceText(result.get("survey76n")))
						.replaceAll("##설문77Y##", replaceText(result.get("survey77y")))
						.replaceAll("##설문77N##", replaceText(result.get("survey77n")))
						.replaceAll("##설문78Y##", replaceText(result.get("survey78y")))
						.replaceAll("##설문78N##", replaceText(result.get("survey78n")))
						.replaceAll("##설문79Y##", replaceText(result.get("survey79y")))
						.replaceAll("##설문79N##", replaceText(result.get("survey79n")))
						.replaceAll("##설문80Y##", replaceText(result.get("survey80y")))
						.replaceAll("##설문80N##", replaceText(result.get("survey80n")))
						.replaceAll("##설문81Y##", replaceText(result.get("survey81y")))
						.replaceAll("##설문81N##", replaceText(result.get("survey81n")))
						.replaceAll("##설문82Y##", replaceText(result.get("survey82y")))
						.replaceAll("##설문82N##", replaceText(result.get("survey82n")))
						.replaceAll("##설문83Y##", replaceText(result.get("survey83y")))
						.replaceAll("##설문83N##", replaceText(result.get("survey83n")))
						.replaceAll("##설문84Y##", replaceText(result.get("survey84y")))
						.replaceAll("##설문84N##", replaceText(result.get("survey84n")))
						.replaceAll("##설문85Y##", replaceText(result.get("survey85y")))
						.replaceAll("##설문85N##", replaceText(result.get("survey85n")))
						.replaceAll("##설문86Y##", replaceText(result.get("survey86y")))
						.replaceAll("##설문86N##", replaceText(result.get("survey86n")))
						
						.replaceAll("##Y점수##", replaceText(result.get("toty")))
						.replaceAll("##N점수##", replaceText(result.get("totn")))
						.replaceAll("##상담사Y점수##", replaceText(result.get("ctoty")))
						.replaceAll("##총점수##", replaceText(result.get("cot")))
						.replaceAll("##상담자ID##", replaceText(result.get("cnsrId")))
						.replaceAll("##상담자##", replaceText(result.get("cnsrNm")))
						.replaceAll("##상담방법##", replaceText(result.get("cnsMethd")))
						.replaceAll("##상담경위##", replaceText(result.get("cnsDtl")))
						.replaceAll("##관계##", replaceText(result.get("olderRel")))
						.replaceAll("##연락처##", replaceText(result.get("mobile"), true))
						.replaceAll("##연락처2##", replaceText(result.get("tel"), true))
						.replaceAll("##주소##", replaceText(result.get("addr")))
						.replaceAll("##학력##", replaceText(result.get("edu")))
						.replaceAll("##문자해독##", replaceText(result.get("decodeLvl")))
						.replaceAll("##종교##", replaceText(result.get("relg")))
						.replaceAll("##주거형태##", replaceText(result.get("housingType")))
						.replaceAll("##주택형태##", replaceText(result.get("houseType")))
						.replaceAll("##결혼상태##", replaceText(result.get("merrStat")))
						.replaceAll("##동거형태##", replaceText(result.get("liveForm")))
						.replaceAll("##주호소문제##", replaceText(result.get("majorApplCd")))
						.replaceAll("##상담내용##", replaceText(result.get("cnsCntn")))
						.replaceAll("##심리검사##", replaceText(result.get("psycExamCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##서비스연계##", replaceText(result.get("intrLinkCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##년도##", replaceText(result.get("year")))
						.replaceAll("##월##", replaceText(result.get("month")))
						.replaceAll("##일##", replaceText(result.get("day")))
						.replaceAll("##본인확인자##", replaceText(result.get("myselfAgre")))
						.replaceAll("##보호자##", replaceText(result.get("guardian")))
						.replaceAll("##성명##", replaceText(result.get("name")))
						// ================================================================
						.replaceAll("##상담상태##", replaceText(result.get("cnsStat")))
						.replaceAll("##상담구분##", replaceText(result.get("cnsGb")))
						.replaceAll("##상담회기##", replaceText(result.get("cnsCnt")))
						.replaceAll("##상담자구분##", replaceText(result.get("cnsDtlGbCd")))
						/*.replaceAll("##상담자명##", replaceText(result.get("cnsrNm")))*/
						.replaceAll("##내담자명##", replaceText(result.get("cnsleNm")))
						.replaceAll("##생년월일##", replaceText(result.get("birthDt")))
						.replaceAll("##성별##", replaceText(result.get("gender")))
						.replaceAll("##주소##", replaceText(result.get("addr")))
						.replaceAll("##내담자연락처##", replaceText(result.get("mobile")))
						.replaceAll("##긴급연락처##", replaceText(result.get("tel")))
						.replaceAll("##상담실시장소##", replaceText(result.get("cnsEfftPlace")))
						.replaceAll("##상담예약장소##", replaceText(result.get("cnsRsvtPlace")))
						.replaceAll("##상담일시##", replaceText(result.get("cnsDt")))
						.replaceAll("##상담요일##", replaceText(result.get("cnsDtWeekCd")))
						.replaceAll("##상담시간1##", replaceText(result.get("stime")))
						.replaceAll("##상담시간2##", replaceText(result.get("etime")))
						.replaceAll("##상담총시간##", replaceText(result.get("ttime")))
						.replaceAll("##다음상담예약일##", replaceText(result.get("cnsRsvtDt")))
						.replaceAll("##다음상담예약요일##", replaceText(result.get("cnsRsvtWeekCd")))
						.replaceAll("##다음상담예약시간1##", replaceText(result.get("srtime")))
						.replaceAll("##다음상담예약시간2##", replaceText(result.get("ertime")))
						.replaceAll("##다음상담예약총시간##", replaceText(result.get("trtime")))
						.replaceAll("##위기긴급성##", replaceText(result.get("emgcyUrgt")))
						.replaceAll("##자살사례##", replaceText(result.get("killsOptn")))
						.replaceAll("##항우울제지원여부##", replaceText(result.get("ahydSuptYn")))
						.replaceAll("##지원게시일##", replaceText(result.get("ahydAcptDt")))
						.replaceAll("##지원종결일##", replaceText(result.get("ahydEndDt")))
						.replaceAll("##학대사례##", replaceText(result.get("abusOptn")))
						.replaceAll("##상담방법##", replaceText(result.get("cnsMethd")))
						.replaceAll("##내담자의관계##", replaceText(result.get("cnsleRel")))
						.replaceAll("##주호소문제##", replaceText(result.get("majorApplCd")))
						.replaceAll("##주거형태##", replaceText(result.get("livgForm")))
						.replaceAll("##주택형태##", replaceText(result.get("houseForm")))
						.replaceAll("##거주지변동##", replaceText(result.get("houseChng")))
						.replaceAll("##결혼상태##", replaceText(result.get("merrStat")))
						.replaceAll("##동거형태##", replaceText(result.get("liveForm")))
						.replaceAll("##건강상태##", replaceText(result.get("sujtHelcyStat")))
						.replaceAll("##진단병력##", replaceText(result.get("dignMhist")))
						.replaceAll("##복용약##", replaceText(result.get("mnds")))
						.replaceAll("##경제상태##", replaceText(result.get("econoStat")))
						.replaceAll("##최근가족사별경험##", replaceText(result.get("recntFmlyDeth")))
						.replaceAll("##최근주변인사별경험##", replaceText(result.get("recntMmanDeth")))
						.replaceAll("##노인일자리참여여부##", replaceText(result.get("olderWorkPtptYn")))
						.replaceAll("##자살위험성##", replaceText(result.get("killsRskn")))
						.replaceAll("##심리문제##", replaceText(result.get("psycQust")))
						.replaceAll("##대인관계문제##", replaceText(result.get("perRelQust")))
						.replaceAll("##중독##", replaceText(result.get("psng")))
						.replaceAll("##치매##", replaceText(result.get("dmta")))
						.replaceAll("##정신장애##", replaceText(result.get("dsm")))
						.replaceAll("##건강문제##", replaceText(result.get("helthQust")))
						.replaceAll("##경제문제##", replaceText(result.get("econoQust")))
						.replaceAll("##생활문제##", replaceText(result.get("lifeQust")))
						.replaceAll("##상담개입및연계##", replaceText(result.get("cnsItvtLink"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##상담회기평가##", replaceText(result.get("cnsCntEvatCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##사후관리##", replaceText(result.get("daftMng"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##상담내용##", replaceText(result.get("cnsCntn"))).replaceAll("&ldquo;", "&quot;").replaceAll("&rdquo;", "&quot;")
						.replaceAll("##정서적편안함##", replaceText(result.get("emotCptvDegr")))
						.replaceAll("##도움받은정도##", replaceText(result.get("helpDegr")))
						.replaceAll("##변화정도##", replaceText(result.get("cnsChngDegr")));
				
			}

			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver ifr = renderer.getFontResolver();
			ifr.addFont(fontPath, BaseFont.IDENTITY_H, true);
			renderer.setDocumentFromString(content);
			renderer.layout();

			OutputStream outputPDF;
			if (request.getParameter("test").toString().equals("1")) {
				String pdfFilePath = utility.func.filePath + "test.pdf";
				outputPDF = new FileOutputStream(pdfFilePath);
				renderer.createPDF(outputPDF);
			} else {
				System.out.println("FILE NAME : " + vo.getFileName());
				response.reset();
				response.setHeader("Content-Disposition", "attachment;filename=" + vo.getFileName() + ".pdf");
				response.setContentType("application/pdf");
				outputPDF = new BufferedOutputStream(response.getOutputStream());
				renderer.createPDF(outputPDF);
				outputPDF.flush();
			}
			outputPDF.close();

		} catch (FileNotFoundException e) {
			utility.func.Logging(this.getClass().getName(), "Fild Not Found");
		} catch (IOException e) {
			utility.func.Logging(this.getClass().getName(), "IOException");
		} catch (com.lowagie.text.DocumentException e) {
			utility.func.Logging(this.getClass().getName(), "Document Error");
		}

	}
	
	private String replaceText(Object object, boolean useFormat) {
		return object != null ? utility.func.Mobile_Replace(object.toString()) : "";
	}
	private String replaceText(Object object) {
		return object != null ? object.toString() : ""; 
	}
	
	
	@RequestMapping(value = "/excelDownLoad.do", method = RequestMethod.POST)
	public void excelDown(HttpServletRequest request, ExcelVO vo, HttpServletResponse response) throws IOException {
		// 권한 관리 시작
		EgovMap loginVo = (EgovMap) request.getSession().getAttribute("LoginVO");

		int userAuth, userCenterGb;
		try {
			userAuth = Integer.parseInt(loginVo.get("authCd").toString());
			userCenterGb = Integer.parseInt(loginVo.get("centerGb").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
			userCenterGb = 0;
			utility.func.Logging(this.getClass().getName(), "Auth Error : NumberFormat");
		} catch (NullPointerException err ) {
			userAuth = 10;
			userCenterGb = 0;
			utility.func.Logging(this.getClass().getName(), "Auth Error : Null");
		}

		if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}
		switch (userAuth) {
			case 1: vo.setAuthCd("1"); break; 
			case 2: vo.setAuthCd("2"); break; 
			case 3: vo.setAuthCd("3"); break; 
			default: vo.setAuthCd("4"); break; 
		}
		
		String regId = loginVo.get("userId").toString();
		vo.setRegId(regId);
		
		// 권한 관리 끝
		
		
		

				
				
		List<EgovMap> list = null;
		switch (vo.getNumber()) {
			case 1: list = reportService.getCnsBookListExcel(vo); break; // 상담관리대장
			case 2: list = reportService.getCnsEndBookListExcel(vo); break; // 상담종결대장
			case 3: list = reportService.getNtwkMeetRptListExcel(vo); break; // 네트워크구축화의보고서
			case 4: list = reportService.getEduAtvyRptListExcel(vo); break; // 교육활동보고서
			case 5: list = reportService.getPrAtvyRptListExcel(vo); break; // 홍보활동보고서 // 수정 필요
			case 6: list = reportService.getMindSharingListExcel(vo); break; // 마음나눔봉사단 // 수정 필요
			case 7: list = reportService.getGenderAwarenessListExcel(vo); break; // 성인식개선사업 // 수정 필요
			case 8: list = reportService.getCnsEndListExcel(vo); break; // 상담사례종결서
			case 9: list = reportService.getLinkOrgRptListExcel(vo); break; // 연계기관방문보고서 // 수정필요
			case 10: list = counselMngService.getCnsAcptListExcel(vo); break; // 신규 상담접수관리
			case 11: list = counselMngService.getExiCnsAcptListExcel(vo); break; // 기존 상담접수관리
			case 12: list = adminManageService.getCnsrListExcel(vo); break; // 상담사 관리
			case 13: list = adminManageService.getCenterListExcel(vo); break; // 센터 관리
			case 14: list = adminManageService.getGroupingListExcel(vo); break; // 분류 관리
			case 15: list = relatedOrganMngService.getRelatedOrganMngListExcel(vo); break; // 유관기관 관리
			case 16: list = resultStatsService.getResultReportListExcel(vo); break; // 실적보고서통계
			
			case 22: list = counselMngService.getPerCnsListExcel(vo); break; // 개인상담
			case 23: list = counselMngService.getGcnsListExcel(vo); break; // 집단상담
			case 26: list = resultStatsService.getPsyCnsStateListExcel(vo); break;
			case 28: list = counselMngService.getPretestListExcel(vo); break; // 사전검사
			case 29: list = counselMngService.getEalyCnsDocListExcel(vo); break; // 초기상담신청서
			case 30: list = counselMngService.getLinkageReqListExcel(vo); break; // 연계의뢰서
			case 31: list = counselMngService.getCnsInfoListExcel(vo); break; // 정보제공상담
			case 32: list = counselMngService.getCnsPrevPostListExcel(vo); break; // 정보제공상담
		}

		OutputStream out = null;
		try {
			vo.init();
			vo.setList(list);
			
			HSSFWorkbook workbook = null;
	         XSSFWorkbook workbook2 = null;
	         vo.init();
	         vo.setList(list);
	         if(vo.getNumber() == 27) {
	            workbook2 = makeExcelFile(vo.getList(), vo.getMapKey(), vo.getHeader(), "Sheet1");
	         }else {
	            workbook = AMSComm.listExcelDownload(vo.getList(), vo.getMapKey(), vo.getHeader(), "Sheet1");
	         }
	         
	         response.reset();
	         response.setHeader("Content-Disposition", "attachment;filename=" + vo.getFileName() + ".xls");
	         response.setContentType("application/vnd.ms-excel");
	         out = new BufferedOutputStream(response.getOutputStream());
	         
	         if(workbook == null) {
	            workbook2.write(out);
	         }else {
	            workbook.write(out);
	         }


			out.flush();
		} catch (IOException e) {
			utility.func.Logging(this.getClass().getName(), "Excel Download Error");
		} finally {
			if (out != null)
				out.close();
		}

	}
	
	@RequestMapping(value = "/executeExcel.do")
	public void executeExcel() {
		resultStatsService.insertTotalResultReportList();
	}
	
	@RequestMapping(value = "/excelTestArea.do", method = RequestMethod.POST)
	public void excelTestArea(HttpServletRequest request, ExcelVO vo, HttpServletResponse response, HSSFWorkbook workbook) throws IOException {
		
		// 권한 관리 시작
		EgovMap loginVo = (EgovMap) request.getSession().getAttribute("LoginVO");

		int userAuth, userCenterGb;
		try {
			userAuth = Integer.parseInt(loginVo.get("authCd").toString());
			userCenterGb = Integer.parseInt(loginVo.get("centerGb").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
			userCenterGb = 0;
			utility.func.Logging(this.getClass().getName(), "Auth Error : NumberFormat");
		} catch (NullPointerException err ) {
			userAuth = 10;
			userCenterGb = 0;
			utility.func.Logging(this.getClass().getName(), "Auth Error : Null");
		}

		if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}
		// 권한 관리 끝
		
		/** 엑셀fileName와 sheetName 으로 쓸 값 가져오기 */
		// String excelName = String.valueOf(model.get("excelName"));
		String excelName = "excel 실적보고서";
		/** 오늘 날짜 가져오기 */
		String todayStr = EgovDateUtil.getToday();
		/** 파일명 세팅 */
		String fileName = excelName + "_" + todayStr;
		/** 유저 환경 */
		String userAgent = request.getHeader("User-Agent");
		/** 파일이름 인코딩 */
		if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
			fileName = URLEncoder.encode(fileName, "utf-8");
		} else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
			fileName = URLEncoder.encode(fileName, "utf-8");
		} else if (userAgent.indexOf("Trident") > -1) { // MS IE 11
			fileName = URLEncoder.encode(fileName, "utf-8");
		} else if (userAgent.indexOf("Chrome") > -1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fileName.length(); i++) {
				char c = fileName.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			fileName = sb.toString();
		} else if (userAgent.indexOf("Opera") > -1) {
			fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
		} else if (userAgent.indexOf("Firefox") > -1) {
			fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
		} else {
			fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		}

		/** 출력 할 엑셀 리스트 데이터 가져오기 */
		// List<EgovMap> excelList = (List<EgovMap>) model.get("excelList");
		List<EgovMap> excelList = null;
		/*for(int i=1; i<5; i++) {
			vo.setCenterGb(String.valueOf(i));
			excelList = resultStatsService.getResultReportListExcel(vo);
			System.out.println("@@@@  "+excelList.get(0));
		}*/
		
		// excelList = resultStatsService.getResultReportListExcel(vo);
		/** 출력 데이터 존재여부 체크 */
		// if (excelList.size() > 0) {
			response.setHeader("Content-Disposition", "ATTachment; Filename=" + fileName + ".xls");
			response.setHeader("Content-Transfer-Encoding", "binary");
			/** sheet 변수선언 */
			HSSFSheet worksheet = null;
			/** row 변수서언 */
			HSSFRow row = null;
			/** cell 변수선언 */
			HSSFCell cell = null;
			/** 워크 시트 생성 */
			worksheet = workbook.createSheet();
			/** 타이틀 키셋 */
			//List<String> keyList = excelList.get(0).keyList();
			/** 컬럼 갯수 가져오기 */
			//int columnTitleNum = keyList.size();
			int columnTitleNum = 216;
			/** 첫행 선언 */
			int rowNum = 0;
			/** 엑셀리스트 제목 폰트 */
			Font headFont = workbook.createFont();
			headFont.setFontHeightInPoints((short) 10);
			headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headFont.setFontName("맑은 고딕");
			/** 컬럼명 폰트 */
			Font coulmnFont = workbook.createFont();
			coulmnFont.setFontHeightInPoints((short) 11);
			coulmnFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			coulmnFont.setFontName("돋움체");
			/** 데이터 폰트 */
			Font dataFont = workbook.createFont();
			dataFont.setFontHeightInPoints((short) 10);
			dataFont.setFontName("돋움체");
			
			/** 엑셀리스트 제목 스타일 */
			CellStyle headStyle = workbook.createCellStyle();
			headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headStyle.setFont(headFont);
			
			CellStyle[] headStyleArr = new CellStyle[10];
			
			for(int i=1; i<10; i++) {
				headStyleArr[i] = workbook.createCellStyle();
				headStyleArr[i].setAlignment(HSSFCellStyle.ALIGN_CENTER);
				headStyleArr[i].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				headStyleArr[i].setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				headStyleArr[i].setFillForegroundColor(HSSFColor.WHITE.index);
				headStyleArr[i].setBorderRight(HSSFCellStyle.BORDER_THIN);
				headStyleArr[i].setBorderLeft(HSSFCellStyle.BORDER_THIN);
				headStyleArr[i].setBorderTop(HSSFCellStyle.BORDER_THIN);
				headStyleArr[i].setBorderBottom(HSSFCellStyle.BORDER_THIN);
				headStyleArr[i].setFont(headFont);
			}
			headStyleArr[1].setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			headStyleArr[2].setFillForegroundColor(HSSFColor.AQUA.index);
			headStyleArr[3].setFillForegroundColor(HSSFColor.ROSE.index);
			headStyleArr[4].setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			headStyleArr[5].setFillForegroundColor(HSSFColor.LAVENDER.index);
			headStyleArr[6].setFillForegroundColor(HSSFColor.TURQUOISE.index);
			headStyleArr[7].setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
			headStyleArr[8].setFillForegroundColor(HSSFColor.TAN.index);
			headStyleArr[9].setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
			
			/** 컬럼명 스타일 */
			CellStyle coulmnNameStyle = workbook.createCellStyle();
			coulmnNameStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			coulmnNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			coulmnNameStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			coulmnNameStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			coulmnNameStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			coulmnNameStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			coulmnNameStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			coulmnNameStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			coulmnNameStyle.setFont(coulmnFont);
			/** 데이터 스타일 */
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			dataStyle.setWrapText(true);
			dataStyle.setFont(dataFont);
			
			// 셀 너비 설정
			worksheet.autoSizeColumn(0);
			worksheet.setColumnWidth(0, (worksheet.getColumnWidth(0))+(short)250);
			worksheet.autoSizeColumn(1);
			worksheet.setColumnWidth(1, (worksheet.getColumnWidth(1))+(short)4096);
			// 셀 너비 설정
			for (int i=2; i<=columnTitleNum; i++){
				worksheet.autoSizeColumn(i);
				worksheet.setColumnWidth(i, (worksheet.getColumnWidth(i))+(short)1024);
			}


		
			
			// 첫번째 row
			row = worksheet.createRow(0);
			row.setHeight((short) 700);
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 0));
			cell = row.createCell(0);
			cell.setCellStyle(headStyle);
			cell.setCellValue("구분");
			worksheet.addMergedRegion(new CellRangeAddress(0, 3, 1, 1));
			cell = row.createCell(1);
			cell.setCellStyle(headStyle);
			cell.setCellValue("상담센터");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 7));
			for(int i=2; i<8; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[2]);
				cell.setCellValue("1-1. 성별(정보제공상담, 개인상담, 집단상담 참석자)");
			}
			
			for (int i = 0; i < columnTitleNum; i++) {
				worksheet.autoSizeColumn(i);
				worksheet.setColumnWidth(i, (worksheet.getColumnWidth(i)) + 1000);
			}
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 15));
			cell = row.createCell(8);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("1-2. 내담자와의 관계(개인상담, 집단상담 참석자)");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 29));
			cell = row.createCell(16);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("1-3. 연령별 현황(정보제공상담, 개인상담, 집단상담 참석자)");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 30, 44));
			cell = row.createCell(30);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("2.신규내담자 상담경위");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 45, 54));
			cell = row.createCell(45);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("3. 상담방법");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 55, 72));
			cell = row.createCell(55);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("4-1. 개인상담 주호소문제");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 73, 80));
			cell = row.createCell(73);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("4-2. 집단상담 주제");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 81, 88));
			cell = row.createCell(81);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("5-1. 연계(내부)");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 89, 100));
			cell = row.createCell(89);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("5-2. 연계(외부)");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 101, 107));
			cell = row.createCell(101);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("6. 네트워크 활동");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 108, 119));
			cell = row.createCell(108);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue(" 7-1. 교육대상");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 120, 135));
			cell = row.createCell(120);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue(" 7-2. 교육내용");
			
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 136, 142));
			for(int i=136; i<143; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[9]);
				cell.setCellValue("8. 홍보");
			}
			
			
			
			// 두번째 row
			row = worksheet.createRow(1);
			row.setHeight((short) 700);
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 0));
			cell = row.createCell(0);
			cell.setCellStyle(headStyle);
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 3));
			cell = row.createCell(2);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("계");
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 7));
			for(int i=4; i<8; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[2]);
				cell.setCellValue("성별");				
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 8, 8));
			cell = row.createCell(8);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("계");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 9, 9));
			cell = row.createCell(9);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("본인");
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 15));
			for(int i=10; i<16; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[2]);
				cell.setCellValue("본인 외");			
			}
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 17));
			cell = row.createCell(16);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("계");
			cell = row.createCell(17);
			cell.setCellStyle(headStyleArr[2]);
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 29));
			for(int i=18; i<30; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[2]);
				cell.setCellValue("연령");			
			}
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 30, 30));
			cell = row.createCell(30);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("계(명)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 31, 31));
			cell = row.createCell(31);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("내부 의뢰\r\n" + 
					"(수행기관)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 32, 32));
			cell = row.createCell(32);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("발굴\r\n" + 
					"(자발적신청/외부발굴 등)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 33, 44));
			for(int i=33; i<45; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[3]);
				cell.setCellValue("외부의뢰");			
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 45, 45));
			cell = row.createCell(45);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("계(건)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 46, 51));
			for(int i=46; i<52; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[4]);
				cell.setCellValue("개인상담(건)");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 52, 54));
			for(int i=51; i<55; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[4]);
				cell.setCellValue("집단상담(건)");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 55, 55));
			cell = row.createCell(55);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("계(건)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 56, 56));
			cell = row.createCell(56);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("소계");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 57, 57));
			cell = row.createCell(57);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("심리정서");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 58, 58));
			cell = row.createCell(58);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("대인관계");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 59, 59));
			cell = row.createCell(59);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("치매");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 60, 60));
			cell = row.createCell(60);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("중독");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 61, 61));
			cell = row.createCell(61);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("정신건강장애");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 62, 62));
			cell = row.createCell(62);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("자살");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 63, 63));
			cell = row.createCell(63);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("건강");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 64, 64));
			cell = row.createCell(64);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("경제");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 65, 65));
			cell = row.createCell(65);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("생활");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 66, 66));
			cell = row.createCell(66);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("성");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 67, 67));
			cell = row.createCell(67);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("부부");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 68, 68));
			cell = row.createCell(68);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("가족");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 69, 69));
			cell = row.createCell(69);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("학대");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 70, 70));
			cell = row.createCell(70);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("노년생애준비");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 71, 71));
			cell = row.createCell(71);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("재난");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 72, 72));
			cell = row.createCell(72);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("기타");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 73, 73));
			cell = row.createCell(73);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("소계");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 74, 74));
			cell = row.createCell(74);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("사별애도");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 75, 75));
			cell = row.createCell(75);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("죽음준비");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 76, 76));
			cell = row.createCell(76);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("관계개선의사소통");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 77, 77));
			cell = row.createCell(77);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("심리정서");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 78, 78));
			cell = row.createCell(78);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("자아통합");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 79, 79));
			cell = row.createCell(79);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("치매예방");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 80, 80));
			cell = row.createCell(80);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("기타");
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 81, 81));
			cell = row.createCell(81);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("합계(건)\r\n" + 
					"(내부+외부)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 82, 88));
			for(int i=82; i<89; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[6]);
				cell.setCellValue("내부연계(수행기관)");
			}
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 1, 89, 100));
			for(int i=89; i<101; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[6]);
				cell.setCellValue("외부연계");
			}
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 101, 101));
			cell = row.createCell(101);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("계(건)");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 102, 102));
			cell = row.createCell(102);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("권역 네트워크회의");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 103, 103));
			cell = row.createCell(103);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("수행기관\r\n" + 
					"네트워크회의");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 104, 104));
			cell = row.createCell(104);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("실무회의");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 105, 105));
			cell = row.createCell(105);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("기관내부 사례회의");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 106, 106));
			cell = row.createCell(106);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("지역사회 네트워크 회의");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 107, 107));
			cell = row.createCell(107);
			cell.setCellStyle(headStyleArr[7]);
			cell.setCellValue("기타");
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 108, 109));
			for(int i=108; i<110; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[8]);
				cell.setCellValue("계");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 110, 111));
			for(int i=110; i<112; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("노인");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 112, 113));
			for(int i=112; i<114; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("일반주민");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 114, 115));
			for(int i=114; i<116; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("소속기관");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 116, 117));
			for(int i=116; i<118; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("유관기관");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 118, 119));
			for(int i=118; i<120; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("기타");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 120, 121));
			for(int i=120; i<122; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("계");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 122, 123));
			for(int i=122; i<124; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("심리정서");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 124, 125));
			for(int i=124; i<126; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("관계개선");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 126, 127));
			for(int i=126; i<128; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("사별애도");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 128, 129));
			for(int i=128; i<130; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("죽음준비");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 130, 131));
			for(int i=130; i<132; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("치매예방");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 132, 133));
			for(int i=132; i<134; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("인권(학대)");
			}
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 134, 135));
			for(int i=134; i<136; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyleArr[8]);
			cell.setCellValue("기타");
			}
			
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 136, 136));
			cell = row.createCell(136);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("계");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 137, 137));
			cell = row.createCell(137);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("대중매체 홍보");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 138, 138));
			cell = row.createCell(138);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("온라인 홍보");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 139, 139));
			cell = row.createCell(139);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("설치물 활용");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 140, 140));
			cell = row.createCell(140);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("직접 활동");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 141, 141));
			cell = row.createCell(141);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("홍보물품 및 자료 제작");
			worksheet.addMergedRegion(new CellRangeAddress(1, 2, 142, 142));
			cell = row.createCell(142);
			cell.setCellStyle(headStyleArr[9]);
			cell.setCellValue("기타");
			
			// 세번째 row
			row = worksheet.createRow(2);
			row.setHeight((short) 700);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 0));
			cell = row.createCell(0);
			cell.setCellStyle(headStyleArr[2]);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 1));
			cell = row.createCell(1);
			cell.setCellStyle(headStyleArr[2]);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));
			cell = row.createCell(4);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("남");
			cell = row.createCell(5);
			cell.setCellStyle(headStyleArr[2]);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));
			cell = row.createCell(6);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("여");
			cell = row.createCell(7);
			cell.setCellStyle(headStyleArr[2]);
			cell = row.createCell(8);
			cell.setCellStyle(headStyleArr[2]);
			cell = row.createCell(9);
			cell.setCellStyle(headStyleArr[2]);
			cell = row.createCell(10);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("소계");
			cell = row.createCell(11);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("배우자");
			cell = row.createCell(12);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("자녀");
			cell = row.createCell(13);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("친인척");
			cell = row.createCell(14);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("이웃");
			cell = row.createCell(15);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("유관기관");
			
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 18, 19));
			cell = row.createCell(18);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("50대 미만");
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 20, 21));
			cell = row.createCell(20);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("50대");
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 22, 23));
			cell = row.createCell(22);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("60대");
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 24, 25));
			cell = row.createCell(24);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("70대");
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 26, 27));
			cell = row.createCell(26);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("80대");
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 28, 29));
			cell = row.createCell(28);
			cell.setCellStyle(headStyleArr[2]);
			cell.setCellValue("90대 이상");
			
			cell = row.createCell(30);
			cell.setCellStyle(headStyleArr[3]);
			cell = row.createCell(31);
			cell.setCellStyle(headStyleArr[3]);
			cell = row.createCell(32);
			cell.setCellStyle(headStyleArr[3]);
			cell = row.createCell(33);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("소계");
			cell = row.createCell(34);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("정신건강자살예방센터");
			cell = row.createCell(35);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("노인보호전문기관");
			cell = row.createCell(36);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("보건소방문간호");
			cell = row.createCell(37);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("일자리관련기관");
			cell = row.createCell(38);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("법률기관");
			cell = row.createCell(39);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("성폭력가정폭력상담소");
			cell = row.createCell(40);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("치매관련기관");
			cell = row.createCell(41);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("건강가정다문화가정지원센터");
			cell = row.createCell(42);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("재가노인관련기관");
			cell = row.createCell(43);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("행정기관(무한돌봄)");
			cell = row.createCell(44);
			cell.setCellStyle(headStyleArr[3]);
			cell.setCellValue("기타");
			
			cell = row.createCell(46);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("소계");
			cell = row.createCell(47);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("내방");
			cell = row.createCell(48);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("방문");
			cell = row.createCell(49);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("전화");
			cell = row.createCell(50);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("온라인");
			cell = row.createCell(51);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("기타");
			cell = row.createCell(52);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("소계");
			cell = row.createCell(53);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("대면");
			cell = row.createCell(54);
			cell.setCellStyle(headStyleArr[4]);
			cell.setCellValue("온라인");
			
			for (int i = 55; i < 81; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[4]);
			}

/*			cell = row.createCell(70);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("소계");
			cell = row.createCell(71);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("심리·정서");
			cell = row.createCell(72);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("대인관계");
			cell = row.createCell(73);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("치매");
			cell = row.createCell(74);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("중독");
			cell = row.createCell(75);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("정신건강장애");
			cell = row.createCell(76);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("건강");
			cell = row.createCell(77);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("경제");
			cell = row.createCell(78);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("생활");
			cell = row.createCell(79);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("성");
			cell = row.createCell(80);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("부부");
			cell = row.createCell(81);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("가족");
			cell = row.createCell(82);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("학대");
			cell = row.createCell(83);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("노년생애준비");
			cell = row.createCell(84);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("자살");
			cell = row.createCell(85);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("재난");
			cell = row.createCell(86);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("기타");*/
			
/*			cell = row.createCell(87);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("소계");
			cell = row.createCell(88);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("사별애도");
			cell = row.createCell(89);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("죽음준비");
			cell = row.createCell(90);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("관계개선의사소통");
			cell = row.createCell(91);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("심리정서");
			cell = row.createCell(92);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("자아통합");
			cell = row.createCell(93);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("치매예방");
			cell = row.createCell(94);
			cell.setCellStyle(headStyleArr[5]);
			cell.setCellValue("기타");*/
			
			cell = row.createCell(82);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("내부연계 소계");
			cell = row.createCell(83);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("여가");
			cell = row.createCell(84);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("법률, 세무");
			cell = row.createCell(85);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("경제후원");
			cell = row.createCell(86);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("취업, 일자리");
			cell = row.createCell(87);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("생활, 식생활, 주거");
			cell = row.createCell(88);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("기타");
			
			cell = row.createCell(89);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("외부연계 소계");
			cell = row.createCell(90);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("정신건강 자살예방센터");
			cell = row.createCell(91);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("노인 보호전문 기관");
			cell = row.createCell(92);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("보건소 방문간호");
			cell = row.createCell(93);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("일자리 관련기관");
			cell = row.createCell(94);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("법률기관");
			cell = row.createCell(95);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("성폭력, 가정폭력 상담소");
			cell = row.createCell(96);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("치매 관련기관");
			cell = row.createCell(97);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("건강가정 다문화 가정 지원센터");
			cell = row.createCell(98);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("재가노인관련기관");
			cell = row.createCell(99);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("행정기관 \n(무한돌봄)");
			cell = row.createCell(100);
			cell.setCellStyle(headStyleArr[6]);
			cell.setCellValue("기타");
			
			for (int i = 101; i < 108; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[7]);
			}
			
			for (int i = 108; i < 136; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[8]);
			}
			
			for (int i = 136; i < 143; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[9]);
			}
			
			// 네번째 row
			row = worksheet.createRow(3);
			row.setHeight((short) 400);
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 0));
			cell = row.createCell(0);
			cell.setCellStyle(headStyle);
			worksheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 1));
			cell = row.createCell(1);
			cell.setCellStyle(headStyle);
			cell = row.createCell(2);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("명");
			cell = row.createCell(3);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(4);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("명");
			cell = row.createCell(5);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(6);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("명");
			cell = row.createCell(7);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(8);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(9);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(10);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			
			cell = row.createCell(11);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(12);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(13);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(14);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(15);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(16);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("명");
			cell = row.createCell(17);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("건");
			cell = row.createCell(18);
			cell.setCellStyle(headStyleArr[1]);
			cell.setCellValue("명");
			
			for(int i=19; i<30; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
				i++;
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("명");
			}
			
			for (int i=30; i<45; i++ ) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("명");
			}
			
			for (int i=45; i<55; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
			}
			
			for (int i=55; i<73; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
			}
			for (int i=73; i<81; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
			}
			for (int i=81; i<89; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
			}
			for (int i=89; i<101; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
			}
			for (int i=101; i<108; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("건");
			}
			
			for(int i=108; i<136; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("회");
				i++;
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("명");
			}
			
			for(int i=136; i<143; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyleArr[1]);
				cell.setCellValue("회");
			}
			
			
			// 다섯번째 row
			/*row = worksheet.createRow(4);
			row.setHeight((short) 400);*/
			
			List<EgovMap> totalExcelList = null;
			totalExcelList = resultStatsService.getTotalResultReportListExcel(vo);
			row = worksheet.createRow(4);
			row.setHeight((short) 400);
			cell = row.createCell(0);
			cell.setCellStyle(headStyle);
			cell = row.createCell(1);
			cell.setCellStyle(headStyle);
			cell.setCellValue("합계");
			
			// 합계 1 ~ 1-2row
			for(int i=1; i<15; i++) {
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				String param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i)));
				cell.setCellValue(param);
			}
			// 합계 1-3row
			for(int i=15; i<29; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				if(i >= 17 && i <= 18) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+12)));
				}else if(i >= 19 && i <= 20) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i-2)));
				}else {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i)));
				}
				
				cell.setCellValue(param);
				
			}
			// 합계 2 row
			for(int i=29; i<44; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				if(i == 31) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+15)));
					System.out.println("c"+String.valueOf(i+15));
				}else {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+2)));
					System.out.println("c"+String.valueOf(i+2));
				}
				cell.setCellValue(param);
			}
			
			// 합계 3 row
			for(int i=44; i<54; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				if(i >= 50) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+5)));
				}else {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+4)));
				}
				cell.setCellValue(param);
			}
			
			// 합계 4-1 row
			for(int i=54; i<72; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				if(i == 61) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+20)));
				}else if (i >= 62 && i <= 69){
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+11)));
				}else {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+12)));
				}
				cell.setCellValue(param);
			}
			
			// 합계 4-2 row
			for(int i=72; i<80; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+12)));
				cell.setCellValue(param);
			}
			
			// 합계 5-1 row
			for(int i=80; i<88; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+29)));
				cell.setCellValue(param);
			}
			
			// 합계 5-2 row
			for(int i=88; i<100; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				if(i == 97) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+25)));
				}else if(i >= 93 && i <=96) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+30)));
				}else {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+29)));
				}
				cell.setCellValue(param);
			}
			
			// 합계 6 row
			for(int i=100; i<107; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+62)));
				cell.setCellValue(param);
			}
			
			// 합계 7-1 row
			for(int i=107; i<119; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+62)));
				cell.setCellValue(param);
			}
			
			// 합계 7-2 row
			for(int i=119; i<135; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+68)));
				cell.setCellValue(param);
			}
			
			// 합계 8 row
			for(int i=135; i<142; i++) {
				String param = null;
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				if(i == 135) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+68)));
				}else if(i == 140) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+72)));
				}else if(i == 141) {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+74)));
				}else {
					param = String.valueOf(totalExcelList.get(0).get("c"+String.valueOf(i+70)));
				}
				cell.setCellValue(param);
			}
			
			// 상담세터 row(59개)
			/*for(int i=1; i<60; i++) {
				row = worksheet.createRow(i+4);
				row.setHeight((short) 400);
				vo.setCenterGb(String.valueOf(i));
				excelList = resultStatsService.getResultReportListExcel(vo);
				for(int j=0; j<=columnTitleNum; j++) {
					cell = row.createCell(j);
					cell.setCellStyle(headStyle);
					String param = String.valueOf(excelList.get(0).get("c"+String.valueOf(j)));
					cell.setCellValue(param);
				}
			} */
			
			/*for(int i=0; i<20; i++) {
				cell = row.createCell(i+1);
				cell.setCellStyle(headStyle);
				String idx = "c"+String.valueOf(i+1);
				String param = String.valueOf(excelList.get(0).get(idx));
				cell.setCellValue(param);
			}*/
			
			/*for(int i=0;i<3;i++) {
				worksheet.getRow(i);
				for(int z=0;z<5;z++) {
					cell = row.getCell(z);
					cell.setCellStyle(headStyle);
				}
					
			}*/
			// 끝
			
			workbook.write(response.getOutputStream());
			workbook.close();
			
			/** 제목라인 */
			/*row = worksheet.createRow(rowNum);
			row.setHeight((short) 500);
			for (int i = 0; i < columnTitleNum; i++) {
				cell = row.createCell(i);
				if (i == 0) {
					cell.setCellValue(excelName);
				}
				cell.setCellStyle(headStyle);
			}*/
			/** 셀 병합 */
			/*worksheet.addMergedRegion(new CellRangeAddress(rowNum, 0, 0, columnTitleNum - 1));
			rowNum += 1;*/
			/** 컬럼 라인 */
			/*row = worksheet.createRow(rowNum);
			row.setHeight((short) 400);
			for (int i = 0; i < columnTitleNum; i++) {
				cell = row.createCell(i);
				cell.setCellValue(keyList.get(i));
				cell.setCellStyle(coulmnNameStyle);
			}
			rowNum += 1;*/
			/** 데이터 라인 */
			/*for (int i = rowNum; i < (excelList.size() + rowNum); i++) {
				row = worksheet.createRow(i);
				for (int j = 0; j < columnTitleNum; j++) {
					cell = row.createCell(j);
					cell.setCellValue(String.valueOf(excelList.get(i - rowNum).get(keyList.get(j))));
					cell.setCellStyle(dataStyle);
				}
			}*/
			/** 컬럼 사이즈 */
			/*for (int i = 0; i < columnTitleNum; i++) {
				worksheet.autoSizeColumn(i);
				worksheet.setColumnWidth(i, (worksheet.getColumnWidth(i)) + 1000);
			}*/
			/** 출력 데이터 없을 시에 alert창 */
		/*} else {
			String rtnStr = "<script>alert('출력할 데이터가 없습니다.');</script>";
			response.setContentType("text/html; charset=UTF-8");
			OutputStream out = response.getOutputStream();
			out.write(rtnStr.getBytes());
			out.flush();
		}*/
	
	}
	
	private XSSFWorkbook makeExcelFile(List<EgovMap> list, String[] getMapKey, String[] getHeader, String SheetName) {
	      
	      XSSFWorkbook workbook = new XSSFWorkbook();
	      XSSFSheet sheet = workbook.createSheet(SheetName);
	      XSSFRow row;
	      XSSFCell cell;
	      int headerRow = 0;

	      row = sheet.createRow(0);
	      cell = row.createCell(0);
	      cell.setCellValue("성명");
	      cell = row.createCell(1);
	      cell.setCellValue("연락처");

	      row = sheet.createRow(1);
	      cell = row.createCell(0);
	      cell.setCellValue("홍길동");
	      cell = row.createCell(1);
	      cell.setCellValue("010-1234-1234");

	      
	      CellStyle cs = workbook.createCellStyle();
	      cs.setBorderTop(CellStyle.BORDER_THIN);
	      cs.setBorderBottom(CellStyle.BORDER_THIN);
	      cs.setBorderLeft(CellStyle.BORDER_THIN);
	      cs.setBorderRight(CellStyle.BORDER_THIN);

	      row = sheet.createRow(headerRow);
	      
	      int nowCellNumber = 0;
	      for (int i = 0; i < getHeader.length; i++) {
	         String header = getHeader[i];
	         if(header != null) {
	            if( header.indexOf("||") > 0) {
	               String[] tmp = header.split("\\|\\|");
	               if(tmp.length == 3) {
	                  String colName = tmp[0];
	                  int colHeight = Integer.parseInt(tmp[2]), colWidth = Integer.parseInt(tmp[1]);
	                  if(colHeight==0) colHeight = 1;
	                  if(colWidth==0) colWidth = 1;
	                  
	                  System.out.println("셀 이름 :" + colName + "/ 현재 로우 : " + headerRow + "/ 현재 셀 : " + nowCellNumber + "/ 마지막 셀 : " +  (nowCellNumber + (colWidth-1)));
	                  sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow + (colHeight-1), nowCellNumber, nowCellNumber + (colWidth-1)));
	                  nowCellNumber = nowCellNumber + (colWidth - 1);
	                  
	                  cell = row.createCell(nowCellNumber);
	                  cell.setCellValue(colName);
	                  cell.setCellStyle(cs);
	                  
	                  nowCellNumber++;
	               }
	               
	            }else if(header.equals("[ROW]")) {
	               row = sheet.createRow(++headerRow);
	               if(headerRow==1) {
	                  nowCellNumber = 2;
	               }else if(headerRow==2) {
	                  nowCellNumber = 4;
	               }else {
	                  nowCellNumber = headerRow >= 1 ? 2 : 0 ;
	               }
	               
	            }else {
	               cell = row.createCell(nowCellNumber++);
	               cell.setCellValue(header);
	               cell.setCellStyle(cs);
	            }
	         }

	         
	         
	      }
	      /*
	      for (int i = 0; i < list.size(); i++) {
	         row = sheet.createRow(i + 1);
	         EgovMap map = list.get(i);

	         for (int j = 0; j < mapKey.length; j++) {
	            cell = row.createCell(j);
	            cell.setCellValue((String) (map.get(mapKey[j]) == null ? "" : String.valueOf(map.get(mapKey[j]))));
	            cell.setCellStyle(cs);
	         }
	      }

	      for (int i = 0; i < list.size(); i++) {
	         sheet.autoSizeColumn(i);
	         sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1000);
	      }
	      
	      */

	      return workbook;
	   }
	
	

}
