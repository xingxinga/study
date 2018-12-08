package util

import grails.gorm.transactions.Transactional
import study.BootStrap

@Transactional
class SaveFileService {

    def boolean serviceMethod(def file,String path) {
        if(!file||path.isEmpty()){
            return false
        }
        file.transferTo(new File(path))
        return true
    }

    def String saveFile(def file,def sort){
        Date d = new Date()
        def date = d.format("yyyyMMddHHmmss")
        String fen = BootStrap.separator
        String path = BootStrap.imagesPath+sort.getClass().name+fen+sort.id+fen+date+fen
        File filePath =new File(path)
        if  (!filePath .exists()  && !filePath .isDirectory())
        {
            filePath .mkdirs();
        }
        path+=file.filename
        if(serviceMethod(file,path)){
            path
        }else{
            null
        }
    }

    def String suffixFile(String filename){
        String[] imageArray = filename.split("\\.")
        return imageArray[imageArray.length-1]
    }
}
