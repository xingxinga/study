package study

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
@Secured(['ROLE_ADMIN'])
class DataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Data.list(params), model:[dataCount: Data.count()]
    }

    def show(Data data) {
        respond data
    }

    def create() {
        respond new Data(params)
    }

    @Transactional
    def save(Data data) {
        if (data == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (data.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond data.errors, view:'create'
            return
        }

        data.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'data.label', default: 'Data'), data.id])
                redirect data
            }
            '*' { respond data, [status: CREATED] }
        }
    }

    def edit(Data data) {
        respond data
    }

    @Transactional
    def update(Data data) {
        if (data == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (data.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond data.errors, view:'edit'
            return
        }

        data.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'data.label', default: 'Data'), data.id])
                redirect data
            }
            '*'{ respond data, [status: OK] }
        }
    }

    @Transactional
    def delete(Data data) {

        if (data == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        data.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'data.label', default: 'Data'), data.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'data.label', default: 'Data'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Secured(['ROLE_USER'])
    def img(String path){
        File file = new File(path)
        if(file.exists()){
            response.outputStream << file.getBytes()
        }else{
            notFound()
            return
        }
    }

    @Secured(['ROLE_USER'])
    def file(String path){
        File file = new File(path)
        String[] imageArray = path.split(BootStrap.separator)
        if(file.exists()){
            String fileName = imageArray[imageArray.length-1]
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;   filename="+new String( fileName.getBytes("gb2312"), "ISO8859-1" ))
            response.outputStream << file.getBytes()
        }else{
            notFound()
            return
        }
    }

    @Secured(['ROLE_USER'])
    def removeImg(String path,String id){
        Data data = Data.get(id)
        List<String> pictureList = data.pictureList
        Iterator<String> it = pictureList.iterator();
        while(it.hasNext()){
            String str = (String)it.next();
            if(path.equals(str)){
                it.remove();
            }
        }
        data.pictureList = pictureList
        data.save flush:true
        flash.message = "图片移除成功"
        if(data instanceof GameData){
            redirect controller:"gameData",action: "edit",id:id
        }else if(data instanceof FunnyData){
            redirect controller:"funnyData",action: "edit",id:id
        }else if(data instanceof AbilitiesData){
            redirect controller:"abilitiesData",action: "edit",id:id
        }else if(data instanceof PhonicsData){
            redirect controller:"phonicsData",action: "edit",id:id
        }else if(data instanceof PronunciationData){
            redirect controller:"pronunciationData",action: "edit",id:id
        }else if(data instanceof TopicsData){
            redirect controller:"topicsData",action: "edit",id:id
        }
        return
    }
}
