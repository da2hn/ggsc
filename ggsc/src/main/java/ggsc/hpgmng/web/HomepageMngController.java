package ggsc.hpgmng.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import ggsc.hpgmng.service.EduAppVO;
import ggsc.hpgmng.service.FreeBrdVO;
import ggsc.hpgmng.service.HomepageMngService;
import ggsc.hpgmng.service.LibraryVO;
import ggsc.hpgmng.service.NewsVO;
import ggsc.hpgmng.service.NoticeVO;
import ggsc.hpgmng.service.OnlineAskVO;
import ggsc.hpgmng.service.PopupVO;
import ggsc.hpgmng.service.YoutubeVO;


@Controller

public class HomepageMngController {
	@Resource(name = "hpgmngService")
	private HomepageMngService hpgmngService;

	@RequestMapping(value = "/noticeList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String noticeList(NoticeVO vo, HttpServletRequest request, ModelMap model){	
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
		
		List<EgovMap> noticeList = hpgmngService.getNoticeList(vo);
		model.addAttribute("noticeList", noticeList);
		int totalPageCnt = hpgmngService.getNoticeListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);
		
		return "hpgmng/notice_list.main";
	}
	
	@RequestMapping(value = "/noticeForm.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String noticeForm(NoticeVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		
		
		model.addAttribute("mnuCd", mnuCd);
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		
		return "hpgmng/notice_reg.main";
	}
	
	@RequestMapping(value = "/noticeDtl.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String noticeDtl(NoticeVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap notice = null;
		List<EgovMap> file = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			notice = hpgmngService.getNoticeDtl(intNum);
			file = hpgmngService.getNoticeFileDtl(intNum);
		}
		
		
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		model.addAttribute("notice", notice);
		model.addAttribute("file", file);
		
		return "hpgmng/notice_dtl.main";
	}
	
	@RequestMapping(value = "/noticeReg.do", method = RequestMethod.POST)
	public String noticeReg(@RequestParam Map<String,Object> map, MultipartHttpServletRequest  request, ModelMap model) throws Exception{
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap login = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)login.get("userId");
		String writer = (String)login.get("userNm");
		map.put("regId", regId);
		map.put("writer", writer);
		
		int noticeNum = Integer.parseInt((String) map.get("num"));
		
		if(save.equals("S")) {
			hpgmngService.insertNotice(map,request);	
		} else if(save.equals("U")) {
			map.put("fileNum",noticeNum);
			hpgmngService.updateNotice(map,request);
		} else if(save.equals("D")) {
			hpgmngService.deleteNotice(noticeNum);
		}
		
		return "redirect:/gnoincoundb/noticeList.do?mnuCd=" + mnuCd;
	}
	
	@RequestMapping(value = "/freeBoardList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String freeBoardList(FreeBrdVO vo, HttpServletRequest request, ModelMap model){	
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
		
		List<EgovMap> freeBrdList = hpgmngService.getFreeBoardList(vo);
		model.addAttribute("freeBrdList", freeBrdList);
		int totalPageCnt = hpgmngService.getFreeBoardListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);
		
		return "hpgmng/freeBoard_list.main";
	}
	
	@RequestMapping(value = "/freeBoardForm.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String freeBoardForm(FreeBrdVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		
		
		model.addAttribute("mnuCd", mnuCd);
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		
		return "hpgmng/freeBoard_reg.main";
	}
	
	@RequestMapping(value = "/freeBoardDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String freeBoardDtl(FreeBrdVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap freeBoard = null;
		List<EgovMap> file = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			freeBoard = hpgmngService.getFreeBoardDtl(intNum);
			file = hpgmngService.getFreeBoardFileDtl(intNum);
		}
		
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		model.addAttribute("freeBoard", freeBoard);
		model.addAttribute("file", file);
		
		return "hpgmng/freeBoard_dtl.main";
	}
	
	@RequestMapping(value = "/freeBoardReg.do", method = RequestMethod.POST)
	public String freeBoardReg(@RequestParam Map<String,Object> map, MultipartHttpServletRequest request,  ModelMap model) throws Exception{
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap login = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)login.get("userId");
		String writer = (String)login.get("userNm");
		map.put("regId", regId);
		map.put("writer", writer);
		
		int freeBrdNum = Integer.parseInt((String) map.get("num"));
		
		if(save.equals("S")) {
			hpgmngService.insertFreeBoard(map,request);	
		} else if(save.equals("U")) {
			map.put("fileNum",freeBrdNum);
			hpgmngService.updateFreeBoard(map,request);
		} else if(save.equals("D")) {
			hpgmngService.deleteFreeBoard(freeBrdNum);
		}
		
		return "redirect:/gnoincoundb/freeBoardList.do?mnuCd=" + mnuCd;
	}
	
	@RequestMapping(value = "/libraryList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String libraryList(LibraryVO vo, HttpServletRequest request, ModelMap model){		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
		
		List<EgovMap> libraryList = hpgmngService.getLibraryList(vo);
		model.addAttribute("libraryList", libraryList);
		int totalPageCnt = hpgmngService.getLibraryListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);
		
		return "hpgmng/library_list.main";
	}
	
	@RequestMapping(value = "/libraryForm.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String libraryForm(LibraryVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		
		
		model.addAttribute("mnuCd", mnuCd);
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		
		return "hpgmng/library_reg.main";
	}
	
	@RequestMapping(value = "/libraryDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String libraryDtl(LibraryVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap library = null;
		List<EgovMap> file = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			library = hpgmngService.getLibraryDtl(intNum);
			file = hpgmngService.getLibraryFileDtl(intNum);
		}
		
		
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		model.addAttribute("library", library);
		model.addAttribute("file", file);
		
		return "hpgmng/library_dtl.main";
	}
	
	@RequestMapping(value = "/libraryReg.do", method = RequestMethod.POST)
	public String libraryReg(@RequestParam Map<String,Object> map, MultipartHttpServletRequest request,  ModelMap model) throws Exception{
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap login = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)login.get("userId");
		String writer = (String)login.get("userNm");
		map.put("regId", regId);
		map.put("writer", writer);
		
		int libraryNum = Integer.parseInt((String) map.get("num"));
		
		if(save.equals("S")) {
			hpgmngService.insertLibrary(map,request);	
		} else if(save.equals("U")) {
			map.put("fileNum",libraryNum);
			hpgmngService.updateLibrary(map,request);
		} else if(save.equals("D")) {
			hpgmngService.deleteLibrary(libraryNum);
		}
		
		return "redirect:/gnoincoundb/libraryList.do?mnuCd=" + mnuCd;
	}
	
	@RequestMapping(value = "/centerNewsList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String centerNewsList(NewsVO vo, HttpServletRequest request, ModelMap model){	
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		int userAuth, userCenterGb;
	      try {
	         userAuth = Integer.parseInt(loginVO.get("authCd").toString());
	         // userCenterGb = Integer.parseInt(loginVO.get("centerGb").toString()); 
	         if(userAuth >= 4) {
	        	 model.addAttribute("authCdYn","N");
	         } else {
	        	 model.addAttribute("authCdYn","Y");
	         }
	         if(userAuth==0) userAuth = 10;
	      }catch(NumberFormatException  err) {
	         userAuth = 10;
	         userCenterGb = 0;
	      }
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
		
		List<EgovMap> newsList = hpgmngService.getCenterNewsList(vo);
		model.addAttribute("newsList", newsList);
		int totalPageCnt = hpgmngService.getCenterNewsListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);
		
		return "hpgmng/centerNews_list.main";
	}
	
	@RequestMapping(value = "/centerNewsForm.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String centerNewsForm(NewsVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		
		
		model.addAttribute("mnuCd", mnuCd);
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		
		return "hpgmng/centerNews_reg.main";
	}
	
	/*@RequestMapping(value = "/centerNewsDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String centerNewsDtl(NewsVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap newsDtl = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			newsDtl = hpgmngService.getCenterNewsDtl(intNum);
			
			String userId = (String)loginVO.get("userId");
			String regId = (String)newsDtl.get("writer");
			int userAuth, userCenterGb;
		      try {
		         userAuth = Integer.parseInt(loginVO.get("authCd").toString());
		         // userCenterGb = Integer.parseInt(loginVO.get("centerGb").toString()); 
		         if(userAuth >= 3) {
		        	 if(userId.equals(regId)) {
		        		 model.addAttribute("authCdYn","Y");
		        	 }
		        	 if(userAuth == 1) {
		        		 model.addAttribute("authCdYn","SY");
		        	 }
		        	 model.addAttribute("authCdYn","N");
		         } else {
		        	 model.addAttribute("authCdYn","Y");
		         }
		         if(userAuth==0) userAuth = 10;
		      }catch(NumberFormatException  err) {
		         userAuth = 10;
		         userCenterGb = 0;
		      }
		} else {
			int userAuth, userCenterGb;
			try {
		         userAuth = Integer.parseInt(loginVO.get("authCd").toString());
		         // userCenterGb = Integer.parseInt(loginVO.get("centerGb").toString()); 
		         if(userAuth >= 3) {
		        	 if(userAuth == 1) {
		        		 model.addAttribute("authCdYn","SY");
		        	 }
		        	 model.addAttribute("authCdYn","N");
		         } else {
		        	 model.addAttribute("authCdYn","Y");
		         }
		         if(userAuth==0) userAuth = 10;
		      }catch(NumberFormatException  err) {
		         userAuth = 10;
		         userCenterGb = 0;
		      }
		}
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("detail", newsDtl);
		
		return "hpgmng/centerNews_dtl.main";
	}*/
	
	@RequestMapping(value = "/centerNewsDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String centerNewsDtl(NewsVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap centerNews = null;
		List<EgovMap> file = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			centerNews = hpgmngService.getCenterNewsDtl(intNum);
			file = hpgmngService.getCenterNewsFileDtl(intNum);
		}
		
		
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		model.addAttribute("centerNews", centerNews);
		model.addAttribute("file", file);
		
		return "hpgmng/centerNews_dtl.main";
	}
	
	@RequestMapping(value = "/centerNewsReg.do", method = RequestMethod.POST)
	public String centerNewsReg(@RequestParam Map<String,Object> map, MultipartHttpServletRequest request,  ModelMap model) throws Exception{
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap login = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)login.get("userId");
		String writer = (String)login.get("userNm");
		map.put("regId", regId);
		map.put("writer", writer);
		
		int newsNum = Integer.parseInt((String) map.get("num"));
		
		if(save.equals("S")) {
			hpgmngService.insertCenterNews(map,request);	
		} else if(save.equals("U")) {
			map.put("fileNum",newsNum);
			hpgmngService.updateCenterNews(map,request);
		} else if(save.equals("D")) {
			hpgmngService.deleteCenterNews(newsNum);
		}
		
		return "redirect:/gnoincoundb/centerNewsList.do?mnuCd=" + mnuCd;
	}
	
	@RequestMapping(value = "/onlineAskList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String onlineAskList(OnlineAskVO vo, HttpServletRequest request, ModelMap model){	
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
		
		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
		
		List<EgovMap> onlineAskList = hpgmngService.getOnlineAskList(vo);
		model.addAttribute("onlineAskList", onlineAskList);
		int totalPageCnt = hpgmngService.getOnlineAskListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);
		
		return "hpgmng/onlineAsk_list.main";
	}
	
	@RequestMapping(value = "/onlineAskDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String onlineAskDtl(OnlineAskVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
		// 권한 관리 끝
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap onlineAskDtl = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			onlineAskDtl = hpgmngService.getOnlineAskDtl(intNum);
		}
		
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("detail", onlineAskDtl);
		model.addAttribute("vo", vo);
		model.addAttribute("userId", loginVO.get("userId").toString());
		
		return "hpgmng/onlineAsk_dtl.main";
	}
	
	@RequestMapping(value = "/onlineAskReg.do", method = RequestMethod.POST)
	public String onlineAskReg(HttpServletRequest request, ModelMap model, OnlineAskVO vo){
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)map.get("userId");
		String regNm = (String)map.get("userNm");
		vo.setRegNm(regNm);
		vo.setRegId(regId);
		
		int onlineNum = vo.getNum();
		
		if(save.equals("S")) {
			hpgmngService.insertOnlineAsk(vo);	
		} else if(save.equals("U")) {
			hpgmngService.updateOnlineAsk(vo);
		} else if(save.equals("D")) {
			hpgmngService.deleteOnlineAsk(onlineNum);
		}
		
		return "redirect:/gnoincoundb/onlineAskList.do?mnuCd=" + mnuCd;
	}
	
	// 교육신청 목록
	@RequestMapping(value = "/eduAppList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String eduAppList(EduAppVO vo, HttpServletRequest request, ModelMap model) {
		EgovMap loginVO = (EgovMap) request.getSession().getAttribute("LoginVO");

		int userAuth, userCenterGb;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			// userCenterGb = Integer.parseInt(loginVO.get("centerGb").toString());
			if (userAuth >= 4) {
				model.addAttribute("authCdYn", "N");
			} else {
				model.addAttribute("authCdYn", "Y");
			}
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
			userCenterGb = 0;
		}

		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);

		model.addAttribute("authCd", userAuth);
		List<EgovMap> eduAppList = hpgmngService.getEduAppList(vo);
		model.addAttribute("eduAppList", eduAppList);
		int totalPageCnt = hpgmngService.getEduAppListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);

		return "hpgmng/eduApp_list.main";

	}
	
	
	@RequestMapping(value = "/eduAppDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String eduAppDtl(EduAppVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
		// 권한 관리 끝
		
		EgovMap eduAppDtl = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			eduAppDtl = hpgmngService.getEduAppDtl(intNum);
		} 
		
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("detail", eduAppDtl);
		model.addAttribute("authCd", userAuth);
		model.addAttribute("userId",loginVO.get("userId").toString());
		model.addAttribute("vo", vo);
		return "hpgmng/eduApp_dtl.main";
	}
	
	
	@RequestMapping(value = "/eduAppReg.do", method = RequestMethod.POST)
	public String eduAppReg(HttpServletRequest request, ModelMap model, EduAppVO vo){
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)map.get("userId");
		String writer = (String)map.get("userNm");
		vo.setWriter(writer);
		vo.setRegId(regId);
		
		/*if(save.equals("S")) {
			hpgmngService.insertCenterNews(vo);	
		} else if(save.equals("U")) {
			hpgmngService.updateCenterNews(vo);
		} */
		
		if(save.equals("U")) {
			hpgmngService.updateEduApp(vo);
		} 
		
		return "redirect:/gnoincoundb/eduAppList.do?mnuCd=" + mnuCd;
	}
	
	// 팝업 목록
	@RequestMapping(value = "/popupList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String popupList(PopupVO vo, HttpServletRequest request, ModelMap model) {
		EgovMap loginVO = (EgovMap) request.getSession().getAttribute("LoginVO");

		int userAuth, userCenterGb;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			// userCenterGb = Integer.parseInt(loginVO.get("centerGb").toString());
			if (userAuth >= 4) {
				model.addAttribute("authCdYn", "N");
			} else {
				model.addAttribute("authCdYn", "Y");
			}
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
			userCenterGb = 0;
		}

		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);

		model.addAttribute("authCd", userAuth);
		List<EgovMap> popupList = hpgmngService.getPopupList(vo);
		model.addAttribute("popupList", popupList);
		int totalPageCnt = hpgmngService.getPopupListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);

		return "hpgmng/popup_list.main";

	}
	
	// 팝업 상세
	@RequestMapping(value = "/popupDtl.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String popupDtl(PopupVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
		// 권한 관리 끝
		
		EgovMap popupDtl = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			popupDtl = hpgmngService.getPopupDtl(intNum);
		} 
		
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("detail", popupDtl);
		model.addAttribute("authCd", userAuth);
		model.addAttribute("userId",loginVO.get("userId").toString());
		model.addAttribute("vo", vo);
		return "hpgmng/popup_dtl.main";
	}
		
	// 팝업 등록
	@RequestMapping(value = "/popupReg.do", method = RequestMethod.POST)
	public String popupReg(HttpServletRequest request, ModelMap model, PopupVO vo){
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)map.get("userId");
		String writer = (String)map.get("userNm");
		vo.setWriter(writer);
		vo.setRegId(regId);
		
		if(save.equals("S")) {
			hpgmngService.insertPopup(vo);
		} else if(save.equals("U")) {
			hpgmngService.updatePopup(vo);
		} else if(save.equals("D")) {
			hpgmngService.deletePopup(vo);
		}
		
		return "redirect:/gnoincoundb/popupList.do?mnuCd=" + mnuCd;
	}
	
	@RequestMapping(value = "/youtubeList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String youtubeList(YoutubeVO vo, HttpServletRequest request, ModelMap model){	
		
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		model.addAttribute("mnuCd", mnuCd);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈

		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
		
		List<EgovMap> youtubeList = hpgmngService.getYoutubeList(vo);
		model.addAttribute("youtubeList", youtubeList);
		int totalPageCnt = hpgmngService.getYoutubeListTotCnt(vo);
		model.addAttribute("totalPageCnt", totalPageCnt);
		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("vo", vo);
		
		return "hpgmng/youtube_list.main";
	}
	
	@RequestMapping(value = "/youtubeForm.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String youtubeForm(YoutubeVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		
		
		model.addAttribute("mnuCd", mnuCd);
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		
		return "hpgmng/youtube_reg.main";
	}
	
	@RequestMapping(value = "/youtubeDtl.do", method = { RequestMethod.GET, RequestMethod.POST } )
	public String youtubeDtl(YoutubeVO vo, HttpServletRequest request, ModelMap model){
		
		EgovMap loginVO = (EgovMap)request.getSession().getAttribute("LoginVO");
		
		// 권한 관리 시작
		int userAuth;
		try {
			userAuth = Integer.parseInt(loginVO.get("authCd").toString());
			if (userAuth == 0)
				userAuth = 10;
		} catch (NumberFormatException err) {
			userAuth = 10;
		} catch (NullPointerException err) {
			userAuth = 10;
		}
	
		/*if (userAuth > 1) { // 센터 검색 권한이 없으면
			vo.setSchCenterGb(Integer.toString(userCenterGb));
		}*/
		
		switch (userAuth) {
			case 1: vo.setAuthCd(1); break; 
			case 2: vo.setAuthCd(2); break; 
			case 3: vo.setAuthCd(3); break; 
				default: vo.setAuthCd(4); break; 
		}
				// 권한 관리 끝
		/*
		if(request.getParameter("page") != null){
			model.addAttribute("page", request.getParameter("page"));
		}
		*/
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
		model.addAttribute("mnuCd", mnuCd);
		
		EgovMap youtube = null;
		List<EgovMap> file = null;
		if(num !=""){
			int intNum = Integer.parseInt(num);
			youtube = hpgmngService.getYoutubeDtl(intNum);
			file = hpgmngService.getYoutubeFileDtl(intNum);
		}
		
		
		model.addAttribute("userId", loginVO.get("userId").toString());
		model.addAttribute("currentPageNo", vo.getCurrentPageNo());
		model.addAttribute("authCd", userAuth);
		model.addAttribute("youtube", youtube);
		model.addAttribute("file", file);
		
		return "hpgmng/youtube_dtl.main";
	}
	
	@RequestMapping(value = "/youtubeReg.do", method = RequestMethod.POST)
	public String youtubeReg(@RequestParam Map<String,Object> map, MultipartHttpServletRequest  request, ModelMap model) throws Exception{
		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
		
		EgovMap login = (EgovMap)request.getSession().getAttribute("LoginVO");
		String regId = (String)login.get("userId");
		String writer = (String)login.get("userNm");
		map.put("regId", regId);
		map.put("writer", writer);
		
		int youtubeNum = Integer.parseInt((String) map.get("num"));
		
		if(save.equals("S")) {
			hpgmngService.insertYoutube(map,request);	
		} else if(save.equals("U")) {
			map.put("fileNum",youtubeNum);
			hpgmngService.updateYoutube(map,request);
		} else if(save.equals("D")) {
			hpgmngService.deleteYoutube(youtubeNum);
		}
		
		return "redirect:/gnoincoundb/youtubeList.do?mnuCd=" + mnuCd;
	}
	
	
//
//	@RequestMapping(value = "/faqList.do")
//	public String faqList(FaqVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
//		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
//
//		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
//		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
//		
//		List<EgovMap> faqList = hpgmngService.getFaqList(vo);
//		model.addAttribute("faqList", faqList);
//		int totalPageCnt = hpgmngService.getFaqListTotCnt(vo);
//		model.addAttribute("totalPageCnt", totalPageCnt);
//		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
//		model.addAttribute("paginationInfo", paginationInfo);
//		model.addAttribute("vo", vo);
//		
//		return "support/faq_list.main";
//	}
//	
//	@RequestMapping(value = "/faqDtl.do")
//	public String faqDtl(FaqVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		// faq 상세보기
//		EgovMap faqDtl = null;
//		if(num !=""){
//			int intNum = Integer.parseInt(num);
//			faqDtl = supportService.getFaqDetail(intNum);
//		}
//		
//		model.addAttribute("detail", faqDtl);
//		
//		return "support/faq_dtl.main";
//	}
//	
//	@RequestMapping(value = "/faqReg.do")
//	public String faqReg(HttpServletRequest request, ModelMap model, FaqVO vo){
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		String regId = (String)map.get("userId");
//		String writer = (String)map.get("userNm");
//		vo.setWriter(writer);
//		vo.setRegId(regId);
//		
//		if(num != "") {
//			supportService.updateFaq(vo);
//		} else {
//			supportService.insertFaq(vo);			
//		}
//		return "redirect:/faqList.do?mnuCd=" + mnuCd;
//	}
//	

//	@RequestMapping(value = "/userManualList.do")
//	public String userManualList(UserManualVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
//		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
//
//		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
//		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
//		
//		List<EgovMap> usermList = supportService.getUserManualList(vo);
//		model.addAttribute("usermList", usermList);
//		int totalPageCnt = supportService.getUserManualListTotCnt(vo);
//		model.addAttribute("totalPageCnt", totalPageCnt);
//		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
//		model.addAttribute("paginationInfo", paginationInfo);
//		model.addAttribute("vo", vo);
//		
//		return "support/userManual_list.main";
//	}
//	
//	@RequestMapping(value = "/userManualDtl.do")
//	public String userManualDtl(UserManualVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		EgovMap usermDtl = null;
//		if(num !=""){
//			int intNum = Integer.parseInt(num);
//			usermDtl = supportService.getUserManualDetail(intNum);
//		}
//		
//		model.addAttribute("detail", usermDtl);
//		
//		return "support/userManual_dtl.main";
//	}
//	
//	@RequestMapping(value = "/userManualReg.do")
//	public String userManualReg(HttpServletRequest request, ModelMap model, UserManualVO vo){
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		String regId = (String)map.get("userId");
//		String writer = (String)map.get("userNm");
//		vo.setWriter(writer);
//		vo.setRegId(regId);
//		
//		if(save.equals("S")) {
//			supportService.insertUserManual(vo);		
//		} else if(save.equals("U")) {
//			supportService.updateUserManual(vo);
//		}
//		return "redirect:/userManualList.do?mnuCd=" + mnuCd;
//	}
//	
//	@RequestMapping(value = "/surveyMngList.do")
//	public String surveyMngList(SurveyMngVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
//		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
//
//		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
//		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
//	
//		List<EgovMap> survList = supportService.getSurveyMngList(vo);
//		model.addAttribute("survList", survList);
//		int totalPageCnt = supportService.getSurveyMngListTotCnt(vo);
//		model.addAttribute("totalPageCnt", totalPageCnt);
//		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
//		model.addAttribute("paginationInfo", paginationInfo);
//		model.addAttribute("vo", vo);
//		
//		return "support/survey_mng_list.main";
//	}
//	
//	@RequestMapping(value = "/surveyMngDtl.do")
//	public String surveyMngDtl(SurveyMngVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		String writer = (String)map.get("userNm");
//		
//		model.addAttribute("writer", writer);
//		
//		EgovMap survDtl = null;
//		if(num !=""){
//			int intNum = Integer.parseInt(num);
//			survDtl = supportService.getSurveyMngDetail(intNum);
//		}
//		model.addAttribute("detail", survDtl);
//		
//		return "support/survey_mng_dtl.main";
//	}
//	
//	@RequestMapping(value = "/signList.do")
//	public String signList(SignVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
//		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
//
//		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
//		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
//		
//		List<EgovMap> signList = supportService.getSignList(vo);
//		model.addAttribute("signList", signList);
//		int totalPageCnt = supportService.getSignListTotCnt(vo);
//		model.addAttribute("totalPageCnt", totalPageCnt);
//		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
//		model.addAttribute("paginationInfo", paginationInfo);
//		model.addAttribute("vo", vo);
//		
//		return "support/sign_list.main";
//	}
//	
//	@RequestMapping(value = "/signDtl.do")
//	public String signDtl(SignVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		model.addAttribute("loginVo", map);
//		
//		EgovMap signDtl = null;
//		if(num !=""){
//			int intNum = Integer.parseInt(num);
//			signDtl = supportService.getSignDetail(intNum);
//		}
//		
//		model.addAttribute("detail", signDtl);
//		
//		return "support/sign_dtl.main";
//	}
//	
//	@RequestMapping(value = "/signReg.do")
//	public String signReg(HttpServletRequest request, ModelMap model, SignVO vo){
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		String regId = (String)map.get("userId");
//		
//		vo.setRegId(regId);
//		vo.setUserId(regId);
//		vo.setCnsGb((String)map.get("cnsGb"));
//		vo.setZoneGb((String)map.get("zoneGb"));
//		vo.setLocalGb((String)map.get("localGb"));
//		vo.setCenterGb((String)map.get("centerGb"));
//		
//		String sign = vo.getFilePath();
//		
//		String fullpath = "";
//		String[] strParts = sign.split(",");
//		String rstStrImg = strParts[1];
//		String fileNm = UUID.randomUUID() + "_img.png";
//		String folder = "D:/proj/ggsc/src/main/webapp/images/sign/";
//		
//		BufferedImage image = null;
//		byte[] byteImg;
//		
//		byteImg = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(rstStrImg);
//		ByteArrayInputStream bis = new ByteArrayInputStream(byteImg);
//		
//		try {
//			image = ImageIO.read(bis);
//			bis.close();
//			fullpath = folder + fileNm;
//			File f = new File(folder);
//			if(!f.isDirectory()){
//				f.mkdir();
//			}
//			File outputFile = new File(fullpath);
//			if(outputFile.exists()){
//				outputFile.delete();
//			}
//			ImageIO.write(image, "png", outputFile);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		vo.setFilePath("/sign/"+fileNm);
//		
//		if(save.equals("S")) {
//			supportService.insertSign(vo);		
//		} else if(save.equals("U")) {
//			supportService.updateSign(vo);
//		}
//		return "redirect:/signList.do?mnuCd=" + mnuCd;
//	}
//	
//	@RequestMapping(value = "/mailList.do")
//	public String mailList(MailVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
//		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
//
//		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
//		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
//		
////		List<EgovMap> mailList = supportService.getMailList(vo);
////		model.addAttribute("mailList", mailList);
////		int totalPageCnt = supportService.getMailListTotCnt(vo);
////		model.addAttribute("totalPageCnt", totalPageCnt);
////		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
//		model.addAttribute("paginationInfo", paginationInfo);
//		model.addAttribute("vo", vo);
//		
//		return "support/mail_list.main";
//	}
//	
//	@RequestMapping(value = "/mailDtl.do")
//	public String mailDtl(MailVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		model.addAttribute("loginVo", map);
//		
//		String curDate = new SimpleDateFormat( "yyyy-MM-dd").format(System.currentTimeMillis());
//		model.addAttribute("curDate", curDate);
//		
//		EgovMap mailDtl = null;
//		if(num !=""){
//			int intNum = Integer.parseInt(num);
////			mailDtl = supportService.getMailDetail(intNum);
//		}
//		
//		model.addAttribute("detail", mailDtl);
//		
//		return "support/mail_dtl.main";
//	}
//	
//	@RequestMapping(value = "/mailReg.do")
//	public String mailReg(HttpServletRequest request, ModelMap model, MailVO vo){
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		String regId = (String)map.get("userId");
//		vo.setRegId(regId);
//		
//		supportService.insertMail(vo);		
//
//		return "redirect:/mailList.do?mnuCd=" + mnuCd;
//	}
//	
//	@RequestMapping(value = "/chtrList.do")
//	public String chtrList(ChtrVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(vo.getCurrentPageNo()); // 현재 페이지 번호
//		paginationInfo.setRecordCountPerPage(10); // 한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(10); // 페이징 리스트의 사이즈
//
//		vo.setFirstIndex((vo.getCurrentPageNo() - 1) * 10);
//		vo.setLastIndex((vo.getCurrentPageNo()) * 10);
//		
////		List<EgovMap> chtrList = supportService.getChtrList(vo);
////		model.addAttribute("chtrList", chtrList);
////		int totalPageCnt = supportService.getChtrListTotCnt(vo);
////		model.addAttribute("totalPageCnt", totalPageCnt);
////		paginationInfo.setTotalRecordCount(totalPageCnt); // 전체 게시물 건 수
//		model.addAttribute("paginationInfo", paginationInfo);
//		model.addAttribute("vo", vo);
//		
//		return "support/chtr_list.main";
//	}
//	
//	@RequestMapping(value = "/chtrDtl.do")
//	public String chtrDtl(ChtrVO vo, HttpServletRequest request, ModelMap model){	
//		if(request.getParameter("page") != null){
//			model.addAttribute("page", request.getParameter("page"));
//		}
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String num = request.getParameter("num") == null ? "" : request.getParameter("num");
//		model.addAttribute("mnuCd", mnuCd);
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		model.addAttribute("loginVo", map);
//		
//		EgovMap chtrDtl = null;
//		if(num !=""){
//			int intNum = Integer.parseInt(num);
////			chtrDtl = supportService.getChtrDetail(intNum);
//		}
//		
//		model.addAttribute("detail", chtrDtl);
//		
//		return "support/chtr_dtl.main";
//	}
//	
//	@RequestMapping(value = "/chtrReg.do")
//	public String chtrReg(HttpServletRequest request, ModelMap model, ChtrVO vo){
//		String mnuCd = request.getParameter("mnuCd") == null ? "" : request.getParameter("mnuCd");
//		String save = request.getParameter("save") == null ? "" : request.getParameter("save");
//		
//		EgovMap map = (EgovMap)request.getSession().getAttribute("LoginVO");
//		String regId = (String)map.get("userId");
//		
//		vo.setRegId(regId);
//		
////		supportService.insertMail(vo);		
//
//		return "redirect:/mailList.do?mnuCd=" + mnuCd;
//	}
}
