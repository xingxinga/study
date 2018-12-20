package util

import com.sys.AuthenticationToken
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.rest.token.AccessToken
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent

@Transactional
class MySecurityEventListenerService implements ApplicationListener<AuthenticationSuccessEvent> {

    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        if(event.getSource() instanceof AccessToken){
            AccessToken source = (AccessToken)event.getSource();
            def bb = AuthenticationToken.findByTokenValue(source.getAccessToken())
            bb.lastUpdated = new Date()
            bb.save(flush:true)
        }
        // The access token is a delegate of the event, so you have access to an instance of `grails.plugin.springsecurity.rest.token.AccessToken`
    }
}
