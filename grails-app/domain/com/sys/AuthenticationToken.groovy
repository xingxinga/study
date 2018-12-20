package com.sys

class AuthenticationToken {
    String tokenValue
    String username
    Date dateCreated
    Date lastUpdated

    def afterLoad() {
        def nowTime = new Date().getTime()
        def updatedTime = lastUpdated.getTime()
        if(nowTime-updatedTime>2*60*60*1000){
            username = null
            this.delete flush:true
        }
    }

    static constraints = {
    }
}
