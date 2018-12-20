import com.sys.UserPasswordEncoderListener
import util.MySecurityEventListenerService

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    mySecurityEventListenerService(MySecurityEventListenerService)
}
