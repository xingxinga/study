package study

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
@Secured(['ROLE_ADMIN'])
class NoticeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Notice.list(params), model:[noticeCount: Notice.count()]
    }



    def create() {
        respond new Notice(params)
    }

    @Transactional
    def save(Notice notice) {
        if (notice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (notice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond notice.errors, view:'create'
            return
        }

        notice.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'notice.label', default: 'Notice'), notice.id])
                redirect action: "index"
            }
            '*' { respond notice, [status: CREATED] }
        }
    }

    def edit(Notice notice) {
        respond notice
    }

    @Transactional
    def update(Notice notice) {
        if (notice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (notice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond notice.errors, view:'edit'
            return
        }

        notice.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = "修改成功"
                redirect action: "index"
            }
            '*'{ respond notice, [status: OK] }
        }
    }

    @Transactional
    def delete(Notice notice) {

        if (notice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        notice.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'notice.label', default: 'Notice'), notice.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'notice.label', default: 'Notice'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
