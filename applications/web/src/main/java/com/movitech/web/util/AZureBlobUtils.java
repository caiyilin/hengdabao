package com.movitech.web.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.StorageUri;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class AZureBlobUtils {
    private static final String blobCatalog = PropertiesUtil.getValue("blobCatalog");
    private static final String blobAccount = PropertiesUtil.getValue("blobAccount");
    private static final String blobKey = PropertiesUtil.getValue("blobKey");
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" 
			+ "AccountName="+blobAccount
			+ ";AccountKey="+blobKey
			+ ";EndpointSuffix=core.chinacloudapi.cn";
	/**
	 * 上传图片
	 * @param fileName
	 * @param stream
	 * @return
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws IOException
	 */
	public static String uploadFile (String fileName,InputStream stream) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
	    return AZureBlobUtils.uploadFilePrivate(blobCatalog, fileName, stream);
	}

	/**
	 * 删除图片
	 * @param fileName
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws IOException
	 */
	public static void deleteFile(String fileName) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
	    AZureBlobUtils.deleteFilePrivate(blobCatalog, fileName);
	}
	
	/**
	 * 从url里面获取图片文件名称
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
        int start = url.lastIndexOf("/");
	    int end = url.lastIndexOf(".");
	    return url.substring(start + 1, end);
	}
	
	private static String uploadFilePrivate(String blobCatalog,String fileName,InputStream stream) throws InvalidKeyException, URISyntaxException, StorageException, IOException{

		CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient serviceClient = account.createCloudBlobClient();

		// Container name must be lower case.
		CloudBlobContainer container = serviceClient.getContainerReference(blobCatalog);
		container.createIfNotExists();

		// Upload an image file.
		CloudBlockBlob blob = container.getBlockBlobReference(fileName);
		blob.upload(stream, stream.available());
		
		StorageUri storageUri = blob.getStorageUri();
		return storageUri.getPrimaryUri().toString();
	}
	
	public static void downloadFile(String blobCatalog,String path,String fileName) throws InvalidKeyException, URISyntaxException, StorageException, IOException{

		CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient serviceClient = account.createCloudBlobClient();

		// Container name must be lower case.
		CloudBlobContainer container = serviceClient.getContainerReference(blobCatalog);
		container.createIfNotExists();

		// Download the image file.
		CloudBlockBlob blob = container.getBlockBlobReference(fileName);
		FileOutputStream out = new FileOutputStream(path);
		blob.download(out);
		out.close();
		///blob.getStorageUri().getPrimaryUri();--返回地址
	}
    
	private static void deleteFilePrivate(String blobCatalog,String fileName) throws InvalidKeyException, URISyntaxException, StorageException, IOException{

        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient serviceClient = account.createCloudBlobClient();

        // Container name must be lower case.
        CloudBlobContainer container = serviceClient.getContainerReference(blobCatalog);
        container.createIfNotExists();

        // delete the image file.
        CloudBlockBlob blob = container.getBlockBlobReference(fileName);
        blob.deleteIfExists();

    
    }
}
