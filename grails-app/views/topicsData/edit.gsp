<%@ page import="study.TypeKeyValue" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'topicsData.label', default: 'TopicsData')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<a href="#edit-topicsData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="edit-topicsData" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]" /> ${topicsData?.typeFile?.value}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.topicsData}">
        <ul class="errors" role="alert">
            <g:eachError bean="${this.topicsData}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="updateAddFile" id="${topicsData.id}" method="POST" enctype="multipart/form-data">
        <fieldset class="form">
            <div class='fieldcontain required'>
                <label>${message(code: 'data.title.label', default: 'title')}<span class='required-indicator'>*</span></label>
                <input type="text" id="title" name="title" required="" value="${topicsData.title}" maxlength="20"/>
            </div>
            <div class='fieldcontain required'>
                <label>${message(code: 'data.typeFile.label', default: 'typeFile')}</label>
                <input  value="${topicsData?.typeFile?.value}" readonly/>
            </div>
            <div class='fieldcontain required'>
                <label>原封面图片</label>
                <g:img  uri="${createLink(controller:'data', action:'img')}?path=${topicsData.image}" style="height: 200px;width: 250px"/>
            </div>
            <div class='fieldcontain required'>
                <label for='imageFile'>
                    ${message(code: 'data.image.label', default: 'image')}
                </label>
                <input  id="imageFile" name="imageFile"  type="file"  value="" style="display:inline" />
            </div>
            <g:if test="${topicsData?.typeFile.ke!=study.TypeKeyValue.fileTypeImage}">
                <div class='fieldcontain required'>
                    <label>原上传文件</label>
                    ${topicsData?.file.split("/")[topicsData?.file.split("/").length-1]} <g:link controller="data" action="file" params='[path:"${topicsData.file}"]'>下载</g:link>
                </div>
                <div class='fieldcontain required'>
                    <label for='dateFile'>
                        ${topicsData?.typeFile?.value}
                    </label>
                    <input  id="dateFile" name="dateFile"  type="file"  value="${topicsData?.file}"  style="display:inline" />
                </div>
            </g:if>
            <g:if test="${topicsData?.typeFile.ke==TypeKeyValue.fileTypeImage}">
                <div class='fieldcontain required'>
                    <label>图片数据</label>
                    <div style="display:block;margin-left: 26%;">
                        <g:each in="${topicsData?.pictureList}" var = "pictureList">
                            <div style="display:inline-block;margin-bottom:10px;vertical-align:top;height: 200px;width: 300px" >
                                <g:img  uri="${createLink(controller:'data', action:'img')}?path=${pictureList}" style="height: 200px;width: 250px" />
                                <g:link controller="data" action="removeImg" params='[path:"${pictureList}",id:"${topicsData.id}"]'>移除</g:link>
                            </div>
                        </g:each>
                    </div>
                </div>
            </g:if>
            <div class='fieldcontain required'>
                <label for='pictureFile'>
                    上传图片数据
                </label>
                <input  id="pictureFile" name="pictureFile"  type="file"  value="" style="display:inline" />
            </div>
            <div class='fieldcontain required'>
                <label for='typeRegion'>
                    ${message(code: 'data.typeRegion.label', default: 'typeRegion')}
                </label>
                <g:select from="${TypeKeyValue.findAllByType(TypeKeyValue.regionType)}" optionKey="id" optionValue="value" name="typeRegion" value="${topicsData.typeRegion.id}"></g:select>
            </div>
            <f:field bean="topicsData" property="vip" value="${topicsData.vip}" label="${message(code: 'data.vip.label', default: 'vip')}"/>
            <div hidden>
                <input type="text" id="image" name="image" value="${topicsData.image}">
                <f:field bean="topicsData" property="scan" value="${topicsData.scan}"/>
                <f:field bean="topicsData" property="typeDate"  value="${topicsData.typeDate}" />
                <f:field bean="topicsData" property="typeFile"  value="${topicsData.typeFile}" />
                <f:field bean="topicsData" property="pictureList"  value="${topicsData?.pictureList}" />
                <f:field bean="topicsData" property="file"  value="${topicsData?.file}" />
            </div>
        </fieldset>
        <fieldset class="buttons">
            <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
        </fieldset>
    </g:form>


%{--<g:form resource="${this.topicsData}" method="PUT">
    <g:hiddenField name="version" value="${this.topicsData?.version}" />
    <fieldset class="form">
        <f:all bean="topicsData"/>
    </fieldset>
    <fieldset class="buttons">
        <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
    </fieldset>
</g:form>--}%
</div>
</body>
</html>
