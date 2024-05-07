<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="/gnoincoundb/css/style.css">
<link href="/gnoincoundb/editor/css/bootstrap_3.3.5.css" rel="stylesheet">
<script src="/gnoincoundb/js/jquery/jquery-1.11.2.min.js"></script>
<script src="/gnoincoundb/js/jquery/jquery.form.js"></script>
<script src="/gnoincoundb/js/jquery/jquery-migrate-1.4.1.js"></script>
<script src="/gnoincoundb/js/jquery/jquery-accordion-menu.js" type="text/javascript"></script>
<script src="/gnoincoundb/js/jquery/jquery-ui.min.js" type="text/javascript"></script>
<script src="/gnoincoundb/js/common.js"></script>
<script src="/gnoincoundb/js/custom.js"></script> 
<script src="/gnoincoundb/js/util/paging.js"></script>
<script src="/gnoincoundb/js/html2canvas.min.js"></script>
<script src="/gnoincoundb/js/jspdf.min.js"></script>
    <style>
		ul.tabs{
		margin : 30px 0 30px 0;
		padding: 0px;
		list-style: none;
		border-bottom-style: solid;
		border-width: thin;
		}
		ul.tabs li{
			background: none;
			color: #222;
			display: inline-block;
			padding: 10px 15px;
			cursor: pointer;
		}
	</style>
<script type="text/javascript">
	$(document).ready(function() {
		var page = "${page}";
		if(page == ""){
			page = 1;
		}
		
		var schCnsGb = "${vo.schCnsGb}";
		var schCenterGb = "${vo.schCenterGb}";
		$("select[name=schCnsGb]").val(schCnsGb);
		$("select[name=schCenterGb]").val(schCenterGb);
		
	});
	

	function fn_popup(type, caseNo){
		var url = "/gnoincoundb/cnsAcceptDtl.do?type="+type+"&caseNo="+caseNo;
		var name = "신청자정보";
		var option = "width = 530, height = 750, top = 50, left = 500, location = yes";
		window.open(url, name, option);
	}
	
	function fn_popupDoc(psyNum, gIdx, dtlIdx, rnum){
		
		if(psyNum == "101") {
			alert("사용하지 않는 심리검사지입니다.");
			return false;
		}
		
		var mnuCd = "${mnuCd}";
		var url = "/gnoincoundb/psyCnsDoc.do?mnuCd="+mnuCd+"&psyNum="+psyNum+"&gIdx="+gIdx+"&dtlIdx="+dtlIdx + "&rnum=" + rnum;
		var name = "상담사전사후기록지";
		var option = "width = 1100, height = 750, top = 50, left = 200, location = yes";
		window.open(url, name, option);
	}
	
	function fn_search(mnuCd) {
		document.searchForm.action = "/gnoincoundb/psyCnsList.do?mnuCd="+mnuCd;
	   	document.searchForm.submit(); 
	}
	
	function fn_detail(caseNo){
		var mnuCd = $("#mnuCd").val();
		document.location.href = "/gnoincoundb/ealyCnsDocDtl.do?caseNo="+caseNo+"&mnuCd="+mnuCd;
	}
	
	function list(curPage) {
		
		$("#currentPageNo").val(curPage);
		document.searchForm.action = "/gnoincoundb/psyCnsList.do";
	   	document.searchForm.submit();
	}
	
	function fn_list() {
		var mnuCd = $("#mnuCd").val();
		document.location.href = "/gnoincoundb/psyCnsList.do?mnuCd="+mnuCd;
	}
	
	
</script>

<section id="content">

	<h2 class="h2-title"><i class="fa fa-check-square"></i>심리검사</h2>
		
		<div class="box-style1 x-scroll-auto" >
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<h3 class="h3-title" style="margin-top:10px;"><i class="fa fa-star"></i>심리검사 <span class='caseNo'></span></h3>
			<table class="table-style1" style="margin-bottom: 5px;">
				<colgroup> 
					<col width="5%"></col>
					<col width="7%"></col> 
					<col width="7%"></col>
					<col width="7%"></col>
					<col width="7%"></col>
				</colgroup>
							
				<thead>
					<tr>
						<th scope="col">순번</th>
						<th scope="col">구분명</th>
						<th scope="col">구분</th>
						<th scope="col">상담자</th> 
						<th scope="col">PDF</th>
					</tr>
				</thead>
				<tbody id="tby1">
					<c:if test="${psyCnsList.size() == 0 }">
						<tr>
							<td colspan="12">정보가 없습니다.</td>
						</tr>
					</c:if>
					<c:if test="${psyCnsList.size() > 0 }">
						<c:forEach items="${psyCnsList }" var="result">
							<tr>
								<td onclick="javascript:fn_popupDoc('${result.psyNum}', '${result.gIdx}', '${result.dtlIdx}','${result.psyOrder}');">${result.psyOrder }</td>
								<td onclick="javascript:fn_popupDoc('${result.psyNum}', '${result.gIdx}', '${result.dtlIdx}','${result.psyOrder}');">${result.psyName }</td>
								<td onclick="javascript:fn_popupDoc('${result.psyNum}', '${result.gIdx}', '${result.dtlIdx}','${result.psyOrder}');">${result.psyGubun }</td>
								<c:choose>
								<c:when test="${empty result.psyCustomer}"><td></td></c:when>
								<c:when test="${not empty result.psyCustomer}"><td>${result.psyCustomer}</td></c:when>
								</c:choose>
								<c:choose>
								<c:when test="${empty result.psyCustomer}"><td></td></c:when>
								<c:when test="${not empty result.psyCustomer}"><td><button type="button" id="pdfBtn" class="btn-basic btn-primary" onclick="javascript:fn_pdfDownload('${result.psyOrder}','${result.dtlIdx}');">PDF 다운</button></td></c:when>
								</c:choose>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
		<!-- end -->
	</section>
</html>

