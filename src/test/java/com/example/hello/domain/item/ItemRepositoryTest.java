package com.example.hello.domain.item;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ItemRepositoryTest {

  ItemRepository itemRepository = new ItemRepository();

  @AfterEach
  void afterEach() {
    itemRepository.clearStore();
  }

  @Test
  void save() {
    //given
    Item item = new Item("itemA", 10000, 10);

    //when
    Item savedItem = itemRepository.save(item);

    //then
    Item foundItem = itemRepository.findById(item.getId());
    assertThat(foundItem).isEqualTo(savedItem);
  }

  @Test
  void findAll() {
    //given
    Item item1 = new Item("item1", 10000, 10);
    Item item2 = new Item("item2", 20000, 20);

    itemRepository.save(item1);
    itemRepository.save(item2);

    //when
    List<Item> foundItems = itemRepository.findAll();

    assertThat(foundItems).hasSize(2);
    assertThat(foundItems).contains(item1, item2);
  }

  @Test
  void updateItem() {
    //given
    Item item = new Item("itemA", 10000, 10);
    itemRepository.save(item);

    Item updateParam = new Item("itemB", 5000, 1);

    //when
    itemRepository.updateItem(item.getId(), updateParam);

    //then
    Item foundItem = itemRepository.findById(item.getId());
    assertThat(foundItem.getItemName()).isEqualTo("itemB");
    assertThat(foundItem.getPrice()).isEqualTo(5000);
    assertThat(foundItem.getQuantity()).isEqualTo(1);
  }

}