<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="/resources/main.css" media="screen"/>
    <title>kakaocert SDK SpringMVC Example.</title>
</head>
<body>
<div id="content">
    <p class="heading1">Response</p>
    <br/>
    <fieldset class="fieldset1">
        <legend>${requestScope['javax.servlet.forward.request_uri']}</legend>
        
        <fieldset class="fieldset2">
            <ul>
            	<li>receiptID(접수아이디) : ${result.receiptID}</li>
            	<li>clientCode (이용기관코드) : ${result.clientCode}</li>
            	<li>clientName (이용기관명) : ${result.clientName}</li>
            	<li>subClientName (별칭) : ${result.subClientName}</li>
            	<li>subClientCode (별칭코드) : ${result.subClientCode}</li>
            	<li>state (상태코드) : ${result.state}</li>
            	            	
            	<li>expires_in (인증요청 만료시간(초)	) : ${result.expires_in}</li>
                <li>callCenterNum(고객센터 전화번호) : ${result.callCenterNum}</li>
                <li>callCenterName(고객센터명) : ${result.callCenterName}</li>
                <li>tmstitle (인증요청 메시지 제목) : ${result.tmstitle}</li>
                <li>tmsmessage (인증요청 메시지 부가내용) : ${result.tmsmessage}</li>
                <li>allowSimpleRegistYN	(은행계좌 실명확인 생략여부) : ${result.allowSimpleRegistYN}</li>
                <li>verifyNameYN (수신자 실명확인 여부) : ${result.verifyNameYN}</li>
                <li>requestDT (카카오 인증서버 등록일시) : ${result.requestDT}</li>
                <li>expireDT (인증 만료일시) : ${result.expireDT}</li>
                <li>regDT (인증요청 등록일시) : ${result.regDT}</li>
                <li>viewDT (수신자 카카오톡 인증메시지 확인일시) : ${result.viewDT}</li>
                <li>completeDT (수신자 카카오톡 전자서명 완료일시	) : ${result.completeDT}</li>
                <li>verifyDT (서명 검증일시) : ${result.verifyDT}</li>
                <li>payload (payload) : ${result.payload}</li>
            </ul>
        </fieldset>
        
    </fieldset>
</div>
</body>
</html>
