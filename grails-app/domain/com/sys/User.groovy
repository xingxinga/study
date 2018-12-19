package com.sys

import com.weixin.WeiXinUser
import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.apache.commons.lang.RandomStringUtils
import study.BootStrap

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password

    WeiXinUser weiXinUser

    boolean vip = false

    Date dateCreated //创建日志 由系统维护
    Date lastUpdated //最后更新日期 由系统维护

    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        weiXinUser nullable: true
    }

    static mapping = {
	    password column: '`password`'
    }

    static User createUser(WeiXinUser weiXinUser){
        User user = new User()
        user.weiXinUser = weiXinUser
        user.username = weiXinUser.openId
        user.password = BootStrap.defaultUserPassword
        user.vip = false
        user.save flush:true
        def role = Role.findByAuthority("ROLE_USER")
        new UserRole(user:user,role:role).save flush:true
        return user
    }
}
