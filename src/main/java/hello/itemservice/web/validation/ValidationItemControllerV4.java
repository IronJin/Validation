package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }


    /**
     * @Validated 에서 supports 메소드가 사용되서 true 이면 호출이 되면서 검증기가 실행이 됨.
     * 그 후에 BindingResult 에 담긴다.
     *
     * @ModelAttribute 는 자동으로 뷰단으로 모델객체에 담겨 넘어가게 된다.
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /**
         * ObjectError 의 경우 직접 자바코드로 작성하는것이 ScriptAssert 를 사용하는 것보다 권장되는 방법이다.
         * 하지만, FieldError 는 Bean Validation 을 활용하는것이 더 좋은 방법이다.
         */
        if(form.getPrice() != null && form.getQuantity() != null){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice},null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}",bindingResult);
            return "validation/v4/addForm";
        }

        //여기서부터는 성공 로직이 될 것임

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    //@ModelAttribute 옆에 "item" 을 넣어주는 이유는 ItemUpdateForm 이 Model 에 담길때 Model.attribute("itemUpdateForm", form) 으로 넘어가기 때문이다.
    //그러나 현재 editForm.html 에는 "item" 으로 form 을 받고 있기 때문에 @ModelAttribute 옆에 "item" 을 해준것이다.
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        //복합룰 검증
        if(form.getPrice() != null && form.getQuantity() != null){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice},null);
            }
        }

        if(bindingResult.hasErrors()){
            return "validation/v4/editForm";
        }

        Item itemParam = new Item();
        itemParam.setQuantity(form.getQuantity());
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());

        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

