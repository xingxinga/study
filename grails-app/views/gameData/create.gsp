<%@ page import="study.TypeKeyValue" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'gameData.label', default: 'GameData')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-gameData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-gameData" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /> ${gameData?.typeFile?.value}</h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.gameData}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.gameData}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.gameData}" method="POST" enctype="multipart/form-data">
                <fieldset class="form">
                    <div class='fieldcontain required'>
                        <label>${message(code: 'data.title.label', default: 'title')}<span class='required-indicator'>*</span></label>
                        <input type="text" id="title" name="title" required="" value="${gameData?.title}" maxlength="20"/>
                    </div>
                    <div class='fieldcontain'>
                        <label>${message(code: 'data.typeFile.label', default: 'typeFile')}</label>
                        <input  value="${gameData?.typeFile?.value}" readonly/>
                    </div>
                    <div class='fieldcontain required'>
                        <label for='imageFile'>
                            ${message(code: 'data.image.label', default: 'image')}<span class='required-indicator'>*</span>
                        </label>
                        <input  id="imageFile" name="imageFile"  type="file" %{--accept="image/*"--}%  value="" required="" style="display:inline" />
                    </div>
                    <g:if test="${gameData?.typeFile.ke!=TypeKeyValue.fileTypeImage}">
                        <div class='fieldcontain required'>
                            <label for='dateFile'>
                                ${gameData?.typeFile?.value}<span class='required-indicator'>*</span>
                            </label>
                            <input  id="dateFile" name="dateFile"  type="file"  value="" required="" style="display:inline" />
                        </div>
                    </g:if>
                    <g:if test="${gameData?.typeFile.ke==TypeKeyValue.fileTypeImage}">
                        <div class='fieldcontain required'>
                            <label for='pictureList'>
                                图片数据<span class='required-indicator'>*</span>
                            </label>
                            <input  id="pictureList" name="pictureList"  type="file"  value="" required="" style="display:inline" />
                        </div>
                    </g:if>
                    <div class='fieldcontain required'>
                        <label for='typeRegion'>
                            ${message(code: 'data.typeRegion.label', default: 'typeRegion')}
                        </label>
                        <g:select from="${TypeKeyValue.findAllByType(TypeKeyValue.regionType)}" value="${gameData?.typeRegion?.id}" optionKey="id" optionValue="value" name="typeRegion"></g:select>
                    </div>
                    <f:field bean="gameData" property="vip" label="${message(code: 'data.vip.label', default: 'vip')}"/>
                    <div hidden>
                        <input type="text" id="image" name="image" value="1">
                        <f:field bean="gameData" property="scan" value="0"/>
                        <f:field bean="gameData" property="typeDate"  value="${study.TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypeGame)}" />
                        <f:field bean="gameData" property="typeFile"  value="${gameData?.typeFile}" />
                        <f:field bean="gameData" property="pictureList"  value="${gameData?.pictureList}" />
                        <f:field bean="gameData" property="file"  value="${gameData?.file}" />
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
