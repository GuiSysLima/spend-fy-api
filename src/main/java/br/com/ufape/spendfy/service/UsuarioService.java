package br.com.ufape.spendfy.service;

import br.com.ufape.spendfy.dto.usuario.*;
import br.com.ufape.spendfy.entity.Usuario;
import br.com.ufape.spendfy.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse getPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getFotoUrl());
    }

    @Transactional
    public void atualizarPerfil(String emailOriginal, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailOriginal)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(request.email()) && usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outra conta");
        }

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setFotoUrl(request.fotoUrl());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(String email, SenhaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
    }
}