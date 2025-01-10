package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.exception.StudentNotFoundException;
import com.tpe.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller//requestler bu classta karşılanacak ve ilgili metodlarla maplenecek
@RequestMapping("/students")//http:localhost:8080/SpringMvc/students/....
//class:tüm metodlar için geçerli olur
//method:sadece ilgili metodu requestle mapler
public class StudentController {

    @Autowired
    private IStudentService service;

    //NOT:controllerda metodlar geriye mav veya String data tipi döndürebilir.

    //http:localhost:8080/SpringMvc/students/hi + GET--okuma
    //http:localhost:8080/SpringMvc/students/hi + POST--kayıt
    //@RequestMapping("/students")
    @GetMapping("/hi")
    public ModelAndView sayHi(){
        //response u hazırlayacak
        ModelAndView mav=new ModelAndView();
        mav.addObject("message","Hi,");
        mav.addObject("messagebody","I'm a Student Management System");
        mav.setViewName("hi");
        return mav;
    }

    //view resolver : /WEB-INF/views/hi.jsp dosyasını bulur ve mav içindeki modelı
    //dosyaya bind eder ve clienta gönderir.

    //1-tüm öğrencileri listeleme:
    //http://localhost:8080/SpringMvc/students + GET
    @GetMapping
    public ModelAndView getStudents(){
        List<Student> allStudent=service.listAllStudents();
        ModelAndView mav=new ModelAndView();
        mav.addObject("studentList",allStudent);
        mav.setViewName("students");
        return mav;
    }

    //2-öğrenciyi kaydetmek için form gösterme
    //request: http://localhost:8080/SpringMvc/students/new + GET
    //response:form göstermek
    @GetMapping("/new")
    public String sendForm(@ModelAttribute("student") Student student){
        return "studentForm";
    }
    //ModelAttribute anotasyonu view katmanı ile controller arasında
    //modelın transferini sağlar.

    //işlem sonunda: studentın firstname,lastname ve grade değerleri set edilmiş halde
    //controller classında yer alır

    //2-b:formun içindeki öğrenciyi kaydetme
    //request:http://localhost:8080/SpringMvc/students/saveStudent + POST
    //response:öğrenciyi tabloya ekleyeceğiz ve liste göndereceğiz
    @PostMapping("/saveStudent")
    public String addStudent(@Valid @ModelAttribute("student")Student student, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "studentForm";
        }

        service.addOrUpdateStudent(student);

        return "redirect:/students";//http://localhost:8080/SpringMvc/students + GET

    }

    //3-öğrenciyi güncelleme
    //request:http://localhost:8080/SpringMvc/students/update?id=3 + GET
    //response:update için id si verilen öğrencinin bilgileri ile formu gösterme
    //idsi verilen öğrenciyi bulmamız gerekir...
    @GetMapping("/update")
    public ModelAndView sendFormUpdate(@RequestParam("id") Long identity){

        Student foundStudent= service.findStudentById(identity);

        ModelAndView mav=new ModelAndView();
        mav.addObject("student",foundStudent);
        mav.setViewName("studentForm");
        return mav;
    }

    //kullanıcıdan bilgi nasıl alınır
    //1-form/body(JSON)
    //2-query param : /query?id=3
    //3-path param : /3
    //query param ve path param sadece 1 tane ise isim belirtmek opsiyonel


    //4-bir öğrenciyi silme
    //request : http://localhost:8080/SpringMvc/students/delete/4 + GET
    //response :öğrenci silinir ve kalan öğrenciler gösterilir
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long identity){

        service.deleteStudent(identity);

        return "redirect:/students";
    }

    //@ExceptionHandler:try-catch bloğunun mantığıyla benzer çalışır
    @ExceptionHandler(StudentNotFoundException.class)
    public ModelAndView handleException(Exception exception){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("notFound");
        return modelAndView;
    }












}
