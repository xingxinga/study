<%@ page import="study.TypeKeyValue" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'topicsData.label', default: 'TopicsData')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-topicsData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create" params='["fileType":"${study.TypeKeyValue.fileTypeImage}"]'><g:message code="default.new.imageLabel" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="create" params='["fileType":"${study.TypeKeyValue.fileTypeVideo}"]'><g:message code="default.new.videoLabel" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="create" params='["fileType":"${study.TypeKeyValue.fileTypePPT}"]'><g:message code="default.new.PPTLabel" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="create" params='["fileType":"${study.TypeKeyValue.fileTypeWord}"]'><g:message code="default.new.wordLabel" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="list-topicsData" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <th class="sortable" >${message(code: 'data.title.label', default: 'title')}</th>
            <th class="sortable" >${message(code: 'data.scan.label', default: 'scan')}</th>
            <th class="sortable" >${message(code: 'data.vip.label', default: 'vip')}</th>
            <th class="sortable" >${message(code: 'data.typeFile.label', default: 'typeFile')}</th>
            <th>操作</th>
            %{--<th class="sortable" >${message(code: 'data.image.label', default: 'image')}</th>--}%
        </tr>
        </thead>
        <tbody>
        <g:each in="${topicsDataList}" var="topicsData" status="i" >
            <tr class="${i%2==0? 'even':'odd'}">
                <td><g:link action="edit" id="${topicsData.id}">${topicsData.title}</g:link></td>
                <td>${topicsData.scan}</td>
                <td>${topicsData.vip? '是':'否'}</td>
                <td>${topicsData.typeFile.value}</td>
                <td >
                    <g:link action="listDelete" id="${topicsData.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">删除</g:link>
                </td>
                %{--<td>${topicsData.image}</td>--}%
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${topicsDataCount ?: 0}" />
    </div>
</div>
</body>
</html>