package study

import com.sys.Role
import com.sys.User
import com.sys.UserRole

import java.util.regex.Pattern

class BootStrap {
    public static String imagesPath
    public static String separator

    public static List<String> imageSuffix
    public static List<String> videoSuffix

    public static String defaultUserPassword = "000000"

    def init = { servletContext ->

        String osName = System.getProperty( "os.name" );
        if( Pattern.matches( "Linux.*", osName ) ) {
            imagesPath = "/study/studyFile/"
            separator = "/"
        } else if( Pattern.matches( "Windows.*", osName ) ) {
            imagesPath = "D:/study/"
            separator = "/"
        }
        imageSuffix = new ArrayList<String>()
        videoSuffix = new ArrayList<String>()
        imageSuffix.add("jpeg")
        imageSuffix.add("png")
        imageSuffix.add("gif")
        imageSuffix.add("bmp")
        imageSuffix.add("jpg")
        videoSuffix.add("mp4")
        videoSuffix.add("avi")
        videoSuffix.add("rmvb")
        videoSuffix.add("flash")
        videoSuffix.add("mkv")
        videoSuffix.add("flv")
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypePhonics)){
            new TypeKeyValue(type:TypeKeyValue.menuType,ke:TypeKeyValue.menuTypePhonics,value:"自然拼读").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypePronunciation)){
            new TypeKeyValue(type:TypeKeyValue.menuType,ke:TypeKeyValue.menuTypePronunciation,value:"发音训练").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypeTopics)){
            new TypeKeyValue(type:TypeKeyValue.menuType,ke:TypeKeyValue.menuTypeTopics,value:"口语主题").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypeAbilities)){
            new TypeKeyValue(type:TypeKeyValue.menuType,ke:TypeKeyValue.menuTypeAbilities,value:"综合能力").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypeFunny)){
            new TypeKeyValue(type:TypeKeyValue.menuType,ke:TypeKeyValue.menuTypeFunny,value:"趣味英语").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.menuType,TypeKeyValue.menuTypeGame)){
            new TypeKeyValue(type:TypeKeyValue.menuType,ke:TypeKeyValue.menuTypeGame,value:"疯狂游戏").save()
        }


        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,TypeKeyValue.fileTypeImage)){
            new TypeKeyValue(type:TypeKeyValue.fileType,ke:TypeKeyValue.fileTypeImage,value:"图片").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,TypeKeyValue.fileTypeVideo)){
            new TypeKeyValue(type:TypeKeyValue.fileType,ke:TypeKeyValue.fileTypeVideo,value:"视频").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,TypeKeyValue.fileTypePPT)){
            new TypeKeyValue(type:TypeKeyValue.fileType,ke:TypeKeyValue.fileTypePPT,value:"PPT").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.fileType,TypeKeyValue.fileTypeWord)){
            new TypeKeyValue(type:TypeKeyValue.fileType,ke:TypeKeyValue.fileTypeWord,value:"Word文档").save()
        }


        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.regionType,TypeKeyValue.regionTypeStudy)){
            new TypeKeyValue(type:TypeKeyValue.regionType,ke:TypeKeyValue.regionTypeStudy,value:"学习区").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.regionType,TypeKeyValue.regionTypeExercise)){
            new TypeKeyValue(type:TypeKeyValue.regionType,ke:TypeKeyValue.regionTypeExercise,value:"练习区").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.regionType,TypeKeyValue.regionTypeHearing)){
            new TypeKeyValue(type:TypeKeyValue.regionType,ke:TypeKeyValue.regionTypeHearing,value:"听力区").save()
        }

        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.ageType,TypeKeyValue.ageTypeYoung)){
            new TypeKeyValue(type:TypeKeyValue.ageType,ke:TypeKeyValue.ageTypeYoung,value:"少儿").save()
        }
        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.ageType,TypeKeyValue.ageTypeAdult)){
            new TypeKeyValue(type:TypeKeyValue.ageType,ke:TypeKeyValue.ageTypeAdult,value:"成年").save()
        }

        if(!TypeKeyValue.findByTypeAndKe(TypeKeyValue.addressTyeo,TypeKeyValue.localAddress)){
            new TypeKeyValue(type:TypeKeyValue.addressTyeo,ke:TypeKeyValue.localAddress,value:"https://herongnizi.cn").save()
        }

        if(Notice.findAll().size()==0){
            new Notice(title:"123",content:"123").save()
        }

        if(User.count() == 0){
            def adminRole = new Role(authority: 'ROLE_ADMIN').save()

            def vipRole = new Role(authority: 'ROLE_VIP').save()

            def userRole = new Role(authority: 'ROLE_USER').save()

            def testUser = new User(username: 'admin', password: 'admin',name:'系统管理员',vip:true,openId:'0').save()

            UserRole.create testUser, adminRole

            UserRole.create testUser, vipRole

            UserRole.create testUser, userRole

        }
    }
    def destroy = {
    }
}
