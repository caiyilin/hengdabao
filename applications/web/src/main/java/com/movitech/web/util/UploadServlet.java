package com.movitech.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.movitech.mbox.common.utils.StringUtils;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

    public static final String DOT = ".";
    
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = "";
        String fileNamePath = "";
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		try {
			List fileList = upload.parseRequest(request);
			Iterator<FileItem> it = fileList.iterator();
			while (it.hasNext()) {
				FileItem item = it.next();
				if (!item.isFormField()) {
					String name = item.getName();
					if (name == null || name.trim().equals("")) {
						continue;
					}

					String fileExtension = this.getExtension(name);
                    fileName = UUID.randomUUID() + fileExtension;
					InputStream inputStream = null;
					try {
						inputStream =  item.getInputStream();
						fileNamePath = AZureBlobUtils.uploadFile(fileName,inputStream);
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						try {
							if(inputStream!=null){
								inputStream.close();
								inputStream = null;
							}
						} catch (IOException e) {
						}
					}
				}
			}
		} catch (Exception ex) {
			return;
		}
		// 文件显示全路径
		response.getWriter().print(fileNamePath);
	}
	

    /**
     * 获取扩展名
     * 
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT))
            return StringUtils.EMPTY;
        String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT));
        return StringUtils.trimToEmpty(ext);
    }

}