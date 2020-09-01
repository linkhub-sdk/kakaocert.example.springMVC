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
    <p class="heading1">kakaocert SDK SpringMVC Example.</p>
    <br/>
    <fieldset class="fieldset1">
        <legend>전자서명 API</legend>
        <ul>
            <li><a href="requestESign">requestESign</a> - 전자서명 요청</li>
            <li><a href="getESignResult">getESignResult</a> - 전자서명 결과확인 (TalkMessage 방식)</li>
            <li><a href="getESignResultApp">getESignResult</a> - 전자서명 결과확인 (AppToApp 방식)</li>
        </ul>
    </fieldset>
    <fieldset class="fieldset1">
        <legend>본인인증 API</legend>
        <ul>
            <li><a href="requestVerifyAuth">requestVerifyAuth</a> - 본인인증 요청</li>
            <li><a href="getVerifyAuthResult">getVerifyAuthResult</a> - 본인인증 결과확인</li>
        </ul>
    </fieldset>
    <fieldset class="fieldset1">
        <legend>자동이체 출금동의 API</legend>
        <ul>
            <li><a href="requestCMS">requestCMS</a> - 자동이체 출금동의 요청</li>
            <li><a href="getCMSResult">getCMSResult</a> - 자동이체 출금동의 결과확인</li>
        </ul>
    </fieldset>
</div>
</body>
</html>