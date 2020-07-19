package one.digitalinnovation.RestIntermediario.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.digitalinnovation.RestIntermediario.controller.request.SoldadoEditRequest;
import one.digitalinnovation.RestIntermediario.controller.response.SoldadoListResponse;
import one.digitalinnovation.RestIntermediario.controller.response.SoldadoResponse;
import one.digitalinnovation.RestIntermediario.dto.Soldado;
import one.digitalinnovation.RestIntermediario.entity.SoldadoEntity;
import one.digitalinnovation.RestIntermediario.repository.SoldadoRepository;
import one.digitalinnovation.RestIntermediario.resource.ResourceSoldado;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoldadoService {

    private SoldadoRepository soldadoRepository;
    private ObjectMapper objectMapper;
    private ResourceSoldado resourceSoldado;


    public SoldadoService(SoldadoRepository soldadoRepository, ObjectMapper objectMapper, ResourceSoldado resourceSoldado) {
        this.soldadoRepository = soldadoRepository;
        this.objectMapper = objectMapper;
        this.resourceSoldado = resourceSoldado;
    }

    public SoldadoResponse buscarSoldado(Long id) {
        SoldadoEntity soldado = soldadoRepository.findById(id).orElseThrow();
        SoldadoResponse soldadoResponse = resourceSoldado.criarLinkDetalhe(soldado);
        return soldadoResponse;
    }

    public void criarSoldado(Soldado soldado){
        SoldadoEntity soldadoEntity = objectMapper.convertValue(soldado, SoldadoEntity.class);
        soldadoRepository.save(soldadoEntity);
    }

    public void alterarSoldado(Long id, SoldadoEditRequest soldadoEditRequest) {
        SoldadoEntity soldadoEntity = objectMapper.convertValue(soldadoEditRequest, SoldadoEntity.class);
        soldadoEntity.setId(id);
        soldadoRepository.save(soldadoEntity);
    }

    public void deletarSoldado(Long id) {
        SoldadoEntity soldado = soldadoRepository.findById(id).orElseThrow();
        soldadoRepository.delete(soldado);
    }

    public CollectionModel<SoldadoListResponse> buscarSoldados(){
        List<SoldadoEntity> all = soldadoRepository.findAll();
        List<SoldadoListResponse> soldadoStream = all.stream()
                .map(it -> resourceSoldado.criarLink(it))
                .collect(Collectors.toList());
        return new CollectionModel<>(soldadoStream);
    }
}
