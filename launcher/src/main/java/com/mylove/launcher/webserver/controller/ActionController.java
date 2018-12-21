package com.mylove.launcher.webserver.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mylove.launcher.bean.Contanst;
import com.mylove.launcher.bean.StyleBean;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.component.LauncherComponent;
import com.mylove.launcher.event.HttpEvent;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.bean.Banner;
import com.mylove.module_base.bean.Element;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_base.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hotapk.fhttpserver.FHttpManager;
import cn.hotapk.fhttpserver.NanoHTTPD;
import cn.hotapk.fhttpserver.annotation.RequestMapping;
import cn.hotapk.fhttpserver.annotation.RequestParam;
import cn.hotapk.fhttpserver.utils.FFileUtils;
import cn.hotapk.fhttpserver.utils.FStaticResUtils;

/**
 * Created by Administrator on 2018/8/14.
 */

public class ActionController {

    private static LauncherApi launcherApi;

    public void init(){
        if (launcherApi == null){
            System.out.println("创建 launcherApi .........................");
            LauncherComponent launcherComponent = DaggerLauncherComponent.builder()
                    .applicationModule(new ApplicationModule(BaseApplication.getAppContext()))
                    .launcherModule(new LauncherModule()).build();

            launcherApi = launcherComponent.getLauncherApi();

            FileUtils.createOrExistsDir(Contanst.APP_DIR);
        }
    }

    //样式
    @RequestMapping("action")
    public NanoHTTPD.Response action(@RequestParam("style") String style, @RequestParam("fragment") String fragment) {
        HttpEvent httpEvent = new HttpEvent();
        httpEvent.setEvent(HttpEvent.CHANGE_STYLE);

        StyleBean styleBean = new StyleBean();
        styleBean.setStyle(style);
        styleBean.setFragment(fragment);
        httpEvent.setObj(styleBean);

        EventBus.getDefault().post(httpEvent);
        return setResponse("action","text/html");
    }

    //上传
    @RequestMapping(Contanst.SERVER_DIR+"/upload")
    public NanoHTTPD.Response upload(NanoHTTPD.IHTTPSession session) {
        init();
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            Map<String, String> parms = session.getParms();
            String tag = parms.get("tag");
            String type = parms.get("type");

            if(tag != null){
                String fileName = java.net.URLEncoder.encode(parms.get("file"),"UTF-8");
/*              if(type != null) {
                    String typeDir = Contanst.APP_DIR+type+File.separator;
                    FileUtils.createOrExistsDir(typeDir);
                    String uploadPath = typeDir + fileName ;
                    FFileUtils.copyFileTo(files.get("file"), uploadPath);
                    switch (type) {
                        case "banner":

                            break;
                        case "image":
                        case "video":

                            break;
                    }
                }
*/
                if(type != null){
                    String typeDir = Contanst.APP_DIR+type+File.separator;
                    FileUtils.createOrExistsDir(typeDir);
                    String uploadPath = typeDir + fileName ;
                    FFileUtils.copyFileTo(files.get("file"), uploadPath);

                    //生成链接并写入数据库
                    String resUrl = createUrl(Contanst.SERVER_DIR,type,fileName);

                    if("banner".equals(type)){
                        Banner banner = new Banner();
                        banner.setId(tag);
                        banner.setImage(resUrl);
                        launcherApi.saveBanner(banner);
                        HttpEvent httpEvent = new HttpEvent();
                        httpEvent.setEvent(HttpEvent.CHANGE_BANNER);
                        EventBus.getDefault().post(httpEvent);
                    }else{
                        Element element = new Element();
                        element.setTag(tag);

                        element.setType(type);
                        if("video".equals(type)){
                            element.setResurl(uploadPath);
                        }else{
                            element.setResurl(resUrl);
                        }
                        launcherApi.saveElement(element);
                        HttpEvent httpEvent = new HttpEvent();
                        httpEvent.setEvent(HttpEvent.CHANGE_ELEMENT);
                        EventBus.getDefault().post(httpEvent);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }

        return setResponse("UPLOAD","text/html");
    }


    //文件
    @RequestMapping(Contanst.SERVER_DIR+"/file")
    public NanoHTTPD.Response data(@RequestParam("name") String name){
        return fetchResponse("file",name,"application/octet-stream");
    }

    //图片
    @RequestMapping(Contanst.SERVER_DIR+"/image")
    public NanoHTTPD.Response image(@RequestParam("name") String name){
        return fetchResponse("image",name,"image/jpeg");
    }

    @RequestMapping(Contanst.SERVER_DIR+"/banner")
    public NanoHTTPD.Response imageBanner(@RequestParam("name") String name){
        return fetchResponse("banner",name,"image/jpeg");
    }

    @RequestMapping(Contanst.SERVER_DIR+"/net/video")
    public NanoHTTPD.Response netVideo(NanoHTTPD.IHTTPSession session){
        init();
        Map<String, String> bodys = new HashMap<>();
        try {
            session.parseBody(bodys);
            Map<String, String> parms = session.getParms();
            String tag = parms.get("tag");
            String type = parms.get("type");
            String url = parms.get("url");

            Element element = new Element();
            element.setTag(tag);
            element.setType(type);
            element.setResurl(url);
            launcherApi.saveElement(element);
            HttpEvent httpEvent = new HttpEvent();
            httpEvent.setEvent(HttpEvent.CHANGE_ELEMENT);
            EventBus.getDefault().post(httpEvent);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
        return setResponse("success","text/html");
    }

    //视频
    @RequestMapping(Contanst.SERVER_DIR+"/video")
    public NanoHTTPD.Response video(@RequestParam("name") String name){
        return fetchResponse("video",name,"video/mp4");
    }

    private NanoHTTPD.Response fetchResponse(String dir,String name,String mimeType) {
        init();
        NanoHTTPD.Response response = null;
        try {
            String filePath = dir + File.separator + java.net.URLEncoder.encode(name,"UTF-8");
            InputStream inputStream = fetchInputStream(filePath);
            response = NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, mimeType,inputStream); //data -- inputStream
            response.addHeader("Accept-Ranges", "bytes");
            long size = FileUtils.getFileLength(Contanst.APP_DIR+filePath);
            if("application/octet-stream".equals(mimeType)){
                response.addHeader("Content-Disposition", "attachment; filename="+name);
                response.addHeader("Content-Length", String.valueOf(size));
            }
            if("video/mp4".equals(mimeType)){
//                response.addHeader("Content-Length", String.valueOf(size));
//                response.addHeader("Cache-Control", "no-cache");
//                response.addHeader("Content-Type", "video/mp4");
            }

        } catch (Exception e) {
        }
        return response;
    }

    //数据
    @RequestMapping(Contanst.SERVER_DIR+"/banners")
    public NanoHTTPD.Response banners() {
        init();
        try{
            List<Banner> banners = launcherApi.fetchBanners();
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String con = gson.toJson(banners);
            return setResponse(con,"text/html");
        }catch (Exception e){

        }
        return null;
    }

    public static NanoHTTPD.Response setResponse(String res,String mimeType) {
        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, res);
    }

    public InputStream fetchInputStream(String name){
        try {
            InputStream inputStream = new FileInputStream(new File(Contanst.APP_DIR+name));
            return inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createUrl(String path,String type,String name){
        String resUrl  = String.format("%s"+File.separator+"%s"+File.separator+"%s?name=%s",Contanst.getServerPath("localhost")
                    ,path,type,name);
        return resUrl;
    }
}
