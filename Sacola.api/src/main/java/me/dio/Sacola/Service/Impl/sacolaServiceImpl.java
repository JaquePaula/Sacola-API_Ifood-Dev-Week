package me.dio.Sacola.Service.Impl;

import lombok.RequiredArgsConstructor;
import me.dio.Sacola.Repository.ItemRepository;
import me.dio.Sacola.Repository.produtoRepository;
import me.dio.Sacola.enumeration.FormaPagamento;
import me.dio.Sacola.model.Item;
import me.dio.Sacola.model.Restaurante;
import me.dio.Sacola.model.Sacola;
import me.dio.Sacola.Repository.sacolaRepository;
import me.dio.Sacola.Resource.dto.ItemDto;
import me.dio.Sacola.Service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class sacolaServiceImpl implements SacolaService {
    private final sacolaRepository sacolaRepository;
    private final produtoRepository produtoRepository;
    private final ItemRepository itemRepository;


    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getIdSacola());

        if (sacola.isFechada()) {
            throw new RuntimeException("Esta sacola está fechada.");
        }

        Item itemParaSerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItens();
        if (itensDaSacola.isEmpty()) {
            itensDaSacola.add(itemParaSerInserido);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante RestauranteDoItemParaAdicionar = itemParaSerInserido.getProduto().getRestaurante();
            if (restauranteAtual.equals(RestauranteDoItemParaAdicionar)) {
                itensDaSacola.add(itemParaSerInserido);
            } else {
                throw new RuntimeException("Não é possivel adicionar produtos de restaurantes diferentes. Fecha a sacola ou esvazie.");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();

        for(Item itemDaSacola: itensDaSacola) {
            double valorTotalItem =
            itemDaSacola.getProduto().getValorUnitario() * itemDaSacola.getQuantidade();
        valorDosItens.add(valorTotalItem);
        }

        double valorTotalSacola = valorDosItens.stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return itemParaSerInserido;

    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe");
                }
        ) ;
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);
        if (sacola.getItens().isEmpty()) {
            throw new RuntimeException("Inclua Itens na sacola");
        }
        FormaPagamento formaPagamento =
                numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;
        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
}

