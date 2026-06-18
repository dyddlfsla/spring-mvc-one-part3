package com.example.hello.web.basic;

import com.example.hello.domain.item.Item;
import com.example.hello.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {

    model.addAttribute("items", List.of(new Item("itemA", 10000, 10), new Item("itemB", 20000, 5)));

    return "basic/items";
  }

  @PostConstruct
  public void init() {

    itemRepository.save(new Item("itemA", 10000, 10));
    itemRepository.save(new Item("itemB", 20000, 10));
  }
}
