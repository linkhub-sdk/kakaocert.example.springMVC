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
        <legend>kakaocertService attribute</legend>
        <ul>
            <li>linkID - ${_linkID}</li>
            <li>secretKey - ${_secretKey}</li>
            <li>IPRestrictOnOff - ${isIPRestrictOnOff}</li>
            <li>useStaticIP - ${useStaticIP}</li>
        </ul>
    </fieldset>
</div>
</body>
</html>