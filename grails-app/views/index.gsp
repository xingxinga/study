<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>英语学习平台</title>

    %{--<asset:link rel="icon" href="favicon.ico" type="image/x-ico" />--}%
</head>
<body>
    <content tag="nav">
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">公共模块<span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><g:link controller="notice">公告栏</g:link></li>
            </ul>
        </li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">学习模块<span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><g:link controller="phonicsData">${message(code: 'phonicsData.label', default: 'PhonicsData')}</g:link></li>
                <li><g:link controller="pronunciationData">${message(code: 'pronunciationData.label', default: 'PronunciationData')}</g:link></li>
                <li><g:link controller="topicsData">${message(code: 'topicsData.label', default: 'TopicsData')}</g:link></li>
                <li><g:link controller="abilitiesData">${message(code: 'abilitiesData.label', default: 'AbilitiesData')}</g:link></li>
                <li><g:link controller="funnyData">${message(code: 'funnyData.label', default: 'FunnyData')}</g:link></li>
                <li><g:link controller="gameData">${message(code: 'gameData.label', default: 'GameData')}</g:link></li>
            </ul>
        </li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">用户信息<span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><g:link controller="user">会员管理</g:link></li>
            </ul>
        </li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">系统设置<span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><g:link>字典管理</g:link></li>
            </ul>
        </li>
    </content>

    <div class="svg" role="presentation">
        <div class="grails-logo-container">
            %{--<asset:image src="English.jpg" class="grails-logo"/>--}%
        </div>
    </div>

    <div id="content" role="main">
        %{--<section class="row colset-2-its">
            <h1>Welcome to Grails</h1>
            <div id="controllers" role="navigation">
                <h2>Available Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                        <li class="controller">
                            <g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link>
                        </li>
                    </g:each>
                </ul>
            </div>
        </section>--}%
    </div>

</body>
</html>
