package com.biblioteca.svc_miembros.controller;

import com.biblioteca.svc_miembros.dto.MemberRequest;
import com.biblioteca.svc_miembros.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/{id}")
    public Map<String, Object> getMember(@PathVariable String id) {
        return memberService.getMemberById(id);
    }

    @PostMapping
    public Map<String, Object> createMember(@Valid @RequestBody MemberRequest dto) {
        return memberService.createMember(dto);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("servicio", "svc-miembros", "estado", "OK");
    }
}
