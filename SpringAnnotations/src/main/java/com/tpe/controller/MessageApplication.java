package com.tpe.controller;

import com.tpe.repository.Repository;
import com.tpe.domain.Message;
import com.tpe.repository.FileRepository;
import com.tpe.service.MailService;
import com.tpe.service.MessageService;
import com.tpe.service.WhatsappService;

public class MessageApplication {
    public static void main(String[] args) {

        Message message=new Message();
        message.setBody("Spring is COMING:)");


        //bu mesajı maille gönderelim.
       // MailService mailService=new MailService();
       // mailService.sendMessage(message);

        //bu mesajı whatsapp ile gönderelim
//        WhatsappService whatsappService=new WhatsappService();
//        whatsappService.sendMessage(message);

        //ref interfaceden alalım
        Repository repository=new FileRepository();//bağımlılık enjeksiyonu_dependency injection
        MessageService service=new WhatsappService(repository);
        service.sendMessage(message);


        service=new MailService(repository);
        service.sendMessage(message);

        //gönderilen mesajı kaydedelim
        service.saveMessage(message);


        MessageService service2=new WhatsappService(repository);
        service2.sendMessage(message);
        service2.saveMessage(message);



     //objeler arasındaki bağımlılığı gevşetmek için
     //   1-ref olarak interface
     //   2-bağımlılığı doğrudan vermek yerine daha
     //   sonra parametreli constructor(veya setter metodu) ile verdik


     //problem:
        //1-objeleri biz oluşturuyoruz
        //2-objelerin bağımlılıklarını biz manuel olarak enjekte etmek zorundayız

     //çözüm:
        //spring is COMING:)




    }
}
