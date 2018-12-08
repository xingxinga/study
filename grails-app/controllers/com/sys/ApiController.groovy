package com.sys

import com.weixin.WeiXinUser
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import org.springframework.security.access.annotation.Secured
import study.BootStrap
import study.Comment
import study.Data
import study.Notice
import study.TypeKeyValue
import util.WeiXinService

@Secured('ROLE_USER')
class ApiController {
    def springSecurityService
    def WeiXinService weiXinService

    @Secured('permitAll')
    def getTypeKeyValue(){
        def list = TypeKeyValue.findAll()
        respond status:"200",data:list
    }

    @Secured('permitAll')
    def getToken(String code,String encryptedData,String iv){
        WeiXinUser weiXinUser =  weiXinService.getWeiXinUser(code,encryptedData,iv)
        User user = null
        if(!weiXinUser.user){
            user = User.createUser(weiXinUser)
            weiXinUser.user = user
            weiXinUser.save flush:true
        }else{
            user = weiXinUser.user
        }
        String uri = TypeKeyValue.findByTypeAndKe(TypeKeyValue.addressTyeo,TypeKeyValue.localAddress).value
        def parames = [username:user.username,password:BootStrap.defaultUserPassword]
        String paths = '/study/api/login'
        String token = null
        HTTPBuilder https = new HTTPBuilder(uri)
        https.post(path:paths, contentType: ContentType.JSON, query:parames) { resp, reader ->;
            def data  =  reader
            token = data.access_token
            if(!data.errcode){
            }
        }
        respond status:"200",token:token
    }

    /**
     * 获取数据列表
     */
    def listData(String typeId){
        TypeKeyValue typeDate = TypeKeyValue.get(typeId)
        def list = Data.findAllByTypeDate(typeDate)
        list.each {
            it.pictureList = null;
            it.file = null;
        }
        respond status:"200",data:list
    }
    /**
     * 获取数据详情
     */
    def getData(String id){
        def data = Data.get(id)
        def user = springSecurityService.currentUser
        if(data){
            if(data.vip&&!user.vip){
                respond status:"403"
            }else{
                respond status:"200",data:data
            }
        }else{
            respond status:"404"
        }

    }

    def creatComment(String id,String content){
        def data = Data.get(id)
        if(!data||!content){
            respond status:"404", message:"null data"
            return
        }
        Comment comment = new Comment()
        comment.content = content
        comment.data = data
        comment.user = springSecurityService.currentUser
        comment.save flush:true
        respond status:"200", message:"Success"
    }

    def getNotice(){
        def data
        def list = Notice.findAll()
        if(list){
            if (list.size()==1){
                data = list.get(0)
            }
        }
        respond status:"200",data:data
    }

    def listComment(String id){
        def data = Data.get(id)
        if(!data){
            respond status:"404", message:"null data"
            return
        }
        def list = Comment.findAllByData(data)
        def listData = []
        list.each {
            def userInfo =  WeiXinUser.findByUser(it.user)
            if(userInfo){
                def dataMap = ["content":it.content,"dateCreated":it.dateCreated,"userName":userInfo.nickname,"headImgUrl":userInfo.headimgurl]
                listData.add(dataMap)
            }
        }
        respond status:"200", data:listData
    }

}
