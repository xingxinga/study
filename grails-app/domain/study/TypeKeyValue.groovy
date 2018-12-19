package study

class TypeKeyValue {

    public static String menuType = "menuType"
    public static String fileType = "fileType"
    public static String regionType = "regionType"
    public static String ageType = "ageType"

    public static String menuTypePhonics = "1"//自然拼读
    public static String menuTypePronunciation = "2"//发音训练
    public static String menuTypeTopics = "3"//口语主题
    public static String menuTypeAbilities = "4"//综合能力
    public static String menuTypeFunny = "5"//趣味英语
    public static String menuTypeGame = "6"//疯狂游戏

    public static String fileTypeImage = "1"//图片
    public static String fileTypeVideo = "2"//视频
    public static String fileTypePPT = "3"//PPT
    public static String fileTypeWord = "4"//Word文档

    public static String regionTypeStudy = "1" // 学习区
    public static String regionTypeExercise = "2"//练习区
    public static String regionTypeHearing = "3"//听力区

    public static String ageTypeYoung = "1" // 少儿
    public static String ageTypeAdult = "2"// 成年

    public static String addressTyeo = "address"
    public static String localAddress = "1"

    String type
    String ke
    String value
    static constraints = {
    }
}
