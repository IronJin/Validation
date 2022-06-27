package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * #Fielderror 관련 annotation 이다.
 *
 * @NotBlank 빈칸이면 안됨
 * @NotNull Null 값이면 안됨
 * @Range min 과 max 사이의 범위여야함
 * @Max max 값 까지여야함
 *
 * ------------------------------------------------------------
 * ObjectError 관련 annotaion이다.
 *
 * @ScriptAssert - 메세지코드 : ScriptAssert.item 또는 ScriptAssert ---> 실무에서는 거의 사용하지 않는다.
 * 따라서, ObjectError 의 경우 직접 자바코드로 작성하는것이 더 좋다.
 *
 *
 * 등등 이것들을 Bean Validation 애노테이션이라고 한다. 이것들을 사용하기 위해서는
 * 	implementation 'org.springframework.boot:spring-boot-starter-validation' 를 gradle 에 추가해주어야 한다.
 * 	추가로 사용할 메소드에서 @Validated 를 추가해주어야 한다.
 */
@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000원 넘게 입력해주세요")
public class Item {

    //@NotNull(groups = UpdateCheck.class)
    private Long id;

    //@NotBlank(groups = {SaveCheck.class, UpdateCheck.class} ,message = "상품이름을 입력해주세요.")
    private String itemName;

    //@NotNull(groups = {SaveCheck.class, UpdateCheck.class} ,message = "가격을 입력해주세요.")
    //@Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class}, message = "1000원에서 100000원 사이여야한다.")
    private Integer price;

    //@NotNull(groups = {SaveCheck.class, UpdateCheck.class}, message = "수량을 입력해주세요.")
    //@Max(value = 9999, groups = {SaveCheck.class}) //수정 요구사항 추가
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
