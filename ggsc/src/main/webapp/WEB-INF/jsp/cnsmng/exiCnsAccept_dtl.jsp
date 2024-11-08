<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="_csrf" th:content="${_csrf.token}"/>
<meta name="_csrf_header" th:content="${_csrf.headerName}"/>
<title>신청자 정보 팝업</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="/gnoincoundb/css/style.css">
<script src="/gnoincoundb/js/jquery-1.11.2.min.js" ></script>
<script src="/gnoincoundb/js/custom.js" ></script>
<script type="text/javascript">

	$(document).ready(function() {
		if("${authCd}" > 1) {
			var centerGb = "${vo.schCenterGb}";
			$("select[name=centerGb]").val(centerGb);
			$("select[name=sigunGb]").val("${loginVo.sigunCd}");
			$("select[name=zoneGb]").val("${loginVo.zoneGb}");
		}
		var type = "${type}";
		
	    //날짜
		var nowTime = $("#nowTime").val();
		var dbInsTm = $("#dbInsTm").val();
		var authCd = $("#authCd").val();
		nowTime = nowTime.replace(/-/gi, "");
		dbInsTm = dbInsTm.replace(/-/gi, "");
		parseInt(nowTime);
		parseInt(dbInsTm);
		var onelastMonth = nowTime.substring(0,6).concat("01");
		
		//check
	 /* console.log(dbInsTm);
		console.log(nowTime.substring(6,8));
		console.log(nowTime.substring(0,6));
		console.log(nowTime.substring(0,6).concat("01"));
		console.log(onelastMonth-100); */
		
			//등록된 데이터가 지난달 1일보다 낮을경우
		/*if(onelastMonth-100 > dbInsTm && authCd >= 2) {
			$('.btn-basic[onClick*=fn_save]').hide();
			$('#showdisable').show();
			
			//현재 날짜가 7일 후일 경우
		} else if(nowTime.substring(6,8) > 07 && authCd >= 2) {
			//등록된 데이터가 요번달이 아닌 경우
			if(nowTime.substring(0,6).concat("01") > dbInsTm) {
				$('.btn-basic[onClick*=fn_save]').hide();
				$('#showdisable').show();
			}
		}*/
		
	});

	function fn_save(type){
		var url = "";
		
		// 공백제거
		$("#tel").val($("#tel").val().replace(/\D/g,""));
		$("#birthDt").val($("#birthDt").val().replace(/\D/g,""));
		var tel = $("#tel").val();
		var mobile = $("#mobile").val();
		
		var userId = $("#userId").val(); // 아이디
		var pw = $("#pw").val(); // 비밀번호
		
		var centerGb = $("#centerGb").val();
		
		if(centerGb == "" || centerGb == null){
			alert("센터구분을 선택해주세요.");
			$("#centerGb").focus();
			return;
		}
		
		if($('input:radio[name="majorApplCd"]').is(':checked')==false){
			alert("주호소문제를 선택해주세요.");
			return;
		}
	
		
		if($("#userNm").val()==""){
			alert("이름를 입력해주세요.");
			$("#userNm").focus();
			return;
		}
		
		if($('input:radio[name="gender"]').is(':checked')==false){
			alert("성별을 선택해주세요.");
			return;
		}
		
		if($("#birthDt").val()==""){
			alert("생년월일을 입력해주세요.");
			$("#birthDt").focus();
			return;
		}
		
		if($("#birthDt").val().length != 8){
			alert("생년월일을 8자리로 적어주세요.");
			$("#birthDt").focus();
			return;
		}
		
		if(mobile == ""){
			alert("핸드폰번호를 입력해주세요.");
			$("#mobile").focus();
			return;
		}
		
		/*if($("#email").val()==""){
			alert("이메일을 입력해주세요.");
			$("#email").focus();
			return;
		}
		
		if($("#tel").val()==""){
			alert("전화번호를 입력해주세요.");
			$("#tel").focus();
			return;
		}
		
		if($("#mobile").val()==""){
			alert("핸드폰을 입력해주세요.");
			$("#mobile").focus();
			return;
		}
		*/
		if($("#addr").val()==""){
			alert("주소를 입력해주세요.");
			$("#addr").focus();
			return;
		}
		
		if($('input:radio[name="cnsHistYn"]').is(':checked')==false){
			alert("상담이력을 선택해주세요.");
			return;
		}
		
		
		
		if(type == "D"){
			if(confirm("해당 내담자의 아이디와 모든 내용이 삭제됩니다. 삭제하시겠습니까? ")){
				url = "/gnoincoundb/exiCnsAcptDel_ajax.do";
			}
		}else if(type == "U"){
			url = "/gnoincoundb/exiCnsAcptUpd_ajax.do";
		}
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
				if(json.msg){
					alert(json.msg);
				}else{
					if(type == "D") {
						alert("상담접수가 삭제완료 되었습니다.");
					} else if(type == "U") {
						alert("상담접수가 수정 되었습니다.");
					}
					
					opener.parent.location.reload();
					window.close();	
				}
				
			},
			error : function(e) {
				if(type == "D"){
					return false;
				} else {
					alert("서버와 통신 오류입니다.");
				}
			}
		});
	}
	
	
	function fn_clear(){
		$("input[type=radio]").removeAttr("checked");
		$("input[type!=radio]").val("");
	}
	
</script>
</head>
<body>
	<h2 class="h2-title mg-t20 mg-l25">
		<i class="fa fa-circle"></i>
		신청자 정보
	</h2>
	<form id="frm" action="/cnsAcptReg_ajax.do" method="post">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input type="hidden" id="dbInsTm" value="${fn:substring(result.dbInsTm, 0, 10) }" />
		<input type="hidden" id="nowTime" value="${nowTime }" />
		<input type="hidden" id="authCd" value="${authCd }" />
		
		<c:if test="${type == 'D' }">
			<input type="hidden" id="caseNo" name="caseNo" value="${result.caseNo }">
		</c:if>
		<table class="table-write wd500 h600 mg-all10">
			<colgroup>
				<col width="20%"></col>
				<col width="80%"></col>
			</colgroup>
			<tbody>
				<tr>
					<td colspan="2" style="text-align: center;">상담신청</td>
				</tr>
				<tr>
					<th>상담구분<span style="color: red;">*</span></th>
					<td>
						<select id="cnsGb" name="cnsGb">
							<c:forEach items="${cnsGbList }" var="list">
								<option value="${list.odr }" <c:if test="${list.odr == result.cnsGb }">selected</c:if> >${list.mclassNm }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>권역구분 <span style="color: red;">*</span></th>
					<td>
						<select id="zoneGb" name="zoneGb">
							<c:forEach items="${zoneGbList }" var="list">
								<c:choose>
									<c:when test="${authCd > 1 }">
										<c:if test="${ list.odr eq loginVo.zoneGb }" >
											<option value="${list.odr }">${list.mclassNm }</option>
										</c:if>
									</c:when>
									<c:when test="${authCd <= 1 }">								
										<option value="${list.odr }" <c:if test="${list.odr == result.zoneGb }">selected</c:if> >${list.mclassNm }</option>
									</c:when>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>센터구분 <span style="color: red;">*</span></th>
					<td>
						<select id="centerGb" name="centerGb">
							<c:forEach items="${cnsCenterList }" var="list">
								<c:choose>
								<c:when test="${authCd > 1 }">
									<c:if test="${ list.num eq vo.schCenterGb }" >
										<option value="${list.num }" >${list.centerGb }</option>
									</c:if>
								</c:when>
								<c:when test="${authCd <= 1 }">								
									<option value="${list.num }" <c:if test="${list.num == result.centerGb }">selected</c:if> >${list.centerGb }</option>
								</c:when>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
				<%-- <tr>
					<th>지역구분 <span style="color: red;">*</span></th>
					<td>
						<select id="localGb" name="localGb">
							<c:forEach items="${localGbList }" var="list">
								<option value="${list.odr }" <c:if test="${list.odr == result.localGb }">selected</c:if>>${list.mclassNm }</option>
							</c:forEach>
						</select>
					</td>
				</tr> --%>
				<tr>
					<th>시군구분 <span style="color: red;">*</span></th>
					<td>
						<select id="sigunCd" name="sigunCd">
							<c:forEach items="${sigunList }" var="list">
								<c:choose>
									<c:when test="${authCd > 1 }">
										<c:if test="${ list.odr eq loginVo.sigunCd }" >
											<option value="${list.odr }">${list.mclassNm }</option>
										</c:if>
									</c:when>
									<c:when test="${authCd <= 1 }">								
										<option value="${list.odr }" <c:if test="${list.odr == result.sigunCd }">selected</c:if> >${list.mclassNm }</option>
									</c:when>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>주호소문제 <span style="color: red;">*</span></th>
					<td>
						<c:forEach items="${mApplCdList}" var="list" varStatus="index">
							<input type="radio" id="majorApplCd${index.count}" name="majorApplCd" value="${list.odr}" <c:if test="${result.majorApplCd eq list.odr }">checked="checked"</c:if> /><label for="majorApplCd${index.count }">${list.mclassNm }</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>이름 <span style="color: red;">*</span></th>
					<td><input type="text" id="userNm" name="userNm" value="${result.userNm }" oninput="this.value = this.value.replace(/[^a-zㄱ-힣]/, '');" maxlength=8/></td>
				</tr>
				<tr>
					<th>성별 <span style="color: red;">*</span></th>
					<td>
						<input type="radio" id="male" name="gender" value="M" <c:if test="${result.gender == 'M' }">checked="checked"</c:if> /><label for="male">남 </label>
						<input type="radio" id="female" name="gender" value="F" <c:if test="${result.gender == 'F' }">checked="checked"</c:if> /><label for="female">여</label>
					</td>
				</tr>
				<tr>
					<th>생년월일 <span style="color: red;">*</span></th>
					<td><input type="text" id="birthDt" name="birthDt" value="${result.birthDt }" onlyNumber maxlength=8/></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input type="text" id="email" name="email" value="${result.email }" /></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input type="text" id="tel" name="tel" value="${result.tel }" onlyNumber maxlength=11/></td>
				</tr>
				<tr>
					<th>핸드폰<span style="color: red;"> *</span></th>
					<td><input type="text" id="mobile" name="mobile" value="${result.mobile }" onlyNumber maxlength=11 /></td>
				</tr>
				<tr>
					<th>주소<span style="color: red;"> *</span></th>
					<td><input type="text" id="addr" name="addr" value="${result.addr }"/></td>
				</tr>
				<tr>
					<th>상담이력 <span style="color: red;">*</span></th>
					<td>
						<input type="radio" id="cnsHistY" name="cnsHistYn" value="Y" <c:if test="${result.cnsHistYn == 'Y' }">checked="checked"</c:if> /><label for="cnsHistY">유 </label>
						<input type="radio" id="cnsHistN" name="cnsHistYn" value="N" <c:if test="${result.cnsHistYn == 'N' }">checked="checked"</c:if> /><label for="cnsHistN">무</label>
					</td>
				</tr>
				<tr id="cnsPath">
					<th>상담접수경로</th>
					<td>
						<c:choose>
							<c:when test="${result.acptGb eq '1' }">
								온라인
							</c:when>
							<c:when test="${result.acptGb eq '2' }">
								모바일
							</c:when>
							<c:otherwise>
								상담시스템(직접등록)
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<div>
		<c:if test="${authCd <= 1 }">
			<button type="button" id="dBtn" class="btn-basic" onclick="javascript:fn_save('D');">삭제</button>
		</c:if>
		<button type="button" id="uBtn" class="btn-basic" onclick="javascript:fn_save('U');">수정</button>
		<button type="button" class="btn-basic" id="showdisable" style="background-color: red;color:white; display:none;">수정불가</button>
		<button type="button" class="btn-basic" onclick="window.close();">닫기</button>
	</div>
</body>
</html>