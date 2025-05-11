// SaleApiController.java
package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.SaleService;
import com.trade.icesi_trade.dtos.SaleDto;
import com.trade.icesi_trade.mappers.SaleMapper;
import com.trade.icesi_trade.model.Sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin
public class SaleApiController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleMapper saleMapper;

    @GetMapping
    public ResponseEntity<List<SaleDto>> getAll() {
        List<SaleDto> sales = saleService.findAll().stream()
                .map(saleMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> getById(@PathVariable Long id) {
        Sale sale = saleService.findById(id);
        return ResponseEntity.ok(saleMapper.entityToDto(sale));
    }

    @PostMapping
    public ResponseEntity<SaleDto> create(@RequestBody SaleDto dto) {
        Sale saved = saleService.save(saleMapper.dtoToEntity(dto));
        return ResponseEntity.status(201).body(saleMapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDto> update(@PathVariable Long id, @RequestBody SaleDto dto) {
        Sale updated = saleService.update(id, saleMapper.dtoToEntity(dto));
        return ResponseEntity.ok(saleMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        saleService.delete(id);|
        return ResponseEntity.ok("Venta eliminado de favoritos.");
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<SaleDto>> getSalesByBuyer(@PathVariable Long buyerId) {
        List<Sale> sales = saleService.findAll().stream()
                .filter(sale -> sale.getBuyer() != null && sale.getBuyer().getId().equals(buyerId))
                .toList();
        List<SaleDto> saleDtos = sales.stream().map(saleMapper::entityToDto).toList();
        return ResponseEntity.ok(saleDtos);
    }
}
