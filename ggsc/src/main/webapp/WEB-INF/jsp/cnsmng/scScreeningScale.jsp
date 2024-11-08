<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

    <link href="/gnoincoundb/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="/gnoincoundb/js/jquery-1.11.2.min.js" ></script>
    <script src="/gnoincoundb/js/html2canvas.min.js" ></script>
    <script src="/gnoincoundb/js/jspdf.min.js" ></script>
    <script src="/gnoincoundb/js/jquery-accordion-menu.js" ></script>
<style>
	ul.tabs{
	margin: 0px;
	padding: 0px;
	list-style: none;
	}
	ul.tabs li{
		background: none;
		color: #222;
		display: inline-block;
		padding: 10px 15px;
		cursor: pointer;
	}
	.filebox input[type="file"] {
    position: absolute;
    width: 0;
    height: 0;
    padding: 0;
    overflow: hidden;
    border: 0;
	}
	.filebox label {
	    display: inline-block;
	    padding: 2px 20px;
	    color: #999;
	    vertical-align: middle;
	    background-color: #fdfdfd;
	    cursor: pointer;
	    border: 1px solid #ebebeb;
	    border-radius: 5px;
	}
	
	/* named upload */
	.filebox .upload-name {
	    display: inline-block;
	  height: 25px;
	  font-size:18px; 
	  padding: 0 10px;
	  vertical-align: middle;
	  background-color: #f5f5f5;
	  border: 1px solid #ebebeb;
	  border-radius: 5px;
	  width: 600px;
	}
	
	.bgcolor {
		background-color: #B2CCFF;
	}
	
</style>

<script type="text/javascript">
	$(document).ready(function() {
		var caseNo = "${caseNo}";
		var cnsleId = "${detail.cnsleId }";
		$("#Ubtn").css("display", "none");
		var fileTarget = $('#file'); 
		fileTarget.on('change', function(){ // 값이 변경되면
		    var cur=$(".filebox input[type='file']").val();
			$(".upload-name").val(cur);
		}); 
		$("input[type=radio]").click(function(){
			if($("input[name=writeYn]:checked").val() == "Y"){
				$("#uploadTr").css("display", "none");
				$("#Ubtn").css("display", "none");
				if(cnsleId != ""){
				$("#Ibtn").css("display", "none");
				}else{
				$("#Ibtn").css("display", "");
				}
			}else if($("input[name=writeYn]:checked").val() == "N"){
				$("#uploadTr").css("display", "");
				$("#Ubtn").css("display", "");
				$("#Ibtn").css("display", "none");
			}
		});
		
		if(cnsleId != "") {
			$("#caseNo").val("${caseNo}");
			$("#Ibtn").css("display","none");
			$("#Sbtn").css("display","");
			$("#findBtn").css("display","none");
			$('input:radio[name=ansr1]:input[value=${detail.ansr1}]').attr("checked", true);
			$('input:radio[name=ansr2]:input[value=${detail.ansr2}]').attr("checked", true);
			$('input:radio[name=ansr3]:input[value=${detail.ansr3}]').attr("checked", true);
			$('input:radio[name=ansr4]:input[value=${detail.ansr4}]').attr("checked", true);
			$('input:radio[name=gender]:input[value=${detail.gender}]').attr("checked", true);
			
			$("input:radio[name=qust1Ansr]:input[value=${detail.qust1Ansr}]").attr("checked", true);
			$("input:radio[name=qust2Ansr]:input[value=${detail.qust2Ansr}]").attr("checked", true);
			$("input:radio[name=qust3Ansr]:input[value=${detail.qust3Ansr}]").attr("checked", true);
			$("input:radio[name=qust4Ansr]:input[value=${detail.qust4Ansr}]").attr("checked", true);
			$("input:radio[name=qust5Ansr]:input[value=${detail.qust5Ansr}]").attr("checked", true);
			$("input:radio[name=qust6Ansr]:input[value=${detail.qust6Ansr}]").attr("checked", true);
			$("input:radio[name=qust7Ansr]:input[value=${detail.qust7Ansr}]").attr("checked", true);
			$("input:radio[name=qust8Ansr]:input[value=${detail.qust8Ansr}]").attr("checked", true);
			$("input:radio[name=qust9Ansr]:input[value=${detail.qust9Ansr}]").attr("checked", true);
			$("input:radio[name=qust10Ansr]:input[value=${detail.qust10Ansr}]").attr("checked", true);
			$("input:radio[name=qust11Ansr]:input[value=${detail.qust11Ansr}]").attr("checked", true);
			$("input:radio[name=qust12Ansr]:input[value=${detail.qust12Ansr}]").attr("checked", true);
			/*
			$("input:radio[name=qust13Ansr]:input[value=${detail.qust13Ansr}]").attr("checked", true);
			$("input:radio[name=qust14Ansr]:input[value=${detail.qust14Ansr}]").attr("checked", true);
			$("input:radio[name=qust15Ansr]:input[value=${detail.qust15Ansr}]").attr("checked", true);
			*/
			
		} else {
			$("#Ibtn").css("display","");
			$("#Sbtn").css("display","none");
			$("#findBtn").css("display","");
		}
		
	});

	function fn_goLink(no){
		var url = "";
		
		if(no == 1){
			url = "/gnoincoundb/securityPledge.do?mnuCd=${mnuCd}&caseNo=${caseNo}";	
		}else if(no == 2){
			url = "/gnoincoundb/cnsAgreement.do?mnuCd=${mnuCd}&caseNo=${caseNo}";
		}else if(no == 3){
			url = "/gnoincoundb/privacyAgreement.do?mnuCd=${mnuCd}&caseNo=${caseNo}";
		}else{
			url = "/gnoincoundb/scScreeningScale.do?mnuCd=${mnuCd}&caseNo=${caseNo}";
		}
		document.location.href = url;
	}

	function fn_reg(save){
		
		//console.log($("#strtTime").val());
		
		if(save == "U") {
			var cnsleId = $("#cnsleId").val();
			if(cnsleId == "") {
				alert("사용자를 선택해 주세요.");
				return false;
			}
			
			if(confirm("파일업로드를 하시겠습니까?")) {
				/*
				$("#strtTime").val(1);
				$("#strtMin").val(1);
				$("#endTimeHour").val(1);
				$("#endTimeMin").val(1);
				$("#totTime").val(1);
				$("#strtTime").val(1);
				*/
				$("#yesTotScore").val(1);
				$("#noTotScore").val(1);
				$("#cnsrOptnScore").val(1);
				$("#cnsrOptnTot").val(1);
				
				document.frm.action = "/gnoincoundb/preExamReg.do?mnuCd=${mnuCd}";
		       	document.frm.submit();
			}
			return false;
		}
		
		if($("#cnsleId").val()==""){
			alert("ID를 입력해주세요.");
			$("#cnsleId").focus();
			return;
		}
		
		if($("#cnsleNm").val()==""){
			alert("사용자명을 입력해주세요.");
			$("#cnsleNm").focus();
			return;
		}
		
		if($("input[name=writeYn]:checked").val() == "Y"){
			
			if($("#rewdNo").val()==""){
				alert("사례번호를 입력해주세요.");
				$("#rewdNo").focus();
				return;
			}
			/*
			if($("#strtTime").val()==""){
				alert("시작시간(시)을 입력해주세요.");
				$("#strtTime").focus();
				return;
			}
			
			if($("#strtTime").val().length != 2){
				alert("시작시간(시)앞에 0을 입력해주세요.");
				$("#strtTime").focus();
				return;
			}
			if($("#strtMin").val()==""){
				alert("시작시간(분)을 입력해주세요.");
				$("#strtMin").focus();
				return;
			}
			if($("#strtMin").val().length != 2){
				alert("시작시간(분)앞에 0을 입력해주세요.");
				$("#strtMin").focus();
				return;
			}
			*/
			if($("#birthDt").val()==""){
				alert("생년월일을 입력해주세요.");
				$("#birthDt").focus();
				return;
			}
			if($('input:radio[name="gender"]').is(':checked')==false){
				alert("성별을 선택해주세요.");
				return;
			}
			if($("#year").val()==""){
				alert("날짜를 입력해주세요.");
				$("#year").focus();
				return;
			}
			if($("#month").val()==""){
				alert("날짜를 입력해주세요.");
				$("#month").focus();
				return;
			}
			if($("#month").val().length != 2){
				alert("해당 [월]앞에 0을 입력해주세요.");
				$("#month").focus();
				return;
			}
			if($("#day").val()==""){
				alert("날짜를 입력해주세요.");
				$("#day").focus();
				return;
			}
			if($("#day").val().length != 2){
				alert("해당 [일]앞에 0을 입력해주세요.");
				$("#day").focus();
				return;
			}
			/*
			if($("#endTimeHour").val()==""){
				alert("종료시간(시)을 입력해주세요.");
				$("#endTimeHour").focus();
				return;
			}
			if($("#endTimeHour").val().length != 2){
				alert("종료시간(시)앞에 0을 입력해주세요.");
				$("#endTimeHour").focus();
				return;
			}
			if($("#endTimeMin").val()==""){
				alert("종료시간(분)을 입력해주세요.");
				$("#endTimeMin").focus();
				return;
			}
			if($("#endTimeMin").val().length != 2){
				alert("종료시간(분)앞에 0을 입력해주세요.");
				$("#endTimeMin").focus();
				return;
			}
			if($("#totTime").val()==""){
				alert("총 소요시간을 입력해주세요.");
				$("#totTime").focus();
				return;
			}
			*/
			if($('input:radio[name="ansr1"]').is(':checked')==false){
				alert("응답1을 선택해주세요.");
				return;
			}
			if($('input:radio[name="ansr2"]').is(':checked')==false){
				alert("응답2을 선택해주세요.");
				return;
			}
			if($('input:radio[name="ansr3"]').is(':checked')==false){
				alert("응답3을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust1Ansr"]').is(':checked')==false){
				alert("질문1 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust2Ansr"]').is(':checked')==false){
				alert("질문2 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust3Ansr"]').is(':checked')==false){
				alert("질문3 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust4Ansr"]').is(':checked')==false){
				alert("질문4 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust5Ansr"]').is(':checked')==false){
				alert("질문5 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust6Ansr"]').is(':checked')==false){
				alert("질문6 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust7Ansr"]').is(':checked')==false){
				alert("질문7 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust8Ansr"]').is(':checked')==false){
				alert("질문8 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust9Ansr"]').is(':checked')==false){
				alert("질문9 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust10Ansr"]').is(':checked')==false){
				alert("질문10 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust11Ansr"]').is(':checked')==false){
				alert("질문11 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust11Ansr"]').is(':checked')==false){
				alert("질문11 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust12Ansr"]').is(':checked')==false){
				alert("질문12 답을 선택해주세요.");
				return;
			}
			/*
			if($('input:radio[name="qust13Ansr"]').is(':checked')==false){
				alert("질문13 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust14Ansr"]').is(':checked')==false){
				alert("질문14 답을 선택해주세요.");
				return;
			}
			if($('input:radio[name="qust15Ansr"]').is(':checked')==false){
				alert("질문15 답을 선택해주세요.");
				return;
			}
			*/
			if($("#yesTotScore").val()==""){
				alert("예 합계점수를 입력해주세요.");
				$("#yesTotScore").focus();
				return;
			}
			if($("#noTotScore").val()==""){
				alert("아니오 합계점수를 입력해주세요.");
				$("#noTotScore").focus();
				return;
			}
			if($("#cnsrOptnScore").val()==""){
				alert("상담사 평점 점수를 입력해주세요.");
				$("#cnsrOptnScore").focus();
				return;
			}
			if($("#cnsrOptnTot").val()==""){
				alert("총점을 입력해주세요.");
				$("#cnsrOptnTot").focus();
				return;
			}
		}else{
			if(!$("#file").val()){
				alert("파일을 업로드해주세요.");
				$("#file").focus();
				return;
			}
		}
		
		var tot = parseInt($("#cnsrOptnTot").val());
		var q9 = $("input[name=qust9Ansr]:checked").val();
			
		if(tot >= 9 ) {
			alert("검사 결과 9점 이상이므로  상담대상자로 선정됐습니다.");
		} else if(q9 == "Y"){
			alert("9번 항목을 체크하셨으므로  상담대상자로 선정됐습니다.");
		} 
		
		if(save == "I") {
			if(confirm("등록 하시겠습니까?")){
				document.frm.action = "/gnoincoundb/preExamReg.do?mnuCd=${mnuCd}&save="+save;
		       	document.frm.submit();
			}			
		} else if(save == "S") {
			if(confirm("수정 하시겠습니까?")){
				document.frm.action = "/gnoincoundb/preExamReg.do?mnuCd=${mnuCd}&save="+save;
		       	document.frm.submit();
			}
		}
	}

	function findUserPopup(){
		var userNm = $("#cnsleNm").val();
		userNm = encodeURI(encodeURIComponent(userNm));
		var caseNo = "${caseNo}";
		var url = "/gnoincoundb/findUserPopup.do?userNm=" + userNm+"&cnsTargetGb=Y"+"&schCaseNo="+caseNo;
		var name = "회원 찾기";
		var option = "width = 530, height = 750, top = 50, left = 100, location = yes";
		window.open(url, name, option);encodeURI(encodeURIComponent(jindan_name));
	}
	
	function getGb(){
		if(document.getElementById('caseNo').value.length > 0){
			var token = $("meta[name='_csrf']").attr("th:content");
			var header = $("meta[name='_csrf_header']").attr("th:content");
			$.ajax({
				url : "/gnoincoundb/cnsAcceptDtl_ajax.do",
				type : "post",
				dataType : "json",
				data : { caseNo : document.getElementById('caseNo').value },
				beforeSend : function(xhr){
					xhr.setRequestHeader(header, token);
				}
			}).done(function(e){
				var data = e.Detail;
				var arr = [ 'cnsNm', 'zoneNm', 'centerNm' ];
				for(var i=0;i<arr.length;i++){
					$("[data-type='" + arr[i] + "']").text(eval("data." + arr[i] ));
				}
				
			}).fail(function(e){
				alert('서버 오류가 발생하였습니다.');
			})
			
		}
	}
	
	function fn_list(mnuCd){
		document.location.href = "/gnoincoundb/pretestList.do?mnuCd=" + mnuCd;
	}
	
</script>

<section id="content">

	<h2 class="h2-title"><i class="fa fa-check-square"></i>노인상담선별척도지 등록</h2>
		<div class="box-style1 x-scroll-auto" >
			<div>
				<ul class="tabs">
					<!-- <li style="border-style: solid; border-bottom-style:none; border-width: thin;" onclick="fn_goLink(1);">보안서약서</li> -->
					<li style="border-style: solid; border-bottom-style:none; border-width: thin;" onclick="fn_goLink(2);">상담동의서</li>
					<li style="border-style: solid; border-bottom-style:none; border-width: thin;" onclick="fn_goLink(3);">개인정보동의서</li>
					<li style="border-style: solid; border-bottom-style:none; border-width: thin; background-color: gray;">노인상담선별척도지</li>
					<div class="txtR" style="width: 73.2%; display: inline-block;">
						<button type="button" id="Ubtn" class="btn-basic" onClick="javascript:fn_reg('U');" style="background-color: green;color:white;">업로드</button>
						<button type="button" id="Ibtn" class="btn-basic" onClick="javascript:fn_reg('I');" style="background-color: green;color:white;">등록</button>
						<button type="button" id="Sbtn" class="btn-basic" onClick="javascript:fn_reg('S');" style="background-color: green;color:white;">수정</button>
						<button type="button" class="btn-basic" onClick='javascript:fn_list("${mnuCd}");' style="background-color: green;color:white;">목록</button>
					</div>
				</ul>
			</div>
			<div style="border-style: solid; border-width: thin; width:100%; height: auto; padding: 10px 10px 10px 10px;">
				<form id="frm" name="frm" method="post" enctype="multipart/form-data">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<input type="hidden" id="mnuCd" name="mnuCd" value="${mnuCd }" />
					<input type="hidden" id="caseNo" name="caseNo" value="0" />
					<input type="hidden" id="examDocCd" name="examDocCd" value="4" />
					<input type="hidden" id="docGb" name="docGb" value="1" />
					<input type="hidden" value="${userNm }" oninput="this.value = this.value.replace(/[^ㄱ-힣]/, '');"/></td>
					<input type="hidden" id="localGb" name="localGb" value="9"/>
					<input type="hidden" id="docCntnPath" name="docCntnPath" value="Y" />
					<table border="1" class="table-write" style="width: 101.2%; position: relative; left: -10px; top: -10px; border-left-style:hidden; border-right-style: hidden; border-top-style: hidden;">
						<colgroup>
							<col width="15%" >
							<col width="35%" >
							<col width="15%" >
							<col width="35%" >
						</colgroup>
						<tbody>
							<tr>
								<th>문서구분</th>
								<td>노인상담선별척도지</td>
								<th>작성여부</th>
								<td>
									<input type="radio" style="margin: 0 4px 4px 7px;" name="writeYn" id="writeY" value="Y" checked="checked" /><label for="writeY">예</label>
									<!-- <input type="radio" style="margin: 0 4px 4px 7px;" name="writeYn" id="writeN" value="N" /><label for="writeN">아니오</label> -->
								</td>
							</tr>
							<tr>
								<th>내담자ID</th>
								<td><input type="text" id="cnsleId" name="cnsleId" style="width: 200px;" value="${detail.cnsleId }" readonly="readonly" /></td>
								<th>내담자명</th>
								<td><input type="text" id="cnsleNm" name="cnsleNm" style="width: 200px;" value="${detail.inputNm }" readonly="readonly" /><button type="button" id="findBtn" class="btn-basic" style="padding: 2px 6px;" onclick="javascript:findUserPopup();">찾기</button></td>
							</tr>
							<tr height=33>
								<th>상담구분</th>
								<td><span id="cnsGbR" data-type='cnsNm'><c:out value="${detail.cnsGbNm }" /></span></td>
								<th>권역구분</th>
								<td><span id="zoneGbR" data-type='zoneNm'><c:out value="${detail.zoneGbNm }" /></span></td>
							</tr>
							<tr height=33>
								<th>센터구분</th>
								<td><span id="centerGbR" data-type='centerNm'><c:out value="${detail.centerGbNm }" /></span></td>
								<th></th>
								<td></td>
							</tr>
							<tr id="uploadTr" style="display: none;">
								<th>직접업로드</th>
								<td colspan="3">
									<div class="filebox"> 
	  									<input type="file" id="file" name="file"> 
										<input class="upload-name" value="파일선택">
										<label class="btn-basic" style="background-color: gray;color:white;" for="file">찾기</label> 
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				<div style="border-style: solid; border-width: thin; width:700px; height: auto; padding: 30px 20px 20px 20px; margin: 10px auto auto auto;">

					<h1 style="text-align: center; font-size:x-large; padding-bottom: 20px;">노인상담선별척도지</h1><br>
					<table border="1">
						<colgroup>
							 <col width="8%"></col>
        					 <col width="17%"></col>
        					 <col width="10%"></col>
        					 <col width="15%"></col>
        					 <col width="10%"></col>
        					 <col width="40%"></col>
						</colgroup>
						<tr>
							<td>작성자</td>			   
							<td><input type="text" value="${detail.cnsleId }" readonly /></td>
							<td>사례번호</td>
							<td><input type="text" id="rewdNo" name="rewdNo" value="${dccNum }" readonly /></td>
							<td>작성일</td>
							<td><input type="text" style="width: 45px;" id="year" name="year" value="${fn:substring(detail.writeDt,0,4) }" oninput="this.value = this.value.replace(/[^0-9]/g, '')" maxlength="4"/>년
							<input type="text" style="width: 25px;" id="month" name="month" value="${fn:substring(detail.writeDt,4,6) }" oninput="this.value = this.value.replace(/[^0-9]/g, '')" maxlength="2"/>월
							<input type="text" style="width: 25px;" id="day" name="day" value="${fn:substring(detail.writeDt,6,8) }" oninput="this.value = this.value.replace(/[^0-9]/g, '')" maxlength="2"/>일</td>
							<%-- 
							<td><input type="text" id="rewdNo" name="rewdNo" value="${dccNum }" readonly /></td>
							<td>시작시간</td>
							<td><input type="text" style="width: 30px;" id="strtTime" name="strtTime" value="${detail.strtTime }" onlyNumber maxlength=2 />시 <input type="text" style="width: 30px;" id="strtMin" name="strtMin" value="${detail.strtMin }" onlyNumber maxlength=2/>분</td>
							 --%>
						</tr>
						<tr>
							<td>이름</td>			   
							<td><input type="text" value="${detail.inputNm }" readonly /></td>
							<td>성별</td>
							<td><input type="radio" id="genderM" name="gender" value="M" />남&nbsp;&nbsp;&nbsp;<input type="radio" id="genderF" name="gender" value="F" />여</td>
							<td>생년월일</td>
							<td><input type="text" id="birthDt" name="birthDt" value="${detail.birthDt }" onlyNumber maxlength=6 readonly/></td>
							<%-- 
							<td>종료시간</td>
							<td><input type="text" style="width: 30px;" id="endTimeHour" name="endTimeHour" value="${detail.endTimeHour }" onlyNumber maxlength=2 />시 <input type="text" style="width: 30px;" id="endTimeMin" name="endTimeMin" value="${detail.endTimeMin }" onlyNumber maxlength=2 />분</td>
							<td>총 소요시간</td>
							<td><input type="text" style="width: 50px;" id="totTime" name="totTime" value="${detail.totTime }" onlyNumber maxlength=3 />분</td>
							 --%>
						</tr>
					</table>
					<br>
					<font style="font-size: medium;"> ※ 상담사는 아래의 척도를 시행하기에 앞서 신체적 질병 및 인지·지각장애 사항을 점검하고, 
					그로 인하여 파생되는 어려움은 아닌지에 대하여 변별·사용하시길 바랍니다.</font><br><br>
					<table border="1">
						<colgroup>
							<col width="*"></col>
							<col width="10%"></col>
							<col width="10%"></col>
							<col width="20%"></col>
						</colgroup>
						<tr>
							<td rowspan="2">문항</td>
							<td colspan="2">응답</td>
							<td>관찰평가</td>
						</tr>
						<tr>
							<td>예</td>
							<td>아니오</td>
							<td>내용 및 특징</td>
						</tr>
						<tr>
							<td style="text-align: left; padding-left: 5px;">1. 최근(1년 이내) 다치거나 병원에 입원한 적이 있다.</td>
							<td><input type="radio" id="ansr1Y" name="ansr1" value="Y" /></td>
							<td><input type="radio" id="ansr1N" name="ansr1" value="N" /></td>
							<td><input type="text" id="ansr1Cntn" name="ansr1Cntn" maxlength="20" value="${detail.ansr1Cntn }" /></td>
						</tr>
						<tr>
							<td style="text-align: left; padding-left: 5px;">2. 만성질환으로 약물을 복용하고 있다.</td>
							<td><input type="radio" id="ansr2Y" name="ansr2" value="Y" /></td>
							<td><input type="radio" id="ansr2N" name="ansr2" value="N" /></td>
							<td><input type="text" id="ansr2Cntn" name="ansr2Cntn" maxlength="20" value="${detail.ansr2Cntn }" /></td>
						</tr>
						<tr>
							<td style="text-align: left; padding-left: 5px;">3. 신경정신 관련 진단을 받은 바 있다. (예: 치매진단)</td>
							<td><input type="radio" id="ansr3Y" name="ansr3" value="Y" /></td>
							<td><input type="radio" id="ansr3N" name="ansr3" value="N" /></td>
							<td><input type="text" id="ansr3Cntn" name="ansr3Cntn" maxlength="20" value="${detail.ansr3Cntn }" /></td>
						</tr>
						<tr>
							<td style="text-align: left; padding-left: 5px;">4. 신체적 학대(부당한 대우)를 받은 적이 있다.</td>
							<td><input type="radio" id="ansr4Y" name="ansr4" value="Y" /></td>
							<td><input type="radio" id="ansr4N" name="ansr4" value="N" /></td>
							<td><input type="text" id="ansr4Cntn" name="ansr4Cntn" maxlength="20" value="${detail.ansr4Cntn }" /></td>
						</tr>
					</table>
					<br>
					<font style="font-size: medium;"> ※ 지시문</font><br><br>
					<table border="1" style="width: 100%;">
						<tr>
							<td style="text-align: left; padding: 5px;">"OOO 어르신에게 몇 개의 문장을 읽어드리겠습니다.<br>이에 대하여 가장 솔직한 생각을 예, 아니오의 수준에 맞도록 답변해주시면 됩니다."</td>
						</tr>
					</table>
					<br>
					<table border="1">
						<colgroup>
							<col width="20%"></col>
							<col width="*"></col>
							<col width="10%"></col>
							<col width="10%"></col>
						</colgroup>
						<thead>
							<th colspan="2">문항</th>
							<th>예</th>
							<th>아니오</th>
						</thead>
						<tbody>
							<tr> 
								<td colspan="2" style="text-align: left; padding-left: 5px;">1. 혼자 남겨졌다고 느끼십니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust1AnsrY" name="qust1Ansr" value="N" /></td>
								<td><input class="radBtn2" type="radio" id="qust1AnsrN" name="qust1Ansr" value="Y" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">2. 주변에 당신을 정말 이해해주는 사람들이 있다고 느낍니까?</td>
								<td><input class="radBtn2" type="radio" id="qust2AnsrY" name="qust2Ansr" value="Y" /></td>
								<td class="bgcolor"><input class="radBtn" type="radio"  id="qust2AnsrN" name="qust2Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">3. 아플 때 의지하고 도움을 요청할 수 있는 사람이 있습니까?</td>
								<td><input class="radBtn2" type="radio" id="qust3AnsrY" name="qust3Ansr" value="Y" /></td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust3AnsrN" name="qust3Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">4. 안절부절 못하거나 가슴이 두근거리고, 걱정하느라 잠을 못 잔 적이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust4AnsrY" name="qust4Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust4AnsrN" name="qust4Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">5. 가족이나 주변 사람들과 갈등이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust5AnsrY" name="qust5Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust5AnsrN" name="qust5Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">6. 최근 한 달 이내에 사랑하는 가족이나 지인 혹은 반려동물의 죽음을 경험한 적이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust6AnsrY" name="qust6Ansr" value="N" /></td>
								<td><input class="radBtn2" type="radio" id="qust6AnsrN" name="qust6Ansr" value="Y" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">7. 최근(2주 이내)에 사소한 일에도 화가 나거나, 주변 사람들에게 짜증을 내는 경우가 자주 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust7AnsrY" name="qust7Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust7AnsrN" name="qust7Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">8. 기분이 가라앉거나 우울할 때가 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust8AnsrY" name="qust8Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust8AnsrN" name="qust8Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">9. 최근(2주 이내)에 죽고 싶다는 생각을 한 적이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust9AnsrY" name="qust9Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust9AnsrN" name="qust9Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">10. 살아온 삶이 의미없다고 생각하신 적이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust10AnsrY" name="qust10Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust10AnsrN" name="qust10Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">11. 최근(2주 이내)에 식욕이 줄거나 평소 하던 일에 대한 흥미가 없고 즐거움을 느끼지 못한 적이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust11AnsrY" name="qust11Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust11AnsrN" name="qust11Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">12. 나는 쓸모가 없는 사람이라는 생각을 한 적이 있습니까?</td>
								<td class="bgcolor"><input class="radBtn" type="radio" id="qust12AnsrY" name="qust12Ansr" value="Y" /></td>
								<td><input class="radBtn2" type="radio" id="qust12AnsrN" name="qust12Ansr" value="N" /></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: left; padding-left: 5px;">■상담사 평점 : 이 응답자의 심리상담 개입 필요정도를 0~6점 중에서 작성해 주세요</td>
								<td colspan="2"><input type="text" class="wd50" id="cnsrOptnScore" name="cnsrOptnScore" value="${detail.cnsrOptnScore }" onlyNumber maxlength=2>점</td>
							</tr>
							<tr>
								<td>채점</td>
								<td style="text-align: left; padding-left: 5px;">※ 총 18점(설문 12점, 상담사 평정 6점) 중 9점 이상인 경우 상담대상자로 선정함. 진한 색에 1점 부여, 그 외 0점을 부여하되, 긴급 상황으로 여겨질 수 있는 9번의 경우 전체 점수가 9점이 되지 않더라도 대상자로 선정할 수 있음. </td>
								<td colspan="2"><input type="text" class="wd30" id="yesTotScore" name="yesTotScore" value="${detail.yesTotScore }" readonly onlyNumber maxlength=2>점 / 12점</td>
							</tr>
							<tr>
								<td colspan="2">총점</td>
								<td colspan="2"><input type="text" class="wd30" id="cnsrOptnTot" name="cnsrOptnTot" value="${detail.cnsrOptnTot }" readonly onlyNumber maxlength=2>점 / 18점</td>
							</tr>
						</tbody>
					</table>
				</div>
				</form>
			</div>
		</div>
	<script>
		$(".radBtn, .radBtn2, #cnsrOptnScore").change(function(e) {
			var y = $(".radBtn:checked").length, n= $(".radBtn2:checked").length;
			$("#yesTotScore").val(y);
			$("#noTotScore").val(n);
			var yesTotal = $("#yesTotScore").val();
			var noTotal = $("#noTotScore").val();
			var total = parseInt(yesTotal)+parseInt(noTotal);
			var cnsrOptnScore = $("#cnsrOptnScore").val();
			
			if(cnsrOptnScore > 6) {
				alert("상담사 평점은 0~6점 까지만 입력 가능합니다.");
				cnsrOptnScore = 0;
				$("#cnsrOptnScore").val(0)
				$("#cnsrOptnScore").focus();
			}
			
			if(cnsrOptnScore){
				$("#cnsrOptnTot").val(parseInt(yesTotal) + parseInt(cnsrOptnScore)).trigger('change');
			}else{
				$("#cnsrOptnTot").val(parseInt(yesTotal)).trigger('change');
			}
			
		});
		
	</script>

		<!-- end -->
	</section>
</html>

