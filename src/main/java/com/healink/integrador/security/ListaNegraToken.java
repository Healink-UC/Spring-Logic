package com.healink.integrador.security;

import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ListaNegraToken {
    private final Set<String> tokensRevocados = ConcurrentHashMap.newKeySet();

    public void agregarAListaNegra(String token) {
        tokensRevocados.add(token);
    }

    public boolean estaEnListaNegra(String token) {
        return tokensRevocados.contains(token);
    }
}
