package com.tpe.controller;

import com.tpe.AppConfiguration;
import com.tpe.domain.Message;
import com.tpe.repository.DbRepository;
import com.tpe.repository.Repository;
import com.tpe.service.MessageService;
import com.tpe.service.SlackService;
import com.tpe.service.SmsService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Random;

public class MessageApplicationWithSpring {
    public static void main(String[] args) {

        Message message=new Message();
        message.setBody("Welcome SPRING:)");

        //config classını okur ve componentscan ile componentları ve beanleri tarar ve her birinden
        //sadece 1 tane Spring bean oluşturur ve contexte atar ve hazır olarak bekletir.
        //bean istendiğinde gerekliyse içine bağımlılığını da enjekte ederek gönderir.
        AnnotationConfigApplicationContext context=
                new AnnotationConfigApplicationContext(AppConfiguration.class);

        //mesajı sms ile gönderelim:smsservicein objesi gerekli
        MessageService service1=context.getBean(SmsService.class);//newlemedik, rica ettik:)
        service1.sendMessage(message);

        MessageService service2=context.getBean("sms_service",MessageService.class);//smsService,slack_service
        service2.sendMessage(message);
        //getBean metoduna parametre olarak parentı verirsek ve birden fazla childı varsa
        //beanin ismini de belirtmeliyiz.


        //mesajı Slack ile gönderelim
        MessageService service3=context.getBean(SlackService.class);
        service3.sendMessage(message);


        //bağımlılık gerekirse
        MessageService service4=context.getBean(SmsService.class);
        service4.sendMessage(message);
        service4.saveMessage(message);
        //smsService i newlemedik
        //service repoya bağımlı ama biz enjekte etmedik
        //repo objesini de biz oluşturmadık
        //Spring SAĞOLSUN:)


        Repository repository =context.getBean(DbRepository.class);
        repository.save(message);

        //random bir değer üretelim ve yazdıralım
        //Random random=new Random();
        Random random=context.getBean(Random.class);
        System.out.println("random değer: "+random.nextInt(100));

        MessageService service5=context.getBean(SlackService.class);
        //service5.saveMessage(message);

        //context objemiz varsa beani getBean ile isteyip kullanabiliriz
        //diğer classlarda ise enjekte ederek aynı beani kullanabiliriz


        MessageService service6=context.getBean(SlackService.class);
        MessageService service7=context.getBean(SlackService.class);

        if (service6==service7){
            System.out.println("Aynı objeler");
            System.out.println(service6);
            System.out.println(service7);
        }else {
            System.out.println("Farklı objeler");
            System.out.println(service6);
            System.out.println(service7);
        }

        //prototype olan beani sonlandırmak için
         context.getBeanFactory().destroyBean(service6);


        SlackService service8=context.getBean(SlackService.class);
        service8.printContact();
        System.out.println("---------------------------");
        service8.getContact();



        context.close();

//        SmsService service=context.getBean(SmsService.class);
//        service.sendMessage(message);



    }
}
/*
Singleton Scope
Spring konteyneri, bir bean tanımı için yalnızca bir örnek (instance) oluşturur ve bu örneği tüm uygulama boyunca paylaşır.
Bean, Spring konteyneri başlatıldığında oluşturulur.
Uygulama kapanana kadar aynı örnek kullanılır.
Stateless (Durumsuz) nesneler için kullanılır.
Aynı davranışı ve veriyi paylaşması gereken hizmet sınıfları (örneğin, Service ve Repository sınıfları) için idealdir.
Tek bir örnek oluşturulduğu için bellek tüketimini azaltır.
----------------
Prototype Scope
Spring konteyneri, bir bean'e her erişimde yeni bir örnek (instance) oluşturur.
Bean, Spring konteyneri tarafından her çağrıldığında (örneğin, getBean() metodu ile) yeniden oluşturulur.
Kısa ömürlüdür ve Spring konteyneri yaşam döngüsünü yönetmez.
Stateful (Durum bilgisi taşıyan) nesneler için kullanılır.
Her istekte farklı veriyle çalışması gereken nesneler (örneğin, kullanıcı oturum bilgisi veya dosya işlemleri) için uygundur.
-------------
Singleton: Varsayılan olduğu için Spring genellikle bu scope'u kullanır ve çoğu servis veya repository sınıfı için yeterlidir.
Prototype: Kısa ömürlü nesneler için kullanışlıdır, ancak manuel olarak yönetilmesi gerekebilir. Özellikle Spring konteynerinin dışında kullanılan prototip nesnelerin yaşam döngüsüne dikkat edilmelidir.
 */