package org.example.appmongodb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping({"/", "/index.html"})
    public String inicio() {
        return "index";
    }

    @GetMapping("/pagina/produtos")
    public String produtos() {
        return "js/html/produtos";
    }

    @GetMapping("/pagina/clientes")
    public String clientes() {
        return "js/html/clientes";
    }

    @GetMapping("/pagina/pedidos")
    public String pedidos() {
        return "js/html/pedidos";
    }

    @GetMapping("/pagina/carrinhos")
    public String carrinhos() {
        return "js/html/carrinhos";
    }
}
