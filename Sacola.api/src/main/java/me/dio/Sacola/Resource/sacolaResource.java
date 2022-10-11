package me.dio.Sacola.Resource;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.dio.Sacola.model.Item;
import me.dio.Sacola.model.Sacola;
import me.dio.Sacola.Resource.dto.ItemDto;
import me.dio.Sacola.Service.SacolaService;
import org.springframework.web.bind.annotation.*;

@Api(value="/ifood-devweek/sacolas")
@RestController
@RequestMapping ("/ifood-devweek/Sacolas")
@RequiredArgsConstructor

public class sacolaResource {
 private final SacolaService sacolaService;

 @PostMapping
 public Item incluirItemNaSacola(@RequestBody ItemDto itemDto) {
  return sacolaService.incluirItemNaSacola(itemDto);

 }

 @GetMapping("/{id}")
 public Sacola verSacola(@PathVariable("id") Long id) {return sacolaService.verSacola(id);}

 @PatchMapping("/fecharSacola/{idSacola}")
 public Sacola fecharSacola (@PathVariable("idSacola") Long idSacola,
                             @RequestParam ("formaPagamento") int formaPagamento) {
  return sacolaService.fecharSacola(idSacola, formaPagamento);
 }
}
