package com.alura.forohub.controller;

import com.alura.forohub.domain.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    
    @Autowired
    private TopicoRepository topicoRepository;
    
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriComponentsBuilder) {
        
        // Validar que no exista un tópico con el mismo título y mensaje
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().build();
        }
        
        Topico topico = new Topico(datos);
        topicoRepository.save(topico);
        
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaTopico(topico));
    }
    
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10) Pageable paginacion) {
        
        var page = topicoRepository.findAllOrderByFechaCreacionAsc(paginacion)
                .map(DatosListadoTopico::new);
        return ResponseEntity.ok(page);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detallarTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id);
        
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DatosRespuestaTopico(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos) {
        
        var topicoOptional = topicoRepository.findById(id);
        
        if (topicoOptional.isPresent()) {
            var topico = topicoOptional.get();
            
            // Validar duplicados solo si se están cambiando título o mensaje
            if ((datos.titulo() != null && !datos.titulo().equals(topico.getTitulo())) ||
                (datos.mensaje() != null && !datos.mensaje().equals(topico.getMensaje()))) {
                
                String nuevoTitulo = datos.titulo() != null ? datos.titulo() : topico.getTitulo();
                String nuevoMensaje = datos.mensaje() != null ? datos.mensaje() : topico.getMensaje();
                
                if (topicoRepository.existsByTituloAndMensaje(nuevoTitulo, nuevoMensaje)) {
                    return ResponseEntity.badRequest().build();
                }
            }
            
            topico.actualizarDatos(datos);
            return ResponseEntity.ok(new DatosRespuestaTopico(topico));
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);
        
        if (topicoOptional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}
