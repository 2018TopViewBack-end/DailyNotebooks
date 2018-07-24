package com.zhangmiaoxin.www.util;



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Pic {
    public static Map<String,String> uploadPic(HttpServletRequest request, Map<String,String> paramMap){
        //检测是否为多媒体上传
        if(!ServletFileUpload.isMultipartContent(request)){
            //如果不是则停止上传,将map原封不动返回
            return paramMap;
        }

        //配置上传参数
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //设置内存临界值，超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(Constants.MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        //设置最大文件上传值
        upload.setFileSizeMax(Constants.MAX_FILE_SIZE);
        //设置最大请求值（包含文件和表单数据）
        upload.setSizeMax(Constants.MAX_REQUEST_SIZE);

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + Constants.UPLOAD_DIRECTORY;

        File newPhoto=new File(uploadPath);
        if(!newPhoto.exists()){
            newPhoto.mkdir();
        }
        String fileName="";
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem fileItem : formItems) {
                        if (!fileItem.isFormField()) {     //如果是图片上传域
                        //将文件名字改成当前时间，防止重复
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        fileName = new File(fileItem.getName()).getName();
                        int index = fileName.indexOf(".");

                        if (index == -1) {  //用户没有上传新头像,开始新循环，以获取到其他的值
                            continue;
                        }
                        fileName = fileName.replace(fileName.substring(0, index), dateFormat.format(date));
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        fileItem.write(storeFile);
                    }else {
                        for(Map.Entry<String,String> entry:paramMap.entrySet()){
                            String key=entry.getKey();
                            if(key.equals(fileItem.getFieldName())){
                                String value=fileItem.getString("UTF-8");
                                paramMap.put(key,value);
                                break;
                            }
                        }
                    }
                }
                if(!fileName.equals("")){
                    paramMap.put("pic",fileName);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return paramMap;
    }
}
