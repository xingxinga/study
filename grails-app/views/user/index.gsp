<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="list-user" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <table>
                <thead>
                <tr>
                    <th class="sortable" >ID</th>
                    <th class="sortable" >用户名</th>
                    <th class="sortable" >VIP</th>
                    <th class="sortable" >微信用户名</th>
                    <th>操作</th>
                    %{--<th class="sortable" >${message(code: 'data.image.label', default: 'image')}</th>--}%
                </tr>
                </thead>
                <tbody>
                <g:each in="${userList}" var="user" status="i" >
                    <tr class="${i%2==0? 'even':'odd'}">
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.vip? '是':'否'}</td>
                        <td>${user?.weiXinUser?.nickName}</td>
                        <td >
                            <g:link action="edit" id="${user.id}">查看</g:link>
                        </td>
                        %{--<td>${pronunciationData.image}</td>--}%
                    </tr>
                </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${userCount ?: 0}" />
            </div>
        </div>
    </body>
</html>