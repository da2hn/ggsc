package ggsc.support.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ams.cmm.PageVO;

public class PdfVO extends PageVO {
	private String FileName;
	private String templateName;
	private String templateSQLName;

	public String getTemplateSQLName() {
		String tmp = this.templateSQLName;
		return tmp;
	}

	private void setTemplateSQLName(String templateSQLName) {
		this.templateSQLName = templateSQLName;
	}

	private int number;
	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	private String caseNo;

	public String getFileName() {
		String tmp = this.FileName;
		return tmp;
	}

	private void setFileName(String fileName) {
		FileName = fileName;
	}

	public int getNumber() {
		return number;
	}

	public String getTemplateName() {
		String tmp = this.templateName;
		return tmp;
	}

	private void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public void setNumber(int number) {
		this.number = number;
		switch (number) {
			case 30: setInit("상담동의서", "cnsAgreement", "PDFcnsAgreement"); break;
			case 31: setInit("개인정보동의서", "privacyAgreement", "PDFprivacyAgreement"); break;
			case 32: setInit("노인상담성별척도지","scScreeningScale", "PDFscScreeningScale"); break;
			case 33: setInit("초기상담신청서", "ealyCnsDocDtl", "PDFealyCnsDocDtl"); break;
			case 34: setInit("개인상담일지", "perCnsDtl", "PDFperCnsDtl"); break;
			case 35: setInit("집단상담일지", "gCnsDtl", "PDFgCnsDtl"); break;
			case 36: setInit("상담사례 종결서", "cnsEndDtl", "PDFcnsEndDtl"); break;
			case 37: setInit("내부 연계의뢰서", "linkageReqDtl", "PDFlinkageReqDtl"); break;
			case 38: setInit("외부 연계의뢰서", "linkageReqDtl2", "PDFlinkageReqDtl2"); break;
			case 39: setInit("네트워크구축회의 보고서", "ntwkMeetRptDtl", "PDFntwkMeetRptDtl"); break;
			case 40: setInit("교육활동보고서", "eduAtvyRptDtl", "PDFeduAtvyRptDtl"); break;
			case 41: setInit("홍보활동보고서", "prAtvyRptDtl", "PDFprAtvyRptDtl"); break;
			case 42: setInit("상담사전사후기록지", "cnsPrevPostDtl", "PDFcnsPrevPostDtl"); break;
			
			case 1: setInit("상담사전사후기록지", "psyCnsDoc1", "PDFpsyCnsDoc1"); break;
			case 2: setInit("DUKE-AD", "psyCnsDoc2", "PDFpsyCnsDoc2"); break;
			case 3: setInit("죽음학대 질문지", "psyCnsDoc3", "PDFpsyCnsDoc3"); break;
			case 4: setInit("노인우울척도", "psyCnsDoc4", "PDFpsyCnsDoc4"); break;
			case 5: setInit("노인우울및자살사고 척도", "psyCnsDoc5", "PDFpsyCnsDoc5"); break;
			case 6: setInit("한국형 알코올 중독 선별 검사(AUDIT-K)", "psyCnsDoc6", "PDFpsyCnsDoc6"); break;
			case 7: setInit("심리적 안녕감척도", "psyCnsDoc7", "PDFpsyCnsDoc7"); break;
			case 8: setInit("대인관계변화척도", "psyCnsDoc8", "PDFpsyCnsDoc8"); break;
			case 9: setInit("한국형알코올 중독 선별 검사", "psyCnsDoc9", "PDFpsyCnsDoc9"); break;
			case 10: setInit("불안척도", "psyCnsDoc10", "PDFpsyCnsDoc10"); break;
			case 11: setInit("스트레스 척도", "psyCnsDoc11", "PDFpsyCnsDoc11"); break;
			case 12: setInit("사별 스트레스 척도", "psyCnsDoc12", "PDFpsyCnsDoc12"); break;
			case 13: setInit("스트레스 자각 척도(PSS)", "psyCnsDoc13", "PDFpsyCnsDoc13"); break;
			case 14: setInit("부부의사소통 척도", "psyCnsDoc14", "PDFpsyCnsDoc14"); break;
			case 15: setInit("한국판 자기자비 척도", "psyCnsDoc15", "PDFpsyCnsDoc15"); break;
			case 16: setInit("치매선별용 한국어판 간이정신상태검사", "psyCnsDoc16", "PDFpsyCnsDoc16"); break;
			case 17: setInit("중장년상담선별척도지", "psyCnsDoc17", "PDFpsyCnsDoc17"); break;
			case 18: setInit("불안 척도(특성불안)", "psyCnsDoc18", "PDFpsyCnsDoc18"); break;
		}
	
	}
	
	private void setInit(String FileName, String tFileName, String SQLName) {
		try {
			this.setFileName(URLEncoder.encode(FileName, "UTF-8").replaceAll("\\+", "%20"));
			this.setTemplateName(tFileName);
			this.setTemplateSQLName(SQLName);
		} catch (UnsupportedEncodingException err) {

		}
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

}