package study

import grails.plugin.springsecurity.annotation.Secured
import util.SaveFileService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class FunnyDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    FileService fileService
    SaveFileService saveFileService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond FunnyData.list(params), model:[funnyDataCount: FunnyData.count()]
    }

    def show(FunnyData funnyData) {
        respond funnyData
    }

    def create() {
        String fileType = params.get("fileType")
        FunnyData funnyData = new FunnyData(params)
        funnyData.typeFile = TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,fileType)
        respond funnyData
    }

    @Transactional
    def save(FunnyData funnyData) {
        if (funnyData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (funnyData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond funnyData.errors, view:'create'
            return
        }

        def imgFile = request.getFile('imageFile')
        def file = funnyData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureList'):request.getFile('dateFile')

        if(!(imgFile && !imgFile.empty && imgFile.filename)|| !(file && !file.empty && file.filename)){
            flash.message = '文件不存在'
            respond funnyData, view:'create'
            return ""
        }
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,funnyData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond funnyData, view:'create'
            return
        }
        funnyData.save()
        if(imgFile && !imgFile.empty && imgFile.filename ){
            funnyData.image = saveFileService.saveFile(imgFile,funnyData)
        }
        if(file && !file.empty && file.filename ){
            if(funnyData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,funnyData)
                List<String> pictureList = new ArrayList<String>()
                pictureList.add(imagePath)
                funnyData.pictureList = pictureList
            }else{
                funnyData.file = saveFileService.saveFile(file,funnyData)
            }
        }
        funnyData.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'funnyData.label', default: 'FunnyData'), funnyData.title])
                redirect action: "index"
            }
            '*' { respond funnyData, [status: CREATED] }
        }
    }

    def edit(FunnyData funnyData) {
        respond funnyData
    }

    /*@Transactional
    def update(FunnyData funnyData) {
        if (funnyData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (funnyData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond funnyData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        if(imgFile && !imgFile.empty && imgFile.filename ){
            funnyData.image = saveFileService.saveFile(imgFile,funnyData)
        }
        funnyData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'funnyData.label', default: 'FunnyData'), funnyData.id])
                redirect action: "index"
            }
            '*'{ respond funnyData, [status: OK] }
        }
    }*/

    @Transactional
    def updateAddFile(FunnyData funnyData) {
        if (funnyData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (funnyData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond funnyData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        def file = funnyData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureFile'):request.getFile('file')
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,funnyData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond funnyData, view:'create'
            return
        }
        if(imgFile && !imgFile.empty && imgFile.filename ){
            funnyData.image = saveFileService.saveFile(imgFile,funnyData)
        }
        if(file && !file.empty && file.filename ){
            if(funnyData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,funnyData)
                List<String> pictureList = funnyData.pictureList
                pictureList.add(imagePath)
                funnyData.pictureList = pictureList
            }else{
                funnyData.file = saveFileService.saveFile(file,funnyData)
            }
        }
        funnyData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'funnyData.label', default: 'FunnyData'), funnyData.title])
                redirect action: "edit",id:funnyData.id
            }
            '*'{ respond funnyData, [status: OK] }
        }
    }

    def listDelete(FunnyData funnyData) {
        if (funnyData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        funnyData.delete flush:true
        redirect action:"index", method:"GET"
    }

    @Transactional
    def delete(FunnyData funnyData) {

        if (funnyData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        funnyData.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'funnyData.label', default: 'FunnyData'), funnyData.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'funnyData.label', default: 'FunnyData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
