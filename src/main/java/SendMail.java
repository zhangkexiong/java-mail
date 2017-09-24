package main.java;

import com.sun.deploy.util.StringUtils;
import org.junit.Test;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @Author: Small Bear
 * @Description:
 * @Data:Create in 20:35 2017/9/23
 * @Modified By:Small Bear
 */
public class SendMail {
   public static void main(String args[])throws Exception{

       //创建环境
       Properties prop=new Properties();
       //表明是否认证信息,必须要有的属性
       prop.setProperty("mail.smtp.auth","true");
       //表明遵循的协议smtp,因此要导入stmp.jar
       prop.setProperty("mail.transport.protocol","smtp");
       Session session=Session.getInstance(prop);
       //表明要发送的信息体
       session.setDebug(true);
       Message msg=new MimeMessage(session);
       msg.setFrom(new InternetAddress("kexiongmail@sina.com"));
       msg.setSubject("测试邮件的发送");
       msg.setText("发送了一条邮件");
       //表示发送的渠道
       Transport transport=session.getTransport();
       transport.connect("smtp.sina.com",25,"kexiongmail","z829829");
       //信息发送
       transport.sendMessage(msg,new Address[]{new InternetAddress("1481935493@qq.com")});
       transport.close();
   }
   @Test
   public void test1()throws Exception{

       Properties prop=new Properties();
       //配置是否可以进行认证
       prop.setProperty("mail.smtp.auth","true");
       //设置协议
       prop.setProperty("mail.transport.protocol","smtp");
       //配置主机
       prop.setProperty("mail.host","smtp.sina.com");
       //用户认证
       Session session=Session.getDefaultInstance(prop, new Authenticator() {
           @Override
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication("kexiongmail","z829829");
           }
       });
       session.setDebug(true);
       Message msg=new MimeMessage(session);
       msg.setFrom(new InternetAddress("kexiongmail@sina.com"));
       msg.setSubject("测试邮件的发送");
       msg.setText("发送了第二封邮件");
       msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse("kexiongmail@sina.com," +
               "kexiongmail@sohu.com"));
       Transport.send(msg);
   }
   @Test
   public void test2()throws Exception{

       //配置是否可以进行认证
       Properties prop=new Properties();
       prop.setProperty("mail.smtp.auth","true");
       //设置协议
       prop.setProperty("mail.transport.protocol","smtp");
       //配置主机
       prop.setProperty("mail.host","smtp.sina.com");
       //用户认证
       Session session=Session.getDefaultInstance(prop, new Authenticator() {
           @Override
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication("kexiongmail","z829829");
           }
       });
       session.setDebug(true);
       MimeMessage msg=new MimeMessage(session);
       //表示内容
       Multipart msgMultipart= new MimeMultipart("mixed");
       msg.setFrom(new InternetAddress("\"" + MimeUtility.encodeText("测试邮箱") + "\" <kexiongmail@sina.com>"));
       msg.setReplyTo(new Address[]{new InternetAddress("kexiongmail@sohu.com")});
       msg.setRecipients(MimeMessage.RecipientType.TO,InternetAddress.parse(MimeUtility.encodeText("张可熊") + " <kexiongmail@sina.com>," + MimeUtility.encodeText("小熊熊") + " <kexiongmail@sohu.com>"));

       msg.setContent(msgMultipart);
       //复杂的内容为两个附件和一个html文件
       MimeBodyPart attach1=new MimeBodyPart();
       MimeBodyPart attach2=new MimeBodyPart();
       MimeBodyPart content=new MimeBodyPart();
       //将附件和内容放入到内容中去
       msgMultipart.addBodyPart(content);
       msgMultipart.addBodyPart(attach1);
       msgMultipart.addBodyPart(attach2);
       //分别设置内容的部分
       //设置附件1
       DataSource ds1=new FileDataSource("file\\a.txt");
       DataHandler dh1=new DataHandler(ds1);
       attach1.setDataHandler(dh1);
       attach1.setFileName(MimeUtility.encodeText("a.txt"));
       //设置附件2
       DataSource ds2=new FileDataSource("file\\b.jpg");
       DataHandler dh2=new DataHandler(ds2);
       attach2.setDataHandler(dh2);
       attach2.setFileName(MimeUtility.encodeText("b.jpg"));
       //设置文本的内容(复杂的格式)
       MimeMultipart  bodyMultipart=new MimeMultipart("related");
       content.setContent(bodyMultipart);
       MimeBodyPart htmlPart=new MimeBodyPart();
       MimeBodyPart gifPart=new MimeBodyPart();
       bodyMultipart.addBodyPart(htmlPart);
       bodyMultipart.addBodyPart(gifPart);
       htmlPart.setContent("你们的Java培训真的是最牛的吗？大家都这么说,我想跟你们比试一下！这可是我自己用程序生成和发送的邮件哦！<img src='http://www.itcast.cn/logo.gif'>"
               , "text/html;charset=gbk");

       DataSource gifds=new FileDataSource("file\\c.jpg");
       DataHandler gifHd=new DataHandler(gifds);
       gifPart.setDataHandler(gifHd);
       gifPart.setHeader("Content-Location", "http://www.itcast.cn/logo.jpg");


       msg.saveChanges();
       Transport.send(msg);
       OutputStream ips = new FileOutputStream("file\\demo3.eml");
       msg.writeTo(ips);
       ips.close();

   } 
   
   
   

}
