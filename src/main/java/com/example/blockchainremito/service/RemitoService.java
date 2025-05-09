package com.example.blockchainremito.service;

import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.repository.RemitoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RemitoService {
    @Autowired
    private RemitoRepository remitoRepository;

    @Autowired
    private BlockchainService blockchainService;


    @Autowired
    private GoogleWeb3Service googleWeb3Service;

    @Value("${private.key.alan}")
    private String privateKey;

    @Async
    public CompletableFuture<Remito> guardarRemito(Remito remito) throws Exception {
        String datosAFirmar = generarDatosParaHash(remito);
        String hash = blockchainService.calcularHash(datosAFirmar);
        remito.setHash(hash);

        
        //Publicar el hash en la blockchain
        String txHash = googleWeb3Service.publishHashToBlockchain(privateKey, hash);
        remito.setTxHash(txHash); //Establezo el txHash
            
        // Ahora todo el resto en forma asíncrona
        return googleWeb3Service.getTxMetadata(txHash)
        .thenCompose(txMetadata -> {
            remito.setBlockNumber(((BigInteger) txMetadata.get("blockNumber")).intValue());
            remito.setTimestamp((Instant) txMetadata.get("timestamp"));


            System.out.println("REMITO GUARDADO EN LA BLOCKCHAIN!!!!! : " + remito.getTxHash());
            return CompletableFuture.supplyAsync(() -> remitoRepository.save(remito));
        });
    }   

    public boolean verificarIntegridad(Long id) {
        Optional<Remito> remitoOpt = remitoRepository.findById(id);
        if (remitoOpt.isEmpty()) return false;

        Remito remito = remitoOpt.get();
        String hashCalculado = blockchainService.calcularHash(remito.toString());

        return hashCalculado.equals(remito.getHash());
    }

    public List<Remito> obtenerTodos() {
        return remitoRepository.findAll();
    }

    private String generarDatosParaHash(Remito r) {
        return r.getFecha() + r.getEmpresaProveedora() + r.getDescripcionTrabajo() + r.getDuracionEstimada() + r.getEmpresaSolicitante();
    }
}
