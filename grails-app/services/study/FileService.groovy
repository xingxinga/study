package study

import grails.gorm.transactions.Transactional
import util.SaveFileService

@Transactional
class FileService {

    SaveFileService saveFileService

    def String checkFile(def file,String fileType){
        if(!(file && !file.empty && file.filename) ){
            return null
        }
        String suffixName = saveFileService.suffixFile(file.filename)
        String result = null;
        if(fileType.equals(TypeKeyValue.fileTypeImage)){//图片
            result = BootStrap.imageSuffix.contains(suffixName.toLowerCase())?null:"图片文件格式错误"
        }else if(fileType.equals(TypeKeyValue.fileTypeVideo)){//视频
            result = BootStrap.videoSuffix.contains(suffixName.toLowerCase())?null:"视频文件格式错误"
        }else if(fileType.equals(TypeKeyValue.fileTypePPT)){//
            result = "ppt,pptx".contains(suffixName.toLowerCase())?null:"PPT文件格式错误"
        }else if(fileType.equals(TypeKeyValue.fileTypeWord)){//
            result = "doc,docx".contains(suffixName.toLowerCase())?null:"word文件格式错误"
        }
        return result
    }
}
