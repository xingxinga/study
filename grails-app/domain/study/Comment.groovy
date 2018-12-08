package study

import com.sys.User

class Comment {
    static belongsTo = [data: Data]
    String content //评论内容
    User user //创建用户
    Date dateCreated //创建时间
    Date lastUpdated //最后修改时间
    static constraints = {
    }
}
