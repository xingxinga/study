package study

import grails.plugin.springsecurity.annotation.Secured
import util.SaveFileService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class AbilitiesDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    FileService fileService
    SaveFileService saveFileService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AbilitiesData.list(params), model:[abilitiesDataCount: AbilitiesData.count()]
    }

    def show(AbilitiesData abilitiesData) {
        respond abilitiesData
    }

    def create() {
        String fileType = params.get("fileType")
        AbilitiesData abilitiesData = new AbilitiesData(params)
        abilitiesData.typeFile = TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,fileType)
        respond abilitiesData
    }

    @Transactional
    def save(AbilitiesData abilitiesData) {
        if (abilitiesData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (abilitiesData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond abilitiesData.errors, view:'create'
            return
        }

        def imgFile = request.getFile('imageFile')
        def file = abilitiesData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureList'):request.getFile('dateFile')

        if(!(imgFile && !imgFile.empty && imgFile.filename)|| !(file && !file.empty && file.filename)){
            flash.message = '文件不存在'
            respond abilitiesData, view:'create'
            return ""
        }
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,abilitiesData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond abilitiesData, view:'create'
            return
        }
        abilitiesData.save()
        if(imgFile && !imgFile.empty && imgFile.filename ){
            abilitiesData.image = saveFileService.saveFile(imgFile,abilitiesData)
        }
        if(file && !file.empty && file.filename ){
            if(abilitiesData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,abilitiesData)
                List<String> pictureList = new ArrayList<String>()
                pictureList.add(imagePath)
                abilitiesData.pictureList = pictureList
            }else{
                abilitiesData.file = saveFileService.saveFile(file,abilitiesData)
            }
        }
        abilitiesData.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'abilitiesData.label', default: 'AbilitiesData'), abilitiesData.title])
                redirect action: "index"
            }
            '*' { respond abilitiesData, [status: CREATED] }
        }
    }

    def edit(AbilitiesData abilitiesData) {
        respond abilitiesData
    }

    /*@Transactional
    def update(AbilitiesData abilitiesData) {
        if (abilitiesData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (abilitiesData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond abilitiesData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        if(imgFile && !imgFile.empty && imgFile.filename ){
            abilitiesData.image = saveFileService.saveFile(imgFile,abilitiesData)
        }
        abilitiesData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'abilitiesData.label', default: 'AbilitiesData'), abilitiesData.id])
                redirect action: "index"
            }
            '*'{ respond abilitiesData, [status: OK] }
        }
    }*/

    @Transactional
    def updateAddFile(AbilitiesData abilitiesData) {
        if (abilitiesData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (abilitiesData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond abilitiesData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        def file = abilitiesData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureFile'):request.getFile('file')
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,abilitiesData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond abilitiesData, view:'create'
            return
        }
        if(imgFile && !imgFile.empty && imgFile.filename ){
            abilitiesData.image = saveFileService.saveFile(imgFile,abilitiesData)
        }
        if(file && !file.empty && file.filename ){
            if(abilitiesData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,abilitiesData)
                List<String> pictureList = abilitiesData.pictureList
                pictureList.add(imagePath)
                abilitiesData.pictureList = pictureList
            }else{
                abilitiesData.file = saveFileService.saveFile(file,abilitiesData)
            }
        }
        abilitiesData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'abilitiesData.label', default: 'AbilitiesData'), abilitiesData.title])
                redirect action: "edit",id:abilitiesData.id
            }
            '*'{ respond abilitiesData, [status: OK] }
        }
    }

    def listDelete(AbilitiesData abilitiesData) {
        if (abilitiesData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        abilitiesData.delete flush:true
        redirect action:"index", method:"GET"
    }

    @Transactional
    def delete(AbilitiesData abilitiesData) {

        if (abilitiesData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        abilitiesData.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'abilitiesData.label', default: 'AbilitiesData'), abilitiesData.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'abilitiesData.label', default: 'AbilitiesData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
