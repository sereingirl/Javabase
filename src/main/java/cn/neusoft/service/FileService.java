package cn.neusoft.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.neusoft.constants.Constants;
import cn.neusoft.entity.Files;
import cn.neusoft.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.neusoft.mapper.FileMapper;
import com.sun.xml.fastinfoset.stax.events.Util;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Service
public class FileService extends ServiceImpl<FileMapper, Files> {
    @Resource
    private FileMapper fileMapper;

    public String upload(MultipartFile uploadFile){
        String originalFilename = uploadFile.getOriginalFilename(); //文件原始名字
        String type = originalFilename.substring(originalFilename.lastIndexOf(".")+1);  //文件后缀
        long size = uploadFile.getSize() / 1024; //文件大小，单位kb
        String url;
        Files files = new Files();  //用于保存于数据库的实体类Files
        files.setName(originalFilename);
        files.setSize(size);
        files.setType(type);

        //通过md5判断文件是否已经存在，防止在服务器存储相同文件
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String md5 = SecureUtil.md5(inputStream);
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> dbFilesList = fileMapper.selectList(queryWrapper);
        if(dbFilesList.size() != 0){
            //数据库中已有该md5，则拷贝其url
            url = dbFilesList.get(0) .getUrl();
            files.setUrl(url);
        }else{
            //文件不存在，则保存文件
            File folder = new File(Constants.fileFolderPath);
            if(!folder.exists()){
                folder.mkdir();
            }
            String folderPath = folder.getAbsolutePath()+"/";   //文件存储文件夹的位置
            System.out.println("文件存储地址"+folderPath);
            //将文件保存为UUID的名字，通过uuid生成url
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String finalFileName = uuid+"."+type;
            File targetFile = new File(folderPath + finalFileName);
            try {
                uploadFile.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            url = "/file/"+finalFileName;
            files.setUrl(url);
        }
        files.setMd5(md5);

        fileMapper.insert(files);
        System.out.println("文件"+originalFilename+" "+url);
        return url;
    }

    //根据文件名下载文件
    public void download(String fileName, HttpServletResponse response){
        File file = new File(Constants.fileFolderPath+fileName);
        if(!file.exists()){
            throw new ServiceException(Constants.CODE_500,"文件不存在");
        }
        try {
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
            response.setContentType("application/octet-stream");
            os.write(FileUtil.readBytes(file));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int fakeDelete(int id){
        UpdateWrapper<Files> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("is_delete",true);
        return fileMapper.update(null,updateWrapper);
    }

    public IPage<Files> selectPage(int pageNum, int pageSize, String fileName) {
        IPage<Files> filesPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        if(!Util.isEmptyString(fileName)){
            filesQueryWrapper.like("name",fileName);
        }
        filesQueryWrapper.eq("is_delete",false);
        filesQueryWrapper.orderByDesc("id");
        return page(filesPage, filesQueryWrapper);
    }


    public int changeEnable(int id, boolean enable) {
        UpdateWrapper<Files> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id).set("enable",enable);
        return fileMapper.update(null, updateWrapper);
    }
}
