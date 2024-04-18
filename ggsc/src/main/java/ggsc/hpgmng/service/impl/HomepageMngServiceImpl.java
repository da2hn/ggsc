package ggsc.hpgmng.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import ams.cmm.AES256Crypto;
import ams.cmm.AMSComm;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ggsc.hpgmng.service.EduAppVO;
import ggsc.hpgmng.service.FreeBrdVO;
import ggsc.hpgmng.service.HomepageMngService;
import ggsc.hpgmng.service.LibraryVO;
import ggsc.hpgmng.service.NewsVO;
import ggsc.hpgmng.service.OnlineAskVO;
import ggsc.hpgmng.service.PopupVO;
import ggsc.hpgmng.service.YoutubeVO;
import ggsc.hpgmng.service.NoticeVO;

@Service("hpgmngService")
public class HomepageMngServiceImpl extends EgovAbstractServiceImpl implements HomepageMngService {
	
	@Resource(name="hpgmngDao")
    private HomepageMngDAO hpgmngDao;
	
	// 공지사항 목록
	@Override
	public List<EgovMap> getNoticeList(NoticeVO vo) {
		return hpgmngDao.getNoticeList(vo);
	}
	
	// 공지사항 갯수
	@Override
	public int getNoticeListTotCnt(NoticeVO vo) {
		return hpgmngDao.getNoticeListTotCnt(vo);
	}
	
	// 공지사항 등록
	@Override
	public void insertNotice(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		
		int noticeNum = 0;
		noticeNum = hpgmngDao.insertNotice(map);
		
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		for(int i=0; i<list.size(); i++) {
			list.get(i).put("fileNum",noticeNum);
			list.get(i).put("regId",map.get("regId"));
			list.get(i).put("writer",map.get("writer"));
			hpgmngDao.insertNoticeUpload(list.get(i));
		}
		
		/*if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "notice");
			vo.setFileNm((String)fMap.get("fileNm"));
			vo.setSysFileNm((String)fMap.get("sysFileNm"));
			vo.setFilePath((String)fMap.get("filePath"));
			// 첨부파일이 있으면 업로드
			supportDao.insertNoticeUpload(vo);			
		} else {
	
			vo.setFileNm("");
			vo.setSysFileNm("");
			vo.setFilePath("");
			// 첨부파일이 없을때
			supportDao.insertNoticeUpload(vo);	
		}*/
		/*Iterator<String> iterator = request.getFileNames();
		String requestName = null;
		String num = null;
		
		while(iterator.hasNext()){  // haxNext=>true , false값 반환
		    String fileName = iterator.next();
			MultipartFile multipartFile = request.getFile(fileName); // next() => 매개변수값 반환
			if(multipartFile.isEmpty() == false){
				EgovMap fMap = AMSComm.fileUpload(multipartFile, "report");

				fMap.put("regId", vo.getRegId());
				vo.setFileNm(fMap.get("fileNm").toString());
				vo.setSysFileNm(fMap.get("sysFileNm").toString());
				vo.setFilePath(fMap.get("filePath").toString());
				hpgmngDao.insertNoticeUpload(vo);
			} else {
				requestName = multipartFile.getName();
				try {
					num = "num"+requestName.substring(requestName.indexOf("_"));
				} catch(Exception e) {
					continue;
				}
				vo.setFileNm(null);
				vo.setSysFileNm(null);
				vo.setFilePath(null);
				hpgmngDao.insertNoticeUpload(vo);
			}
		}*/
	}
	
	// 공지사항 수정
	@Override
	public void updateNotice(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		hpgmngDao.updateNotice(map);
		
		hpgmngDao.deleteNoticeUploadGbY(map);
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		EgovMap fileMap = null;
		for(int i=0; i<list.size(); i++) {
			fileMap = list.get(i);
			fileMap.put("fileNum",map.get("fileNum"));
			fileMap.put("regId",map.get("regId"));
			fileMap.put("writer",map.get("writer"));
			if (fileMap.get("delGb").equals("Y")) {
				hpgmngDao.insertNoticeUpload(fileMap);
			} else {
				hpgmngDao.deleteNoticeUploadGbN(fileMap);
			}
		}
		
		/*
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "notice");
			vo.setFileNm((String)fMap.get("fileNm"));
			vo.setSysFileNm((String)fMap.get("sysFileNm"));
			vo.setFilePath((String)fMap.get("filePath"));
			// 첨부파일이 있으면 업로드
			hpgmngDao.updateNoticeUpload(vo);			
		} */
	}
	
	// 공지사항 삭제
	@Override
	public void deleteNotice(int noticeNum) {
		hpgmngDao.deleteNotice(noticeNum);
		hpgmngDao.deleteNoticeUpload(noticeNum);
	}
	
	// 공지사항 상세
	@Override
	public EgovMap getNoticeDtl(int num) {
		return hpgmngDao.getNoticeDtl(num);
	}
	
	// 공지사항 상세 파일 목록
	@Override
	public List<EgovMap> getNoticeFileDtl(int num) {
		return hpgmngDao.getNoticeFileDtl(num);
	}
	
	// 자유게시판 목록
	@Override
	public List<EgovMap> getFreeBoardList(FreeBrdVO vo) {
		return hpgmngDao.getFreeBoardList(vo);
	}
	
	// 자유게시판 갯수
	@Override
	public int getFreeBoardListTotCnt(FreeBrdVO vo) {
		return hpgmngDao.getFreeBoardListTotCnt(vo);
	}
	
	// 자유게시판 등록
	@Override
	public void insertFreeBoard(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		
		int freeBrdNum = 0;
		freeBrdNum = hpgmngDao.insertFreeBoard(map);
		
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		for(int i=0; i<list.size(); i++) {
			list.get(i).put("fileNum",freeBrdNum);
			list.get(i).put("regId",map.get("regId"));
			list.get(i).put("writer",map.get("writer"));
			hpgmngDao.insertFreeBoardUpload(list.get(i));
		}
	}
	
	// 자유게시판 수정
	@Override
	public void updateFreeBoard(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		hpgmngDao.updateFreeBoard(map);
		
		hpgmngDao.deleteFreeBoardUploadGbY(map);
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		EgovMap fileMap = null;
		for(int i=0; i<list.size(); i++) {
			fileMap = list.get(i);
			fileMap.put("fileNum",map.get("fileNum"));
			fileMap.put("regId",map.get("regId"));
			fileMap.put("writer",map.get("writer"));
			if (fileMap.get("delGb").equals("Y")) {
				hpgmngDao.insertFreeBoardUpload(fileMap);
			} else {
				hpgmngDao.deleteFreeBoardUploadGbN(fileMap);
			}
		}
	}
	
	// 자유게시판 삭제
	@Override
	public void deleteFreeBoard(int freeBrdNum) {
		hpgmngDao.deleteFreeBoard(freeBrdNum);
		hpgmngDao.deleteFreeBoardUpload(freeBrdNum);
	}
	
	// 자유게시판 상세
	@Override
	public EgovMap getFreeBoardDtl(int num) {
		return hpgmngDao.getFreeBoardDtl(num);
	}
	
	// 자유게시판 상세 파일 목록
	@Override
	public List<EgovMap> getFreeBoardFileDtl(int num) {
		return hpgmngDao.getFreeBoardFileDtl(num);
	}
	
	
	// 문서자료실 목록
	@Override
	public List<EgovMap> getLibraryList(LibraryVO vo) {
		return hpgmngDao.getLibraryList(vo);
	}
	
	// 문서자료실 갯수
	@Override
	public int getLibraryListTotCnt(LibraryVO vo) {
		return hpgmngDao.getLibraryListTotCnt(vo);
	}
	
	// 문서자료실 등록
	@Override
	public void insertLibrary(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		
		int libraryNum = 0;
		libraryNum = hpgmngDao.insertLibrary(map);
		
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		for(int i=0; i<list.size(); i++) {
			list.get(i).put("fileNum",libraryNum);
			list.get(i).put("regId",map.get("regId"));
			list.get(i).put("writer",map.get("writer"));
			hpgmngDao.insertLibraryUpload(list.get(i));
		}
	}
	
	// 문서자료실 수정
	@Override
	public void updateLibrary(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		hpgmngDao.updateLibrary(map);
		
		hpgmngDao.deleteLibraryUploadGbY(map);
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		EgovMap fileMap = null;
		for(int i=0; i<list.size(); i++) {
			fileMap = list.get(i);
			fileMap.put("fileNum",map.get("fileNum"));
			fileMap.put("regId",map.get("regId"));
			fileMap.put("writer",map.get("writer"));
			if (fileMap.get("delGb").equals("Y")) {
				hpgmngDao.insertLibraryUpload(fileMap);
			} else {
				hpgmngDao.deleteLibraryUploadGbN(fileMap);
			}
		}
	}
	
	// 문서자료실 삭제
	@Override
	public void deleteLibrary(int newsNum) {
		hpgmngDao.deleteLibrary(newsNum);
		hpgmngDao.deleteLibraryUpload(newsNum);
	}
	
	// 문서자료실 상세
	@Override
	public EgovMap getLibraryDtl(int num) {
		return hpgmngDao.getLibraryDtl(num);
	}
	
	// 문서자료실 상세 파일 목록
	@Override
	public List<EgovMap> getLibraryFileDtl(int num) {
		return hpgmngDao.getLibraryFileDtl(num);
	}
		
	
	// 센터소식 목록
	@Override
	public List<EgovMap> getCenterNewsList(NewsVO vo) {
		return hpgmngDao.getCenterNewsList(vo);
	}
	
	// 센터소식 갯수
	@Override
	public int getCenterNewsListTotCnt(NewsVO vo) {
		return hpgmngDao.getCenterNewsListTotCnt(vo);
	}
	
	// 센터소식 등록
	@Override
	public void insertCenterNews(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		
		int newsNum = 0;
		newsNum = hpgmngDao.insertCenterNews(map);
		
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		for(int i=0; i<list.size(); i++) {
			list.get(i).put("fileNum",newsNum);
			list.get(i).put("regId",map.get("regId"));
			list.get(i).put("writer",map.get("writer"));
			hpgmngDao.insertCenterNewsUpload(list.get(i));
		}
	}
	
	// 센터소식 수정
	@Override
	public void updateCenterNews(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		hpgmngDao.updateCenterNews(map);
		
		hpgmngDao.deleteCenterNewsUploadGbY(map);
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		EgovMap fileMap = null;
		for(int i=0; i<list.size(); i++) {
			fileMap = list.get(i);
			fileMap.put("fileNum",map.get("fileNum"));
			fileMap.put("regId",map.get("regId"));
			fileMap.put("writer",map.get("writer"));
			if (fileMap.get("delGb").equals("Y")) {
				hpgmngDao.insertCenterNewsUpload(fileMap);
			} else {
				hpgmngDao.deleteCenterNewsUploadGbN(fileMap);
			}
		}
	}
	
	// 게시판관리 삭제
	@Override
	public void deleteCenterNews(int newsNum) {
		hpgmngDao.deleteCenterNews(newsNum);
		hpgmngDao.deleteCenterNewsUpload(newsNum);
	}
	
	// 센터소식 상세
	@Override
	public EgovMap getCenterNewsDtl(int num) {
		return hpgmngDao.getCenterNewsDtl(num);
	}
	
	// 센터소식 상세 파일 목록
	@Override
	public List<EgovMap> getCenterNewsFileDtl(int num) {
		return hpgmngDao.getCenterNewsFileDtl(num);
	}
	
	// 온라인상담 목록
	@Override
	public List<EgovMap> getOnlineAskList(OnlineAskVO vo) {
		return hpgmngDao.getOnlineAskList(vo);
	}
	
	// 온라인상담 갯수
	@Override
	public int getOnlineAskListTotCnt(OnlineAskVO vo) {
		return hpgmngDao.getOnlineAskListTotCnt(vo);
	}
	
	// 온라인상담 등록
	@Override
	public void insertOnlineAsk(OnlineAskVO vo) {
		hpgmngDao.insertOnlineAsk(vo);
		
		// 온라인상담 답변 여부 수정
		hpgmngDao.updateOnlineAskAnswer(vo);
	}
	
	// 온라인상담 수정
	@Override
	public void updateOnlineAsk(OnlineAskVO vo) {
		hpgmngDao.updateOnlineAsk(vo);
	}
	
	// 온라인상담 삭제
	@Override
	public void deleteOnlineAsk(int onlineNum) {
		hpgmngDao.deleteOnlineAsk(onlineNum);
		hpgmngDao.deleteOnlineAskAns(onlineNum);
	}
	
	// 온라인상담 상세
	@Override
	public EgovMap getOnlineAskDtl(int num) {
		return hpgmngDao.getOnlineAskDtl(num);
	}
	
	// 교육신청 목록
	@Override
	public List<EgovMap> getEduAppList(EduAppVO vo) {
		return hpgmngDao.getEduAppList(vo);
	}
	
	// 교육신청 갯수
	@Override
	public int getEduAppListTotCnt(EduAppVO vo) {
		return hpgmngDao.getEduAppListTotCnt(vo);
	}
	
	// 교육신청 상세
	@Override
	public EgovMap getEduAppDtl(int num) {
		return hpgmngDao.getEduAppDtl(num);
	}
	
	// 교육신청 수정
	@Override
	public void updateEduApp(EduAppVO vo) {
		hpgmngDao.updateEduApp(vo);
	}
	
	// 팝업 목록
	@Override
	public List<EgovMap> getPopupList(PopupVO vo) {
		return hpgmngDao.getPopupList(vo);
	}
	
	// 팝업 갯수
	@Override
	public int getPopupListTotCnt(PopupVO vo) {
		return hpgmngDao.getPopupListTotCnt(vo);
	}
	
	// 팝업 상세
	@Override
	public EgovMap getPopupDtl(int num) {
		return hpgmngDao.getPopupDtl(num);
	}
	
	// 팝업 등록
	@Override
	public void insertPopup(PopupVO vo) {
		hpgmngDao.insertPopup(vo);
		
		if(vo.getFile().getSize() != 0) {
			String RealFileName = "";
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "popup");
			fMap.put("regId", vo.getRegId());
			fMap.put("writer", vo.getWriter());
			
			vo.setFileNm(fMap.get("fileNm").toString());
			vo.setFilePath(fMap.get("filePath").toString());
			vo.setSysFileNm(fMap.get("sysFileNm").toString());
			try {
				RealFileName = AES256Crypto.getInstance().AESDecode(vo.getSysFileNm());
			} catch (InvalidKeyException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Invalid Key Exception");
			} catch (NoSuchAlgorithmException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : No Such AlgorithmException");
			} catch (NoSuchPaddingException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : No Such Padding Exception");
			} catch (InvalidAlgorithmParameterException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Invaild Algorithm Parameter Exception");
			} catch (IllegalBlockSizeException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Illegal Block Size Exception");
			} catch (BadPaddingException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Bad Padding Exception");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			vo.setRealFileNm(RealFileName);
			
			// 첨부파일이 있으면 업로드
			hpgmngDao.insertPopupUpload(vo);			
		}
	}
	
	// 팝업 수정
	@Override
	public void updatePopup(PopupVO vo) {
		hpgmngDao.updatePopup(vo);
		
		if(vo.getFile().getSize() != 0) {
			String RealFileName = "";
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "popup");
			vo.setFileNm((String)fMap.get("fileNm"));
			vo.setSysFileNm((String)fMap.get("sysFileNm"));
			vo.setFilePath((String)fMap.get("filePath"));
			try {
				RealFileName = AES256Crypto.getInstance().AESDecode(vo.getSysFileNm());
			} catch (InvalidKeyException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Invalid Key Exception");
			} catch (NoSuchAlgorithmException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : No Such AlgorithmException");
			} catch (NoSuchPaddingException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : No Such Padding Exception");
			} catch (InvalidAlgorithmParameterException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Invaild Algorithm Parameter Exception");
			} catch (IllegalBlockSizeException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Illegal Block Size Exception");
			} catch (BadPaddingException e) {
				RealFileName = vo.getSysFileNm();
				utility.func.Logging(this.getClass().getName(), "DECODE_ERROR : Bad Padding Exception");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			vo.setRealFileNm(RealFileName);
			// 첨부파일이 있으면 업로드
			hpgmngDao.updatePopupUpload(vo);			
		} 
	}
	
	// 팝업 수정
	@Override
	public void deletePopup(PopupVO vo) {
		hpgmngDao.deletePopup(vo);
		hpgmngDao.deletePopupUpload(vo);			
	}
	
	// 유튜브 목록
	@Override
	public List<EgovMap> getYoutubeList(YoutubeVO vo) {
		return hpgmngDao.getYoutubeList(vo);
	}
	
	// 유튜브 갯수
	@Override
	public int getYoutubeListTotCnt(YoutubeVO vo) {
		return hpgmngDao.getYoutubeListTotCnt(vo);
	}
	
	// 유튜브 등록
	@Override
	public void insertYoutube(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		
		int youtubeNum = 0;
		youtubeNum = hpgmngDao.insertYoutube(map);
		
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		for(int i=0; i<list.size(); i++) {
			list.get(i).put("fileNum",youtubeNum);
			list.get(i).put("regId",map.get("regId"));
			list.get(i).put("writer",map.get("writer"));
			hpgmngDao.insertYoutubeUpload(list.get(i));
		}
		
	}
	
	// 유튜브 수정
	@Override
	public void updateYoutube(Map<String,Object> map, MultipartHttpServletRequest request) throws Exception {
		hpgmngDao.updateYoutube(map);
		
		hpgmngDao.deleteYoutubeUploadGbY(map);
		List<EgovMap> list = AMSComm.parseInsertFileInfo(map,request);
		EgovMap fileMap = null;
		for(int i=0; i<list.size(); i++) {
			fileMap = list.get(i);
			fileMap.put("fileNum",map.get("fileNum"));
			fileMap.put("regId",map.get("regId"));
			fileMap.put("writer",map.get("writer"));
			if (fileMap.get("delGb").equals("Y")) {
				hpgmngDao.insertYoutubeUpload(fileMap);
			} else {
				hpgmngDao.deleteYoutubeUploadGbN(fileMap);
			}
		}
		
		/*
		if(vo.getFile().getSize() != 0) {
			MultipartFile file = vo.getFile();
			EgovMap fMap = AMSComm.fileUpload(file, "youtube");
			vo.setFileNm((String)fMap.get("fileNm"));
			vo.setSysFileNm((String)fMap.get("sysFileNm"));
			vo.setFilePath((String)fMap.get("filePath"));
			// 첨부파일이 있으면 업로드
			hpgmngDao.updateYoutubeUpload(vo);			
		} */
	}
	
	// 유튜브 삭제
	@Override
	public void deleteYoutube(int youtubeNum) {
		hpgmngDao.deleteYoutube(youtubeNum);
		hpgmngDao.deleteYoutubeUpload(youtubeNum);
	}
	
	// 유튜브 상세
	@Override
	public EgovMap getYoutubeDtl(int num) {
		return hpgmngDao.getYoutubeDtl(num);
	}
	
	// 유튜브 상세 파일 목록
	@Override
	public List<EgovMap> getYoutubeFileDtl(int num) {
		return hpgmngDao.getYoutubeFileDtl(num);
	}

//	// FAQ 목록
//	@Override
//	public List<EgovMap> getFaqList(FaqVO vo) {
//		return supportDao.getFaqList(vo);
//	}
//	
//	// FAQ 갯수
//	@Override
//	public int getFaqListTotCnt(FaqVO vo) {
//		return supportDao.getFaqListTotCnt(vo);
//	}
//	
//	// FAQ 등록
//	@Override
//	public void insertFaq(FaqVO vo) {
//		supportDao.insertFaq(vo);
//	}
//	
//	// FAQ 상세
//	@Override
//	public EgovMap getFaqDetail(int num) {
//		return supportDao.getFaqDetail(num);
//	}
//	
//	// FAQ 수정
//	@Override
//	public void updateFaq(FaqVO vo) {
//		supportDao.updateFaq(vo);
//	}
//	

}
