<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="/gnoincoundb/js/html2canvas.min.js" ></script>
<script src="/gnoincoundb/js/jspdf.min.js" ></script>
<style>
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
	
</style>
  </head>
<script type="text/javascript">
	const daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];
	const delExamNumList = [];
	
	$(document).ready(function() {
		
		var fileTarget = $('#file'); 
		fileTarget.on('change', function(){ // 값이 변경되면
		    //var cur=$(".filebox input[type='file']").val();
		    var cur=$("#file").val();
			//$(".upload-name").val(cur);
			$("#upNm").val(cur);
		});
		
		var fileTarget1 = $('#file1'); 
		fileTarget1.on('change', function(){ // 값이 변경되면
		    var cur1=$("#file1").val();
			$("#upNm1").val(cur1);
		});
		
		var fileTarget2 = $('#file2'); 
		fileTarget2.on('change', function(){ // 값이 변경되면
		    var cur2=$("#file2").val();
			$("#upNm2").val(cur2);
		});
		
		var cnsStat = "${result.cnsStat}";
		if(cnsStat != ""){
			$("#cnsStat").val(cnsStat).prop("selected", true);
		}
		var cnsEndCd = "${result.cnsEndCd}";
		if(cnsEndCd != ""){
			$("#cnsEndCd").val(cnsEndCd).prop("selected", true);
		}
		var defrCd = "${result.defrCd}";
		if(defrCd != ""){
			$("#defrCd").val(defrCd).prop("selected", true);
		}
		var cnsGb = "${result.cnsGb}";
		if(cnsGb != ""){
			$("#cnsGb").val(cnsGb).prop("selected", true);
		}
		var cnsrGb = "${result.cnsrGb}";
		if(cnsrGb != ""){
			$("#cnsrGb").val(cnsrGb).prop("selected", true);
		}
		var cnsDtWeekCd = "${result.cnsDtWeekCd}";
		var cnsDt = "${result.cnsDtWeekCd}";
		if(cnsDtWeekCd != "" && cnsDt != ""){
			$("#cnsDtWeekNm").val(daysOfWeek[cnsDtWeekCd]);
		}
		var cnsRsvtWeekCd = "${result.cnsRsvtWeekCd}";
		var cnsRsvtDt = "${result.cnsRsvtDt}";
		if(cnsRsvtWeekCd != "" && cnsRsvtDt != ""){
			$("#cnsRsvtWeekNm").val(daysOfWeek[cnsRsvtWeekCd]);
		}else{
			$("#cnsRsvtStrtHour").val(0);
			$("#cnsRsvtStrtMin").val(0);
			$("#cnsRsvtEndHour").val(0);
			$("#cnsRsvtEndMin").val(0);
			$("#cnsRsvtTotMin").val(0);
		}
		var cnsMethd = "${result.cnsMethd}";
		if(cnsMethd != ""){
			$("#cnsMethd").val(cnsMethd).prop("selected", true);
		}
		
		var str;
		var name;
		
		str = "${result.gender}";
		name = "gender";
		fn_checked(str, name);
		
		str = "${result.emgcyUrgt}";
		name = "emgcyUrgt";
		fn_checked(str, name);
		
		str = "${result.killsOptn}";
		name = "killsOptn";
		fn_checked(str, name);
		
		str = "${result.ahydSuptYn}";
		name = "ahydSuptYn";
		fn_checked(str, name);
		
		str = "${result.abusOptn}";
		name = "abusOptn";
		fn_checked(str, name);
		
		str = "${result.cnsleRel}";
		name = "cnsleRel";
		fn_checked(str, name);
		
		str = "${result.majorApplCd}";
		name = "majorApplCd";
		fn_checked(str, name);
		
		appl_detailInit(str);
		
		str = "${result.killsRskn}";
		name = "killsRskn";
		fn_checked(str, name);
		
		str = "${result.psycQust}";
		name = "psycQust";
		fn_checked(str, name);
		
		str = "${result.perRelQust}";
		name = "perRelQust";
		fn_checked(str, name);
		
		str = "${result.psng}";
		name = "psng";
		fn_checked(str, name);
		
		str = "${result.dmta}";
		name = "dmta";
		fn_checked(str, name);
		
		str = "${result.dsm}";
		name = "dsm";
		fn_checked(str, name);
		
		str = "${result.helthQust}";
		name = "helthQust";
		fn_checked(str, name);
		
		str = "${result.econoQust}";
		name = "econoQust";
		fn_checked(str, name);
		
		str = "${result.emotCptvDegr}";
		name = "emotCptvDegr";
		fn_checked(str, name);
		
		str = "${result.helpDegr}";
		name = "helpDegr";
		fn_checked(str, name);
		
		str = "${result.cnsChngDegr}";
		name = "cnsChngDegr";
		fn_checked(str, name);
		
		
		//체크박스 해제
	    $('.emotCptvDegrRemove').click(function() {
	        $("input:radio[name='emotCptvDegr']").removeAttr("checked");
	    });
	    $('.helpDegrRemove').click(function() {
	        $("input:radio[name='helpDegr']").removeAttr("checked");
	    });
	    $('.cnsChngDegrRemove').click(function() {
	        $("input:radio[name='cnsChngDegr']").removeAttr("checked");
	    });
	    
	    //날짜
	    
	    var nowTime = $("#nowTime").val();
	    var dbInsTm = $("#dbInsTm").val(); 
	    parseInt(nowTime);
	    parseInt(dbInsTm);		
		var authCd = $("input[name='cnsrGb']").val();
		
		var closetDate = new Date(nowTime.substring(0,7)+"-8");
		var monthDate = new Date(nowTime.substring(0,7)+"-1");
		var nowDate = new Date();
		var dbInstDate = new Date(dbInsTm);
		
		var dbInsertMonth = dbInstDate.getMonth()+1;
		var nowMonth = nowDate.getMonth()+1;
		
		var dbInsertYear = dbInstDate.getFullYear();
		var nowYear = nowDate.getFullYear();
		console.log(closetDate);
		console.log(nowDate);
		console.log(dbInstDate);
		// 마감일자보다 현재 시간이 큰가 ? 
		// 익월 7일 기준으로 마감이 진행되야 하기 때문에 if 구조 동작
		if(closetDate < nowDate){
			// start if
			console.log("여기도 안 들어와 ?");
			
			// 게시글 등록 월과 현재 월이 같지 않은가 ?
			// 그럼 마감이 된 데이터로 판단
			if(dbInsertMonth != nowMonth){
				
				// 게시글 등록 시간보다 마감시간이 큰가 ?
				// 맞다면 전월 데이터로 판단하여 마감 처리 진행
				if(dbInstDate < closetDate){
					// 수정 불가 버튼 로딩
					$('.btn-basic[onClick*=fn_update]').hide();
					$('.btn-basic[onClick*=fn_delete]').hide();
					$('#showdisable').show();
				}
				
			}else{ // 게시글 등록일과 마감일자의 월이 같은가 ?
				
				// 이 블럭에선 같다면 별도의 동작 프로세스가 없음 
				// 당월 데이터는 수정이 가능해야 하기 때문에 
				
				// 해당 IF 는 월만 비교해서 전년도 데이터가 수정이 가능한 것을 확인하였음 , 작년도 데이터는 당연히 마감이 되어야 하기 때문에 이러한 검증 if 가 있음 
				if(dbInsertYear != nowYear){
					
					// 게시글 등록 시간보다 마감시간이 더 큰가 ? 
					// 크다면 전월 데이터로 취급되야 하기 때문에 마감 처리 진행
					if(dbInstDate <= closetDate){
						$('.btn-basic[onClick*=fn_update]').hide();
						$('.btn-basic[onClick*=fn_delete]').hide();
						$('#showdisable').show();
					}
				}
			}
		
		}else{
			
			// 해당 블럭은 마감일자가 지나기전의 상황임 
			// 위의 블럭과 같은 프로세스를 구현하기 위해 구성된 블럭임.
			
			if(dbInsertMonth != nowMonth){
				if(dbInstDate < closetDate){
					$('.btn-basic[onClick*=fn_update]').hide();
					$('.btn-basic[onClick*=fn_delete]').hide();
					$('#showdisable').show();
				}					
			}else{
				if(dbInsertYear != nowYear){
					if(dbInstDate <= closetDate){
						$('.btn-basic[onClick*=fn_update]').hide();
						$('.btn-basic[onClick*=fn_delete]').hide();
						$('#showdisable').show();
					}
				}
			}
		// end if
		}
		

		// dbInstDate로부터 익월 8일 생성
		var nextMonthDate = new Date(dbInstDate.getFullYear(), dbInstDate.getMonth() + 1, 8);
		
		if(authCd == 1){
			$('.btn-basic[onClick*=fn_update]').show();
			$('.btn-basic[onClick*=fn_delete]').show();
			$('#showdisable').hide();
		}else if((nowDate < nextMonthDate) && authCd == 3){
			console.log("들어옴");
			$('.btn-basic[onClick*=fn_update]').show();
			$('#showdisable').hide();
		}

		
	});
	
	function fn_checked(str, name){
		if(str != ""){
			var arr = str.split(",");
			for(var i in arr){
				$("input[name=" + name + "][value=" + arr[i] + "]").prop("checked", true);
			}	
		}
	}
	
	function fn_down(fileNm, filePath){
		document.location.href = "/fileDown.do?fileNm=" + fileNm + "&filePath=" + filePath;
	}  	
	
	/*
	function fn_fileUpload(){
		 var form = $('#fileFrm')[0];
         var formData = new FormData(form);
         formData.append("file", $("#file")[0].files[0]);

		alert($("#file")[0].files[0]);
		alert($("input[name=file]").val());
        $.ajax({
            data : formData,
            type : "POST",
            url : "/acptFileUpload.do",
            enctype : "multipart/form-data",
            contentType : false,
            processData : false,
            success : function(data) {
				alert("문서 업로드가 완료 되었습니다.");
            }
        });
	}
	*/
	function fn_update(){
		
		var cnsleNm = $("#cnsleNm").val();
		var cnsCnt = $("#cnsCnt").val();
		var cnsStat = $("#cnsStat").val();
		var datepicker8 = $("#datepicker8").val(); 
		var cnsDtStdHour = $("#cnsDtStdHour").val(); 
		var cnsDtStdMin = $("#cnsDtStdMin").val(); 
		var cnsDtEndHour = $("#cnsDtEndHour").val();
		var cnsDtEndMin = $("#cnsDtEndMin").val();
		var cnsTimeTotMin = $("#cnsTimeTotMin").val();
		
		var cnsRsvtStrtHour = $("#cnsRsvtStrtHour").val();
		var cnsRsvtStrtMin = $("#cnsRsvtStrtMin").val();
		var cnsRsvtEndHour = $("#cnsRsvtEndHour").val();
		var cnsRsvtEndMin = $("#cnsRsvtEndMin").val();
		var cnsRsvtTotMin = $("#cnsRsvtTotMin").val();

		if(cnsleNm.length == 0) {
			alert("내담자명을 입력해 주세요");
			$("#cnsleNm").focus();
			return;
		}
		if(cnsCnt.length == 0) {
			alert("상담회기를 입력해 주세요");
			$("#cnsCnt").focus();
			return;
		}
		if(datepicker8.length == 0) {
			alert("상담일시를 입력해 주세요");
			$("#datepicker8").focus();
			return;
		}
		if(cnsDtStdHour.length == 0) {
			alert("상담일시 시간을 입력해 주세요");
			$("#cnsDtStdHour").focus();
			return;
		}
		if(cnsDtStdHour.length != 2){
			alert("상담일시 시간을 2자리로 입력해 주세요.");
			$("#cnsDtStdHour").focus();
			return;
		}
		if(cnsDtStdMin.length == 0) {
			alert("상담일시 분을 입력해 주세요");
			$("#cnsDtStdMin").focus();
			return;
		}
		if(cnsDtStdMin.length != 2){
			alert("상담일시 분을 2자리로 입력 해주세요.");
			$("#cnsDtStdMin").focus();
			return;
		}
		if(cnsDtEndHour.length == 0) {
			alert("상담일시 시간을 입력해 주세요");
			$("#cnsDtEndHour").focus();
			return;
		}
		if(cnsDtEndHour.length != 2){
			alert("상담일시 시간을 2자리로 입력해 주세요.");
			$("#cnsDtEndHour").focus();
			return;
		}
		if(cnsDtEndMin.length == 0) {
			alert("상담일시 분을 입력해 주세요");
			$("#cnsDtEndMin").focus();
			return;
		}
		if(cnsDtEndMin.length != 2){
			alert("상담일시 분을 2자리로 입력해 주세요.");
			$("#cnsDtEndMin").focus();
			return;
		}
		if(cnsTimeTotMin.length == 0) {
			alert("상담일시 총시간을 입력해 주세요");
			$("#cnsTimeTotMin").focus();
			return;
		}
		if($('input:radio[name="cnsleRel"]').is(':checked')==false){
			alert("내담자와의 관계를 선택해주세요.");
			return;
		}
		if($("input:radio[name='majorApplCd']").is(":checked") == false) {
			alert("주호소문제를 체크해 주세요.");
			return;
		}
		if($("input:radio[name='majorApplCdDetail']").is(":checked") == false) {
			alert("주호소문제 상세를 체크해 주세요.");
			return;
		}
		
		if(cnsRsvtStrtHour.length == 0) {
			$("#cnsRsvtStrtHour").val(0);
		}
		if(cnsRsvtStrtMin.length == 0) {
			$("#cnsRsvtStrtMin").val(0);
		}
		if(cnsRsvtEndHour.length == 0) {
			$("#cnsRsvtEndHour").val(0);
		}
		if(cnsRsvtEndMin.length == 0) {
			$("#cnsRsvtEndMin").val(0);
		}
		if(cnsRsvtTotMin.length == 0) {
			$("#cnsRsvtTotMin").val(0);
		}
		
		if(validateExamDoc() == 'f') {
		    alert("검사지명과 점수를 모두 입력해주세요.")
			return;
		}else{
			var dataToSend = validateExamDoc();
			var form = document.getElementById("frm");
			
			dataToSend.forEach(function(i){
				// 새로운 input 요소 생성
	            var newExamInput = document.createElement("input");
	            newExamInput.type = "hidden";
	            newExamInput.name = "newExamDocNm";
	            newExamInput.value = i.examDocNm;
	            form.appendChild(newExamInput);
	            
	            var newNo1Input = document.createElement("input");
	            newNo1Input.type = "hidden";
	            newNo1Input.name = "newNo1";
	            newNo1Input.value = i.no1;
	            form.appendChild(newNo1Input);
	            
			});
			
			//삭제리스트 추가
			if(delExamNumList.length > 0){
				delExamNumList.forEach(function(i){
		            var delExamNumInput = document.createElement("input");
		            delExamNumInput.type = "hidden";
		            delExamNumInput.name = "delExamNumList";
		            delExamNumInput.value = i;
		            form.appendChild(delExamNumInput);
				});
			}
			
			
		}
		
		if(confirm("수정 하시겠습니까?")){
			document.frm.action = "/gnoincoundb/perCnsUpd.do?mnuCd=${mnuCd}";
	       	document.frm.submit();
		}
	}
	
	function validateExamDoc(){
		 var dataToSend = [];
		 var flag = 't';
	        // 모든 입력 컨테이너를 선택
	        var inputContainers = document.querySelectorAll('.input-container');
		
	        // 각 입력 컨테이너에 대하여 반복
	        inputContainers.forEach(function(container) {
	           // var examNumInput = container.querySelector('input[name="examNum"]');
	            var examDocNmInput = container.querySelector('input[name="examDocNm"]');
	            var no1Input = container.querySelector('input[name="no1"]');
	            
	            // examDocNm와 no1이 모두 값이 있는 경우에만 데이터를 추가
	            if (examDocNmInput.value.trim() !== '' && no1Input.value.trim() !== '') {
	                var rowData = {
	                    //examNum: examNumInput.value.trim(),
	                    examDocNm: examDocNmInput.value.trim(),
	                    no1: no1Input.value.trim()
	                };
	                dataToSend.push(rowData);
	            }else if (examDocNmInput.value.trim() === '' && no1Input.value.trim() === '') {
	                // 두 입력 모두 값이 없을 때 아무런 처리 없음
	                flag = 'f';
	            } else if (examDocNmInput.value.trim() === '') {
	                flag = 'f';
	            } else if (no1Input.value.trim() === '') {
	                flag = 'f';
	            }
	        });
	        console.log(flag);
	        for (var i = 0; i < dataToSend.length; i++) {
				console.log(dataToSend[i].examDocNm);
				console.log(dataToSend[i].no1);
			}
	        
	        return flag == 'f' ? flag : dataToSend;
	}
	
	function appl_detailInit(appl) {
		appl = appl.padStart(4,'G10')
		var token = $("meta[name='_csrf']").attr("th:content");
		var header = $("meta[name='_csrf_header']").attr("th:content");
		
		$.ajax({
			type : "POST" ,
			url  : "/gnoincoundb/majorApplDetailList_ajax.do" ,
			data : {hclassCd: "G58", mclassCd: appl},
			dataType : "json",
			beforeSend : function(xhr){ 
				xhr.setRequestHeader(header, token);
			},
			success : function(json) {
				var html = '<th>주호소문제 상세<span style="color: red;">*</span></th>';
				html += '<td colspan="4">';
				$.each(json.list, function(i, d) {
					html += '<input type="radio" name="majorApplCdDetail" id="majorApplCdDetail'+Number(i+1)+'" value="'+Number(i+1)+'">';
					html += '<label for="majorApplCdDetail'+Number(i+1)+'">';
					html +=  d.sclassNm;
					html += '</label>';
				});
				html += '</td>';
				$("#appl").html(html);
				var appldt = "${result.majorApplCdDetail}";
				$("input:radio[name=majorApplCdDetail]:radio[value="+appldt+"]").prop("checked",true);
			},
			error : function(e) {
				alert("서버와 통신 오류입니다");	
			}
		});
	}
	
	function appl_detail(appl) {
		appl = appl.padStart(4,'G10')
		var token = $("meta[name='_csrf']").attr("th:content");
		var header = $("meta[name='_csrf_header']").attr("th:content");
		
		$.ajax({
			type : "POST" ,
			url  : "/gnoincoundb/majorApplDetailList_ajax.do" ,
			data : {hclassCd: "G58", mclassCd: appl},
			dataType : "json",
			beforeSend : function(xhr){ 
				xhr.setRequestHeader(header, token);
			},
			success : function(json) {
				var html = '<th>주호소문제 상세<span style="color: red;">*</span></th>';
				html += '<td colspan="4">';
				$.each(json.list, function(i, d) {
					html += '<input type="radio" name="majorApplCdDetail" id="majorApplCdDetail'+Number(i+1)+'" value="'+Number(i+1)+'">';
					html += '<label for="majorApplCdDetail'+Number(i+1)+'">';
					html +=  d.sclassNm;
					html += '</label>';
				});
				html += '</td>';
				$("#appl").html(html);
			},
			error : function(e) {
				alert("서버와 통신 오류입니다");	
			}
		});
	}
	
	function fn_delete() {	
		if(confirm("개인상담과 연결된 심리검사와 종결서(상담회기 기준)까지 삭제됩니다. 삭제 하시겠습니까?")) {
			document.frm.action = "/gnoincoundb/perCnsDel.do?mnuCd=${mnuCd}";
	       	document.frm.submit();
		}
	}
	
	function fn_list(mnuCd){
		document.location.href = "/gnoincoundb/perCnsList.do?mnuCd=" + mnuCd;
	}
	
	function findUserPopup(){
		var userNm = $("#cnsleNm").val();
		userNm = encodeURI(encodeURIComponent(userNm));		
		var url = "/gnoincoundb/findUserPopup.do?userNm=" + userNm+"&cnsTargetGb=Y&cnsPerTargetGb=Y";
		var name = "회원 찾기";
		var option = "width = 530, height = 750, top = 50, left = 250, location = yes";
		window.open(url, name, option);encodeURI(encodeURIComponent(jindan_name));
	}
	
	$(document).on('change','#cnsStat',function(){
		console.log($(this).val());
		$("[data-type='hideClass1']").addClass('hide');
		$("[data-type='hideClass1'][data-class='" + $(this).val() + "']").removeClass('hide');
	});
	
	$(document).on('click','input[name="majorApplCd"]', function() {
		var appl = $('input[name="majorApplCd"]:checked').val();
		appl_detail(appl);
	});

	function cnsEndPopup(){
		
		var cnsEndDt = $("input[name=cnsEndDt]").val();
		var rmCnsEndDt = cnsEndDt.replaceAll(".","");
		var cnsEndCd = $("#cnsEndCd").val();
		if(cnsEndDt != "" && cnsEndCd != "") {
			document.location.href = "/gnoincoundb/cnsEndPopup.do?mnuCd=${mnuCd}&cnsEndCd="+cnsEndCd+"&cnsEndDt="+rmCnsEndDt+"&num=${result.num }&cnsleId=${result.cnsleId}";			
		} else {
			alert("종결일과 종결사유를 선택해주세요.");
		}
	}
	
	$(document).on('focusout', 'input[name="cnsDtStdMin"]' ,function(){
		var cnsDtStdMin = $("input[name='cnsDtStdMin']").val();
		
		if(cnsDtStdMin.length < 2){
			$("input[name='cnsDtStdMin']").val("0" + cnsDtStdMin);
		}
	}); 
	
	$(document).on('focusout', 'input[name="cnsDtEndMin"]' ,function(){
		var cnsDtEndMin = $("input[name='cnsDtEndMin']").val();
		
		if(cnsDtEndMin.length < 2){
			$("input[name='cnsDtEndMin']").val("0" + cnsDtEndMin);
		}
	}); 
	
	$(document).on('change', 'input[name*="cnsDt"]', function(){
		var cnsDtStdHour = $("input[name='cnsDtStdHour']").val();
		var cnsDtStdMin = $("input[name='cnsDtStdMin']").val();
		var cnsDtEndHour = $("input[name='cnsDtEndHour']").val();
		var cnsDtEndMin = $("input[name='cnsDtEndMin']").val();
		var cnsTimeTotMin = $("input[name='cnsTimeTotMin']");
		
		if(cnsDtStdHour !== "" && cnsDtStdMin !== "" && cnsDtEndHour !== "" && cnsDtEndMin !== ""){
			// 입력된 값들을 정수로 변환합니다.
	        cnsDtStdHour = parseInt(cnsDtStdHour);
	        cnsDtStdMin = parseInt(cnsDtStdMin);
	        cnsDtEndHour = parseInt(cnsDtEndHour);
	        cnsDtEndMin = parseInt(cnsDtEndMin);
	        
	     	// 종료 시간이 시작 시간보다 이전인지 확인합니다.
	        if(cnsDtEndHour < cnsDtStdHour || (cnsDtEndHour === cnsDtStdHour && cnsDtEndMin < cnsDtStdMin)){
	            alert("종료 시간이 시작 시간보다 이전입니다.");
	            $("input[name='cnsTimeTotMin']").val("");
	            return; // 종료 시간이 시작 시간보다 이전이면 함수 종료
	        }
	     	
			var stdTime = cnsDtStdHour + ":" + cnsDtStdMin;
			var endTime = cnsDtEndHour + ":" + cnsDtEndMin;
			
			cnsTimeTotMin.val(calculateTimeDifference(stdTime, endTime));
		}
		
	});
	
	$(document).on('change', 'input[name="cnsDt"]', function(){
		var dateString = $(this).val();
		
		var dayIndex = getDayOfWeek(dateString);
		
		
	    // 요일을 텍스트로 반환합니다.
	    var dayOfWeek = daysOfWeek[dayIndex];
	    
	   dayIndex = dayIndex == 0 ? 7 : dayIndex; 
	
		$("input[name='cnsDtWeekCd']").val(dayIndex);
		$("input[name='cnsDtWeekNm']").val(dayOfWeek);
		
	});
	
	$(document).on('focusout', 'input[name="cnsRsvtStrtMin"]' ,function(){
		var cnsRsvtStrtMin = $("input[name='cnsRsvtStrtMin']").val();
		
		if(cnsRsvtStrtMin.length < 2){
			$("input[name='cnsRsvtStrtMin']").val("0" + cnsRsvtStrtMin);
		}
	}); 
	
	$(document).on('focusout', 'input[name="cnsRsvtEndMin"]' ,function(){
		var cnsRsvtEndMin = $("input[name='cnsRsvtEndMin']").val();
		
		if(cnsRsvtEndMin.length < 2){
			$("input[name='cnsRsvtEndMin']").val("0" + cnsRsvtEndMin);
		}
	}); 
	
	$(document).on('change', 'input[name*="cnsRsvt"]', function(){
		var cnsRsvtStrtHour = $("input[name='cnsRsvtStrtHour']").val();
		var cnsRsvtStrtMin = $("input[name='cnsRsvtStrtMin']").val();
		var cnsRsvtEndHour = $("input[name='cnsRsvtEndHour']").val();
		var cnsRsvtEndMin = $("input[name='cnsRsvtEndMin']").val();
		var cnsRsvtTotMin = $("input[name='cnsRsvtTotMin']");
		
		if(cnsRsvtStrtHour !== "" && cnsRsvtStrtMin !== "" && cnsRsvtEndHour !== "" && cnsRsvtEndMin !== ""){
			// 입력된 값들을 정수로 변환합니다.
	        cnsRsvtStrtHour = parseInt(cnsRsvtStrtHour);
	        cnsRsvtStrtMin = parseInt(cnsRsvtStrtMin);
	        cnsRsvtEndHour = parseInt(cnsRsvtEndHour);
	        cnsRsvtEndMin = parseInt(cnsRsvtEndMin);
	        
	     	// 종료 시간이 시작 시간보다 이전인지 확인합니다.
	        if(cnsRsvtEndHour < cnsRsvtStrtHour || (cnsRsvtEndHour === cnsRsvtStrtHour && cnsRsvtEndMin < cnsRsvtStrtMin)){
	            alert("종료 시간이 시작 시간보다 이전입니다.");
	            $("input[name='cnsRsvtTotMin']").val("");
	            return; // 종료 시간이 시작 시간보다 이전이면 함수 종료
	        }
	     	
			var stdTime = cnsRsvtStrtHour + ":" + cnsRsvtStrtMin;
			var endTime = cnsRsvtEndHour + ":" + cnsRsvtEndMin;
			
			cnsRsvtTotMin.val(calculateTimeDifference(stdTime, endTime));
		}
		
	});
	
	$(document).on('change', 'input[name="cnsRsvtDt"]', function(){
		var dateString = $(this).val();
		
		var dayIndex = getDayOfWeek(dateString);		
		
	    // 요일을 텍스트로 반환합니다.
	    var dayOfWeek = daysOfWeek[dayIndex];
	    
	   dayIndex = dayIndex == 0 ? 7 : dayIndex; 
	
		$("input[name='cnsRsvtWeekCd']").val(dayIndex);
		$("input[name='cnsRsvtWeekNm']").val(dayOfWeek);
		
		console.log("들어왔는데?");
	});
	
	function getDayOfWeek(dateString) {
	    // 입력된 날짜 문자열로부터 Date 객체 생성
	    var date = new Date(dateString);

	    // Date 객체에서 getDay() 메서드를 사용하여 요일을 숫자로 반환합니다.
	    // 0: 일요일, 1: 월요일, ..., 6: 토요일
	    var dayIndex = date.getDay();

	    return dayIndex;
	}
	
	function calculateTimeDifference(start, end) {
	    // 시작 시간과 종료 시간을 ":"를 기준으로 시와 분으로 분리합니다.
	    var [startHour, startMinute] = start.split(':').map(Number);
	    var [endHour, endMinute] = end.split(':').map(Number);

	    // 시작 시간과 종료 시간을 분 단위로 변환합니다.
	    var startTimeInMinutes = startHour * 60 + startMinute;
	    var endTimeInMinutes = endHour * 60 + endMinute;

	    // 시간 차이를 계산하여 양수로 반환합니다.
	    var timeDifference = Math.abs(endTimeInMinutes - startTimeInMinutes);

	    return timeDifference;
	    
	}
	
	
	document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('add-row').addEventListener('click', function() {
            var inputFields = document.getElementById('input-fields');
            
            if(inputFields.children.length == 10){
            	alert("입력 가능한 심리검사는 최대 10개입니다.");
            	return
            }
            
        	 // 새로운 input-container div 생성
            var newDiv = document.createElement('div');
            newDiv.className = 'input-container';

            // 새로운 input 태그와 텍스트 노드 추가
            newDiv.innerHTML = '검사지명&nbsp; <input type="text" name="examDocNm" value="" style="width: 250px;"/> &nbsp;점수&nbsp; <input type="text" name="no1" class="wd100" value="" onkeyup="this.value=this.value.replace(/[^0-9]/g,\'\');"/><input type="hidden" name="examNum" value=""/>';

            document.getElementById('input-fields').appendChild(newDiv);
        });

        document.getElementById('remove-row').addEventListener('click', function() {
            var inputFields = document.getElementById('input-fields');
            // 가장 마지막 input-container div를 찾음
            //var lastInputContainer = inputFields.lastElementChild;
               // id가 'input-container'인 모든 div 요소를 찾음
		    var inputContainers = inputFields.querySelectorAll('div.input-container');
		    
		    // 가장 마지막 input-container div를 찾음
		    var lastInputContainer = inputContainers[inputContainers.length - 1];
    
            if (inputFields.children.length > 0) {
                inputFields.removeChild(lastInputContainer);
            }
            
        	
            // input_fields 하위에 있는 name이 "examNum" 인 input 태그들의 값을 배열에 저장
            var lastExamNumInputs = lastInputContainer.querySelectorAll('input[name="examNum"]');
            lastExamNumInputs.forEach(function(input) {
                // -1이 아닌 값을 배열에 추가
                if (input.value !== "") {
                	delExamNumList.push(input.value);
                	console.log(input.value);
                }
            });
            
         	
        });
    });
	
</script>
<section id="content">

	<h2 class="h2-title"><i class="fa fa-check-square"></i>개인상담일지 상세</h2>
		<div class="box-style1 x-scroll-auto" >
			<div class="btn" style="float: right; margin: 0;">
				<button type="button" class="btn-basic" onClick="javascript:fn_list('${mnuCd }');" style="background-color: green;color:white;">목록</button>
				<c:choose>
					<c:when test="${result.cnsStat eq '2' && map.authCd > 1}">
					</c:when>
					<c:otherwise>
						<button type="button" class="btn-basic" onClick="javascript:fn_update('${mnuCd }');" style="background-color: green;color:white;">수정</button>
					</c:otherwise>
				</c:choose>
				<c:if test="${map.authCd <= 1}">
					<button type="button" class="btn-basic" onClick="javascript:fn_delete();" style="background-color: green;color:white;">삭제</button>
				</c:if>
				<button type="button" class="btn-basic" id="showdisable" style="background-color: red;color:white; display:none;">수정불가</button>
				<%--
				<button type="button" class="btn-basic" id="openPsyCnsPop" onClick="javascript:fn_popup('${result.num}')" style="background-color: #af841d;color:white;">심리검사</button>
				 --%>
			</div>	
			<form id="frm" name="frm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="hidden" id="cnsleId" name="cnsleId" value="${result.cnsleId }" />
				<input type="hidden" id="caseNo" name="caseNo" value="${result.caseNo }" />
				<input type="hidden" id="cnsDtWeekCd" name="cnsDtWeekCd" value="${result.cnsDtWeekCd }" />
				<input type="hidden" id="cnsRsvtWeekCd" name="cnsRsvtWeekCd"value="${result.cnsRsvtWeekCd }"  />
				<input type="hidden" id="num" name="num" value="${result.num }" />
				<input type="hidden" name="cnsrGb" value="${map.authCd }" />
				<input type="hidden" id="dbInsTm" value="${fn:substring(result.dbInsTm, 0, 10) }" />
				<input type="hidden" id="nowTime" value="${nowTime }" />				
				<table class="table-write">
					<colgroup>
						<col width="15%"></col>
						<col width="10%"></col> 
						<col width="25%"></col>
						<col width="15%"></col>
						<col width="35%"></col>
					</colgroup>
					<tr>
						<th><input type="radio" id="cnsStatRb" name="radioB" /> <label for="cnsStatRb">상담상태</label><span style="color: red;">*</span></th>
						<td colspan="2">
							<select class="wd200" id="cnsStat" name="cnsStat">
								<c:forEach items="${cnsStatList }" var="list" begin="0" end="2">
									<option value="${list.odr }">${list.mclassNm }</option>
								</c:forEach>
							</select> 
						</td>
						<th class='hide' data-type='hideClass1' data-class='2'><label>종결 <span style="color: red;">*</span></label></th>
						<td class='hide' data-type='hideClass1' data-class='2' colspan="2">
							<span class="form"><input type="text" class="wd100" id="datepicker1" name="cnsEndDt" value="" readonly /></span>
							<select class="wd200 mg-l25" id="cnsEndCd" name="cnsEndCd">
								<option value="" selected="selected">선택</option>
									<c:choose>
										<c:when test="${result.cnsCnt eq 1 }">
											<option value="13">단순정보상담</option>
											<option value="14">복지서비스안내</option>
										</c:when>
										<c:otherwise>
											<c:forEach items="${cnsEndCdList }" var="list">
												<option value="${list.odr }">${list.mclassNm }</option>
											</c:forEach>
										</c:otherwise>
									</c:choose>
							</select> 
							<a href="javascript:cnsEndPopup();" style='color:red'>&nbsp;&nbsp;▶ 종결서 작성하기</a>
						</td>
						<th class='hide' data-type='hideClass1' data-class='4'><label>재신청 <span style="color: red;">*</span></label></th>
						<td class='hide' data-type='hideClass1' data-class='4' colspan="2">
							<span class="form"><input type="text" class="wd200" id="datepicker3" name="reReqDt" value="" /></span> 
						</td>
						<th class='hide' data-type='hideClass1' data-class='5'><label>복귀 <span style="color: red;">*</span></label></th>
						<td class='hide' data-type='hideClass1' data-class='5'>
							<span class="form"><input type="text" class="wd200" id="datepicker4" name="cmbkDt" value="" /></span>
						</td>
					</tr>
					<%-- <tr>
						<th><input type="radio" id="cnsEnd" name="radioB" /> <label for="cnsEnd">종결</label></th>
						<td colspan="2">
							<span class="form"><input type="text" class="wd200" id="datepicker1" name="cnsEndDt" value="${result.cnsEndDt}" readonly /></span>
							<select class="wd200 mg-l25" id="cnsEndCd" name="cnsEndCd">
								<option value="1" selected="selected">선택</option>
								<c:forEach items="${cnsEndCdList }" var="list">
									<option value="${list.odr }">${list.mclassNm }</option>
								</c:forEach>
							</select> 
						</td>
						<th><input type="radio" id="defr" name="radioB" /> <label for="defr">보류</label></th>
						<td>
							<span class="form"><input type="text" class="wd200" id="datepicker2" name="defrDt" value="${result.defrDt}" readonly /></span>
							<select class="wd200 mg-l25" id="defrCd" name="defrCd">
								<option value="1" selected="selected">선택</option>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
							</select> 
						</td>
					</tr>
					<tr>
						<th><input type="radio" id="reReq" name="radioB" /> <label for="reReq">재신청</label></th>
						<td colspan="2">
							<span class="form"><input type="text" class="wd200" id="datepicker3" name="reReqDt" value="${result.reReqDt}" readonly /></span> 
						</td>
						<th><input type="radio" id="cmbk" name="radioB" /> <label for="cmbk">복귀</label></th>
						<td>
							<span class="form"><input type="text" class="wd200" id="datepicker4" name="cmbkDt" value="${result.cmbkDt}" readonly /></span>
						</td>
					</tr> --%>
					<tr>
						<th>상담구분 <span style="color: red;">*</span></th>
						<td colspan="2">
							<select class="wd200" id="cnsGb" name="cnsGb">
								<c:forEach items="${cnsGbList }" var="list">
									<option value="${list.odr }">${list.mclassNm }</option>
								</c:forEach>
							</select> 
						</td>
						<th>상담회기 <span style="color: red;">*</span></th>
						<td>
							<input type="text" class="wd200" id="cnsCnt" name="cnsCnt" value="${result.cnsCnt }" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"/>
						</td>
					</tr>
					<tr>
						<th>상담자구분 <span style="color: red;">*</span></th>
						<td colspan="2">
							<select class="wd200" id="cnsrGb" >
								<c:forEach items="${cnsrGbList }" var="list">
									<option value="${list.odr }">${list.mclassNm }</option>
								</c:forEach>
							</select> 
						</td>
						<th>상담자명 <span style="color: red;">*</span></th>
						<td>
							<input type="text" class="wd200" id="cnsrNm" name="cnsrNm" value="${result.cnsrNm }" readonly/>
						</td>
					</tr>
					<tr>
						<th>내담자명 <span style="color: red;">*</span></th>
						<td colspan="2">
							<input type="text" class="wd200" id="cnsleNm" name="cnsleNm" value="${result.cnsleNm }" readonly/>
						</td>
						<th>생년월일 <span style="color: red;">*</span></th>
						<td>
							<span class="form"><input type="text" class="wd200" id="birthDt" name="birthDt" value="${result.birthDt }" readonly/></span>
						</td>
					</tr>
					<tr>
						<th>성별 <span style="color: red;">*</span></th>
						<td colspan="2">
							<input type="radio" id="genderM" name="gender" value="M" /><label for="genderM">남</label>
							<input type="radio" id="genderF" name="gender" value="F" /><label for="genderF">여</label>
						</td>
						<th>주소 <span style="color: red;">*</span></th>
						<td>
							<input type="text" id="addr" name="addr" value="${result.addr }" readonly/>
						</td>
					</tr>
					<tr>
						<th>내담자연락처 <span style="color: red;">*</span></th>
						<td colspan="2">
							<input type="text" class="wd200" id="mobile" name="mobile" value="${result.mobile }" readonly/>
						</td>
						<th>긴급연락처</th>
						<td>
							<input type="text" class="wd200" id="emgcyTel" name="emgcyTel" value="${result.emgcyTel }" />
						</td>
					</tr>
					<tr>
						<th>상담실시장소</th>
						<td colspan="2">
							<input type="text" class="wd200" id="cnsEfftPlace" name="cnsEfftPlace" value="${result.cnsEfftPlace }" />
						</td>
						<th>상담예약장소</th>
						<td>
							<input type="text" class="wd200" id="cnsRsvtPlace" name="cnsRsvtPlace" value="${result.cnsRsvtPlace }" />
						</td>
					</tr>
					<tr>
						<th>상담일시 <span style="color: red;">*</span></th>
						<td colspan="2">
							<span class="form"><input type="text" class="wd200" id="datepicker8" name="cnsDt" value="${result.cnsDt }" readonly /></span>
							<input class="wd50 mg-l25" id="cnsDtWeekNm" name="cnsDtWeekNm" style="text-align: center;" readonly/>
							<br>
							(<input type="text" class="wd50" id="cnsDtStdHour" name="cnsDtStdHour" value="${result.cnsDtStdHour }" />시
							<input type="text" class="wd50" id="cnsDtStdMin" name="cnsDtStdMin" value="${result.cnsDtStdMin }" />분 ~
							<input type="text" class="wd50" id="cnsDtEndHour" name="cnsDtEndHour" value="${result.cnsDtEndHour }" />시
							<input type="text" class="wd50" id="cnsDtEndMin" name="cnsDtEndMin" value="${result.cnsDtEndMin }" />분)
							(총 <input type="text" class="wd50" id="cnsTimeTotMin" name="cnsTimeTotMin" value="${result.cnsTimeTotMin }" />분) 
						</td>
						<th>다음상담예약일</th>
						<td>
							<span class="form"><input type="text" class="wd200" id="datepicker9" name="cnsRsvtDt" value="${result.cnsRsvtDt }" readonly /></span>
							<input class="wd50 mg-l25" id="cnsRsvtWeekNm" name="cnsRsvtWeekNm" style="text-align: center;" readonly/>
							<br>
							(<input type="text" class="wd50" id="cnsRsvtStrtHour" name="cnsRsvtStrtHour" value="${result.cnsRsvtStrtHour }" />시
							<input type="text" class="wd50" id="cnsRsvtStrtMin" name="cnsRsvtStrtMin" value="${result.cnsRsvtStrtMin }" />분 ~
							<input type="text" class="wd50" id="cnsRsvtEndHour" name="cnsRsvtEndHour" value="${result.cnsRsvtEndHour }" />시
							<input type="text" class="wd50" id="cnsRsvtEndMin" name="cnsRsvtEndMin" value="${result.cnsRsvtEndMin }" />분)
							(총 <input type="text" class="wd50" id="cnsRsvtTotMin" name="cnsRsvtTotMin" value="${result.cnsRsvtTotMin }" />분) 
						</td>
					</tr>
					<tr>
						<th>위기긴급성</th>
						<td colspan="2">
							<input type="radio" id="emgcyUrgtY" name="emgcyUrgt" value="Y" /><label for="emgcyUrgtY">예</label>
							<input type="radio" id="emgcyUrgtN" name="emgcyUrgt" value="N" /><label for="emgcyUrgtN">아니오</label>
						</td>
						<th>자살사례</th>
						<td>
							<input type="radio" id="killsOptnY" name="killsOptn" value="Y" /><label for="killsOptnY">예</label>
							<input type="radio" id="killsOptnN" name="killsOptn" value="N" /><label for="killsOptnN">아니오</label>
						</td>
					</tr>
					<tr>
						<th>심리검사</th>
						<td colspan="2" id="examDoc">
							<div style="min-height: 66px; display: flex;">
								<div id="input-fields" style="float: left; width:464px">
									<c:choose>
										<%--
										<c:when test="${empty exam}">
										 	<div class="input-container">검사지명&nbsp; <input type="text" name="examDocNm" value="" style="width: 250px;"/> &nbsp;점수&nbsp; <input type="text" name="no1" class="wd100" value="" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');"/><input type="hidden" name="examNum" value="" /></div>
										</c:when>
										 --%>
										<c:when test="${not empty exam}">
										 	<c:forEach var="i" items="${exam}">
											 	<div class="input-container">검사지명&nbsp; <input type="text" name="examDocNm" value="${i.examDocNm}" style="width: 250px;"/> &nbsp;점수&nbsp; <input type="text" name="no1" class="wd100" value="${i.no1}" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');"/><input type="hidden" name="examNum" value="${i.num}" /></div>
										 	</c:forEach>
										</c:when>
									</c:choose>
								</div>
								<div style="width: 60px; text-align: center; float: left;margin-left: 10px; position: relative;">
									<button type="button" class="btn-basic" id="add-row" style="padding: 2px 6px; margin-bottom: 3px; background: green;">행추가</button>
									<button type="button" class="btn-basic" id="remove-row" style="padding: 2px 6px;">행삭제</button>
								</div>
							</div>
						</td>
						<th>학대사례</th>
						<td>
							<input type="radio" id="abusOptnY" name="abusOptn" value="Y" /><label for="abusOptnY">예</label>
							<input type="radio" id="abusOptnN" name="abusOptn" value="N" /><label for="abusOptnN">아니오</label>
						</td>
					</tr>
					<tr>
						<th>상담방법 <span style="color: red;">*</span></th>
						<td colspan="4">
							<select class="wd200" id="cnsMethd" name="cnsMethd">
								<c:forEach items="${cnsMethdList }" var="list">
									<option value="${list.odr }">${list.mclassNm }</option>
								</c:forEach>
							</select> 
						</td>
					</tr>
					<tr>
						<th>내담자의관계<span style="color: red;">*</span></th>
						<td colspan="4">
							<input type="radio" id="cnsleRel1" name="cnsleRel" value="1" /><label for="cnsleRel1">노인본인</label>
							<input type="radio" id="cnsleRel2" name="cnsleRel" value="2" /><label for="cnsleRel2">배우자</label>
							<input type="radio" id="cnsleRel3" name="cnsleRel" value="3" /><label for="cnsleRel3">자녀</label>
							<input type="radio" id="cnsleRel4" name="cnsleRel" value="4" /><label for="cnsleRel4">친인척</label>
							<input type="radio" id="cnsleRel5" name="cnsleRel" value="5" /><label for="cnsleRel5">이웃</label>
							<input type="radio" id="cnsleRel6" name="cnsleRel" value="6" /><label for="cnsleRel6">유관기관</label>
						</td>
					</tr>
					<tr>
						<th>주호소문제 <span style="color: red;">*</span></th>
						<td colspan="4">
							<input type="radio" id="majorApplCd1" name="majorApplCd" value="1" /><label for="majorApplCd1">심리정서</label>
							<input type="radio" id="majorApplCd2" name="majorApplCd" value="2" /><label for="majorApplCd2">대인관계</label>
							<input type="radio" id="majorApplCd3" name="majorApplCd" value="3" /><label for="majorApplCd3">치매</label>
							<input type="radio" id="majorApplCd4" name="majorApplCd" value="4" /><label for="majorApplCd4">중독</label>
							<input type="radio" id="majorApplCd5" name="majorApplCd" value="5" /><label for="majorApplCd5">정신건강장애</label>
							<input type="radio" id="majorApplCd6" name="majorApplCd" value="6" /><label for="majorApplCd6">건강</label>
							<input type="radio" id="majorApplCd7" name="majorApplCd" value="7" /><label for="majorApplCd7">경제</label>
							<input type="radio" id="majorApplCd8" name="majorApplCd" value="8" /><label for="majorApplCd8">생활</label>
							<input type="radio" id="majorApplCd9" name="majorApplCd" value="9" /><label for="majorApplCd9">성</label>
							<input type="radio" id="majorApplCd10" name="majorApplCd" value="10" /><label for="majorApplCd10">부부</label>
							<input type="radio" id="majorApplCd11" name="majorApplCd" value="11" /><label for="majorApplCd11">가족</label>
							<input type="radio" id="majorApplCd12" name="majorApplCd" value="12" /><label for="majorApplCd12">학대</label>
							<input type="radio" id="majorApplCd13" name="majorApplCd" value="13" /><label for="majorApplCd13">노년생애준비</label>
							<input type="radio" id="majorApplCd14" name="majorApplCd" value="14" /><label for="majorApplCd14">자살</label>
							<input type="radio" id="majorApplCd15" name="majorApplCd" value="15" /><label for="majorApplCd15">재난</label>
							<input type="radio" id="majorApplCd16" name="majorApplCd" value="16" /><label for="majorApplCd16">기타</label>
						</td>
					</tr>
					<tr id="appl">
						<th>주호소문제 상세 <span style="color: red;">*</span></th>
						<td colspan="4">
						</td>
					</tr>
					<!-- <tr>
						<td>자살위험성</td>
						<td colspan="3">
							<input type="checkbox" id="killsRskn1" name="killsRskn" value="1" /><label for="killsRskn1">자살생각</label> 
							<input type="checkbox" id="killsRskn2" name="killsRskn" value="2" /><label for="killsRskn2">자살의도</label> 
							<input type="checkbox" id="killsRskn3" name="killsRskn" value="3" /><label for="killsRskn3">자살계획</label> 
							<input type="checkbox" id="killsRskn4" name="killsRskn" value="4" /><label for="killsRskn4">자살시도력</label> 
							<input type="checkbox" id="killsRskn5" name="killsRskn" value="5" /><label for="killsRskn5">자살용기력</label> 
							<input type="checkbox" id="killsRskn6" name="killsRskn" value="6" /><label for="killsRskn6">자살행동</label> 
						</td>
					</tr>
					<tr>
						<td>심리문제</td>
						<td colspan="3">
							<input type="checkbox" id="psycQust1" name="psycQust" value="1" /><label for="psycQust1">우울</label> 
							<input type="checkbox" id="psycQust2" name="psycQust" value="2" /><label for="psycQust2">불안</label> 
							<input type="checkbox" id="psycQust3" name="psycQust" value="3" /><label for="psycQust3">외로움</label> 
							<input type="checkbox" id="psycQust4" name="psycQust" value="4" /><label for="psycQust4">상실감</label> 
							<input type="checkbox" id="psycQust5" name="psycQust" value="5" /><label for="psycQust5">절망감</label> 
							<input type="checkbox" id="psycQust6" name="psycQust" value="6" /><label for="psycQust6">분노(화병)</label> 
							<input type="checkbox" id="psycQust7" name="psycQust" value="7" /><label for="psycQust7">기타</label> 
						</td>
					</tr>
					<tr>
						<td>대인관계문제</td>
						<td colspan="3">
							<input type="checkbox" id="perRelQuest1" name="perRelQust" value="1" /><label for="perRelQuest1">종교인교우</label> 
							<input type="checkbox" id="perRelQuest2" name="perRelQust" value="2" /><label for="perRelQuest2">친구/동료</label> 
							<input type="checkbox" id="perRelQuest3" name="perRelQust" value="3" /><label for="perRelQuest3">이웃</label> 
							<input type="checkbox" id="perRelQuest4" name="perRelQust" value="4" /><label for="perRelQuest4">사회복지사(요양보호사, 노인돌보미 등)</label> 
							<input type="checkbox" id="perRelQuest5" name="perRelQust" value="5" /><label for="perRelQuest5">기타</label> 
						</td>
					</tr>
					<tr>
						<td>중독</td>
						<td colspan="3">
							<input type="checkbox" id="psng1" name="psng" value="1" /><label for="psng1">알코올</label> 
							<input type="checkbox" id="psng2" name="psng" value="2" /><label for="psng2">도박</label> 
							<input type="checkbox" id="psng3" name="psng" value="3" /><label for="psng3">니코틴</label> 
							<input type="checkbox" id="psng4" name="psng" value="4" /><label for="psng4">향정신성약품</label> 
							<input type="checkbox" id="psng5" name="psng" value="5" /><label for="psng5">인터넷, 게임</label> 
							<input type="checkbox" id="psng6" name="psng" value="6" /><label for="psng6">기타</label> 
						</td>
					</tr>
					<tr>
						<td>치매</td>
						<td colspan="3">
							<input type="checkbox" id="dmta1" name="dmta" value="1" /><label for="dmta1">치매염려</label> 
							<input type="checkbox" id="dmta2" name="dmta" value="2" /><label for="dmta2">경도인지 장애</label>
							<input type="checkbox" id="dmta3" name="dmta" value="3" /><label for="dmta3">주요인지 장애</label> 
							<input type="checkbox" id="dmta4" name="dmta" value="4" /><label for="dmta4">치매증상의 급격한 악화</label> 
							<input type="checkbox" id="dmta5" name="dmta" value="5" /><label for="dmta5">기타</label>
						</td>
					</tr>
					<tr>
						<td>정신장애</td>
						<td colspan="3">
							<input type="checkbox" id="dsm1" name="dsm" value="1" /><label for="dsm1">기분장애(우울증 등)</label> 
							<input type="checkbox" id="dsm2" name="dsm" value="2" /><label for="dsm2">불안장애</label> 
							<input type="checkbox" id="dsm3" name="dsm" value="3" /><label for="dsm3">인격장애</label> 
							<input type="checkbox" id="dsm4" name="dsm" value="4" /><label for="dsm4">정신분열증</label> 
							<input type="checkbox" id="dsm5" name="dsm" value="5" /><label for="dsm5">기타 정신증적 증상</label> 
						</td>
					</tr>
					<tr>
						<td>건강문제</td>
						<td colspan="3">
							<input type="checkbox" id="helthQust1" name="helthQust" value="1" /><label for="helthQust1">건강염려</label> 
							<input type="checkbox" id="helthQust2" name="helthQust" value="2" /><label for="helthQust2">질환으로 인한 신체적 증상</label> 
							<input type="checkbox" id="helthQust3" name="helthQust" value="3" /><label for="helthQust3">전체적인 건강악화</label> 
							<input type="checkbox" id="helthQust4" name="helthQust" value="4" /><label for="helthQust4">중증질환</label> <br>
							<input type="checkbox" id="helthQust5" name="helthQust" value="5" /><label for="helthQust5">신체증상의 급격한 악화(입원, 수술 등)</label> 
							<input type="checkbox" id="helthQust6" name="helthQust" value="6" /><label for="helthQust6">시한부 및 장애진단</label> 
						</td>
					</tr>
					<tr>
						<td>경제문제</td>
						<td colspan="3">
							<input type="checkbox" id="econoQust1" name="econoQust" value="1" /><label for="econoQust1">수입감소</label> 
							<input type="checkbox" id="econoQust2" name="econoQust" value="2" /><label for="econoQust2">저소득</label> 
							<input type="checkbox" id="econoQust3" name="econoQust" value="3" /><label for="econoQust3">경제지원 축소 및 탈락</label> 
							<input type="checkbox" id="econoQust4" name="econoQust" value="4" /><label for="econoQust4">부채증가</label> 
							<input type="checkbox" id="econoQust5" name="econoQust" value="5" /><label for="econoQust5">실직</label> <br>
							<input type="checkbox" id="econoQust6" name="econoQust" value="6" /><label for="econoQust6">자녀에게 경제적 지원</label> 
							<input type="checkbox" id="econoQust7" name="econoQust" value="7" /><label for="econoQust7">재산분할 갈등</label> 
							<input type="checkbox" id="econoQust8" name="econoQust" value="8" /><label for="econoQust8">사기(다단계, 보이스피싱 등)</label> 
						</td>
					</tr> -->
					<%-- <tr>
						<td>생활문제</td>
						<td colspan="3"><textarea name="lifeQust">${result.lifeQust }</textarea></td>
					</tr> --%>
					<tr>
						<th rowspan="4">상담내용</th>
						<td>상담내용</td>
						<td colspan="3"><textarea name="cnsCntn" style="height: 250px; width: 600px;">${result.cnsCntn }</textarea></td>
					</tr>
					<tr>
						<td>상담개입및연계</td>
						<td colspan="3"><textarea name="cnsItvtLink" style="width: 600px;">${result.cnsItvtLink }</textarea></td>
					</tr>
					<tr>
						<td>상담회기 평가 및 추후계획</td>
						<td colspan="3"><textarea name="cnsCntEvatCntn" style="width: 600px;">${result.cnsCntEvatCntn }</textarea></td>
					</tr>
					<tr>
						<td>사후관리(사례관리)</td>
						<td colspan="3"><textarea name="daftMng" style="width: 600px;">${result.daftMng }</textarea></td>
					</tr>
					

					<!-- <tr>
						<td>정서적 편안함 정도</td>
						<td colspan="3">
							<input type="radio" id="emotCptvDegr1" name="emotCptvDegr" value="1" /><label for="emotCptvDegr1">1</label> 
							<input type="radio" id="emotCptvDegr2" name="emotCptvDegr" value="2" /><label for="emotCptvDegr2">2</label> 
							<input type="radio" id="emotCptvDegr3" name="emotCptvDegr" value="3" /><label for="emotCptvDegr3">3</label> 
							<input type="radio" id="emotCptvDegr4" name="emotCptvDegr" value="4" /><label for="emotCptvDegr4">4</label> 
							<input type="radio" id="emotCptvDegr5" name="emotCptvDegr" value="5" /><label for="emotCptvDegr5">5</label> 
							<input type="radio" id="emotCptvDegr6" name="emotCptvDegr" value="6" /><label for="emotCptvDegr6">6</label> 
							<input type="radio" id="emotCptvDegr7" name="emotCptvDegr" value="7" /><label for="emotCptvDegr7">7</label> 
							<input type="radio" id="emotCptvDegr8" name="emotCptvDegr" value="8" /><label for="emotCptvDegr8">8</label> 
							<input type="radio" id="emotCptvDegr9" name="emotCptvDegr" value="9" /><label for="emotCptvDegr9">9</label> 
							<input type="radio" id="emotCptvDegr10" name="emotCptvDegr" value="10" /><label for="emotCptvDegr10">10</label> 
							<span class="emotCptvDegrRemove" style="cursor: pointer; text-decoration:underline">초기화</span>
						</td>
					</tr>
					<tr>
						<td>도움 받은 정도</td>
						<td colspan="3">
							<input type="radio" id="helpDegr1" name="helpDegr" value="1" /><label for="helpDegr1">1</label> 
							<input type="radio" id="helpDegr2" name="helpDegr" value="2" /><label for="helpDegr2">2</label> 
							<input type="radio" id="helpDegr3" name="helpDegr" value="3" /><label for="helpDegr3">3</label> 
							<input type="radio" id="helpDegr4" name="helpDegr" value="4" /><label for="helpDegr4">4</label> 
							<input type="radio" id="helpDegr5" name="helpDegr" value="5" /><label for="helpDegr5">5</label> 
							<input type="radio" id="helpDegr6" name="helpDegr" value="6" /><label for="helpDegr6">6</label>
							<input type="radio" id="helpDegr7" name="helpDegr" value="7" /><label for="helpDegr7">7</label> 
							<input type="radio" id="helpDegr8" name="helpDegr" value="8" /><label for="helpDegr8">8</label> 
							<input type="radio" id="helpDegr9" name="helpDegr" value="9" /><label for="helpDegr9">9</label> 
							<input type="radio" id="helpDegr10" name="helpDegr" value="10" /><label for="helpDegr10">10</label> 
							<span class="helpDegrRemove" style="cursor: pointer; text-decoration:underline">초기화</span> 
						</td>
					</tr>
					<tr>
						<td>상담으로 인한 변화 정도</td>
						<td colspan="3">
							<input type="radio" id="cnsChngDegr1" name="cnsChngDegr" value="1" /><label for="cnsChngDegr1">1</label> 
							<input type="radio" id="cnsChngDegr2" name="cnsChngDegr" value="2" /><label for="cnsChngDegr2">2</label> 
							<input type="radio" id="cnsChngDegr3" name="cnsChngDegr" value="3" /><label for="cnsChngDegr3">3</label> 
							<input type="radio" id="cnsChngDegr4" name="cnsChngDegr" value="4" /><label for="cnsChngDegr4">4</label> 
							<input type="radio" id="cnsChngDegr5" name="cnsChngDegr" value="5" /><label for="cnsChngDegr5">5</label> 
							<input type="radio" id="cnsChngDegr6" name="cnsChngDegr" value="6" /><label for="cnsChngDegr6">6</label>
							<input type="radio" id="cnsChngDegr7" name="cnsChngDegr" value="7" /><label for="cnsChngDegr7">7</label> 
							<input type="radio" id="cnsChngDegr8" name="cnsChngDegr" value="8" /><label for="cnsChngDegr8">8</label> 
							<input type="radio" id="cnsChngDegr9" name="cnsChngDegr" value="9" /><label for="cnsChngDegr9">9</label> 
							<input type="radio" id="cnsChngDegr10" name="cnsChngDegr" value="10" /><label for="cnsChngDegr10">10</label> 
							<span class="cnsChngDegrRemove" style="cursor: pointer; text-decoration:underline">초기화</span>
						</td>
					</tr> -->
				</table>
			</form>
		</div>
		<!-- end -->
	</section>

</html>

