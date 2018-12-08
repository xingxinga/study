package study

import grails.plugin.springsecurity.annotation.Secured
import util.SaveFileService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class TopicsDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    FileService fileService
    SaveFileService saveFileService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TopicsData.list(params), model:[topicsDataCount: TopicsData.count()]
    }

    def show(TopicsData topicsData) {
        respond topicsData
    }

    def create() {
        String fileType = params.get("fileType")
        TopicsData topicsData = new TopicsData(params)
        topicsData.typeFile = TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,fileType)
        respond topicsData
    }

    @Transactional
    def save(TopicsData topicsData) {
        if (topicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (topicsData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topicsData.errors, view:'create'
            return
        }

        def imgFile = request.getFile('imageFile')
        def file = topicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureList'):request.getFile('dateFile')

        if(!(imgFile && !imgFile.empty && imgFile.filename)|| !(file && !file.empty && file.filename)){
            flash.message = '文件不存在'
            respond topicsData, view:'create'
            return ""
        }
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,topicsData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond topicsData, view:'create'
            return
        }
        topicsData.save()
        if(imgFile && !imgFile.empty && imgFile.filename ){
            topicsData.image = saveFileService.saveFile(imgFile,topicsData)
        }
        if(file && !file.empty && file.filename ){
            if(topicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,topicsData)
                List<String> pictureList = new ArrayList<String>()
                pictureList.add(imagePath)
                topicsData.pictureList = pictureList
            }else{
                topicsData.file = saveFileService.saveFile(file,topicsData)
            }
        }
        topicsData.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'topicsData.label', default: 'TopicsData'), topicsData.title])
                redirect action: "index"
            }
            '*' { respond topicsData, [status: CREATED] }
        }
    }

    def edit(TopicsData topicsData) {
        respond topicsData
    }

    /*@Transactional
    def update(TopicsData topicsData) {
        if (topicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (topicsData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topicsData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        if(imgFile && !imgFile.empty && imgFile.filename ){
            topicsData.image = saveFileService.saveFile(imgFile,topicsData)
        }
        topicsData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'topicsData.label', default: 'TopicsData'), topicsData.id])
                redirect action: "index"
            }
            '*'{ respond topicsData, [status: OK] }
        }
    }*/

    @Transactional
    def updateAddFile(TopicsData topicsData) {
        if (topicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (topicsData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topicsData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        def file = topicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureFile'):request.getFile('file')
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,topicsData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond topicsData, view:'create'
            return
        }
        if(imgFile && !imgFile.empty && imgFile.filename ){
            topicsData.image = saveFileService.saveFile(imgFile,topicsData)
        }
        if(file && !file.empty && file.filename ){
            if(topicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,topicsData)
                List<String> pictureList = topicsData.pictureList
                pictureList.add(imagePath)
                topicsData.pictureList = pictureList
            }else{
                topicsData.file = saveFileService.saveFile(file,topicsData)
            }
        }
        topicsData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'topicsData.label', default: 'TopicsData'), topicsData.title])
                redirect action: "edit",id:topicsData.id
            }
            '*'{ respond topicsData, [status: OK] }
        }
    }

    def listDelete(TopicsData topicsData) {
        if (topicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        topicsData.delete flush:true
        redirect action:"index", method:"GET"
    }

    @Transactional
    def delete(TopicsData topicsData) {

        if (topicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        topicsData.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'topicsData.label', default: 'TopicsData'), topicsData.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'topicsData.label', default: 'TopicsData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
