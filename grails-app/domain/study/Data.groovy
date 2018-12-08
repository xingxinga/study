package study

class Data {

    TypeKeyValue typeDate //模块数据类型
    TypeKeyValue typeFile //文件类型
    TypeKeyValue typeRegion //区域划分
    String title //标题
    String image //封面图片
    Integer scan //浏览次数
    boolean vip //是否VIP

    String file //数据文件
    List<String> pictureList //图片集合

    Date dateCreated //创建日志 由系统维护
    Date lastUpdated //最后更新日期 由系统维护

    static constraints = {
        pictureList nullable: true
        file nullable: true
    }


}
