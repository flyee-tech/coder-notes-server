package com.peiel.notes;

import com.peiel.notes.automation.mapper.ArticleMapper;
import com.peiel.notes.automation.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

@SpringBootTest
class CoderNotesApplicationTests {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void test() throws FileNotFoundException {


        String path = "/Users/peiel/Documents/docs/";        //要遍历的路径
        File file = new File(path);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                    //遍历File[]数组
            if (!f.isDirectory()) {        //若非目录(即文件)，则打印
                System.out.println(f);
                String s = readToString(f.toString());
                long createDate = readCreateDate(f.toString());
//                System.out.println(s);
                System.out.println(new Date(createDate));
                String name = "";
                try {
                    name = s.substring(0, s.indexOf("\n")).replace("#", "").trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(name);
                articleMapper.insert(Article.builder()
                        .name(name)
                        .content(s)
                        .createdTime(new Date(createDate))
                        .type(2)
                        .isPublic(0)
                        .build());
            }
        }

//		String s = readToString("/Users/peiel/Documents/docs/15523597713496.md");
//		long createDate = readCreateDate("/Users/peiel/Documents/docs/15523597713496.md");
//		System.out.println(s);
//		System.out.println(new Date(createDate));
    }

    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public long readCreateDate(String fileName) {
        BasicFileAttributes bAttributes = null;
        File file = new File(fileName);
        try {
            bAttributes = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 修改时间
        return bAttributes.creationTime().toMillis();
    }

}
