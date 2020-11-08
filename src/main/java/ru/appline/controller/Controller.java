package ru.appline.controller;

import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private static final PetModel petModel = PetModel.getInstance();
    private static final AtomicInteger newId = new AtomicInteger(1);

    @PostMapping(value = "/createPet", consumes = "application/json", produces = "application/json")
    public String createPet(@RequestBody Pet pet){
        int id = newId.getAndIncrement();
        petModel.add(pet, id);
        if (id == 1) {
            return "{\n\"Answer\": \"Поздравляем, вы создали вашего первого домашнего питомца!\"\n}";
        } else {
            return "{\n\"Answer\": \"Поздравляем, вы создали домашнего питомца!\"\n}";
        }
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Map<Integer, Pet> getAll() {
        return petModel.getAll();
    }

    @GetMapping(value = "/getPet", consumes = "application/json", produces = "application/json")
    public Pet getPet(@RequestBody Map<String, Integer> id) {
        return petModel.getFromList(id.get("id"));
    }

    //Удаляет питомца с указанным id, если такой существует
    @DeleteMapping(value = "/deletePet", consumes = "application/json")
    public void deletePet(@RequestBody Map<String, Integer> id) {
        petModel.deletePet(id.get("id"));
    }

    //Изменяет питомца с заданным id, если такой существует. Не создаёт нового питомца при отсутствии записи с нужным id.
    @PutMapping(value = "/updatePet", consumes = "application/json")
    public void updatePet(@RequestBody Map<String, String> id) {
        petModel.updatePet(Integer.parseInt(id.get("id")), new Pet(id.get("name"), id.get("type"), Integer.parseInt(id.get("age"))));
    }

}
