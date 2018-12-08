<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'notice.label', default: 'Notice')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-notice" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link action="index">返回</g:link></li>
            </ul>
        </div>
        <div id="edit-notice" class="content scaffold-edit" role="main">
            <h1>编辑公告栏</h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.notice}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.notice}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.notice}" method="PUT">
                <g:hiddenField name="version" value="${this.notice?.version}" />
                <fieldset class="form">
                    <div class='fieldcontain required'>
                        <label>公告栏标题<span class='required-indicator'>*</span></label>
                        <input  value="${notice?.title}" name="title" id="title" required/>
                    </div>
                    <div class='fieldcontain required'>
                        <label>公告栏内容<span class='required-indicator'>*</span></label>
                        <textarea name="content" id="content" required>${notice?.content}</textarea>
                    </div>
                </fieldset>
                %{--<fieldset class="form">
                    <f:all bean="notice"/>
                </fieldset>--}%
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
