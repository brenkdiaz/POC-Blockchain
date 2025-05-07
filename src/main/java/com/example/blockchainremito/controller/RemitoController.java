package com.example.blockchainremito.controller;

import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.service.RemitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/remitos")
public class RemitoController {

    @Autowired
    private RemitoService remitoService;


    @Async
    @PostMapping
    public CompletableFuture<ResponseEntity<Remito>> crearRemito(@RequestBody Remito remito) throws Exception {
        return remitoService.guardarRemito(remito).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}/verificar")
    public ResponseEntity<String> verificarHash(@PathVariable Long id) {
        boolean valido = remitoService.verificarIntegridad(id);
        return valido
                ? ResponseEntity.ok("El hash es válido. El remito no fue modificado.")
                : ResponseEntity.status(HttpStatus.CONFLICT)
                .body("¡Atención! El hash no coincide o el remito no existe.");
    }

    @GetMapping
    public ResponseEntity<List<Remito>> listarRemitos() {
        List<Remito> remitos = remitoService.obtenerTodos();
        return ResponseEntity.ok(remitos);
    }

}
