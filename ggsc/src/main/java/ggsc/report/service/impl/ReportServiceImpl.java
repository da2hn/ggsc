package ggsc.report.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ams.cmm.AMSComm;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ggsc.report.service.CnsBookVO;
import ggsc.report.service.CnsEndVO;
import ggsc.report.service.EduAtvyRptVO;
import ggsc.report.service.LinkOrgRptVO;
import ggsc.report.service.NtwkMeetRptVO;
import ggsc.report.service.PrAtvyRptVO;
import ggsc.report.service.ReportService;
import ggsc.report.service.GenderAwarenessVO;
import ggsc.report.service.MindSharingVO;
import ggsc.support.service.ExcelVO;

@Service("ReportService")
public class ReportServiceImpl extends EgovAbstractServiceImpl implements ReportService {
	
	@Resource(name="ReportDAO")
    private ReportDAO reportDao;
	
	// 마음나눔봉사단  목록
	@Override
	public List<EgovMap> getMindSharingList(MindSharingVO vo) {
		return reportDao.getMindSharingList(vo);
	}
	
	// 마음나눔봉사단  갯수
	@Override
	public int getMindSharingCnt(MindSharingVO vo) {
		return reportDao.getMindSharingCnt(vo);
	}
	
	// 마음나눔봉사단 엑셀다운
	@Override
	public List<EgovMap> getMindSharingListExcel(ExcelVO vo) {
		return reportDao.getMindSharingListExcel(vo);
	}
	
	// 마음나눔봉사단 상세
	@Override
	public EgovMap getMindSharingDtl(MindSharingVO vo) {
		return reportDao.getMindSharingDtl(vo);
	}
	// 마음나눔봉사단 등록
	@Override
	public void insertMindSharing(MindSharingVO vo) {
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "report");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getUserNum());
			
			vo.setFileName(fMap.get("fileNm").toString());
			vo.setSysFileName(fMap.get("sysFileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			try {
				vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
			}catch(Exception err) {
				vo.setFileSize(0);
			}
		}
		
		
		reportDao.insertMindSharing(vo);
	}
	
	// 마음나눔봉사단  수정
	@Override
	public void updateMindSharing(MindSharingVO vo) {
		if(vo.getFile()!=null) {
			if(vo.getFile().getSize() != 0) {
				System.out.println("File Upload Start");
				MultipartFile file = vo.getFile();
				System.out.println("Upload File Name : " + vo.getFile().getName());
				
				EgovMap fMap = AMSComm.fileUpload(file, "report");
				
				System.out.println("File Upload End");
				
				fMap.put("regId", vo.getRegId());
				fMap.put("writer", vo.getUserNum());
				
				vo.setFileName(fMap.get("fileNm").toString());
				vo.setSysFileName(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				try {
					vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
				}catch(Exception err) {
					vo.setFileSize(0);
				}
			}
		}else {
			vo.setFileName(null);
			vo.setSysFileName(null);
			vo.setFilePath(null);
			vo.setFileSize(0);
		}
		
		
		reportDao.updateMindSharing(vo);
	}	
	
	// 마음나눔봉사단 삭제
	@Override
	public void deleteMindSharing(MindSharingVO vo) {
		reportDao.deleteMindSharing(vo);
	}	
	
	// 성인식개선사업  목록
	@Override
	public List<EgovMap> getGenderAwarenessList(GenderAwarenessVO vo) {
		return reportDao.getGenderAwarenessList(vo);
	}
	
	// 성인식개선사업  갯수
	@Override
	public int getGenderAwarenessCnt(GenderAwarenessVO vo) {
		return reportDao.getGenderAwarenessCnt(vo);
	}
	
	// 성인식개선사업 엑셀다운
	@Override
	public List<EgovMap> getGenderAwarenessListExcel(ExcelVO vo) {
		return reportDao.getGenderAwarenessListExcel(vo);
	}
	
	// 성인식개선사업 상세
	@Override
	public EgovMap getGenderAwarenessDtl(GenderAwarenessVO vo) {
		return reportDao.getGenderAwarenessDtl(vo);
	}
	// 성인식개선사업 등록
	@Override
	public void insertGenderAwareness(GenderAwarenessVO vo) {
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "report");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getUserNum());
			
			vo.setFileName(fMap.get("fileNm").toString());
			vo.setSysFileName(fMap.get("sysFileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			try {
				vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
			}catch(Exception err) {
				vo.setFileSize(0);
			}
		}
		
		reportDao.insertGenderAwareness(vo);
	}
	
	// 성인식개선사업  수정
	@Override
	public void updateGenderAwareness(GenderAwarenessVO vo) {
		if(vo.getFile()!=null) {
			if(vo.getFile().getSize() != 0) {
				System.out.println("File Upload Start");
				MultipartFile file = vo.getFile();
				System.out.println("Upload File Name : " + vo.getFile().getName());
				
				EgovMap fMap = AMSComm.fileUpload(file, "report");
				
				System.out.println("File Upload End");
				
				fMap.put("regId", vo.getRegId());
				fMap.put("writer", vo.getUserNum());
				
				vo.setFileName(fMap.get("fileNm").toString());
				vo.setSysFileName(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				try {
					vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
				}catch(Exception err) {
					vo.setFileSize(0);
				}
			}
		}else {
			vo.setFileName(null);
			vo.setSysFileName(null);
			vo.setFilePath(null);
			vo.setFileSize(0);
		}
		
		
		reportDao.updateGenderAwareness(vo);
	}	
	
	// 성인식개선사업 삭제
	@Override
	public void deleteGenderAwareness(GenderAwarenessVO vo) {
		reportDao.deleteGenderAwareness(vo);
	}	

		
	// 상담사례종결서 목록
	@Override
	public List<EgovMap> getCnsEndList(CnsEndVO vo) {
		return reportDao.getCnsEndList(vo);
	}
	
	// 상담사례종결서 갯수
	@Override
	public int getCnsEndListTotCnt(CnsEndVO vo) {
		return reportDao.getCnsEndListTotCnt(vo);
	}
	
	// 상담사례종결서 엑셀다운
	@Override
	public List<EgovMap> getCnsEndListExcel(ExcelVO vo) {
		return reportDao.getCnsEndListExcel(vo);
	}
	
	// 상담사례종결서 상세
	@Override
	public EgovMap getCnsEndDtl(CnsEndVO vo) {
		return reportDao.getCnsEndDtl(vo);
	}
	
	// 상담사례종결서 수정
	@Override
	public void updateCnsEnd(CnsEndVO vo) {
		reportDao.updateCnsEnd(vo);
	}
	
	// 상담대상자관리대장 목록
	@Override
	public List<EgovMap> getCnsBookList(CnsBookVO vo) {
		return reportDao.getCnsBookList(vo);
	}
	
	// 상담대상자관리대장 갯수
	@Override
	public int getCnsBookListTotCnt(CnsBookVO vo) {
		return reportDao.getCnsBookListTotCnt(vo);
	}
	
	// 상담대상자 관리대장 엑셀다운
	@Override
	public List<EgovMap> getCnsBookListExcel(ExcelVO vo) {
		return reportDao.getCnsBookListExcel(vo);
	}
	
	// 상담종결관리대장 목록
	@Override
	public List<EgovMap> getCnsEndBookList(CnsEndVO vo) {
		return reportDao.getCnsEndBookList(vo);
	}
	
	// 상담종결관리대장 갯수
	@Override
	public int getCnsEndBookListTotCnt(CnsEndVO vo) {
		return reportDao.getCnsEndBookListTotCnt(vo);
	}
	
	// 상담종결 관리대장 엑셀다운
	@Override
	public List<EgovMap> getCnsEndBookListExcel(ExcelVO vo) {
		return reportDao.getCnsEndBookListExcel(vo);
	}
	
	// 연계기관방문보고서 목록
	@Override
	public List<EgovMap> getLinkOrgRptList(LinkOrgRptVO vo) {
		return reportDao.getLinkOrgRptList(vo);
	}
	// 연계기관방문보고서 갯수
	@Override
	public int getLinkOrgRptListTotCnt(LinkOrgRptVO vo) {
		return reportDao.getLinkOrgRptListTotCnt(vo);
	}
	// 연계기관방문보고서 상세
	@Override
	public EgovMap getLinkOrgRptDtl(LinkOrgRptVO vo) {
		return reportDao.getLinkOrgRptDtl(vo);
	}
	// 연계기관방문보고서 엑셀
	@Override
	public List<EgovMap> getLinkOrgRptListExcel(ExcelVO vo) {
		return reportDao.getLinkOrgRptListExcel(vo);
	}
	// 연계기관방문보고서 등록
	@Override
	public void insertLinkOrgRpt(LinkOrgRptVO vo) {
		
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "report");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getInputNm());
			
			vo.setFileNm(fMap.get("fileNm").toString());
			vo.setSysFileNm(fMap.get("sysFileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			try {
				vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
			}catch(Exception err) {
				vo.setFileSize(0);
			}
		}
		
		reportDao.insertLinkOrgRpt(vo);
	}
	// 연계기관방문보고서 수정
	@Override
	public void updateLinkOrgRpt(LinkOrgRptVO vo) {
		
		if(vo.getFile()!=null) {
			if(vo.getFile().getSize() != 0) {
				System.out.println("File Upload Start");
				MultipartFile file = vo.getFile();
				System.out.println("Upload File Name : " + vo.getFile().getName());
				
				EgovMap fMap = AMSComm.fileUpload(file, "report");
				
				System.out.println("File Upload End");
				
				fMap.put("regId", vo.getRegId());
				fMap.put("writer", vo.getInputNm());
				
				vo.setFileNm(fMap.get("fileNm").toString());
				vo.setSysFileNm(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				try {
					vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
				}catch(Exception err) {
					vo.setFileSize(0);
				}
			}
		}else {
			vo.setFileNm(null);
			vo.setSysFileNm(null);
			vo.setFilePath(null);
			vo.setFileSize(0);
		}
		
		reportDao.updateLinkOrgRpt(vo);
	}
	
	// 연계기관방문보고서 삭제
	@Override
	public void deleteLinkOrgRpt(LinkOrgRptVO vo) {
		reportDao.deleteLinkOrgRpt(vo);
	}
		
	// 네트워크구축회의보고서 목록
	@Override
	public List<EgovMap> getNtwkMeetRptList(NtwkMeetRptVO vo) {
		return reportDao.getNtwkMeetRptList(vo);
	}
	// 네트워크구축회의보고서 갯수
	@Override
	public int getNtwkMeetRptListTotCnt(NtwkMeetRptVO vo) {
		return reportDao.getNtwkMeetRptListTotCnt(vo);
	}
	// 네트워크구축회의보고서 상세
	@Override
	public EgovMap getNtwkMeetRptDtl(NtwkMeetRptVO vo) {
		return reportDao.getNtwkMeetRptDtl(vo);
	}
	// 네트워크구축회의보고서 등록
	@Override
	public void insertNtwkMeetRpt(NtwkMeetRptVO vo) {
		
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "report");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getInputNm());
			
			vo.setFileName(fMap.get("fileNm").toString());
			vo.setSysFileName(fMap.get("sysFileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			try {
				vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
			}catch(Exception err) {
				vo.setFileSize(0);
			}
		}
		
		reportDao.insertNtwkMeetRpt(vo);
	}
	// 네트워크구축회의보고서 엑셀
	@Override
	public List<EgovMap> getNtwkMeetRptListExcel(ExcelVO vo) {
		return reportDao.getNtwkMeetRptListExcel(vo);
	}
	// 네트워크구축회의보고서 수정
	@Override
	public void updateNtwkMeetRpt(NtwkMeetRptVO vo) {
		
		if(vo.getFile()!=null) {
			if(vo.getFile().getSize() != 0) {
				System.out.println("File Upload Start");
				MultipartFile file = vo.getFile();
				System.out.println("Upload File Name : " + vo.getFile().getName());
				
				EgovMap fMap = AMSComm.fileUpload(file, "report");
				
				System.out.println("File Upload End");
				
				fMap.put("regId", vo.getRegId());
				fMap.put("writer", vo.getInputNm());
				
				vo.setFileName(fMap.get("fileNm").toString());
				vo.setSysFileName(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				try {
					vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
				}catch(Exception err) {
					vo.setFileSize(0);
				}
			}
		}else {
			vo.setFileName(null);
			vo.setSysFileName(null);
			vo.setFilePath(null);
			vo.setFileSize(0);
		}
		
		reportDao.updateNtwkMeetRpt(vo);
	}
	
	// 네트워크구축회의보고서 삭제
	@Override
	public void deleteNtwkMeetRpt(NtwkMeetRptVO vo) {
		reportDao.deleteNtwkMeetRpt(vo);
	}
	// 교육활동보고서 목록
	@Override
	public List<EgovMap> getEduAtvyRptList(EduAtvyRptVO vo) {
		return reportDao.getEduAtvyRptList(vo);
	}
	// 교육활동보고서 갯수
	@Override
	public int getEduAtvyRptListTotCnt(EduAtvyRptVO vo) {
		return reportDao.getEduAtvyRptListTotCnt(vo);
	}
	// 교육활동보고서 상세
	@Override
	public EgovMap getEduAtvyRptDtl(EduAtvyRptVO vo) {
		return reportDao.getEduAtvyRptDtl(vo);
	}
	// 교육활동보고서 엑셀
	@Override
	public List<EgovMap> getEduAtvyRptListExcel(ExcelVO vo) {
		return reportDao.getEduAtvyRptListExcel(vo);
	}
	// 교육활동보고서 등록
	@Override
	public void insertEduAtvyRpt(EduAtvyRptVO vo) {
		
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "report");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getInputNm());
			
			vo.setFileName(fMap.get("fileNm").toString());
			vo.setSysFileName(fMap.get("sysFileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			try {
				vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
			}catch(Exception err) {
				vo.setFileSize(0);
			}
		}
		
		reportDao.insertEduAtvyRpt(vo);
	}
	// 교육활동보고서 수정
	@Override
	public void updateEduAtvyRpt(EduAtvyRptVO vo) {
		
		if(vo.getFile()!=null) {
			if(vo.getFile().getSize() != 0) {
				System.out.println("File Upload Start");
				MultipartFile file = vo.getFile();
				System.out.println("Upload File Name : " + vo.getFile().getName());
				
				EgovMap fMap = AMSComm.fileUpload(file, "report");
				
				System.out.println("File Upload End");
				
				fMap.put("regId", vo.getRegId());
				fMap.put("writer", vo.getInputNm());
				
				vo.setFileName(fMap.get("fileNm").toString());
				vo.setSysFileName(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				try {
					vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
				}catch(Exception err) {
					vo.setFileSize(0);
				}
			}
		}else {
			vo.setFileName(null);
			vo.setSysFileName(null);
			vo.setFilePath(null);
			vo.setFileSize(0);
		}
		
		reportDao.updateEduAtvyRpt(vo);
	}
	
	// 교육활동보고서 삭제
	@Override
	public void deleteEduAtvyRpt(EduAtvyRptVO vo) {
		reportDao.deleteEduAtvyRpt(vo);
	}
	
	// 홍보활동보고서 목록
	@Override
	public List<EgovMap> getPrAtvyRptList(PrAtvyRptVO vo) {
		return reportDao.getPrAtvyRptList(vo);
	}
	// 홍보활동보고서 갯수
	@Override
	public int getPrAtvyRptListTotCnt(PrAtvyRptVO vo) {
		return reportDao.getPrAtvyRptListTotCnt(vo);
	}
	// 홍보활동보고서 상세
	@Override
	public EgovMap getPrAtvyRptDtl(PrAtvyRptVO vo) {
		return reportDao.getPrAtvyRptDtl(vo);
	}
	// 홍보활동보고서 엑셀
	@Override
	public List<EgovMap> getPrAtvyRptListExcel(ExcelVO vo) {
		return reportDao.getPrAtvyRptListExcel(vo);
	}
	// 홍보활동보고서 등록
	@Override
	public void insertPrAtvyRpt(PrAtvyRptVO vo) {
		
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "report");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getInputNm());
			
			vo.setFileNm(fMap.get("fileNm").toString());
			vo.setSysFileNm(fMap.get("sysFileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			try {
				vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
			}catch(Exception err) {
				vo.setFileSize(0);
			}
		}
		
		reportDao.insertPrAtvyRpt(vo);
	}
	// 홍보활동보고서 수정
	@Override
	public void updatePrAtvyRpt(PrAtvyRptVO vo) {
		
		if(vo.getFile()!=null) {
			if(vo.getFile().getSize() != 0) {
				System.out.println("File Upload Start");
				MultipartFile file = vo.getFile();
				System.out.println("Upload File Name : " + vo.getFile().getName());
				
				EgovMap fMap = AMSComm.fileUpload(file, "report");
				
				System.out.println("File Upload End");
				
				fMap.put("regId", vo.getRegId());
				fMap.put("writer", vo.getInputNm());
				
				vo.setFileNm(fMap.get("fileNm").toString());
				vo.setSysFileNm(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				try {
					vo.setFileSize(Integer.parseInt(fMap.get("fileSize").toString()));
				}catch(Exception err) {
					vo.setFileSize(0);
				}
			}
		}else {
			vo.setFileNm(null);
			vo.setSysFileNm(null);
			vo.setFilePath(null);
			vo.setFileSize(0);
		}
		
		reportDao.updatePrAtvyRpt(vo);
	}
	
	// 홍보활동보고서 삭제
	@Override
	public void deletePrAtvyRpt(PrAtvyRptVO vo) {
		reportDao.deletePrAtvyRpt(vo);
	}
	
	// 상담사례종결서 삭제
	@Override
	public void deleteCnsEndDtl(int num) {
		reportDao.deleteCnsEndDtl(num);
	}

}
