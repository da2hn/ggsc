
<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


	
    <script src="/gnoincoundb/js/html2canvas.min.js" ></script>
    <script src="/gnoincoundb/js/jspdf.min.js" ></script>

<script type="text/javascript">
	$(document).ready(function() {
		
	    //날짜+특수문자해체
		var nowTime = $("#nowTime").val();
		var dbInsTm = $("#dbInsTm").val();
		var ogdbInsTm = dbInsTm;
		parseInt(ogdbInsTm);
		var authCd = $("#authCd").val();
		nowTime = nowTime.replace(/-/gi, "");
		dbInsTm = dbInsTm.replace(/-/gi, "");
		
		// 각 월 일수 로직
		var Md = nowTime.substring(4,6);
		if(Md==01||Md==03||Md==05||Md==07||Md==08||Md==10||Md==12) {
			var MonthDay = 29;
		} else if(Md==04||Md==06||Md==09||Md==11) {
			var MonthDay = 30;
		} else {
			var MonthDay = 31;
		}
		
		parseInt(nowTime);
		parseInt(dbInsTm);
		var onelastMonth = nowTime.substring(0,6).concat("01");
		var onelastDay = '"-10Y"';
		var maxDay = '"+10Y"';
		var hideBtn = new Boolean(false);
		var dbInstDate = new Date(ogdbInsTm);
		var dbInsertYear = dbInstDate.getFullYear();			
		if(authCd >= 2) {
			var onelastDay = '"-'+(nowTime.substring(6,8)-1)+'D"';
			var maxDay = '"+1Y"';
			
			//현재 날짜가 7일 후일 경우
		    if(nowTime.substring(6,8) > 07) {			
		    	//등록된 데이터가 이번달이 아닌경우
				if(nowTime.substring(0,6).concat("01") > dbInsTm) {		
					if(dbInsTm != "") { hideBtn = true; }
				}
		    	
			} else if(nowTime.substring(6,8) <= 07) {	
				var onelastDay = '"-'+(parseInt(nowTime.substring(6,8))+MonthDay)+'D"';
				//등록된 데이터가 지난달 1일보다 낮을경우
				if(onelastMonth-100 > dbInsTm) {	
					if(dbInsTm != "") { hideBtn = true; }				
				} 
				
			} if (hideBtn==true) {
				$('#uBtn').hide();
				$('#dBtn').hide();
				$('.btn-basic[onClick*=fn_fileDelete]').hide();
				$('#showdisable').show();
				$('#datepicker').attr('id','datepicker_none');
			}
		}	
			// 현재 날짜 객체 생성
		var currentDate = new Date();

		// 비교할 날짜 객체 생성 (2024년 6월 30일)
		var targetDate = new Date("2024-06-30");

		if(authCd == 1){
			$('#uBtn').show();
			$('#showdisable').hide();
		}else if((currentDate < targetDate) && authCd == 3 && dbInsertYear == 2024){
			$('#uBtn').show();
			$('#showdisable').hide();
			
		}
		
 		// datepicker
		  $( function() {
				$( "#datepicker" ).datepicker({
				  showOn: "button",
				  buttonImage: "/gnoincoundb/images/calendar.gif",
				  buttonImageOnly: true,
				  buttonText: "Select date",
				  showMonthAfterYear: true ,
				  dateFormat: 'yy.mm.dd',
				  monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
				  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
				  minDate: onelastDay,
				  maxDate: maxDay,
				  changeYear: true,
				  changeMonth: true,
				  beforeShowDay: function(date){
					  var day = date.getDay();
					  if(day != 1 && day != 2 && day != 3 && day != 4 && day != 5 && day != 0){			//토요일
						  return [true, "Highlighted_sat", ''];
					  }else if(day != 1 && day != 2 && day != 3 && day != 4 && day != 5 && day != 6){	//일요일
						  return [true, "Highlighted_sun", ''];
					  }
					  return [true, '', ''];
				  }
				});
		  } ); 
		
		dataReady();
	});

	function dataReady() {
		var resultStr = ["${result.prAtvyRcpt}"];
		var resultName = ["prAtvyRcpt"];
		
		for(let i=0; i<resultName.length; i++){
			var str = resultStr[i];
			var name = resultName[i];
			if(str!=null && str!="") {
				fn_checked(str, name);	
			}
		}
	}
		
	function fn_checked(str, name){
		var arr = str.split(",");
		for(var i in arr){
			$("input[name=" + name + "][value=" + arr[i] + "]").prop("checked", true);
		}
	}
	
	function fn_save(save){
		
		var frm = document.frm;
		
		if(frm.centerGb.value.length == 0 ){
			alert('[센터구분]을 선택해주세요.');
			return false;
		}else if(frm.promotion.value.length == 0 ){
			alert('[홍보]를 선택해주세요.');
			return false;
		}else if(frm.prAtvyDt.value.length < 8 ){
			alert('[일시]를 선택해주세요.');
			return false;
		}else if(frm.prSttHour.value.length < 1 ){
			alert('[홍보 시간]을 입력해주세요.');
			frm.prSttHour.focus();
			return false;
		}else if(frm.prSttMin.value.length < 1 ){
			alert('[홍보 시간]을 입력해주세요.');
			frm.prSttMin.focus();
			return false;
		}else if(frm.prEndHour.value.length < 1 ){
			alert('[홍보 시간]을 입력해주세요.');
			frm.prEndHour.focus();
			return false;
		}else if(frm.prEndMin.value.length < 1 ){
			alert('[홍보 시간]을 입력해주세요.');
			frm.prEndMin.focus();
			return false;
		}else if(frm.prAtvyPtct.value.length < 1 ){
			alert('[참여자 인원]을 입력해주세요.');
			return false;
		}else if($("input[name='prAtvyRcpt']:checked").length == 0){
			alert('[대상자]를 선택해주세요.');
			return false;
		}else if(frm.prAtvyMan.value.length < 1 ){
			alert('[대상 인원]을 입력해주세요.');
			return false;
		}else if(frm.prAtvyCntn.value.length > 254){
			alert('[내용]을 255자 미만으로 입력해주세요.');
			return false;
		}		
		
		var msg = "";
		if(save=="I"){
			msg = "등록 하시겠습니까?";
		}else if(save == "U"){
			msg = "수정 하시겠습니까?";	
		}else if(save == "D"){
			msg = "삭제 하시겠습니까?";	
		}
		
		// validation
		$("#save").val(save);
		if(confirm(msg)){
			document.frm.action = "/gnoincoundb/prAtvyRptSave.do";
		   	document.frm.submit();
		}
	}
	
	function fn_list(mnuCd){
		document.location.href = "/gnoincoundb/prAtvyRptList.do?mnuCd=" + mnuCd;
		/* document.frm.action = "/prAtvyRptList.do";
	   	document.frm.submit(); */
	}
	
	
	function fn_down(fileNm, sysFileNm, filePath){
		$("#fileNm").val(fileNm);
		$("#sysFileNm").val(sysFileNm);
		$("#filePath").val(filePath);
		document.downForm.action = "/gnoincoundb/fileDown.do";
	   	document.downForm.submit();
	}
	
	function fn_fileDelete(idx){
		if(confirm('정말 삭제하시겠습니까?')){
			
		const frm = document.frm;
		frm.fDel.value = 'Y';
		var url = "/gnoincoundb/prAtvyRptFileDel.do";
			
		var param = $("#frm").serialize();
		var token = $("meta[name='_csrf']").attr("th:content");
		var header = $("meta[name='_csrf_header']").attr("th:content");
		
			$.ajax({
				type : "POST",
				url : url,
				data : param,
				dataType : "json",
				beforeSend : function(xhr){
					xhr.setRequestHeader(header, token);
				},
				success : function(json) {
					document.location.reload();
				},
				error : function(e) {
					alert("서버와 통신 오류입니다.");
				}
			});
		}
	}
</script>

<section id="content">
	<h2 class="h2-title"><i class="fa fa-check-square"></i>홍보활동보고서 등록/수정</h2>
		
		<div class="box-style1 x-scroll-auto" >
		
			<form name="downForm" id="downForm" method="post">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="hidden" id="fileNm" name="fileNm" value="" />
				<input type="hidden" id="sysFileNm" name="sysFileNm" value="" />
				<input type="hidden" id="filePath" name="filePath" value="" />
			</form>
			
			<form id="frm" name="frm" method="post" encType="multipart/form-data">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="hidden" id="mnuCd" name="mnuCd" value="${mnuCd }" />
				<input type="hidden" id="prAtvyNo" name="prAtvyNo" value="${result.prAtvyNo }" />
				<input type="hidden" id="save" name="save" />
				<input type='hidden' name='cnsGb' value='0' />
				<input type='hidden' name='localGb' value='0' />
				<input type='hidden' name='fDel' value='N' />
				<input type='hidden' name='delYn' value='N' />
				<input type="hidden" id="dbInsTm" value="${fn:substring(result.dbInsTm, 0, 10) }" />
				<input type="hidden" id="nowTime" value="${nowTime }" />
				<input type="hidden" id="authCd" value="${authCd }" />			
				
				<table class="table-write">
					<colgroup>
						<col width="25%"></col>
						<col width="75%"></col> 
					</colgroup>
					<tr>
						<th>작성자</th>
						<td>
							<c:if test="${result.inputNm == null}">
								<!-- 상담사명 -->
								${map.userNm }
							</c:if>
							<c:if test="${result.inputNm != null}">
								${result.inputNm }
							</c:if>
						</td>
					</tr>
					<tr>
						<th>센터구분</th>
						<td>
							<select class="wd200" id="centerGb" name="centerGb">
								<option value="">선택하세요</option>
								<c:forEach items="${cnsCenterList }" var="list">
									<c:choose>
									<c:when test="${authCd > 1 }">
										<c:if test="${ list.num eq centerGb }" >
											<option value="${list.num }" <c:if test="${ list.num eq centerGb }">selected</c:if> >${list.centerGb }</option>
										</c:if>
									</c:when>
									<c:when test="${authCd <= 1 }">	
										<option value="${list.num }" <c:if test="${ list.num eq result.centerGb }">selected</c:if> >${list.centerGb }</option>		
									</c:when>
									</c:choose>
								</c:forEach>
							</select> 
						</td>
					</tr>
					<tr>
						<th>홍보</th>
						<td>
							<select class="wd200" id="promotion" name="promotion">
								<option value=''>선택하세요</option>
								<c:forEach items="${promotionList }" var="list">
									<option value="${list.odr }" <c:if test="${list.odr == result.promotion}">selected</c:if>>${list.mclassNm }</option>
								</c:forEach>
							</select> 
						</td>
					</tr>
					<tr>
						<th>일시</th>
						<td>
							<span class="form"><input type="text" class="wd100" id="datepicker" name="prAtvyDt" value="${result.prAtvyDt }" readOnly/></span>
							<input type="text" class="wd50 mg-l25" maxlength="2" id="prSttHour" name="prSttHour" value="${result.prSttHour }" onlyNumber/>시
							<input type="text" class="wd50" maxlength="2" id="prSttMin" name="prSttMin" value="${result.prSttMin }" onlyNumber/>분 ~
							<input type="text" class="wd50" maxlength="2" id="prEndHour" name="prEndHour" value="${result.prEndHour }" onlyNumber/>시
							<input type="text" class="wd50" maxlength="2" id="prEndMin" name="prEndMin" value="${result.prEndMin }" onlyNumber/>분
						</td>
					</tr>
<%-- 					<tr>
						<th>홍보활동분야</th>
						<td>
							<c:forEach items="${cnsPrAreaList }" var="list">
								<input type="checkbox" id="prAtvyArea${list.odr}" name="prAtvyArea" value="${list.odr}" <c:if test="${fn:contains(result.prAtvyArea, list.odr)}">checked</c:if> /><label for="prAtvyArea${list.odr}">&nbsp;${list.mclassNm}</label>
							</c:forEach>
						</td>
					</tr> --%>
					<tr>
						<th>참여자구분</th>
						<td>
							<c:forEach items="${prAtvyPtctList }" var="list">
								<input type="radio" id="prAtvyPtctGb${list.odr}" name="prAtvyPtctGb" value="${list.odr}" <c:if test="${list.odr == result.prAtvyPtctGb}">checked</c:if> /><label for="prAtvyPtctGb${list.odr}">${list.mclassNm}</label>
							</c:forEach>
							
						</td>
					</tr>
					<tr>
						<th>참여자</th>
						<td>
							<input type="text" class="wd50" id="prAtvyPtct"  name="prAtvyPtct" value="${result.prAtvyPtct }" onlyNumber maxLength=3 />명
						</td>
					</tr>
					<tr>
						<th>홍보 대상자</th>
						<td>
							<c:forEach items="${cnsEdu4List }" var="list">
								<input type="checkbox" id="prAtvyRcpt${list.odr}" name="prAtvyRcpt" value="${list.odr}" <c:if test="${fn:contains(result.prAtvyRcpt, list.odr)}">checked</c:if>/><label for="prAtvyRcpt${list.odr}">&nbsp;${list.mclassNm}</label>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th>대상인원</th>
						<td>
							<input type="text" class="wd50" id="prAtvyMan" name="prAtvyMan" value="${result.prAtvyMan }" onlyNumber maxLength=3 />명 (개)
						</td>
					</tr>
					<tr>
						<th>장소</th>
						<td>
							<input type="text" id="prAtvyPlace" name="prAtvyPlace" value="${result.prAtvyPlace }" />
						</td>
					</tr>
					<tr>
						<th>심리검사 추가</th>
						<td>
							<input type="text" id="psycExam" name="psycExam" value="${result.psycExam }" />
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<textarea id="prAtvyCntn" name="prAtvyCntn" rows="15" cols="100">${result.prAtvyCntn }</textarea>
						</td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td>
							<c:if test="${result.fileNm == null}">
								<input type='file' name='file' id='file' style='margin:10px 0; height:auto'/>
							</c:if>
							<c:if test="${result.fileNm != null}">
								${result.fileNm}
								<button type='button' onClick="fn_down('${result.fileNm}','${result.sysFileNm}','${result.filePath}')" class="btn-basic">다운로드</button>
								<button type='button' onClick="fn_fileDelete(${result.prAtvyNo})" class="btn-basic" style='background-color: red'>삭제</button>
							</c:if> 
						</td>
					</tr>
				</table>
				<div class="btn" style="float: right; margin: 0;">
					<button type="button" class="btn-basic" onClick="javascript:fn_list('${mnuCd}');"style="background-color:green;color:white;">목록</button>
					<c:if test="${ result != null && (authCd <= 3 || ( authCd > 3 && userId == result.cnsrId )) }">
							<button type="button" id="uBtn" class="btn-basic" onClick="javascript:fn_save('U');" style="background-color: green;color:white;">수정</button>	
					</c:if>
					<c:if test="${result == null}">
						<button type="button" id="sBtn" class="btn-basic" onClick="javascript:fn_save('I');" style="background-color: green;color:white;">저장</button>
					</c:if>
					<c:if test="${ result != null && (authCd <= 1 || ( authCd > 1 && userId == result.cnsrId )) }">
						<button type="button" id="dBtn" class="btn-basic"  onClick="javascript:fn_save('D');">삭제</button>
					</c:if>
					<button type="button" class="btn-basic" id="showdisable" style="background-color: red;color:white; display:none;">수정불가</button>
					<c:if test="${result != null}">
						<button type="button" id="pdfBtn" class="btn-basic btn-primary" onclick="javascript:fn_pdfDownload(41, '${result.prAtvyNo }')">PDF 다운</button>
					</c:if>
				</div>	
			</form>
		</div>
		<!-- end -->
	</section>
</html>

