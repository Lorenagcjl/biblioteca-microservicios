package com.biblioteca.svc_miembros.service;

import com.biblioteca.svc_miembros.dto.MemberRequest;
import com.biblioteca.svc_miembros.model.Member;
import com.biblioteca.svc_miembros.repository.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> createMember(MemberRequest dto) {
        Member member = new Member();
        member.setId("MEM-" + UUID.randomUUID().toString());
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setMemberType(dto.getMemberType());
        member.setRegistrationDate(new java.util.Date().toString());
        member.setActiveLoans(0);

        memberRepository.save(member);
        return Map.of("mensaje", "Miembro registrado exitosamente", "id", member.getId());
    }

    public Map<String, Object> getMemberById(String id) {
        String cacheKey = "member:" + id;
        String cached = redis.opsForValue().get(cacheKey);

        if (cached != null) {
            try {
                Map<String, Object> memberCache = objectMapper.readValue(
                        cached, new TypeReference<Map<String, Object>>() {});
                return Map.of("fuente", "CACHE REDIS (2ms)", "datos", memberCache);
            } catch (Exception e) {
                redis.delete(cacheKey);
            }
        }

        Optional<Member> memberOpt = memberRepository.findById(id);

        if (memberOpt.isEmpty()) {
            return Map.of("error", "Miembro no encontrado: " + id);
        }

        Member member = memberOpt.get();

        try {
            String jsonMember = objectMapper.writeValueAsString(member);
            redis.opsForValue().set(cacheKey, jsonMember, 120, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Error en caché: " + e.getMessage());
        }

        return Map.of("fuente", "BASE DE DATOS (~80ms)", "datos", member);
    }
}
