//package demo.ddd;
//
//import demo.ddd.model.Book;
//import demo.ddd.service.IBookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/book")
//public class BookController {
//    @Autowired
//    private IBookService ibookService;
//
//    @PostMapping("/add")
//    public ResponseEntity<Book> add(@RequestBody Book book) {
//        HttpStatus httpStatus = HttpStatus.OK;
//        Book result = null;
//        try {
//            Optional<Book> optionalBook = ibookService.add(book, true);
//            if (optionalBook != null) {
//                result = optionalBook.get();
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(result, httpStatus);
//    }
//
//    @GetMapping("/getbyid/{id}")
//    public ResponseEntity<Book> getById(@PathVariable String id) {
//        HttpStatus httpStatus = HttpStatus.OK;
//        Book result = null;
//        try {
//            Optional<Book> optionalBook = ibookService.getById(id, true);
//            if (optionalBook != null) {
//                result = optionalBook.get();
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<>(result, httpStatus);
//    }
//}
