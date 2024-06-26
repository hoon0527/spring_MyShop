package portfolio1.Drink.Controller.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import portfolio1.Drink.DTO.Shopping.BasketDTO;
import portfolio1.Drink.DTO.Shopping.DeliveryDTO;
import portfolio1.Drink.DTO.Shopping.OrderDTO;
import portfolio1.Drink.DTO.Shopping.PaymentDTO;
import portfolio1.Drink.Entity.Items.ItemsEntity;
import portfolio1.Drink.Service.ItemsService;
import portfolio1.Drink.Service.ShoppingService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/User")
@RequiredArgsConstructor
public class ShoppingController
{
    private final ItemsService itemsService;
    private final ShoppingService shoppingService;

    @PostMapping("/SetLabel")
    @ResponseBody
    public List<String> Category_Setting(@RequestParam("code")String code)
    {
        return itemsService.SetLabel(code);
    }

    @PostMapping("/Basket_proc")
    public String Basket_proc(BasketDTO basketDTO, Principal principal)
    {
        shoppingService.InputBasket(basketDTO, principal);
        return "redirect:/main/ItemView?idx="+basketDTO.getItem_idx();
    }

    @GetMapping("/Basket")
    public String Basket(Principal principal, Model model)
    {
        model.addAttribute("list",shoppingService.UserBasket(principal));
        return "User/Items/Basket";
    }

    @PostMapping("/Payment")
    public String Payment(PaymentDTO paymentDTO, Model model, Principal principal)
    {
        model.addAttribute("list",shoppingService.Payment(paymentDTO));
        model.addAttribute("user",shoppingService.Users(principal));
        return "User/Items/Payment";
    }

    @PostMapping("/PayResult")
    public String OrderAndDelivery(OrderDTO orderDTO, DeliveryDTO deliveryDTO, Principal principal)
    {
        shoppingService.PayResult(orderDTO, deliveryDTO, principal);
        return "redirect:/User/Order";
    }

    @GetMapping("/MyPage")
    public String MyPage()
    {
        return "User/Items/MyPage";
    }

    @GetMapping("/Order")
    public String OrderCheck(Model model, Principal principal,
                             @RequestParam(name="oder_start", defaultValue="none") String order_start, @RequestParam(name="order_end", defaultValue="none") String order_end,
                             @RequestParam(name="delivery_start", defaultValue="none")String delivery_start, @RequestParam(name="delivery_end", defaultValue="none")String delivery_end)
    {
        model.addAttribute("orders",shoppingService.MyOrders(principal, order_start, order_end));
        model.addAttribute("deliverys",shoppingService.MyDelivery(principal, delivery_start, delivery_end));
        return "User/Items/OrderCheck";
    }

    @GetMapping("/test")
    public String test()
    {
        return "User/Items/test";
    }
}
