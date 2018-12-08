package util

import com.weixin.WeiXinUser
import grails.gorm.transactions.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import net.sf.json.JSON
import net.sf.json.JSONObject
import org.bouncycastle.jce.provider.BouncyCastleProvider

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.AlgorithmParameters
import java.security.Security
import org.codehaus.xfire.util.Base64;
@Transactional
class WeiXinService {

    String uri = "https://api.weixin.qq.com"

    //xing
    //String appid = "wxede71567af274753"
    //String secret = "55a1866e072f33e1c4cb44ecba10eb6c"


    String appid = "wx6b675cbe99a51355"
    String secret = "a30f6858bedba6a1f09c2a0d43c0a21b"

    def serviceMethod() {

    }

    def WeiXinUser getWeiXinUser(String code,String encryptedData,String iv){
        if(!code){
            return null
        }
        def openid,sessionKey
        def data
        String paths = 'sns/jscode2session'
        HTTPBuilder https = new HTTPBuilder(uri)
        def parames = [appid:appid,secret:secret,grant_type:"authorization_code",js_code:code]
        https.get(path:paths, contentType: ContentType.JSON, query:parames) { resp, reader ->;
            data  =  reader
            if(!data.errcode){
                openid = data.openid
                sessionKey = data.session_key
            }
        }
        JSONObject userJson = getUserInfo(encryptedData,sessionKey,iv);
        WeiXinUser user = new WeiXinUser();
        user.openId = userJson.get("openId")
        user.nickName = userJson.get("nickName")
        user.city = userJson.get("city")
        user.province = userJson.get("province")
        user.country = userJson.get("country")
        user.avatarUrl = userJson.get("avatarUrl")
        if(!user){
            return null
        }
        def oldUser = WeiXinUser.findByOpenId(user.openId)
        if(oldUser){
            oldUser.nickName = user.nickName
            oldUser.city = user.city
            oldUser.province = user.province
            oldUser.country = user.country
            oldUser.avatarUrl = user.avatarUrl
            oldUser.save flush:true
            return oldUser
        }else{
            user.save flush:true
            return user
        }
    }

    public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {

            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            def aa = keyByte.length
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


}
