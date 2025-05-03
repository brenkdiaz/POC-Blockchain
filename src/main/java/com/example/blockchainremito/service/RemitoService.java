package com.example.blockchainremito.service;

import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.repository.RemitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RemitoService {
    @Autowired
    private RemitoRepository remitoRepository;

    @Autowired
    private BlockchainService blockchainService;

    public Remito guardarRemito(Remito remito) {
        String datosAFirmar = generarDatosParaHash(remito);
        String hash = blockchainService.calcularHash(datosAFirmar);
        remito.setHash(hash);
        return remitoRepository.save(remito);
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
