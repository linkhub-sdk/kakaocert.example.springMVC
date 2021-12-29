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
    <p class="heading1">State</p>
    <br/>
    <fieldset class="fieldset1">
        <legend>${requestScope['javax.servlet.forward.request_uri']}</legend>
        <ul>
            <li>receiptId (접수아이디) : ${receiptId}</li>
            <li>tx_id (카카오톡 트랜잭션아이디[AppToApp 앱스킴 호출용]) : ${tx_id}</li>
        </ul>
    </fieldset>
</div>
</body>
</html>