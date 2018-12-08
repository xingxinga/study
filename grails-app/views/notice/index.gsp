<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'notice.label', default: 'Notice')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-notice" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            </ul>
        </div>
        <div id="list-notice" class="content scaffold-list" role="main">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <table>
                <thead>
                <tr>
                    <th class="sortable" >公告栏标题</th>
                    <th class="sortable" >公告栏内容</th>
                </tr>
                </thead>
                <tbody>
                    <g:each in="${noticeList}" var="notice" status="i" >
                        <tr class="${i%2==0? 'even':'odd'}">
                            <td><g:link action="edit" id="${notice.id}">${notice.title}</g:link></td>
                            <td>${notice.content}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${noticeCount ?: 0}" />
            </div>
        </div>
    </body>
</html>