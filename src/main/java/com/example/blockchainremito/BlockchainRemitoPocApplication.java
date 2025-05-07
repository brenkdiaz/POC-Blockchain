package com.example.blockchainremito;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.service.RemitoService;

@SpringBootApplication
@EnableAsync
public class BlockchainRemitoPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainRemitoPocApplication.class, args);
    }

    /* 
    @Bean
    CommandLineRunner init(RemitoService remitoService) {
        return args -> {
            Remito remito = Remito.builder()
                .fecha(LocalDate.now())
                .empresaSolicitante("Empresa Solicitante")
                .empresaProveedora("Empresa Proveedora")
                .descripcionTrabajo("Descripción del trabajo")
                .tecnicoAsignado("Técnico Asignado")
                .build();


            CompletableFuture<Remito> future = remitoService.guardarRemito(remito);
            System.out.println("Remito guardado en la blockchain: " + future.get()); // Esto hará que el hilo principal espere a que se complete el proceso asíncrono       
        };
    }
    */
}
