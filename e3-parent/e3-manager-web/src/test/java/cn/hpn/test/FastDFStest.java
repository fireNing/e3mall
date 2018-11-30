package cn.hpn.test;

import cn.hpn.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDFStest {

    @Test
    public void  test(){
        try {
            ClientGlobal.init("D:\\JavaUtils\\workspace\\e3mall\\e3-parent\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageService = null;
            StorageClient storageClient = new StorageClient(trackerServer,storageService);
            String[] strings = storageClient.upload_file("D:\\Documents\\Pictures\\Saved Pictures\\1540602032824.jpg", "jpg", null);
            for (String string : strings) {
                System.out.println(string);
            }

        }catch (Exception e ){

        }
    }


    /**
     * 使用工具上传图片
     */
    @Test
    public void test2(){
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("D:\\JavaUtils\\workspace\\e3mall\\e3-parent\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
            String file = fastDFSClient.uploadFile("D:\\Documents\\Pictures\\Saved Pictures\\cart-empty.png");
            System.out.println(file);
        }catch (Exception e){

        }

    }
}
