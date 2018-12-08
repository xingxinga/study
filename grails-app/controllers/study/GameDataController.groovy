package study

import grails.plugin.springsecurity.annotation.Secured
import util.SaveFileService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class GameDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    FileService fileService
    SaveFileService saveFileService
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond GameData.list(params), model:[gameDataCount: GameData.count()]
    }

    def show(GameData gameData) {
        respond gameData
    }

    def create() {
        String fileType = params.get("fileType")
        GameData gameData = new GameData(params)
        gameData.typeFile = TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,fileType)
        respond gameData
    }

    @Transactional
    def save(GameData gameData) {
        if (gameData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (gameData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond gameData.errors, view:'create'
            return
        }

        def imgFile = request.getFile('imageFile')
        def file = gameData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureList'):request.getFile('dateFile')

        if(!(imgFile && !imgFile.empty && imgFile.filename)|| !(file && !file.empty && file.filename)){
            flash.message = '文件不存在'
            respond gameData, view:'create'
            return ""
        }
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,gameData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond gameData, view:'create'
            return
        }
        gameData.save()
        if(imgFile && !imgFile.empty && imgFile.filename ){
            gameData.image = saveFileService.saveFile(imgFile,gameData)
        }
        if(file && !file.empty && file.filename ){
            if(gameData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,gameData)
                List<String> pictureList = new ArrayList<String>()
                pictureList.add(imagePath)
                gameData.pictureList = pictureList
            }else{
                gameData.file = saveFileService.saveFile(file,gameData)
            }
        }
        gameData.save flush:true
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'gameData.label', default: 'GameData'), gameData.title])
                redirect action: "index"
            }
            '*' { respond gameData, [status: CREATED] }
        }
    }

    def edit(GameData gameData) {
        respond gameData
    }

    /*@Transactional
    def update(GameData gameData) {
        if (gameData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (gameData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond gameData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        if(imgFile && !imgFile.empty && imgFile.filename ){
            gameData.image = saveFileService.saveFile(imgFile,gameData)
        }
        gameData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'gameData.label', default: 'GameData'), gameData.id])
                redirect action: "index"
            }
            '*'{ respond gameData, [status: OK] }
        }
    }*/

    @Transactional
    def updateAddFile(GameData gameData) {
        if (gameData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (gameData.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond gameData.errors, view:'edit'
            return
        }
        def imgFile = request.getFile('imageFile')
        def file = gameData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)?request.getFile('pictureFile'):request.getFile('file')
        String imgMessage = fileService.checkFile(imgFile,TypeKeyValue.fileTypeImage)
        String fileMessage = fileService.checkFile(file,gameData.typeFile.ke)
        if(imgMessage!=null||fileMessage!=null){
            flash.message = ''
            flash.message +=  imgMessage!=null?imgMessage:''
            flash.message +=  fileMessage!=null?fileMessage:''
            transactionStatus.setRollbackOnly()
            respond gameData, view:'create'
            return
        }
        if(imgFile && !imgFile.empty && imgFile.filename ){
            gameData.image = saveFileService.saveFile(imgFile,gameData)
        }
        if(file && !file.empty && file.filename ){
            if(gameData.typeFile.ke.equals(TypeKeyValue.fileTypeImage)){
                String imagePath = saveFileService.saveFile(file,gameData)
                List<String> pictureList = gameData.pictureList
                pictureList.add(imagePath)
                gameData.pictureList = pictureList
            }else{
                gameData.file = saveFileService.saveFile(file,gameData)
            }
        }
        gameData.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'gameData.label', default: 'GameData'), gameData.title])
                redirect action: "edit",id:gameData.id
            }
            '*'{ respond gameData, [status: OK] }
        }
    }

    def listDelete(GameData gameData) {
        if (gameData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        gameData.delete flush:true
        redirect action:"index", method:"GET"
    }
    
    @Transactional
    def delete(GameData gameData) {

        if (gameData == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        gameData.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'gameData.label', default: 'GameData'), gameData.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'gameData.label', default: 'GameData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
