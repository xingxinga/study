package study

import grails.plugin.springsecurity.annotation.Secured
import util.SaveFileService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class PronunciationDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    FileService fileService
    SaveFileService saveFileService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond PronunciationData.list(params), model:[pronunciationDataCount: PronunciationData.count()]
    }

    def show(PronunciationData pronunciationData) {
        respond pronunciationData
    }

    def create() {
        String fileType = params.get("fileType")
        PronunciationData pronunciationData = new PronunciationData(params)
        pronunciationData.typeFile = TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,fileType)
        respond pronunciationData
    }

    @Transactional
    def save(PronunciationData pronunciationData) {
        if (pronunciationData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (pronunciationData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond pronunciationData.errors, view:'create'
            return
        }

        def imgFile = request.getFile('imageFile')
        def file = pronunciationData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureList'):request.getFile('dateFile')

        if(!(imgFile && !imgFile.empty && imgFile.filename)|| !(file && !file.empty && file.filename)){
            flash.message = '文件不存在'
            respond pronunciationData, view:'create'
            return ""
        }
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,pronunciationData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond pronunciationData, view:'create'
            return
        }
        pronunciationData.save()
        if(imgFile && !imgFile.empty && imgFile.filename ){
            pronunciationData.image = saveFileService.saveFile(imgFile,pronunciationData)
        }
        if(file && !file.empty && file.filename ){
            if(pronunciationData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,pronunciationData)
                List<String> pictureList = new ArrayList<String>()
                pictureList.add(imagePath)
                pronunciationData.pictureList = pictureList
            }else{
                pronunciationData.file = saveFileService.saveFile(file,pronunciationData)
            }
        }
        pronunciationData.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pronunciationData.label', default: 'PronunciationData'), pronunciationData.title])
                redirect action: "index"
            }
            '*' { respond pronunciationData, [status: CREATED] }
        }
    }

    def edit(PronunciationData pronunciationData) {
        respond pronunciationData
    }

    /*@Transactional
    def update(PronunciationData pronunciationData) {
        if (pronunciationData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (pronunciationData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond pronunciationData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        if(imgFile && !imgFile.empty && imgFile.filename ){
            pronunciationData.image = saveFileService.saveFile(imgFile,pronunciationData)
        }
        pronunciationData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'pronunciationData.label', default: 'PronunciationData'), pronunciationData.id])
                redirect action: "index"
            }
            '*'{ respond pronunciationData, [status: OK] }
        }
    }*/

    @Transactional
    def updateAddFile(PronunciationData pronunciationData) {
        if (pronunciationData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (pronunciationData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond pronunciationData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        def file = pronunciationData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureFile'):request.getFile('file')
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,pronunciationData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond pronunciationData, view:'create'
            return
        }
        if(imgFile && !imgFile.empty && imgFile.filename ){
            pronunciationData.image = saveFileService.saveFile(imgFile,pronunciationData)
        }
        if(file && !file.empty && file.filename ){
            if(pronunciationData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,pronunciationData)
                List<String> pictureList = pronunciationData.pictureList
                pictureList.add(imagePath)
                pronunciationData.pictureList = pictureList
            }else{
                pronunciationData.file = saveFileService.saveFile(file,pronunciationData)
            }
        }
        pronunciationData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'pronunciationData.label', default: 'PronunciationData'), pronunciationData.title])
                redirect action: "edit",id:pronunciationData.id
            }
            '*'{ respond pronunciationData, [status: OK] }
        }
    }

    def listDelete(PronunciationData pronunciationData) {
        if (pronunciationData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        pronunciationData.delete flush:true
        redirect action:"index", method:"GET"
    }

    @Transactional
    def delete(PronunciationData pronunciationData) {

        if (pronunciationData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        pronunciationData.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'pronunciationData.label', default: 'PronunciationData'), pronunciationData.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pronunciationData.label', default: 'PronunciationData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
