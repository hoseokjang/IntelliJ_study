package jpastudy.jpashop.web;

import jpastudy.jpashop.domain.item.Book;
import jpastudy.jpashop.domain.item.Item;
import jpastudy.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createItem(Model model)
    {
        model.addAttribute("bookForm", new BookForm());
        return "/items/createItemForm";
    }
    @PostMapping(value = "/items/new")
    public String create(@Valid BookForm form, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "items/createItemForm";
        }
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }
    @GetMapping("/items")
    public String list(Model model)
    {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }
    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model)
    {
        Book book = (Book) itemService.findOne(itemId);
        BookForm bookForm = new BookForm();
        bookForm.setId(book.getId());
        bookForm.setName(book.getName());
        bookForm.setPrice(book.getPrice());
        bookForm.setStockQuantity(book.getStockQuantity());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setIsbn(book.getIsbn());
        model.addAttribute("bookForm", bookForm);
        return "items/updateItemForm";
    }
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("bookForm") BookForm bookForm) {
        // 1. Book에 입력항목을 모두 넣고 저장해서 merge하는 방식
        /* Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);*/

        // 2. Service에서 만든 updateItem을 호출해서 부분 수정 - Dirty Checking
        itemService.updateItem(bookForm.getId(), bookForm.getName(), bookForm.getPrice());
        return "redirect:/items";
    }
}
