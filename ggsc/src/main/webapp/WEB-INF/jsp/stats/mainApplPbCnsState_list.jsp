<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="/gnoincoundb/js/util/paging.js"></script>

<script type="text/javascript">
	$(function(){
		list();
	});
	function fn_excelDown(){
		document.location.href = "/gnoincoundb/center_ExcelDown.do";
	}

	function list() {
		var token = $("meta[name='_csrf']").attr("th:content");
		var header = $("meta[name='_csrf_header']").attr("th:content");
		
		$.ajax({
			type : "POST",
			url : "/gnoincoundb/mainApplPbCnsStateList_json.do",
			data : $("#searchForm").serialize(),
			dataType : "json",
			beforeSend : function(xhr){
				xhr.setRequestHeader(header, token);
			}
		}).done(function(json){
			$("#tbl_item_box *").remove();
			if(json.list.length==0){
				$("#tbl_item_box").append('<tr class="item"><td colspan=10>등록된 정보가 없습니다.</td></tr>'); 
			} 
			for(var i=0;i<json.list.length;i++){
				var obj = json.list[i];
				var appl = json.map4[i];
				for(var c=0; c<7; c++){
					if(appl[c] == undefined) {
						appl[c] = "";
					}
				}
				$("#tbl_item_box").append(
					'<tr class="item">' +
					'<th rowspan=\'2\'>' + obj.mclassNm + '</th>' +
					'<td>소계</td>' +
					'<td>' + appl[0] + '</td>' +
					'<td>' + appl[1] + '</td>' +
					'<td>' + appl[2] + '</td>' +
					'<td>' + appl[3] + '</td>' +
					'<td>' + appl[4] + '</td>' +
					'<td>' + appl[5] + '</td>' +
					'<td>' + appl[6] + '</td>' +
					'<td>상세내용 없음</td>' +
					'</tr>' +
					'<tr>' + 
					'<td>' + obj.fieldNameAll + '</td>' +
					'<td>' + obj.fieldName01 + '</td>' +
					'<td>' + obj.fieldName02 + '</td>' +
					'<td>' + obj.fieldName03 + '</td>' +
					'<td>' + obj.fieldName04 + '</td>' +
					'<td>' + obj.fieldName05 + '</td>' +
					'<td>' + obj.fieldName06 + '</td>' +
					'<td>' + obj.fieldName07 + '</td>' +
					'<td>' + obj.fieldNameNon + '</td>' +
					'</tr>'
				);					
			}
		}).fail(function(e){
			alert("서버와 통신 오류입니다.");
		});
	}
</script>
<style>
	.search-group select{width:150px;}
	.label {margin-right:40px;}
	.label2 {display:inline-block;width:275px;color:#333;padding-right:5px;margin-bottom:2px;letter-spacing:-0.5px;text-align:right;}
	
</style>
<section id="content">

	<h2 class="h2-title"><i class="fa fa-check-square"></i>개인상담 주호소별 상담현황</h2>
		
		<div class="box-style1 x-scroll-auto" >
				<!-- 검색영역 -->
		<form name="searchForm" id="searchForm" action="/bbsList.do" method="post" onsubmit="return false">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<input type="hidden" id="currentPageNo" name="currentPageNo" value="1" />
			<input type="hidden" id="mnuCd" name="mnuCd" value="${mnuCd }" />
			<div class="search-box">
				<div class="search-group" style="margin-left:70px;">
					<span class="label"><label>상담구분</label></span>
					<span class="label2">
						 <select name=schCnsGb style="width:275px;">
						 	<option value="">전체</option>
							<c:forEach items="${cnsGbList }" var="result">
								<option value="${result.odr }">${result.mclassNm }</option>
							</c:forEach>
						</select>
					</span>
					<!-- <span class="form"><input type="text" name="schText" id="schText" value="" /></span> -->
				</div>
				<div class="search-group" style="margin-left:70px;">
					<span class="label"><label>센터구분</label></span>
					<span class="label2">
						<select name="schCenterGb" style="width:275px;">
							<option value="">전체</option>
							<c:forEach items="${cnsCenterList}" var="list">
								<c:choose>
									<c:when test="${authCd > 1}">
										<c:if test="${ list.num eq vo.schCenterGb }" >
											<option value="${list.num}" selected >${list.centerGb}</option>
										</c:if>
									</c:when>
									<c:when test="${authCd <= 1 }">								
										<option value="${list.num}" >${list.centerGb}</option>
									</c:when>
								</c:choose>
							</c:forEach>
						</select>
					</span>
				</div>
				<br>
				<div class="btn" style="padding-bottom:15px;padding-right:120px;">
					<button type="button" class="btn-search" id="searchBtn" onclick="javascript:list(1);">
						<i class="fa fa-search"></i>검색
					</button>
				</div>
				<div class="search-group" style="margin-left:70px;">
					<span class="label"><label>년도</label></span>
					<span class="label2">
						<input type="text" name="schYearGb" value="" style="width: 275px;" maxlength="4" onlyNumber />
					</span>
				</div>
				<div class="search-group" style="margin-left:70px;">
					<span class="label"><label>월</label></span>
					<span class="label2">
						<select name="schMonthGb" style="width:275px;">
							<option value="">전체</option>
							<c:forEach begin="1" end="12" step="1" var="month">
							<%--<fmt:formatNumber value="${month}" pattern="00" var="month"/>--%>
								<option value="${month}">${month}</option>
							</c:forEach>
						</select>
					</span>
				</div>
			</div>
		</form>
			<h3 class="h3-title" style="margin-top: 15px;">개인상담 주호소별 상세 상담현황</h3>
			<table class="table-style1">
                <colgroup>
                    <col style="width: 6%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                    <col style="width: 5%;" />
                </colgroup>
                <tbody id='tbl_item_box'>
                </tbody>
            </table>
            
		</div>
		<!-- end -->

	</section>

</html>
