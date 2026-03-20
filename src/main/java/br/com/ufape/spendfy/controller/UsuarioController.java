package br.com.ufape.spendfy.controller;

import br.com.ufape.spendfy.dto.usuario.*;
import br.com.ufape.spendfy.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obterMeuPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(usuarioService.getPerfil(userDetails.getUsername()));
    }

    @PutMapping("/perfil")
    public ResponseEntity<Void> atualizarPerfil(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UsuarioRequest request) {
        usuarioService.atualizarPerfil(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/senha")
    public ResponseEntity<Void> alterarSenha(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SenhaRequest request) {
        usuarioService.alterarSenha(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}