package club.juliy.community.provider;

import club.juliy.community.dto.AccessTokenDTO;
import club.juliy.community.dto.GithubUser;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * github第三方提供类
 */
@Component
public class GithubProvider {
    //拿到access_token
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string= response.body().string();
            /*System.out.println(string);*/
            //做一些切割字符串
            String s = string.split("&")[0];
            /*System.out.println(s);*/
            String token = s.split("=")[1];
            /*System.out.println(token);*/
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    //携带access_token去到github(然后再返回来的access_token里面就有携带用户信息了)此步骤为1.2.1.2
    public GithubUser getGithubUser(String accessToken){
        System.out.println("accessToken:"+accessToken);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //拿到string对象
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            /*System.out.println(githubUser.getName());*/
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
