package study

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import util.SaveFileService

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_ADMIN'])
class PhonicsDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    FileService fileService
    SaveFileService saveFileService
    def springSecurityService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
       def list =  PhonicsData.list(params)
        respond PhonicsData.list(params), model:[phonicsDataCount: PhonicsData.count()]
    }

    def show(PhonicsData phonicsData) {
        respond phonicsData
    }

    def create() {
        String fileType = params.get("fileType")
        PhonicsData phonicsData = new PhonicsData(params)
        phonicsData.typeFile = TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,fileType)
        respond phonicsData
    }

    @Transactional
    def save(PhonicsData phonicsData) {
        if (phonicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (phonicsData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond phonicsData.errors, view:'create'
            return
        }

        def imgFile = request.getFile('imageFile')
        def file = phonicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureList'):request.getFile('dateFile')

        if(!(imgFile && !imgFile.empty && imgFile.filename)|| !(file && !file.empty && file.filename)){
            flash.message = '文件不存在'
            respond phonicsData, view:'create'
            return ""
        }
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,phonicsData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond phonicsData, view:'create'
            return
        }
        phonicsData.save()
        if(imgFile && !imgFile.empty && imgFile.filename ){
            phonicsData.image = saveFileService.saveFile(imgFile,phonicsData)
        }
        if(file && !file.empty && file.filename ){
            if(phonicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,phonicsData)
                List<String> pictureList = new ArrayList<String>()
                pictureList.add(imagePath)
                phonicsData.pictureList = pictureList
            }else{
                phonicsData.file = saveFileService.saveFile(file,phonicsData)
            }
        }
        phonicsData.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'phonicsData.label', default: 'PhonicsData'), phonicsData.title])
                redirect action: "index"
            }
            '*' { respond phonicsData, [status: CREATED] }
        }
    }

    def edit(PhonicsData phonicsData) {
        respond phonicsData
    }


    @Transactional
    def updateAddFile(PhonicsData phonicsData) {
        if (phonicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (phonicsData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond phonicsData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        def file = phonicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureFile'):request.getFile('file')
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,phonicsData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond phonicsData, view:'create'
            return
        }
        if(imgFile && !imgFile.empty && imgFile.filename ){
            phonicsData.image = saveFileService.saveFile(imgFile,phonicsData)
        }
        if(file && !file.empty && file.filename ){
            if(phonicsData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,phonicsData)
                List<String> pictureList = phonicsData.pictureList
                pictureList.add(imagePath)
                phonicsData.pictureList = pictureList
            }else{
                phonicsData.file = saveFileService.saveFile(file,phonicsData)
            }
        }
        phonicsData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'phonicsData.label', default: 'PhonicsData'), phonicsData.title])
                redirect action: "edit",id:phonicsData.id
            }
            '*'{ respond phonicsData, [status: OK] }
        }
    }

    def listDelete(PhonicsData phonicsData) {
        if (phonicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        phonicsData.delete flush:true
        redirect action:"index", method:"GET"
    }

    @Transactional
    def delete(PhonicsData phonicsData) {

        if (phonicsData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        phonicsData.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'phonicsData.label', default: 'PhonicsData'), phonicsData.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'phonicsData.label', default: 'PhonicsData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
